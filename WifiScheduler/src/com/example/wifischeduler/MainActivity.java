package com.example.wifischeduler;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static Switch scheduleView;
	public static Switch schedulerStatusButton;
	public static TextView showTime;
	public static Calendar scheduleTime = Calendar.getInstance();
	public static Switch cancel;
	public static Calendar systemTime = Calendar.getInstance();
	public static View cancelView;
	public static String touch = null;
	public static DatabaseOperations dbOp = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scheduleView = (Switch) findViewById(R.id.scheduleView);
		showTime = (TextView) findViewById(R.id.showTime);
		schedulerStatusButton = (Switch) findViewById(R.id.schedulerStatus);
		cancel = (Switch) findViewById(R.id.cancel);
		touch = (String) getResources().getString(R.string.text_touch);
		cancelView = (View) findViewById(R.id.cancelView);
		schedulerStatusButton.setEnabled(false);

		//DB operations
		dbOp = new DatabaseOperations(getApplicationContext());
		dbOp.getWritableDatabase();
		
		
		schedulerStatusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(schedulerStatusButton.isChecked()){
					dbOp.updateValues(1, WiFiOperations.time);
					
				}else{
					dbOp.updateValues(0, WiFiOperations.time);
				}
			}
		});
		
		scheduleView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				View timerLayout = findViewById(R.id.timerLayout);
				// TODO Auto-generated method stub
			   if(scheduleView.isChecked()){
				   timerLayout.setVisibility(View.VISIBLE);
			   }else{
                   timerLayout.setVisibility(View.INVISIBLE); 
			   }
			}
		}); 
		
		showTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogFragment newFragment = new ShowTimePicker();
			    newFragment.show(getFragmentManager(), "timePicker");
				Log.d(USER_SERVICE, "in show time after");
			}
		});
		
		cancel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(cancel.isChecked()){
					if(!touch.equalsIgnoreCase((String) showTime.getText())){
						WiFiOperations wo = new WiFiOperations();
						wo.cancelAlarm(getApplicationContext());
						showTime.setText(touch);
						showTime.setGravity(Gravity.CENTER);
						showTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
						schedulerStatusButton.setChecked(false);
						schedulerStatusButton.setEnabled(false);
						cancel.setChecked(false);
						cancel.setTextOff("Off");
						cancelView.setVisibility(View.INVISIBLE);
						showToasts("Schedule Cancelled", getApplicationContext());
					}
				}else{
					cancelView.setVisibility(View.INVISIBLE);
				}
				
			}
		});
	}
	
	@SuppressLint("NewApi")
	public void setTextBox(String time){
		showTime.setText(time);
		showTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55);
		showTime.setGravity(Gravity.CENTER);
		
		if(ShowTimePicker.hasJellyBeanAndAbove())
		  showTime.setTextAlignment(4);
		
		Log.d("BroadCast", "Edit text set");
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		//showVariableStatus("onResume");
		if(DatabaseOperations.getSchedulerState()){
                 setTextBox(DatabaseOperations.getTime()); 			
				 scheduleView.setChecked(false);
				 schedulerStatusButton.setChecked(false);
				 
		} else {
				 scheduleView.setChecked(scheduleView.isChecked());
		}
	}

	public void showToasts(String message, Context context){
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void setSchedulerProcess(Context context, int hourOfDay, int minute){
		WiFiOperations.cal = Calendar.getInstance();
		 if(hourOfDay < systemTime.get(Calendar.HOUR_OF_DAY) || minute < systemTime.get(Calendar.MINUTE)){
			 Log.d(USER_SERVICE, "Day increased ");
	        	WiFiOperations.cal.set(Calendar.DATE, systemTime.get(Calendar.DATE) + 1);
	        }
	        
	        WiFiOperations.cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
	        WiFiOperations.cal.set(Calendar.MINUTE, minute);
	        WiFiOperations.cal.set(Calendar.SECOND, 0);
	        WiFiOperations.cal.set(Calendar.MILLISECOND, 0);
	        Log.d("BroadCast", "" + WiFiOperations.cal.get(Calendar.HOUR_OF_DAY) + " --- " + WiFiOperations.cal.get(Calendar.MINUTE));
	        WiFiOperations wifiOp = new WiFiOperations();
    		wifiOp.setAlarm(context.getApplicationContext());
		}
	
	private void showVariableStatus(String method){
		Log.d(USER_SERVICE, "in "+method+" ");
		if(WiFiOperations.schedulerStatus){
			Toast toast1 = Toast.makeText(MainActivity.this, "in "+method+" - VARIABLE", Toast.LENGTH_SHORT);
			toast1.show();	
		}else{
			Toast toast1 = Toast.makeText(MainActivity.this, "in "+method+" - NO VARIABLE", Toast.LENGTH_SHORT);
			toast1.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		showVariableStatus("onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		showVariableStatus("onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		showVariableStatus("onRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		showVariableStatus("onStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		showVariableStatus("onStop");
	}

}
