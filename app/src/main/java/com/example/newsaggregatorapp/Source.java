package com.example.newsaggregatorapp;

import java.io.Serializable;

public class Source implements Serializable {
    public String ID;
    public String Name;
    public String Category;

    public Source(String id, String n, String c)
    {
        ID = id;
        Name = n;
        Category = c;
    }
}
