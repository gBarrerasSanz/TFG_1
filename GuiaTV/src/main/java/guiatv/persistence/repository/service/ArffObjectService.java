//package guiatv.persistence.repository.service;
//
//import java.util.List;
//
//import guiatv.catalog.datatypes.ListChannels;
//import guiatv.persistence.domain.Blob;
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain.MLChannel;
//import guiatv.persistence.domain.Programme;
//import guiatv.persistence.domain.Schedule;
//import guiatv.persistence.domain.StreamSource;
//import guiatv.persistence.domain.helper.ArffHelper;
//import guiatv.persistence.repository.ArffObjectRepository;
//import guiatv.persistence.repository.BlobRepository;
//import guiatv.persistence.repository.MLChannelRepository;
//import guiatv.persistence.repository.StreamSourceRepository;
//
//import org.hibernate.Hibernate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class ArffObjectService {
//	
//	@Autowired
//	ArffObjectRepository arffRep;
//	
//	@Transactional(readOnly = true)
//	public List<ArffHelper> findAll() {
//		return arffRep.findAll();
//	}
//	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void save(ArffHelper arffObject) {
//		arffRep.save(arffObject);
//    }
//	
//}
