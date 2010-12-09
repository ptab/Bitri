package org.tbrd.bitri ;

import java.text.SimpleDateFormat ;
import java.util.Calendar ;
import java.util.GregorianCalendar ;
import java.util.Locale ;

import org.tbrd.bitri.data.Ciclo ;
import org.tbrd.bitri.data.Constants ;
import org.tbrd.bitri.data.Tarifa ;
import org.tbrd.bitri.data.Timetable ;

import android.app.Activity ;
import android.content.Context ;
import android.content.Intent ;
import android.content.SharedPreferences ;
import android.os.Bundle ;
import android.util.Log ;
import android.view.Menu ;
import android.view.MenuInflater ;
import android.view.MenuItem ;
import android.view.View ;
import android.widget.AdapterView ;
import android.widget.ArrayAdapter ;
import android.widget.ImageView ;
import android.widget.Spinner ;
import android.widget.TextView ;
import android.widget.AdapterView.OnItemSelectedListener ;

public class Bitri extends Activity {
	private Tarifa mTarifa ;
	private Ciclo mCiclo ;
	private Spinner mTarifaSpinner ;
	private Spinner mCicloSpinner ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;
		setContentView(R.layout.bitri) ;

		mTarifaSpinner = (Spinner) findViewById(R.id.spinner_tarifa) ;
		ArrayAdapter<CharSequence> tarifa_adapter = ArrayAdapter.createFromResource(this, R.array.tarifas,
				android.R.layout.simple_spinner_item) ;
		tarifa_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;
		mTarifaSpinner.setAdapter(tarifa_adapter) ;
		mTarifaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				Tarifa new_tarifa = Tarifa.get(position) ;
				if (!new_tarifa.equals(mTarifa)) {
					mTarifa = new_tarifa ;
					savePreferences() ;
					buildLayout() ;
					updateWidget() ;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here ;
			}

		}) ;

		mCicloSpinner = (Spinner) findViewById(R.id.spinner_ciclo) ;
		ArrayAdapter<CharSequence> ciclo_adapter = ArrayAdapter.createFromResource(this, R.array.ciclos,
				android.R.layout.simple_spinner_item) ;
		ciclo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;
		mCicloSpinner.setAdapter(ciclo_adapter) ;
		mCicloSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				Ciclo new_ciclo = Ciclo.get(position) ;
				if (!new_ciclo.equals(mCiclo)) {
					mCiclo = new_ciclo ;
					savePreferences() ;
					buildLayout() ;
					updateWidget() ;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here ;
			}

		}) ;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState) ;
		savePreferences() ;
	}

	@Override
	protected void onPause() {
		super.onPause() ;
		savePreferences() ;
	}

	@Override
	protected void onResume() {
		super.onResume() ;
		restorePreferences() ;
		buildLayout() ;
	}

	private void savePreferences() {
		SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE) ;
		SharedPreferences.Editor editor = preferences.edit() ;
		editor.putInt(Constants.TARIFA_PREF, mTarifa.ordinal()) ;
		editor.putInt(Constants.CICLO_PREF, mCiclo.ordinal()) ;
		editor.commit() ;
	}

	private void restorePreferences() {
		SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE) ;
		mTarifa = Tarifa.get(preferences.getInt(Constants.TARIFA_PREF, Tarifa.BI.ordinal())) ;
		mCiclo = Ciclo.get(preferences.getInt(Constants.CICLO_PREF, Ciclo.SEMANAL.ordinal())) ;
	}

	private void buildLayout() {
		mTarifaSpinner.setSelection(mTarifa.ordinal()) ;
		mCicloSpinner.setSelection(mCiclo.ordinal()) ;

		ImageView horario = (ImageView) findViewById(R.id.horario) ;
		TextView today = (TextView) findViewById(R.id.today) ;
		ImageView img_periodo = (ImageView) findViewById(R.id.img_periodo) ;
		TextView periodo = (TextView) findViewById(R.id.periodo) ;
		TextView next_horario = (TextView) findViewById(R.id.next_horario) ;
		TextView next_change = (TextView) findViewById(R.id.next_change) ;

		GregorianCalendar now = new GregorianCalendar() ;

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy") ;
		String now_formatted = formatter.format(now.getTime()) ;
		today.setText(now_formatted) ;

		if (now.getTimeZone().inDaylightTime(now.getTime())) {
			img_periodo.setImageResource(R.drawable.button_summer) ;
			periodo.setText(R.string.summer) ;
		}
		else {
			img_periodo.setImageResource(R.drawable.button_winter) ;
			periodo.setText(R.string.winter) ;
		}

		Timetable t = calcHorario(now, mTarifa, mCiclo) ;
		horario.setImageResource(t.getHorario().getDrawable()) ;
		next_horario.setText(t.getNextHorario().getText()) ;
		next_horario.setTextColor(getResources().getColor(t.getNextHorario().getColor())) ;
		next_change.setText(getDay(this, now, t.getNextChange())) ;
	}

	private void updateWidget() {
		Intent intent = new Intent(this, BitriWidget.class) ;
		intent.setAction(BitriWidget.WIDGET_UPDATE) ;
		Log.d(this.getClass().getCanonicalName(), "Broadcasting intent with action " + BitriWidget.WIDGET_UPDATE) ;
		sendBroadcast(intent) ;
	}

	/*
	 * Refresh menu
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater() ;
		inflater.inflate(R.menu.menu, menu) ;
		return true ;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh :
				buildLayout() ;
				return true ;
			case R.id.about :
				Intent i = new Intent(this, About.class) ;
				startActivity(i) ;
				return true ;
			default :
				return super.onOptionsItemSelected(item) ;
		}
	}

	/*
	 * Utilities
	 */

	public static Timetable calcHorario(GregorianCalendar date, Tarifa tarifa, Ciclo ciclo) {
		if (date.getTimeZone().inDaylightTime(date.getTime())) {
			if (tarifa.equals(Tarifa.BI))
				if (ciclo.equals(Ciclo.DIARIO))
					return org.tbrd.bitri.data.bi.diario.Calculator.calc(date) ;
				else
					return org.tbrd.bitri.data.bi.semanal.verao.Calculator.calc(date) ;
			else if (ciclo.equals(Ciclo.DIARIO))
				return org.tbrd.bitri.data.tri.diario.verao.Calculator.calc(date) ;
			else
				return org.tbrd.bitri.data.tri.semanal.verao.Calculator.calc(date) ;
		}
		else if (tarifa.equals(Tarifa.BI))
			if (ciclo.equals(Ciclo.DIARIO))
				return org.tbrd.bitri.data.bi.diario.Calculator.calc(date) ;
			else
				return org.tbrd.bitri.data.bi.semanal.inverno.Calculator.calc(date) ;
		else if (ciclo.equals(Ciclo.DIARIO))
			return org.tbrd.bitri.data.tri.diario.inverno.Calculator.calc(date) ;
		else
			return org.tbrd.bitri.data.tri.semanal.inverno.Calculator.calc(date) ;
	}

	public static String getDay(Context context, GregorianCalendar now, GregorianCalendar next) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm") ;
		if (next.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR))
			return context.getString(R.string.today) + ", " + formatter.format(next.getTime()) ;
		else if (next.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) + 1)
			return context.getString(R.string.tomorrow) + ", " + formatter.format(next.getTime()) ;
		else {
			formatter = new SimpleDateFormat("EEEE, HH:mm", new Locale("pt", "PT")) ;
			String date = formatter.format(next.getTime()) ;
			return date.substring(0, 1).toUpperCase() + date.substring(1) ;
		}
	}
}