package com.bpwizard.wcm.repo.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventJournal;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.jcr.api.index.IndexColumnDefinitionTemplate;
import org.modeshape.jcr.api.index.IndexDefinition;
import org.modeshape.jcr.api.index.IndexDefinition.IndexKind;
import org.modeshape.jcr.api.index.IndexDefinitionTemplate;
import org.modeshape.jcr.api.query.Query;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.dto.IndexColumn;
import com.bpwizard.wcm.repo.dto.IndexModel;
import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.QueryStatement;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(ModeshapeController.BASE_URI)
public class ModeshapeController {
	private static final Logger logger = LogManager.getLogger(ModeshapeController.class);
	public static final String BASE_URI = "/modeshape/server";
	@Autowired
	private RepositoryManager repositoryManager;
	@Autowired
	private RestItemHandler itemHandler;
	
	@PostMapping(path = "/execQuery", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> execQueryStatement(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, WcmConstants.DEFAULT_WS).getWorkspace().getQueryManager();
				javax.jcr.query.Query jcrQuery = qrm.createQuery(query.getQuery(), Query.JCR_SQL2);
				QueryResult jcrResult = jcrQuery.execute();
				String columnNames[] = jcrResult.getColumnNames();
				if (columnNames != null && columnNames.length > 0) {
					ArrayNode valueArray = JsonUtils.creatArrayNode();
					for (String value : columnNames) {
						valueArray.add(value);
					}
					qJson.set("bpw:columns", valueArray);	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}	
	}
	
	@PostMapping(path = "/execAndResult", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> execAndResult(@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			// ObjectNode qJson = (ObjectNode) query.toJson();
			ObjectNode qJson = JsonUtils.createObjectNode();
			QueryManager qrm = this.repositoryManager.getSession(repositoryName, WcmConstants.DEFAULT_WS).getWorkspace().getQueryManager();
			javax.jcr.query.Query jcrQuery = qrm.createQuery(query.getQuery(), Query.JCR_SQL2);
			QueryResult jcrResult = jcrQuery.execute();
			String columnNames[] = jcrResult.getColumnNames();
			if (columnNames != null && columnNames.length > 0) {
				ArrayNode valueArray = JsonUtils.creatArrayNode();
				for (String value : columnNames) {
					valueArray.add(value);
				}
				qJson.set("bpw:columns", valueArray);	
			}
			ArrayNode rowNodes = JsonUtils.creatArrayNode();
			qJson.set("bpw:rows", rowNodes);
			RowIterator iterator = jcrResult.getRows();
			while (iterator.hasNext()) {
				Row row = iterator.nextRow();
				ObjectNode rowNode = JsonUtils.createObjectNode();
				rowNodes.add(rowNode);
				for (String columnName: columnNames) {
					System.out.println("==================================================");
					System.out.println("columnName:" + columnName + ", values:" + row.getValue(columnName));
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					// rowNode.set(columnName, JsonUtils.createTextNode(row.getValue(columnName).getString()));
					if (null != row.getValue(columnName)) {
						rowNode.put(columnName, row.getValue(columnName).getString());
					}
				}
//				System.out.println("==================================================");
//				System.out.println("element.jcr:path" + row.getValue("news.jcr:path"));
//				System.out.println("element.jcr:name" + row.getValue("news.jcr:name"));
//				System.out.println("element.bpw:value" + row.getValue("news.bpw:value"));
//				System.out.println("content.jcr:name" + row.getValue("news.jcr:name"));
//				System.out.println("content.jcr:path" + row.getValue("news.jcr:path"));
//				System.out.println("content.jcr:score" + row.getValue("news.jcr:score"));
//				System.out.println("element.jcr:score" + row.getValue("news.jcr:score"));
				System.out.println("==================================================");
			}
			return ResponseEntity.ok(qJson);
			//return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}	
	}
	
	@PostMapping(path = "/addQuery", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addQuery(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, path, qJson);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));

		}	
	}
	
	@PostMapping(path = "/addQueryAndClone", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addQueryAndClone(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			
			// javax.jcr.query.qom.QueryObjectModel qom = null
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WcmConstants.NODE_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, path, qJson);

			Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
			session.getWorkspace().clone(WcmConstants.DEFAULT_WS, path, path, true);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
	    } catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));

		}	
	}
	@GetMapping("/ping")
	public String modeShape() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO);
			// Get the root node ...
            Node root = session.getRootNode();
            
            assert root != null;
            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            logger.debug("Added one node under root");
            logger.debug("+ Root childs");
            session.exportSystemView("/folder", System.out, false, false);
            System.out.println("+-- jcr value factory -> " + session.getValueFactory());
            // org.modeshape.jcr.JcrValueFactory
            
            NodeIterator it = root.getNodes();
            while (it.hasNext()) {
            	Node n = it.nextNode();
            	// logger.debug("+---> " + it.nextNode().getName());
            	System.out.println("+---> " + n.getName());
            	System.out.println("+------path---> " + n.getPath());
            	System.out.println("+------p type---> " + n.getPrimaryNodeType().getName());
            	
            }
    		return "{\"modeshape\": \"up\"}";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
        } 
	}
	
	@GetMapping("/export/{folder}")
	public String exportContent(@PathVariable("folder") String folder) {
		Session session = null;
		try {
			session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO);
			// Get the root node ...
            Node root = session.getRootNode();
            
            assert root != null;
            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            logger.debug("Added one node under root");
            logger.debug("+ Root childs");
            String absPath = "root".equals(folder) ? "/" : ("/" + folder);
            OutputStream os = new FileOutputStream("c:\\temp\\sample_content.xml");
            session.exportSystemView(absPath, os, false, false);
            os.close();
    		return "{\"export\": \"done\"}";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
        } 
	}
	
	@GetMapping("/import")
	public String importContent() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO);
			// Get the root node ...
            Node root = session.getRootNode();
            
            assert root != null;
            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            // Node n = root.addNode("Node" + rnd.nextInt());

            // n.setProperty("key", "value");
            // n.setProperty("content", session.getValueFactory().createBinary(new ByteArrayInputStream(new byte[1000])));

            // 
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("bpw_content.xml");
            session.importXML("/", in, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
            session.save();
            logger.debug("Added one node under root");
            logger.debug("+ Root childs");
            return "{\"import\": \"done\"}";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
        } 
	}
	
	@GetMapping("/node-types")
	public String getNodeTypes() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO);
			//Random rnd = new Random();
			// Get the root node ...
            Node root = session.getRootNode();
            assert root != null;
            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            // Node n = root.addNode("Node" + rnd.nextInt());

            // n.setProperty("key", "value");
            // n.setProperty("content", session.getValueFactory().createBinary(new ByteArrayInputStream(new byte[1000])));

            // session.save();

            logger.debug("Added one node under root");
            logger.debug("+ Root childs");

            javax.jcr.nodetype.NodeTypeManager nodeTypeMgr = session.getWorkspace().getNodeTypeManager();
            NodeTypeIterator nodeTypeIterator = nodeTypeMgr.getAllNodeTypes();
            int i = 1;
            while(nodeTypeIterator.hasNext()) {
            	NodeType nodeType = nodeTypeIterator.nextNodeType();
            	System.out.println(">>>>>>>>>>> nodeType " + i++ + ":" + nodeType.getName());
            	System.out.println("      ===========  getPrimaryItemName" + nodeType.getPrimaryItemName());
            	NodeDefinition[] cnds = nodeType.getChildNodeDefinitions();
            	for (int k = 0; k < cnds.length; k++) {
            		System.out.println("              ****************  NodeDefinition "  + i + ":"+ cnds[k].getName());
            	}
            	
            	PropertyDefinition[] pds = nodeType.getPropertyDefinitions();
            	for (int k = 0; k < pds.length; k++) {
            		System.out.println("              &&&&&&&&&&&&&&&  PropertyDefinition "  + i + ":"+ pds[k].getName());
            	}
            }
            return "{\"node-type\": \"up\"}";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
        } 
	}
	
	@GetMapping("/journalEvent")
	public ResponseEntity<?> getJournalEvent() {
		try {
			Workspace workspace = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS).getWorkspace();
			EventJournal eventJournal = workspace.getObservationManager().getEventJournal();
			logger.debug("Journal Events>>>>");

			Map<String, String> eventsTypes = new HashMap<>();
			while (eventJournal.hasNext()) {
				try {
					Event event = eventJournal.nextEvent();
					logger.debug("Journal Event:" + event.getClass());
					logger.debug(event);
					eventsTypes.put(event.getClass().toString(), event.toString());
				} catch (java.util.NoSuchElementException ex) {
					logger.debug("donw event journaling");
					break;
				}
			}
			return ResponseEntity.ok().body(eventsTypes);
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/journal")
	public ResponseEntity<?> getJournal() {
		try {
			Workspace workspace = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS).getWorkspace();
			EventJournal eventJournal = workspace.getObservationManager().getEventJournal();
			logger.debug("Journal >>>>");
			Map<String, String> eventsTypes = new HashMap<>();
			eventJournal.forEachRemaining(e -> this.processEvent(e, eventsTypes));
			return ResponseEntity.ok().body(eventsTypes);
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
	
	private void processEvent(Object event, Map<String, String> eventsTypes) {
		logger.debug("Journal Event:" + event.getClass());
		logger.debug(event);
		eventsTypes.put(event.getClass().toString(), event.toString());
	}
	
	@GetMapping("/index")
	public ResponseEntity<?> getIndex() {
		try {
			org.modeshape.jcr.api.Workspace workspace = (org.modeshape.jcr.api.Workspace) this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS).getWorkspace();
			org.modeshape.jcr.api.index.IndexManager indexManager = workspace.getIndexManager();
			Map<String, IndexDefinition> indexes = indexManager.getIndexDefinitions();
			Map<String, IndexModel> indexModels = new HashMap<>();
			for (String key: indexes.keySet()) {
				IndexModel indexModel = new IndexModel();
				IndexDefinition indexDefinition = indexes.get(key);
				indexModel.setName(indexDefinition.getName());
				indexModel.setKind(indexDefinition.getKind().name());
				indexModel.setProvider(indexDefinition.getProviderName());
				indexModel.setNodeType(indexDefinition.getNodeTypeName());
				indexModel.setSynchronous(indexDefinition.isSynchronous());
				IndexColumn columns[] = new IndexColumn[indexDefinition.size()];
				for (int i = 0 ; i < indexDefinition.size(); i++) {
					columns[i] = new IndexColumn(
							indexDefinition.getColumnDefinition(i).getPropertyName(),
							PropertyType.nameFromValue(indexDefinition.getColumnDefinition(i).getColumnType()));
				}
				indexModel.setColumns(columns);
				indexModels.put(key, indexModel);
			}
			return ResponseEntity.ok().body(indexModels);
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
	@PostMapping("/index")
	public String createIndex(@RequestBody IndexModel indexModel) {
		try {
			org.modeshape.jcr.api.Workspace workspace = (org.modeshape.jcr.api.Workspace) this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS).getWorkspace();
			org.modeshape.jcr.api.index.IndexManager indexManager = workspace.getIndexManager();
			IndexDefinitionTemplate indexDefinition = indexManager.createIndexDefinitionTemplate();
			
			List<IndexColumnDefinitionTemplate> columnDefinitions = new ArrayList<>();
			for (int i = 0; i < indexModel.getColumns().length; i++) {
				IndexColumnDefinitionTemplate columnDefinition = indexManager.createIndexColumnDefinitionTemplate();
				columnDefinitions.add(columnDefinition);
				columnDefinition.setPropertyName(indexModel.getColumns()[i].getColumnName());
				columnDefinition.setColumnType(PropertyType.valueFromName(indexModel.getColumns()[i].getPropertyType()));
			}
			indexDefinition.setNodeTypeName(indexModel.getNodeType());
			indexDefinition.setProviderName(indexModel.getProvider());
			indexDefinition.setWorkspace(indexModel.getWorkspace());
			indexDefinition.setKind(IndexKind.valueOf(indexModel.getKind()));
			indexDefinition.setName(indexModel.getName());
			indexDefinition.setColumnDefinitions(columnDefinitions);
			indexDefinition.setSynchronous(indexModel.isSynchronous());
			indexManager.registerIndex(indexDefinition, true);
			return "done";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
	
	@DeleteMapping("/index")
	public String removeIndex(@PathVariable("index") String index) {
		try {
			org.modeshape.jcr.api.Workspace workspace = (org.modeshape.jcr.api.Workspace) this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS).getWorkspace();
			org.modeshape.jcr.api.index.IndexManager indexManager = workspace.getIndexManager();
			indexManager.unregisterIndexes(index);
			return "done";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
	
	@GetMapping("/features")
	public String features() throws Exception {
		try {
			Repository repo = this.repositoryManager.getRepository(WcmConstants.BPWIZARD_REPO);
			StringBuilder support = new StringBuilder("Descriptors").append(" : ");
			for (String desc: repo.getDescriptorKeys()) {
				support.append(desc).append("---:---").append(repo.getDescriptor(desc)).append("<br /><br />");
			}
			return support.toString();
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
		
	}
	
	@GetMapping("/testACL")
	public String testACL() throws Exception {
		try {
			String path = WcmConstants.NODE_ROOT_PATH;
			String[] readPrivileges = new String[] {
			  Privilege.JCR_READ
			};
			
			String[] editorPrivileges = new String[] {
			  Privilege.JCR_READ,
			  Privilege.JCR_WRITE,
			  Privilege.JCR_REMOVE_NODE,
			  Privilege.JCR_ADD_CHILD_NODES,
			  Privilege.JCR_REMOVE_CHILD_NODES,
			  Privilege.JCR_REMOVE_CHILD_NODES
			};
			
			String[] adminPrivileges = new String[] {
				Privilege.JCR_ALL 
			};
			// Principal principal = new TestPrincipal();
			
			Principal adminPrincipal = new TestPrincipal("admin@example.com");
			Principal demoPrincipal = new TestPrincipal("demo@example.com");  
			Principal anonymousPrincipal = new TestPrincipal("anonymouse"); 
			// Principal group = new TestGroup();
			Principal wcmViewer = new TestGroup("wcm-viewer");
			Principal wcmReviewer = new TestGroup("wcm-reviewer");
			Principal wcmEditor = new TestGroup("wcm-editor");
			Principal wcmAdmin = new TestGroup("wcm-admin");
			
			Session session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS);
			AccessControlManager acm = session.getAccessControlManager();
			 
			// Convert the privilege strings to Privilege instances ...
			Privilege[] readPermissions = new Privilege[readPrivileges.length];
			for (int i = 0; i < readPrivileges.length; i++) {
				readPermissions[i] = acm.privilegeFromName(readPrivileges[i]);
			}
			 
			Privilege[] editorPermissions = new Privilege[editorPrivileges.length];
			for (int i = 0; i < editorPrivileges.length; i++) {
				editorPermissions[i] = acm.privilegeFromName(editorPrivileges[i]);
			}
			
			Privilege[] adminPermissions = new Privilege[adminPrivileges.length];
			for (int i = 0; i < adminPrivileges.length; i++) {
				adminPermissions[i] = acm.privilegeFromName(adminPrivileges[i]);
			}
			
			AccessControlList acl = null;
			AccessControlPolicyIterator it = acm.getApplicablePolicies(path);
			
			if (it.hasNext()) {
			    acl = (AccessControlList)it.nextAccessControlPolicy();
			} else {
			    acl = (AccessControlList)acm.getPolicies(path)[0];
			}
	
			acl.addAccessControlEntry(anonymousPrincipal, readPermissions);
			acl.addAccessControlEntry(adminPrincipal, adminPermissions);
			acl.addAccessControlEntry(demoPrincipal, editorPermissions);
			acl.addAccessControlEntry(wcmViewer, readPermissions);
			acl.addAccessControlEntry(wcmReviewer, readPermissions);
			acl.addAccessControlEntry(wcmEditor, editorPermissions);
			acl.addAccessControlEntry(wcmAdmin, adminPermissions);
	//		 
			acm.setPolicy(path, acl);
			session.save();
	
			Session draftSession = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DRAFT_WS);
			AccessControlManager darftAcm = draftSession.getAccessControlManager();
			
			// Convert the privilege strings to Privilege instances ...
			Privilege[] draftReadPermissions = new Privilege[readPrivileges.length];
			for (int i = 0; i < readPrivileges.length; i++) {
				draftReadPermissions[i] = darftAcm.privilegeFromName(readPrivileges[i]);
			}
			 
			Privilege[] draftEditorPermissions = new Privilege[editorPrivileges.length];
			for (int i = 0; i < editorPrivileges.length; i++) {
				draftEditorPermissions[i] = darftAcm.privilegeFromName(editorPrivileges[i]);
			}
			
			Privilege[] draftAdminPermissions = new Privilege[adminPrivileges.length];
			for (int i = 0; i < adminPrivileges.length; i++) {
				draftAdminPermissions[i] = darftAcm.privilegeFromName(adminPrivileges[i]);
			}
			
			AccessControlList draftAcl = null;
			AccessControlPolicyIterator draftIt = darftAcm.getApplicablePolicies(path);
			
			if (draftIt.hasNext()) {
				 draftAcl = (AccessControlList)draftIt.nextAccessControlPolicy();
			} else {
				 draftAcl = (AccessControlList)darftAcm.getPolicies(path)[0];
			}
	
			 draftAcl.addAccessControlEntry(anonymousPrincipal, draftReadPermissions);
			 draftAcl.addAccessControlEntry(adminPrincipal, draftAdminPermissions);
			 draftAcl.addAccessControlEntry(demoPrincipal, draftEditorPermissions);
			 draftAcl.addAccessControlEntry(wcmViewer, draftReadPermissions);
			 draftAcl.addAccessControlEntry(wcmReviewer, draftReadPermissions);
			 draftAcl.addAccessControlEntry(wcmEditor, draftEditorPermissions);
			 draftAcl.addAccessControlEntry(wcmAdmin, draftAdminPermissions);
			
			darftAcm.setPolicy(path,  draftAcl);
			draftSession.save();
	
			//
			
			Session expiredSession = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.EXPIRED_WS);
			AccessControlManager expiredAcm = draftSession.getAccessControlManager();
			
			// Convert the privilege strings to Privilege instances ...
			Privilege[] expiredReadPermissions = new Privilege[readPrivileges.length];
			for (int i = 0; i < readPrivileges.length; i++) {
				expiredReadPermissions[i] = darftAcm.privilegeFromName(readPrivileges[i]);
			}
			 
			Privilege[] expiredEditorPermissions = new Privilege[editorPrivileges.length];
			for (int i = 0; i < editorPrivileges.length; i++) {
				expiredEditorPermissions[i] = darftAcm.privilegeFromName(editorPrivileges[i]);
			}
			
			Privilege[] expiredAdminPermissions = new Privilege[adminPrivileges.length];
			for (int i = 0; i < adminPrivileges.length; i++) {
				expiredAdminPermissions[i] = darftAcm.privilegeFromName(adminPrivileges[i]);
			}
			
			AccessControlList expiredAcl = null;
			AccessControlPolicyIterator expiredIt = darftAcm.getApplicablePolicies(path);
			
			if (expiredIt.hasNext()) {
				expiredAcl = (AccessControlList)expiredIt.nextAccessControlPolicy();
			} else {
				expiredAcl = (AccessControlList)expiredAcm.getPolicies(path)[0];
			}
	
			expiredAcl.addAccessControlEntry(anonymousPrincipal, expiredReadPermissions);
			expiredAcl.addAccessControlEntry(adminPrincipal, expiredAdminPermissions);
			expiredAcl.addAccessControlEntry(demoPrincipal, expiredEditorPermissions);
			expiredAcl.addAccessControlEntry(wcmViewer, expiredReadPermissions);
			expiredAcl.addAccessControlEntry(wcmReviewer, expiredReadPermissions);
			expiredAcl.addAccessControlEntry(wcmEditor, expiredEditorPermissions);
			expiredAcl.addAccessControlEntry(wcmAdmin, expiredAdminPermissions);
			
			expiredAcm.setPolicy(path,  expiredAcl);
			expiredSession.save();
			return "done";
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.createWcmError(t.getMessage(), WcmErrors.WCM_ERROR, null));
		}
	}
}
