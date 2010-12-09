package org.tbrd.bitri.data.tri.diario.inverno ;

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

		// data is between 08:00 and 09:00 -> CHEIO
		limit.set(Calendar.HOUR_OF_DAY, 9) ;
		limit.set(Calendar.MINUTE, 0) ;

		if (data.before(limit))
			return new Timetable(Horario.CHEIO, limit, Horario.PONTA) ;

		// data is between 09:00 and 10:30 -> PONTA
		limit.set(Calendar.HOUR_OF_DAY, 10) ;
		limit.set(Calendar.MINUTE, 30) ;

		if (data.before(limit))
			return new Timetable(Horario.PONTA, limit, Horario.CHEIO) ;

		// data is between 10:30 and 18:00 -> CHEIO
		limit.set(Calendar.HOUR_OF_DAY, 18) ;
		limit.set(Calendar.MINUTE, 0) ;

		if (data.before(limit))
			return new Timetable(Horario.CHEIO, limit, Horario.PONTA) ;

		// data is between 18:00 and 20:30 -> PONTA
		limit.set(Calendar.HOUR_OF_DAY, 20) ;
		limit.set(Calendar.MINUTE, 30) ;

		if (data.before(limit))
			return new Timetable(Horario.PONTA, limit, Horario.CHEIO) ;

		// data is between 20:30 and 22:00 -> CHEIO
		limit.set(Calendar.HOUR_OF_DAY, 22) ;
		limit.set(Calendar.MINUTE, 0) ;

		if (data.before(limit))
			return new Timetable(Horario.CHEIO, limit, Horario.VAZIO) ;

		// data is between 22:00 and 08:00 next morning -> VAZIO
		limit.add(Calendar.DAY_OF_YEAR, 1) ;
		limit.set(Calendar.HOUR_OF_DAY, 8) ;
		limit.set(Calendar.MINUTE, 0) ;
		return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

	}
}