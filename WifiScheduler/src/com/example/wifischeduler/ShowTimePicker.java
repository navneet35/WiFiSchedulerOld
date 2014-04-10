package com.example.wifischeduler;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class ShowTimePicker extends DialogFragment
                            implements TimePickerDialog.OnTimeSetListener {
	
	private boolean setTime = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog myTimePicker =  new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        
        if(hasJellyBeanAndAbove()){
        	myTimePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setTime = true;
					Log.d("BroadCast", "In Set Positive");
				}
			});
        	
        	myTimePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setTime = false;
					Log.d("BroadCast", "In Cancel");
				}
			});
        }else{
        	setTime = true;
        }
        return myTimePicker;
    }

    @SuppressLint("NewApi")
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    	if(setTime){
    		MainActivity ma = new MainActivity();
    		ma.setSchedulerProcess(getActivity(), hourOfDay, minute);
    		MainActivity.schedulerStatusButton.setChecked(true);
    		ma.showToasts("Schedule Set", getActivity());
    		Log.d("BroadCast", "In time Set");
    	}
    }
    
    public  static boolean hasJellyBeanAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

}