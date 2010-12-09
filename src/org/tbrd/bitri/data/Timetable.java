package org.tbrd.bitri.data ;

import java.util.GregorianCalendar ;

public class Timetable {
	private Horario horario ;

	private GregorianCalendar nextChange ;
	private Horario nextHorario ;

	public Timetable(Horario horario, GregorianCalendar next_change, Horario next_horario) {
		setHorario(horario) ;
		setNextChange(next_change) ;
		setNextHorario(next_horario) ;
	}

	public Horario getHorario() {
		return horario ;
	}

	public void setHorario(Horario horario) {
		this.horario = horario ;
	}

	public GregorianCalendar getNextChange() {
		return nextChange ;
	}

	public void setNextChange(GregorianCalendar nextChange) {
		this.nextChange = nextChange ;
	}

	public Horario getNextHorario() {
		return nextHorario ;
	}

	public void setNextHorario(Horario nextHorario) {
		this.nextHorario = nextHorario ;
	}
}