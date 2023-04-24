package com.example.newsaggregatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.newsaggregatorapp.databinding.ActivityMainBinding;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Menu opt_menu;

    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<Source> allSourceList = new ArrayList<>();
    private ArrayList<String> currentSourceList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> Categories = new HashMap<>();
    private int[] colors = {Color.BLACK, Color.RED, Color.parseColor("#FFA500"), Color.BLUE, Color.GREEN, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.LTGRAY, Color.DKGRAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDrawerLayout = binding.drawerLayout;
        mConstraintLayout = binding.cLayout;
        mDrawerList = binding.leftDrawer;
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_item, currentSourceList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                String str = textView.getText().toString();
                Object[] keys = Categories.keySet().toArray();
                for (int j=1; j<Categories.keySet().size(); j++)
                {
                    if(Categories.get(keys[j].toString()).contains(str))
                    {
                        textView.setTextColor(colors[j]);
                    }
                }

                return textView;
            }
        };
        mDrawerList.setAdapter(arrayAdapter);


        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    String source = currentSourceList.get(position);
                    String sourceID = "";
                    for (Source s : allSourceList)
                    {
                        if(s.Name.equals(source))
                        {
                            sourceID = s.ID;
                        }
                    }
                    Intent intent = new Intent(MainActivity.this, ArticlesActivity.class);
                    intent.putExtra("SOURCE", sourceID);
                    startActivity(intent);
                    mDrawerLayout.closeDrawer(mConstraintLayout);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,           /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        API.getSources(this);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        currentSourceList.clear();
        ArrayList<String> clist = Categories.get(item.getTitle().toString());
        if (clist != null) {
            currentSourceList.addAll(clist);
        }

        arrayAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);

    }

    // You need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    public void updateSourcesData(ArrayList<Source> sources, HashMap<String, ArrayList<String>> categories)
    {
        Categories = categories;

        Object[] keys = Categories.keySet().toArray();
        for (int i = 0; i<keys.length; i++)
        {
            String key = (String)keys[i];
            opt_menu.add(key);
            if(i!=0)
            {
                SpannableString s = new SpannableString(key);
                s.setSpan(new ForegroundColorSpan(colors[i]), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // provide whatever color you want here.
                opt_menu.getItem(i).setTitle(s);
            }

        }

        allSourceList.addAll(sources);
        for(Source s : allSourceList)
        {
            currentSourceList.add(s.Name);
        }
        arrayAdapter.notifyDataSetChanged();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void downloadSourcesFailed()
    {
        allSourceList.clear();
        currentSourceList.clear();
        arrayAdapter.notifyDataSetChanged();
        Categories.clear();
    }
}