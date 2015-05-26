package guiatv.schedule.utils;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ListScheduleCreator {
	
	public static List<Schedule> getListSchedule() {
		// Construir resultado esperado
		List<Schedule> listScheduleExpected = new ArrayList<Schedule>();
		// Crear channels
		Channel chNeox = new Channel();
		chNeox.setNameIdCh("neox-722.laguiatv.com");
		// Crear programmes
		Programme progMadre = new Programme();
		progMadre.setNameProg("Cómo conocí a vuestra Madre");
		Programme progSimpsons = new Programme();
		progSimpsons.setNameProg("Los Simpson");
		// Crear schedules
		Schedule schedMadre = new Schedule();
		schedMadre.setChannel(chNeox);
		schedMadre.setProgramme(progMadre);
//		schedMadre.setStart(CommonUtility.strToDate("20150316173500 +0100"));
//		schedMadre.setEnd(CommonUtility.strToDate("20150316175400 +0100"));
		schedMadre.setStart(CommonUtility.strToTimestamp("20150316173500 +0100"));
		schedMadre.setEnd(CommonUtility.strToTimestamp("20150316175400 +0100"));
		
		
		Schedule schedSimpsons = new Schedule();
		schedSimpsons.setChannel(chNeox);
		schedSimpsons.setProgramme(progSimpsons);
//		schedSimpsons.setStart(CommonUtility.strToDate("20150316214500 +0100"));
//		schedSimpsons.setEnd(CommonUtility.strToDate("20150316220000 +0100"));
		schedSimpsons.setStart(CommonUtility.strToTimestamp("20150316214500 +0100"));
		schedSimpsons.setEnd(CommonUtility.strToTimestamp("20150316220000 +0100"));
		
		listScheduleExpected.add(schedMadre);
		listScheduleExpected.add(schedSimpsons);
		
		return listScheduleExpected;
	}
}
