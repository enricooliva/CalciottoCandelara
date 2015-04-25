package com.enrico.calciottocandelara;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;





public class Utility extends AsyncTask<String,Object,String>  {

	String result = "";
	private IAction callback;
	//the year data to send
//	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//	nameValuePairs.add(new BasicNameValuePair("year","1980"));
	public interface IAction
	{
		void postAction(String result);	
	}
	
			
	public Utility(IAction postAction) {
	
		this.callback = postAction;
	}
	
		
	public String MadeUpdateRate(String posturl, String number, String rate, String votenumber )
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("rate",String.valueOf(rate)));
		nameValuePairs.add(new BasicNameValuePair("numbervote",String.valueOf(votenumber)));
		nameValuePairs.add(new BasicNameValuePair("number",number));

		return MadePostQuery(posturl, nameValuePairs);
	}
	
	public String MadePostQuery(String posturl, ArrayList<NameValuePair> nameValuePairs)
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
		        Log.e("log_tag", "Error converting result "+e.toString());
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
	
	@Override
    protected void onPostExecute(String result) {
    
		if (callback!=null)
			callback.postAction(result);    	
    }


	
	@Override
	protected String doInBackground(String... params) {
	
		if (params.length>1){
			return MadeUpdateRate(params[0], params[1],params[2], params[3]);
		}else{
			return MadeGetQuery(params[0]);
		}
	}
	
	
}
