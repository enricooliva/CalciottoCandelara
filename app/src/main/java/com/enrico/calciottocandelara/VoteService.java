package com.enrico.calciottocandelara;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.enrico.calciottocandelara.data.DataContract;
import com.enrico.calciottocandelara.sync.SyncUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Enrico on 25/04/2015.
 */
public class VoteService extends IntentService {

    public static final String PLAYER = "player";
    public static String NUMBER = "number";
    public static String RATE = "rate";
    public static String NUMBERVOTE = "numbervote";

    private final String LOG_TAG = VoteService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public VoteService() {
        super("VoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(LOG_TAG, "Start the service vote");


        int number = intent.getIntExtra(VoteService.NUMBER,999);
        float rate = intent.getFloatExtra(VoteService.RATE, 999);
        int numbervote = intent.getIntExtra(VoteService.NUMBERVOTE, 999);

        if (number!=999 && numbervote!=999 && rate != 999 ) {

            ContentValues values = new ContentValues();
            values.put(DataContract.PlayerEntry.COLUMN_RATE, rate);
            values.put(DataContract.PlayerEntry.COLUMN_NUMBER_VOTE, numbervote);

            int rowsUpdated = getApplicationContext().getContentResolver().update(DataContract.PlayerEntry.buildPlayerUri(number)
                    , values
                    , ""
                    , null);

            //query di aggiornamento
            //new Utility(null).execute(new String[]{"http://www.enricooliva.com/setRateValue.php",
            //        String.valueOf(number), String.format("%.02f", rate), String.valueOf(numbervote)});

            MadeUpdateRate("http://www.enricooliva.com/setRateValue.php",number,rate,numbervote);

            //richiamo la sincronizzazione con il db
            SyncUtils.TriggerRefresh();
        }
    }


    public void MadeUpdateRate(String posturl, int number, float rate, int votenumber )
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("rate",String.valueOf(rate)));
        nameValuePairs.add(new BasicNameValuePair("numbervote",String.valueOf(votenumber)));
        nameValuePairs.add(new BasicNameValuePair("number",String.valueOf(number)));

        MadePostQuery(posturl, nameValuePairs);
    }

    public void MadePostQuery(String posturl, ArrayList<NameValuePair> nameValuePairs)
    {
        //the year data to send
        //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        InputStream is=null;

        //http post
        try{
            //http://www.enricooliva.com/getAllPlayers.php
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(posturl);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            //HttpEntity entity = response.getEntity();

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }


    }
}
