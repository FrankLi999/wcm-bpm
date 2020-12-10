package com.bpwizard.gateway.plugin.api.sofa;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The interface Generic param service.
 * This service is used to construct the parameters required for the sofa generalization.
 */
public interface SofaParamResolveService {

    /**
     * Build parameter pair.
     * this is Resolve http body to get sofa param.
     *
     * @param body           the body
     * @param parameterTypes the parameter types
     * @return the pair
     */
    Pair<String[], Object[]> buildParameter(String body, String parameterTypes);
}
