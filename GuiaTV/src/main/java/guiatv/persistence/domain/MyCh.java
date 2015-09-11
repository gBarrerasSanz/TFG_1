package guiatv.persistence.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.common.CommonUtility;
import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.RtSchedule.InstantState;
import guiatv.persistence.domain.Schedule.CustomSchedule;
import guiatv.persistence.domain.helper.ArffHelper;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

//@Entity(name = "mych")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel_fk", "streamSource_fk"})})
public class MyCh {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Id
    @Column(name = "idMyChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMyChPersistence;
	
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
	@OneToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
	@OneToOne(targetEntity=StreamSource.class, fetch=FetchType.LAZY)
	@JoinColumn(name="streamsource_fk", referencedColumnName="idStreamSourcePersistence")
	private StreamSource streamSrc;
	
	@OneToOne(targetEntity=TrainedModel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="trainedmodel_fk", referencedColumnName="idTrainedModelPersistence")
	private TrainedModel trainedModel;
	
	@Transient
	private MyChState myChState;
	
	public MyCh() {
	}
	
	/*
	 * CONSTRUCTOR PREFERIDO
	 */
	public MyCh(Channel ch, StreamSource streamSrc, TrainedModel trainedModel, MyChState myChState) {
		this.channel = ch;
		this.streamSrc = streamSrc;
		this.trainedModel = trainedModel;
		this.myChState = myChState;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public StreamSource getStreamSrc() {
		return streamSrc;
	}

	public void setStreamSrc(StreamSource streamSrc) {
		this.streamSrc = streamSrc;
	}

	public TrainedModel getTrainedModel() {
		return trainedModel;
	}

	public void setTrainedModel(TrainedModel trainedModel) {
		this.trainedModel = trainedModel;
	}

	public MyChState getMyChState() {
		return myChState;
	}

	public void setMyChState(MyChState myChState) {
		this.myChState = myChState;
	}

	public Long getIdMyChPersistence() {
		return idMyChPersistence;
	}
	
}
