package com.twl.example.factory;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by panwei on 2018/6/26.
 */
public class EsFactory extends BasePooledObjectFactory<Es> {
    private Logger logger = LoggerFactory.getLogger(EsFactory.class);

    @Override
    public Es create() throws Exception {
        logger.info("new es");
        return new Es();
    }

    @Override
    public PooledObject<Es> wrap(Es es) {
        return new DefaultPooledObject<>(es);
    }

    @Override
    public void destroyObject(PooledObject<Es> p) throws Exception {
        p.getObject().close();
        logger.info("destroy es");
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<Es> p) {
        logger.info("validate es");
        return p.getObject().validate();
    }


}
