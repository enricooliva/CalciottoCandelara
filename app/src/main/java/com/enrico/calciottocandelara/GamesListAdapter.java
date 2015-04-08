package com.enrico.calciottocandelara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.enrico.calciottocandelara.data.Game;

import java.util.List;

public class GamesListAdapter extends ArrayAdapter<Game> {

	static class ViewHolder {
	    public TextView textResult;
	    public TextView textGame;
	    public TextView textGoals;
	   
	  }
	
	private List<Game> Items;
	private LayoutInflater inflater;
	private Context currentContext;
	

	
	public GamesListAdapter(Context context, List<Game> games) {
		super(context, R.layout.fragment_game, games);
	    this.Items = games;	    
	    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	    
	    currentContext = context;	    		
	}
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    
		 if (convertView == null) {
	      
	      convertView=inflater.inflate(R.layout.game, null);
    	    
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.textResult = (TextView) convertView.findViewById(R.id.result);
	      viewHolder.textGame = (TextView) convertView.findViewById(R.id.game);
	      viewHolder.textGoals = (TextView) convertView.findViewById(R.id.goals);
	      	      	      	  	     	      	   
	      convertView.setTag(viewHolder);
	    }
	
		ViewHolder holder = (ViewHolder) convertView.getTag();
	    Game item= Items.get(position);
	    
	    String s = item.getGame();
	    holder.textGame.setText(s);
	    holder.textResult.setText(item.getResult());	  
	    if (item.getVictory())
	    {
	    	holder.textResult.setBackgroundResource(R.color.green);
	    }else
	    {
	    	holder.textResult.setBackgroundResource(R.color.red);
	    }
	    
	    
	    
	    holder.textGoals.setText(item.getGoals());
	   
	    //holder.ratings.setText(String.format("Il punteggio �: %.02f", item.getRate()));
 	           
        //ratings.setText("Il punteggio �: " + String.valueOf(ratingbar.getRating()));
	    
	    
	    /*if (s.startsWith("Windows7") || s.startsWith("iPhone")
	    || s.startsWith("Solaris")) {
	      holder.image.setImageResource(R.drawable.no);
	    } else {
	      holder.image.setImageResource(R.drawable.ok);
	    }
	*/
	    return convertView;
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  

	  



	
}
