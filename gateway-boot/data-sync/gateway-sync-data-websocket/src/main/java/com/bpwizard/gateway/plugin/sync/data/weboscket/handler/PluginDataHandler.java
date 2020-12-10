package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.common.dto.PluginData;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;

/**
 * The type Plugin data handler.
 */
@RequiredArgsConstructor
public class PluginDataHandler extends AbstractDataHandler<PluginData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    public List<PluginData> convert(final String json) {
        return JsonUtils.fromList(json, PluginData[].class);
    }

    @Override
    protected void doRefresh(final List<PluginData> dataList) {
        pluginDataSubscriber.refreshPluginDataSelf(dataList);
        dataList.forEach(pluginDataSubscriber::onSubscribe);
    }

    @Override
    protected void doUpdate(final List<PluginData> dataList) {
        dataList.forEach(pluginDataSubscriber::onSubscribe);
    }

    @Override
    protected void doDelete(final List<PluginData> dataList) {
        dataList.forEach(pluginDataSubscriber::unSubscribe);
    }

}
