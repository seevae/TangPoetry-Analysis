package io.github.seevae;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.seevae.analyze.dao.AnalyzeDao;
import io.github.seevae.analyze.dao.impl.AnalyzeDaoImpl;
import io.github.seevae.analyze.service.AnalyzeService;
import io.github.seevae.analyze.service.impl.AnalyzeServiceImpl;
import io.github.seevae.config.ConfigProperties;
import io.github.seevae.config.ObjectFactory;
import io.github.seevae.crawler.Crawler;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.pipeline.DatabasePipeline;
import io.github.seevae.crawler.prase.DataPageParse;
import io.github.seevae.crawler.prase.DocumentParse;
import javax.sql.DataSource;

public class TestCrawlerMain {

    public static void main(String[] args) {

        Crawler crawler = ObjectFactory .getInstance().getObject(Crawler.class);
        crawler.start();


    }
}
