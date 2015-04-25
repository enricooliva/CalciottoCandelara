package com.enrico.calciottocandelara;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;

import com.enrico.calciottocandelara.data.DataContract.PlayerEntry;
import com.enrico.calciottocandelara.data.Player;

/**
 * Created by Enrico on 05/04/2015.
 */

public  class PlayersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PLAYER_LOADER = 0;
    private static final String LOG_TAG = "PlayersFragment";
    private ListView playerListView;

    private PlayerAdapter mPlayerAdapter;

    private static final String[] PLAYER_COLUMN = {
            PlayerEntry.TABLE_NAME+"."+ PlayerEntry._ID,
            PlayerEntry.COLUMN_NAME,
            PlayerEntry.COLUMN_NUMBER,
            PlayerEntry.COLUMN_RATE,
            PlayerEntry.COLUMN_ROLE,
            PlayerEntry.COLUMN_NUMBER_VOTE
    };

    public static final int COL_PLAYER_ID = 0;
    public static final int COL_PLAYER_NAME = 1;
    public static final int COL_PLAYER_NUMBER = 2;
    public static final int COL_PLAYER_RATE = 3;
    public static final int COL_PLAYER_ROLE = 4;
    public static final int COL_PLAYER_NUMBER_VOTE = 5;

    public PlayersFragment()  {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPlayerAdapter = new PlayerAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_players, container, false);


        //playerListView = getListView();
        // Get a reference to the ListView, and attach this adapter to it.
        playerListView = (ListView) rootView.findViewById(R.id.listPlayer);

       //new Utility(this).execute(new String[]{"http://www.enricooliva.com/getAllPlayers.php"});

        playerListView.setAdapter(mPlayerAdapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mPlayerAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Player p = new Player(cursor);
                    LinearLayout layout = (LinearLayout) view;
                    RatingBar ratingBar=(RatingBar) layout.findViewById(R.id.ratingBar1);
                    new Vote(p,ratingBar,getActivity()).showvote();
                }
            }
        });

        return rootView;
    }

    private void updatePlayer() {

        new FetchPlayerTask(getActivity()).execute("");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PLAYER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //updatePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(PLAYER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                PlayerEntry.CONTENT_URI,
                PLAYER_COLUMN,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        mPlayerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayerAdapter.swapCursor(null);
    }




}
