package io.github.seevae;

import io.github.seevae.config.ObjectFactory;
import io.github.seevae.crawler.Crawler;
import io.github.seevae.web.WebController;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestCrawlerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCrawlerMain.class);

    public static void main(String[] args) {

        WebController webController = ObjectFactory.getInstance().getObject(WebController.class);

        //运行web服务,提供接口
        LOGGER.info("Web Server launch ...");
        webController.launch();

        if(args.length == 1&& args[0].equals("run-crawler")){
            //启动爬虫
            Crawler crawler = ObjectFactory .getInstance().getObject(Crawler.class);
            LOGGER.info("crawler start ...");
            crawler.start();
        }

    }
}
