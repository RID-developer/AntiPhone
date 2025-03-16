package com.RIDdev.antiphone.background;

import com.RIDdev.antiphone.Database.DBOperation;

public class BkMain extends Thread{
    DBOperation Opr = null;
    @Override
    public void run() {
        while (true) {
            long begining = System.currentTimeMillis();
            //something();

            long end = System.currentTimeMillis() - begining;
            pause(safety((5000 - end)));
        }
    }
        public long safety(long x)
        {
            if(x<1)
                return 1;
            else
                return x;
        }
        public void pause(long time)
        {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
}
