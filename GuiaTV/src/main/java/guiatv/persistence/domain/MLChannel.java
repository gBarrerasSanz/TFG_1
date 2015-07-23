package guiatv.persistence.domain;

import java.util.List;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.persistence.domain.Schedule.CustomSchedule;
import guiatv.schedule.publisher.SchedulePublisher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import weka.classifiers.Classifier;

@Entity(name = "mlchannel")
public class MLChannel {
	
	@Id
    @Column(name = "idMlChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMlChPersistence;
	
	@OneToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
	@OneToOne(targetEntity=ArffObject.class, fetch=FetchType.LAZY)
	@JoinColumn(name="arffObject_fk", referencedColumnName="idArffPersistence")
	private ArffObject arffObject;
	
	@Column(name="trainedClassifier", nullable=true)
	private Classifier trainedClassifier;
	
	@Column(name="topLeft", nullable=false)
	private int[] topLeft;
	
	@Column(name="botRight", nullable=false)
	private int[] botRight;
	
	@OneToMany(targetEntity=StreamSource.class, mappedBy="mlChannel", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<StreamSource> listStreamSources;
	
	@OneToMany(targetEntity=RoiBlob.class, mappedBy="mlChannel", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<StreamSource> listRoiBlobs;
	
	public MLChannel() {
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ArffObject getArffObject() {
		return arffObject;
	}

	public void setArffObject(ArffObject arffObject) {
		this.arffObject = arffObject;
	}

	public Classifier getTrainedClassifier() {
		return trainedClassifier;
	}

	public void setTrainedClassifier(Classifier trainedClassifier) {
		this.trainedClassifier = trainedClassifier;
	}

	public int[] getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(int[] topLeft) {
		this.topLeft = topLeft;
	}

	public int[] getBotRight() {
		return botRight;
	}

	public void setBotRight(int[] botRight) {
		this.botRight = botRight;
	}

	public List<StreamSource> getListStreamSources() {
		return listStreamSources;
	}

	public void setListStreamSources(List<StreamSource> listStreamSources) {
		this.listStreamSources = listStreamSources;
	}

	public List<StreamSource> getListRoiBlobs() {
		return listRoiBlobs;
	}

	public void setListRoiBlobs(List<StreamSource> listRoiBlobs) {
		this.listRoiBlobs = listRoiBlobs;
	}

	public Long getIdMlChPersistence() {
		return idMlChPersistence;
	}
	
	
}
