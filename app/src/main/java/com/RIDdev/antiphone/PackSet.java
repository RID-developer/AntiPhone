package com.RIDdev.antiphone;

public class PackSet {
    public String PackName = "";
    public String BaseName;
    public int lim;
    public boolean ad;
    public int time;
    public boolean popup;
    public String Message;
    public boolean blacklist;
    public int day;
    public int someething;
    public void Reset()
    {
        BaseName = "?";
        lim = 120;
        day = 0;
        ad = false;
        time = 0;
        popup = true;
        Message = "FINNISH USING THIS APP AND TOUCH GRASS!";
        blacklist = true;
    }
}
