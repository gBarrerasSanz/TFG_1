//package guiatv.persistence.domain_NOT_USED;
//
//import guiatv.persistence.domain.Channel;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.Lob;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.PrimaryKeyJoinColumn;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.UniqueConstraint;
//import javax.persistence.Version;
//
//import org.hibernate.annotations.Cascade;
//import org.hibernate.annotations.CascadeType;
//
///*
// * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
// * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
// * business-Id: (channel, rtmpUrl)
// */
//@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel", "rtmpUrl"})})
//@Entity(name = "rtmpSource")
//public class RtmpSource implements Serializable {
//
//	private static final long serialVersionUID = -8835185421528324020L;
//	
//	@Id
//    @Column(name = "idRtmpSource", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idRtmpSource;
//	
//	@Cascade(value=CascadeType.ALL)
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="channel")
//	private Channel channel;
//	
//	@Column(name = "rtmpUrl", nullable = false)
//	private String rtmpUrl;
//	
//    @OneToMany(mappedBy="rtmpSource", fetch=FetchType.LAZY)
//    private Set<LearnedRtmpSource> setLearnedRtmpSources;
//
//    /**********************************************************
//     * 					GETTERS / SETTERS
//     *********************************************************/
//	
//	public RtmpSource() {
//	}
//
//	public RtmpSource(Channel ch, String rtmpUrl) {
//		this.channel = ch;
//		this.rtmpUrl = rtmpUrl;
//	}
//	public Channel getChannel() {
//		return channel;
//	}
//
//	public void setChannel(Channel channel) {
//		this.channel = channel;
//	}
//
//	public String getRtmpUrl() {
//		return rtmpUrl;
//	}
//
//	public void setRtmpUrl(String rtmpUrl) {
//		this.rtmpUrl = rtmpUrl;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((channel == null) ? 0 : channel.hashCode());
//		result = prime * result + ((rtmpUrl == null) ? 0 : rtmpUrl.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		RtmpSource other = (RtmpSource) obj;
//		if (channel == null) {
//			if (other.channel != null)
//				return false;
//		} else if (!channel.equals(other.channel))
//			return false;
//		if (rtmpUrl == null) {
//			if (other.rtmpUrl != null)
//				return false;
//		} else if (!rtmpUrl.equals(other.rtmpUrl))
//			return false;
//		return true;
//	}
//	
//}
