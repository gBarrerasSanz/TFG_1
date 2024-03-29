package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChannelService {
	
	@Autowired
	ChannelRepository chRep;
	
	
	@Transactional(readOnly = true)
	public ListChannels findAll() {
		return chRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Channel findByIdChBusiness(String idBusiness, boolean refs) {
		Channel ch = chRep.findByIdChBusiness(idBusiness);
		if (ch != null && refs) {
			Hibernate.initialize(ch.getListSchedules());
		}
		return ch;
	}
	
	@Transactional(readOnly = true)
	public Channel findByHashIdChBusiness(String hashIdChBusiness, boolean refs) {
		Channel ch = chRep.findByHashIdChBusiness(hashIdChBusiness);
		if (refs) {
			for (Schedule sched: ch.getListSchedules()) {
				Hibernate.initialize(sched.getChannel());
				Hibernate.initialize(sched.getProgramme());
			}
		}
		return ch;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Channel ch) {
    	chRep.delete(ch);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ListChannels lCh) {
    	chRep.save(lCh);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public Channel saveOrGetExistent(Channel ch) {
		Channel existentCh = chRep.findByHashIdChBusiness(ch.getHashIdChBusiness());
		if (existentCh == null) {
			chRep.save(ch);
			return null;
		}
		else { // Si se ha encontrado
			if (existentCh.equals(ch)) {
				// TODO: Actualizar channel
				chRep.updateNameProgWhereIdChPersistence(ch.getNameCh(), ch.getIdChPersistence());
				return existentCh;
			}
			else {
				return existentCh; 
			}
		}
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Channel ch) {
		chRep.save(ch);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(ListChannels lCh) {
    	chRep.delete(lCh);
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateNameProgWhereIdChPersistence(String nameCh, Channel ch) {
		chRep.updateNameProgWhereIdChPersistence(nameCh, ch.getIdChPersistence());
	}
	
}
