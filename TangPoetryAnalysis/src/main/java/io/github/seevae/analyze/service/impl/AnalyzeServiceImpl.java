package io.github.seevae.analyze.service.impl;/*
    name zhang;
    */

import io.github.seevae.analyze.dao.AnalyzeDao;
import io.github.seevae.analyze.entity.PoetryInfo;
import io.github.seevae.analyze.model.AuthorCount;
import io.github.seevae.analyze.model.WordCount;
import io.github.seevae.analyze.service.AnalyzeService;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import sun.font.TextRecord;

import java.util.*;

public class AnalyzeServiceImpl implements AnalyzeService {

    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //此处结果并未排序
        //排序方式
        //1.DAO层sql排序  order by
        //2.在Servicr排序  对List集合进行排序

        //降序排序
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount();

        Collections.sort(authorCounts, new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
                return o1.getCount().compareTo(o2.getCount());
            }
        });

        return authorCounts;

        //不排序
//        return analyzeDao.analyzeAuthorCount();
    }

    @Override
    public List<WordCount> analyzeWordCloud() {

        //1.查询出所有数据
        //2.取出 title content
        //3.分词 过滤掉 /w null  具体查看词性说明文档  nlpchina.github.io/ansj_seg/
        //4.统计  k-v  k是词  v是词频

        Map<String,Integer> map = new HashMap<>();

        List<PoetryInfo> poetryInfos = analyzeDao.queryAllPoetryInfo();

        for(PoetryInfo poetryInfo:poetryInfos){
            List<Term> terms = new ArrayList<>();
            String title = poetryInfo.getTitle();
            String content = poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());

            //过滤
            Iterator<Term> iterator = terms.iterator();
            while(iterator.hasNext()){
                Term term = iterator.next();
                //词性的过滤
                if(term.getNatureStr()==null || term.getNatureStr().equals("w")){
                    iterator.remove();
                    continue;
                }
                //词的过滤(根据长度过滤)
                if(term.getRealName().length()<2){
                    iterator.remove();
                    continue;
                }
                //统计
                String realName = term.getRealName();
                int count = 0;
                if(map.containsKey(realName)){
                    count = map.get(realName)+1;
                }else {
                    count= 1;
                }
                map.put(realName,count);

            }
        }

        List<WordCount> wordCounts = new ArrayList<>();
        for(Map.Entry<String,Integer> entry: map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setCount(entry.getValue());
            wordCount.setWord(entry.getKey());
            wordCounts.add(wordCount);
        }

        return wordCounts;
    }

}
