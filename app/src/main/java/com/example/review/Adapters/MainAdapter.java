package com.example.review.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Viewholder> {

    Activity activity;
    JSONArray jsonArray;

    public MainAdapter(Activity activity, JSONArray jsonArray) {
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        try {
            //initialize json object
            JSONObject object= jsonArray.getJSONObject(position);
            //get rating from json array
            String sRating = object.getString("rating");
            //set rating on text view
            holder.tvRating.setText(sRating);
            //set rating on rating bar
            holder.ratingBar.setRating(Float.parseFloat(sRating));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView tvRating;
        RatingBar ratingBar;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvRating= itemView.findViewById(R.id.tv_rating);
            ratingBar= itemView.findViewById(R.id.rating_bar);

        }
    }
}
