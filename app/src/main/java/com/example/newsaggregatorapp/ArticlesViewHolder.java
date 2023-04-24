package com.example.newsaggregatorapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ArticlesViewHolder extends RecyclerView.ViewHolder {

    TextView Headline;
    TextView Date;
    TextView Author;
    ImageView Pic;
    TextView Content;

    public ArticlesViewHolder(@NonNull View itemView) {
        super(itemView);

        Headline = itemView.findViewById(R.id.Headline);
        Date = itemView.findViewById(R.id.Date);
        Author = itemView.findViewById(R.id.Author);
        Pic = itemView.findViewById(R.id.Image);
        Content = itemView.findViewById(R.id.Content);
    }

    public void setPic(String url)
    {
        if(url == "missing")
        {
            Pic.setImageResource(R.drawable.noimage);
        }
        else
        {
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage)
                    .into(Pic);
        }
    }
}
