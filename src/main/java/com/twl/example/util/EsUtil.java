package com.twl.example.util;



import com.twl.example.factory.Es;
import com.twl.example.factory.EsFactory;
import com.twl.example.queryBuilder.ESQueryBuilderConstructor;
import com.twl.example.resp.RespObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 2018/6/26.
 */
public class EsUtil {

    // 初始化一个池子实例
    private static GenericObjectPool<Es> pool;

    static {
        // 池子配置文件
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);// 整个池最大值
        config.setMaxIdle(10);// 最大空闲
        config.setMinIdle(2);// 最小空闲
        config.setMaxWaitMillis(-1);// 最大等待时间，-1表示一直等
        config.setBlockWhenExhausted(true);// 当对象池没有空闲对象时，新的获取对象的请求是否阻塞。true阻塞。默认值是true
        config.setTestOnBorrow(false);// 在从对象池获取对象时是否检测对象有效，true是；默认值是false
        config.setTestOnReturn(false);// 在向对象池中归还对象时是否检测对象有效，true是，默认值是false
        config.setTestWhileIdle(true);// 在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。true是，默认值是false
        config.setMinEvictableIdleTimeMillis(10 * 60000L); // 可发呆的时间,10mins
        config.setTestWhileIdle(true); // 发呆过长移除的时候是否test一下先

        pool = new GenericObjectPool<>(new EsFactory(), config);
    }

    /*********************************************index:库*******************************************************/

    /**
     * 验证索引是否存在
     *
     * @param index
     */
    public static void isExistsIndex(String index) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.isExistsIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 创建索引
     *
     * @param index
     */
    public static void createIndex(String index) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.createIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 创建索引
     *
     * @param index 索引名
     * @param shards    分片数
     * @param replicas  副本数
     */
    public static void createIndex(String index, int shards, int replicas) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.createIndex(index, shards, replicas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 给索引indexName设置mapping
     *
     * @param index
     * @param type
     * @param mapping
     */
    public static void setMapping(String index, String type, String mapping) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.setMapping(index, type, mapping);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static void deleteIndex(String index) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.deleteIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }


    /*********************************************doc:record*******************************************************/

    /**
     * 添加记录
     *
     * @param index
     * @param type
     * @param json
     */
    public static void insertData(String index, String type, String json) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.insertData(index, type,  json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 添加记录
     *
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public static void insertData(String index, String type, Object id, String json) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.insertData(index, type, id, json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 批量添加记录
     *
     * @param index: 索引-库
     * @param type:  类型-表  *6.x一个索引只能有一个类型
     * @param jsonList
     */
    public void bulkInsertData(String index, String type, List<String> jsonList) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.bulkInsertData(index, type, jsonList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 批量添加记录
     *
     * @param index: 索引-库
     * @param type:  类型-表  *6.x一个索引只能有一个类型
     * @param data: (_id 主键, json 数据)
     */
    public void bulkInsertData(String index, String type, HashMap<String, String> data) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.bulkInsertData(index, type, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 更新记录
     *
     * @param index
     * @param type
     * @param id
     * @param json
     */
    public static void updateData(String index, String type, Object id, String json) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.updateData(index, type, id, json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 删除记录
     *
     * @param index
     * @param type
     * @param id
     */
    public static void deleteData(String index, String type, Object id) {
        Es es = null;
        try {
            es = pool.borrowObject();
            es.deleteData(index, type, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
    }

    /**
     * 查询记录
     *
     * @param index
     * @param type
     * @param constructor
     */
    public static RespObject search(String index, String type, ESQueryBuilderConstructor constructor) {
        RespObject respObject = new RespObject();
        Es es = null;
        try {
            es = pool.borrowObject();
            respObject = es.search(index, type, constructor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return respObject;
    }

    public static void main(String[] args) {
//        String index = "case_detail", type = "doc";
//        // 构建查询条件构造器
//        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
//        constructor.must(new ESQueryBuilders().term("app_id", "988888").range("case_status", "1100", "1200")); // must: and, mustNot: not, should: or
//        // 分页
////        constructor.setFrom(0);
////        constructor.setSize(10);
//        // 排序
//        constructor.setDesc("create_time");
//        constructor.setDesc("update_time");
//        RespObject respObject = EsUtil.search(index, type, constructor);
//        System.out.println(respObject);

        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setFrom(0);
        constructor.setSize(10);
        constructor.setDesc("update_time");
        RespObject respObject = new Es().search("case_detail_list", "doc", constructor);
        System.out.println("respObject===>" + respObject.toString());
    }


}
