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

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
// import javax.servlet.http.HttpServletRequest;

import org.modeshape.common.util.StringUtil;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.modeshape.model.RestItem;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * An extension to the {@link ItemHandler} which is used by {@link org.modeshape.web.jcr.rest.ModeShapeRestService} to interact
 * with properties and nodes.
 */
@Component
public final class RestNodeHandler extends ItemHandler {

    /**
     * Retrieves the JCR {@link Item} at the given path, returning its rest representation.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param repositoryName the URL-encoded repository name
     * @param workspaceName the URL-encoded workspace name
     * @param id the node identifier
     * @param depth the depth of the node graph that should be returned if {@code path} refers to a node. @{code 0} means return
     *        the requested node only. A negative value indicates that the full subgraph under the node should be returned. This
     *        parameter defaults to {@code 0} and is ignored if {@code path} refers to a property.
     * @return a the rest representation of the item, as a {@link RestItem} instance.
     * @throws RepositoryException if any JCR operations fail.
     */
    public RestItem nodeWithId( // HttpServletRequest request,
    		String baseUrl,
                                String repositoryName,
                                String workspaceName,
                                String id,
                                int depth ) throws RepositoryException {
        Session session = getSession(repositoryName, workspaceName);
        Node node = nodeWithId(id, session);
        return createRestItem(baseUrl, depth, session, node);
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
     * @param id the node identifier
     * @param requestContent the JSON-encoded representation of the values and, possibly, properties to be set
     * @return the JSON-encoded representation of the node on which the property or properties were set.
     * @throws JSONException if there is an error encoding the node
     * @throws RepositoryException if any error occurs at the repository level.
     */
    public RestItem updateNodeWithId( //HttpServletRequest request,
    		String baseUrl,
                                      String rawRepositoryName,
                                      String rawWorkspaceName,
                                      String id,
                                      String requestContent ) throws IOException, RepositoryException {
        Session session = getSession(rawRepositoryName, rawWorkspaceName);
        Node node = nodeWithId(id, session);
        node = updateNode(node, stringToJSONObject(requestContent));
        session.save();

        return createRestItem(baseUrl, 0, session, node);
    }

    private JsonNode stringToJSONObject( String requestBody ) throws IOException {
        return StringUtil.isBlank(requestBody) ? this.mapper.createObjectNode() : this.mapper.readTree(requestBody);
    }

    /**
     * Deletes the subgraph at the node with the specified id, including all descendants.
     *
     * @param request the servlet request; may not be null or unauthenticated
     * @param rawRepositoryName the URL-encoded repository name
     * @param rawWorkspaceName the URL-encoded workspace name
     * @param id the node identifier
     * @throws NotFoundException if no item exists at {@code path}
     * @throws javax.ws.rs.NotAuthorizedException if the user does not have the access required to delete the node with this id.
     * @throws RepositoryException if any other error occurs
     */
    public void deleteNodeWithId( // HttpServletRequest request,
                                  String rawRepositoryName,
                                  String rawWorkspaceName,
                                  String id ) throws RepositoryException {

        assert rawRepositoryName != null;
        assert rawWorkspaceName != null;
        assert id != null;

        Session session = getSession(rawRepositoryName, rawWorkspaceName);
        Node node = nodeWithId(id, session);
        node.remove();
        session.save();
    }

    @Override
    protected Node nodeWithId( String id,
                               Session session ) throws RepositoryException {
        //try {
            return session.getNodeByIdentifier(id);
        // } catch (ItemNotFoundException infe) {
        //    throw new NotFoundException(infe.getMessage(), infe);
        //}
    }

}
