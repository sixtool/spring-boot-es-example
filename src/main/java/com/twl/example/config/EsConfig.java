package com.twl.example.config;

import com.twl.example.factory.Es;
import com.twl.example.factory.EsFactory;
import com.twl.example.queryBuilder.ESQueryBuilderConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EsConfig {




    @Value("${es.cluster.name}")
    private String clusterName;

    @Value("${es.host}")
    private String hosts;

    @Bean
    public Es getEs() throws Exception{
        EsFactory esFactory = new EsFactory();
        esFactory.clusterName = clusterName;
        esFactory.hosts = hosts;
       return esFactory.create();
    }
}
