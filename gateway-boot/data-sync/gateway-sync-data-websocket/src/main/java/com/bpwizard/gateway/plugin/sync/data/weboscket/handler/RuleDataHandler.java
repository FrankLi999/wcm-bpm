package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.common.dto.RuleData;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;

/**
 * The type rule data handler.
 */
@RequiredArgsConstructor
public class RuleDataHandler extends AbstractDataHandler<RuleData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    public List<RuleData> convert(final String json) {
        return JsonUtils.fromList(json, RuleData[].class);
    }

    @Override
    protected void doRefresh(final List<RuleData> dataList) {
        pluginDataSubscriber.refreshRuleDataSelf(dataList);
        dataList.forEach(pluginDataSubscriber::onRuleSubscribe);
    }

    @Override
    protected void doUpdate(final List<RuleData> dataList) {
        dataList.forEach(pluginDataSubscriber::onRuleSubscribe);
    }

    @Override
    protected void doDelete(final List<RuleData> dataList) {
        dataList.forEach(pluginDataSubscriber::unRuleSubscribe);
    }
}
