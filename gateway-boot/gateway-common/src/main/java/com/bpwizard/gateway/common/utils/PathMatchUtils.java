package com.bpwizard.gateway.common.utils;

import java.util.Arrays;

//import com.google.common.base.Splitter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/**
 * The type Path match utils.
 */
public class PathMatchUtils {
    
    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    
    /**
     * Match boolean.
     *
     * @param matchUrls the ignore urls
     * @param path      the path
     * @return the boolean
     */
    public static boolean match(final String matchUrls, final String path) {
    	// new StringTokenizer(matchUrls, ",");
        // return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(matchUrls).stream().anyMatch(url -> reg(url, path));
    	return Arrays.stream(matchUrls.split(",")).filter(StringUtils::hasText).anyMatch(url -> reg(url, path));
    }
    
    private static boolean reg(final String pattern, final String path) {
        return MATCHER.match(pattern, path);
    }
    
}
