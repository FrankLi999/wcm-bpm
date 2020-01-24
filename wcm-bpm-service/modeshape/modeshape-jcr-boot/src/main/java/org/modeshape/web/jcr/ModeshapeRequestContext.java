package org.modeshape.web.jcr;

import javax.jcr.Session;

public class ModeshapeRequestContext {
	  /**
     * The holder of the active session. Since we're dealing with a service which operates on a request-response basis and
     * after each request the active session is closed, we don't need multiple active sessions (e.g. for each WS and repository)
     */
    private static final ThreadLocal<Session> ACTIVE_SESSION = new ThreadLocal<Session>();
    
    public static void set(Session session) {
    	ACTIVE_SESSION.set(session);
    }
    
    public static void cleanup() {
    	Session session = ACTIVE_SESSION.get();
    	if (session != null) {
    		session.logout();
    		ACTIVE_SESSION.remove();
    	}
    }
    
    public static Session get() {
    	return ACTIVE_SESSION.get();
    }
}
