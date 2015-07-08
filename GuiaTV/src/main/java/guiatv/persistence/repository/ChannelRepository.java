package guiatv.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

public interface ChannelRepository extends JpaRepository<Channel, Long>,
		JpaSpecificationExecutor<Channel>, ChannelRepositoryCustom {

//	Channel findByIdChPersistence(Long idChPersistence);
	
	ListChannels findAll();
	
	Channel findByIdChBusiness(String idChBusiness);
	
	Channel findByHashIdChBusiness(String hashIdChBusiness);
	
	List<Channel> findByCountry(String country);
	
	<S extends Channel> Channel save(Channel ch);
}
