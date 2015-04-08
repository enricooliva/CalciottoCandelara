package com.enrico.calciottocandelara;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;


public class CalciottoCandelaraStartPage extends ActionBarActivity {

    private static final int PLAYERS_ID = 0;
    private static final int GAMES_ID = 1;
    private static final int TEAM_ID = 2;
    public static final int INFO=3;
    private RatingBar teamIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calciotto_candelara_start_page);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calciotto_candelara_start_page, menu);

        menu.add(Menu.NONE,PLAYERS_ID, Menu.NONE, R.string.menuitem_palyers);
        menu.add(Menu.NONE,GAMES_ID, Menu.NONE, R.string.menuitem_games);
        menu.add(Menu.NONE,TEAM_ID, Menu.NONE, R.string.menuitem_team);
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
            case PLAYERS_ID:
                showPlayersList();
                return true;

            case GAMES_ID:
                showGamesList();
                return true;

            case TEAM_ID:
                showRateTeam();
                return true;
            case  INFO:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Info Calc8 Candelara");
                alert.setMessage(""
                        + "Calc8 Candelara \n\n"
                        + "Vota le prestazioni dei giocatori \n"
                        + "e guarda l'indicatore dello stato di forma della squadra."
                        + "\n\nhttp://www.facebook.com/candelara.calciotto");
                alert.show();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void showRateTeam() {
        //aprire activity players list
        Intent team = new Intent(CalciottoCandelaraStartPage.this,TeamRateActivity.class);
        startActivity(team);
    }

    private void showPlayersList() {
        //aprire activity players list
        Intent playerlist = new Intent(CalciottoCandelaraStartPage.this,PlayersActivity.class);
        startActivity(playerlist);
    }

    private void showGamesList() {
        //aprire activity players list
        Intent gamelist = new Intent(CalciottoCandelaraStartPage.this,GamesActivity.class);
        startActivity(gamelist);
    }


}
