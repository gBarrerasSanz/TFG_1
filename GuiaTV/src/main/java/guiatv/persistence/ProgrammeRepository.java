package guiatv.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import guiatv.domain.Channel;
import guiatv.domain.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long>,
		JpaSpecificationExecutor<Programme>, ChannelRepositoryCustom {

	Programme findByIdProg(Long idProg);
}
