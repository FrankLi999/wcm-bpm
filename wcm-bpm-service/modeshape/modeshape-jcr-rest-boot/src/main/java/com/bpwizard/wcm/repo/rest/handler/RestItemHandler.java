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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.modeshape.common.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.modeshape.model.RestItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * An extension to the {@link ItemHandler} which is used by {@link org.modeshape.web.jcr.rest.ModeShapeRestService} to interact
 * with properties and nodes.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Component
public final class RestItemHandler extends ItemHandler {

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
    		String baseUrl,
                             String repositoryName,
                             String workspaceName,
                             String path,
                             String requestBody ) throws IOException, RepositoryException {
    	
       JsonNode requestBodyJSON = stringToJsonNode(requestBody);
       return this.addItem(baseUrl, repositoryName, workspaceName, path, requestBodyJSON);
    }

    public ResponseEntity<RestItem> addItem( 
    		//HttpServletRequest request,
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

        session.save();
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
    		//HttpServletRequest request,
    		String baseUrl,
                                String rawRepositoryName,
                                String rawWorkspaceName,
                                String path,
                                String requestContent ) throws IOException, RepositoryException {
        Session session = getSession(rawRepositoryName, rawWorkspaceName);
        Item item = itemAtPath(path, session);
        item = updateItem(item, stringToJsonNode(requestContent));
        session.save();

        return createRestItem(baseUrl, 0, session, item);
    }

    public RestItem updateItem( 
    		//HttpServletRequest request,
    		String baseUrl,
            String rawRepositoryName,
            String rawWorkspaceName,
            String path,
            JsonNode jsonItem ) throws IOException, RepositoryException {
		Session session = getSession(rawRepositoryName, rawWorkspaceName);
		Item item = itemAtPath(path, session);
		item = updateItem(item, jsonItem);
		session.save();
		
		return createRestItem(baseUrl, 0, session, item);
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
     * @see RestItemHandler#addItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
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
     * @see RestItemHandler#addItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    public ResponseEntity<?> addItems( //HttpServletRequest request,
    		String baseUrl,
                              String repositoryName,
                              String workspaceName,
                              InputStream requestContent ) throws IOException, RepositoryException {
    	JsonNode requestBody = this.inputStreamToJsonNode(requestContent);
        return this.doAddItems(baseUrl, repositoryName, workspaceName, requestBody);
    }
    
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
     * @see RestItemHandler#updateItem(javax.servlet.http.HttpServletRequest, String, String, String, String)
     */
    public ResponseEntity<?> updateItems( 
    		//HttpServletRequest request,
    		String baseUrl,
                                 String repositoryName,
                                 String workspaceName,
                                 String requestContent ) throws IOException, RepositoryException {
    	JsonNode requestBody = stringToJsonNode(requestContent);
        if (requestBody.size() == 0) {
            return ResponseEntity.ok().build();
        }
        Session session = getSession(repositoryName, workspaceName);
        TreeMap<String, JsonNode> nodesByPath = createNodesByPathMap(requestBody);
        List<RestItem> result = updateMultipleNodes(baseUrl, session, nodesByPath);
        return createOkResponse(result);
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
     * @see RestItemHandler#deleteItem(javax.servlet.http.HttpServletRequest, String, String, String)
     */
    public ResponseEntity<Void> deleteItems( // HttpServletRequest request,
                                 String repositoryName,
                                 String workspaceName,
                                 String requestContent ) throws IOException, RepositoryException {
        ArrayNode requestArray = stringToJsonArray(requestContent);
        if (requestArray.size() == 0) {
            return ResponseEntity.ok().build();
        }

        Session session = getSession(repositoryName, workspaceName);
        TreeSet<String> pathsInOrder = new TreeSet<>();
        for (int i = 0; i < requestArray.size(); i++) {
            pathsInOrder.add(absPath(requestArray.get(i).asText()));
        }
        List<String> pathsInOrderList = new ArrayList<>(pathsInOrder);
        Collections.reverse(pathsInOrderList);
        for (String path : pathsInOrderList) {
            try {
                doDelete(path, session);
            } catch (PathNotFoundException e) {
                logger.info("Node at path {0} already deleted", path);
            }
        }
        session.save();
        return ResponseEntity.ok().build();
    }

    private List<RestItem> updateMultipleNodes( // HttpServletRequest request,
    		String baseUrl,
                                                Session session,
                                                TreeMap<String, JsonNode> nodesByPath )
        throws RepositoryException, IOException {
        List<RestItem> result = new ArrayList<RestItem>();
        for (String nodePath : nodesByPath.keySet()) {
            Item item = session.getItem(nodePath);
            item = updateItem(item, nodesByPath.get(nodePath));
            result.add(createRestItem(baseUrl, 0, session, item));
        }
        session.save();
        return result;
    }

    private TreeMap<String, JsonNode> createNodesByPathMap( JsonNode requestBodyJSON ) throws IOException {
        TreeMap<String, JsonNode> nodesByPath = new TreeMap<>();
        for (Iterator<?> iterator = requestBodyJSON.fieldNames(); iterator.hasNext();) {
            String key = iterator.next().toString();
            String nodePath = absPath(key);
            JsonNode nodeJSON = requestBodyJSON.get(key);
            nodesByPath.put(nodePath, nodeJSON);
        }
        return nodesByPath;
    }

    private ResponseEntity<List<RestItem>> addMultipleNodes( 
    		//HttpServletRequest request,
    		String baseUrl,
                                       TreeMap<String, JsonNode> nodesByPath,
                                       Session session ) throws RepositoryException, IOException {
        List<RestItem> result = new ArrayList<RestItem>();

        for (String nodePath : nodesByPath.keySet()) {
            String parentAbsPath = parentPath(nodePath);
            String newNodeName = newNodeName(nodePath);

            Node parentNode = (Node)session.getItem(parentAbsPath);
            Node newNode = addNode(parentNode, newNodeName, nodesByPath.get(nodePath));
            RestItem restNewNode = createRestItem(baseUrl, 0, session, newNode);
            result.add(restNewNode);
        }

        session.save();
        return createOkResponse(result);
    }

    private ResponseEntity<List<RestItem>> createOkResponse( final List<RestItem> result ) {
        return ResponseEntity.ok().body(result);
    }
    
    private ResponseEntity<?> doAddItems(
    		//HttpServletRequest request,
    		String baseUrl,
            String repositoryName,
            String workspaceName,
            JsonNode requestBody) throws IOException, RepositoryException {
    	if (requestBody.size() == 0) {
            return ResponseEntity.ok().build();
        }
        Session session = getSession(repositoryName, workspaceName);
        TreeMap<String, JsonNode> nodesByPath = createNodesByPathMap(requestBody);
        return addMultipleNodes(baseUrl, nodesByPath, session);
    }
}
