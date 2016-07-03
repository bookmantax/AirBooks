package com.airbooks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by RodrigoE on 6/24/2016.
 *
 * TODO: http://www.accella.net/knowledgebase/scheduling-code-execution-on-android/
 *
 * setRepeating
 * Added in API level 1
 * void setRepeating (int type,
 * long triggerAtMillis,
 * long intervalMillis,
 * PendingIntent operation)
 *
 * Schedule a repeating alarm.
 *
 * Note:
 * for timing operations (ticks, timeouts, etc) it is easier and much more efficient to use Handler.
 * If there is already an alarm scheduled for the same IntentSender, it will first be canceled.
 * Like set(int, long, PendingIntent), except you can also supply a period at which the alarm will automatically repeat.
 * This alarm continues repeating until explicitly removed with cancel(AlarmManager.OnAlarmListener).
 * If the stated trigger time is in the past, the alarm will be triggered immediately,
 * with an alarm count depending on how far in the past the trigger time is relative to the repeat interval.
 * If an alarm is delayed (by system sleep, for example, for non _WAKEUP alarm types),
 * a skipped repeat will be delivered as soon as possible.
 * After that, future alarms will be delivered according to the original schedule;
 * they do not drift over time. For example, if you have set a recurring alarm for the top of every hour but
 * the phone was asleep from 7:45 until 8:45, an alarm will be sent as soon as the phone awakens,
 * then the next alarm will be sent at 9:00.
 * If your application wants to allow the delivery times to drift in order to guarantee that at least a certain time
 * interval always elapses between alarms, then the approach to take is to use one-time alarms,
 * scheduling the next one yourself when handling each alarm delivery.
 *
 * Note: as of API 19, all repeating alarms are inexact.
 * If your application needs precise delivery times then it must use one-time exact alarms,
 * rescheduling each time as described above.
 * Legacy applications whose targetSdkVersion is earlier than API 19 will continue to have all of their alarms,
 * including repeating alarms, treated as exact.
 *
 * Parameters
 * type	int: One of ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC, or RTC_WAKEUP.
 * triggerAtMillis	long: time in milliseconds that the alarm should first go off, using the appropriate clock
 * (depending on the alarm type).
 * intervalMillis	long: interval in milliseconds between subsequent repeats of the alarm.
 * operation	PendingIntent: Action to perform when the alarm goes off; typically comes from IntentSender.getBroadcast().
 *
 */
public class AlarmHandler extends Activity {

    // Variables
    Long scheduledTime = Long.valueOf(81420000);
    Long currentTime = new GregorianCalendar().getTimeInMillis();
    Long time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_test);
    }

    public void scheduleAlarm(View V) {
        // Time at which alarm will be scheduled here alarm is scheduled at 23:59,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
        // 23*59*60*1000 = 81420000
//        Long time = new GregorianCalendar().getTimeInMillis() + 24 * 60 * 60 * 1000;

            time = (currentTime + (scheduledTime - currentTime));


//        Long scheduleTime = Long.valueOf(12*59*60*1000);
//        Long time = null;
//        for (int i = 0; i == 0; i++) {
//            if (i == 0){
//                Long difference = scheduleTime - new GregorianCalendar().getTimeInMillis();
//                time = new GregorianCalendar().getTimeInMillis() + difference;
//            } else {
//                time = new GregorianCalendar().getTimeInMillis() + 24*60*60*1000;
//            }
//


        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(this, AlarmReciever.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
//        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setInexactRepeating(0,time,24*60*60*1000,PendingIntent.getBroadcast(
                this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, "Alarm Scheduled for " + time, Toast.LENGTH_LONG).show();
  }

    public void setScheduledAlarm() {
        time = (currentTime + (scheduledTime - currentTime));
        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(0,time,24*60*60*1000,PendingIntent.getBroadcast(
                this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, "Alarm Scheduled for " + time, Toast.LENGTH_LONG).show();
    }

}