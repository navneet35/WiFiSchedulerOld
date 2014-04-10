package com.example.wifischeduler;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WiFiOperations extends BroadcastReceiver{

	private AlarmManager alarmMgr;
	private int requestCode = 123;
	public static boolean schedulerStatus = false;
	
	public static Calendar cal = Calendar.getInstance();
	public static String time = "";
	
	public void onReceive(Context context, Intent intent) {
		Log.d("BroadCast", "in on receive");
		if(DatabaseOperations.getSchedulerState()){
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			MainActivity ma = new MainActivity();
			if(wm.isWifiEnabled()){
				 wm.setWifiEnabled(false);
				 ma.showToasts("WiFi disabled", context);
			 }else{
				 ma.showToasts("WiFi is already disabled", context);
			 }
			
			MainActivity.dbOp.updateValues(0, MainActivity.touch);
	    }
    }

	public void setAlarm(Context context){
		Log.d("Broadcast", "time set =");
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
	 	Intent intent = new Intent(context, WiFiOperations.class);
	  	PendingIntent pI = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	  	
	  	long sleepTime = (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
	  	Log.d("Broadcast", "time set == " + sleepTime);
	  	alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + sleepTime, pI);
	  	changesAfterScheduleSet();
	}
	
	@SuppressLint("NewApi")
	private void changesAfterScheduleSet(){
		String time = WiFiOperations.cal.get(Calendar.HOUR_OF_DAY) + ":" + WiFiOperations.cal.get(Calendar.MINUTE);
	  	MainActivity.dbOp.updateValues(1, time);
	  	MainActivity wsma = new MainActivity();
	  	MainActivity.scheduleView.setChecked(false);
	  	wsma.setTextBox(time);
	  	MainActivity.cancelView.setVisibility(View.VISIBLE);
		MainActivity.schedulerStatusButton.setEnabled(true);
	}
	
	public void cancelAlarm(Context context){
	  	MainActivity.dbOp.updateValues(0, MainActivity.touch);
        Intent intent = new Intent(context, WiFiOperations.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
