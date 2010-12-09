package org.tbrd.bitri.data ;

import java.util.EnumSet ;
import java.util.HashMap ;
import java.util.Map ;

public enum Ciclo {
	DIARIO(0), SEMANAL(1) ;

	private static final Map<Integer, Ciclo> lookup = new HashMap<Integer, Ciclo>() ;

	static {
		for (Ciclo s : EnumSet.allOf(Ciclo.class))
			lookup.put(s.getCode(), s) ;
	}

	private int code ;

	private Ciclo(int code) {
		this.code = code ;
	}

	public int getCode() {
		return code ;
	}

	public static Ciclo get(int code) {
		return lookup.get(code) ;
	}

}
