package guiatv.persistence.repository;

import guiatv.persistence.domain.Blob;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoiBlobRepository extends JpaRepository<Blob, Long>,
JpaSpecificationExecutor<Blob>, ChannelRepositoryCustom {
	
	
}
