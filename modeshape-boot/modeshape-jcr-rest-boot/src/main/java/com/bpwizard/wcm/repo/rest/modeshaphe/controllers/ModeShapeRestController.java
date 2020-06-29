package com.bpwizard.wcm.repo.rest.modeshaphe.controllers;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.common.util.StringUtil;
import org.modeshape.jcr.api.BackupOptions;
import org.modeshape.jcr.api.RestoreOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestBinaryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestQueryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.modeshape.model.BackupResponse;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestException;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestItem;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNodeType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestQueryPlanResult;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestQueryResult;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(ModeShapeRestController.BASE_URI)
public class ModeShapeRestController {
	private static final Logger logger = LogManager.getLogger(ModeShapeRestController.class);
	public static final String BASE_URI = "/modeshape/api";
	
	@Autowired
	private RestRepositoryHandler repositoryHandler;
	
	@Autowired
	private RestItemHandler itemHandler;
    
	@Autowired
	private RestNodeHandler nodeHandler;
    
	@Autowired
	private RestQueryHandler queryHandler;
    
	@Autowired
	private RestBinaryHandler binaryHandler;
    
	@Autowired
	private RestNodeTypeHandler nodeTypeHandler;
    
    /**
     * Returns the list of JCR repositories available on this server
     *
     * @param request the servlet request; may not be null
     * @return the list of JCR repositories available on this server, as a {@link RestRepositories} instance.
     */
    @GetMapping(path="/repo", produces= MediaType.APPLICATION_JSON_VALUE)
    public RestRepositories getRepositories(HttpServletRequest request ) {
    	logger.debug("Entering ...");
    	RestRepositories repositories = this.getRepositories(request);
        logger.debug("Exiting ...");
        return repositories;
    }
    
    /**
     * Returns the list of workspaces available to this user within the named repository.
     *
     * @param rawRepositoryName the name of the repository; may not be null
     * @param request the servlet request; may not be null
     * @return the list of workspaces available to this user within the named repository, as a {@link RestWorkspaces} instance.
     * @throws RepositoryException if there is any other error accessing the list of available workspaces for the repository
     */
    @GetMapping(path="/{repositoryName}", produces= MediaType.APPLICATION_JSON_VALUE)
    		//produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestWorkspaces getWorkspaces(
    		HttpServletRequest request,
    		@PathVariable("repositoryName")String rawRepositoryName) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
            String repositoryUrl = RestHelper.urlFrom(request);
	    	RestWorkspaces workspacess = this.repositoryHandler.getWorkspaces(repositoryUrl, rawRepositoryName);
	        logger.debug("Exiting ...");
	        return workspacess;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_GET_WS, null));
    	}
    }
    
    /**
     * Performs a repository backup on the server, allowing a list of custom parameters which can control the backup process.
     * The root location of the backup file on the server is the first accessible folder in the following order:
     *
     * <ul>
     * <li>the value of the servlet context 'backupLocation' parameter</li>
     * <li>the value of the system property 'jboss.domain.data.dir'</li>
     * <li>the value of the system property 'jboss.server.data.dir'</li>
     * <li>the value of the system property 'user.home'</li>
     * </ul>
     * 
     * If none of those locations is available and writable, the request will fail.
     *
     * @param servletContext the {@link ServletContext} instance
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName the name of the repository; may not be null
     * @param includeBinaries whether or not binary values should be part of the backup or not; defaults to {@code true}
     * @param documentsPerFile the number of nodes each backup file will contains; defaults to {@code 100k}
     * @param compress whether or not each documents file should be compressed or not; default to {@code true}
     * @param batchSize how many documents to backup in a single batch; default to {@code 10000}
     * @return a {@link ResponseEntity} instance which if successful will contain the name of the backup file and the location on the 
     * server where the backup was performed.
     * @throws RepositoryException if there is any unexpected error while performing the backup
     * 
     * @see org.modeshape.jcr.api.RepositoryManager#backupRepository(File, BackupOptions)
     */
    @PostMapping(path="/{repositoryName}/" + RestHelper.BACKUP_METHOD_NAME, produces= MediaType.APPLICATION_JSON_VALUE) 
    public ResponseEntity<BackupResponse> backup(HttpServletRequest request,
                            @PathVariable("repositoryName") String repositoryName,
                            @RequestParam(name="from", required = false) final String from,
                            @RequestParam(name="includeBinaries", defaultValue="true") final boolean includeBinaries,
                            @RequestParam(name="documentsPerFile", defaultValue="100000") final long documentsPerFile,
                            @RequestParam(name="compress", defaultValue="true") final boolean compress,
                            @RequestParam(name="batchSize", defaultValue="10000") final int batchSize) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	BackupResponse response = this.repositoryHandler.backupRepository(repositoryName, from, new BackupOptions() {
	            @Override
	            public boolean includeBinaries() {
	                return includeBinaries;
	            }
	
	            @Override
	            public long documentsPerFile() {
	                return documentsPerFile;
	            }
	
	            @Override
	            public boolean compress() {
	                return compress;
	            }
	
	            @Override
	            public int batchSize() {
	                return batchSize;
	            }
	        });
	    	logger.debug("Exiting ...");
	    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_BACKUP, null));
    	}
    }
    
    @PostMapping(path="/{repositoryName}/" + RestHelper.COPY_METHOD_NAME, produces= MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<?> copyWorkspace(HttpServletRequest request,
	                    @PathVariable("repositoryName") String repositoryName,
	                    @RequestParam(name="from", defaultValue=WcmConstants.DEFAULT_WS) final String from,
	                    @RequestParam(name="ito", defaultValue=WcmConstants.DRAFT_WS) final String to,
	                    @RequestParam(name="includeBinaries", defaultValue="true") final boolean includeBinaries,
	                    @RequestParam(name="documentsPerFile", defaultValue="100000") final long documentsPerFile,
	                    @RequestParam(name="compress", defaultValue="true") final boolean compress,
	                    @RequestParam(name="batchSize", defaultValue="10000") final int batchSize,
	                    @RequestParam(name="reindexTargetWorkspaceContent", defaultValue="true") final boolean reindexContent) throws WcmRepositoryException {
		logger.debug("Entering ...");
		try {
			BackupResponse backupResponse = this.repositoryHandler.backupRepository(repositoryName, from, new BackupOptions() {
		        @Override
		        public boolean includeBinaries() {
		            return includeBinaries;
		        }
		
		        @Override
		        public long documentsPerFile() {
		            return documentsPerFile;
		        }
		
		        @Override
		        public boolean compress() {
		            return compress;
		        }
		
		        @Override
		        public int batchSize() {
		            return batchSize;
		        }
		    });
			
			List<RestException> restoreResponse = this.repositoryHandler.restoreRepository(repositoryName, to, backupResponse.getName(), new RestoreOptions() {
	            @Override
	            public boolean reindexContentOnFinish() {
	                return reindexContent;
	            }
	
	            @Override
	            public boolean includeBinaries() {
	                return includeBinaries;
	            }
	
	            @Override
	            public int batchSize() {
	                return batchSize;
	            }
	        });
			logger.debug("Exiting ...", restoreResponse);
			return (restoreResponse!= null) ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restoreResponse) : 
				ResponseEntity.status(HttpStatus.CREATED).body(backupResponse);
		} catch (Throwable t) {
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_COPY_WS, null));
		}
	}
    /**
     * Performs a repository restore on the server based on the name of a backup provided as argument. 
     * The root location where the backup will be searched is in order:
     *
     * <ul>
     * <li>the value of the servlet context 'backupLocation' parameter</li>
     * <li>the value of the system property 'jboss.domain.data.dir'</li>
     * <li>the value of the system property 'jboss.server.data.dir'</li>
     * <li>the value of the system property 'user.home'</li>
     * </ul>
     * 
     * If a backup with the given name cannot be found at any of those location, the request will fail.
     *
     * @param servletContext the {@link ServletContext} instance
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName the name of the repository; may not be null
     * @param backupName a {@link String} representing the name of the backup folder as returned by the backup request
     * @param includeBinaries whether or not binary values should be part of the backup or not; defaults to {@code true}
     * @param reindexContent whether or not a full repository reindexing should be performed, once restore has completed; defaults to {@code true}
     * @param batchSize how many documents to restore in a single batch; defaults to {@code 1000}
     * @return a {@link ResponseEntity} instance, never {@code null}
     * @throws RepositoryException if there is any unexpected error while performing the restore
     * 
     * @see org.modeshape.jcr.api.RepositoryManager#restoreRepository(File, RestoreOptions)
     */
    @PostMapping(path="/{repositoryName}/" + RestHelper.RESTORE_METHOD_NAME, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restore(HttpServletRequest request,
                            @PathVariable("repositoryName") String repositoryName,
                            @RequestParam(name="to", required = false) final String to,
                            @RequestParam(name="name") final String backupName,
                            @RequestParam(name="includeBinaries", defaultValue="true") final boolean includeBinaries,
                            @RequestParam(name="reindexContent", defaultValue="true") final boolean reindexContent,
                            @RequestParam(name="batchSize", defaultValue="1000") final int batchSize) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		List<RestException> response = this.repositoryHandler.restoreRepository(repositoryName, to, backupName, new RestoreOptions() {
	            @Override
	            public boolean reindexContentOnFinish() {
	                return reindexContent;
	            }
	
	            @Override
	            public boolean includeBinaries() {
	                return includeBinaries;
	            }
	
	            @Override
	            public int batchSize() {
	                return batchSize;
	            }
	        });
	    	logger.debug("Exiting ...");
	    	return (response != null) ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response) :  ResponseEntity.ok().build();
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_RESTORE, null));
    	}
    }
    
    /**
     * Retrieves the binary content of the binary property at the given path, allowing 2 extra (optional) parameters: the
     * mime-type and the content-disposition of the binary value.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param path a non-null {@link String} representing the absolute path to a binary property.
     * @param mimeType an optional {@link String} representing the "already-known" mime-type of the binary. Can be {@code null}
     * @param contentDisposition an optional {@link String} representing the client-preferred content disposition of the respose.
     *        Can be {@code null}
     * @return the binary stream of the requested binary property or NOT_FOUND if either the property isn't found or it isn't a
     *         binary
     * @throws RepositoryException if any JCR related operation fails, including the case when the path to the property isn't
     *         valid.
     */
    @GetMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.BINARY_METHOD_NAME, 
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBinary( HttpServletRequest request,
    		                   @PathVariable( "repositoryName" ) String repositoryName,
    		                   @PathVariable( "workspaceName" ) String workspaceName,
    		                   @RequestParam( "path" ) String wcmPath,
    		                   @RequestParam( name="mimeType", required=false ) String mimeType,
    		                   @RequestParam( name="contentDisposition", required=false ) String contentDisposition ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	ResponseEntity<?> response = this.getBindaryContent(request, repositoryName, workspaceName, (wcmPath), mimeType, contentDisposition);
	        
	    	//ResponseEntity<String> response = ResponseEntity.ok().body(path);
	        logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_GET_BINARY, null));
    	}
    }
    
    /**
     * Retrieves the node type definition with the given name from the repository.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param nodeTypeName a non-null {@link String} representing the name of a node type.
     * @return the node type information.
     * @throws RepositoryException if any JCR related operation fails.
     */
    @GetMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.NODE_TYPES_METHOD_NAME + "/{nodeTypeName}", 
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public RestNodeType getNodeType( 
    		HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repositoryName,
    		@PathVariable( "workspaceName" ) String workspaceName,
    		@PathVariable( "nodeTypeName" ) String nodeTypeName ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestNodeType restNodeType = nodeTypeHandler.getNodeType(baseUrl, repositoryName, workspaceName, nodeTypeName);
	    	logger.debug("Exiting ...");
	    	return restNodeType;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_GET_NODE_TYPE, null));
    	}
    }
    
    /**
     * Imports a single CND(Node type definition) file into the repository. The CND file should be submitted as the body of the request.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param allowUpdate an optional parameter which indicates whether existing node types should be updated (overridden) or not.
     * @param requestBodyInputStream a {@code non-null} {@link InputStream} instance, representing the body of the request.
     * @return a list with the registered node types if the operation was successful, or an appropriate error code otherwise.
     * @throws RepositoryException if any JCR related operation fails.
     */
    @PostMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.NODE_TYPES_METHOD_NAME + "/conent", 
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postCND(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repositoryName,
    		@PathVariable( "workspaceName" ) String workspaceName,
    		@RequestParam( name="allowUpdate", defaultValue="true" ) boolean allowUpdate,
            @RequestBody Resource requestBody ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
    		ResponseEntity<?> response = this.nodeTypeHandler.importCND(baseUrl, repositoryName, workspaceName, allowUpdate, requestBody.getInputStream());
    		logger.debug("Exiting ...");
        	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
    	
    }
    
    /**
     * Imports a single CND file into the repository, using a {@link MediaType#_FORM_DATA} request. The CND file is
     * expected to be submitted from an HTML element with the name <i>file</i>
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param allowUpdate an optional parameter which indicates whether existing node types should be updated (overridden) or not.
     * @param form a {@link FileUploadForm} instance representing the HTML form from which the cnd was submitted
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws RepositoryException if any JCR operations fail
     * @throws IllegalArgumentException if the submitted form does not contain an HTML element named "file".
     */
    @PostMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.NODE_TYPES_METHOD_NAME + "/file", 
    	produces= MediaType.APPLICATION_JSON_VALUE, 
        consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postCNDViaForm(HttpServletRequest request,
    		@PathVariable("repositoryName" ) String repositoryName,
    		@PathVariable("workspaceName" ) String workspaceName,
    		@RequestParam(name="allowUpdate", defaultValue="true") boolean allowUpdate,
    		@RequestParam("file") MultipartFile file) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.nodeTypeHandler.importCND(baseUrl, repositoryName, workspaceName, allowUpdate, file.getInputStream());
	        logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_CND, null));
    	}
    }
    
    /**
     * Retrieves an item from a workspace
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @param depth the depth of the node graph that should be returned if {@code path} refers to a node. @{code 0} means return
     *        the requested node only. A negative value indicates that the full subgraph under the node should be returned. This
     *        parameter defaults to {@code 0} and is ignored if {@code path} refers to a property.
     * @return a {@code non-null} {@link RestItem}
     * @throws RepositoryException if any JCR error occurs
     * @see javax.jcr.Session#getItem(String)
     */
    @GetMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME,
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public RestItem getItem(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name="path", defaultValue="" ) String wcmPath,
    		@RequestParam( name="depth", defaultValue="0" ) int depth ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
    		
	    	RestItem item = this.itemHandler.item(baseUrl, repository, workspace, (wcmPath), depth);
	    	logger.debug("Exiting ...");
	    	return item;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_GET_ITEM, null));
    	}
    }
    
    /**
     * Adds the content of the request as a node (or subtree of nodes) at the location specified by {@code path}.
     * <p>
     * The primary type and mixin type(s) may optionally be specified through the {@code jcr:primaryType} and
     * {@code jcr:mixinTypes} properties as request attributes.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @param requestContent the JSON-encoded representation of the node or nodes to be added
     * @return a {@code non-null} {@link ResponseEntity} instance which either contains the node or an error code.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     */
    @PostMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME,
	        consumes= MediaType.APPLICATION_JSON_VALUE, 
	        produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestItem> postItem( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
            @PathVariable( "workspaceName" ) String workspace,
            @RequestParam( name = "path", required = true ) String wcmPath,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<RestItem> response = this.itemHandler.addItem(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), requestContent);
	    	logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEM, null));
    	}
    }

    /**
     * Performs a bulk creation of items via a single session, using the body of the request, which is expected to be a valid JSON
     * object. The format of the JSON request must be an object of the form:
     * <ul>
     * <li>{ "node1_path" : { node1_body }, "node2_path": { node2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "property2_path": { property2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "node1_path": { node1_body } ... }</li>
     * </ul>
     * where each body (either of a property or of a node) is expected to be a JSON object which has the same format as the one
     * used when creating a single item.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the node or nodes to be added
     * @return a {@code non-null} {@link ResponseEntity} instance which either contains the item or an error code.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     * @see ModeShapeRestService#postItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME + "/content",
	    consumes= MediaType.APPLICATION_JSON_VALUE, 
	    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postItems( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.itemHandler.addItems(baseUrl, rawRepositoryName, rawWorkspaceName, requestContent);
	    	logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	}
    }
    
    /**
     * Performs a bulk creation of items via a single session, using the body of the request, which is expected to be a valid JSON
     * object. The format of the JSON request must be an object of the form:
     * <ul>
     * <li>{ "node1_path" : { node1_body }, "node2_path": { node2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "property2_path": { property2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "node1_path": { node1_body } ... }</li>
     * </ul>
     * where each body (either of a property or of a node) is expected to be a JSON object which has the same format as the one
     * used when creating a single item.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the node or nodes to be added
     * @return a {@code non-null} {@link ResponseEntity} instance which either contains the item or an error code.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     * @see ModeShapeRestService#postItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME + "/file",
	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
	    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postItemsViaForm( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam("file") MultipartFile file ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.itemHandler.addItems(baseUrl, rawRepositoryName, rawWorkspaceName, file.getInputStream());
	    	logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
    	} 
    }
    
    /**
     * Deletes the item at {@code path}.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @return a {@code non-null} {@link ResponseEntity} instance.
     * @throws RepositoryException if any other error occurs
     */
    @DeleteMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME)
    public ResponseEntity<?> deleteItem(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath
    		) throws WcmRepositoryException {
        
    	logger.debug("Entering ...");
    	try {
	    	this.itemHandler.deleteItem(repository, workspace, WcmUtils.nodePath(wcmPath));
	        logger.debug("Exiting ...");
	        return ResponseEntity.noContent().build();
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_DELETE_ITEM, null));
    	}
    }

    /**
     * Performs a bulk deletion of nodes via a single session, using the body of the request, which is expected to be a valid JSON
     * array. The format of the JSON request must an array of the form:
     * <ul>
     * <li>["node1_path", "node2_path",...]</li>
     * <li>["property1_path", "property2_path",...]</li>
     * <li>["property1_path", "node1_path",...]</li>
     * </ul>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the node or nodes to be added
     * @return a {@code non-null} {@link ResponseEntity} instance.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     * @see ModeShapeRestService#deleteItem(javax.servlet.http.HttpServletRequest, String, String, String)
     */
    @DeleteMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME + "/content", 
    		consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItems( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	ResponseEntity<Void> response = this.itemHandler.deleteItems(rawRepositoryName, rawWorkspaceName, requestContent);
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_DELETE_ITEMS, null));
    	}
    }
    
    /**
     * Updates the node or property at the path.
     * <p>
     * If path points to a property, this method expects the request content to be either a JSON array or a JSON string. The array
     * or string will become the values or value of the property. If path points to a node, this method expects the request
     * content to be a JSON object. The keys of the objects correspond to property names that will be set and the values for the
     * keys correspond to the values that will be set on the properties.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @param requestContent the JSON-encoded representation of the values and, possibly, properties to be set
     * @return a {@link RestItem} instance representing the modified item.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     */
    @PutMapping(path="/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME,
	    consumes= MediaType.APPLICATION_JSON_VALUE,
	    produces= MediaType.APPLICATION_JSON_VALUE)
    public RestItem putItem( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestItem item = itemHandler.updateItem(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), requestContent);
	        logger.debug("Exiting ...");
	        return item;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_PUT_ITEM, null));
    	}
    }

    /**
     * Performs a bulk update of items via a single session, using the body of the request, which is expected to be a valid JSON
     * object. The format of the JSON request must be an object of the form:
     * <ul>
     * <li>{ "node1_path" : { node1_body }, "node2_path": { node2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "property2_path": { property2_body } ... }</li>
     * <li>{ "property1_path" : { property1_body }, "node1_path": { node1_body } ... }</li>
     * </ul>
     * where each body (either of a property or of a node) is expected to be a JSON object which has the same format as the one
     * used when updating a single item.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the values and, possibly, properties to be set
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     * @see ModeShapeRestService#putItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    @PutMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.ITEMS_METHOD_NAME + "/content",
    		consumes= MediaType.APPLICATION_JSON_VALUE,
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putItems(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
            @PathVariable( "workspaceName" ) String rawWorkspaceName,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.itemHandler.updateItems(baseUrl, rawRepositoryName, rawWorkspaceName, requestContent);
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_PUT_ITEMS, null));
    	}
    }
    
    /**
     * Creates or updates a binary property in the repository, at the given path. The binary content is expected to be written
     * directly to the request body.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param path a non-null {@link String} representing the absolute path to a binary property.
     * @param requestBodyInputStream a non-null {@link InputStream} stream which represents the body of the request, where the
     *        binary content is expected.
     * @return a representation of the binary property that was created/updated.
     * @throws RepositoryException if any JCR related operation fails.
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.BINARY_METHOD_NAME,
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postBinary(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath,
            @RequestBody Resource requestBodyInputStream ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.binaryHandler.updateBinary(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), requestBodyInputStream.getInputStream(), true);
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_BINARY, null));
    	}
    }
    
    /**
     * Updates a binary property in the repository, at the given path. If the binary property does not exist, the NOT_FOUND http
     * response code is returned. The binary content is expected to be written directly to the request body.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param path a non-null {@link String} representing the absolute path to an existing binary property.
     * @param requestBodyInputStream a non-null {@link InputStream} stream which represents the body of the request, where the
     *        binary content is expected.
     * @return a representation of the binary property that was updated.
     * @throws RepositoryException if any JCR related operation fails.
     */
    @PutMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.BINARY_METHOD_NAME,
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putBinary(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath,
            @RequestBody Resource requestBodyInputStream ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.binaryHandler.updateBinary(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), requestBodyInputStream.getInputStream(), false);
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_PUT_BINARY, null));
    	}
    }

    /**
     * Creates/updates a binary file into the repository, using a {@link MediaType#MULTIPART_FORM_DATA} request, at {@code path}.
     * The binary file is expected to be submitted from an HTML element with the name <i>file</i>
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param path the path to the binary property
     * @param form a {@link FileUploadForm} instance representing the HTML form from which the binary was submitted
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws RepositoryException if any JCR related operation fails.
     * @see ModeShapeRestService#postBinary(javax.servlet.http.HttpServletRequest, String, String, String, java.io.InputStream)
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.BINARY_METHOD_NAME + "/file",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postBinaryViaForm(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath,
    		@RequestParam("file") MultipartFile file ) throws WcmRepositoryException {
        // form.validate();
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	ResponseEntity<?> response = this.binaryHandler.updateBinary(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), file.getInputStream(), true);
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_BINARY, null));
    	}
    }
    
    /**
     * Retrieves the binary content of an existing [nt:file] node at the given path, allowing 2 extra (optional) parameters: the
     * mime-type and the content-disposition of the binary value.
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param path a non-null {@link String} representing the absolute path to a content file.
     * @param mimeType an optional {@link String} representing the "already-known" mime-type of the binary. Can be {@code null}
     * @param contentDisposition an optional {@link String} representing the client-preferred content disposition of the respose.
     *        Can be {@code null}
     * @return the binary stream of the requested binary property or NOT_FOUND if either the property isn't found or it isn't a
     *         binary
     * @throws RepositoryException if any JCR related operation fails, including the case when the path to the property isn't
     *         valid.
     */
    @GetMapping(
    		path="/{repositoryName}/{workspaceName}/" + RestHelper.DOWNLOAD_METHOD_NAME, 
    		produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> downloadBinaryFile( HttpServletRequest request,
    		                   @PathVariable( "repositoryName" ) String repository,
    		                   @PathVariable( "workspaceName" ) String workspace,
    		                   @RequestParam( name = "path", required = false ) String wcmPath,
    		                   @RequestParam( name="mimeType", required=false ) String mimeType,
    		                   @RequestParam( name="contentDisposition", required=false ) String contentDisposition ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	ResponseEntity<?> response = this.getBindaryContent(request, repository, workspace, WcmUtils.nodePath(wcmPath) + "/jcr:content/jcr:data", mimeType, contentDisposition);        
	        logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_DOWNLOAD_BINARY, null));
    	}
    }
    
    /**
     * Creates/updates a binary file into the repository, using a {@link MediaType#MULTIPART_FORM_DATA} request, at {@code path}.
     * The binary content is expected to be submitted from an HTML element with the name <i>file</i>.
     * <p>
     * Depending on the whether any node exists or not at {@code path}, this method behaves in different ways:
     * <ul>
     * <li>If {@code path} exists on the server, it is expected to point to an existing [nt:file] node, for which the
     * [jcr:content]/[jcr:data] property will be updated/set</li>
     * <li>If {@code path} doesn't exist or only a <b>subpath</b> exists on the server, then for each missing segment but the last
     * an [nt:folder] node will be created. The last segment of the path will always represent the name of the [nt:file] node
     * which will be created together with its content: [jcr:content]/[jcr:data].</li>
     * </ul>
     * For example: issuing a POST request via this method to a path at which no node exists - {@code node1/node2/node3} - will
     * trigger the creation of the corresponding nodes with the types -
     * {@code [nt:folder]/[nt:folder]/[nt:file]/[jcr:content]/[jcr:data]}
     * </p>
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param filePath the path to the binary property
     * @param form a {@link FileUploadForm} instance representing the HTML form from which the binary was submitted
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws RepositoryException if any JCR related operation fails.
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.UPLOAD_METHOD_NAME,
	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
	    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadBinaryViaForm(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		 @RequestParam( name = "path", required = false ) String wcmPath,
    		@RequestParam("file") MultipartFile file) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	// form.validate();
    		String baseUrl = RestHelper.repositoryUrl(request);

	    	ResponseEntity<?> response = this.binaryHandler.uploadBinary(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), file.getInputStream());
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_UPLOAD_BINARY, null));
    	}
    }
    
    /**
     * Creates/updates a binary file into the repository, <b>using the body of the request as the contents of the binary file</b>,
     * at {@code path}.
     * <p>
     * Depending on the whether any node exists or not at {@code path}, this method behaves in different ways:
     * <ul>
     * <li>If {@code path} exists on the server, it is expected to point to an existing [nt:file] node, for which the
     * [jcr:content]/[jcr:data] property will be updated/set</li>
     * <li>If {@code path} doesn't exist or only a <b>subpath</b> exists on the server, then for each missing segment but the last
     * an [nt:folder] node will be created. The last segment of the path will always represent the name of the [nt:file] node
     * which will be created together with its content: [jcr:content]/[jcr:data].</li>
     * </ul>
     * For example: issuing a POST request via this method to a path at which no node exists - {@code node1/node2/node3} - will
     * trigger the creation of the corresponding nodes with the types -
     * {@code [nt:folder]/[nt:folder]/[nt:file]/[jcr:content]/[jcr:data]}
     * </p>
     *
     * @param request a non-null {@link HttpServletRequest} request
     * @param repositoryName a non-null {@link String} representing the name of a repository.
     * @param workspaceName a non-null {@link String} representing the name of a workspace.
     * @param filePath the path to the binary property
     * @param requestBodyInputStream a non-null {@link InputStream} stream which represents the body of the request, where the
     *        binary content is expected.
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws RepositoryException if any JCR related operation fails.
     */
    @PostMapping(path= "/{repositoryName}/{workspaceName}/" + RestHelper.UPLOAD_METHOD_NAME + "/conent",
    	    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadBinary(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String repository,
    		@PathVariable( "workspaceName" ) String workspace,
    		@RequestParam( name = "path", required = false ) String wcmPath,
    		@RequestBody Resource requestBodyInputStream ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);

	    	ResponseEntity<?> response = this.binaryHandler.uploadBinary(baseUrl, repository, workspace, WcmUtils.nodePath(wcmPath), requestBodyInputStream.getInputStream());
	    	logger.debug("Exiting ...");
	        return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_UPLOAD_BINARY, null));
    	}
    }
    
    /**
     * Executes the XPath query contained in the body of the request against the give repository and workspace.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} {@link RestQueryResult} instance.
     * @throws RepositoryException if any JCR error occurs
     */
    @SuppressWarnings( "deprecation" )
    @PostMapping(path= "{repositoryName}/{workspaceName}/query/xpath",
	    consumes = "application/jcr+xpath",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryResult postXPathQuery(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);    		
	    	RestQueryResult result = this.queryHandler.executeQuery(request.getParameterMap().entrySet(), baseUrl, rawRepositoryName, rawWorkspaceName, Query.XPATH, requestContent, offset,
	                                         limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_XPATH_QUERY, null));
    	}
    }
    
    /**
     * Executes the JCR-SQL query contained in the body of the request against the give repository and workspace.
     * <p>
     * The query results will be JSON-encoded in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} {@link RestQueryResult} instance.
     * @throws RepositoryException if any JCR error occurs
     */
    @SuppressWarnings( "deprecation" )
    @PostMapping(path= "{repositoryName}/{workspaceName}/query/sql",
	    consumes = "application/jcr+sql",
	    produces= MediaType.APPLICATION_JSON_VALUE)
    public RestQueryResult postJcrSqlQuery(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);  
	    	RestQueryResult result = this.queryHandler.executeQuery(request.getParameterMap().entrySet(), baseUrl, rawRepositoryName, rawWorkspaceName, Query.SQL, requestContent, offset, limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JSQL_QUERY, null));
    	}
    }
    
    /**
     * Executes the JCR-SQL2 query contained in the body of the request against the give repository and workspace.
     * <p>
     * The query results will be JSON-encoded in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} {@link RestQueryResult} instance.
     * @throws RepositoryException if any JCR error occurs
     */
    @PostMapping(path= "{repositoryName}/{workspaceName}/query/sql2",
	    consumes = "application/jcr+sql2",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryResult postJcrSql2Query(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestQueryResult result = this.queryHandler.executeQuery(request.getParameterMap().entrySet(), baseUrl, rawRepositoryName, rawWorkspaceName, Query.JCR_SQL2, requestContent, offset,
	                                         limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JSQL2_QUERY, null));
    	}
    }

    /**
     * Executes the JCR-SQL query contained in the body of the request against the give repository and workspace.
     * <p>
     * The query results will be JSON-encoded in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} {@link RestQueryResult} instance.
     * @throws RepositoryException if any JCR error occurs
     */
    @PostMapping(path= "{repositoryName}/{workspaceName}/query/search",
	    consumes = "application/jcr+search",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryResult postJcrSearchQuery(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestQueryResult result = this.queryHandler.executeQuery(request.getParameterMap().entrySet(), baseUrl, rawRepositoryName, rawWorkspaceName,
	                                         org.modeshape.jcr.api.query.Query.FULL_TEXT_SEARCH, requestContent, offset, limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JCR_SEARCH, null));
    	}
    }
    
    /**
     * Executes the XPath query contained in the body of the request against the give repository and workspace.
     * <p>
     * The string representation of the query plan will be returned in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} response containing a string representation of the query plan
     * @throws RepositoryException if any JCR error occurs
     */
    @SuppressWarnings( "deprecation" )
    @PostMapping(path= "{repositoryName}/{workspaceName}/queryPlan/xpath",
	    consumes = "application/jcr+xpath",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryPlanResult postXPathQueryPlan(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	RestQueryPlanResult result = this.queryHandler.planQuery(request.getParameterMap().entrySet(), rawRepositoryName, rawWorkspaceName, Query.XPATH, requestContent, offset, limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_XPATH_QUERY_PLAN, null));
    	}
    }
    
    /**
     * Executes the JCR-SQL query contained in the body of the request against the give repository and workspace.
     * <p>
     * The string representation of the query plan will be returned in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} response containing a string representation of the query plan
     * @throws RepositoryException if any JCR error occurs
     */
    @SuppressWarnings( "deprecation" )
    @PostMapping(path= "{repositoryName}/{workspaceName}/queryPlan/sql",
	    consumes = "application/jcr+sql",
	    produces= MediaType.APPLICATION_JSON_VALUE)
	    // produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryPlanResult postJcrSqlQueryPlan( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	RestQueryPlanResult result = this.queryHandler.planQuery(request.getParameterMap().entrySet(), rawRepositoryName, rawWorkspaceName, Query.SQL, requestContent, offset, limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JSQL_QUERY_PLAN, null));
    	}
    }

    /**
     * Executes the JCR-SQL2 query contained in the body of the request against the give repository and workspace.
     * <p>
     * The string representation of the query plan will be returned in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} response containing a string representation of the query plan
     * @throws RepositoryException if any JCR error occurs
     */
    @PostMapping(path= "{repositoryName}/{workspaceName}/queryPlan/sql2",
	    consumes = "application/jcr+sql2",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryPlanResult postJcrSql2QueryPlan(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	RestQueryPlanResult result = this.queryHandler.planQuery(request.getParameterMap().entrySet(), rawRepositoryName, rawWorkspaceName, Query.JCR_SQL2, requestContent, offset,
	                                      limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JSQL2_QUERY_PLAN, null));
    	}
    }

    /**
     * Compute the plan for the JCR-SQL query contained in the body of the request against the give repository and workspace.
     * <p>
     * The string representation of the query plan will be returned in the response body.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param offset the offset to the first row to be returned. If this value is greater than the size of the result set, no
     *        records will be returned. If this value is less than 0, results will be returned starting from the first record in
     *        the result set.
     * @param limit the maximum number of rows to be returned. If this value is greater than the size of the result set, the
     *        entire result set will be returned. If this value is less than zero, the entire result set will be returned. The
     *        results are counted from the record specified in the offset parameter.
     * @param uriInfo the information about the URI (from which the other query parameters will be obtained)
     * @param requestContent the query expression
     * @return a {@code non-null} response containing a string representation of the query plan
     * @throws RepositoryException if any JCR error occurs
     */
    @PostMapping(path= "{repositoryName}/{workspaceName}/queryPlan/search",
	    consumes = "application/jcr+search",
	    produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestQueryPlanResult postJcrSearchQueryPlan(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@RequestParam( name="offset", defaultValue="-1" ) long offset,
    		@RequestParam( name="limit", defaultValue="-1" ) long limit,
    		@RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	    	RestQueryPlanResult result = this.queryHandler.planQuery(request.getParameterMap().entrySet(), rawRepositoryName, rawWorkspaceName,
	                                      org.modeshape.jcr.api.query.Query.FULL_TEXT_SEARCH, requestContent, offset, limit);
	    	logger.debug("Exiting ...");
	        return result;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_JCR_SEARCH_PLAN, null));
    	}
    }
    
    /**
     * Retrieves from a workspace the node with the specified identifier.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param id the node identifier of the existing item
     * @param depth the depth of the node graph that should be returned. @{code 0} means return the requested node only. A
     *        negative value indicates that the full subgraph under the node should be returned. This parameter defaults to
     *        {@code 0}.
     * @return a {@code non-null} {@link RestItem}
     * @throws RepositoryException if any JCR error occurs
     * @see javax.jcr.Session#getNodeByIdentifier(String)
     */
    @GetMapping(path= "{repositoryName}/{workspaceName}/" + RestHelper.NODES_METHOD_NAME + "/{id}",
    		produces= MediaType.APPLICATION_JSON_VALUE)
			// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public RestItem getNodeWithId( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@PathVariable( "id" ) String id,
    		@RequestParam( name="depth", defaultValue="0" ) int depth ) 
    				throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestItem item = this.nodeHandler.nodeWithId(baseUrl, rawRepositoryName, rawWorkspaceName, id, depth);
	    	logger.debug("Exiting ...");
	    	return item;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_GET_NODE_ID, null));
    	}
    }
    
    /**
     * Updates the node with the given identifier
     * <p>
     * This method expects the request content to be a JSON object. The keys of the objects correspond to property names that will
     * be set and the values for the keys correspond to the values that will be set on the properties.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param id the node identifier of the existing item
     * @param requestContent the JSON-encoded representation of the values and, possibly, properties to be set
     * @return a {@link RestItem} instance representing the modified item.
     * @throws JSONException if there is an error reading the request body as a valid JSON object.
     * @throws RepositoryException if any other error occurs
     */
    @PutMapping(path= "{repositoryName}/{workspaceName}/" + RestHelper.NODES_METHOD_NAME + "/{id}",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces= MediaType.APPLICATION_JSON_VALUE)
		// produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE})    
    public RestItem putNodeWithId( HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@PathVariable( "id" ) String id,
            @RequestBody String requestContent ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
    		String baseUrl = RestHelper.repositoryUrl(request);
	    	RestItem item = this.nodeHandler.updateNodeWithId(baseUrl, rawRepositoryName, rawWorkspaceName, id, requestContent);
	    	logger.debug("Exiting ...");
	    	return item;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_PUT_NODE_ID, null));
    	}
    }
    
    /**
     * Deletes the subgraph at the node with the given identifier.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param id the node identifier of the existing item
     * @return a {@code non-null} {@link ResponseEntity} instance.
     * @throws RepositoryException if any other error occurs
     */
    @DeleteMapping(path= "{repositoryName}/{workspaceName}/" + RestHelper.NODES_METHOD_NAME + "/{id}")
    public ResponseEntity<Void> deleteNodeWithId(HttpServletRequest request,
    		@PathVariable( "repositoryName" ) String rawRepositoryName,
    		@PathVariable( "workspaceName" ) String rawWorkspaceName,
    		@PathVariable( "id" ) String id ) throws WcmRepositoryException {
    	logger.debug("Entering ...");
    	try {
	        nodeHandler.deleteNodeWithId(rawRepositoryName, rawWorkspaceName, id);
	        ResponseEntity<Void> response = ResponseEntity.noContent().build();
	        logger.debug("Exiting ...");
	    	return response;
    	} catch (Throwable t) {
    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_DELETE_NODE_ID, null));
    	}
    }
    
    private ResponseEntity<?> getBindaryContent(HttpServletRequest request,
            String repositoryName,
            String workspaceName,
            String path,
            String mimeType,
            String contentDisposition) throws RepositoryException {
    	
    	Property binaryProperty = this.binaryHandler.getBinaryProperty(repositoryName, workspaceName, path);
    	if (binaryProperty.getType() != PropertyType.BINARY) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(new RestException("The property " + binaryProperty.getPath() + " is not a binary"));
        }
        Binary binary = binaryProperty.getBinary();
        if (StringUtil.isBlank(mimeType)) {
            mimeType = binaryHandler.getDefaultMimeType(binaryProperty);
        }
        
        if (StringUtil.isBlank(mimeType)) {
        	mimeType = MediaType.IMAGE_PNG_VALUE;
        }
        if (StringUtil.isBlank(contentDisposition)) {
            contentDisposition = binaryHandler.getDefaultContentDisposition(binaryProperty);
        }
        
        return ResponseEntity.ok().header("Content-Disposition", contentDisposition).contentType(MediaType.valueOf(mimeType)).body(new InputStreamResource(binary.getStream()));
    }
    
}
