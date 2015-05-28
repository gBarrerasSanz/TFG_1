package guiatv.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

public interface ChannelRepository extends JpaRepository<Channel, Long>,
		JpaSpecificationExecutor<Channel>, ChannelRepositoryCustom {

	Channel findByIdCh(Long idCh);
	
	Channel findByNameIdCh(String nameIdCh);
	
	List<Channel> findByCountry(String country);

	<S extends Channel> Channel save(Channel ch);
}
