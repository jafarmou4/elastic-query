package com.example;

import org.springframework.stereotype.Component;

@Component
public class QueryUtilities {
    public static ElasticsearchQueryService service;

    public QueryUtilities(ElasticsearchQueryService service) {
        QueryUtilities.service = service;
    }

    public static void runIt() {
        service.queryNo1();
    }
}
