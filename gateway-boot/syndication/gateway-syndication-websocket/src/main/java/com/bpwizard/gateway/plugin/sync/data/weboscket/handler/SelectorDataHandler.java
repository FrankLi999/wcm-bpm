package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.common.dto.SelectorData;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;

/**
 * The type Selector data handler.
 */
@RequiredArgsConstructor
public class SelectorDataHandler extends AbstractDataHandler<SelectorData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    public List<SelectorData> convert(final String json) {
        return JsonUtils.fromList(json, SelectorData[].class);
    }

    @Override
    protected void doRefresh(final List<SelectorData> dataList) {
        pluginDataSubscriber.refreshSelectorDataSelf(dataList);
        dataList.forEach(pluginDataSubscriber::onSelectorSubscribe);
    }

    @Override
    protected void doUpdate(final List<SelectorData> dataList) {
        dataList.forEach(pluginDataSubscriber::onSelectorSubscribe);
    }

    @Override
    protected void doDelete(final List<SelectorData> dataList) {
        dataList.forEach(pluginDataSubscriber::unSelectorSubscribe);
    }
}
