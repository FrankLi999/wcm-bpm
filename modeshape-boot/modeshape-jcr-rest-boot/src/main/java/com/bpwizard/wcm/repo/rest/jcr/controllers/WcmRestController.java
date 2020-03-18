package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.jcr.api.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
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

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.BpmnWorkflow;
import com.bpwizard.wcm.repo.rest.jcr.model.Category;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlField;
import com.bpwizard.wcm.repo.rest.jcr.model.JsonForm;
import com.bpwizard.wcm.repo.rest.jcr.model.PageConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.QueryStatement;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.ValidationRule;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNavigatorFilter;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNode;
import com.bpwizard.wcm.repo.rest.jcr.model.WorkflowNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(WcmRestController.BASE_URI)
@Validated
public class WcmRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmRestController.class);
	
	public static final String BASE_URI = "/wcm/api";

	
	@GetMapping(path = "/at/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, AuthoringTemplate> getAuthoringTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, AuthoringTemplate> authoringTemplates = this.doGetAuthoringTemplates(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return authoringTemplates;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/authoringTemplate/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthoringTemplate getAuthoringTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String atPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String baseUrl = RestHelper.repositoryUrl(request);
		AuthoringTemplate at = this.wcmUtils.getAuthoringTemplate(repository, workspace, atPath, baseUrl);
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return at;
	}

	@PutMapping(path = "/authoringTemplate/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthoringTemplate lockAuthoringTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return at;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/jsonform/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, JsonForm[]> getAuthoringTemplateAsJsonForm(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, JsonForm[]> jsonForms = this.doGetAuthoringTemplateAsJsonForm(repository, workspace, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return jsonForms;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@GetMapping(path = "/control/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ControlField[] getControlField(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			ControlField[] ControlFileds = this.doGetControlField(repository, workspace, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ControlFileds;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	//http://localhost:8080/wcm/api/wcmSystem/bpwizard/default/camunda/bpm
	@GetMapping(path = "/pageConfig/{repository}/{workspace}/{library}/{siteConfig}", 
		produces = MediaType.APPLICATION_JSON_VALUE)
	public PageConfig getPageConfig(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@PathVariable("library") String library, 
			@PathVariable("siteConfig") String siteConfigName,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			PageConfig pageConfig = new PageConfig();
			SiteConfig siteConfig = this.getSiteConfig(
					request,
					repository,
					workspace,
					library,
					siteConfigName);
			pageConfig.setSiteConfig(siteConfig);			
			pageConfig.setNavigations(this.getNavigations(
				baseUrl, repository, workspace, library, siteConfig.getRootSiteArea()));
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return pageConfig;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}
		
	@PostMapping(path = "/siteConfig", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteConfig(
			@RequestBody SiteConfig siteConfig, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_SITECONFIG_PATH_PATTERN, siteConfig.getLibrary(), siteConfig.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName, 
					DEFAULT_WS, 
					path, 
					siteConfig.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, "path", true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PostMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = (StringUtils.hasText(category.getParent())) ? 
					String.format(WCM_CATEGORY_PATH_PATTERN + "/%s", category.getLibrary(), category.getParent(), category.getName()) :
					String.format(WCM_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName,
					category.getWorkspace(),
					path, 
					category.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}	
	
	@PutMapping(path = "/category", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCategory(
			@RequestBody Category category, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = category.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_CATEGORY_PATH_PATTERN, category.getLibrary(), category.getName());
			JsonNode categoryJson = category.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, category.getWorkspace(), path, categoryJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, categoryJson);
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}	
	
	
	
	@PutMapping(path = "/siteConfig", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteConfig(
			@RequestBody SiteConfig siteConfig, HttpServletRequest request) 
			throws WcmRepositoryException {
		try {
			String path = String.format(WCM_SITECONFIG_PATH_PATTERN, siteConfig.getLibrary(), siteConfig.getName());
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			JsonNode jsonItem = siteConfig.toJson();
			this.itemHandler.updateItem(
					baseUrl, 
					repositoryName, 
					DEFAULT_WS, 
					path, 
					jsonItem);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(
						baseUrl, 
						repositoryName, 
						DRAFT_WS, 
						path, 
						jsonItem);
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping("/siteConfig/get/{repository}/{workspace}/{library}/{siteConfig}")
	public SiteConfig getSiteConfig(
			HttpServletRequest request,
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@PathVariable("library") String library, 
			@PathVariable("siteConfig") String siteConfigName) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			SiteConfig siteConfig = this.doGetSiteConfig(request, repository, workspace, library, siteConfigName);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteConfig;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@PutMapping("/siteConfig/get/loclk/{workspace}/{library}/{siteConfig}")
	public SiteConfig lockSiteConfig(
			HttpServletRequest request,
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@PathVariable("library") String library, 
			@PathVariable("siteConfigName") String siteConfigName)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String absPath = String.format(WCM_SITECONFIG_PATH_PATTERN, library, siteConfigName);
			this.doLock(repository, workspace, absPath);
			SiteConfig siteConfig = this.getSiteConfig(request, repository, workspace, library, siteConfigName);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteConfig;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	 
	}
	
	@GetMapping(path = "/validationRule/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@PostMapping(path = "/validationRule", consumes = MediaType.APPLICATION_JSON_VALUE)
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

	@PutMapping(path = "/validationRule", consumes = MediaType.APPLICATION_JSON_VALUE)
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
	
	@GetMapping(path = "/bpmnWorkflow/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BpmnWorkflow getBpmnWorkflow(
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
			RestNode workflowNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					nodePath, 2);
			BpmnWorkflow bpmnWorkflow = this.toBpmnWorkflow(workflowNode);
			bpmnWorkflow.setRepository(repository);
			bpmnWorkflow.setWorkspace(workspace);
			bpmnWorkflow.setLibrary(nodePath.split("/", 5)[3]);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return bpmnWorkflow;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}
	
	@PostMapping(path = "/bpmnWorkflow", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBpmnWorkflow(
			@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
			String repositoryName = bpmnWorkflow.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, bpmnWorkflow.toJson());
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
	
	@PutMapping(path = "/bpmnWorkflow", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveBpmnWorkflow(@RequestBody BpmnWorkflow bpmnWorkflow, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_WORKFLOW_PATH_PATTERN, bpmnWorkflow.getLibrary(), bpmnWorkflow.getName());
			String repositoryName = bpmnWorkflow.getRepository();
			JsonNode atJson = bpmnWorkflow.toJson();
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
	
	@GetMapping(path = "/queryStatement/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public QueryStatement getQueryStatement(
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
			RestNode queryNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					nodePath, 2);
			QueryStatement queryStatement = this.toQueryStatement(queryNode);
			queryStatement.setRepository(repository);
			queryStatement.setWorkspace(workspace);
			queryStatement.setLibrary(nodePath.split("/", 5)[3]);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return queryStatement;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}

	@PostMapping(path = "/queryStatement", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createQueryStatement(
			@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, DEFAULT_WS).getWorkspace().getQueryManager();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			// javax.jcr.query.qom.QueryObjectModel qom = null
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, qJson);
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

	@PutMapping(path = "/queryStatement", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveQuery(@RequestBody QueryStatement query, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_QUERY_PATH_PATTERN, query.getLibrary(), query.getName());
			String repositoryName = query.getRepository();
			ObjectNode qJson = (ObjectNode) query.toJson();
			try {
				QueryManager qrm = this.repositoryManager.getSession(repositoryName, DEFAULT_WS).getWorkspace().getQueryManager();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.itemHandler.updateItem(baseUrl, repositoryName, DEFAULT_WS, path, qJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, qJson);
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
	
	@PostMapping(path = "/at", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_AT_PATH_PATTERN, at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, DEFAULT_WS, path, at.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
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

	@PutMapping(path = "/at", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_AT_PATH_PATTERN, at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			JsonNode atJson = at.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, DEFAULT_WS, path, atJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, atJson);
//				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
//				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/rt/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, RenderTemplate> getRenderTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, RenderTemplate> renderTemplates = this.doGetRenderTemplates(repository, workspace, request);
	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return renderTemplates;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@GetMapping(path = "/renderTemplate/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RenderTemplate getRenderTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String rtPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			rtPath = rtPath.startsWith("/") ? rtPath : "/" + rtPath;
			String baseUrl = RestHelper.repositoryUrl(request);
			String library = rtPath.split("/", 5)[3];
			RestNode rtNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					rtPath, 4);
			
			RenderTemplate rt = this.toRenderTemplate(rtNode, repository, workspace, library);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return rt;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/renderTemplate/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RenderTemplate lockRenderTemplate(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			RenderTemplate renderTemplate = this.getRenderTemplate(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return renderTemplate;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@PostMapping(path = "/rt", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createRenderTemplate(
			@RequestBody RenderTemplate rt, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = rt.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_RT_PATH_PATTERN, rt.getLibrary(), rt.getName());
			this.itemHandler.addItem(baseUrl, repositoryName, DEFAULT_WS, path, rt.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@PutMapping(path = "/rt", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveRenderTemplate(
			@RequestBody RenderTemplate rt, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String repositoryName = rt.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format(WCM_RT_PATH_PATTERN, rt.getLibrary(), rt.getName());
			JsonNode rtJson = rt.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, rt.getWorkspace(), path, rtJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, rt.getWorkspace(), path, rtJson);
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/contentAreaLayout/list/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ContentAreaLayout> getContentAreaLayouts(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, ContentAreaLayout> contentAreaLayouts = this.doGetContentAreaLayouts(repository, workspace, request);	
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentAreaLayouts;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@GetMapping(path = "/contentAreaLayout/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentAreaLayout getContentAreaLayout(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			// "/bpwizard/library/" + contentAreaLayout.getLibrary() + "/contentAreaLayout
			RestNode contentAreaLayoutNode = (RestNode) this.itemHandler.item(baseUrl, repository,
					workspace, absPath, 4);
			ContentAreaLayout layout = new ContentAreaLayout();
			layout.setRepository(repository);
			layout.setWorkspace(workspace);
			String library = absPath.split("/")[2];
			layout.setLibrary(library);
			this.toContentAreaLayout(contentAreaLayoutNode, layout);
			return layout;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
		
	}
	
	@PutMapping(path = "/contentAreaLayout/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentAreaLayout lockContentAreaLayout(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			ContentAreaLayout contentAreaLayout = this.getContentAreaLayout(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentAreaLayout;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PostMapping(path = "/contentAreaLayout", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {

			String repositoryName = pageLayout.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format( WCM_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
			this.itemHandler.addItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, pageLayout.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PutMapping(path = "/contentAreaLayout", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveContentAreaLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format( WCM_CONTENT_LAYOUT_PATH_PATTERN, pageLayout.getLibrary(), pageLayout.getName());
			String repositoryName = pageLayout.getRepository();
			JsonNode layoutJson = pageLayout.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, layoutJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, layoutJson);
//				
//				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
//				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
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

	// @PostMapping(path = "/sitearea/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/sitearea", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(
			@RequestBody SiteArea sa, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String path = String.format("%s/%s", sa.getNodePath(), sa.getName());
			String repositoryName = sa.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, repositoryName, sa.getWorkspace(), path, sa.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
	
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PutMapping(path = "/sitearea", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteArea(
			@RequestBody SiteArea sa, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String path = sa.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = sa.getRepository();
			JsonNode saJson = sa.toJson();
			this.itemHandler.updateItem(baseUrl, repositoryName, sa.getWorkspace(), path, saJson);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, repositoryName, DRAFT_WS, path, saJson);
				
//				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
//				session.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// session.save();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@GetMapping(path = "/siteArea/get/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea getSiteArea(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String saPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			saPath = saPath.startsWith("/") ? saPath : "/" + saPath;
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					saPath, 4);
			
			SiteArea sa = new SiteArea(); 
			sa.setRepository(repository);
			sa.setWorkspace(workspace);
			sa.setNodePath(saPath);
			this.loadSiteArea(saNode, sa);
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return sa;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PutMapping(path = "/siteArea/lock/{repository}/{workspace}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public SiteArea lockSiteArea(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String absPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			SiteArea siteArea = this.getSiteArea(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return siteArea;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PostMapping(path = "/contentItem/save-drfat", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String path = contentItem.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), DRAFT_WS, path, contentItem.toJson());
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
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
	
	@PostMapping(path = "/contentItem/create-publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAndPublishContentItem(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			AuthoringTemplate at = this.getAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			String path = String.format("%s/%s", contentItem.getNodePath(), contentItem.getName());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), DEFAULT_WS, path, contentItem.toJson());
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
			}
			if (authoringEnabled) {
				Session draftSession = this.repositoryManager.getSession(contentItem.getRepository(), DRAFT_WS);
				draftSession.getWorkspace().clone(DEFAULT_WS, path, path, true);
				// draftSession.save();
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
	
	@PostMapping(path = "/contentItem/edit-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			AuthoringTemplate at = this.getAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			String path = contentItem.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItem.toJson());

			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
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
	
	@PostMapping(path = "/contentItem/cancel-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@PathVariable("repository") String repository,
			@RequestParam("path") String contentItemPath,
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			Session session = this.repositoryManager.getSession(repository, DEFAULT_WS);
			session.getWorkspace().clone(DRAFT_WS, contentItemPath, contentItemPath, true);
			// session.save();
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
	
	@PutMapping(path = "/contentItem/update-published", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateContentItem(@RequestBody ContentItem contentItem, HttpServletRequest request) { 
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			String path = contentItem.getNodePath();
			AuthoringTemplate at = this.getAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			String baseUrl = RestHelper.repositoryUrl(request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			JsonNode contentItemJson = contentItem.toJson();
			this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItemJson);
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
			}
			this.wcmUtils.unlock(contentItem.getRepository(), contentItem.getWorkspace(), path);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), DRAFT_WS, path, contentItemJson);
//				Session draftSession = this.repositoryManager.getSession(contentItem.getRepository(), );
//				draftSession.getWorkspace().clone(DEFAULT_WS, contentItem.getNodePath(), contentItem.getNodePath(), true);
				// draftSession.save();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/contentItem/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem getContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String contentItemPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			contentItemPath = contentItemPath.startsWith("/") ? contentItemPath : "/" + contentItemPath;
			RestNode contentItemNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					contentItemPath, 4);
			
			ContentItem contentItem = new ContentItem(); 
			contentItem.setRepository(repository);
			contentItem.setWorkspace(workspace);
			contentItem.setNodePath(contentItemPath);
			Map<String, String> elements = new HashMap<>();
			Map<String, String> properties = new HashMap<>();
			contentItem.setElements(elements);
			properties.put("name", contentItemNode.getName());
			contentItem.setProperties(properties);
	
			this.resolveWorkflowNode(contentItem, contentItemNode, properties);
			for (RestProperty property: contentItemNode.getJcrProperties()) {
				if ("bpw:authoringTemplate".equals(property.getName())) {
					contentItem.setAuthoringTemplate(property.getValues().get(0));
				} else if ("bpw:categories".equals(property.getName())) {
					contentItem.setCategories(property.getValues().toArray(new String[property.getValues().size()]));
					properties.put("categories", String.join(",", property.getValues()));
				} 
			}
			for (RestNode node: contentItemNode.getChildren()) {
				if (this.wcmUtils.checkNodeType(node, "bpw:contentElementFolder")) {
					for (RestNode enode: node.getChildren()) {
						if (this.wcmUtils.checkNodeType(enode, "bpw:contentElement")) {
							for (RestProperty property: enode.getJcrProperties()) {
								if ("bpw:value".equals(property.getName())) {
									elements.put(enode.getName(), property.getValues().get(0));
									break;
								} 
							}
						}
					}
				} else if (this.wcmUtils.checkNodeType(node, "bpw:propertyElementFolder")) {
					for (RestNode pnode: node.getChildren()) {
						if (this.wcmUtils.checkNodeType(pnode, "bpw:contentElement")) {
							for (RestProperty property: pnode.getJcrProperties()) {
								if ("bpw:value".equals(property.getName())) {
									properties.put(pnode.getName(), property.getValues().get(0));
									break;
								} 
							}
						}
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentItem;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PostMapping(path = "/contentItem/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> creatContentItemAsDraft(			
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			contentItem.setCurrentLifecycleState(DRAFT_WS); //TODO
			String path = contentItem.getNodePath();
			AuthoringTemplate at = this.getAuthoringTemplate(contentItem.getRepository(), DRAFT_WS, 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnSaveDraftPermissions());
			
			// this.itemHandler.addItem(request, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItem.toJson());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), DRAFT_WS, path, contentItem.toJson());
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), at.getContentItemAcl().getOnSaveDraftPermissions());
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
	
	@PutMapping(path = "/contentItem/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			
			UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = principal.getUsername();
	        Session session = this.repositoryManager.getSession(repository, DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-reject-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
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
	
	@PutMapping(path = "/contentItem/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {			
			UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = principal.getUsername();
	        Session session = this.repositoryManager.getSession(repository, DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-approval-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
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
	
	@PutMapping(path = "/contentItem/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> publishContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
	        Session session = this.repositoryManager.getSession(repository, DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        contentNode.setProperty("bpw:currentLifecycleState", "Published");
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnPublishPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        Session defaultSession = this.repositoryManager.getSession(repository, DEFAULT_WS);
	        defaultSession.getWorkspace().clone(DRAFT_WS, contentItemPath, contentItemPath, true);
	        // defaultSession.save();
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
	
	@PutMapping(path = "/contentItem/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem lockContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String absPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			ContentItem contentItem = this.getContentItem(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentItem;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PutMapping("/wcmItem/unlock/{repository}/{workspace}")
    public void unlock(
			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath) { 
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.wcmUtils.unlock(repository, workspace, absPath);
    	} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
	};

    @PutMapping("/WcmItem/restore/{repository}/{workspace}")
  	public void restore(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath, 
  			@RequestParam("version") String version) {
    	if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
    	try {
	    	javax.jcr.Workspace ws = this.repositoryManager.getSession(repository, workspace).getWorkspace();
			javax.jcr.version.VersionManager vm = ws.getVersionManager();
			
		    vm.restore(absPath, version, true);
			javax.jcr.lock.LockManager lm = ws.getLockManager();
			if (lm.isLocked(absPath)) {
				lm.unlock(absPath);
			} 
    	} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
    	if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
  	}

    @PutMapping("/contentItem/expire/{repository}/{workspace}")
  	public void expireContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
    	if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		try {
  			Session session = this.repositoryManager.getSession(repository, DEFAULT_WS);
  			Node node = session.getNode(absPath);
  			String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
            if ("Published".equals(workflowState)) {
            	node.remove();
            	session.save();
            	if (this.authoringEnabled) {
	        		session = this.repositoryManager.getSession(repository, DRAFT_WS);
	      			node = session.getNode(absPath);
	            	node.setProperty("bpw:currentLifecycleState", EXPIRED_WS);
	            	session.save();
            	}
            }
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
  	};
  	
  	@DeleteMapping("/WcmItem/purge/{repository}/{workspace}")
  	public ResponseEntity<?> purgeWcmItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
  		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		absPath = (absPath.startsWith("/")) ? absPath : String.format("/%s", absPath);
  		try {
  			this.doPurgeWcmItem(repository, workspace, absPath);
  	  		if (logger.isDebugEnabled()) {
  				logger.traceExit();
  			}
  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
//		} catch (RepositoryException re) { 
//			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}

  	};

  	@DeleteMapping("/wcmItem/delete/{repository}/{workspace}")
  	public void deleteWcmItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
  		try {
  			Session session = this.repositoryManager.getSession(repository, DEFAULT_WS);
  			Node node = session.getNode(absPath);
  			node.remove();
            session.save();
            if (this.authoringEnabled) {
	            session = this.repositoryManager.getSession(repository, DRAFT_WS);
	  			node = session.getNode(absPath);
	  			node.remove();
	            session.save();
            }
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  	};
  	
  	@PutMapping("/contentItem/workflowState/{repository}/{workspace}")
  	public void updateWcmItemWorkflowStage(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("state") String state,
  			@RequestParam("path") String absPath) { 
  		try {
  			Session session = this.repositoryManager.getSession(repository, workspace);
  			Node node = session.getNode(absPath);
            node.setProperty("bpw:currentLifecycleState", state);
  			session.save();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  	};
  	
	@PostMapping(path = "/wcmNodes/{repository}/{workspace}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmNode[] getWcmNodes(
			@PathVariable("repository") final String repository, 
			@PathVariable("workspace") final String workspace,
			@RequestBody final WcmNavigatorFilter filter,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			final String siteAreaPath = filter.getNodePath().startsWith("/") ? filter.getNodePath() : "/" + filter.getNodePath();
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					siteAreaPath, 2);
			WcmNode[] wcmNodes = saNode.getChildren().stream()
			    .filter(node -> this.applyFilter(node, filter))
			    .map(node -> this.toWcmNode(node, siteAreaPath))
			    .toArray(WcmNode[]::new);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmNodes; 
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	};
	
	private boolean applyFilter(final RestNode node, WcmNavigatorFilter filter) {
		return (filter == null || filter.getNodeTypes() == null) ?
				node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> nodeType.startsWith("bpw:"))
		    : node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> Arrays.stream(
						filter.getNodeTypes()).anyMatch(nodeType::equals) &&
						this.propertyMatch(node, filter, nodeType));
	}
	
	private boolean propertyMatch(RestNode node, WcmNavigatorFilter filter, String nodeType) {
		if (filter.getFilters() == null || filter.getFilters().get(nodeType) == null) { return true; } 
		Map<String, String> nameValues = filter.getFilters().get(nodeType);
		Set<String> properties = new HashSet<>();
		properties.addAll(nameValues.keySet());
		for (RestProperty property: node.getJcrProperties()) {
			if (nameValues.get(property.getName()) != null && property.getValues().get(0).equals(nameValues.get(property.getName()))) {
				properties.remove(property.getName());
				if (properties.size() == 0) {
					break;
				}
			} 
		}
		return properties.size() == 0;
	}
	
	private WcmNode toWcmNode(RestNode node, String siteAreaPath) {
		WcmNode wcmNode = new WcmNode();
		wcmNode.setName(node.getName());
		for (RestProperty property: node.getJcrProperties()) {
			if ("jcr:primaryType".equals(property.getName())) {
				wcmNode.setNodeType(property.getValues().get(0));
				break;
			} 
		}
		wcmNode.setWcmPath(String.format("%s/%s", siteAreaPath, node.getName()));
		return wcmNode;
	}
	
	private QueryStatement toQueryStatement(RestNode node) {
		QueryStatement queryStatement = new QueryStatement();
		queryStatement.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				queryStatement.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:query".equals(restProperty.getName())) {
				queryStatement.setQuery(restProperty.getValues().get(0));
			} 
		}
		return queryStatement;
	}
	
	private BpmnWorkflow toBpmnWorkflow(RestNode node) {
		BpmnWorkflow bpmnWorkflow = new BpmnWorkflow();
		bpmnWorkflow.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				bpmnWorkflow.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:description".equals(restProperty.getName())) {
				bpmnWorkflow.setDescription(restProperty.getValues().get(0));
			} else if ("bpw:bpmn".equals(restProperty.getName())) {
				bpmnWorkflow.setBpmn(restProperty.getValues().get(0));
			} 
		}
		return bpmnWorkflow;
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
	
	private void resolveWorkflowNode(WorkflowNode workflowNode, RestNode restNode, Map<String, String> properties) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				workflowNode.setTitle(property.getValues().get(0));
				properties.put("title", property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				workflowNode.setDescription(property.getValues().get(0));
				properties.put("description", property.getValues().get(0));
			} else if ("bpw:publishDate".equals(property.getName())) {
				workflowNode.setPublishDate(property.getValues().get(0));
			} else if ("bpw:expireDate".equals(property.getName())) {
				workflowNode.setExpireDate(property.getValues().get(0));
			} else if ("bpw:workflow".equals(property.getName())) {
				workflowNode.setWorkflow(property.getValues().get(0));
				properties.put("workflow", property.getValues().get(0));
			} else if ("bpw:workflowStage".equals(property.getName())) {
				workflowNode.setWorkflowStage(property.getValues().get(0));
			} else if ("jcr:lockOwner".equals(property.getName())) {
				workflowNode.setLockOwner(property.getValues().get(0));
			} else if ("bpw:currentLifecycleState".equals(property.getName())) {
				workflowNode.setCurrentLifecycleState(property.getValues().get(0));
			}				
		}
	}

	private void doLock(
			String repository,
			String workspace, 
			String absPath) throws RepositoryException {
		
		javax.jcr.lock.LockManager lm = this.repositoryManager.getSession(repository, workspace).getWorkspace().getLockManager();
		if (!lm.isLocked(absPath)) {
			lm.lock(absPath, true, false, Long.MAX_VALUE, ModeshapeUtils.getUserName());
		}
	}
	
	protected void deleteDraftItem(String repository, String path) throws RepositoryException {
		Session draftSession = this.repositoryManager.getSession(repository, DRAFT_WS);
		Item item = draftSession.getItem(path);
        item.remove();
        draftSession.save();
	}
}
