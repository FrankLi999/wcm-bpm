package com.bpwizard.wcm.repo.rest.jcr.controllers;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ValidationRule;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ValidationRuleRestController.BASE_URI)
@Validated
public class ValidationRuleRestController extends BaseWcmRestController {
	
	public static final String BASE_URI = "/wcm/api/validationRule";
	private static final Logger logger = LogManager.getLogger(ValidationRuleRestController.class);
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidationRule getValidationRule(
			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String nodePath, 
			HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode validationRuleNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					nodePath, 2);
			ValidationRule validationRule = this.toValidationRule(validationRuleNode);
			validationRule.setRepository(repository);
			validationRule.setWorkspace(workspace);
			validationRule.setLibrary(nodePath.split("/", 5)[3]);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return validationRule;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createValidationRule(
			@RequestBody ValidationRule validationRule, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_VALIDATTOR_PATH_PATTERN, validationRule.getLibrary(), validationRule.getName());
			String repositoryName = validationRule.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, validationRule.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
			}
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

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveValidationRule(
			@RequestBody ValidationRule validationRule, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_VALIDATTOR_PATH_PATTERN, validationRule.getLibrary(), validationRule.getName());
			String repositoryName = validationRule.getRepository();
			JsonNode atJson = validationRule.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, DEFAULT_WS, path, atJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, atJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	private ValidationRule toValidationRule(RestNode node) {
		ValidationRule validationRule = new ValidationRule();
		validationRule.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				validationRule.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:description".equals(restProperty.getName())) {
				validationRule.setDescription(restProperty.getValues().get(0));
			} else if ("bpw:rule".equals(restProperty.getName())) {
				validationRule.setRule(restProperty.getValues().get(0));
			} else if ("bpw:type".equals(restProperty.getName())) {
				validationRule.setType(restProperty.getValues().get(0));
			}
		}
		return validationRule;
	}
}
