/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bpwizard.wcm.repo.jcr.security;

import java.util.Map;

import javax.jcr.Credentials;

import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.modeshape.jcr.security.AuthorizationProvider;
import org.modeshape.jcr.security.SecurityContext;
import org.modeshape.jcr.value.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 * @author M.Sarhan
 */
public class SpringSecurityProvider implements AuthenticationProvider, AuthorizationProvider {

    final static Logger logger = LoggerFactory.getLogger(SpringSecurityProvider.class);

    @Override
    public ExecutionContext authenticate(Credentials credentials,
            String repositoryName,
            String workspaceName,
            ExecutionContext repositoryContext,
            Map<String, Object> sessionAttributes) {

    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> authentication:" + credentials.toString());
    	System.out.println(credentials.getClass());
    	System.out.println(credentials);
        if (credentials instanceof SpringSecurityCredentials) {
            SpringSecurityCredentials creds = (SpringSecurityCredentials) credentials;
            Authentication auth = creds.getAuth();
            if (auth != null) {
                logger.info("[{}] Successfully authenticated.", auth.getName());
                return repositoryContext.with(new SpringSecurityContext(auth));
            }
        }
        return null;
    } 

	@Override
	public boolean hasPermission(
			ExecutionContext context, 
			String repositoryName, 
			String repositorySourceName,
			String workspaceName, 
			Path absPath, 
			String... actions) {
		SecurityContext securityContext = context.getSecurityContext();
		if (securityContext == null) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>securityContext is null");
		} else {
			System.out.println(">>>>>>>>>>>>>>>>>> user name:" + securityContext.getUserName());
		}
		
		System.out.println(">>>>>>>>>>>>hasPermission>>>>>>>>>repositoryName:" + repositoryName);
		System.out.println(">>>>>>>>>>>hasPermission>>>>>>>>>>repositorySourceName:" + repositorySourceName);
		System.out.println(">>>>>>>>>>>hasPermission>>>>>>>>>>workspaceName:" + workspaceName);
		System.out.println(">>>>>>>>>>>>hasPermission>>>>>>>>>abs path:" + absPath.getString());
		System.out.println(">>>>>>>>>>>hasPermission>>>>>>>>>>actions:" + actions);
		return true;
	}
}
