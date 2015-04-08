package com.enrico.calciottocandelara.data;

import android.database.Cursor;
import com.enrico.calciottocandelara.data.DataContract.PlayerEntry;
import com.enrico.calciottocandelara.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

public class Player {
	
	private String name;
	private String role;
	private Date born;
	private int number;
	private double rate;
	private int numbervote;

    public Player(Cursor cursor) {

        name = cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_NAME));
        role = cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_ROLE));
        number = cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_NUMBER));
        rate = cursor.getDouble(cursor.getColumnIndex(PlayerEntry.COLUMN_RATE));
        numbervote = cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_NUMBER_VOTE));

    }


    public Player(JSONObject json_data)
	{
		
		try {
			name=json_data.getString("Name");
			role=json_data.getString("Role");
			number=json_data.getInt("Number");
			//born=  Date.valueOf(json_data.getString("Born"));
			rate=json_data.getDouble("Rate");
			numbervote=json_data.getInt("NumberVote");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public String getName()
	{
		return name;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public Date getBorn()
	{
		return born;
	}
	public double getRate()
	{
		return rate;
	}
	
	public void addRate(float rating) {
		// TODO Auto-generated method stub
		//incrementare numero dei voti
		//sommo il voto alla media dei voti precedenti
		numbervote +=1;		
		rate= ((rate*(numbervote-1))+rating) / numbervote;
	}
	
	public void updateDatabase()
	{
		new Utility(null).execute(new String[]{"http://www.enricooliva.com/setRateValue.php",String.valueOf(number),String.format("%.02f", rate),String.valueOf(numbervote)});
	}

	public int getNumberVotes() {
		// TODO Auto-generated method stub
		return numbervote;
	}

}
