package org.modeshape.web.jcr;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

public class ModeshapeRequestContext {
	  /**
     * The holder of the active session. Since we're dealing with a service which operates on a request-response basis and
     * after each request the active session is closed, we don't need multiple active sessions (e.g. for each WS and repository)
     */
    private static final ThreadLocal<Map<String, Session>> ACTIVE_SESSION = new ThreadLocal<>();
    
    public static void set(String workspace, Session session) {
    	Map<String, Session> sessions = ACTIVE_SESSION.get();
    	if (sessions == null) {
    		sessions = new HashMap<String, Session>();
    		ACTIVE_SESSION.set(sessions);
    	}
    	sessions.put(workspace, session);
  
    }
    
    public static void cleanup() {
    	Map<String, Session> sessions = ACTIVE_SESSION.get();
    	if (sessions != null) {
    		sessions.forEach((w, s) -> s.logout());
    		ACTIVE_SESSION.remove();
    	}
    }
    
    public static Session get(String workspace) {
    	Map<String, Session> sessions = ACTIVE_SESSION.get();
    	return (sessions == null) ? null : sessions.get(workspace);
    }
}
