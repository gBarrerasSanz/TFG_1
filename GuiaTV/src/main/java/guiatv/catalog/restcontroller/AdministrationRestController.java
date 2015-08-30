package guiatv.catalog.restcontroller;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.RtSchedule.InstantState;
import guiatv.persistence.domain.helper.ArffHelper;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.service.BlobService;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.rtmpspying.MonitorMyCh;
import guiatv.schedule.publisher.SchedulePublisher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import weka.classifiers.bayes.NaiveBayesUpdateable;

import com.fasterxml.jackson.annotation.JsonView;


@RestController
@RequestMapping("/admin/channels")
public class AdministrationRestController {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ChannelService chServ;
	@Autowired
	SchedulePublisher schedPublisher;
	@Autowired
	ScheduleService schedServ;
	@Autowired
	BlobService blobServ;
	@Autowired
	MonitorMyCh monitorMyCh;
	
	@RequestMapping(
			value = "/activation/", 
			method = RequestMethod.POST)
	public ResponseEntity<Boolean> switchChannelActivationState(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="activation", defaultValue="", required=true) boolean activation)
	{
		ResponseEntity<Boolean> okResp = new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
		ResponseEntity<Boolean> errResp = new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, false);
		if (ch != null) {
			if (activation) {
				monitorMyCh.getByChannel(ch).getMyChState().activate();
				logger.debug("Channel ["+ch.getIdChBusiness()+"] is now ACTIVE");
			}
			else {
				monitorMyCh.getByChannel(ch).getMyChState().deactivate();
				logger.debug("Channel ["+ch.getIdChBusiness()+"] is now OFF");
			}
			return okResp;
		}
		else {
			return errResp;
		}
	}
	
	@RequestMapping(
			value = "/spying/", 
			method = RequestMethod.POST)
	public ResponseEntity<Boolean> switchChannelSpyingState(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="spying", defaultValue="", required=true) boolean spying)
	{
		ResponseEntity<Boolean> okResp = new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
		ResponseEntity<Boolean> errResp = new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, false);
		if (ch != null) {
			if (spying) {
				boolean queryResult = monitorMyCh.getByChannel(ch).getMyChState().requestSpying();
				if (queryResult) {
					logger.debug("Channel ["+ch.getIdChBusiness()+"] is now BEING SPIED");
					return okResp;
				}
				else {
					logger.debug("Channel ["+ch.getIdChBusiness()+
							"] CANNOT BE SPIED at this moment or it's already spied");
					return errResp;
				}
			}
			else {
				monitorMyCh.getByChannel(ch).getMyChState().releaseSpying();
				logger.debug("Channel ["+ch.getIdChBusiness()+"] has been queried for STOP BEING SPIED");
				return okResp;
			}
			
		}
		else {
			return errResp;
		}
	}
	
	@RequestMapping(value = "/crossvalidation/", method = RequestMethod.GET)
	public ResponseEntity<String> doCrossValidation(
			@RequestParam(value = "hashIdChBusiness", defaultValue = "", required = true) String hashIdChBusiness,
			@RequestParam(value = "alreadyTrained", defaultValue = "", required = true) boolean alreadyTrained) {
//		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, false);
		
		MyCh myCh = monitorMyCh.getByHashIdChBusiness(hashIdChBusiness);
		if (myCh == null) {
			return new ResponseEntity<String>("MyCh NOT FOUND",
					HttpStatus.NOT_FOUND);
		}
		boolean loadedClassifier = ( myCh.getTrainedModel().getTrainedClassifier() != null);
		if (!loadedClassifier) {
			return new ResponseEntity<String>("CLASSIFIER NOT FOUND",
					HttpStatus.NOT_FOUND);
		}
		// ATENCI�N: SE ASUME QUE fullDataSet est� ya cargado
//		myCh.getTrainedModel().loadOrCreateFullDataSet(blob) // Cargar TODOS los datos (atributos + muestras)
		String cvResults;
		/*
		 * Si se quiere evaluar C-V con el clasificador ya entrenado (Habiendo
		 * espiado ya los datos de test)
		 */
		if (alreadyTrained) {
			// Utilizar el clasificador ya entrenado con todas las muestras
			cvResults = ArffHelper.doCrossValidation(
					myCh.getTrainedModel().getTrainedClassifier(), 
					myCh.getTrainedModel().getFullDataSet());
		}
		/*
		 * Si se quiere evaluar C-V estricto (Sin Haber espiado los datos de
		 * test)
		 */
		else {
			// Crear nuevo clasificador
			NaiveBayesUpdateable newClassifier = new NaiveBayesUpdateable();
			cvResults = ArffHelper.doCrossValidation(newClassifier,
					myCh.getTrainedModel().getFullDataSet());
		}
		return new ResponseEntity<String>(cvResults, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/publishSchedule/", 
			method = RequestMethod.GET)
	public ResponseEntity<Boolean> publishSchedulesByHashIdChBusiness(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="realtime", defaultValue="", required=true) boolean realtime)
	{
		ResponseEntity<Boolean> okResp = new ResponseEntity<Boolean>(
				true, HttpStatus.OK);
		ResponseEntity<Boolean> errResp = new ResponseEntity<Boolean>(
				false, HttpStatus.BAD_REQUEST);
		if (hashIdChBusiness.length()>0) {
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			if (realtime) { // RtSchedule
				MyCh myCh = new MyCh();
				myCh.setChannel(ch);
				RtSchedule rtSched = new RtSchedule();
				rtSched.setMyCh(myCh);
				rtSched.setInstant(new Date());
				rtSched.setState(InstantState.ON_PROGRAMME);
				/********************************************
				 * PUBLICAR RTSCHEDULE
				 *******************************************/
				Message<RtSchedule> rtSchedMsg = MessageBuilder.withPayload(rtSched).build();
				schedPublisher.publishRtSchedule(rtSchedMsg);
				return okResp;
			}
			else { // Schedule
				Schedule sched = schedServ.findOneByChannelOrderByStartAsc(ch, true);
				if (sched != null) {
					List<Schedule> lSched = new ArrayList<Schedule>();
					lSched.add(sched);
					Message<List<Schedule>> lSchedMsg = MessageBuilder.withPayload(lSched).build();
					schedPublisher.publishListSchedules(lSchedMsg);
					return okResp;
				}
				else {
					return errResp;
				}
			}
		}
		else {
			return errResp;
		}
	}
	
	@RequestMapping(
			value = "/learnNewClassifiedSample/", 
			method = RequestMethod.POST)
	public ResponseEntity<Boolean> learnNewClassifiedSample(
			@RequestParam(value="idBlobPersistence", defaultValue="", required=true) long idBlobPersistence,
			@RequestParam(value="classificationResult", defaultValue="", required=true) boolean classificationResult)
	{
		Blob blob = blobServ.findOneByIdBlobPersistence(idBlobPersistence);
		MyCh mlCh = blob.getMyCh();
		// A�adir muestra
		mlCh.getTrainedModel().learnSample(blob, classificationResult);
		// Eliminar blob de la Base de Datos
		blobServ.delete(blob);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
