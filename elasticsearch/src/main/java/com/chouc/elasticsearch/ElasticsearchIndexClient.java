package com.chouc.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
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
public class ElasticsearchIndexClient {
    RestHighLevelClient restHighLevelClient = null;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void start(){
        logger.info("start");
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
    }


    @Test
    public void createIndex(){
        CreateIndexRequest request = new CreateIndexRequest("users");
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            logger.info("operate acknowledged:{}",acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIndex(){
        GetIndexRequest request = new GetIndexRequest("users");
        try {
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
            logger.info("index alias:{}",getIndexResponse.getAliases());
            logger.info("index mapping:{}",getIndexResponse.getMappings());
            logger.info("index setting:{}",getIndexResponse.getSettings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void deleteIndex(){
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("users");
        try {
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            logger.info("operate acknowledgedResponse:{}",acknowledgedResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void end(){
        try {
            logger.info("end");
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
