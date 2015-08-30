package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.repository.StreamSourceRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StreamSourceService {
	
	@Autowired
	StreamSourceRepository streamSourceRep;
	
	@Transactional(readOnly = true)
	public List<StreamSource> findAll() {
		return streamSourceRep.findAll();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(StreamSource streamSource) {
		streamSourceRep.save(streamSource);
    }
	
}
