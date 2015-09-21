package guiatv.persistence.repository;

import guiatv.persistence.domain.BlobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlobRepository extends JpaRepository<BlobFrame, Long>,
JpaSpecificationExecutor<BlobFrame>, ChannelRepositoryCustom {
	
	Page<BlobFrame> findAll(Pageable pageable);
	
	Page<BlobFrame> findByChannel(Channel channel, Pageable pageable);
	
	BlobFrame findOneByIdBlobPersistence(long idBlobPersistence);

}
