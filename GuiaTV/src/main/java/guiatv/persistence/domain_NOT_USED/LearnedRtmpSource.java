//package guiatv.persistence.domain_NOT_USED;
//
//import java.io.Serializable;
//import java.util.Date;
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
//
////TODO: Hacer el Entidad Relacion y hacer los @OneToMany y @ManyToOne y luego ya las restricciones
//// TODO: Esta tabla no sé todavía cómo la voy a utilizar
///*
// * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
// * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
// * 
// */
//@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"rtmpSource", "method", "learned"})})
//@Entity(name = "learnedRtmpSource")
//public class LearnedRtmpSource implements Serializable {
//
//	private static final long serialVersionUID = -5158218062363768675L;
//    
//	@Id
//    @Column(name = "idLearnedCh", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idLearnedCh;
//    
//	@Cascade(value=CascadeType.ALL)
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="rtmpSource")
//	private RtmpSource rtmpSource;
//	
//	@Column(name = "method", nullable = false)
//	private String method;
//	
//	@Column(name = "learned", nullable = false)
//	private boolean learned;
//	
//    @Lob
//    @Column(name = "templateImg", nullable = true)
//    private byte[] templateImg;
//    
//    
//    /**********************************************************
//     * 					GETTERS / SETTERS
//     *********************************************************/
//    public LearnedRtmpSource() {
//    }
//
//    public LearnedRtmpSource(RtmpSource rtmpSource, String method, boolean learned) {
//    	this.rtmpSource = rtmpSource;
//    	this.method = method;
//    	this.learned = learned;
//    }
//    
//	public RtmpSource getRtmpSource() {
//		return rtmpSource;
//	}
//
//	public void setRtmpSource(RtmpSource rtmpSource) {
//		this.rtmpSource = rtmpSource;
//	}
//
//	public boolean isLearned() {
//		return learned;
//	}
//
//	public void setLearned(boolean learned) {
//		this.learned = learned;
//	}
//
//	public byte[] getTemplateImg() {
//		return templateImg;
//	}
//
//	public void setTemplateImg(byte[] templateImg) {
//		this.templateImg = templateImg;
//	}
//
//	public Long getIdLearnedCh() {
//		return idLearnedCh;
//	}
//
//	public void setIdLearnedCh(Long idLearnedCh) {
//		this.idLearnedCh = idLearnedCh;
//	}
//
//	public String getMethod() {
//		return method;
//	}
//
//	public void setMethod(String method) {
//		this.method = method;
//	}
//    
//	
//}
