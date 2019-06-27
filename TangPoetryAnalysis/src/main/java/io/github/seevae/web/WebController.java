package io.github.seevae.web;


import com.google.gson.Gson;
import io.github.seevae.analyze.model.AuthorCount;
import io.github.seevae.analyze.model.WordCount;
import io.github.seevae.analyze.service.AnalyzeService;
import io.github.seevae.config.ObjectFactory;
import io.github.seevae.crawler.Crawler;
import spark.*;

import java.util.List;


/**
 * web API 使用的是sparkJava框架完成的 Web API的开发
 */
public class WebController {

    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    // -> http://127.0.0.1:4567/
    // -> analyze/author_count
    public List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }

    // -> http://127.0.0.1:4567/
    // -> analyze/word_cloud
    public List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }

    public void launch(){
        ResponseTransformer transformer = new JSONResponseTransformer();

        //src/main/resourse/static
        Spark.staticFileLocation("/static");

        Spark.get("/analyze/author_count",((request, response) -> analyzeAuthorCount()),transformer);
        Spark.get("/analyze/word_cloud",((request, response) -> analyzeWordCloud()),transformer);
        Spark.get("/crawler/stop",((request, response) -> {
            Crawler crawler = ObjectFactory.getInstance().getObject(Crawler.class);
            crawler.stop();
            return  "爬虫停止了 !";
        }));
    }

    public static class JSONResponseTransformer implements ResponseTransformer{
        Gson gson = new Gson();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }

}
