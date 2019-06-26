package io.github.seevae.analyze.dao;/*
    name zhang;
    */

import io.github.seevae.analyze.entity.PoetryInfo;
import io.github.seevae.analyze.model.AuthorCount;

import java.util.List;

public interface AnalyzeDao {

    /**
     * 分析唐诗中作者的创作数量
     * @return
     */

    public List<AuthorCount> analyzeAuthorCount();

    /**
     * 查询所有的诗文提供给业务层进行分析
     */
    List<PoetryInfo> queryAllPoetryInfo();


}
