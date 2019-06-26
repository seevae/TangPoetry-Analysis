package io.github.seevae.crawler.common;/*
    name zhang;
    */

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 放的都是公共模块
 */

@Data
public class Page {

    /**
     * 数据网站的根地址
     * eg: https://so.gushiwen.org
     */

    private final String base;

    /**
     * 具体网页的路径
     * eg:/gushi/tangshi.aspx
     */

    private final String path;

    /**
     * 网页Dom对象 -->文档对象模型
     */
    private HtmlPage htmlPage;

    /**
     *标识网页是否是详情页
     */
    private final boolean detail;

    /**
     * 子页面对象集合
     */
    private Set<Page> subPage = new HashSet<>();

    /**
     * 数据对象--->把所有数据放在一个对象当中,抽象出来一个包装类,即以后的数据就是这个类
     */
    private DataSet dataSet = new DataSet();

    public String getUrl(){
        return this.base+this.path;
    }

}
