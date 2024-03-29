package guiatv.persistence.domain;

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
import javax.persistence.Transient;


//@Entity(name = "streamsource")
public class StreamSource {
	
//	@Id
//    @Column(name = "idStreamSourcePersistence", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idStreamSourcePersistence;
	
//	@OneToOne(targetEntity=MyCh.class, mappedBy="streamSrc", fetch=FetchType.LAZY)
	
//	@OneToOne(targetEntity=MyCh.class, fetch=FetchType.LAZY)
//	@JoinColumn(name="mych_fk", referencedColumnName="idMyChPersistence")
//	@Transient
	private MyCh myCh;
	
	
	
//	@Column(name="url", nullable=false)
	private String url;
	
//	@Column(name="cols", nullable=false)
	private int cols;
	
//	@Column(name="rows", nullable=false)
	private int rows;
	
//	@Column(name="topLeft", nullable=false)
	private int[] topLeft;
	
//	@Column(name="botRight", nullable=false)
	private int[] botRight;
	
	public StreamSource(){
	}
	
	public StreamSource(String url) {
		this.url = url;
	}
	
	/*
	 * CONSTRUCTOR PREFERIDO
	 */
	public StreamSource(String url, int cols, int rows, int[] topLeft, int[] botRight) {
		this.url = url;
		this.cols = cols;
		this.rows = rows;
		this.topLeft = topLeft;
		this.botRight = botRight;
	}
	
	public void setParent(MyCh myCh) {
		this.myCh = myCh;
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public MyCh getMlChannel() {
		return myCh;
	}

	public void setMyCh(MyCh myCh) {
		this.myCh = myCh;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public Long getIdStreamSourcePersistence() {
//		return idStreamSourcePersistence;
//	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
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
	
	@Override 
	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final StreamSource other = (StreamSource) obj;
	    if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
	        return false;
	    }
	    if (this.cols != other.cols || this.rows != this.rows) {
	    	return false;
	    }
	    if ((this.topLeft == null) ? (other.topLeft != null) : !this.topLeft.equals(other.topLeft)) {
	        return false;
	    }
	    if ((this.botRight == null) ? (other.botRight != null) : !this.botRight.equals(other.botRight)) {
	        return false;
	    }
	    return true;
	}
	
	
}
