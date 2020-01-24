package com.bpwizard.wcm.repo.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.qom.Column;
import javax.jcr.query.qom.Constraint;
import javax.jcr.query.qom.Ordering;
import javax.jcr.query.qom.QueryObjectModel;
import javax.jcr.query.qom.QueryObjectModelFactory;
import javax.jcr.query.qom.Selector;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.jcr.JcrRepository;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ModeshapeController.BASE_URI)
public class ModeshapeController {
	private static final Logger logger = LogManager.getLogger(ModeshapeController.class);
	public static final String BASE_URI = "/modeshape/server";
	@Autowired
	private RepositoryManager repositoryManager;

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
	
	@GetMapping("/sql2/{repository}")
	public String sql2(
			@PathVariable("repository") String repository,
			@RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			QueryManager qm = fromSession.getWorkspace().getQueryManager();
			String statement = "SELECT * FROM [bpw:content] AS contents WHERE ISDESCENDANTNODE(contents,'/bpwizard/library/camunda/rootSiteArea/home')";
			Query query = qm.createQuery(statement, JcrRepository.QueryLanguage.JCR_SQL2);
		    QueryResult qr = query.execute();
		    System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr: " + qr);
		    System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr row: " + qr.getRows());
			String statement1 = "SELECT * FROM [bpw:content] AS contents WHERE PATH() LIKE '/bpwizard/library/camunda/rootSiteArea/home/%'";
			Query query1 = qm.createQuery(statement1, JcrRepository.QueryLanguage.JCR_SQL2);
			QueryResult qr1 = query1.execute();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1: " + qr1);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 row: " + qr1.getRows());
			javax.jcr.query.RowIterator rowIter = qr.getRows();
			String[] columnNames = qr.getColumnNames();
			while ( rowIter.hasNext() ) {
			    javax.jcr.query.Row row = rowIter.nextRow();
			    // Iterate over the column values in each row ...
			    javax.jcr.Value[] values = row.getValues();
			    for ( javax.jcr.Value value : values ) {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr v: " + value);
			    }
			    // Or access the column values by name ...
			    for ( String columnName : columnNames ) {
			        javax.jcr.Value value = row.getValue(columnName);
			        System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr column v: " + columnName + ":" + value);
			    }
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 row: " + qr1.getRows());
			javax.jcr.query.RowIterator rowIter1 = qr1.getRows();
			String[] columnNames1 = qr.getColumnNames();
			while ( rowIter1.hasNext() ) {
			    javax.jcr.query.Row row = rowIter1.nextRow();
			    // Iterate over the column values in each row ...
			    javax.jcr.Value[] values = row.getValues();
			    for ( javax.jcr.Value value : values ) {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 v: " + value);
			    }
			    // Or access the column values by name ...
			    for ( String columnName : columnNames1 ) {
			        javax.jcr.Value value = row.getValue(columnName);
			        System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 column v: " + columnName + ":" + value);
			    }
			}
			

			
			String statement2 = "SELECT * FROM [bpw:contentElement]  AS elements WHERE CONTAINS(elements.[bpw:value],'*Content*')";
			Query query2 = qm.createQuery(statement2, JcrRepository.QueryLanguage.JCR_SQL2);
			QueryResult qr2 = query2.execute();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr2: " + qr2);
			javax.jcr.query.RowIterator rowIter2 = qr2.getRows();
			String[] columnNames2 = qr.getColumnNames();
			while ( rowIter2.hasNext() ) {
			    javax.jcr.query.Row row = rowIter2.nextRow();
			    // Iterate over the column values in each row ...
			    javax.jcr.Value[] values = row.getValues();
			    for ( javax.jcr.Value value : values ) {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 v: " + value);
			    }
			    // Or access the column values by name ...
			    for ( String columnName : columnNames2 ) {
			        javax.jcr.Value value = row.getValue(columnName);
			        System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 column v: " + columnName + ":" + value);
			    }
			}
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@GetMapping("/search/{repository}")
	public String search(
			@PathVariable("repository") String repository,
			@RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			QueryManager qm = fromSession.getWorkspace().getQueryManager();
			
//			String statement2 = "SELECT * FROM [bpw:content] WHERE CONTAINS([bpw:content],'*Content*')";
//			Query query2 = qm.createQuery(statement2, JcrRepository.QueryLanguage.SEARCH);

//			
			Query query2 = qm.createQuery("*Content*", JcrRepository.QueryLanguage.SEARCH);
			QueryResult qr2 = query2.execute();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr2: " + qr2);
			javax.jcr.query.RowIterator rowIter2 = qr2.getRows();
			String[] columnNames2 = qr2.getColumnNames();
			while ( rowIter2.hasNext() ) {
			    javax.jcr.query.Row row = rowIter2.nextRow();
			    // Iterate over the column values in each row ...
			    javax.jcr.Value[] values = row.getValues();
			    for ( javax.jcr.Value value : values ) {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 v: " + value);
			    }
			    // Or access the column values by name ...
			    for ( String columnName : columnNames2 ) {
			        javax.jcr.Value value = row.getValue(columnName);
			        System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr1 column v: " + columnName + ":" + value);
			    }
			}
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
//    String ftsExpression = "myfile*"; 
//    Query ftsQuery = qm.createQuery(ftsExpression, "Search");
//
//    or, if you do want to use JCR-SQL2, it should be this:
//
//    String ftsExpression = "SELECT [jcr:primaryType] FROM [nt:base] WHERE CONTAINS( [nt:base].*, 'myfile*' )"; 
//    Query ftsQuery = qm.createQuery(ftsExpression, Query.JCR_SQL2);
    
	@GetMapping("/joqm/{repository}")
	public String jqom(
			@PathVariable("repository") String repository,
			@RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			
			QueryManager qm = fromSession.getWorkspace().getQueryManager();
			QueryObjectModelFactory factory = qm.getQOMFactory();
			Selector source = factory.selector("bpw:content","contents");
			// Create the SELECT clause (we want all columns defined on the node type) ...
			Column[] columns = null;
			// Create the WHERE clause (we have none for this query) ...
			
			Constraint constraint = factory.descendantNode(source.getSelectorName(), "/bpwizard/library/camunda/rootSiteArea/home");
			// Define the orderings (we have none for this query)...
			Ordering[] orderings = null;
			QueryObjectModel query = factory.createQuery(source, constraint, orderings, columns);
			QueryResult qr = query.execute();
		    System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr: " + qr);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr row: " + qr.getRows());
			javax.jcr.query.RowIterator rowIter = qr.getRows();
			String[] columnNames = qr.getColumnNames();
			while ( rowIter.hasNext() ) {
			    javax.jcr.query.Row row = rowIter.nextRow();
			    // Iterate over the column values in each row ...
			    javax.jcr.Value[] values = row.getValues();
			    for ( javax.jcr.Value value : values ) {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr v: " + value);
			    }
			    // Or access the column values by name ...
			    for ( String columnName : columnNames ) {
			        javax.jcr.Value value = row.getValue(columnName);
			        System.out.println(">>>>>>>>>>>>>>>>>>>>>> qr column v: " + columnName + ":" + value);
			    }
			}
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@GetMapping("/reference/{repository}")
	public String checkReference(
			@PathVariable("repository") String repository,
			@RequestParam(name="path", defaultValue="/reference-test") String path,
			@RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			Node node = fromSession.getNode(path);
			
			Property property = node.getProperty("bpw:reference");
			System.out.println(">>>>>>>>>>>>>>>>>> reference property:" + property);
			System.out.println(">>>>>>>>>>>>>>>>>> reference property class:" + property.getClass());
			Node referenceNode = fromSession.getNodeByIdentifier(property.getString());
			System.out.println(">>>>>>>>>>>>>>>>>> referenceNode:" + referenceNode);
			System.out.println(">>>>>>>>>>>>>>>>>> referenceNode path:" + referenceNode.getPath());
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@PostMapping("/reference/{repository}")
	public String reference(
			@PathVariable("repository") String repository,
			@RequestParam(name="path", defaultValue="/bpwizard/library/camunda/rootSiteArea/tasklist") String path,
			@RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			Node taskListNode = fromSession.getNode(path);
		
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>.... task list node id:" + taskListNode.getIdentifier());
			String uuid = taskListNode.getProperty("jcr:uuid").getString();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>.... task list uuid: " + taskListNode.getIdentifier());
			Node root = fromSession.getRootNode();
			Node node = root.addNode("reference-test");
			node.setPrimaryType("bpw:rereference");
			node.setProperty("bpw:name", "reference-a");
			node.setProperty("bpw:reference", uuid);
			fromSession.save();
			OutputStream fromOs = new FileOutputStream("c:\\temp\\reference_content.xml");
            fromSession.exportSystemView("/reference-test", fromOs, false, false);
            fromOs.close();
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@PostMapping("/share/{repository}")
	public String share(@PathVariable("repository") String repository,
			 @RequestParam(name="path", defaultValue="/bpwizard/library/camunda/rootSiteArea/home/MySidePaneContent2") final String srcPath,
 			 @RequestParam(name="path", defaultValue="/draft/library/camunda/rootSiteArea/home/MySidePaneContent2") final String destPath,
			 @RequestParam(name="from", defaultValue="default") final String from) {
		
		try {
			Session fromSession = this.repositoryManager.getSession(repository, from);
			Node root = fromSession.getRootNode();
			try {
				Node node = root.addNode("draft");
				node = node.addNode("library");
				node = node.addNode("camunda");
				node = node.addNode("rootSiteArea");
				node.addNode("home");
				fromSession.save();		        
			} catch (Exception e) {		
				e.printStackTrace();
			}
			try {
				Workspace fromWorkspace = fromSession.getWorkspace();
		        fromWorkspace.clone(from, srcPath, destPath, false);
			} catch (Exception e) {	
				e.printStackTrace();
			}
			
	        OutputStream fromOs = new FileOutputStream("c:\\temp\\shared_content.xml");
            fromSession.exportSystemView(destPath, fromOs, false, false);
            fromOs.close();
            
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@PostMapping("/clone/{repository}")
	public String clone(@PathVariable("repository") String repository,
			 @RequestParam(name="path", defaultValue="/") final String path,
			 @RequestParam(name="from", defaultValue="default") final String from,
			 @RequestParam(name="to", defaultValue="draft") final String to) {
		
		try {
			Session toSession = this.repositoryManager.getSession(repository, to);
			Workspace toWorkspace = toSession.getWorkspace();
			toWorkspace.clone(from, path, path, true);
			OutputStream toOs = new FileOutputStream("c:\\temp\\to_ws_content.xml");
			toSession.exportSystemView(path, toOs, false, false);
            toOs.close();
            
            Session fromSession = this.repositoryManager.getSession(repository, from);
            OutputStream fromOs = new FileOutputStream("c:\\temp\\from_ws_content.xml");
            fromSession.exportSystemView(path, fromOs, false, false);
            fromOs.close();
		} catch (Exception e) {
	        e.printStackTrace();
        } 
		return "done";
	}
	
	@GetMapping("/export/{folder}")
	public String exportContent(
			@PathVariable("folder") String folder) {
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
		Session session = this.repositoryManager.getSession("bpwizard");
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
		return "done";
	}
}
