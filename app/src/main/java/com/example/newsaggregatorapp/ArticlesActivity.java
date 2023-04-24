package com.example.newsaggregatorapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.newsaggregatorapp.databinding.ActivityArticlesBinding;

import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity
{
    private ActivityArticlesBinding binding;
    private ArticlesViewAdapter articlesAdapter;
    private final ArrayList<Article> articlesList = new ArrayList<>();
    private ViewPager2 viewPager2;

    private String sourceID;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        binding = ActivityArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if(!intent.hasExtra("SOURCE"))
        {
            finish();
            return;
        }

        sourceID = intent.getStringExtra("SOURCE");
        viewPager2 = binding.viewPager;

        articlesAdapter = new ArticlesViewAdapter(this, articlesList);
        viewPager2.setAdapter(articlesAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        API.getArticles(this, intent.getStringExtra("SOURCE"));

        // To get swipe event of viewpager2 (Source: GeeksForGeeks)
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                pos = position;
                binding.ArticleCount.setText((pos+1) + " of " + articlesList.size());
                Toast.makeText(getApplicationContext(), "POS: " + pos, Toast.LENGTH_SHORT);
                super.onPageSelected(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("POS", pos);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        pos = savedInstanceState.getInt("POS");
//        viewPager2.setCurrentItem(pos);
    }

    public void updateArticlesData(ArrayList<Article> aL) {
        if (aL == null) {
            Toast.makeText(this, "API.getArticles failed", Toast.LENGTH_LONG).show();
            articlesList.clear();
            articlesAdapter.notifyItemChanged(0, articlesList.size());
        } else {
            articlesList.addAll(aL);
            articlesAdapter.notifyItemRangeChanged(0, aL.size());
        }
        binding.ArticleCount.setText("1 of " + articlesList.size());
        viewPager2.setCurrentItem(pos+1);
    }

    public void downloadArticlesFailed()
    {
        articlesList.clear();
        articlesAdapter.notifyItemChanged(0, articlesList.size());
    }

    public void onArticleClick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String articleCount = binding.ArticleCount.getText().toString();
//        int pos = Integer.parseInt(articleCount.substring(0,articleCount.indexOf(" ")));
        intent.setData(Uri.parse(articlesList.get(pos).URL));
        startActivity(intent);
    }

}
