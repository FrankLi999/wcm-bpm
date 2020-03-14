package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.JsonForm;
import com.bpwizard.wcm.repo.rest.jcr.model.Navigation;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.Theme;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmLibrary;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmOperation;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmRepository;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmWorkspace;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories.Repository;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces.Workspace;

@RestController
@RequestMapping(WcmSystemRestController.BASE_URI)
@Validated
public class WcmSystemRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmSystemRestController.class);

	public static final String BASE_URI = "/wcm/api";

	// http://localhost:8080/wcm/api/wcmSystem/bpwizard/default/camunda/bpm
	@GetMapping(path = "/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmSystem getWcmSystem(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, @PathVariable("library") String library,
			@PathVariable("siteConfig") String siteConfigName, HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			WcmSystem wcmSystem = new WcmSystem();
			if (this.authoringEnabled) {
				Map<String, WcmOperation[]> operations = this.getWcmOperations(repository, workspace, request);
				wcmSystem.setOperations(operations);
				
				wcmSystem.setAuthoringTemplates(this.doGetAuthoringTemplates(repository, workspace, request));
				
				wcmSystem.setControlFiels(this.doGetControlField(repository, workspace, request));
				wcmSystem.setWcmRepositories(this.getWcmRepositories(request));	

				Map<String, JsonForm[]> jsonForms = this.doGetAuthoringTemplateAsJsonForm(repository, workspace, request);
				wcmSystem.setJsonForms(jsonForms);
			} else {
				Map<String, JsonForm[]> jsonForms = this.doGetApplicationJsonForm(repository, workspace, library, request);
				wcmSystem.setJsonForms(jsonForms);
			}
			
			wcmSystem.setJcrThemes(this.getTheme(repository, workspace, request));
			Map<String, RenderTemplate> renderTemplates = this.doGetRenderTemplates(repository, workspace, request);
			wcmSystem.setRenderTemplates(renderTemplates);

			Map<String, ContentAreaLayout> contentAreaLayouts = this.doGetContentAreaLayouts(repository, workspace,
					request);
			wcmSystem.setContentAreaLayouts(contentAreaLayouts);
			
			SiteConfig siteConfig = this.doGetSiteConfig(request, repository, workspace, library, siteConfigName);
			String rootSiteArea = siteConfig.getRootSiteArea();
			
			String baseUrl = RestHelper.repositoryUrl(request);
			Navigation[] navigations = this.getNavigations(baseUrl, repository, workspace, library, rootSiteArea);
			wcmSystem.setNavigations(navigations);
			wcmSystem.setSiteConfig(siteConfig);
			
			Map<String, SiteArea> siteAreas = this.getSiteAreas(repository, workspace, library, rootSiteArea, baseUrl);
			wcmSystem.setSiteAreas(siteAreas);
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmSystem;
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@GetMapping(path = "/wcmRepository/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmRepository[] getWcmRepositories(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryUrl = RestHelper.urlFrom(request);
			RestRepositories restRepositories = this.getRepositories(request);
			WcmRepository[] wcmRepositories = restRepositories.getRepositories().stream()
					.map(restReoisitory -> this.toWcmRepository(restReoisitory, repositoryUrl, baseUrl))
					.toArray(WcmRepository[]::new);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmRepositories;
		} catch (WcmRepositoryException e) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}

	@GetMapping(path = "/wcmOperation/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, WcmOperation[]> getWcmOperations(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode operationsNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					String.format(WCM_ROOT_PATH_PATTERN, "system/configuration/operations"), 2);
			Map<String, WcmOperation[]> wcmOperationMap = operationsNode.getChildren().stream()
					.filter(node -> this.wcmUtils.checkNodeType(node, "bpw:supportedOpertions"))
					.map(this::supportedOpertionsToWcmOperation)
					.collect(Collectors.toMap(wcmOperations -> wcmOperations[0].getJcrType(), Function.identity()));
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmOperationMap;
		} catch (WcmRepositoryException e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new WcmRepositoryException(t);
		}
	}

	@GetMapping(path = "/theme/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Theme[] getTheme(@PathVariable("repository") String repository, @PathVariable("workspace") String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String baseUrl = RestHelper.repositoryUrl(request);
		Theme[] themes = this.getThemeLibraries(repository, workspace, baseUrl)
				.flatMap(theme -> this.getThemes(theme, baseUrl)).toArray(Theme[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return themes;
	}

	private Stream<Theme> getThemeLibraries(String repository, String workspace, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toThemeWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<Theme> getThemes(Theme theme, String baseUrl) throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(baseUrl, theme.getRepositoryName(),
					theme.getWorkspace(), "/bpwizard/library/" + theme.getLibrary() + "/theme", 1);
			return themeNode.getChildren().stream().filter(this::isTheme).map(node -> this.toTheme(node, theme));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Theme toThemeWithLibrary(RestNode node, String repository, String workspace) {
		Theme themeWithLibrary = new Theme();
		themeWithLibrary.setRepositoryName(repository);
		themeWithLibrary.setWorkspace(workspace);
		themeWithLibrary.setLibrary(node.getName());
		return themeWithLibrary;
	}

	private Theme toTheme(RestNode node, Theme theme) {
		Theme result = new Theme();
		result.setRepositoryName(theme.getRepositoryName());
		result.setWorkspace(theme.getWorkspace());
		result.setLibrary(theme.getLibrary());
		result.setName(node.getName());
		this.wcmUtils.resolveResourceNode(result, node);
		return result;
	}

	private WcmOperation[] supportedOpertionsToWcmOperation(final RestNode node) {
		return node.getChildren().stream().filter(n -> this.wcmUtils.checkNodeType(n, "bpw:supportedOpertion"))
			.map(n -> {
				WcmOperation wcmOperation = new WcmOperation();
				wcmOperation.setJcrType(this.getJcrType(node));
				for (RestProperty property: n.getJcrProperties()) {
					if ("bpw:defaultIcon".equals(property.getName())) {
						wcmOperation.setDefaultIcon(property.getValues().get(0));
					} else if ("bpw:operation".equals(property.getName())) {
						wcmOperation.setOperation(property.getValues().get(0));
					} else if ("bpw:resourceName".equals(property.getName())) {
						wcmOperation.setResourceName(property.getValues().get(0));
					} else if ("bpw:defaultTitle".equals(property.getName())) {
						wcmOperation.setDefaultTitle(property.getValues().get(0));
					} else if ("bpw:condition".equals(property.getName())) {
						wcmOperation.setCondition(property.getValues().get(0));
					}
				}
				return wcmOperation;
			}).toArray(WcmOperation[]::new);
	}
	
	/**
     * Returns the list of JCR repositories available on this server
     *
     * @param request the servlet request; may not be null
     * @return a list of available JCR repositories, as a {@link RestRepositories} instance.
     */
    private RestRepositories getRepositories(
    		HttpServletRequest request 
    		) {
        RestRepositories repositories = new RestRepositories();
        for (String repositoryName : this.repositoryManager.getJcrRepositoryNames()) {
        	String workspacesUrl = RestHelper.urlFrom(request, repositoryName);
            String backupUrl = RestHelper.urlFrom(request, repositoryName, RestHelper.BACKUP_METHOD_NAME);
            String restoreUrl = RestHelper.urlFrom(request, repositoryName, RestHelper.RESTORE_METHOD_NAME);
        	this.serverHandler.addRepository(workspacesUrl, backupUrl, restoreUrl, repositories, repositoryName);
        }
        return repositories;
    }
    
    private WcmRepository toWcmRepository(Repository restReoisitory, String repositoryUrl, String baseUrl) throws WcmRepositoryException {
		try {
			WcmRepository wcmRepository = new WcmRepository();
			wcmRepository.setName(restReoisitory.getName());
			RestWorkspaces restWorkspaces = this.repositoryHandler.getWorkspaces(repositoryUrl, restReoisitory.getName());
			WcmWorkspace[] wcmWorkspaces = restWorkspaces.getWorkspaces().stream()
					.map(workspace -> toWcmWorkspace(restReoisitory.getName(), workspace, baseUrl))
					.toArray(WcmWorkspace[]::new);
			wcmRepository.setWorkspaces(wcmWorkspaces);
			return wcmRepository;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}		
	}
    
    private WcmWorkspace toWcmWorkspace(String repository, Workspace restWorkspace, String baseUrl) {
		WcmWorkspace wcmWorkspace = new WcmWorkspace();
		wcmWorkspace.setName(restWorkspace.getName());
		wcmWorkspace.setLibraries(this.getWcmLibraries(repository, wcmWorkspace.getName(), baseUrl));
		return wcmWorkspace;
	}
    
    private WcmLibrary[] getWcmLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> {
						 WcmLibrary  wcmLibrary = new  WcmLibrary();
						 wcmLibrary.setName(node.getName());
						 return wcmLibrary;
					}).toArray(WcmLibrary[]::new);
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
}