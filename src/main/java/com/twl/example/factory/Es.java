package com.twl.example.factory;


import com.twl.example.queryBuilder.ESQueryBuilderConstructor;
import com.twl.example.resp.RespObject;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by panwei on 2018/6/26.
 */
public class Es {
    private Logger logger = LoggerFactory.getLogger(Es.class);

    private final static int MAX = 10000;

    private static TransportClient client;
    // 集群名,默认值elasticsearch
//    private static final String CLUSTER_NAME = DeployUtil.getEsClusterName();
    private static final String CLUSTER_NAME = "elasticsearch";

    public Es() {
        // 通过 setting对象来指定集群配置信息
        Settings settings = Settings.builder() //
                //.put("cluster.name", CLUSTER_NAME)  // 设置ES实例的名称
                .put("client.transport.sniff", false) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                .put("client.transport.ignore_cluster_name", false) // 设置 true ，忽略连接节点集群名验证
                // .put("client.transport.ping_timeout", 5) // ping一个节点的响应时间 默认5秒
                // .put("client.transport.nodes_sampler_interval", 5) // sample/ping 节点的时间间隔，默认是5s
                .build();
        client = new PreBuiltTransportClient(settings);
//        client = new PreBuiltXPackTransportClient(settings);

        try {
//            client.addTransportAddress(new TransportAddress(InetAddress.getByName(DeployUtil.getEsHost()), DeployUtil.getEsPort()));
            client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        logger.info("success connect es");
    }


    /**
     * 关闭连接
     */
    public void close() {
        client.close();
    }

    /**
     * 验证链接是否正常
     *
     * @return
     */
    public boolean validate() {
        return client.connectedNodes().size() != 0;
    }

    /*********************************************index:库*******************************************************/

    /**
     * 验证索引是否存在
     *
     * @param index
     * @return
     */
    public boolean isExistsIndex(String index) {
        IndicesAdminClient indicesAdminClient = client.admin().indices(); // 获取IndicesAdminClient对象
        IndicesExistsResponse response = indicesAdminClient.prepareExists(index).get();
        return response.isExists();
    }

    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public boolean createIndex(String index) {
        IndicesAdminClient indicesAdminClient = client.admin().indices(); // 获取IndicesAdminClient对象
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index.toLowerCase()).get();
        return response.isAcknowledged();
    }

    /**
     * 创建索引
     *
     * @param index    索引名
     * @param shards   分片数
     * @param replicas 副本数
     * @return
     */
    public static boolean createIndex(String index, int shards, int replicas) {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
                .build();
        IndicesAdminClient indicesAdminClient = client.admin().indices(); // 获取IndicesAdminClient对象
        CreateIndexResponse response = indicesAdminClient.prepareCreate(index.toLowerCase())
                .setSettings(settings)
                .execute().actionGet();
        return response.isAcknowledged();
    }

    /**
     * 给索引index设置mapping
     *
     * @param index
     * @param type
     * @param mapping
     */
    public static void setMapping(String index, String type, String mapping) {
        IndicesAdminClient indicesAdminClient = client.admin().indices(); // 获取IndicesAdminClient对象
        indicesAdminClient.preparePutMapping(index)
                .setType(type)
                .setSource(mapping, XContentType.JSON)
                .get();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index) {
        IndicesAdminClient indicesAdminClient = client.admin().indices(); // 获取IndicesAdminClient对象
        DeleteIndexResponse response = indicesAdminClient
                .prepareDelete(index.toLowerCase())
                .execute()
                .actionGet();
        return response.isAcknowledged();
    }

    /*********************************************doc:record*******************************************************/

    /**
     * 添加记录
     *
     * @param index: 索引-库
     * @param type:  类型-表  *6.x一个索引只能有一个类型
     * @param json
     */
    public void insertData(String index, String type, String json) {
        client.prepareIndex(index, type).setSource(json).get();
    }

    /**
     * 添加记录
     *
     * @param index: 索引-库
     * @param type:  类型-表  *6.x一个索引只能有一个类型
     * @param id
     * @param json
     */
    public void insertData(String index, String type, Object id, String json) {
        client.prepareIndex(index, type, id.toString()).setSource(json).get();
    }

    /**
     * 批量添加记录
     *
     * @param index:   索引-库
     * @param type:    类型-表  *6.x一个索引只能有一个类型
     * @param jsonList
     */
    public void bulkInsertData(String index, String type, List<String> jsonList) {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        jsonList.forEach(json -> bulkRequestBuilder.add(client.prepareIndex(index, type).setSource(json)));
        bulkRequestBuilder.get();
    }

    /**
     * 批量添加记录
     *
     * @param index: 索引-库
     * @param type:  类型-表  *6.x一个索引只能有一个类型
     * @param data:  (_id 主键, json 数据)
     */
    public void bulkInsertData(String index, String type, HashMap<String, String> data) {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        data.forEach((id, json) -> bulkRequestBuilder.add(client.prepareIndex(index, type, id).setSource(json)));
        bulkRequestBuilder.get();
    }

    /**
     * 更新记录
     *
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public void updateData(String index, String type, Object id, String json) {
        client.prepareUpdate(index, type, id.toString()).setDoc(json).get();
    }

    /**
     * 删除记录
     *
     * @param index
     * @param type
     * @param id
     */
    public void deleteData(String index, String type, Object id) {
        client.prepareDelete(index, type, id.toString()).get();
    }

    /**
     * 查询
     *log4j.properties
     * @param index
     * @param type
     * @param constructor
     * @return
     */
    public RespObject search(String index, String type, ESQueryBuilderConstructor constructor) {
        RespObject respObject = new RespObject();
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);
        // 排序
        String asc = constructor.getAsc(), desc = constructor.getDesc();
        if (StringUtils.isNotBlank(asc)) {
            searchRequestBuilder.addSort(asc, SortOrder.ASC);
        }
        if (StringUtils.isNotBlank(desc)) {
            searchRequestBuilder.addSort(desc, SortOrder.DESC);
        }
        // 设置查询类型
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        // 设置查询体
        searchRequestBuilder.setQuery(constructor.listBuilders());
        // 设置分页
        int from = constructor.getFrom(), size = constructor.getSize();
        if (from < 0) {
            from = 0;
        }
        if (size < 0) {
            size = 0;
        }
        if (size > MAX) {
            size = MAX;
        }
        int currentPage = from == 0 ? 0 : (from - 1) * size;
        searchRequestBuilder.setFrom(currentPage).setSize(size);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit sh : hits.getHits()) {
            list.add(sh.getSourceAsMap());
        }
        Map<String, Object> rtn = new HashMap<>();
        rtn.put("totalCount", hits.getTotalHits());
        rtn.put("list", list);
        respObject.setData(rtn);
        return respObject;
    }

    public static void main(String[] args) {
        Es es = new Es();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setFrom(0);
        constructor.setSize(10);
        constructor.setDesc("update_time");
        RespObject respObject = es.search("case_detail", "doc", constructor);
        System.out.println("respObject===>" + respObject.toString());
    }

}
