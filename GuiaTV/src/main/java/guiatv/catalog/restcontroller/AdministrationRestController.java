package guiatv.catalog.restcontroller;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.RtSchedule.InstantState;
import guiatv.persistence.domain.helper.ArffHelper;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.service.BlobService;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;
import guiatv.realtime.rtmpspying.MonitorMyCh;
import guiatv.realtime.rtmpspying.RtmpSpyingService;
import guiatv.schedule.publisher.SchedulePublisher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;

import com.fasterxml.jackson.annotation.JsonView;


@RestController
@RequestMapping("/admin/channels")
public class AdministrationRestController {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ProgrammeService progServ;
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
	@Autowired
	RtmpSpyingService rtmpSpyingServ;
	
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
				MyCh myCh = monitorMyCh.getByChannel(ch);
				boolean queryResult = myCh.getMyChState().requestSpying();
				if (queryResult) {
					rtmpSpyingServ.doSpying(myCh);
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
		
		if ( ! myCh.getTrainedModel().isAbleToCV()) {
			return new ResponseEntity<String>("ERROR: NOT ENOUGH SAMPLES",
					HttpStatus.BAD_REQUEST);
		}
		// ATENCIÓN: SE ASUME QUE fullDataSet está ya cargado
//		myCh.getTrainedModel().loadOrCreateFullDataSet(blob) // Cargar TODOS los datos (atributos + muestras)
		Evaluation cvEvaluation;
		/*
		 * Si se quiere evaluar C-V con el clasificador ya entrenado (Habiendo
		 * espiado ya los datos de test)
		 */
		if (alreadyTrained) {
			// Utilizar el clasificador ya entrenado con todas las muestras
			cvEvaluation = ArffHelper.doCrossValidation(
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
			cvEvaluation = ArffHelper.doCrossValidation(newClassifier,
					myCh.getTrainedModel().getFullDataSet());
		}
//		 Convertir los saltos de línea Java a los de HTML
		//cvResults = cvResults.replaceAll("(\r\n|\n)", "<br />");
//		cvResults = StringEscapeUtils.
		return new ResponseEntity<String>(cvEvaluation.toSummaryString(), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/publishSchedule/", 
			method = RequestMethod.GET)
	public ResponseEntity<String> publishSchedulesByHashIdChBusiness(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="realtime", defaultValue="", required=true) boolean realtime)
	{
		String returnMsg = null;
		String returnErrMsg = "Some error ocurred while publishing Schedule";
		if (hashIdChBusiness.length()>0) {
			Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
			MyCh myCh = monitorMyCh.getByChannel(ch);
			myCh.setChannel(ch);
			if ( ! myCh.getMyChState().isActive()) {
				returnMsg = "ERROR: Cannot publish Schedule nor RtSchedule in a NOT ACTIVE Channel";
				return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
			}
			if (realtime) { // RtSchedule
				RtSchedule rtSched = new RtSchedule();
				rtSched.setMyCh(myCh);
				rtSched.setInstant(new Date());
				rtSched.setState(InstantState.ON_PROGRAMME);
				// Determinar a qué programa afecta
				Programme prog = progServ.findOneByChannelAndInstant(rtSched.getMyCh().getChannel(), 
						rtSched.getInstant());
				rtSched.setProgramme(prog);
				/********************************************
				 * PUBLICAR RTSCHEDULE
				 *******************************************/
				Message<RtSchedule> rtSchedMsg = MessageBuilder.withPayload(rtSched).build();
				schedPublisher.publishRtSchedule(rtSchedMsg);
				if (prog != null) {
					returnMsg = "Published RtSchedule ("+rtSched.getMyCh().getChannel().getIdChBusiness()+"): "
						+rtSched.getProgramme().getNameProg()
						+" -> "+rtSched.getState();
				}
				else {
					returnMsg = "Channel ["+rtSched.getMyCh().getChannel().getIdChBusiness()+"]:RtSchedule DOES NOT contain any programme";
				}
				return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
			}
			else { // Schedule
				Schedule sched = schedServ.findByChannelAndStartAfterOrderByStartAsc(ch, true);
				if (sched != null) {
					List<Schedule> lSched = new ArrayList<Schedule>();
					lSched.add(sched);
					Message<List<Schedule>> lSchedMsg = MessageBuilder.withPayload(lSched).build();
					schedPublisher.publishListSchedules(lSchedMsg);
					returnMsg = "Published Sched ("+sched.getIdSched()+"): "+sched.getProgramme().getNameProg()
						+ " ["+sched.getChannel().getIdChBusiness()+"]"
						+" -> "+CommonUtility.dateToStr(sched.getStart())+
						" --- "+CommonUtility.dateToStr(sched.getEnd())+
						" ==> Published: "+sched.isPublished();
					return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>(returnErrMsg, HttpStatus.BAD_REQUEST);
				}
			}
		}
		else {
			return new ResponseEntity<String>(returnErrMsg, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(
			value = "/batchTraining/", 
			method = RequestMethod.GET)
	public ResponseEntity<Boolean> batchTraining(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness)
	{
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
		MyCh myCh = monitorMyCh.getByChannel(ch);
		if (myCh.getTrainedModel().getBatchGoodSamplesUri().length() > 0) {
			boolean result = myCh.getTrainedModel().trainWithBatchSamples();
			if (result) {
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
			}
		}
		else {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(
			value = "/clearTrainedModel/", 
			method = RequestMethod.GET)
	public ResponseEntity<Boolean> clearTrainedModel(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness)
	{
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
		MyCh myCh = monitorMyCh.getByChannel(ch);
//		if (myCh.getTrainedModel().getBatchGoodSamplesUri().length() > 0) {
		myCh.getTrainedModel().clearTrainedModel();
		if ( ! myCh.getTrainedModel().isTrained()) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
//		}
//		else {
//			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
//		}
	}
	
	@RequestMapping(
			value = "/setTrained/", 
			method = RequestMethod.GET)
	public ResponseEntity<Boolean> setTrained(
			@RequestParam(value="hashIdChBusiness", defaultValue="", required=true) String hashIdChBusiness,
			@RequestParam(value="trained", defaultValue="", required=true) boolean must_train)
	{
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
		MyCh myCh = monitorMyCh.getByChannel(ch);
		if (must_train) {
			if ( myCh.getTrainedModel().isAbleToCV()) {
				myCh.getTrainedModel().setTrained(true);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
			}
		}
		else {
			myCh.getTrainedModel().setTrained(false);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
	
	@RequestMapping(
			value = "/learnNewClassifiedSample/", 
			method = RequestMethod.POST)
	public ResponseEntity<Boolean> learnNewClassifiedSample(
			@RequestParam(value="idBlobPersistence", defaultValue="", required=true) long idBlobPersistence,
			@RequestParam(value="classificationResult", defaultValue="", required=true) boolean classificationResult)
	{
		BlobFrame blob = blobServ.findOneByIdBlobPersistenceInitChannel(idBlobPersistence);
		// IMPORTANTE: Obtener el MyCh que está en memoria. No una nueva instancia de la BD
		if (blob != null) {
			MyCh myCh = monitorMyCh.getByChannel(blob.getChannel());
			// Añadir muestra
			myCh.getTrainedModel().learnSample(blob, classificationResult);
			// Eliminar blob de la Base de Datos
			blobServ.delete(blob);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
