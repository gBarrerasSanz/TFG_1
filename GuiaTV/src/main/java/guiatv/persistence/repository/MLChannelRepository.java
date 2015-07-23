package guiatv.persistence.repository;

import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MLChannelRepository extends JpaRepository<MLChannel, Long>,
JpaSpecificationExecutor<MLChannel>, ChannelRepositoryCustom {
	
	
}
