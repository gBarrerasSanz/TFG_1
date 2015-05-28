package guiatv.persistence.repository.NOTUSED;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long>,
		JpaSpecificationExecutor<Programme>, ChannelRepositoryCustom {

	Programme findByIdProg(Long idProg);
	
	Programme findByNameProg(String nameProg);
}
