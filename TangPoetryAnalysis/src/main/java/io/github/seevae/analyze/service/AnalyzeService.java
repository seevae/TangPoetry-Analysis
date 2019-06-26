package io.github.seevae.analyze.service;/*
    name zhang;
    */

import io.github.seevae.analyze.model.AuthorCount;
import io.github.seevae.analyze.model.WordCount;

import java.util.List;

public interface AnalyzeService {

    /**
     * 分析唐诗中作者的创作数量
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 词云分析
     *
     * @return
     */
    List<WordCount> analyzeWordCloud();

}
