package com.example.newsaggregatorapp;

public class Article {

    public String Headline;
    public String Date;
    public String Author;
    public String Pic;
    public String Text;
    public String URL;

    public Article(String h, String d, String a, String p, String t, String url)
    {
        Headline = h;
        Date = d;
        Author = a;
        Pic = p;
        Text = t;
        URL = url;
    }
}