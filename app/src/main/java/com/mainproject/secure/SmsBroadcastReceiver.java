package com.mainproject.secure;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HP on 11-03-2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {



    private static final String TAG = "SmsBroadcastReceiver";

    private Listener listener;

    public SmsBroadcastReceiver() {

    }

    GPSTracker gps;
    double latitude,longitude;


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {



        gps = new GPSTracker(context);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            } else {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }


            if (smsBody.startsWith("Lost")) {
                if (listener != null) {
                    listener.onTextReceived(smsBody);
                    Log.e(TAG, "SMS RECEIVED");
                }

                Intent front_translucent = new Intent(context, CameraActivity.class);
                front_translucent.putExtra("Front_Request", true);
                front_translucent.putExtra("Quality_Mode", 100);
                context.startService(
                        front_translucent);

                Toast.makeText(context, smsBody, Toast.LENGTH_SHORT).show();
                SmsManager.getDefault().sendTextMessage(smsSender, null, "Your Phone is at https://www.google.com/maps/?q="+latitude+","+longitude, null, null);


                sentMail();
            }


        }

    }

    public void sentMail() {


        try


        {


            LongOperation l=new LongOperation();


            l.execute();  //sends the email in background





        } catch (Exception e) {


            Log.e("SendMail", e.getMessage(), e);


        }


    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onTextReceived(String text);
    }

}


