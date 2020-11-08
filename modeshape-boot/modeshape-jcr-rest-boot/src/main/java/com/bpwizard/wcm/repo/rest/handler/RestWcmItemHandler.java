package com.bpwizard.wcm.repo.rest.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.modeshape.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.SyndicationUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestItem;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Item handler at WCM layer instead of at JCR layer.
 */
@Component
public final class RestWcmItemHandler extends ItemHandler {
	
	@Value("${bpw.modeshape.authoring.enabled:true}")
	protected boolean authoringEnabled = true;
	
	@Value("${bpw.modeshape.syndication.enabled:true}")
	protected boolean syndicationEnabled = true;
	
	@Autowired	
	protected SyndicationUtils syndicationUtils;

	@Autowired	
	protected WcmUtils wcmUtils;
    
	/**
     * Retrieves the JCR {@link Item} at the given path, returning its rest representation.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @param depth the depth of the node graph that should be returned if {@code path} refers to a node. @{code 0} means return
     *        the requested node only. A negative value indicates that the full subgraph under the node should be returned. This
     *        parameter defaults to {@code 0} and is ignored if {@code path} refers to a property.
     * @return a the rest representation of the item, as a {@link RestItem} instance.
     * @throws RepositoryException if any JCR operations fail.
     */
    public RestItem item( 
    		//HttpServletRequest request,
    		String baseUrl,
                          String repositoryName,
                          String workspaceName,
                          String path,
                          int depth ) throws RepositoryException {
        Session session = getSession(repositoryName, workspaceName);
        Item item = itemAtPath(path, session);
        return createRestItem(baseUrl, depth, session, item);
    }

    /**
     * Adds the content of the request as a node (or subtree of nodes) at the location specified by {@code path}.
     * <p>
     * The primary type and mixin type(s) may optionally be specified through the {@code jcr:primaryType} and
     * {@code jcr:mixinTypes} properties.
     * </p>
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param path the path to the item
     * @param requestBody the JSON-encoded representation of the node or nodes to be added
     * @return the JSON-encoded representation of the node or nodes that were added. This will differ from {@code requestBody} in
     *         that auto-created and protected properties (e.g., jcr:uuid) will be populated.
     * @throws org.codehaus.jettison.json.JSONException if the request body cannot be translated into json
     * @throws RepositoryException if any other error occurs while interacting with the repository
     */
    public ResponseEntity<RestItem> addItem( 
    		//HttpServletRequest request,
    		WcmEvent.WcmItemType itemType,
    		String baseUrl,
	         String repositoryName,
	         String workspaceName,
	         String path,
	         String requestBody ) throws IOException, RepositoryException {
    	
       JsonNode requestBodyJSON = stringToJsonNode(requestBody);
       return this.addItem(itemType, baseUrl, repositoryName, workspaceName, path, requestBodyJSON);
    }

    public ResponseEntity<RestItem> addItem( 
    		WcmEvent.WcmItemType itemType,
    		String baseUrl,
            String repositoryName,
            String workspaceName,
            String path,
            JsonNode requestBodyJSON ) throws IOException, RepositoryException {
    	String parentAbsPath = parentPath(path);
        String newNodeName = newNodeName(path);

        Session session = getSession(repositoryName, workspaceName);
        Node parentNode = (Node)session.getItem(parentAbsPath);
        Node newNode = addNode(parentNode, newNodeName, requestBodyJSON);
        if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(workspaceName)) {
        	int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
			RestNode restNewNode = (RestNode)createRestItem(baseUrl, depth, session, newNode);
			syndicationUtils.addNewItemEvent(
					(RestNode) restNewNode, 
					repositoryName, 
					WcmConstants.DEFAULT_WS, 
					path,
					itemType);
		} 
        session.save();
        if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(workspaceName)) {
			Session drafrSession = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
			drafrSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
		}
        
        RestItem restNewNode = createRestItem(baseUrl, 0, session, newNode);
		
        return ResponseEntity.status(HttpStatus.CREATED).body(restNewNode);
    }
    
    @Override
    protected JsonNode getProperties( JsonNode jsonNode ) throws IOException {
    	ObjectNode properties = this.mapper.createObjectNode();
        for (Iterator<?> keysIterator = jsonNode.fieldNames(); keysIterator.hasNext();) {
            String key = keysIterator.next().toString();
            if (CHILD_NODE_HOLDER.equalsIgnoreCase(key)) {
                continue;
            }
            properties.set(key, jsonNode.get(key));
        }
        return properties;
    }

    private String newNodeName( String path ) {
        int lastSlashInd = path.lastIndexOf('/');
        String name = lastSlashInd == -1 ? path : path.substring(lastSlashInd + 1);
        // Remove any SNS index ...
        name = name.replaceAll("\\[\\d+\\]$", "");
        return name;
    }

    /**
     * Updates the properties at the path.
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
     * @return the JSON-encoded representation of the node on which the property or properties were set.
     * @throws JSONException if there is an error encoding the node
     * @throws RepositoryException if any error occurs at the repository level.
     */
    public RestItem updateItem( 
    		WcmEvent.WcmItemType itemType,
    		String baseUrl,
            String rawRepositoryName,
            String rawWorkspaceName,
            String path,
            String requestContent ) throws IOException, RepositoryException {
    	List<String> currentDescendants = new ArrayList<String>();		
    	if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
			RestNode restNode = (RestNode)this.item(baseUrl, rawRepositoryName, WcmConstants.DEFAULT_WS, path, WcmConstants.FULL_SUB_DEPTH);
			syndicationUtils.populateDescendantIds(restNode, currentDescendants);
		}
        Session session = getSession(rawRepositoryName, rawWorkspaceName);
        Item item = itemAtPath(path, session);
        JsonNode json = stringToJsonNode(requestContent);
        item = updateItem(item, json);
        
        if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
        	int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
			// RestNode restNode = (RestNode)this.item(baseUrl, rawRepositoryName, WcmConstants.DEFAULT_WS, path, depth);
        	RestNode restNode = (RestNode)createRestItem(baseUrl, depth, session, item);
			syndicationUtils.addUpdateItemEvent(
					restNode, 
					rawRepositoryName, 
					WcmConstants.DEFAULT_WS,  
					path,
					itemType,
					currentDescendants);
		}
        session.save();
        
        if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
			Session drafrSession = this.repositoryManager.getSession(rawWorkspaceName, WcmConstants.DRAFT_WS);
			drafrSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
		}
        
        RestItem restItem = createRestItem(baseUrl, 0, session, item);
        return restItem;
    }

    public RestItem updateItem( 
    		WcmEvent.WcmItemType itemType,
    		String baseUrl,
            String rawRepositoryName,
            String rawWorkspaceName,
            String path,
            JsonNode jsonItem ) throws IOException, RepositoryException {
    	
    	List<String> currentDescendants = new ArrayList<String>();		
    	if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
			RestNode restNode = (RestNode)this.item(baseUrl, rawRepositoryName, WcmConstants.DEFAULT_WS, path, WcmConstants.FULL_SUB_DEPTH);
			syndicationUtils.populateDescendantIds(restNode, currentDescendants);
		}	
		
		Session session = getSession(rawRepositoryName, rawWorkspaceName);
		Item item = itemAtPath(path, session);
		item = updateItem(item, jsonItem);
		if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
        	int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
			// RestNode restNode = (RestNode)this.item(baseUrl, rawRepositoryName, WcmConstants.DEFAULT_WS, path, depth);
        	RestNode restNode = (RestNode)createRestItem(baseUrl, depth, session, item);
			syndicationUtils.addUpdateItemEvent(
					restNode, 
					rawRepositoryName, 
					WcmConstants.DEFAULT_WS,  
					path,
					itemType,
					currentDescendants);
		}
		session.save();
		if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(rawWorkspaceName)) {
			Session drafrSession = this.repositoryManager.getSession(rawWorkspaceName, WcmConstants.DRAFT_WS);
			drafrSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
		}
		RestItem restItem  = createRestItem(baseUrl, 0, session, item);
		return restItem;
	}
    
    private JsonNode inputStreamToJsonNode( InputStream requestBody ) throws IOException {
        return this.mapper.readTree(requestBody);
    }
    
    private JsonNode stringToJsonNode( String requestBody ) throws IOException {
        return StringUtil.isBlank(requestBody) ? this.mapper.createObjectNode() : this.mapper.readTree(requestBody);
    }

    private ArrayNode stringToJsonArray( String requestBody ) throws IOException {
    	return StringUtil.isBlank(requestBody) ? this.mapper.createArrayNode() : (ArrayNode) this.mapper.readTree(requestBody);
    }

    /**
     * Performs a bulk creation of items, using a single {@link Session}. If any of the items cannot be created for whatever
     * reason, the entire operation fails.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the nodes and, possibly, properties to be added
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws JSONException if the body of the request is not a valid JSON object
     * @throws RepositoryException if any of the JCR operations fail
     * @see RestWcmItemHandler#addItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    public ResponseEntity<?> addItems( //HttpServletRequest request,
    		String baseUrl,
                              String repositoryName,
                              String workspaceName,
                              String requestContent ) throws IOException, RepositoryException {
    	JsonNode requestBody = this.stringToJsonNode(requestContent);
    	return this.doAddItems(baseUrl, repositoryName, workspaceName, requestBody);
    }

    /**
     * Performs a bulk creation of items, using a single {@link Session}. If any of the items cannot be created for whatever
     * reason, the entire operation fails.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the nodes and, possibly, properties to be added
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws JSONException if the body of the request is not a valid JSON object
     * @throws RepositoryException if any of the JCR operations fail
     * @see RestWcmItemHandler#addItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    public ResponseEntity<?> addItems( //HttpServletRequest request,
    		String baseUrl,
                              String repositoryName,
                              String workspaceName,
                              InputStream requestContent ) throws IOException, RepositoryException {
    	JsonNode jsonNode = this.inputStreamToJsonNode(requestContent);
		ArrayNode wcmItems = (ArrayNode)jsonNode.get("wcmItems");
        return this.doAddItems(baseUrl, repositoryName, workspaceName, wcmItems);
    }
    
//    protected void loadItems(String baseUrl, String repository, String workspace, InputStream is) throws IOException, RepositoryException {
//		try {
//			JsonNode jsonNode = JsonUtils.inputStreamToJsonNode(is);
//			ArrayNode wcmItems = (ArrayNode)jsonNode.get("wcmItems");
//			// for (int i = 0; i < wcmItems.size(); i++) {
//			this.wcmItemHandler.addItems(
//					baseUrl, 
//					repository, 
//					workspace,
//					wcmItems);
//			//}
//		} catch (Throwable t) {
//    		throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.MODESHAPE_POST_ITEMS, null));
//    	} 
//	}
	
    
//    public ResponseEntity<?> addItems( //HttpServletRequest request,
//    		String baseUrl,
//                              String repositoryName,
//                              String workspaceName,
//                              JsonNode requestBody ) throws IOException, RepositoryException {
//        return this.doAddItems(baseUrl, repositoryName, workspaceName, requestBody);
//    }
    
    /**
     * Performs a bulk updating of items, using a single {@link Session}. If any of the items cannot be updated for whatever
     * reason, the entire operation fails.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded representation of the values and, possibly, properties to be set
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws JSONException if the body of the request is not a valid JSON object
     * @throws RepositoryException if any of the JCR operations fail
     * @see RestWcmItemHandler#updateItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    public ResponseEntity<?> updateItems( 
    		 String baseUrl,
	         String repositoryName,
	         String workspaceName,
	         String requestContent ) throws IOException, RepositoryException {
    	JsonNode requestBody = stringToJsonNode(requestContent).get("wcmItems");
        if (requestBody.size() == 0) {
            return ResponseEntity.ok().build();
        }
        Session session = getSession(repositoryName, workspaceName);
        TreeMap<String, JsonNode> nodesByPath = createWcmNodesByPathMap(requestBody);
        List<RestItem> result = updateMultipleWcmNodes(baseUrl, repositoryName, session, nodesByPath);
        return createOkResponse(result);
    }
    
    public void deleteItem(WcmEvent.WcmItemType itemType,  String baseUrl, String repository, String workspace, String path) 
    		throws PathNotFoundException, RepositoryException {

		path = (path.startsWith("/")) ? path : String.format("/%s", path);
		List<WcmEvent> wcmEvents = new ArrayList<>();
		Session session = this.repositoryManager.getSession(repository, workspace);
		Session draftSession = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
		Session expiredSession = this.repositoryManager.getSession(repository, WcmConstants.EXPIRED_WS);
		this.doDelete(path, itemType, baseUrl, repository, workspace, session, draftSession, expiredSession, wcmEvents);
		if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
			syndicationUtils.addDeleteItemEvents(wcmEvents);
		}
		if (WcmConstants.DEFAULT_WS.equals(workspace)) {
	        draftSession.save();
	        expiredSession.save();
		}
        
		session.save();
	}

    protected void doDelete( 
    		String path,
    		WcmEvent.WcmItemType itemType,
    		String baseUrl,
    		String repository,
    		String workspace,
            Session session,
            Session draftSession,
            Session expiredSession,
            List<WcmEvent> wcmEvents) throws RepositoryException {
    	//TODO: only delete from all three workspaces when it is deleting from WcmConstants.DEFAULT_WS
    	List<String> currentDescendants = new ArrayList<String>();	
  		String nodeId = null;

		if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(workspace)) {
			int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
			RestNode restNode = (RestNode)this.item(baseUrl, repository, workspace, path, depth);
			nodeId = restNode.getId();
			syndicationUtils.populateDescendantIds(restNode, currentDescendants);
		}	
		if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(workspace)) {
			try {
				
				Node draftNode = draftSession.getNode(path);
				draftNode.remove();
			} catch (Exception e) {
				logger.warn(String.format("Failed to delete item %s from draft repository", path), e);
			}
			try {
				Node expiredNode = expiredSession.getNode(path);
				expiredNode.remove();
			} catch (Exception e) {
				logger.warn(String.format("Failed to delete item %s from expired repository", path), e);
			}
		}
		Node node = session.getNode(path);
		node.remove();
		if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(workspace)) {
			wcmEvents.add(syndicationUtils.createDeleteItemEvent(
					nodeId, 
					repository, 
					workspace, 
					path,
					itemType,
					currentDescendants));
		}
		
    }
    
    /**
     * Performs a bulk deletion of items, using a single {@link Session}. If any of the items cannot be deleted for whatever
     * reason, the entire operation fails.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param requestContent the JSON-encoded array of the nodes to remove
     * @return a {@code non-null} {@link ResponseEntity}
     * @throws JSONException if the body of the request is not a valid JSON array
     * @throws RepositoryException if any of the JCR operations fail
     * @see RestWcmItemHandler#deleteItem(javax.servlet.http.HttpServletRequest, String, String, String)
     */
    public ResponseEntity<Void> deleteItems( 
    		String baseUrl,
            String repositoryName,
            String workspace,
            String requestContent ) throws IOException, RepositoryException {
        ArrayNode requestArray = stringToJsonArray(requestContent);
        if (requestArray.size() == 0) {
            return ResponseEntity.ok().build();
        }

        Session session = getSession(repositoryName, workspace);
        Session draftSession = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
        Session expiredSession = this.repositoryManager.getSession(repositoryName, WcmConstants.EXPIRED_WS);
        TreeSet<String> pathsInOrder = new TreeSet<>();
        Map<String, WcmEvent.WcmItemType> itemTypes = new HashMap<>();
        for (int i = 0; i < requestArray.size(); i++) {
        	String absPath = absPath(requestArray.get(i).get("wcmItem").asText());
            pathsInOrder.add(absPath);
            itemTypes.put(absPath, WcmEvent.WcmItemType.valueOf(requestArray.get(i).get("itemType").asText()));
        }
        List<String> pathsInOrderList = new ArrayList<>(pathsInOrder);
        Collections.reverse(pathsInOrderList);

        List<WcmEvent> wcmEvents = new ArrayList<>();
        for (String path : pathsInOrderList) {
            try {
                doDelete( path, itemTypes.get(path), baseUrl, repositoryName, workspace, session, draftSession, expiredSession, wcmEvents);
            } catch (PathNotFoundException e) {
                logger.info("Node at path {0} already deleted", path);
            }
        }
        if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
			syndicationUtils.addDeleteItemEvents(wcmEvents);
		}
        if (WcmConstants.DEFAULT_WS.equals(workspace)) {
	        draftSession.save();
	        expiredSession.save();
		}
        session.save();
        return ResponseEntity.ok().build();
    }

    private List<RestItem> updateMultipleWcmNodes( 
    		String baseUrl,
    		String repositoryName,
            Session session,
            TreeMap<String, JsonNode> wcmNodesByPath)
        throws RepositoryException, IOException {
        List<RestItem> result = new ArrayList<RestItem>();
        List<WcmEvent> wcmEvents = new ArrayList<WcmEvent>();
        for (String nodePath : wcmNodesByPath.keySet()) {
        	JsonNode jsonNode = wcmNodesByPath.get(nodePath);
        	List<String> currentDescendants = new ArrayList<String>();		
        	if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
    			RestNode restNode = (RestNode)this.item(
    					baseUrl, 
    					repositoryName, 
    					WcmConstants.DEFAULT_WS, 
    					nodePath, 
    					WcmConstants.FULL_SUB_DEPTH);
    			syndicationUtils.populateDescendantIds(restNode, currentDescendants);
    		}	
        	// Node newNode = addNode(parentNode, newNodeName, jsonNode.get("content"));
            TreeMap<String, JsonNode> jcrNodesByPath = createJcrNodesByPathMap(jsonNode.get("content"));
            List<RestItem> restNodes = updateMultipleJcrNodes(baseUrl, repositoryName, session, jcrNodesByPath);
            result.addAll(restNodes);
            
            if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
            	WcmEvent.WcmItemType itemType = WcmEvent.WcmItemType.valueOf(jsonNode.get("itemType").asText());
            	int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
    			RestNode restNode = (RestNode)this.item(baseUrl, repositoryName, WcmConstants.DEFAULT_WS, nodePath, depth);
            	// RestNode restNode = (RestNode)createRestItem(baseUrl, depth, session, item);
    			wcmEvents.add(syndicationUtils.createUpdateItemEvent(
    					restNode, 
    					repositoryName, 
    					WcmConstants.DEFAULT_WS,  
    					nodePath,
    					itemType,
    					currentDescendants));
    		}
        }
        
        syndicationUtils.addUpdateItemEvents( wcmEvents);
        session.save();
        if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
        	Session drafrSession = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
        	for (String nodePath : wcmNodesByPath.keySet()) {
        		drafrSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, nodePath, nodePath, true);
        	}
        }
        
        return result;
    }

    private List<RestItem> updateMultipleJcrNodes( 
    		String baseUrl,
    		String repositoryName,
            Session session,
            TreeMap<String, JsonNode> nodesByPath)
        throws RepositoryException, IOException {
        List<RestItem> result = new ArrayList<RestItem>();
        for (String nodePath : nodesByPath.keySet()) {
        	JsonNode jsonNode = nodesByPath.get(nodePath);
        	Item item = session.getItem(nodePath);
            item = updateItem(item, jsonNode.get("content"));
            result.add(createRestItem(baseUrl, 0, session, item));
        }
        
        return result;
    }
    private TreeMap<String, JsonNode> createWcmNodesByPathMap( JsonNode requestBodyJSON ) throws IOException {
        TreeMap<String, JsonNode> nodesByPath = new TreeMap<>();
        
        for (Iterator<JsonNode> iterator = requestBodyJSON.iterator(); iterator.hasNext();) {
            JsonNode jsonNode = iterator.next();
            String key = jsonNode.get("wcmPath").asText();
        	// String key = iterator.next().toString();
            String nodePath = absPath(key);
            // JsonNode nodeJSON = requestBodyJSON.get(key);
            nodesByPath.put(nodePath, jsonNode);
        }
        return nodesByPath;
    }

    private TreeMap<String, JsonNode> createJcrNodesByPathMap( JsonNode requestBodyJSON ) throws IOException {
        TreeMap<String, JsonNode> nodesByPath = new TreeMap<>();
        for (Iterator<?> iterator = requestBodyJSON.fieldNames(); iterator.hasNext();) {
            String key = iterator.next().toString();
            String nodePath = absPath(key);
            JsonNode nodeJSON = requestBodyJSON.get(key);
            nodesByPath.put(nodePath, nodeJSON);
        }
        
        return nodesByPath;
    }
    private ResponseEntity<List<RestItem>> addMultipleWcmNodes( 
    		String baseUrl,
	    	String repositoryName,
	        TreeMap<String, JsonNode> wcmNodesByPath,
	        Session session ) throws RepositoryException, IOException {
    	
        List<RestItem> result = new ArrayList<RestItem>();
        List<WcmEvent> wcmEvents = new ArrayList<WcmEvent>();
	        
        for (String nodePath : wcmNodesByPath.keySet()) {
            JsonNode jsonNode = wcmNodesByPath.get(nodePath);
            TreeMap<String, JsonNode> nodesByPath = createJcrNodesByPathMap(jsonNode.get("content"));
            List<RestItem> restNewNodes = addMultipleJcrNodes(baseUrl, repositoryName, nodesByPath, session);
            result.addAll(restNewNodes);
            if (this.syndicationEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
            	WcmEvent.WcmItemType itemType = WcmEvent.WcmItemType.valueOf(jsonNode.get("itemType").asText());
            	int depth = WcmEvent.WcmItemType.library.equals(itemType) ? WcmConstants.READ_DEPTH_TWO_LEVEL : WcmConstants.FULL_SUB_DEPTH;
    			RestNode restNode = (RestNode)this.item(baseUrl, repositoryName, WcmConstants.DEFAULT_WS, nodePath, depth);
            	// RestNode restNode = (RestNode)createRestItem(baseUrl, depth, session, newNode);
            	 wcmEvents.add(syndicationUtils.createNewItemEvent(
    					restNode,
    					repositoryName,
    					WcmConstants.DEFAULT_WS, 
    					nodePath,
    					itemType));
    		}
            
        }
        syndicationUtils.addNewItemEvents( wcmEvents);
        session.save();
        if (this.authoringEnabled && WcmConstants.DEFAULT_WS.equals(session.getWorkspace().getName())) {
        	Session drafrSession = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
        	for (String nodePath : wcmNodesByPath.keySet()) {
        		drafrSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, nodePath, nodePath, true);
        	}
        }

        return createOkResponse(result);
    }
    
    private List<RestItem> addMultipleJcrNodes( 
    		String baseUrl,
	    	String repositoryName,
	        TreeMap<String, JsonNode> nodesByPath,
	        Session session ) throws RepositoryException, IOException {
    	
        List<RestItem> result = new ArrayList<RestItem>();
	        
        for (String nodePath : nodesByPath.keySet()) {
            String parentAbsPath = parentPath(nodePath);
            String newNodeName = newNodeName(nodePath);

            Node parentNode = (Node) session.getItem(parentAbsPath);
            JsonNode jsonNode = nodesByPath.get(nodePath);
            Node newNode = addNode(parentNode, newNodeName, jsonNode);
            RestItem restNewNode = createRestItem(baseUrl, 0, session, newNode);
            result.add(restNewNode);
        }
        
        return result;
    }
    
    private ResponseEntity<List<RestItem>> createOkResponse( final List<RestItem> result ) {
        return ResponseEntity.ok().body(result);
    }
    
    private ResponseEntity<?> doAddItems(
    		String baseUrl,
            String repositoryName,
            String workspaceName,
            JsonNode requestBody) throws IOException, RepositoryException {
    	if (requestBody.size() == 0) {
            return ResponseEntity.ok().build();
        }
        Session session = getSession(repositoryName, workspaceName);
        TreeMap<String, JsonNode> nodesByPath = createWcmNodesByPathMap(requestBody);
        return addMultipleWcmNodes(baseUrl, repositoryName, nodesByPath, session);
    }
}
