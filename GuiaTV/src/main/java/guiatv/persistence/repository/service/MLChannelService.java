package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.MLChannelRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MLChannelService {
	
	@Autowired
	MLChannelRepository mlChRep;
	
	@Transactional(readOnly = true)
	public List<MLChannel> findAll() {
		return mlChRep.findAll();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(MLChannel mlCh) {
		mlChRep.save(mlCh);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAndSaveFiles(MLChannel mlCh) {
		mlCh.saveDataSet();
		mlCh.saveFullDataSet();
		mlCh.saveTrainedClassifier();
		mlChRep.save(mlCh);
    }
	
}
