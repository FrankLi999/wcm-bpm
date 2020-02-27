/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.modeshape.web.jcr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.common.annotation.ThreadSafe;
import org.modeshape.jcr.api.RepositoriesContainer;
import org.modeshape.jcr.api.ServletCredentials;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bpwizard.wcm.repo.jcr.security.SpringSecurityCredentials;

/**
 * Manager for accessing JCR Repository instances. This manager uses the idiomatic way to find JCR Repository (and ModeShape
 * Repositories) instances via the {@link ServiceLoader} and {@link org.modeshape.jcr.api.RepositoriesContainer} mechanism.
 */
@ThreadSafe
public class RepositoryManager {
	private static final Logger logger = LogManager.getLogger(RepositoryManager.class);
    //private static final Logger LOGGER = WebLogger.getLogger(RepositoryManager.class);
    private Map<String, Object> factoryParams = new HashMap<>();
    private RepositoriesContainer repositoriesContainer;

    public RepositoryManager(Map<String, Object> factoryParams) {
    	this.factoryParams = factoryParams;
    }
    /**
     * Initializes the repository factory. For more details, please see the {@link RepositoryManager class-level documentation}.
     * 
     * @param context the servlet context; may not be null
     * @see RepositoryManager
     */
    @PostConstruct
    public synchronized void initialize() throws NoSuchRepositoryException  {
    	logger.debug("Entering ...");
        this.loadRepositoriesContainer();
        String repositoryName = (String)factoryParams.get(RepositoriesContainer.REPOSITORY_NAME);
        if (repositoryName != null) {
        	this.getRepository(repositoryName);
        }
        logger.debug("Exiting ...");
    }

    @PreDestroy
    public void shutdown() {
        this.repositoriesContainer.shutdown();
    }
    
    /**
     * Get a JCR Session for the named workspace in the named repository, using the supplied HTTP servlet request for
     * authentication information.
     * 
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the name of the repository in which the session is created
     * @param workspaceName the name of the workspace to which the session should be connected
     * @return an active session with the given workspace in the named repository
     * @throws RepositoryException if the named repository does not exist or there was a problem obtaining the named repository
     */
    public Session getSession( String repositoryName, String workspaceName ) throws RepositoryException {
        // Go through all the RepositoryFactory instances and try to create one ...
        Repository repository = getRepository(repositoryName);
        // If there's no authenticated user, try an anonymous login
        Session session = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
        	session = repository.login(workspaceName);
        } else {
        	session = repository.login(new SpringSecurityCredentials(authentication), workspaceName);
        }
        
        ModeshapeRequestContext.set(workspaceName, session);
        return session;
    }
    
    /**
     * Get a JCR Session for the named workspace in the named repository, using the supplied HTTP servlet request for
     * authentication information.
     * 
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the name of the repository in which the session is created
     * @param workspaceName the name of the workspace to which the session should be connected
     * @return an active session with the given workspace in the named repository
     * @throws RepositoryException if the named repository does not exist or there was a problem obtaining the named repository
     */
    public Session getSession( HttpServletRequest request,
                                      String repositoryName,
                                      String workspaceName ) throws RepositoryException {
        // Go through all the RepositoryFactory instances and try to create one ...
        Repository repository = getRepository(repositoryName);
        // If there's no authenticated user, try an anonymous login
        Session session = null;
        if (request == null || request.getUserPrincipal() == null) {
        	session = repository.login(workspaceName);
        } else {
        	session = repository.login(new ServletCredentials(request), workspaceName);
        }
        ModeshapeRequestContext.set(workspaceName, session);
        return session;
    }

    /**
     * Returns the {@link Repository} instance with the given name.
     * @param repositoryName a {@code non-null} string
     * @return a {@link Repository} instance, never {@code null}
     * @throws NoSuchRepositoryException if no repository with the given name exists.
     */
    public Repository getRepository( String repositoryName ) throws NoSuchRepositoryException {
        Repository repository = null;
        try {
            repository = this.repositoriesContainer.getRepository(repositoryName, Collections.unmodifiableMap(this.factoryParams));
        } catch (RepositoryException e) {
            throw new NoSuchRepositoryException(WebJcrI18n.cannotInitializeRepository.text(repositoryName), e);
        }

        if (repository == null) {
            throw new NoSuchRepositoryException(WebJcrI18n.repositoryNotFound.text(repositoryName));
        }
        return repository;
    }

    public Session getSession( String repositoryName ) throws LoginException, RepositoryException, NoSuchRepositoryException {
    	
    	Session session = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
        	session = this.getRepository(repositoryName).login();
        } else {
        	session = this.getRepository(repositoryName).login(new SpringSecurityCredentials(authentication));
        }
    	ModeshapeRequestContext.set("default", session);
    	return session;
    }
    
    /**
     * Returns a set with all the names of the available repositories.
     * @return a set with the names, never {@code null}
     */
    public Set<String> getJcrRepositoryNames() {
        try {
            return this.repositoriesContainer.getRepositoryNames(Collections.unmodifiableMap(factoryParams));
        } catch (RepositoryException e) {
            logger.error(WebJcrI18n.cannotLoadRepositoryNames.text(), e);
            return Collections.emptySet();
        }
    }
    
    private void loadRepositoriesContainer() {
        Iterator<RepositoriesContainer> containersIterator = ServiceLoader.load(RepositoriesContainer.class).iterator();
        if (!containersIterator.hasNext()) {
            throw new IllegalStateException(
                    WebJcrI18n.repositoriesContainerNotFoundInClasspath.text(RepositoriesContainer.class.getName()));
        }
        //there shouldn't be more than 1 container
        this.repositoriesContainer = containersIterator.next();
    }
}
