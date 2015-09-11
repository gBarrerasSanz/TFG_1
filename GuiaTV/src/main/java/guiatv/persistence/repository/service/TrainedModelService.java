//package guiatv.persistence.repository.service;
//
//import java.util.List;
//
//import guiatv.catalog.datatypes.ListChannels;
//import guiatv.persistence.domain.Blob;
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain.Programme;
//import guiatv.persistence.domain.Schedule;
//import guiatv.persistence.domain.StreamSource;
//import guiatv.persistence.domain.TrainedModel;
//import guiatv.persistence.repository.BlobRepository;
//import guiatv.persistence.repository.StreamSourceRepository;
//import guiatv.persistence.repository.TrainedModelRepository;
//
//import org.hibernate.Hibernate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class TrainedModelService {
//	
//	@Autowired
//	TrainedModelRepository trainedModelRep;
//	
//	@Transactional(readOnly = true)
//	public List<TrainedModel> findAll() {
//		return trainedModelRep.findAll();
//	}
//	
//	@Transactional(readOnly = true)
//	public TrainedModel findOneByIdTrainedModelPersistence(long idTrainedModelPersistence) {
//		TrainedModel trainedModel = trainedModelRep.findOneByIdTrainedModelPersistence(idTrainedModelPersistence);
//		return trainedModel;
//	}
//	
//	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void save(TrainedModel trainedModel) {
//		trainedModelRep.save(trainedModel);
//    }
//	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void delete(TrainedModel trainedModel) {
//		trainedModelRep.delete(trainedModel);
//    }
//	
//}
