package guiatv.catalog.restcontroller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import guiatv.Application;
import guiatv.ApplicationTest;
import guiatv.catalog.datatypes.Catalog;
import guiatv.catalog.datatypes.ListChannels;
import guiatv.catalog.datatypes.ListProgrammes;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.ProgrammeRepository;
import guiatv.persistence.repository.ScheduleRepository;
import guiatv.persistence.repository.service.AsyncTransactionService;
import guiatv.schedule.utils.ListScheduleCreator;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;








import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("CatalogRestControllerTests")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebIntegrationTest
public class CatalogRestControllerTests extends
		AbstractTransactionalJUnit4SpringContextTests {

	private static Logger logger = Logger.getLogger("debugLog");

	@Autowired
	ScheduleRepository schedRep;

	@Autowired
	ProgrammeRepository progRep;

	@Autowired
	ChannelRepository chRep;

	@Autowired
	AsyncTransactionService transService;
	
	
	@Value("${host.addr:http://127.0.0.1}")
	private String HOST_ADDR;
	@Value("${host.port:8080}")
	private String HOST_PORT;

	/**
	 * TODO: Si se ejecutan todos los tests a la vez, algunos fallan. Creo que es debido a que el servicio transService 
	 * que inserta los datos es asíncrono y el comportamiento es impredecible. 
	 * Una opción es insertar los datos en la BD una sola vez al principi y asegurar el orden de ejecución de los tests.
	 * La mejor opción sería mockear la base de datos. Además así se conseguiría eliminar la dependencia de la bd.
	 */
	
	
	@Test
	public void catalogTest() {
		String CATALOG_URI = HOST_ADDR + ":" + HOST_PORT + "/" + "catalog";
		try {
			ResponseEntity<Catalog> resp = new TestRestTemplate().getForEntity(
					CATALOG_URI, Catalog.class);
			assertEquals(HttpStatus.OK, resp.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void channelsTest() {
		String CATALOG_URI = HOST_ADDR + ":" + HOST_PORT + "/" + "catalog";
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator
					.getListSchedule();
			List<Channel> lCh = new ArrayList<Channel>();
			for (Schedule sched : listSchedExpected) {
				Channel ch = sched.getChannel();
				if (!lCh.contains(ch)) {
					lCh.add(ch);
				}
			}
			transService.insertChannels(lCh);
			ResponseEntity<ListChannels> resp = new TestRestTemplate()
					.getForEntity(CATALOG_URI + "/channels/",(ListChannels.class));

			assertEquals(HttpStatus.OK, resp.getStatusCode());
			assertEquals(lCh, resp.getBody());
			transService.deleteAllData();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
//	@Test
	public void singleChannelTest() {
		String CATALOG_URI = HOST_ADDR + ":" + HOST_PORT + "/" + "catalog";
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator
					.getListSchedule();
			List<Channel> lCh = new ArrayList<Channel>();
			for (Schedule sched : listSchedExpected) {
				Channel ch = sched.getChannel();
				if (!lCh.contains(ch)) {
					lCh.add(ch);
				}
			}
			transService.insertChannels(lCh);
			ResponseEntity<Channel> resp = new TestRestTemplate()
					.getForEntity(CATALOG_URI + "/channels/"+lCh.get(0).getIdChBusiness(),Channel.class);

			assertEquals(HttpStatus.OK, resp.getStatusCode());
			assertEquals(lCh, resp.getBody());
			transService.deleteAllData();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

//	@Test
	public void programmesTest() {
		String CATALOG_URI = HOST_ADDR + ":" + HOST_PORT + "/" + "catalog";
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator
					.getListSchedule();

			List<Programme> lProg = new ArrayList<Programme>();
			for (Schedule sched : listSchedExpected) {
				Programme prog = sched.getProgramme();
				if (!lProg.contains(prog)) {
					lProg.add(prog);
				}
			}
			transService.insertProgrammes(lProg);
			
			ResponseEntity<ListProgrammes> resp = new TestRestTemplate()
					.getForEntity(CATALOG_URI + "/programmes/",
							ListProgrammes.class);
			

			assertEquals(HttpStatus.OK, resp.getStatusCode());
			assertEquals(lProg, resp.getBody());
			transService.deleteAllData();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

//	@Test
	public void programmeSchedulesTest() {
		String CATALOG_URI = HOST_ADDR + ":" + HOST_PORT + "/" + "catalog";
		try {
			// Cargar valores de prueba en la base de datos
			List<Schedule> listSchedExpected = ListScheduleCreator
					.getListSchedule();
			// listSchedExpected.getlSched().get(0).getProgramme().setNameProg("1");
			transService.insertSchedules(listSchedExpected);
			ResponseEntity<ListProgrammes> resp = new TestRestTemplate()
					.getForEntity(CATALOG_URI + "/programmes/",
							ListProgrammes.class);

			assertEquals(HttpStatus.OK, resp.getStatusCode());

			List<Schedule> lSchedReturned = new ArrayList<Schedule>();
			for (Programme prog : resp.getBody()) {
				for (Schedule sched : prog.getListSchedules()) {
					if (!lSchedReturned.contains(sched)) {
						lSchedReturned.add(sched);
					}
				}
			}
			assertEquals(listSchedExpected.size(), lSchedReturned.size());
			for (int i=0; i<listSchedExpected.size(); i++) {
				assertEquals(listSchedExpected.get(i).getProgramme(), lSchedReturned.get(i).getProgramme());
			}
			
			transService.deleteAllData();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
