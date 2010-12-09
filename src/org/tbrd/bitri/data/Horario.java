package org.tbrd.bitri.data ;

import org.tbrd.bitri.R ;

public enum Horario {
	VAZIO(R.drawable.clock_vazio, R.drawable.widget_clock_vazio, R.string.vazio, R.color.vazio), CHEIO(
			R.drawable.clock_cheio, R.drawable.widget_clock_cheio, R.string.cheio, R.color.cheio), PONTA(
			R.drawable.clock_ponta, R.drawable.widget_clock_ponta, R.string.ponta, R.color.ponta) ;

	private int drawable ;
	private int widgetDrawable ;
	private int text ;
	private int color ;

	private Horario(int drawable, int widget_drawable, int text, int color) {
		this.drawable = drawable ;
		this.text = text ;
		this.color = color ;
		widgetDrawable = widget_drawable ;
	}

	public int getDrawable() {
		return drawable ;
	}

	public int getText() {
		return text ;
	}

	public int getColor() {
		return color ;
	}

	public int getWidgetDrawable() {
		return widgetDrawable ;
	}

}