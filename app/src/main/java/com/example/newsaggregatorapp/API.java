package com.example.newsaggregatorapp;

import android.net.Uri;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class API {

    private static final String SOURCES_URL = "https://newsapi.org/v2/sources?apiKey=ecfb30dd6ff34c66bc932ca130064ef6";
    private static final String ARTICLES_URL = "https://newsapi.org/v2/top-headlines?sources=SOURCE&apiKey=ecfb30dd6ff34c66bc932ca130064ef6";

    public static void getSources(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(SOURCES_URL).buildUpon();
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleSourcesResults(mainActivity, response.toString());

        Response.ErrorListener error = error1 -> {};

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlToUse, listener, error)
        {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void handleSourcesResults(MainActivity mainActivity, String s)
    {
        if (s == null) {
            mainActivity.downloadSourcesFailed();
            return;
        }

        Pair<ArrayList<Source>, HashMap<String, ArrayList<String>>> result = parseSourcesJSON(s);
        ArrayList<Source> sources = result.first;
        HashMap<String, ArrayList<String>> categories = result.second;

        if (sources != null && categories != null)
            Toast.makeText(mainActivity, "Loaded Sources Data.", Toast.LENGTH_SHORT).show();
        mainActivity.updateSourcesData(sources, categories);
    }

    private static Pair<ArrayList<Source>, HashMap<String, ArrayList<String>>> parseSourcesJSON(String s)
    {
        ArrayList<Source> SourcesList = new ArrayList<>();
        HashMap<String, ArrayList<String>> Categories = new HashMap<>();

        Categories.put("All", new ArrayList<>());

        try
        {
            JSONObject data = new JSONObject(s);
            JSONArray sources = data.optJSONArray("sources");
            for (int i = 0; i < sources.length(); i++)
            {
                JSONObject source = (JSONObject) sources.get(i);
                String id = source.getString("id");
                String name = source.getString("name");
                String category = source.getString("category");

                SourcesList.add(new Source(id, name, category));

                if(Categories.containsKey(category))
                {
                    Categories.get(category).add(name);
                }
                else
                {
                    Categories.put(category, new ArrayList<String>(){{add(name);}});
                }
                Categories.get("All").add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(SourcesList, Categories);
    }


    public static void getArticles(ArticlesActivity articlesActivity, String SourceID) {
        RequestQueue queue = Volley.newRequestQueue(articlesActivity);

//        Uri.Builder buildURL = Uri.parse(RAW_URL.replaceFirst("\\?","="+SourceID)).buildUpon();
//        String urlToUse = buildURL.build().toString();

        String urlToUse = ARTICLES_URL.replaceFirst("SOURCE",SourceID);

        Response.Listener<JSONObject> listener =
                response -> handleArticlesResults(articlesActivity, response.toString());

        Response.ErrorListener error = error1 -> {
//            JSONObject jsonObject;
//            try {
//                jsonObject = new JSONObject(new String(error1.networkResponse.data));
//                handleResultsRecycler(mainActivity, null);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlToUse, listener, error) {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void handleArticlesResults(ArticlesActivity articlesActivity, String s)
    {
        if (s == null) {
            articlesActivity.downloadArticlesFailed();
            return;
        }

        ArrayList<Article> articles = parseArticlesJSON(s);

        if (articles != null)
            Toast.makeText(articlesActivity, "Loaded Sources Data.", Toast.LENGTH_SHORT).show();
        articlesActivity.updateArticlesData(articles);
    }

    private static ArrayList<Article> parseArticlesJSON(String s)
    {
        ArrayList<Article> articlesList = new ArrayList<>();

        try
        {
            JSONObject data = new JSONObject(s);
            JSONArray articles = data.optJSONArray("articles");
            int length = Math.min(articles.length(), 10);
            for (int i = 0; i < length; i++)
            {
                JSONObject article = (JSONObject) articles.get(i);
                String headline = "missing";
                String date = "missing";
                String author = "missing";
                String picURL = "missing";
                String content = "missing";
                String url = "missing";
                if(article.has("title"))
                {
                    headline = article.getString("title");
                }
                if(article.has("publishedAt"))
                {
                    date = article.getString("publishedAt");
                    String[] formats = {"yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss.SSSXXX"};
                    for (String f : formats) {
                        try {
                            SimpleDateFormat raw = new SimpleDateFormat(f);
                            raw.setTimeZone(TimeZone.getDefault());
                            date = new SimpleDateFormat("MMM dd, yyyy HH:mm").format((raw.parse(date)));
                            break;
                        }catch(ParseException e)
                        {

                        }
                    }
                }
                if(article.has("author"))
                {
                    author = article.getString("author");
                }
                if(article.has("urlToImage"))
                {
                    picURL = article.getString("urlToImage");
                }
                if(article.has("description"))
                {
                    content = article.getString("description");
                }
                if(article.has("url"))
                {
                    url = article.getString("url");
                }

                articlesList.add(new Article(headline, date, author, picURL, content, url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return articlesList;
    }

}
