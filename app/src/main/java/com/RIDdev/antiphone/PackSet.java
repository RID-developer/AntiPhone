package com.RIDdev.antiphone;

public class PackSet {
    public String PackName;
    public int lim;
    public boolean ad;
    public int time;
    public boolean popup;
    public String redir;
    public boolean blacklist;

    public void Reset()
    {
        lim = 2;
        ad = false;
        time = 0;
        popup = true;
        redir = null;
        blacklist = true;
    }
}
