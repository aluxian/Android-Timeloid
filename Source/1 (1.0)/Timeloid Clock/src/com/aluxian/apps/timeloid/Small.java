package com.aluxian.apps.timeloid;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class Small extends AppWidgetProvider {
	private static String WIDGET_UPDATE = "com.aluxian.apps.timeloid.SMALL_WIDGET_UPDATE";
	
	private PendingIntent clockTickIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(WIDGET_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private AlarmManager alarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		updateAppWidget(context, appWidgetManager, appWidgetIds);
	}
	
	@Override 
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
		Calendar calendar = Calendar.getInstance();
 		calendar.setTimeInMillis(System.currentTimeMillis());
 		calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 1));
		
		alarmManager(context).setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000, clockTickIntent(context));
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		
		alarmManager(context).cancel(clockTickIntent(context));
		PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		if (WIDGET_UPDATE.equals(intent.getAction())) {
		    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), getClass().getName())));
		}
	}

	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		int h = calendar.get(Calendar.HOUR);
		int s = calendar.get(Calendar.SECOND);
		int min = calendar.get(Calendar.MINUTE);
		int m = min / 10;
		int mm = min % 10;
		
		int h_24 = calendar.get(Calendar.HOUR_OF_DAY);
		if (h_24 == 0)
			h = 0;
		else if (h_24 == 12)
			h = 12;
		
		SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.small);
		
		for (int i = 0; i < appWidgetIds.length; i++) {
			String flavour = myPref.getString(appWidgetIds[i] + "_FLAVOUR", "grapefruit");
			
			try {
				remoteViews.setImageViewResource(R.id.h, R.drawable.class.getField(flavour + "_h" + h).getInt(null));
				remoteViews.setImageViewResource(R.id.m, R.drawable.class.getField(flavour + "_m" + m).getInt(null));
				remoteViews.setImageViewResource(R.id.mm, R.drawable.class.getField(flavour + "_mm" + mm).getInt(null));
				remoteViews.setImageViewResource(R.id.shadow, R.drawable.class.getField(flavour + "_shadow").getInt(null));
				remoteViews.setImageViewResource(R.id.s, R.drawable.class.getField(flavour + "_s" + s).getInt(null));
			} catch (Exception e) {}
			
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
	}
}