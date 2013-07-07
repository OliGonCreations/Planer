package com.kaith.planer;

import java.util.Calendar;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class EventsUpdateService extends Service {

	DatabaseHandler db;
	Calendar calendar = Calendar.getInstance();
	int btId, day;
	String[] newEvent = new String[2];

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		db = new DatabaseHandler(this);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		buildUpdate();
		return super.onStartCommand(intent, flags, startId);
	}

	private void buildUpdate() {
		day = calendar.get(Calendar.DAY_OF_WEEK);
		switch (calendar.get(Calendar.HOUR_OF_DAY) + 1) {
		case 7:
			btId = R.id.bt1;
			break;
		case 8:
			btId = R.id.bt2;
			break;
		case 9:
			btId = R.id.bt3;
			break;
		case 10:
			btId = R.id.bt4;
			break;
		case 11:
			btId = R.id.bt5;
			break;
		case 12:
			btId = R.id.bt6;
			break;
		case 13:
			btId = R.id.bt7;
			break;
		case 14:
			btId = R.id.bt8;
			break;
		case 15:
			btId = R.id.bt9;
			break;
		case 16:
			btId = R.id.bt10;
			break;
		case 17:
			btId = R.id.bt11;
			break;
		case 18:
			btId = R.id.bt12;
			break;
		case 19:
			btId = R.id.bt13;
			break;
		case 20:
			btId = R.id.bt14;
			break;
		default:
			break;
		}
		newEvent = db.getEvent(btId, day);
		String title = getResources().getString(R.string.widget_tvEmpty);
		if (newEvent[0] != null && !newEvent[0].isEmpty())
			title = newEvent[0];

		RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget);
		view.setTextViewText(R.id.widget_tvText, title);
		view.setTextViewText(R.id.widget_tvDescription, newEvent[1]);

		ComponentName thisWidget = new ComponentName(this, AppWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, view);
	}

}
