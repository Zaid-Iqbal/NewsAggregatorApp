package com.example.newsaggregatorapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.newsaggregatorapp.databinding.ArticlesEntryBinding;

public class ArticlesViewAdapter extends RecyclerView.Adapter<ArticlesViewHolder>
{

    private final ArticlesActivity articlesActivity;
    private final ArrayList<Article> articlesList;

    public ArticlesViewAdapter(ArticlesActivity articlesActivity, ArrayList<Article> articlesList) {
        this.articlesActivity = articlesActivity;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticlesViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.articles_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {
        Article article = articlesList.get(position);

        if(article.Headline.equals("missing"))
        {
            holder.Headline.setVisibility(View.GONE);
        }
        else
        {
            holder.Headline.setText(article.Headline);
        }

        if(article.Date.equals("missing"))
        {
            holder.Date.setVisibility(View.GONE);
        }
        else
        {
            holder.Date.setText(article.Date);
        }

        if(article.Author.equals("missing"))
        {
            holder.Author.setVisibility(View.GONE);
        }
        else
        {
            holder.Author.setText(article.Author);
        }

        if(article.Text.equals("missing"))
        {
            holder.Content.setVisibility(View.GONE);
        }
        else
        {
            holder.Content.setText(article.Text);
        }
        holder.setPic(article.Pic);
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }
}

