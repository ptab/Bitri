package org.tbrd.bitri.data ;

import java.util.EnumSet ;
import java.util.HashMap ;
import java.util.Map ;

public enum Tarifa {
	BI(0), TRI(1) ;

	private static final Map<Integer, Tarifa> lookup = new HashMap<Integer, Tarifa>() ;

	static {
		for (Tarifa s : EnumSet.allOf(Tarifa.class))
			lookup.put(s.getCode(), s) ;
	}

	private int code ;

	private Tarifa(int code) {
		this.code = code ;
	}

	public int getCode() {
		return code ;
	}

	public static Tarifa get(int code) {
		return lookup.get(code) ;
	}

}
