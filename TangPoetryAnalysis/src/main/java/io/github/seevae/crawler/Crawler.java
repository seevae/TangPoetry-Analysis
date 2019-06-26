package io.github.seevae.crawler;/*
    name zhang;
    */

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.pipeline.ConsolePipeline;
import io.github.seevae.crawler.pipeline.Pipeline;
import io.github.seevae.crawler.prase.DataPageParse;
import io.github.seevae.crawler.prase.DocumentParse;
import io.github.seevae.crawler.prase.Parse;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {

    /*
        放置文档页面(超链接)
        放置详情页面(数据)

        未被采集和解析的页面
     */
    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();

    /*
        放置详情页面
        处理完成,数据已经在dataSet中
     */
    private final Queue<Page> detailQueue = new LinkedBlockingQueue<>();

    /*
        采集器
     */
    private final WebClient webClient;

    /*
        所有的解析器
     */
    private final List<Parse> parseList = new LinkedList<>();

    /*
        所有的清洗器(管道)
     */
    private final List<Pipeline> pipelineList = new LinkedList<>();

    /*
        线程调度器
     */
    private final ExecutorService executorService;


    public Crawler(){
        this.webClient = new WebClient();
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-Thread-"+id.getAndIncrement());
                return thread;
            }
        });

    }


    public void start(){
       //爬取

        //解析

        //清洗
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });

        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                pipeline();;
            }
        });

    }

    private void parse(){

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Page page = this.docQueue.poll();

            if(page == null){
                continue;
            }

            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //匿名内部类,通过类名来访问对象和方法
                        //采集(爬取)
                        HtmlPage htmlPage =  Crawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlPage);

                        for(Parse parse:Crawler.this.parseList){
                            parse.parse(page);
                        }


                        if(page.isDetail()){
                            Crawler.this.detailQueue.add(page);
                        }else {
                            Iterator<Page> iterator = page.getSubPage().iterator();
                            while(iterator.hasNext()){
                                Page subPage = iterator.next();
                                Crawler.this.docQueue.add(subPage);
                                iterator.remove();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    //清洗操作的是详情页面
    private void pipeline(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Page page = this.detailQueue.poll();
            if(page == null){
                continue;
            }

            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for(Pipeline pipeline: Crawler.this.pipelineList){
                        pipeline.pipeline(page);
                    }
                }
            });

        }
    }


    public void addPage(Page page){
        this.docQueue.add(page);
    }

    public void addParse(Parse parse){
        this.parseList.add(parse);
    }

    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }

    /*
        爬虫停止
     */
    public void stop(){
        //线程池停止,爬虫自然停止
        if(this.executorService!=null && !this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
    }

}
