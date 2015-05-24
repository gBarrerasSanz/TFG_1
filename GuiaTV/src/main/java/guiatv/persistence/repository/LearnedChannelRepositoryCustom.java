package guiatv.persistence.repository;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;

public interface LearnedChannelRepositoryCustom {

	/**
	 * Method actually triggering a finder but being overridden.
	 */
//	void findByOverrridingMethod();

	/**
	 * Some custom method to implement.
	 * 
	 * @param user
	 */
//	void someCustomMethod(Channel ch);
	
	public boolean isTrained(Channel ch, RtmpSource source);
}
