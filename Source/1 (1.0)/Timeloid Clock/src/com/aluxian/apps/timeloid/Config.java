package com.aluxian.apps.timeloid;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;

public class Config extends SherlockActivity {
	private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private String flavour = "grapefruit";
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		setContentView(R.layout.config);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flavours, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner spinner = (Spinner) findViewById(R.id.flavours);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	 flavour = parentView.getItemAtPosition(position).toString().toLowerCase();
		    	 widgetPreview();
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {}
		});
		
		findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor myPrefEditor = myPref.edit();
				
				myPrefEditor.putString(widgetId + "_FLAVOUR", flavour);
				myPrefEditor.commit();
				
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
				setResult(RESULT_OK, resultValue);
				finish();
			}
		});

		Calendar calendar = Calendar.getInstance();
 		calendar.setTimeInMillis(System.currentTimeMillis());
 		calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 1));
 		
 		widgetPreview();
		
 		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						widgetPreview();
				     }
				});
			}
		}, calendar.getTimeInMillis() - System.currentTimeMillis(), 1000);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

		if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
			finish();
	}
	
	private void widgetPreview() {
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
		
		try {
			ImageView hView = (ImageView) findViewById(R.id.h);
	 		hView.setImageResource(R.drawable.class.getField(flavour + "_h" + h).getInt(null));
	 		
	 		ImageView mView = (ImageView) findViewById(R.id.m);
	 		mView.setImageResource(R.drawable.class.getField(flavour + "_m" + m).getInt(null));
	 		
	 		ImageView mmView = (ImageView) findViewById(R.id.mm);
	 		mmView.setImageResource(R.drawable.class.getField(flavour + "_mm" + mm).getInt(null));
	 		
	 		ImageView shadowView = (ImageView) findViewById(R.id.shadow);
	 		shadowView.setImageResource(R.drawable.class.getField(flavour + "_shadow").getInt(null));
	 		
	 		ImageView sView = (ImageView) findViewById(R.id.s);
	 		sView.setImageResource(R.drawable.class.getField(flavour + "_s" + s).getInt(null));
		} catch (Exception e) {}
	}
}