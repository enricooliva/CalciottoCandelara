package com.enrico.calciottocandelara;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.enrico.calciottocandelara.data.DataContract.PlayerEntry;
import com.enrico.calciottocandelara.data.Player;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Enrico on 06/04/2015.
 */
public class FetchPlayerTask extends AsyncTask<String,Void,Void>  {

    private final Context mContext;
    String result = "";
    private final static String LOG_TAG = "FetchPlayerTask";

    public FetchPlayerTask(Context context){
        mContext = context;
    }

    private String getResult(InputStream is)
    {
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result=sb.toString();
        }catch(Exception e){
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        return result;
    }

    public String MadeGetQuery(String posturl)
    {
        InputStream is=null;

        //http post
        try{
            //http://www.enricooliva.com/getAllPlayers.php
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost(posturl);
            HttpGet httpget = new HttpGet(posturl);
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        return this.getResult(is);

    }

    public static ArrayList<Player> getPlayerList(String players)
    {
        ArrayList<Player> playerlist = new ArrayList<Player>();
        //parse json data
        try{
            JSONArray jArray = new JSONArray(players);
            for(int i=0;i<jArray.length();i++){
                playerlist.add(new Player( jArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());

        }

        return playerlist;
    }

    @Override
    protected Void doInBackground(String... params) {
        String result = MadeGetQuery("http://www.enricooliva.com/getAllPlayers.php");
        List<Player> playerList = getPlayerList(result);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(playerList.size());

        for (Player p : playerList){

            ContentValues playerValues = new ContentValues();
            playerValues.put(PlayerEntry.COLUMN_NAME, p.getName());
            playerValues.put(PlayerEntry.COLUMN_NUMBER, p.getNumber());
            playerValues.put(PlayerEntry.COLUMN_NUMBER_VOTE, p.getNumberVotes());
            playerValues.put(PlayerEntry.COLUMN_RATE, p.getRate());
            playerValues.put(PlayerEntry.COLUMN_ROLE, p.getRole());
            cVVector.add(playerValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            int rowsDeleted = mContext.getContentResolver().delete(PlayerEntry.CONTENT_URI,"",null);
            int rowsInserted =  mContext.getContentResolver().bulkInsert(PlayerEntry.CONTENT_URI, cvArray);

            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of weather data");
            Log.v(LOG_TAG, "deleted " + rowsDeleted + " rows of weather data");
        }


        return null;
    }


}


