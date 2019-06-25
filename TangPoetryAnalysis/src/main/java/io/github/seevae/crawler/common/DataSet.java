package io.github.seevae.crawler.common;/*
    name zhang;
    */

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储清洗的数据
 *
 */

@ToString
public class DataSet {

    /**
     * data把Dom解析,清洗之后存储的数据
     * 比如:
     *     标题: 静夜思
     *     作者: 李白
     *     正文: xxx
     *     k   :  v
     */
    private Map<String,Object> data = new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key,value);
    }

    public Object getData(String key){
        return this.data.get(key);
    }


    public Map<String,Object> getData(){
        return new HashMap<>(this.data);
    }
}
