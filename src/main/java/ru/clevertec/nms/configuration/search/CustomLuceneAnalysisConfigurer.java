package ru.clevertec.nms.configuration.search;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class CustomLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("english").custom()
                .tokenizer("standard")
                .tokenFilter("lowercase")
                .tokenFilter("snowballPorter")
                .param("language", "English")
                .tokenFilter("asciiFolding");
        context.analyzer("standard").custom()
                .tokenizer("standard")
                .tokenFilter("lowercase")
                .tokenFilter("asciiFolding");
    }
}
