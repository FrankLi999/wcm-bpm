package com.bpwizard.wcm.repo.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;
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
import org.modeshape.jcr.api.query.Query;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.QueryStatement;
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
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, "default").getWorkspace().getQueryManager();
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
			throw new WcmRepositoryException(t);
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
			ObjectNode qJson = (ObjectNode) query.toJson();

			QueryManager qrm = this.repositoryManager.getSession(repositoryName, "default").getWorkspace().getQueryManager();
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
			RowIterator iterator = jcrResult.getRows();
			while (iterator.hasNext()) {
				Row row = iterator.nextRow();
				System.out.println("==================================================");
				System.out.println("element.jcr:path" + row.getValue("element.jcr:path"));
				System.out.println("element.jcr:name" + row.getValue("element.jcr:name"));
				System.out.println("element.bpw:value" + row.getValue("element.bpw:value"));
				System.out.println("content.jcr:name" + row.getValue("content.jcr:name"));
				System.out.println("content.jcr:path" + row.getValue("content.jcr:path"));
				System.out.println("content.jcr:score" + row.getValue("content.jcr:score"));
				System.out.println("element.jcr:score" + row.getValue("element.jcr:score"));
				System.out.println("==================================================");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
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
			
			// javax.jcr.query.qom.QueryObjectModel qom = null
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/query/%s", query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, "default", path, qJson);
//			if (this.authoringEnabled) {
//				Session session = this.repositoryManager.getSession(repositoryName, "draft");
//				session.getWorkspace().clone("default", path, path, true);
//			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
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
			String path = String.format("/bpwizard/library/%s/query/%s", query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, "default", path, qJson);

			Session session = this.repositoryManager.getSession(repositoryName, "draft");
			session.getWorkspace().clone("default", path, path, true);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	@GetMapping("/ping")
	public String modeShape() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession("bpwizard");
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
		} catch (Exception e) {
	        e.printStackTrace();
        } 
//		finally {
//        	if (session != null) {
//                session.logout();
//        	}
//        }
		return "{\"modeshape\": \"up\"}";
	}
	
	@GetMapping("/export/{folder}")
	public String exportContent(@PathVariable("folder") String folder) {
		Session session = null;
		try {
			session = this.repositoryManager.getSession("bpwizard");
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
            String absPath = "root".equals(folder) ? "/" : ("/" + folder);
            OutputStream os = new FileOutputStream("c:\\temp\\sample_content.xml");
            session.exportSystemView(absPath, os, false, false);
            os.close();
		} catch (Exception e) {
	        e.printStackTrace();
        } 
//		finally {
//        	if (session != null) {
//                session.logout();
//        	}
//        }
		return "{\"export\": \"done\"}";
	}
	
	@GetMapping("/import")
	public String importContent() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession("bpwizard");
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
		} catch (Exception e) {
	        e.printStackTrace();
        } 
//		finally {
//        	if (session != null) {
//                session.logout();
//        	}
//        }
		return "{\"import\": \"done\"}";
	}
	
	@GetMapping("/node-types")
	public String getNodeTypes() {
		Session session = null;
		try {
			session = this.repositoryManager.getSession("bpwizard");
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
            
		} catch (Exception e) {
	        e.printStackTrace();
        } 
//		finally {
//        	if (session != null) {
//                session.logout();
//        	}
//        }
		return "{\"node-type\": \"up\"}";
	}
	
	@GetMapping("/features")
	public String features() throws Exception {
		
		Repository repo = this.repositoryManager.getRepository("bpwizard");
		StringBuilder support = new StringBuilder("Descriptors").append(" : ");
		for (String desc: repo.getDescriptorKeys()) {
			support.append(desc).append("---:---").append(repo.getDescriptor(desc)).append("<br /><br />");
		}
		return support.toString();
		
		
	}
	@GetMapping("/testACL")
	public String testACL() throws Exception {
		String path = "/bpwizard/library";
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
		
		Session session = this.repositoryManager.getSession("bpwizard", "default");
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

		Session draftSession = this.repositoryManager.getSession("bpwizard", "draft");
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
		
		Session expiredSession = this.repositoryManager.getSession("bpwizard", "expired");
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
	}
}
