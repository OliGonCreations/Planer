package com.kaith.planer;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

	private PendingIntent service = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		final Calendar TIME = Calendar.getInstance();
		TIME.set(Calendar.MINUTE, 0);
		TIME.set(Calendar.SECOND, 0);
		TIME.set(Calendar.MILLISECOND, 0);

		final Intent intent = new Intent(context, EventsUpdateService.class);

		if (service == null) {
			service = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		}

		m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), AlarmManager.INTERVAL_HOUR, service);
		
		for (int i = 0; i < appWidgetIds.length; i++) {
	        int appWidgetId = appWidgetIds[i];

	        Intent intent1 = new Intent(context, MainActivity.class);
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

	        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
	        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
	        appWidgetManager.updateAppWidget(appWidgetId, views);
	    }
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		m.cancel(service);
		super.onDisabled(context);
	}

}
