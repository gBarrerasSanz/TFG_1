package guiatv.persistence.repository.service;

import java.util.List;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.repository.BlobRepository;

import org.apache.log4j.Logger;
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
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	BlobRepository blobRep;
	
	@Transactional(readOnly = true)
	public List<BlobFrame> findAll() {
		return blobRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<BlobFrame> findAll(Pageable pageable) {
		Page<BlobFrame> page = blobRep.findAll(pageable);
		for (BlobFrame blob: page) {
			Hibernate.initialize(blob.getChannel());
		}
		return page;
	}
	
	@Transactional(readOnly = true)
	public Page<BlobFrame> findByChannel(Channel channel, Pageable pageable) {
		return blobRep.findByChannel(channel, pageable);
	}
	
	
//	@Transactional(readOnly = true)
//	public Blob findOneByIdBlobPersistenceInitTrainedModel(long idBlobPersistence) {
//		Blob blob = blobRep.findOneByIdBlobPersistence(idBlobPersistence);
//		Hibernate.initialize(blob.getBlob());
//		// Inicializar TrainedModel
//		Hibernate.initialize(blob.getMyCh().getTrainedModel());
//		return blob;
//	}
	
	@Transactional(readOnly = true)
	public BlobFrame findOneByIdBlobPersistence(long idBlobPersistence) {
		BlobFrame blob = blobRep.findOneByIdBlobPersistence(idBlobPersistence);
		Hibernate.initialize(blob.getBlobImg());
		return blob;
	}
	
	@Transactional(readOnly = true)
	public BlobFrame findOneByIdBlobPersistenceInitChannel(long idBlobPersistence) {
		BlobFrame blob = blobRep.findOneByIdBlobPersistence(idBlobPersistence);
		if (blob != null) {
			Hibernate.initialize(blob.getBlobImg());
			// Inicializar Channel
			Hibernate.initialize(blob.getChannel());
			return blob;
		}
		else {
			return null;
		}
	}
	
	
	@Transactional(readOnly = true)
	public Long count() {
		return blobRep.count();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(BlobFrame blob) {
		try {
			blobRep.save(blob);
		} catch(Exception e) {
			logger.debug(e.getMessage());
		}
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(BlobFrame blob) {
		blobRep.delete(blob);
    }
	
}
