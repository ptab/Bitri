package org.tbrd.bitri.data.bi.diario ;

import java.util.Calendar ;
import java.util.GregorianCalendar ;

import org.tbrd.bitri.data.Horario ;
import org.tbrd.bitri.data.Timetable ;

public abstract class Calculator {

	public static Timetable calc(GregorianCalendar data) {
		GregorianCalendar limit = (GregorianCalendar) data.clone() ;

		// data is before 08:00 -> VAZIO
		limit.set(Calendar.HOUR_OF_DAY, 8) ;
		limit.set(Calendar.MINUTE, 0) ;

		if (data.before(limit))
			return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

		// data is between 08:00 and 22:00 -> CHEIO
		limit.set(Calendar.HOUR_OF_DAY, 22) ;
		limit.set(Calendar.MINUTE, 0) ;

		if (data.before(limit))
			return new Timetable(Horario.CHEIO, limit, Horario.VAZIO) ;

		// data is between 22:00 and 08:00 next day -> VAZIO
		limit.add(Calendar.DAY_OF_YEAR, 1) ;
		limit.set(Calendar.HOUR_OF_DAY, 8) ;
		limit.set(Calendar.MINUTE, 0) ;
		return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;
	}
}