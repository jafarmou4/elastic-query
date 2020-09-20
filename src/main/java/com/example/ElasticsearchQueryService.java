package com.example;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ElasticsearchQueryService {
    private final RestTemplate restTemplate;

    public ElasticsearchQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void ElasticQuery(String url, Integer size, Integer from, JSONObject[] matches) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set query json
        JSONObject jsonQuery = new JSONObject()
                .put("size", size)
                .put("from", from)
                .put("query", new JSONObject()
                        .put("bool", new JSONObject()
                                .put("must", matches)));

        String stringQuery = jsonQuery.toString();

        // call the api
        ResponseEntity<Object> exchange = restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(stringQuery, headers),
                Object.class);

        JSONObject body = new JSONObject(exchange).getJSONObject("body");

        // extracting desired values
        int counts = body.getJSONObject("hits").getJSONObject("total").getInt("value");

        for (int i = 0; i < size; i++) {
            Object o = body.getJSONObject("hits").getJSONArray("hits").get(i);
            JSONObject oo = new JSONObject(o.toString());
            JSONObject auditItem = oo.getJSONObject("_source").getJSONObject("message").getJSONObject("auditItem");
        }

        System.out.println("counts: " + counts);
    }

    public void queryNo1() {

        // set desired filters and size
        JSONObject[] matches = new JSONObject[4];

        matches[0] = matchBuilder("message.auditItem.route", "/v1/user/verification");
        matches[1] = matchBuilder("message.auditItem.microServiceName", "ms-auth-server");
        matches[2] = matchBuilder("message.auditItem.rrn", "a628da97-8626-4c2f-8516-172660717d92");
        matches[3] = matchBuilderOperatorAND("message.auditItem.requestTime", "Jul 9, 2020 AM");
        // call Elasticsearch Service
        this.ElasticQuery("http://192.168.10.74:9200/newbabno/_search?_source_includes=message", 2, 4, matches);
    }

    private JSONObject matchBuilder(String fieldName, String fieldValue) {
        return new JSONObject().put("match", new JSONObject().put(fieldName, fieldValue));
    }

    private JSONObject matchBuilderOperatorAND(String fieldName, String fieldValue) {
        return new JSONObject().put("match", new JSONObject()
                .put(fieldName, new JSONObject()
                        .put("query", fieldValue)
                        .put("operator", "and")));
    }
}
