package org.tbrd.bitri.data.bi.semanal.inverno ;

import java.util.Calendar ;
import java.util.GregorianCalendar ;

import org.tbrd.bitri.data.Horario ;
import org.tbrd.bitri.data.Timetable ;

public abstract class Calculator {

	public static Timetable calc(GregorianCalendar data) {
		GregorianCalendar limit = (GregorianCalendar) data.clone() ;

		switch (data.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SATURDAY :
				// data is before 09:30 -> VAZIO
				limit.set(Calendar.HOUR_OF_DAY, 9) ;
				limit.set(Calendar.MINUTE, 30) ;

				if (data.before(limit))
					return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

				// data is between 09:30 and 13:00 -> CHEIO
				limit.set(Calendar.HOUR_OF_DAY, 13) ;
				limit.set(Calendar.MINUTE, 0) ;

				if (data.before(limit))
					return new Timetable(Horario.CHEIO, limit, Horario.VAZIO) ;

				// data is between 13:00 and 18:30 -> VAZIO
				limit.set(Calendar.HOUR_OF_DAY, 18) ;
				limit.set(Calendar.MINUTE, 30) ;

				if (data.before(limit))
					return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

				// data is between 18:30 and 22:00 -> CHEIO
				limit.set(Calendar.HOUR_OF_DAY, 22) ;
				limit.set(Calendar.MINUTE, 0) ;

				if (data.before(limit))
					return new Timetable(Horario.CHEIO, limit, Horario.VAZIO) ;

				// data is between 22:00 and 07:00 next *monday* -> VAZIO
				limit.add(Calendar.DAY_OF_YEAR, 2) ;
				limit.set(Calendar.HOUR_OF_DAY, 7) ;
				limit.set(Calendar.MINUTE, 0) ;
				return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

			case Calendar.SUNDAY :
				limit.add(Calendar.DAY_OF_YEAR, 1) ;
				limit.set(Calendar.HOUR_OF_DAY, 9) ;
				limit.set(Calendar.MINUTE, 0) ;
				return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

			default :
				// data is before 07:00 -> VAZIO
				limit.set(Calendar.HOUR_OF_DAY, 7) ;
				limit.set(Calendar.MINUTE, 0) ;

				if (data.before(limit))
					return new Timetable(Horario.VAZIO, limit, Horario.CHEIO) ;

				// data is between 07:00 and 00:00 next day -> CHEIO
				limit.add(Calendar.DAY_OF_YEAR, 1) ;
				limit.set(Calendar.HOUR_OF_DAY, 0) ;
				limit.set(Calendar.MINUTE, 0) ;
				return new Timetable(Horario.CHEIO, limit, Horario.VAZIO) ;
		}
	}
}