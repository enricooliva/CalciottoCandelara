package com.enrico.calciottocandelara;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.enrico.calciottocandelara.data.Player;

/**
 * Created by Enrico on 06/04/2015.
 */
public class Vote {
    private final Context context;
    private Dialog rankDialog;
    private TextView ranktext;
    private float dialograting;
    private Player item;
    private RatingBar ratingBar;

    public Vote(Player item, RatingBar ratingBar, Context context) {
        this.context = context;
        this.item = item;
        this.ratingBar = ratingBar;
    }

    public void update() {
        item.addRate(dialograting);
        LinearLayout parent = (LinearLayout) ratingBar.getParent();
        ratingBar.setRating((float) item.getRate());
        TextView label = (TextView) parent.findViewById(R.id.rate);

//        ContentValues values = new ContentValues();
//        values.put(DataContract.PlayerEntry.COLUMN_RATE, item.getRate());
//        values.put(DataContract.PlayerEntry.COLUMN_NUMBER_VOTE, item.getNumberVotes());
//
//        int rowsUpdated = context.getContentResolver().update(DataContract.PlayerEntry.buildPlayerUri(item.getNumber())
//                ,values
//                ,""
//                , null);

        Intent voteService = new Intent(context, VoteService.class);
        voteService.putExtra(VoteService.NUMBER, item.getNumber());
        voteService.putExtra(VoteService.NUMBERVOTE, item.getNumberVotes());
        voteService.putExtra(VoteService.RATE, (float)item.getRate());
        context.startService(voteService);

        //label.setText(String.format("Punteggio: %.02f", item.getRate()) + String.format(" Voti: %d", item.getNumberVotes()));
       // item.updateDatabase();

    }

    public void back() {
        LinearLayout parent = (LinearLayout) ratingBar.getParent();
        TextView label = (TextView) parent.findViewById(R.id.rate);
        ratingBar.setRating((float) item.getRate());
        //label.setText(String.format("Il punteggio �: %.02f", item.getRate()));
        label.setText(String.format("Punteggio: %.02f", item.getRate()) + String.format(" Voti: %d", item.getNumberVotes()));
    }

    public void showvote() {
        rankDialog = new Dialog(context, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.votedialog);
        rankDialog.setCancelable(true);
        rankDialog.setTitle(R.string.addvote);

        float rating = 0;
        RatingBar ratingBar1 = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        if (this.ratingBar != null) {
            rating = ratingBar.getRating();
        }

        ratingBar1.setRating(rating);
        dialograting = rating;

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(item.getName());
        ranktext = (TextView) rankDialog.findViewById(R.id.rank_text);
        ranktext.setText(String.format("Voto: %.02f", rating));
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //aggiornato il voto!
                update();
                rankDialog.dismiss();
            }
        });

        Button cancelButton = (Button) rankDialog.findViewById(R.id.rank_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                back();
                rankDialog.dismiss();
            }
        });

        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar arg0,
                                        float arg1, boolean arg2) {
                ranktext.setText(String.format("Voto: %.02f", arg1));
                dialograting = arg1;
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }
}

