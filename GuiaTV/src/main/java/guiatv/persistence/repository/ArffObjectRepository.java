package guiatv.persistence.repository;

import guiatv.persistence.domain.ArffObject;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArffObjectRepository extends JpaRepository<ArffObject, Long>,
JpaSpecificationExecutor<ArffObject>, ChannelRepositoryCustom {
	
	
}
