package guiatv.persistence.repository;

import guiatv.persistence.domain.blobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlobRepository extends JpaRepository<blobFrame, Long>,
JpaSpecificationExecutor<blobFrame>, ChannelRepositoryCustom {
	
	Page<blobFrame> findAll(Pageable pageable);
	
	Page<blobFrame> findByChannel(Channel channel, Pageable pageable);
	
	blobFrame findOneByIdBlobPersistence(long idBlobPersistence);

}
