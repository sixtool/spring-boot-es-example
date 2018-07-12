package com.twl.example.queryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

/**
 * Created by panwei on 2018/6/27.
 * 条件接口
 */
public interface ESCriterion {
    enum Operator {
        TERM, TERMS, RANGE, FUZZY, QUERY_STRING, MISSING
    }

    enum MatchMode {
        START, END, ANYWHERE
    }

    enum Projection {
        MAX, MIN, AVG, LENGTH, SUM, COUNT
    }

    List<QueryBuilder> listBuilders();
}
