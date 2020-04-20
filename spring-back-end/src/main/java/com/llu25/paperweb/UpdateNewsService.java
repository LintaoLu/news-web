package com.llu25.paperweb;
import java.io.IOException;
import java.util.*;

public class UpdateNewsService extends TimerTask {

    private PaperWebApplication paperWebApplication;

    public UpdateNewsService(PaperWebApplication p) {
        paperWebApplication = p;
    }

    @Override
    public void run() {
        for (String type : Utils.basicNewsTypes) {
            String json;
            Map<Integer, List<News>> news = null;
            try {
                json = Utils.getJson(type);
                news = Utils.parseNewsJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (news != null) {
                FIFO<List<News>> fifo = paperWebApplication.getNews().get(type);
                fifo.append(news);
            }
        }
    }
}
