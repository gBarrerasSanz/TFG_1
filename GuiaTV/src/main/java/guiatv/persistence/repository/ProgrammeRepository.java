package guiatv.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

public interface ProgrammeRepository extends JpaRepository<Programme, Long>,
		JpaSpecificationExecutor<Programme>, ChannelRepositoryCustom {

//	Programme findByIdProgPersistence(Long idProgPersistence);
	
	ListProgrammes findAll();
	
	Programme findByNameProg(String nameProg);
	
	Programme findByHashNameProg(String hashNameProg);
	
}
