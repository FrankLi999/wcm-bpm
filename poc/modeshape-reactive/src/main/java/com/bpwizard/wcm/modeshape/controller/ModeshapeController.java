package com.bpwizard.wcm.modeshape.controller;

import java.io.ByteArrayInputStream;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class ModeshapeController {
	private static final Logger logger = LogManager.getLogger(ModeshapeController.class);
//	@Autowired
//	private Repository repository;
	
	@Autowired
	private Session session;
	
	@RequestMapping("/api/modeshape")
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<String> modeShape() {
		try {
			Random rnd = new Random();
			// Get the root node ...
            Node root = session.getRootNode();
            assert root != null;

            logger.debug("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");

            Node n = root.addNode("Node" + rnd.nextInt());

            n.setProperty("key", "value");
            n.setProperty("content", session.getValueFactory().createBinary(new ByteArrayInputStream(new byte[1000])));

            session.save();
            logger.debug("Added one node under root");
            logger.debug("+ Root childs");

            NodeIterator it = root.getNodes();
            while (it.hasNext()) {
            	logger.debug("+---> " + it.nextNode().getName());
            }
		} catch (Exception e) {
	        e.printStackTrace();
        } finally {
            this.session.logout();           
        }
		return Mono.just("Hello modeshape");
	}
}
