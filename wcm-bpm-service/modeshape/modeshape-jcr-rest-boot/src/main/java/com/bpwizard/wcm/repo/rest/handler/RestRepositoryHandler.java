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

package com.bpwizard.wcm.repo.rest.handler;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.modeshape.common.util.StringUtil;
import org.modeshape.jcr.api.BackupOptions;
import org.modeshape.jcr.api.Problem;
import org.modeshape.jcr.api.Problems;
import org.modeshape.jcr.api.Repository;
import org.modeshape.jcr.api.RepositoryManager;
import org.modeshape.jcr.api.RestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.modeshape.model.BackupResponse;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestException;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces;

/**
 * An handler which returns POJO-based rest model instances.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Component
public final class RestRepositoryHandler extends AbstractHandler {

	
    private static final String SPRING_BACKUP_LOCATION_PROPERTY = "org.modeshape.backupLocation";
    private static final String JBOSS_DOMAIN_DATA_DIR = "jboss.domain.data.dir";
    private static final String JBOSS_SERVER_DATA_DIR = "jboss.server.data.dir";
    private static final String USER_HOME = "user.home";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyyy_HHmmss");

    @Value("${org.modeshape.backupLocation}")
    private String springBackupRoot;
    /**
     * Returns the list of workspaces available to this user within the named repository.
     *
     * @param request the servlet request; may not be null
     * @param repositoryName the name of the repository; may not be null
     * @return the list of workspaces available to this user within the named repository, as a {@link RestWorkspaces} object
     *
     * @throws RepositoryException if there is any other error accessing the list of available workspaces for the repository
     */
    public RestWorkspaces getWorkspaces( HttpServletRequest request,
                                         String repositoryName ) throws RepositoryException {
        assert request != null;
        assert repositoryName != null;

        RestWorkspaces workspaces = new RestWorkspaces();
        Session session = getSession(repositoryName, null);
        for (String workspaceName : session.getWorkspace().getAccessibleWorkspaceNames()) {
            String repositoryUrl = RestHelper.urlFrom(request);
            workspaces.addWorkspace(workspaceName, repositoryUrl);
        }
        return workspaces;
    }

    /**
     * Performs a repository backup.
     */
    public ResponseEntity<BackupResponse> backupRepository(HttpServletRequest request, 
                                      String repositoryName,
                                      BackupOptions options ) throws RepositoryException {
        final File backupLocation = resolveBackupLocation();

        Session session = getSession(repositoryName, null);
        String repositoryVersion = session.getRepository().getDescriptorValue(Repository.REP_VERSION_DESC).getString().replaceAll("\\.","");
        final String backupName = "modeshape_" + repositoryVersion + "_" + repositoryName + "_backup_" + DATE_FORMAT.format(new Date());
        final File backup = new File(backupLocation, backupName);
        if (!backup.mkdirs()) {
            throw new RuntimeException("Cannot create backup folder: " + backup);
        }
        logger.debug("Backing up repository '{0}' to '{1}', using '{2}'", repositoryName, backup, options);


        RepositoryManager repositoryManager = ((org.modeshape.jcr.api.Workspace)session.getWorkspace()).getRepositoryManager();
        repositoryManager.backupRepository(backup, options);
        final String backupURL;
        try {
            backupURL = backup.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            //should never happen
            throw new RuntimeException(e);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new BackupResponse(backupName, backupURL));
    }

    /**
     * Restores a repository using an existing backup.
     */
    public ResponseEntity<?> restoreRepository(HttpServletRequest request, 
                                       String repositoryName,
                                       String backupName, 
                                       RestoreOptions options ) throws RepositoryException {
        if (StringUtil.isBlank(backupName)) {
            throw new IllegalArgumentException("The name of the backup cannot be null");
        }
        File backup = resolveBackup(backupName);
        logger.debug("Restoring repository '{0}' from backup '{1}' using '{2}'", repositoryName, backup, options);
        Session session = getSession(repositoryName, null);
        RepositoryManager repositoryManager = ((org.modeshape.jcr.api.Workspace)session.getWorkspace()).getRepositoryManager();
        final Problems problems = repositoryManager.restoreRepository(backup, options);
        if (!problems.hasProblems()) {
            return ResponseEntity.ok().build();
        }
        List<RestException> response = new ArrayList<>(problems.size());
        for (Problem problem : problems) {
            RestException exception = problem.getThrowable() != null ? 
                                      new RestException(problem.getMessage(), problem.getThrowable()) : 
                                      new RestException(problem.getMessage());
            response.add(exception);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    private File resolveBackup(String backupName) {
        // first look at the servlet init param
        // String backupRoot = context.getInitParameter(BACKUP_LOCATION_INIT_PARAM);
    	String backupRoot = this.springBackupRoot;
        File backupFolder = null;
        if (!StringUtil.isBlank(backupRoot)) {
            if (isValidDir(backupRoot + "/" + backupName)) {
                //return new File(backupRoot + "/" + backupName);
                backupFolder = new File(backupRoot + "/" + backupName);
            } else {

	            // try resolving it as a system property
	            backupRoot = System.getProperty(backupRoot);
	            if (isValidDir(backupRoot + "/" + backupName)) {
	            	//return new File(backupRoot + "/" + backupName);
	                backupFolder = new File(backupRoot + "/" + backupName);
	            }
            }
        }
        if (backupFolder == null) {
	        // jboss.domain.data.dir
	        backupRoot = System.getProperty(JBOSS_DOMAIN_DATA_DIR);
	        if (isValidDir(backupRoot + "/" + backupName)) {
	        	//return new File(backupRoot + "/" + backupName);
                backupFolder = new File(backupRoot + "/" + backupName);
	        }
        }

        if (backupFolder == null) {
	        // jboss.server.data.dir
	        backupRoot = System.getProperty(JBOSS_SERVER_DATA_DIR);
	        if (isValidDir(backupRoot + "/" + backupName)) {
	        	//return new File(backupRoot + "/" + backupName);
                backupFolder = new File(backupRoot + "/" + backupName);
	        }
        }
        if (backupFolder == null) {
	        // finally user.home
	        backupRoot = System.getProperty(USER_HOME);
	        if (isValidDir(backupRoot + "/" + backupName)) {
	        	//return new File(backupRoot + "/" + backupName);
                backupFolder = new File(backupRoot + "/" + backupName);
	        }
        }
        if (backupFolder != null) {
        	return backupFolder;
        }

        // none of the above are available, so fail
        throw new IllegalArgumentException(
                "Cannot locate backup '" + backupName + "' anywhere on the server in the following locations:" +
                SPRING_BACKUP_LOCATION_PROPERTY + " spring property, " + JBOSS_DOMAIN_DATA_DIR + ", " + JBOSS_SERVER_DATA_DIR + ", "
                + USER_HOME);

    }

    private File resolveBackupLocation() {
        // first look at the servlet init param 'backupLocation'
        //String backupLocation = context.getInitParameter(BACKUP_LOCATION_INIT_PARAM);
    	String backupLocation = this.springBackupRoot;
        File backupRoot = null;
        if (!StringUtil.isBlank(backupLocation)) {
            if (isValidDir(backupLocation)) {
                // return new File(backupLocation);
            	backupRoot = new File(backupLocation);
            } else {

	            // try resolving it as a system property
	            backupLocation = System.getProperty(backupLocation);
	            if (isValidDir(backupLocation)) {
	                // return new File(backupLocation);
	            	backupRoot = new File(backupLocation);
	            }
            }
        }

        if (backupRoot == null) {
	        // jboss.domain.data.dir
	        backupLocation = System.getProperty(JBOSS_DOMAIN_DATA_DIR);
	        if (isValidDir(backupLocation)) {
	        	// return new File(backupLocation);
            	backupRoot = new File(backupLocation);
	        }
        }

        // jboss.server.data.dir
        if (backupRoot == null) {
	        backupLocation = System.getProperty(JBOSS_SERVER_DATA_DIR);
	        if (isValidDir(backupLocation)) {
	        	// return new File(backupLocation);
            	backupRoot = new File(backupLocation);
	        }
        }
        // finally user.home
        if (backupRoot == null) {
	        backupLocation = System.getProperty(USER_HOME);
	        if (isValidDir(backupLocation)) {
	        	// return new File(backupLocation);
            	backupRoot = new File(backupLocation);
	        }
        }
        if (backupRoot != null) {
        	return backupRoot;
        }
        // none of the above are available, so fail
        throw new IllegalArgumentException(
                "None of the following locations are writable folders on the server: " +
                SPRING_BACKUP_LOCATION_PROPERTY + " spring property, " + JBOSS_DOMAIN_DATA_DIR + ", " + JBOSS_SERVER_DATA_DIR + ", "
                + USER_HOME);
    }

    private boolean isValidDir( String dir ) {
        if (StringUtil.isBlank(dir)) {
            return false;
        }
        File dirFile = new File(dir);
        return dirFile.exists() && dirFile.canWrite() && dirFile.isDirectory();
    }
}
