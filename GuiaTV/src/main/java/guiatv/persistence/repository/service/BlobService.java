package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.repository.BlobRepository;
import guiatv.persistence.repository.MLChannelRepository;
import guiatv.persistence.repository.StreamSourceRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BlobService {
	
	@Autowired
	BlobRepository blobRep;
	
	@Transactional(readOnly = true)
	public List<Blob> findAll() {
		return blobRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Blob> findAll(Pageable pageable) {
		return blobRep.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Blob findOneByIdBlobPersistence(long idBlobPersistence) {
		Blob blob = blobRep.findOneByIdBlobPersistence(idBlobPersistence);
		Hibernate.initialize(blob.getBlob());
		return blob;
	}
	
	@Transactional(readOnly = true)
	public Long count() {
		return blobRep.count();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Blob blob) {
		blobRep.save(blob);
    }
	
}
