package com.bpwizard.wcm.repo.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
//@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(ModeshapeController.BASE_URI)
@Api(tags = {"jcr"})
public class ModeshapeController {
	private static final Logger logger = LogManager.getLogger(ModeshapeController.class);
	public static final String BASE_URI = "/api/modeshape";
	@Autowired
	private Repository repository;
	
//	@Autowired
//	private Session session;

	@GetMapping("/ping")
	@ApiOperation(value = "Bootstrap modeshape", response = String.class)
	public String modeShape() {
		Session session = null;
		try {
			
			session = repository.login();
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
        } finally {
        	if (session != null) {
                session.logout();
        	}
        }
		return "{\"modeshape\": \"up\"}";
	}
	
	@GetMapping("/export/{folder}")
	@ApiOperation(value = "Bootstrap modeshape", response = String.class)
	public String exportContent(@PathVariable("folder") String folder) {
		Session session = null;
		try {
			
			session = repository.login();
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
        } finally {
        	if (session != null) {
                session.logout();
        	}
        }
		return "{\"export\": \"done\"}";
	}
	
	@GetMapping("/import")
	@ApiOperation(value = "Bootstrap modeshape", response = String.class)
	public String importContent() {
		Session session = null;
		try {
			
			session = repository.login();
			// Get the root node ...
            Node root = session.getRootNode();
            
            assert root != null;
            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            // Node n = root.addNode("Node" + rnd.nextInt());

            // n.setProperty("key", "value");
            // n.setProperty("content", session.getValueFactory().createBinary(new ByteArrayInputStream(new byte[1000])));

            // 
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("bpw_content.xml");
            System.out.println(">>>>>>> import from:" + in);
            session.importXML("/", in, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
            session.save();
            logger.debug("Added one node under root");
            logger.debug("+ Root childs");
		} catch (Exception e) {
	        e.printStackTrace();
        } finally {
        	if (session != null) {
                session.logout();
        	}
        }
		return "{\"import\": \"done\"}";
	}
	
	@GetMapping("/node-types")
	@ApiOperation(value = "Bootstrap modeshape", response = String.class)
	public String getNodeTypes() {
		Session session = null;
		try {
			
			session = repository.login();
			Random rnd = new Random();
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
        } finally {
        	if (session != null) {
                session.logout();
        	}
        }
		return "{\"node-type\": \"up\"}";
	}
	
	@GetMapping("/registerNodeTypes")
	@ApiOperation(value = "Load custom data types", response = String.class)
	public String loadCustomDataTypes() {
		Session session = null;
		try {
			session = repository.login();
			org.modeshape.jcr.api.nodetype.NodeTypeManager nodeTypeManager = (org.modeshape.jcr.api.nodetype.NodeTypeManager)
				session.getWorkspace().getNodeTypeManager();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 1");
			InputStream myCndStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bpw.cnd");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 2: " + myCndStream);
			nodeTypeManager.registerNodeTypes(myCndStream,true);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 3 ");
		} catch (Exception e) {
	        e.printStackTrace();
        } finally {
        	if (session != null) {
        		try {
                session.logout();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        }
		return "{\"modeshape\": \"Register node types\"}";
	}
}
