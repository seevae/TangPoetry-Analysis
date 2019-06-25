package io.github.seevae.crawler.prase;
/*
    name zhang;
    */

import com.gargoylesoftware.htmlunit.html.*;
import io.github.seevae.crawler.common.Page;
import io.github.seevae.crawler.common.PoetryInfo;

/**
 * 详情页解析
 *
 */

public class DataPageParse implements Parse{


    @Override
    public void parse(Page page) {
        if(!page.isDetail()){
            return;
        }

        /**
         * xml路径选择器  浏览器内部自带的
         * copy xPath: /html/body/div[3]/div[1]/div[2]/div[1]/h1
         *
         * 选择器:
         * copy selector : body > div.main3 > div.left > div:nth-child(2) > div.cont > h1
         */

        HtmlPage htmlPage = page.getHtmlPage();

        HtmlElement body = htmlPage.getBody();

        //标题
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();

        //朝代
        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();

        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //正文
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

        PoetryInfo poetryInfo = new PoetryInfo();
        poetryInfo.setDynasty(dynasty);
        poetryInfo.setAuthor(author);
        poetryInfo.setTitle(title);
        poetryInfo.setContent(content);

        page.getDataSet().putData("poetry",poetryInfo);
    }
}
