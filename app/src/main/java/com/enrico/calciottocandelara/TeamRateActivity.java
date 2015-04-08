package com.enrico.calciottocandelara;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;

import com.enrico.calciottocandelara.data.Player;

import java.util.ArrayList;
import java.util.Iterator;


public class TeamRateActivity extends ActionBarActivity implements Utility.IAction {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
    private RatingBar teamIndex;
    private ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_rate);

        teamIndex = (RatingBar)findViewById(R.id.teamRate);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED  ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
            Toast.makeText(this, "Connessione dati assente", Toast.LENGTH_LONG).show();

        }
        new Utility(this).execute(new String[]{"http://www.enricooliva.com/getAllPlayers.php"});


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_rate, menu);
        menu.add(Menu.NONE,CalciottoCandelaraStartPage.INFO, Menu.NONE, R.string.menuitem_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch	(item.getItemId())
        {
            case  CalciottoCandelaraStartPage.INFO:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Info Calc8 Candelara");
                alert.setMessage(""
                        + "Calc8 Candelara \n\n  "
                        + "L'indice è ricavato dalla media \n"
                        + "dei voti dei singoli giocatori!"
                        + "\n\nhttp://www.facebook.com/candelara.calciotto");
                alert.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void postAction(String result) {

        playerList = FetchPlayerTask.getPlayerList(result);
        updateTeamIndex();
    }

    public void updateTeamIndex()
    {
        if (playerList==null)
            return;
        if (playerList.size()<=0)
            return;

        double index=0;
        double num=0;
        for (Iterator iterator = playerList.iterator(); iterator.hasNext();) {
            Player type = (Player) iterator.next();
            if (type.getRate()>0)
            {
                num = num +1;
                index = index + type.getRate();
            }
        }

        float teamindex = (float) (index / num);
        teamIndex.setRating(teamindex);

    }
}
