package com.example.demo.source;


import com.hazelcast.jet.Traverser;
import com.hazelcast.jet.Traversers;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.pipeline.BatchSource;
import com.hazelcast.jet.pipeline.Sources;
import com.hazelcast.spring.context.SpringAware;
import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static com.hazelcast.jet.core.ProcessorMetaSupplier.preferLocalParallelismOne;

/**
 * A processor which uses auto-wired dao to find all users and emit them to downstream.
 * {@code @SpringAware} annotation enables this auto-wiring functionality.
 */
@SpringAware
public class CustomSourceP extends AbstractProcessor {

    private Traverser<User> traverser;

    @Autowired
    private transient UserDao userDao;

    @Override
    public boolean isCooperative() {
        return false;
    }

    @Override
    protected void init(@Nonnull Context context) {
        traverser = Traversers.traverseIterable(userDao.findAll());
    }

    @Override
    public boolean complete() {
        return emitFromTraverser(traverser);
    }

    public static BatchSource<User> customSource() {
        return Sources.batchFromProcessor("custom-source", preferLocalParallelismOne(CustomSourceP::new));
    }
}