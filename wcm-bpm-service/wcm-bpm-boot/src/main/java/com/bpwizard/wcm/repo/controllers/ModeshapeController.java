package com.bpwizard.wcm.repo.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(ModeshapeController.BASE_URI)
public class ModeshapeController {
	private static final Logger logger = LogManager.getLogger(ModeshapeController.class);
	public static final String BASE_URI = "/modeshape/server";
	@Autowired
	// private Repository repository;
	private RepositoryManager repositoryManager;
//	@Autowired
//	private Session session;

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
		String[] privileges = new String[] {
				Privilege.JCR_ALL 
//				Privilege.JCR_READ, 
//				Privilege.JCR_WRITE,
//				Privilege.JCR_ADD_CHILD_NODES, 
//				Privilege.JCR_MODIFY_ACCESS_CONTROL
		};
		Principal principal = new TestPrincipal();
		Principal adminPrincipal = new TestPrincipal("admin@example.com");  
		Principal group = new TestGroup();
		Principal wcmViewer = new TestGroup("wcm-viewer");
		Session session = this.repositoryManager.getSession("bpwizard");
		AccessControlManager acm = session.getAccessControlManager();
		 
		// Convert the privilege strings to Privilege instances ...
		Privilege[] permissions = new Privilege[privileges.length];
		for (int i = 0; i < privileges.length; i++) {
		    permissions[i] = acm.privilegeFromName(privileges[i]);
		}
		 
		AccessControlList acl = null;
		AccessControlPolicyIterator it = acm.getApplicablePolicies(path);
		
		if (it.hasNext()) {
		    acl = (AccessControlList)it.nextAccessControlPolicy();
		} else {
		    acl = (AccessControlList)acm.getPolicies(path)[0];
		}
		
		acl.addAccessControlEntry(principal, permissions);
		acl.addAccessControlEntry(adminPrincipal, permissions);
		acl.addAccessControlEntry(group, permissions);
		acl.addAccessControlEntry(wcmViewer, permissions);
//		 
		acm.setPolicy(path, acl);
		session.save();
		return "done";
	}
}
