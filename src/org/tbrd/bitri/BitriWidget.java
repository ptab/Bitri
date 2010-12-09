package org.tbrd.bitri ;

import java.util.GregorianCalendar ;

import org.tbrd.bitri.data.Ciclo ;
import org.tbrd.bitri.data.Constants ;
import org.tbrd.bitri.data.Tarifa ;
import org.tbrd.bitri.data.Timetable ;

import android.app.AlarmManager ;
import android.app.PendingIntent ;
import android.app.Service ;
import android.appwidget.AppWidgetManager ;
import android.appwidget.AppWidgetProvider ;
import android.content.ComponentName ;
import android.content.Context ;
import android.content.Intent ;
import android.content.SharedPreferences ;
import android.widget.RemoteViews ;

public class BitriWidget extends AppWidgetProvider {
	public static final String WIDGET_UPDATE = BitriWidget.class.getPackage().getName() + ".WIDGET_UPDATE" ;

	private Timetable mNextChange ;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Log.d(this.getClass().getCanonicalName(), "Received intent with action " + intent.getAction()) ;
		super.onReceive(context, intent) ;

		if (WIDGET_UPDATE.equals(intent.getAction())) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context) ;
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BitriWidget.class)) ;
			update(context, appWidgetManager, appWidgetIds) ;
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds) ;
		update(context, appWidgetManager, appWidgetIds) ;
	}

	@Override
	public void onDisabled(Context context) {
		cancelAlarm(context) ;
		super.onDisabled(context) ;
	}

	private void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i : appWidgetIds) {
			// Log.d(this.getClass().getCanonicalName(), "Updating widget " + i) ;
			RemoteViews updatedViews = buildLayout(context) ;
			Intent intent = new Intent(context, Bitri.class) ;
			PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0) ;
			updatedViews.setOnClickPendingIntent(R.id.layout_widget, pending) ;
			appWidgetManager.updateAppWidget(i, updatedViews) ;
		}

		if (appWidgetIds.length > 0)
			setAlarm(context) ;
	}

	private void setAlarm(Context context) {
		Intent intent = new Intent(context, BitriWidget.class) ;
		intent.setAction(WIDGET_UPDATE) ;
		PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, 0) ;

		GregorianCalendar date = mNextChange.getNextChange() ;

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE) ;
		alarmManager.set(AlarmManager.RTC, date.getTimeInMillis(), pending_intent) ;

		// SimpleDateFormat formatter = new SimpleDateFormat("EEEE, HH:mm") ;
		// Log.d(this.getClass().getName(), "Alarm set for " + formatter.format(date.getTime())) ;
	}

	private void cancelAlarm(Context context) {
		Intent intent = new Intent(context, BitriWidget.class) ;
		intent.setAction(WIDGET_UPDATE) ;
		PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, 0) ;

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE) ;
		alarmManager.cancel(pending_intent) ;
		// Log.d(this.getClass().getName(), "Cancelled all alarms") ;
	}

	private RemoteViews buildLayout(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget) ;

		SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE) ;
		Tarifa mTarifa = Tarifa.get(settings.getInt(Constants.TARIFA_PREF, Tarifa.BI.ordinal())) ;
		Ciclo mCiclo = Ciclo.get(settings.getInt(Constants.CICLO_PREF, Ciclo.SEMANAL.ordinal())) ;

		GregorianCalendar now = new GregorianCalendar() ;

		mNextChange = Bitri.calcHorario(now, mTarifa, mCiclo) ;
		views.setImageViewResource(R.id.widget_horario, mNextChange.getHorario().getWidgetDrawable()) ;
		views.setTextViewText(R.id.widget_next_horario, context.getString(mNextChange.getNextHorario().getText())) ;
		views.setTextColor(R.id.widget_next_horario, context.getResources().getColor(
				mNextChange.getNextHorario().getColor())) ;

		views.setTextViewText(R.id.widget_next_date, Bitri.getDay(context, now, mNextChange.getNextChange())) ;
		return views ;
	}
}