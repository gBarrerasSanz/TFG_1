package xmltv.datatypes;

import java.util.List;

import org.springframework.messaging.handler.annotation.Payload;

public interface EventoService {

	/**
	 * Creates a {@link Evento} instance from the {@link Evento} instance passed
	 *
	 * @param person created EventoTV instance, it will contain the generated primary key and the formated name
	 * @return The persisted Entity
	 */
	Evento createEvento(Evento evt);

	/**
	 *
	 * @return the matching {@link Evento} record(s)
	 */
	@Payload("new java.util.Date()")
	List<Evento> findEvento();

}