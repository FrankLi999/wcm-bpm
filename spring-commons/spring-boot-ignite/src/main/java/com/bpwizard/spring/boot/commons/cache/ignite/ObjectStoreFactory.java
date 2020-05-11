package com.bpwizard.spring.boot.commons.cache.ignite;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.internal.IgniteComponentType;
import org.apache.ignite.internal.util.spring.IgniteSpringHelper;
import org.apache.ignite.internal.util.tostring.GridToStringExclude;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.apache.ignite.resources.SpringApplicationContextResource;

public class ObjectStoreFactory<K, V> implements Factory<ObjectStore<K, V>> {
    /** */
    private static final long serialVersionUID = 0L;

    /** Connection URL. */
    private String connUrl = ObjectStore.DFLT_CONN_URL;

    /** Query to create table. */
    private String createTblQry = ObjectStore.DFLT_CREATE_TBL_QRY;

    /** Query to load entry. */
    private String loadQry = ObjectStore.DFLT_LOAD_QRY;

    /** Query to update entry. */
    private String updateQry = ObjectStore.DFLT_UPDATE_QRY;

    /** Query to insert entries. */
    private String insertQry = ObjectStore.DFLT_INSERT_QRY;

    /** Query to delete entries. */
    private String delQry = ObjectStore.DFLT_DEL_QRY;

    /** User name for database access. */
    private String user;

    /** Password for database access. */
    @GridToStringExclude
    private String passwd;

    /** Flag for schema initialization. */
    private boolean initSchema = true;

    /** Name of data source bean. */
    private String dataSrcBean;

    /** Data source. */
    private transient DataSource dataSrc;

    /** Application context. */
    @SpringApplicationContextResource
    private Object appCtx;

    /** {@inheritDoc} */
    @Override public ObjectStore<K, V> create() {
    	ObjectStore<K, V> store = new ObjectStore<>();

        store.setInitSchema(initSchema);
        store.setConnectionUrl(connUrl);
        store.setCreateTableQuery(createTblQry);
        store.setLoadQuery(loadQry);
        store.setUpdateQuery(updateQry);
        store.setInsertQuery(insertQry);
        store.setDeleteQuery(delQry);
        store.setUser(user);
        store.setPassword(passwd);

        if (dataSrc != null)
            store.setDataSource(dataSrc);
        else if (dataSrcBean != null) {
            if (appCtx == null)
                throw new IgniteException("Spring application context resource is not injected.");

            IgniteSpringHelper spring;

            try {
                spring = IgniteComponentType.SPRING.create(false);

                DataSource data = spring.loadBeanFromAppContext(appCtx, dataSrcBean);

                store.setDataSource(data);
            }
            catch (IgniteCheckedException ignored) {
                throw new IgniteException("Failed to load bean in application context [beanName=" + dataSrcBean +
                    ", igniteConfig=" + appCtx + ']');
            }
        }

        return store;
    }

    /**
     * Flag indicating whether DB schema should be initialized by Ignite (default behaviour) or
     * was explicitly created by user.
     *
     * @param initSchema Initialized schema flag.
     * @return {@code This} for chaining.
     * @see ObjectStore#setInitSchema(boolean)
     */
    public ObjectStoreFactory<K, V> setInitSchema(boolean initSchema) {
        this.initSchema = initSchema;

        return this;
    }

    /**
     * Sets connection URL.
     *
     * @param connUrl Connection URL.
     * @return {@code This} for chaining.
     * @see ObjectStore#setConnectionUrl(String)
     */
    public ObjectStoreFactory<K, V> setConnectionUrl(String connUrl) {
        this.connUrl = connUrl;

        return this;
    }

    /**
     * Sets create table query.
     *
     * @param createTblQry Create table query.
     * @return {@code This} for chaining.
     * @see ObjectStore#setCreateTableQuery(String)
     */
    public ObjectStoreFactory<K, V> setCreateTableQuery(String createTblQry) {
        this.createTblQry = createTblQry;

        return this;
    }

    /**
     * Sets load query.
     *
     * @param loadQry Load query
     * @return {@code This} for chaining.
     * @see ObjectStore#setLoadQuery(String)
     */
    public ObjectStoreFactory<K, V> setLoadQuery(String loadQry) {
        this.loadQry = loadQry;

        return this;
    }

    /**
     * Sets update entry query.
     *
     * @param updateQry Update entry query.
     * @return {@code This} for chaining.
     * @see  ObjectStore#setUpdateQuery(String)
     */
    public ObjectStoreFactory<K, V> setUpdateQuery(String updateQry) {
        this.updateQry = updateQry;

        return this;
    }

    /**
     * Sets insert entry query.
     *
     * @param insertQry Insert entry query.
     * @return {@code This} for chaining.
     * @see ObjectStore#setInsertQuery(String)
     */
    public ObjectStoreFactory<K, V> setInsertQuery(String insertQry) {
        this.insertQry = insertQry;

        return this;
    }

    /**
     * Sets delete entry query.
     *
     * @param delQry Delete entry query.
     * @return {@code This} for chaining.
     * @see ObjectStore#setDeleteQuery(String)
     */
    public ObjectStoreFactory<K, V> setDeleteQuery(String delQry) {
        this.delQry = delQry;

        return this;
    }

    /**
     * Sets user name for database access.
     *
     * @param user User name.
     * @return {@code This} for chaining.
     * @see ObjectStore#setUser(String)
     */
    public ObjectStoreFactory<K, V> setUser(String user) {
        this.user = user;

        return this;
    }

    /**
     * Sets password for database access.
     *
     * @param passwd Password.
     * @return {@code This} for chaining.
     * @see ObjectStore#setPassword(String)
     */
    public ObjectStoreFactory<K, V> setPassword(String passwd) {
        this.passwd = passwd;

        return this;
    }

    /**
     * Sets name of the data source bean.
     *
     * @param dataSrcBean Data source bean name.
     * @return {@code This} for chaining.
     * @see ObjectStore#setDataSource(DataSource)
     */
    public ObjectStoreFactory<K, V> setDataSourceBean(String dataSrcBean) {
        this.dataSrcBean = dataSrcBean;

        return this;
    }

    /**
     * Sets data source. Data source should be fully configured and ready-to-use.
     *
     * @param dataSrc Data source.
     * @return {@code This} for chaining.
     * @see ObjectStore#setDataSource(DataSource)
     */
    public ObjectStoreFactory<K, V> setDataSource(DataSource dataSrc) {
        this.dataSrc = dataSrc;

        return this;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(ObjectStoreFactory.class, this);
    }
}
