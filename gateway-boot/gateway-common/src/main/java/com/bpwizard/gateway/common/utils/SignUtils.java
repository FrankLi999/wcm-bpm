package com.bpwizard.gateway.common.utils;

import org.springframework.util.DigestUtils;

import com.bpwizard.gateway.common.constant.Constants;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * SignUtils.
 */
public final class SignUtils {

    private static final SignUtils SIGN_UTILS = new SignUtils();

    private SignUtils() {
    }

    /**
     * getInstance.
     *
     * @return {@linkplain SignUtils}
     */
    public static SignUtils getInstance() {
        return SIGN_UTILS;
    }

    /**
     * acquired sign.
     *
     * @param signKey sign key
     * @param params  params
     * @return sign
     */
    public static String generateSign(final String signKey, final Map<String, String> params) {
        List<String> storedKeys = Arrays.stream(params.keySet()
                .toArray(new String[]{}))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        final String sign = storedKeys.stream()
                .filter(key -> !Objects.equals(key, Constants.SIGN))
                .map(key -> String.join("", key, params.get(key)))
                .collect(Collectors.joining()).trim()
                .concat(signKey);
        return DigestUtils.md5DigestAsHex(sign.getBytes()).toUpperCase();
    }

    /**
     * isValid.
     *
     * @param sign    sign
     * @param params  params
     * @param signKey signKey
     * @return boolean boolean
     */
    public boolean isValid(final String sign, final Map<String, String> params, final String signKey) {
        return Objects.equals(sign, generateSign(signKey, params));
    }

    /**
     * Generate key string.
     *
     * @return the string
     */
    public String generateKey() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
