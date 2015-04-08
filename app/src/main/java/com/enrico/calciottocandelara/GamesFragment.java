package com.enrico.calciottocandelara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.enrico.calciottocandelara.data.Game;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Enrico on 05/04/2015.
 */
public class GamesFragment extends Fragment implements Utility.IAction {

    static ArrayList<Game> gameList;
    private GamesListAdapter gameListAdapter;
    private ListView gameListView;

    public GamesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        gameListView = (ListView) rootView.findViewById(R.id.listGame);

        new Utility(this).execute(new String[]{"http://www.enricooliva.com/getAllGames.php"});

        return rootView;
    }



    public void onTaskCompleted(String result)
    {
        if (result.isEmpty())
        {
            Toast.makeText(getActivity(), "Connessione dati assente", Toast.LENGTH_LONG).show();
        }

        gameList = getGameList(result);
        gameListAdapter = new GamesListAdapter(getActivity(),gameList);
        gameListView.setAdapter(gameListAdapter);
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //quale partita � stata selezionata?
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://http://www.daltrocalcio.it/news-dalle-squadre/comitato-di-pesaro-urbino/calcio-a-8-serie-b/girone-2/candelara-c8"));

                startActivity(browserIntent);

            }
        });

        gameListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


//				WebView webView = (WebView) findViewById(R.id.webView1);
//				WebSettings settings = webview.getSettings();
//				settings.setJavaScriptEnabled(true);
//				webView.loadUrl(URL);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


    }

    public ArrayList<Game> getGameList(String games)
    {
        ArrayList<Game> gamelist = new ArrayList<Game>();
        //parse json data
        try{
            JSONArray jArray = new JSONArray(games);
            for(int i=0;i<jArray.length();i++){
                gamelist.add(new Game( jArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());

        }

        return gamelist;
    }

    @Override
    public void postAction(String result) {
        onTaskCompleted(result);
    }
}