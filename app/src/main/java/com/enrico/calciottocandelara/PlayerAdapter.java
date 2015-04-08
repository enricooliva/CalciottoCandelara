package com.enrico.calciottocandelara;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.enrico.calciottocandelara.data.Player;

/**
 * Created by Enrico on 06/04/2015.
 */
public class PlayerAdapter extends CursorAdapter {

    private Context currentContext;
    private Player item;

    public PlayerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.currentContext=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.player, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        item = new Player(cursor);

        TextView textName = (TextView) convertView.findViewById(R.id.name);
        textName.setText(item.getName());

        TextView textRole = (TextView) convertView.findViewById(R.id.role);
        textRole.setText(item.getRole());

        TextView textNumber = (TextView) convertView.findViewById(R.id.number);
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.icon);

        TextView ratings = (TextView) convertView.findViewById(R.id.rate);
        RatingBar ratingbar = (RatingBar) convertView.findViewById(R.id.ratingBar1);

        textName.setText(item.getName());
        textRole.setText(item.getRole());
        if (item.getNumber()>0)
            textNumber.setText(String.valueOf(item.getNumber()));
        else
            textNumber.setText("");

        ratingbar.setRating((float) item.getRate());
        ratings.setText(String.format("Punteggio: %.02f", item.getRate())+String.format(" Voti: %d", item.getNumberVotes()));

    }

}