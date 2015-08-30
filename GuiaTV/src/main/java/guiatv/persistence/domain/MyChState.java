package guiatv.persistence.domain;

import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.Logger;

import guiatv.persistence.domain.RtSchedule.InstantState;

/*
 * ESTA CLASE NO ES UNA ENTITY
 */
public class MyChState {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	private MyCh myCh;
	private boolean active;
	private boolean spied;
	private InstantState rtCurrState;
	private Queue<InstantState> rtSchedFifo;
	private int samplesToFalse;
	private int samplesToTrue;
	
	
	public MyChState() {
	}
	
	/*
	 * CONSTRUCTOR PREFERIDO
	 */
	public MyChState(boolean active, int samplesToFalse, int samplesToTrue) {
		this.active = active;
		this.samplesToFalse = samplesToFalse;
		this.samplesToTrue = samplesToTrue;
		
		/**
		 * Inicializar el tamaño de rtSchedFifo como max(samplesToFalse, samplesToTrue)
		 * Luego ya cuando se añada un RtSchedule a la fifo, dependiendo del rtCurrState 
		 * se valorará cuántos elementos de la fifo (desde el último hacia atrás)
		 * hay que tener en cuenta
		 */
		int max = Math.max(samplesToFalse, samplesToTrue);
		this.rtSchedFifo = new CircularFifoQueue<InstantState>(max);
	}
	
	public void setParent(MyCh myCh) {
		this.myCh = myCh;
	}
	
	public synchronized boolean requestSpying() {
		// Si estado activo y no está siendo espiado
		if (active && ! spied) {
			// Entonces dar semáforo verde
			return true;
		}
		else { // En otro caso
			return false;
		}
	}
	
	public synchronized void releaseSpying() {
		spied = false;
	}
	
	public synchronized void activate() {
		active = true;
	}
	
	public synchronized void deactivate() {
		active = false;
		spied = false;
	}
	
	public synchronized void resetStateAndFifo() {
		rtCurrState = null;
		rtSchedFifo.clear();
	}
	
	
	/**
	 * Añade el estado del RtSchedule actual a la cola circular
	 * Devuelve TRUE si el estado currentState ha cambiado y por lo tanto el cambio
	 * debe ser notificado y FALSE en otro caso.
	 * 
	 * En principio NO hace falta la keyword synchronized, ya que cada instancia
	 * de MLChannel debe se accedida por un solo thread
	 */
	public synchronized boolean addRtSched(RtSchedule rtSched) {
		int lastEventsToWatch;
		if (rtCurrState.equals(InstantState.ON_PROGRAMME)) { // Si está en programa (true)
			lastEventsToWatch = samplesToFalse;
		}
		else { // Si está en publicidad (false)
			lastEventsToWatch = samplesToTrue;
		}
		if (rtSchedFifo.size() < lastEventsToWatch) { // Si la cola NO está llena aun
			rtSchedFifo.add(rtSched.getState());
			return false;
		}
		else { // Si la cola está llena
			if (rtCurrState == null) { // Si todavía NO se ha inicializado currentState
				int numOnProgramme = 0, numOnAdverts = 0;
				// TODO: Revisar esto
				// Quedarse con el instante mayoritario
				for (InstantState instant: rtSchedFifo) {
					switch(instant) {
					case ON_PROGRAMME:
						numOnProgramme++;
						break;
					case ON_ADVERTS:
						numOnAdverts++;
						break;
					default:
						break;
					}
				}
				if (numOnProgramme > numOnAdverts) {
					rtCurrState = InstantState.ON_PROGRAMME;
				}
				if (numOnAdverts > numOnProgramme) {
					rtCurrState = InstantState.ON_ADVERTS;
				}
				Channel ch = rtSched.getMyCh().getChannel();
				logger.debug("Initialized channel ("+ch.getIdChBusiness()+") -> "+rtCurrState);
				return false;
			}
			else { // Si currentState ya estaba inicializado
				rtSchedFifo.add(rtSched.getState());
				InstantState[] instantArr = (InstantState[]) rtSchedFifo.toArray();
				for (int i=instantArr.length-1; i >= instantArr.length-lastEventsToWatch; i--) {
					InstantState instant = instantArr[i];
					// Si alguno de los estados de la cola coincide con el estado actual -> Salir
					if (instant.equals(rtSched)) {
						return false;
					}
				}
				/*
				 * Post: Todos los estados de la cola son distintos al estado actual currentState
				 * Por lo tanto rtSched.getState(), al pertenecer a la cola, también 
				 * es distinto al estado actual 
				 */
				// Cambiar el estado actual
				if (rtCurrState.equals(InstantState.ON_PROGRAMME)) { 
					rtCurrState = InstantState.ON_ADVERTS;
				}
				else {
					rtCurrState = InstantState.ON_PROGRAMME;
				}
				return true;
			}
		}
	}
	
	
	public synchronized boolean isActive() {
		return active;
	}

	public synchronized void setActive(boolean active) {
		this.active = active;
	}

	public synchronized boolean isSpied() {
		return spied;
	}

	public synchronized void setSpied(boolean spied) {
		this.spied = spied;
	}

	public synchronized InstantState getRtCurrState() {
		return rtCurrState;
	}

	public synchronized void setRtCurrState(InstantState rtCurrState) {
		this.rtCurrState = rtCurrState;
	}

	public synchronized Queue<InstantState> getRtSchedFifo() {
		return rtSchedFifo;
	}

	public synchronized void setRtSchedFifo(Queue<InstantState> rtSchedFifo) {
		this.rtSchedFifo = rtSchedFifo;
	}

	public synchronized int getSamplesToFalse() {
		return samplesToFalse;
	}

	public synchronized void setSamplesToFalse(int samplesToFalse) {
		this.samplesToFalse = samplesToFalse;
	}

	public synchronized int getSamplesToTrue() {
		return samplesToTrue;
	}

	public synchronized void setSamplesToTrue(int samplesToTrue) {
		this.samplesToTrue = samplesToTrue;
	}
	
}
