package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.bpwizard.gateway.common.enums.DataEventTypeEnum;

/**
 * The type Abstract data handler.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractDataHandler<T> implements DataHandler {

    /**
     * Convert list.
     *
     * @param json the json
     * @return the list
     */
    protected abstract List<T> convert(String json);

    /**
     * Do refresh.
     *
     * @param dataList the data list
     */
    protected abstract void doRefresh(List<T> dataList);

    /**
     * Do update.
     *
     * @param dataList the data list
     */
    protected abstract void doUpdate(List<T> dataList);

    /**
     * Do delete.
     *
     * @param dataList the data list
     */
    protected abstract void doDelete(List<T> dataList);

    @Override
    public void handle(final String json, final String eventType) {
        List<T> dataList = convert(json);
        if (!ObjectUtils.isEmpty(dataList)) {
            DataEventTypeEnum eventTypeEnum = DataEventTypeEnum.acquireByName(eventType);
            switch (eventTypeEnum) {
                case REFRESH:
                case MYSELF:
                    doRefresh(dataList);
                    break;
                case UPDATE:
                case CREATE:
                    doUpdate(dataList);
                    break;
                case DELETE:
                    doDelete(dataList);
                    break;
                default:
                    break;
            }
        }
    }
}
