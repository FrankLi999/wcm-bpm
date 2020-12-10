package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;

/**
 * The type Meta data handler.
 */
@RequiredArgsConstructor
public class MetaDataHandler extends AbstractDataHandler<MetaData> {

    private final List<MetaDataSubscriber> metaDataSubscribers;

    @Override
    public List<MetaData> convert(final String json) {
        return JsonUtils.fromList(json, MetaData[].class);
    }

    @Override
    protected void doRefresh(final List<MetaData> dataList) {
        metaDataSubscribers.forEach(MetaDataSubscriber::refresh);
        dataList.forEach(metaData -> metaDataSubscribers.forEach(metaDataSubscriber -> metaDataSubscriber.onSubscribe(metaData)));
    }

    @Override
    protected void doUpdate(final List<MetaData> dataList) {
        dataList.forEach(metaData -> metaDataSubscribers.forEach(metaDataSubscriber -> metaDataSubscriber.onSubscribe(metaData)));
    }

    @Override
    protected void doDelete(final List<MetaData> dataList) {
        dataList.forEach(metaData -> metaDataSubscribers.forEach(metaDataSubscriber -> metaDataSubscriber.unSubscribe(metaData)));
    }
}
