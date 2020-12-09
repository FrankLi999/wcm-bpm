package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ValidationRule;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.bpwizard.wcm.repo.validation.ValidateString;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ValidationRuleRestController.BASE_URI)
@Validated
public class ValidationRuleRestController extends BaseWcmRestController {
	
	public static final String BASE_URI = "/wcm/api/validationRule";
	private static final Logger logger = LoggerFactory.getLogger(ValidationRuleRestController.class);
	

	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@GetMapping(path = "/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ValidationRule[]> loadValidationRules(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam(name="filter", defaultValue = "") String filter,
		    @RequestParam(name="sort", defaultValue = "asc") 
			@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
			String sortDirection,
		    @RequestParam(name="pageIndex", defaultValue = "0") int pageIndex,
		    @RequestParam(name="pageSize", defaultValue = "3") @Min(3) @Max(10) int pageSize,
			HttpServletRequest request) 
				throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}

		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			
			ValidationRule[] validationRules = this.getValidationRuleLibraries(repository, workspace, baseUrl)
					.flatMap(library -> this.doGetValidationRule(library, baseUrl))
					.toArray(ValidationRule[]::new);
			if ("asc".equals(sortDirection)) {
				Arrays.sort(validationRules);
			} else if ("desc".equals(sortDirection)) {
				Arrays.sort(validationRules, Collections.reverseOrder());
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.OK).body(validationRules);
		} catch (WcmRepositoryException e) {
			logger.error("Failed to load validation rule", e);
			throw e;			
		} catch (Throwable t) {
			logger.error("Failed to load validation rule", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}
	
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createValidationRule(
			@RequestBody ValidationRule validationRule, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = String.format(WcmConstants.NODE_VALIDATTOR_PATH_PATTERN, validationRule.getLibrary(), validationRule.getName());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			
			String repositoryName = validationRule.getRepository();
			this.wcmItemHandler.addItem(WcmEvent.WcmItemType.validationRule, baseUrl,  repositoryName, WcmConstants.DEFAULT_WS, absPath, validationRule.toJson());
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			logger.error("Failed to create validation rule", e);
			throw e;
		} catch (RepositoryException re) {
			logger.error("Failed to create validation rule", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.CREATE_VALIDATION_RULE_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error("Failed to create validation rule", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	}

	@PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveValidationRule(
			@RequestBody ValidationRule validationRule, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		String absPath = String.format(WcmConstants.NODE_VALIDATTOR_PATH_PATTERN, validationRule.getLibrary(), validationRule.getName());
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = validationRule.getRepository();
				
			JsonNode atJson = validationRule.toJson();
			this.wcmItemHandler.updateItem(WcmEvent.WcmItemType.validationRule, baseUrl, repositoryName, WcmConstants.DEFAULT_WS, absPath, atJson);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			logger.error("Failed to save validation rule", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.UPDATE_VALIDATION_RULE_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error("Failed to save validation rule", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	protected Stream<ValidationRule> doGetValidationRule(ValidationRule rule, String baseUrl)
			throws WcmRepositoryException {
		String absPath = String.format(WcmConstants.NODE_VALIDATION_RULE_ROOT_PATH_PATTERN, rule.getLibrary());
		try {
			RestNode atNode = (RestNode) this.wcmItemHandler.item(baseUrl, rule.getRepository(), rule.getWorkspace(),
					absPath, WcmConstants.VALIDATION_RULE_DEPTH);
			
			return atNode.getChildren().stream().filter(this.wcmRequestHandler::isValidationRule)
					.map(node -> this.toValidationRule(node, rule.getRepository(), rule.getWorkspace(), rule.getLibrary()));
		} catch (RepositoryException re) {;
			logger.error("Failed to get validation rule", re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.GET_VALIDATION_RULE_ERROR, new String[] {absPath}));
		}
	}
	
	protected Stream<ValidationRule> getValidationRuleLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace,
					WcmConstants.NODE_ROOT_PATH, WcmConstants.READ_DEPTH_DEFAULT);
			return bpwizardNode.getChildren().stream()
					.filter(this.wcmRequestHandler::notSystemLibrary)
					.map(node -> toValidationRuleWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e, new WcmError(e.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		}
	}
	
	protected ValidationRule toValidationRuleWithLibrary(RestNode node, String repository, String workspace) {
		ValidationRule validationRuleWithLibrary = new ValidationRule();
		validationRuleWithLibrary.setRepository(repository);
		validationRuleWithLibrary.setWorkspace(workspace);
		validationRuleWithLibrary.setLibrary(node.getName());
		return validationRuleWithLibrary;
	}
	

	private ValidationRule toValidationRule(RestNode node, String repository, String workspace, String library) {
		ValidationRule validationRule = new ValidationRule();
		validationRule.setWcmAuthority(WcmUtils.getWcmAuthority(null));
		validationRule.setRepository(repository);
		validationRule.setWorkspace(workspace);
		validationRule.setLibrary(library);
		
		validationRule.setName(node.getName());
		for (RestNode childNode : node.getChildren()) {
			if (WcmConstants.WCM_ITEM_ELEMENTS.equals(childNode.getName())) {
				for (RestProperty restProperty : childNode.getJcrProperties()) {
					if ("rule".equals(restProperty.getName())) {
						validationRule.setRule(restProperty.getValues().get(0));
					} else if ("type".equals(restProperty.getName())) {
						validationRule.setType(restProperty.getValues().get(0));
					}
				}
			} else if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())) {
				for (RestProperty restProperty : childNode.getJcrProperties()) {
					if ("bpw:title".equals(restProperty.getName())) {
						validationRule.setTitle(restProperty.getValues().get(0));
					} else if ("bpw:description".equals(restProperty.getName())) {
						validationRule.setDescription(restProperty.getValues().get(0));
					}
				}
			} 
		}
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
	
	@DeleteMapping("/{repository}/{workspace}")
  	public ResponseEntity<?> purgeValidationRule(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String wcmPath,
  			HttpServletRequest request) { 
  		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
  		String baseUrl = RestHelper.repositoryUrl(request);
  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
  		try {
	  		
  			this.wcmItemHandler.deleteItem(WcmEvent.WcmItemType.validationRule, baseUrl, repository, workspace, absPath);
  			
  	  		if (logger.isDebugEnabled()) {
  				logger.debug("Exit");
  			}
  	  		
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
			throw e;
	    } catch (Throwable t) {
	    	logger.error("Failed to purge validation rule", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}

  	};
}
