package com.chouc.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author chouc
 * @version V1.0
 * @Title: ElasticsearchClient
 * @Package com.chouc.elasticsearch
 * @Description:
 * @date 2021/5/11
 */
public class ElasticsearchDocClient {
    RestHighLevelClient restHighLevelClient = null;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void start() {
        logger.info("start");
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    @Test
    public void createDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("users").id("1001");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "梅谢兵");
        jsonObject.put("age", 18);
        jsonObject.put("sex", "男");
        indexRequest.source(jsonObject.toJSONString(), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        logger.info("create doc result:{}", indexResponse.getResult());
        logger.info("create doc shardinfo:{}", indexResponse.getShardInfo());
    }

    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("users").id("1001");
        updateRequest.doc(XContentType.JSON, "sex", "女");
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        logger.info("update doc result:{}", updateResponse.status());
        logger.info("update doc shardinfo:{}", updateResponse.getShardInfo());
    }


    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest = new GetRequest();
        getRequest.index("users").id("1001");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", getResponse.getSourceAsString());
    }

    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("users").id("1001");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", deleteResponse.status());
    }


    @Test
    public void bulkInsertDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("users").source(XContentType.JSON, "name", "wangwu1", "age", "18", "sex", "男"));
        bulkRequest.add(new IndexRequest().index("users").source(XContentType.JSON, "name", "wangwu2", "age", "19", "sex", "女"));
        bulkRequest.add(new IndexRequest().index("users").source(XContentType.JSON, "name", "wangwu33", "age", "20", "sex", "男"));
        bulkRequest.add(new IndexRequest().index("users").source(XContentType.JSON, "name", "wang1wu", "age", "21", "sex", "女"));
        bulkRequest.add(new IndexRequest().index("users").source(XContentType.JSON, "name", "wang1wu2", "age", "22", "sex", "男"));
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", bulkResponse.status());
        logger.info("get doc items:{}", bulkResponse.getItems());
    }

    @Test
    public void bulkDeleteDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest().index("users").id("TJkAXHkBUcmF3PWTaZKi"));
        bulkRequest.add(new DeleteRequest().index("users").id("TZkAXHkBUcmF3PWTaZKl"));
        bulkRequest.add(new DeleteRequest().index("users").id("TpkAXHkBUcmF3PWTaZKl"));
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", bulkResponse.status());
        logger.info("get doc items:{}", bulkResponse.getItems());
    }


    @Test
    public void searchDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users")
                .source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }

    @Test
    public void searchWithConditionDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "李");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(matchQueryBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }


    @Test
    public void searchWithPageDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "李");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(matchQueryBuilder);
        searchSourceBuilder.from(1).size(2);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }

    @Test
    public void searchWithSortDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "李");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(matchQueryBuilder);
        searchSourceBuilder.sort("age", SortOrder.DESC);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }


    @Test
    public void searchWithSepicfyColumnDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "李");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(matchQueryBuilder);
        String[] excludes = {};
        String[] includes = {"name"};
        searchSourceBuilder.fetchSource(includes, excludes);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }


    @Test
    public void searchMutiConditionDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchQuery("age",18));
//        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex","男"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", 18));
        boolQueryBuilder.should(QueryBuilders.matchQuery("sex", "男"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }

    @Test
    public void searchRangeDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 18 > and < 19
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age")
                .lte("19").gte(18);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder).postFilter(rangeQueryBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }

    @Test
    public void searchFuzzyDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "wangwu").fuzziness(Fuzziness.TWO);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(fuzzyQueryBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
        }
    }

    @Test
    public void searchHighlightDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("name");
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "wangwu").fuzziness(Fuzziness.TWO);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(fuzzyQueryBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc getTotalHits:{}", hits.getTotalHits());
        for (SearchHit searchHit : hits) {
            logger.info("his:{}", searchHit.getSourceAsString());
            logger.info("his:{}", searchHit.getHighlightFields());
        }
    }


    @Test
    public void aggreateMaxDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String aggregationName = "max_age";
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max(aggregationName).field("age");
        searchSourceBuilder.aggregation(maxAggregationBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc searchResponse:{}", searchResponse.toString());
        ParsedMax parsedMax = searchResponse.getAggregations().get(aggregationName);
        logger.info("max:{}",parsedMax.getValue());
    }


    @Test
    public void aggreateGroupDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String aggregationName = "group_age";
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(aggregationName).field("age");
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchRequest.indices("users")
                .source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        logger.info("get doc result:{}", searchResponse.status());
        logger.info("get doc took:{}", searchResponse.getTook());
        SearchHits hits = searchResponse.getHits();
        logger.info("get doc searchResponse:{}", searchResponse.toString());
        Terms terms = searchResponse.getAggregations().get(aggregationName);
        for (Terms.Bucket bucket:terms.getBuckets()){
            logger.info("age:{} count:{}",bucket.getKey(),bucket.getDocCount());
        }
    }

    @After
    public void end() {
        try {
            logger.info("end");
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
