package com.bpwizard.gateway.plugin.sync.data.weboscket.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.common.dto.AppAuthData;
import com.bpwizard.gateway.common.dto.AuthParamData;
import com.bpwizard.gateway.common.dto.AuthPathData;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.google.gson.Gson;

/**
 * Test cases for {@link AuthDataHandler}.
 */
public final class AuthDataHandlerTest {

    private final List<AuthDataSubscriber> authDataSubscribers;

    private final AuthDataHandler authDataHandler;

    public AuthDataHandlerTest() {
        authDataSubscribers = new LinkedList<>();
        authDataSubscribers.add(mock(AuthDataSubscriber.class));
        authDataSubscribers.add(mock(AuthDataSubscriber.class));
        authDataSubscribers.add(mock(AuthDataSubscriber.class));
        authDataHandler = new AuthDataHandler(authDataSubscribers);
    }

    /**
     * test case for {@link AuthDataHandler#convert(String)}.
     */
    @Test
    public void testConvert() {
        AppAuthData appAuthData = createFakerAppAuthDataObjects(1).get(0);
        setAppAuthDataProperties(appAuthData);
        List<AppAuthData> sources = Collections.singletonList(appAuthData);
        Gson gson = new Gson();
        String json = gson.toJson(sources);
        List<AppAuthData> appAuthDataResults = authDataHandler.convert(json);
        Assertions.assertEquals(appAuthData, appAuthDataResults.get(0));
    }

    private void setAppAuthDataProperties(final AppAuthData appAuthData) {
        appAuthData.setAppKey("appKey");
        appAuthData.setAppSecret("appSecret");
        appAuthData.setEnabled(true);
        List<AuthParamData> authParamDataList = new LinkedList<>();
        authParamDataList.add(new AuthParamData("appName1", "appParam1"));
        authParamDataList.add(new AuthParamData("appName2", "appParam2"));
        appAuthData.setParamDataList(authParamDataList);
        List<AuthPathData> authPathDataList = new LinkedList<>();
        authPathDataList.add(new AuthPathData("appName1", "path1", true));
        authPathDataList.add(new AuthPathData("appName2", "path2", false));
        appAuthData.setPathDataList(authPathDataList);
    }

    /**
     * test case for {@link AuthDataHandler#doRefresh(List)}.
     * First,verify that each AuthDataSubscriber bean has called the {@link AuthDataSubscriber#refresh()} method.
     * then,verify that each AuthDataSubscriber bean has called the {@link AuthDataSubscriber#onSubscribe(AppAuthData)} method.
     */
    @Test
    public void testDoRefresh() {
        List<AppAuthData> appAuthDataList = createFakerAppAuthDataObjects(3);
        authDataHandler.doRefresh(appAuthDataList);
        authDataSubscribers.forEach(authDataSubscriber -> verify(authDataSubscriber).refresh());
        appAuthDataList.forEach(appAuthData ->
                authDataSubscribers.forEach(authDataSubscriber -> verify(authDataSubscriber).onSubscribe(appAuthData)));
    }

    /**
     * test case for {@link AuthDataHandler#doUpdate(List)}.
     * verify that each AuthDataSubscriber bean has called the {@link AuthDataSubscriber#onSubscribe(AppAuthData)} method.
     */
    @Test
    public void testDoUpdate() {
        List<AppAuthData> appAuthDataList = createFakerAppAuthDataObjects(4);
        authDataHandler.doUpdate(appAuthDataList);
        appAuthDataList.forEach(appAuthData ->
                authDataSubscribers.forEach(authDataSubscriber -> verify(authDataSubscriber).onSubscribe(appAuthData)));
    }

    /**
     * test case for {@link AuthDataHandler#doDelete(List)}.
     * verify that each AuthDataSubscriber bean has called the {@link AuthDataSubscriber#unSubscribe(AppAuthData)} method.
     */
    @Test
    public void testDoDelete() {
        List<AppAuthData> appAuthDataList = createFakerAppAuthDataObjects(3);
        authDataHandler.doDelete(appAuthDataList);
        appAuthDataList.forEach(appAuthData ->
                authDataSubscribers.forEach(authDataSubscriber -> verify(authDataSubscriber).unSubscribe(appAuthData)));
    }

    private List<AppAuthData> createFakerAppAuthDataObjects(final int count) {
        List<AppAuthData> result = new LinkedList<>();
        for (int i = 1; i <= count; i++) {
            AppAuthData appAuthData = new AppAuthData();
            appAuthData.setAppKey("appKey-" + i);
            result.add(appAuthData);
        }
        return result;
    }
}
