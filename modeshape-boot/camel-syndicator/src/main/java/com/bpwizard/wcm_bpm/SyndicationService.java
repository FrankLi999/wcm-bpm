package com.bpwizard.wcm_bpm;

import org.springframework.stereotype.Service;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Service("syndicationService")
public class SyndicationService {

    public void syndicate(Syndication syndication) {
        System.out.println(">>>>>>>>>>>>>>>> do syndication:" +syndication.getName());
    }
}
