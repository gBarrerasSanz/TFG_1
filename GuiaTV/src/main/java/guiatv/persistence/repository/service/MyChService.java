//package guiatv.persistence.repository.service;
//
//import java.util.List;
//
//import guiatv.catalog.datatypes.ListChannels;
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain.MyCh;
//import guiatv.persistence.domain.Programme;
//import guiatv.persistence.domain.Schedule;
//import guiatv.persistence.repository.MyChRepository;
//
//import org.hibernate.Hibernate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class MyChService {
//	
//	@Autowired
//	MyChRepository myChRep;
//	
//	@Transactional(readOnly = true)
//	public List<MyCh> findAll() {
//		return myChRep.findAll();
//	}
//	
//	@Transactional(readOnly = true)
//	public MyCh findByChannel(Channel ch) {
//		return myChRep.findByChannel(ch);
//	}
//	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void save(MyCh myCh) {
//		myChRep.save(myCh);
//    }
//	
//}
