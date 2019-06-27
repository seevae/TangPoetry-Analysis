package io.github.seevae.config;/*
    name zhang;
    */

import com.alibaba.druid.pool.DruidDataSource;
import io.github.seevae.analyze.dao.AnalyzeDao;
import io.github.seevae.analyze.dao.impl.AnalyzeDaoImpl;
import io.github.seevae.analyze.service.AnalyzeService;
import io.github.seevae.analyze.service.impl.AnalyzeServiceImpl;
import io.github.seevae.crawler.Crawler;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.pipeline.ConsolePipeline;
import io.github.seevae.crawler.pipeline.DatabasePipeline;
import io.github.seevae.crawler.prase.DataPageParse;
import io.github.seevae.crawler.prase.DocumentParse;
import io.github.seevae.web.WebController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象工厂只关心对象
 */
public class ObjectFactory {

    private static final ObjectFactory instance = new ObjectFactory();

    //有很多对象所以放在map中
    private final Map<Class,Object> objectHashMap = new HashMap<>();

    private ObjectFactory(){
        //1.初始化配置对象
        initConfigProperties();

        //2.创建数据源对象
        initDataSource();

        //3.爬虫对象
        initCrawler();

        //4.Web对象
        initWebController();

        //5.对象清单打印输出
        printObjectList();


    }

    private void initWebController() {
        DataSource dataSource = getObject(DataSource.class);
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        WebController webController = new WebController(analyzeService);
        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DataSource dataSource = getObject(DataSource.class);
        final Page page = new Page(
                configProperties.getCrawlerBase(),
                configProperties.getCrawlerPath(),
                configProperties.isCrawlerDetail()
        );

        Crawler crawler = new Crawler();
        crawler.addParse(new DocumentParse());
        crawler.addParse(new DataPageParse());
        if(configProperties.isEnableConsole()){
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPipeline(new DatabasePipeline(dataSource));
        crawler.addPage(page);

        objectHashMap.put(Crawler.class,crawler);

    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());

        objectHashMap.put(DataSource.class,dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class,configProperties);

    }

    public  <T> T getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class "+classz.getName()+" not found Object");
        }
        return (T) objectHashMap.get(classz);
    }

    public static ObjectFactory getInstance(){
        return instance;
    }

    private void printObjectList(){
        //对应的类 实现类的对象
        System.out.println("------------- ObjectFactory ----------------");
        for(Map.Entry<Class,Object> entry: objectHashMap.entrySet()){
            System.out.println(String.format("\t[%s] --> [%s]",entry.getKey().
                    getCanonicalName(),entry.getValue().getClass().getCanonicalName()));
        }
        System.out.println("--------------------------------------------");
    }

}
