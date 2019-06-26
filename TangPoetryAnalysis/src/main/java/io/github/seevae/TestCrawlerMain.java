package io.github.seevae;/*
    name zhang;
    */

import com.alibaba.druid.pool.DruidDataSource;
import io.github.seevae.crawler.Crawler;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.pipeline.DatabasePipeline;
import io.github.seevae.crawler.prase.DataPageParse;
import io.github.seevae.crawler.prase.DocumentParse;

import javax.sql.DataSource;

public class TestCrawlerMain {

    public static void main(String[] args) {

        final Page page = new Page("https://so.gushiwen.org","/gushi/tangshi.aspx",false);

        Crawler crawler = new Crawler();
        crawler.addParse(new DocumentParse());
        crawler.addParse(new DataPageParse());

//        crawler.addPipeline(new ConsolePipeline());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("zhang");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/tangshi");

        crawler.addPipeline(new DatabasePipeline(dataSource));

        crawler.addPage(page);

        crawler.start();

    }

}
