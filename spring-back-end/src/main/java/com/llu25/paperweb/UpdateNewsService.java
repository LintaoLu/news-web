package com.llu25.paperweb;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.*;

public class UpdateNewsService extends TimerTask {

    private PaperWebApplication paperWebApplication;

    public UpdateNewsService(PaperWebApplication p) {
        paperWebApplication = p;
    }

    @Override
    public void run() {
        for (NewsType type : NewsType.values()) {
            String json;
            LRU<List<News>> news = null;
            try {
                json = Utils.getJson(type);
                news = Utils.parseNewsJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            paperWebApplication.getNews().put(type, news);
        }
    }
}
