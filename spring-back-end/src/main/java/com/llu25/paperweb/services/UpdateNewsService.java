package com.llu25.paperweb.services;
import com.llu25.paperweb.RequestType;
import com.llu25.paperweb.datastructures.FIFO;
import com.llu25.paperweb.News;
import com.llu25.paperweb.PaperWebApplication;
import com.llu25.paperweb.Utils;
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
                json = Utils.getJson(RequestType.NEWS, type);
                news = Utils.parseNewsJson(paperWebApplication.keyWordExtractionService, true, json);
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
