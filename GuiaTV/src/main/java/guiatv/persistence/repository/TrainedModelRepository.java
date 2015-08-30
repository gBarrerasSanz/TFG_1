package guiatv.persistence.repository;

import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.TrainedModel;
import guiatv.persistence.repository.NOTUSED.ChannelRepositoryCustom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainedModelRepository extends JpaRepository<TrainedModel, Long>,
JpaSpecificationExecutor<TrainedModel>, ChannelRepositoryCustom {
	
	TrainedModel findOneByIdTrainedModelPersistence(Long idTrainedModelPersistence);

}
