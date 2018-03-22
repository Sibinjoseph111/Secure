package com.mainproject.secure;

/**
 * Created by HP on 22-03-2018.
 */

import android.os.AsyncTask;


import android.util.Log;





/**


 * Created by GsolC on 2/24/2017.


 */





public class LongOperation extends AsyncTask<Void, Void, String> {


    @Override


    protected String doInBackground(Void... params) {


        try {




            GMailSender sender = new GMailSender("sibinjoseph111@gmail.com", "lagoonblue12345");


            sender.sendMail("This is a testing mail",


                    "This is Body of testing mail","sibinjoseph111@gmail.com",


                    "sibinjoseph111@gmail.com,sibin1000@gmail.com")                   ;





        } catch (Exception e) {


            Log.e("error", e.getMessage(), e);


            return "Email Not Sent";


        }


        return "Email Sent";


    }





    @Override


    protected void onPostExecute(String result) {





        Log.e("LongOperation",result+"");


    }





    @Override


    protected void onPreExecute() {


    }





    @Override


    protected void onProgressUpdate(Void... values) {


    }


}
