package io.github.seevae.crawler.pipeline;/*
    name zhang;
    */

import io.github.seevae.crawler.common.Page;

public interface Pipeline {

    void pipeline(final Page page);
}
