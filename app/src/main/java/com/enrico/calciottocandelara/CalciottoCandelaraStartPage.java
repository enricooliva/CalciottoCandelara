package com.enrico.calciottocandelara;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;

import com.enrico.calciottocandelara.sync.SyncUtils;


public class CalciottoCandelaraStartPage extends Activity {

    public static final String AUTHORITY = "com.enrico.calciottocandelara.app";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.enrico.calciottocandelara.account";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    private static final int PLAYERS_ID = 0;
    private static final int GAMES_ID = 1;
    private static final int TEAM_ID = 2;
    public static final int INFO=3;
    private RatingBar teamIndex;
    private boolean mTwoPane;

    private Account mAccount;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calciotto_candelara_start_page);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(CalciottoCandelaraStartPage.this, PlayersActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        // Create account, if needed
         //mAccount = CreateSyncAccount(this);
        //mAccount = CreateSyncAccount(this);
        SyncUtils.TriggerRefresh();
        //refresh();

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

//    public void refresh(){
//        // Pass the settings flags by inserting them in a bundle
//        Bundle settingsBundle = new Bundle();
//        settingsBundle.putBoolean(
//                ContentResolver.SYNC_EXTRAS_MANUAL, true);
//        settingsBundle.putBoolean(
//                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//        /*
//         * Request the sync for the default account, authority, and
//         * manual sync settings
//         */
//        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
//    }
//
//
//    /**
//     * Create a new dummy account for the sync adapter
//     *
//     * @param context The application context
//     */
//    public static Account CreateSyncAccount(Context context) {
//        // Create the account type and default account
//        Account newAccount = new Account(
//                ACCOUNT, ACCOUNT_TYPE);
//        // Get an instance of the Android account manager
//        AccountManager accountManager =
//                (AccountManager) context.getSystemService(
//                        ACCOUNT_SERVICE);
//        /*
//         * Add the account and account type, no password or user data
//         * If successful, return the Account object, otherwise report an error.
//         */
//        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
//            /*
//             * If you don't set android:syncable="true" in
//             * in your <provider> element in the manifest,
//             * then call context.setIsSyncable(account, AUTHORITY, 1)
//             * here.
//             */
//        } else {
//            /*
//             * The account exists or some other error occurred. Log this, report it,
//             * or handle it internally.
//             */
//        }
//        return newAccount;
//    }

}
