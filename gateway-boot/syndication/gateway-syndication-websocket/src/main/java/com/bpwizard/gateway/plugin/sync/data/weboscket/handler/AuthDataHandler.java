package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.common.dto.AppAuthData;
import com.bpwizard.gateway.common.utils.JsonUtils;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;

/**
 * The type Auth data handler.
 */
@RequiredArgsConstructor
public class AuthDataHandler extends AbstractDataHandler<AppAuthData> {

    private final List<AuthDataSubscriber> authDataSubscribers;

    @Override
    public List<AppAuthData> convert(final String json) {
        return JsonUtils.fromList(json, AppAuthData[].class);
    }

    @Override
    protected void doRefresh(final List<AppAuthData> dataList) {
        authDataSubscribers.forEach(AuthDataSubscriber::refresh);
        dataList.forEach(appAuthData -> authDataSubscribers.forEach(authDataSubscriber -> authDataSubscriber.onSubscribe(appAuthData)));
    }

    @Override
    protected void doUpdate(final List<AppAuthData> dataList) {
        dataList.forEach(appAuthData -> authDataSubscribers.forEach(authDataSubscriber -> authDataSubscriber.onSubscribe(appAuthData)));
    }

    @Override
    protected void doDelete(final List<AppAuthData> dataList) {
        dataList.forEach(appAuthData -> authDataSubscribers.forEach(authDataSubscriber -> authDataSubscriber.unSubscribe(appAuthData)));
    }
}
