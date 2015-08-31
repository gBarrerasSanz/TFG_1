package guiatv.persistence.repository;

import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlobRepository extends JpaRepository<Blob, Long>,
JpaSpecificationExecutor<Blob>, ChannelRepositoryCustom {
	
	Page<Blob> findAll(Pageable pageable);
	
	Page<Blob> findByMyCh(MyCh myCh, Pageable pageable);
	
	Blob findOneByIdBlobPersistence(long idBlobPersistence);

}
