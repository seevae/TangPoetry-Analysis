package io.github.seevae;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.seevae.analyze.dao.AnalyzeDao;
import io.github.seevae.analyze.dao.impl.AnalyzeDaoImpl;
import io.github.seevae.analyze.service.AnalyzeService;
import io.github.seevae.analyze.service.impl.AnalyzeServiceImpl;
import io.github.seevae.config.ConfigProperties;
import io.github.seevae.crawler.Crawler;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.pipeline.DatabasePipeline;
import io.github.seevae.crawler.prase.DataPageParse;
import io.github.seevae.crawler.prase.DocumentParse;
import javax.sql.DataSource;

public class TestCrawlerMain {

    public static void main(String[] args) {

        ConfigProperties configProperties = new ConfigProperties();

//        final Page page = new Page(
//                configProperties.getCrawlerBase(),
//                configProperties.getCrawlerPath(),
//                configProperties.isCrawlerDetail()
//        );
//
//        Crawler crawler = new Crawler();
//        crawler.addParse(new DocumentParse());
//        crawler.addParse(new DataPageParse());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());
//
//        crawler.addPipeline(new DatabasePipeline(dataSource));
//
//        crawler.addPage(page);
//
//        crawler.start();
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);

        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);

        analyzeService.analyzeWordCloud().forEach(System.out::println);

    }

}
