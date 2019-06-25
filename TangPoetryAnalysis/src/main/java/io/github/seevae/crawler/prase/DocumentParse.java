package io.github.seevae.crawler.prase;/*
    name zhang;
    */

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.github.seevae.crawler.common.Page;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


/**
 * 链接解析
 *
 */
public class DocumentParse implements  Parse {

    @Override
    public void parse(Page page) {
        if(page.isDetail()){
            return;
        }

        HtmlPage htmlPage = page.getHtmlPage();

        //取出所有div块中的内容
        htmlPage.getBody()
                .getElementsByAttribute("div","class","typecont")
                .forEach(div -> {
    //                System.out.println(htmlElement.asXml());

                    //取出了div标签块中所有的a标签的节点
                    DomNodeList<HtmlElement> aNodeList =  div.getElementsByTagName("a");
                    aNodeList.forEach(
                            aNode->{
                                //根据a标签里面的内容取出子页面中的路径
                                String path = aNode.getAttribute("href");

                                Page subPage = new Page(
                                        page.getBase(),
                                        path,
                                        true
                                );

                                page.getSubPage().add(subPage);
                            }
                    );

                });
    }
}
