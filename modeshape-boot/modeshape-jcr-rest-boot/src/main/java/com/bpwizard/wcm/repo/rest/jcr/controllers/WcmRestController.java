package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.BaseFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlField;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlFieldMetadata;
import com.bpwizard.wcm.repo.rest.jcr.model.FieldLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.Footer;
import com.bpwizard.wcm.repo.rest.jcr.model.FormColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRow;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRows;
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormSteps;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTabs;
import com.bpwizard.wcm.repo.rest.jcr.model.JsonForm;
import com.bpwizard.wcm.repo.rest.jcr.model.KeyValue;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutRow;
import com.bpwizard.wcm.repo.rest.jcr.model.NavBar;
import com.bpwizard.wcm.repo.rest.jcr.model.Navigation;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationBadge;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationItem;
import com.bpwizard.wcm.repo.rest.jcr.model.NavigationType;
import com.bpwizard.wcm.repo.rest.jcr.model.PageConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.PageLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplateLayoutColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplateLayoutRow;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceElementRender;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceViewer;
import com.bpwizard.wcm.repo.rest.jcr.model.SearchData;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePane;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePanel;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.Theme;
import com.bpwizard.wcm.repo.rest.jcr.model.Toolbar;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmLibrary;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNavigatorFilter;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNode;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmOperation;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmRepository;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmWorkspace;
import com.bpwizard.wcm.repo.rest.jcr.model.WorkflowNode;
import com.bpwizard.wcm.repo.rest.jcr.model.keyValues;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestChildType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNodeType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestPropertyType;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories.Repository;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces.Workspace;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(WcmRestController.BASE_URI)
public class WcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmRestController.class);
	public static final String BASE_URI = "/wcm/api";

	 @Value("${bpw.modeshape.authoring.enabled:true}")
	private boolean authoringEnabled = true;
	
	@Autowired
	private RestItemHandler itemHandler;

	@Autowired
	protected RepositoryManager repositoryManager;
	
	@Autowired
	private RestRepositoryHandler repositoryHandler;

	@Autowired
	private RestNodeTypeHandler nodeTypeHandler;
	
	@Autowired
    private RestServerHandler serverHandler;

	@Autowired
    private WcmUtils wcmUtils;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
    //http://localhost:8080/wcm/api/wcmSystem/bpwizard/default/camunda/bpm
	@GetMapping(path = "/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}", 
		produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmSystem getWcmSystem(
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
			WcmSystem wcmSystem = new WcmSystem();
			Map<String, WcmOperation[]> operations = this.getWcmOperations(repository, workspace, request);
			wcmSystem.setOperations(operations);
			wcmSystem.setJcrThemes(this.getTheme(repository, workspace, request));
			Map<String, JsonForm> jsonForms = this.getAuthoringTemplateAsJsonForm(repository, workspace, request);
			wcmSystem.setJsonForms(jsonForms);
			Map<String, RenderTemplate> renderTemplates = this.getRenderTemplates(repository, workspace, request);
			wcmSystem.setRenderTemplates(renderTemplates);
			Map<String, ContentAreaLayout> contentAreaLayouts = this.getContentAreaLayouts(repository, workspace, request);
			wcmSystem.setContentAreaLayouts(contentAreaLayouts);
			wcmSystem.setAuthoringTemplates(this.getAuthoringTemplates(repository, workspace, request));
			SiteConfig siteConfig = this.getSiteConfig(
					request,
					repository,
					workspace,
					library,
					siteConfigName);
			String rootSiteArea = siteConfig.getRootSiteArea();
			String baseUrl = RestHelper.repositoryUrl(request);
			Navigation[] navigations = this.getNavigations(baseUrl, repository, workspace, library, rootSiteArea);
			wcmSystem.setNavigations(navigations);
			wcmSystem.setSiteConfig(siteConfig);
			Map<String, SiteArea> siteAreas = this.getSiteAreas(repository, workspace, library, rootSiteArea, baseUrl);
			wcmSystem.setSiteAreas(siteAreas);
			wcmSystem.setControlFiels(this.getControlField(repository, workspace, request));
			wcmSystem.setWcmRepositories(this.getWcmRepositories(request));
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmSystem;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}

	@GetMapping(path = "/wcmRepository/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmRepository[] getWcmRepositories(HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
            String repositoryUrl = RestHelper.urlFrom(request);
            RestRepositories restRepositories = this.getRepositories(request);
			WcmRepository[] wcmRepositories = restRepositories.getRepositories().stream()
					.map(restReoisitory -> this.toWcmRepository(restReoisitory, repositoryUrl, baseUrl)).toArray(WcmRepository[]::new);
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmRepositories;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/wcmOperation/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, WcmOperation[]> getWcmOperations(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode operationsNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library/system/configuration/operations", 2);
			Map<String, WcmOperation[]> wcmOperationMap = operationsNode.getChildren().stream().filter(node -> this.wcmUtils.checkNodeType(node, "bpw:supportedOpertions"))
			    .map(this::supportedOpertionsToWcmOperation)
			    .collect(Collectors.toMap(
						wcmOperations -> wcmOperations[0].getJcrType(), 
						Function.identity()));
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmOperationMap;
		} catch (WcmRepositoryException e ) {
			e.printStackTrace();
			throw e;
	    } catch (Throwable t) {
			t.printStackTrace();
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/theme/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Theme[] getTheme(
			@PathVariable("repository") String repository, 
			@PathVariable("workspace") String workspace,
			HttpServletRequest request) 
			throws WcmRepositoryException {
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

	@GetMapping(path = "/at/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, AuthoringTemplate> getAuthoringTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			Map<String, AuthoringTemplate> authoringTemplates = this.getAuthoringTemplateLibraries(repository, workspace, baseUrl)
					.flatMap(at -> this.getAuthoringTemplates(at, baseUrl))
					.collect(Collectors.toMap(
							at -> String.format("%s/%s/%s/%s", repository, workspace, at.getLibrary(), at.getName()), 
							Function.identity()));
	
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
	public Map<String, JsonForm> getAuthoringTemplateAsJsonForm(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			Map<String, JsonForm> jsonForms = this.getAuthoringTemplateLibraries(repository, workspace, baseUrl)
					.flatMap(at -> this.getAuthoringTemplates(at, baseUrl))
					.map(at -> this.toJsonForm(request, repository, workspace, at)).collect(Collectors.toMap(
							jsonForm -> String.format("%s/%s/%s/%s", repository, workspace, jsonForm.getLibrary(), jsonForm.getResourceType()), 
							Function.identity()));
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
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode controlFieldFolder = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library/system/controlField", 2);
			ControlField[] ControlFileds = controlFieldFolder.getChildren().stream().filter(this::isControlField)
					.map(this::toControlField).toArray(ControlField[]::new);
	
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
//			String repositoryName = siteConfig.getRepository();
//	
//			Session session = repositoryManager.getSession(repositoryName, "default");
//			Node siteConfigFolder = session.getNode(String.format("/bpwizard/library/%s/siteConfig", siteConfig.getLibrary()));
//			Node siteConfigNode = siteConfigFolder.addNode(siteConfig.getName(), "bpw:siteConfig");
//			
//			siteConfigNode.setProperty("bpw:name", siteConfig.getName());
//			siteConfigNode.setProperty("bpw:colorTheme", siteConfig.getColorTheme());
//			siteConfigNode.setProperty("bpw:rootSiteArea", siteConfig.getRootSiteArea());
//			siteConfigNode.setProperty("bpw:customScrollbars", siteConfig.isCustomScrollbars());
//			
//			PageLayout layout = siteConfig.getLayout();
//			Node layoutNode = siteConfigNode.addNode("layout", "bpw:pageLayout");
//			layoutNode.setProperty("bpw:style", layout.getStyle());
//			layoutNode.setProperty("bpw:width", layout.getWidth());
//			
//			Node navbarNode = layoutNode.addNode("navbar", "bpw:navbar");
//			navbarNode.setProperty("primaryBackground", layout.getNavbar().getPrimaryBackground());
//			navbarNode.setProperty("secondaryBackground", layout.getNavbar().getSecondaryBackground());
//			navbarNode.setProperty("hidden", layout.getNavbar().isHidden());
//			navbarNode.setProperty("folded", layout.getNavbar().isFolded());
//			navbarNode.setProperty("position", layout.getNavbar().getPosition());
//			navbarNode.setProperty("variant", layout.getNavbar().getVariant());
//	
//			
//			Node toolbarNode = layoutNode.addNode("toolbar", "bpw:toolbar");
//			toolbarNode.setProperty("customBackgroundColor", layout.getToolbar().isCustomBackgroundColor());
//			toolbarNode.setProperty("background", layout.getToolbar().getBackground());
//			toolbarNode.setProperty("hidden", layout.getToolbar().isHidden());
//			toolbarNode.setProperty("position", layout.getToolbar().getPosition());
//			
//			Node footerNode = layoutNode.addNode("footer", "bpw:footer");
//			footerNode.setProperty("customBackgroundColor", layout.getFooter().isCustomBackgroundColor());
//			footerNode.setProperty("background", layout.getFooter().getBackground());
//			footerNode.setProperty("hidden", layout.getFooter().isHidden());
//			footerNode.setProperty("position", layout.getFooter().getPosition());
//	
//			Node sidePanelNode = layoutNode.addNode("sidePanel", "bpw:sidePanel");
//			sidePanelNode.setProperty("hidden", layout.getSidePanel().isHidden());
//			sidePanelNode.setProperty("position", layout.getSidePanel().getPosition());
//			session.save();
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/siteConfig/%s", siteConfig.getLibrary(), siteConfig.getName());
			this.itemHandler.addItem(
					baseUrl, 
					repositoryName, 
					"default", 
					path, 
					siteConfig.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("darft", path, path, true);
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
			String path = String.format("/bpwizard/library/%s/siteConfig/%s", siteConfig.getLibrary(), siteConfig.getName());
			String repositoryName = siteConfig.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.updateItem(
					baseUrl, 
					repositoryName, 
					"default", 
					path, 
					siteConfig.toJson());
			if (this.authoringEnabled) {
				Session session = repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("darft", path, path, true);
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
		
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = String.format("/bpwizard/library/%s/siteConfig/%s", library, siteConfigName);
			RestNode siteConfigNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace, absPath, 2);
			SiteConfig siteConfig = this.getSiteConfig(siteConfigNode);
			siteConfig.setRepository(repository);
			siteConfig.setWorkspace(workspace);
			siteConfig.setLibrary(library);
		
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
			String absPath = String.format("/bpwizard/library/%s/siteConfig/%s", library, siteConfigName);
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
	
	@PostMapping(path = "/at", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/authoringTemplate/%s", at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			this.itemHandler.addItem(baseUrl,  repositoryName, "default", path, at.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
//		try {
//			String repositoryName = at.getRepository();
//			String workspaceName = at.getWorkspace();
//	
//			Session session = repositoryManager.getSession(repositoryName, workspaceName);
//			Node atFolder = session.getNode(String.format("/bpwizard/library/%s/authoringTemplate", at.getLibrary()));
//			Node atNode = atFolder.addNode(at.getName(), "bpw:authoringTemplate");
//			if (StringUtils.hasText(at.getBaseResourceType())) {
//				atNode.setProperty("bpw:baseResourceType", at.getBaseResourceType());
//			}
//			if (StringUtils.hasText(at.getTitle())) {
//				atNode.setProperty("bpw:title", at.getTitle());
//			}
//			if (StringUtils.hasText(at.getDescription())) {
//				atNode.setProperty("bpw:description", at.getDescription());
//			}
//			
//			if (StringUtils.hasText(at.getWorkflow())) {
//				atNode.setProperty("bpw:workflow", at.getWorkflow());
//			}
//	
//			this.addRow(atNode, at.getPropertyRow(), "property-group");
//			
//			Node elementGroupNode = atNode.addNode("element-group", "bpw:formGroupFoler");
//			int groupCount = 0;
//			for (BaseFormGroup g : at.getElementGroups()) {
//				groupCount++;
//				String groupName = StringUtils.hasText(g.getGroupTitle()) ? g.getGroupTitle() : ("group" + groupCount);
//				String primaryFormGroupType = (g instanceof FormSteps) ? "bpw:formSteps"
//						: (g instanceof FormTabs) ? "bpw:formTabs"
//								: (g instanceof FormRows) ? "bpw:formRows" : "bpw:formRow";
//				Node groupNode = elementGroupNode.addNode(groupName, primaryFormGroupType);
//	
//				if (g instanceof FormSteps) {
//					this.addSteps(groupNode, ((FormSteps) g).getSteps());
//				} else if (g instanceof FormTabs) {
//					this.addTabs(groupNode, ((FormTabs) g).getTabs());
//				} else if (g instanceof FormRows) {
//					this.addRows(groupNode, ((FormRows) g).getRows());
//				}
//			}
//			Node elementFolder = atNode.addNode("elements", "bpw:elementFolder");
//			for (String controlName : at.getElements().keySet()) {
//				this.addControl(elementFolder, at.getElements().get(controlName));
//			}
//			Node propertiesFolder = atNode.addNode("properties", "bpw:propertyFolder");
//			for (String controlName : at.getProperties().keySet()) {
//				this.addControl(propertiesFolder, at.getElements().get(controlName));
//			}
//			session.save();
//			if (logger.isDebugEnabled()) {
//				logger.traceExit();
//			}
//	
//			return ResponseEntity.status(HttpStatus.CREATED).build();
//		} catch (WcmRepositoryException e ) {
//			throw e;
//		} catch (Throwable t) {
//			throw new WcmRepositoryException(t);
//		}	
	}

	@PutMapping(path = "/at", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/authoringTemplate/%s", at.getLibrary(), at.getName());
			String repositoryName = at.getRepository();
			this.itemHandler.updateItem(baseUrl, repositoryName, "default", path, at.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
	
	@GetMapping(path = "/rt/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, RenderTemplate> getRenderTemplates(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, RenderTemplate> renderTemplates = this.getRenderTemplateLibraries(repository, workspace, request)
					.flatMap(rt -> this.getRenderTemplates(rt, request)).collect(Collectors.toMap(
							rt -> String.format("%s/%s/%s/%s", repository, workspace, rt.getLibrary(), rt.getName()), 
							Function.identity()));
	
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
//			String repositoryName = rt.getRepository();
//			String workspaceName = rt.getWorkspace();
//	
//			Session session = repositoryManager.getSession(repositoryName, workspaceName);
//			Node rtFolder = session.getNode(String.format("/bpwizard/library/%s/renderTemplate", rt.getLibrary()));
//			Node rtNode = rtFolder.addNode(rt.getName(), "bpw:renderTemplate");
//			rtNode.setProperty("bpw:name", rt.getName());
//			rtNode.setProperty("bpw:title", StringUtils.hasText(rt.getTitle()) ? rt.getTitle() : rt.getName());
//			if (StringUtils.hasText(rt.getDescription())) {
//				rtNode.setProperty("bpw:description", rt.getDescription());
//			}
//			if (StringUtils.hasText(rt.getCode())) {
//				rtNode.setProperty("bpw:code", rt.getCode());
//			}
//			if (StringUtils.hasText(rt.getPreloop())) {
//				rtNode.setProperty("bpw:preloop", rt.getPreloop());
//			}
//			if (StringUtils.hasText(rt.getPostloop())) {
//				rtNode.setProperty("bpw:postloop", rt.getPostloop());
//			}
//			rtNode.setProperty("bpw:maxEntries", rt.getMaxEntries());
//			if (StringUtils.hasText(rt.getNote())) {
//				rtNode.setProperty("bpw:note", rt.getNote());
//			}
//			if (StringUtils.hasText(rt.getResourceName())) {
//				rtNode.setProperty("bpw:resourceName", rt.getResourceName());
//			}
//			rtNode.setProperty("bpw:isQuery", rt.isQuery());
//			
//			if (rt.getRows() != null && rt.getRows().length > 0) {
//				int rowCount = 1;
//				for(RenderTemplateLayoutRow row: rt.getRows()) {
//					Node rowNode = rtNode.addNode("row" + rowCount++, "bpw:RenderTemplateLayoutRow");
//					int columnCount = 1;
//					for (RenderTemplateLayoutColumn column: row.getColumns()) {
//						Node columnNode = rowNode.addNode("column" + columnCount++, "bpw:RenderTemplateLayoutColumn");
//						if (StringUtils.hasText(column.getId())) {
//							columnNode.setProperty("bpw:id", column.getId());
//						}
//						if (column.getWidth() > 0) {
//							columnNode.setProperty("bpw:width", column.getWidth());
//						}
//						for (ResourceElementRender element: column.getElements()) {
//							Node elementNode = columnNode.addNode(element.getName(), "bpw:ResourceElementRender");
//							elementNode.setProperty("bpw:name", element.getName());
//						}
//					    
//					}
//				}
//			}
//			session.save();
			String repositoryName = rt.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/renderTemplate", rt.getLibrary(), rt.getName());
			this.itemHandler.addItem(baseUrl, repositoryName, "default", path, rt.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
			String path = String.format("/bpwizard/library/%s/renderTemplate", rt.getLibrary(), rt.getName());
			this.itemHandler.updateItem(baseUrl, repositoryName, rt.getWorkspace(), path, rt.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
			String baseUrl = RestHelper.repositoryUrl(request);
			Map<String, ContentAreaLayout> contentAreaLayouts = this.getContentAreaLayoutLibraries(repository, workspace, baseUrl)
					.flatMap(layout -> this.getContentArealayouts(layout, baseUrl))
					.collect(Collectors.toMap(
							layout -> String.format("%s/%s/%s/%s", repository, workspace, layout.getLibrary(), layout.getName()), 
							Function.identity()));
	
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
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> getContentAreaLayout: " + library);
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
//			String repositoryName = pageLayout.getRepository();
//			String workspaceName = pageLayout.getWorkspace();
//	
//			Session session = repositoryManager.getSession(repositoryName, workspaceName);
//			Node contentAreaLayoutFolder = session
//					.getNode(String.format("/bpwizard/library/%s/contentAreaLayout", pageLayout.getLibrary()));
//			Node contentAreaLayoutNode = contentAreaLayoutFolder.addNode(pageLayout.getName(), "bpw:contentAreaLayout");
//			contentAreaLayoutNode.setProperty("bpw:name", pageLayout.getName());
//			contentAreaLayoutNode.setProperty("bpw:contentWidth", 80);
//	
//			Node sidePaneNode = contentAreaLayoutNode.addNode("sidePane", "bpw:contentAreaSidePanel");
//			this.addSidePaneNode(sidePaneNode, pageLayout.getSidePane());
//			this.addPageLayoutNodes(contentAreaLayoutNode, pageLayout.getRows());
//			session.save();
			
			String repositoryName = pageLayout.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			String path = String.format("/bpwizard/library/%s/contentAreaLayout/%s", pageLayout.getLibrary(), pageLayout.getName());
			this.itemHandler.addItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, pageLayout.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
			String path = String.format("/bpwizard/library/%s/contentAreaLayout/%s", pageLayout.getLibrary(), pageLayout.getName());
			String repositoryName = pageLayout.getRepository();
			this.itemHandler.updateItem(baseUrl, repositoryName, pageLayout.getWorkspace(), path, pageLayout.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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

	@PostMapping(path = "/sitearea/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(
			@RequestBody SiteArea sa, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
//			String repositoryName = sa.getRepository();
//			String workspaceName = sa.getWorkspace();
//	
//			Session session = repositoryManager.getSession(repositoryName, workspaceName);
//			Node parentFolder = session.getNode("/" + sa.getNodePath());
//			Node saNode = parentFolder.addNode(sa.getName(), "bpw:siteArea");
//	
//			saNode.setProperty("bpw:name", sa.getName());
//			saNode.setProperty("bpw:title", StringUtils.hasText(sa.getTitle()) ? sa.getTitle() : sa.getName());
//			if (StringUtils.hasText(sa.getDescription())) {
//				saNode.setProperty("bpw:description", sa.getDescription());
//			}
//			saNode.setProperty("bpw:url", StringUtils.hasText(sa.getUrl()) ? sa.getUrl() : sa.getName());
//			if (StringUtils.hasText(sa.getFriendlyURL())) {
//				saNode.setProperty("bpw:friendlyURL", sa.getFriendlyURL());
//			}
//			saNode.setProperty("bpw:sorderOrder", sa.getSorderOrder());
//			saNode.setProperty("bpw:showOnMenu", sa.isShowOnMenu());
//	
//			if (StringUtils.hasText(sa.getDefaultContent())) {
//				saNode.setProperty("bpw:defaultContent", sa.getDefaultContent());
//			}
//			if (StringUtils.hasText(sa.getAllowedArtifactTypes())) {
//				saNode.setProperty("bpw:allowedFileExtension", sa.getAllowedArtifactTypes().split(","));
//			}
//	
//			if (StringUtils.hasText(sa.getAllowedFileExtension())) {
//				saNode.setProperty("bpw:allowedFileExtension", sa.getAllowedFileExtension().split(","));
//			}
//	
//			saNode.setProperty("bpw:contentAreaLayout", sa.getContentAreaLayout());
//			saNode.setProperty("bpw:siteConfig", sa.getSiteConfig());
//			saNode.setProperty("bpw:securePage", sa.isSecurePage());
//			saNode.setProperty("bpw:cacheTTL", sa.getCacheTTL());
//	
//			if (StringUtils.hasText(sa.getNavigationId())) {
//				saNode.setProperty("bpw:navigationId", sa.getNavigationId());
//			}
//			
//			if (StringUtils.hasText(sa.getNavigationType())) {
//				saNode.setProperty("bpw:navigationType", sa.getNavigationType());
//			}
//			
//			if (StringUtils.hasText(sa.getTranslate())) {
//				saNode.setProperty("bpw:translate", sa.getTranslate());
//			}
//			
//			if (StringUtils.hasText( sa.getIcon())) {
//				saNode.setProperty("bpw:function", sa.getFunction());
//			}
//			
//			if (StringUtils.hasText(sa.getIcon())) {
//				saNode.setProperty("bpw:icon", sa.getIcon());
//			}
//			
//			if (StringUtils.hasText(sa.getClasses())) {
//				saNode.setProperty("bpw:classes", sa.getClasses());
//			}
//	
//			saNode.setProperty("bpw:exactMatch", sa.isExactMatch());
//			saNode.setProperty("bpw:externalUrl", sa.isExternalUrl());
//			saNode.setProperty("bpw:openInNewTab", sa.isOpenInNewTab());
//			
//			if (sa.getMetadata() != null && sa.getMetadata().getKeyValues() != null) {
//				Node metaDataNode = saNode.addNode("bpw:metaData", "bpw:keyValues");
//				int count = 0;
//				for (KeyValue keyValue: sa.getMetadata().getKeyValues()) {
//					Node kvNode = metaDataNode.addNode("kv" + count++, "bpw:keyValue");
//					kvNode.setProperty("bpw:name", keyValue.getName());
//					kvNode.setProperty("bpw:value", keyValue.getValue());
//				}
//			}
//			
//			if (sa.getSearchData() != null) {
//				Node searchDataDataNode = saNode.addNode("bpw:searchData", "bpw:pageSearchData");
//				if (StringUtils.hasText(sa.getSearchData().getDescription())) {
//					searchDataDataNode.setProperty("description", sa.getSearchData().getDescription());
//				}
//				if (sa.getSearchData().getKeywords() != null) {
//					searchDataDataNode.setProperty("keywords", sa.getSearchData().getKeywords());
//				}
//			}
//			
//			if (sa.getBadge() != null) {
//				Node badgeNode = saNode.addNode("bpw:badge", "bpw:navigationBadge");
//				if (StringUtils.hasText(sa.getBadge().getTitle())) {
//					badgeNode.setProperty("bpw:title", sa.getBadge().getTitle());
//				}
//				if (StringUtils.hasText(sa.getBadge().getTranslate())) {
//					badgeNode.setProperty("bpw:translate", sa.getBadge().getTranslate());
//				}
//				if (StringUtils.hasText(sa.getBadge().getBg())) {
//					badgeNode.setProperty("bpw:bg", sa.getBadge().getBg());
//				}
//				if (StringUtils.hasText(sa.getBadge().getFg())) {
//					badgeNode.setProperty("bpw:fg", sa.getBadge().getFg());
//				}
//			}
//	
//			Node siteAreaLayoutNode = saNode.addNode("siteAreaLayout", "bpw:siteAreaLayout");
//			if (sa.getSiteAreaLayout() != null) {
//				Node sidePaneNode = siteAreaLayoutNode.addNode("sidePane", "bpw:contentAreaSidePanel");
//				sidePaneNode.setProperty("bpw:contentWidth", sa.getSiteAreaLayout().getContentWidth());
//				this.addSidePaneNode(sidePaneNode, sa.getSiteAreaLayout().getSidePane());
//				this.addPageLayoutNodes(siteAreaLayoutNode, sa.getSiteAreaLayout().getRows());
//			}
//			session.save();
			
			String path = sa.getNodePath();
			String repositoryName = sa.getRepository();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, repositoryName, sa.getWorkspace(), path, sa.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
	
	@PutMapping(path = "/siteArea/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveSiteArea(
			@RequestBody SiteArea sa, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String path = sa.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			String repositoryName = sa.getRepository();
			this.itemHandler.updateItem(baseUrl, repositoryName, sa.getWorkspace(), path, sa.toJson());
			if (this.authoringEnabled) {
				Session session = this.repositoryManager.getSession(repositoryName, "default");
				session.getWorkspace().clone("draft", path, path, true);
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
			sa.setName(saNode.getName());
			
			for (RestProperty property: saNode.getJcrProperties()) {
				if ("bpw:name".equals(property.getName())) {
					sa.setName(property.getValues().get(0));
				} else if ("bpw:title".equals(property.getName())) {
					sa.setTitle(property.getValues().get(0));
				} else if ("bpw:description".equals(property.getName())) {
					sa.setDescription(property.getValues().get(0));
				} else if ("bpw:url".equals(property.getName())) {
					sa.setUrl(property.getValues().get(0));
				} else if ("bpw:friendlyURL".equals(property.getName())) {
					sa.setFriendlyURL(property.getValues().get(0));
				} else if ("bpw:sorderOrder".equals(property.getName())) {
					sa.setSorderOrder(Integer.parseInt(property.getValues().get(0)));
				} else if ("bpw:showOnMenu".equals(property.getName())) {
					sa.setShowOnMenu(Boolean.parseBoolean(property.getValues().get(0)));
				} else if ("bpw:defaultContent".equals(property.getName())) {
					sa.setDefaultContent(property.getValues().get(0));
				} else if ("bpw:allowedFileExtension".equals(property.getName())) {
					sa.setAllowedArtifactTypes(property.getValues().get(0));
				} else if ("bpw:allowedFileExtension".equals(property.getName())) {
					sa.setAllowedFileExtension(property.getValues().get(0));
				} else if ("bpw:contentAreaLayout".equals(property.getName())) {
					sa.setContentAreaLayout(property.getValues().get(0));
				} else if ("bpw:siteConfig".equals(property.getName())) {
					sa.setSiteConfig(property.getValues().get(0));
				} else if ("bpw:securePage".equals(property.getName())) {
					sa.setSecurePage(Boolean.parseBoolean(property.getValues().get(0)));
				} else if ("bpw:cacheTTL".equals(property.getName())) {
					sa.setCacheTTL(Integer.parseInt(property.getValues().get(0)));
				} else if ("bpw:navigationId".equals(property.getName())) {
					sa.setNavigationId(property.getValues().get(0));
				} else if ("bpw:navigationType".equals(property.getName())) {
					sa.setNavigationType(property.getValues().get(0));
				} else if ("bpw:translate".equals(property.getName())) {
					sa.setTranslate(property.getValues().get(0));
				} else if ("bpw:function".equals(property.getName())) {
					sa.setFunction(property.getValues().get(0));
				} else if ("bpw:icon".equals(property.getName())) {
					sa.setIcon(property.getValues().get(0));
				} else if ("bpw:classes".equals(property.getName())) {
					sa.setClasses(property.getValues().get(0));
				} else if ("bpw:exactMatch".equals(property.getName())) {
					sa.setExactMatch(Boolean.parseBoolean(property.getValues().get(0)));
				} else if ("bpw:externalUrl".equals(property.getName())) {
					sa.setExternalUrl(Boolean.parseBoolean(property.getValues().get(0)));
				} else if ("bpw:openInNewTab".equals(property.getName())) {
					sa.setOpenInNewTab(Boolean.parseBoolean(property.getValues().get(0)));
				} else if ("jcr:lockOwner".equals(property.getName())) {
					sa.setLockOwner(property.getValues().get(0));
				}
			}

			
			for (RestNode node: saNode.getChildren()) {
			   if (this.wcmUtils.checkNodeType(node, "bpw:keyValues") && ("bpw:metaData".equals(node.getName()))) {
				   keyValues metadata = new keyValues();
				   List<KeyValue> kvList = new ArrayList<>();
				   for (RestNode kvNode: node.getChildren()) {
					   if (this.wcmUtils.checkNodeType(kvNode, "bpw:keyValue")) {
						   KeyValue kv = new KeyValue();
						   for (RestProperty property: kvNode.getJcrProperties()) {
							   if ("bpw:name".equals(property.getName())) {
								   kv.setName(property.getValues().get(0));
							   } else if ("bpw:value".equals(property.getName())) {
								   kv.setValue(property.getValues().get(0));
							   }
						   }
						   kvList.add(kv);
					   }
				   }
				   metadata.setKeyValue(kvList.toArray(new KeyValue[kvList.size()]));
				   sa.setMetadata(metadata);
			   } else if (this.wcmUtils.checkNodeType(node, "bpw:pageSearchData") && ("bpw:searchData".equals(node.getName()))) {
				   SearchData searchData = new SearchData();
				   for (RestProperty property: node.getJcrProperties()) {
					   if ("description".equals(property.getName())) {
						   searchData.setDescription(property.getValues().get(0));
					   } else if ("keywords".equals(property.getName())) {
						   searchData.setKeywords(property.getValues().toArray(new String[property.getValues().size()]));
					   }
				   }
				   sa.setSearchData(searchData);
			   } else if (this.wcmUtils.checkNodeType(node, "bpw:navigationBadge") && ("bpw:badge".equals(node.getName()))) {
				   NavigationBadge badge = new NavigationBadge();
				   for (RestProperty property: node.getJcrProperties()) {
					   if ("bpw:title".equals(property.getName())) {
						   badge.setTitle(property.getValues().get(0));
					   } else if ("bpw:translate".equals(property.getName())) {
						   badge.setTranslate(property.getValues().get(0));
					   } else if ("bpw:bg".equals(property.getName())) {
						   badge.setBg(property.getValues().get(0));
					   } else if ("bpw:fg".equals(property.getName())) {
						   badge.setFg(property.getValues().get(0));
					   }
				   }
				   sa.setBadge(badge);
			   } else if (this.wcmUtils.checkNodeType(node, "bpw:siteAreaLayout") && ("siteAreaLayout".equals(node.getName()))) {
				   SiteAreaLayout siteAreaLayout = new SiteAreaLayout();
				   List<LayoutRow> rows = new ArrayList<>();
				   for (RestProperty property : node.getJcrProperties()) {
						if ("bpw:contentWidth".equals(property.getName())) {
							siteAreaLayout.setContentWidth(Integer.parseInt(property.getValues().get(0)));
							break;
						} 
				   }
				   for (RestNode childNode: node.getChildren()) {
					   if (this.wcmUtils.checkNodeType(childNode, "bpw:contentAreaSidePanel") && "sidePane".equals(childNode.getName())) {
						   SidePane sidePane = new SidePane();
						   for (RestProperty property : childNode.getJcrProperties()) {
								if ("bpw:isLeft".equals(property.getName())) {
									sidePane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
								} else if ("bpw:width".equals(property.getName())) {
									sidePane.setWidth(Integer.parseInt(property.getValues().get(0)));
								}
							}
						   sidePane.setViewers(this.resolveResourceViewer(childNode));
						   siteAreaLayout.setSidePane(sidePane);
					   } else if (this.wcmUtils.checkNodeType(childNode, "bpw:layoutRow")) {
							LayoutRow row = this.resolveLayoutRow(childNode);
							rows.add(row);
					   }
				   }
				   siteAreaLayout.setRows(rows.toArray(new LayoutRow[rows.size()]));
				   sa.setSiteAreaLayout(siteAreaLayout);
			   }
			}
			
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
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), "draft", path, contentItem.toJson());
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
			contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			String path = contentItem.getNodePath();
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), "default", path, contentItem.toJson());
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), "default");
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
			}
			if (authoringEnabled) {
				session.getWorkspace().clone("draft", contentItem.getNodePath(), contentItem.getNodePath(), true);
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
			Session session = this.repositoryManager.getSession(repository, "default");
			session.getWorkspace().clone("darft", contentItemPath, contentItemPath, true);
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
			contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItem.toJson());
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), "default");
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, contentItem.getNodePath(), contentItem.getAcl());
			}
			this.wcmUtils.unlock(contentItem.getRepository(), contentItem.getWorkspace(), path);
			if (this.authoringEnabled) {
				session.getWorkspace().clone("draft", contentItem.getNodePath(), contentItem.getNodePath(), true);
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
					contentItemPath, 3);
			
			ContentItem contentItem = new ContentItem(); 
			contentItem.setRepository(repository);
			contentItem.setWorkspace(workspace);
			contentItem.setNodePath(contentItemPath);
			Map<String, String> elements = new HashMap<>();
			Map<String, String> properties = new HashMap<>();
			contentItem.setElements(elements);
			contentItem.setProperties(properties);
			contentItem.setName(contentItemNode.getName());
			this.resolveWorkflowNode(contentItem, contentItemNode);
			for (RestProperty property: contentItemNode.getJcrProperties()) {
				if ("bpw:authoringTemplate".equals(property.getName())) {
					contentItem.setAuthoringTemplate(property.getValues().get(0));
				} else if ("bpw:categories".equals(property.getName())) {
					contentItem.setCategories(property.getValues().toArray(new String[property.getValues().size()]));
				} 
			}
			for (RestNode node: contentItemNode.getChildren()) {
				if (this.wcmUtils.checkNodeType(node, "bpw:contentElementFolder")) {
					for (RestNode enode: node.getChildren()) {
						if (this.wcmUtils.checkNodeType(enode, "bpw:contentElement")) {
							for (RestProperty property: enode.getJcrProperties()) {
								if ("bpw:value".equals(property.getName())) {
									properties.put(enode.getName(), property.getValues().get(0));
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
			contentItem.setCurrentLifecycleState("Draft"); //TODO
			String path = contentItem.getNodePath();
			AuthoringTemplate at = this.getAuthoringTemplate(contentItem.getRepository(), "draft", 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnSaveDraftPermissions());
			
			// this.itemHandler.addItem(request, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItem.toJson());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), "Draft", path, contentItem.toJson());
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, "draft");
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
	        Session session = this.repositoryManager.getSession(repository, "draft"); 
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
	        Session session = this.repositoryManager.getSession(repository, "draft"); 
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
	        Session session = this.repositoryManager.getSession(repository, "draft"); 
	        Node contentNode = session.getNode(contentItemPath);
	        contentNode.setProperty("bpw:currentLifecycleState", "Published");
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.getAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnPublishPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        session.getWorkspace().clone("default", contentItemPath, contentItemPath, true);
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
  			Session session = this.repositoryManager.getSession(repository, "default");
  			Node node = session.getNode(absPath);
  			String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
            if ("Published".equals(workflowState)) {
            	node.remove();
            	session.save();
            	if (this.authoringEnabled) {
	        		session = this.repositoryManager.getSession(repository, "draft");
	      			node = session.getNode(absPath);
	            	node.setProperty("bpw:currentLifecycleState", "Expired");
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
  	
  	@DeleteMapping("/contentItem/purge/{repository}/{workspace}")
  	public void purgeContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
  		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		try {
  			if (this.authoringEnabled) {
	  			Session session = this.repositoryManager.getSession(repository, "draft");
	  			Node node = session.getNode(absPath);
	  			String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
	            if ("Expired".equals(workflowState)) {
	            	node.remove();
	            	session.save();
	            }
  			}
  			
  			Session session = this.repositoryManager.getSession(repository, "default");
  			try {
	  			Node node = session.getNode(absPath);
	            node.remove();
	            session.save();
  			} catch (PathNotFoundException ex) {
  				logger.warn(String.format("Content item %s does not exist", absPath));
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

  	@DeleteMapping("/wcmItem/delete/{repository}/{workspace}")
  	public void deleteWcmItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
  		try {
  			Session session = this.repositoryManager.getSession(repository, "default");
  			Node node = session.getNode(absPath);
  			node.remove();
            session.save();
            if (this.authoringEnabled) {
	            session = this.repositoryManager.getSession(repository, "draft");
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
	
	private ObjectNode toPropertyNode(FormControl formControl, ObjectNode definitions) {
		ObjectNode propertyNode = JsonNodeFactory.instance.objectNode();
		// propertyNode.put("name", formControl.getName());
		// propertyNode.put("type", formControl.getDataType());
		if ("keyValue".equals(formControl.getControlName())) {
			propertyNode.put("$ref", "#/definitions/keyValues");
		} else if ("customField".equals(formControl.getControlName())) {
			propertyNode.put("$ref",
					String.format("#/definitions/%s", this.fieldNameFromNodeTypeName(formControl.getDataType())));
		} else {
			propertyNode.put("type", "string");
		}
		// type boolean, number
		// range: integer:
		// "minimum": 7,
		/// "maximum": 77,
		// "format": "color"

		// emum: "enum": [ "male", "female", "alien" ]

		// array
		if (StringUtils.hasText(formControl.getHint())) {
			propertyNode.put("description", formControl.getHint());
		}
		if (StringUtils.hasText(formControl.getDefaultValue())) {
			propertyNode.put("default", formControl.getDefaultValue());
		}
		return propertyNode;
	}

	private Optional<FieldLayout> getCustomeFileLayout(FormColumn formColumn, String controlName) {
		FieldLayout customFieldLayout = null;
		for (FieldLayout layout : formColumn.getFieldLayouts()) {
			if (controlName.equals(layout.getName())) {
				customFieldLayout = layout;
				break;
			}
		}
		return customFieldLayout == null ? Optional.empty() : Optional.of(customFieldLayout);
	}

	private String getLayoutFieldKey(String key, String placeHolder) {
		return String.format("%s.%s", placeHolder, key);
	}
	
	private ObjectNode getColumnNode(AuthoringTemplate at, FormColumn formColumn, String placeHolder) {
		
		ObjectNode columnNode = this.objectMapper.createObjectNode();
		columnNode.put("type", "div");
		columnNode.put("displayFlex", true);
		columnNode.put("flex-direction", "column");
		columnNode.put("fxFlex", formColumn.getFxFlex());

		ArrayNode fieldNodes = this.objectMapper.createArrayNode();
		for (String name : formColumn.getFormControls()) {			
			FormControl formControl = at.getElements().get(name);
			formControl = (formControl == null) ? at.getProperties().get(name) : formControl;
			Optional<FieldLayout> customeFileLayout = this.getCustomeFileLayout(
					formColumn,
					name);
			if (customeFileLayout.isPresent()) {
				ArrayNode layoutNodes = null;
				if (customeFileLayout.get().isMultiple()) {
					ObjectNode fieldNode = this.objectMapper.createObjectNode();
					fieldNode.put("type", "array");
					fieldNode.put("title", name);
					fieldNodes.add(fieldNode);					
					layoutNodes = this.objectMapper.createArrayNode();
					fieldNode.set("items", layoutNodes);
				} else {
					layoutNodes = fieldNodes;
				}
				if (customeFileLayout.get().getFieldLayouts() == null || customeFileLayout.get().getFieldLayouts().length == 0) {
					FieldLayout fieldLayout = customeFileLayout.get();
					ObjectNode fieldNode = this.objectMapper.createObjectNode();
					if (StringUtils.hasText(fieldLayout.getTitle())) {
						fieldNode.put("title", fieldLayout.getTitle());
					} else {
						fieldNode.put("notitle", "true");
					}
					fieldNode.put("key", this.getLayoutFieldKey(fieldLayout.getKey(), placeHolder));
					if (fieldLayout.isMultiple()) {
						fieldNode.put("type", "array");
						ArrayNode itemsNode = this.objectMapper.createArrayNode();
						fieldNode.set("items", itemsNode);
						itemsNode.add(fieldLayout.getItems());
					} 
					fieldNodes.add(fieldNode);
				} else {
					for (FieldLayout fieldLayout: customeFileLayout.get().getFieldLayouts()) {
						ObjectNode fieldNode = this.objectMapper.createObjectNode();
						if (StringUtils.hasText(fieldLayout.getTitle())) {
							fieldNode.put("title", fieldLayout.getTitle());
						} else {
							fieldNode.put("notitle", "true");
						}
						fieldNode.put("key", this.getLayoutFieldKey(fieldLayout.getName(), placeHolder));
						if (fieldLayout.isMultiple()) {
							fieldNode.put("type", "array");
							ArrayNode itemsNode = this.objectMapper.createArrayNode();
							fieldNode.set("items", itemsNode);
							itemsNode.add(fieldLayout.getItems());
						} 
						layoutNodes.add(fieldNode);
					}
				}
			} else {
				ObjectNode fieldNode = this.objectMapper.createObjectNode();
				fieldNodes.add(fieldNode);
				fieldNode.put("key", this.getLayoutFieldKey(formControl.getName(), placeHolder));
				// fieldNode.put("type", formControl.getDataType());
				if (StringUtils.hasText(formControl.getTitle())) {
					fieldNode.put("title", formControl.getTitle());
				} else {
					fieldNode.put("notitle", true);
				}
				// TODO: others
			}
		}
		columnNode.set("items", fieldNodes);
		return columnNode;
	}

	private ObjectNode getRowNode(AuthoringTemplate at, FormRow formRow, String placeHolder) {
		ObjectNode rowNode = this.objectMapper.createObjectNode();
		rowNode.put("type", "flex");
		rowNode.put("flex-flow", "row wrap");
		ArrayNode columnNodes = this.objectMapper.createArrayNode();
		for (FormColumn column : formRow.getColumns()) {
			ObjectNode columnNode = this.getColumnNode(at, column, placeHolder);
			columnNodes.add(columnNode);
		}
		// { , , "items": [ "first_name", "last_name" ] },
		rowNode.set("items", columnNodes);
		return rowNode;
	}

	private ArrayNode toFormLayoutNode(AuthoringTemplate at) {
		ArrayNode formNode = this.objectMapper.createArrayNode();
		if (at.getPropertyRow() != null) {
			formNode.add(this.getRowNode(at, at.getPropertyRow(), "properties"));
		}
		for (BaseFormGroup formGroup : at.getElementGroups()) {
			if (formGroup instanceof FormRow) {
				ObjectNode rowNode = this.getRowNode(at, (FormRow) formGroup, "elements");
				formNode.add(rowNode);
			}

			if (formGroup instanceof FormRows) {
				for (FormRow formRow : ((FormRows) formGroup).getRows()) {
					ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow, "elements");
					formNode.add(rowNode);
				}
			}

			if (formGroup instanceof FormTabs) {
				ObjectNode tabsNode = this.objectMapper.createObjectNode();
				tabsNode.put("type", "tabs");
				ArrayNode tabArrayNode = this.objectMapper.createArrayNode();
				for (FormTab formTab : ((FormTabs) formGroup).getTabs()) {
					ObjectNode tabNode = this.objectMapper.createObjectNode();
					tabNode.put("title", formTab.getTabTitle());
					ArrayNode tabItemNodes = this.objectMapper.createArrayNode();
					for (BaseFormGroup formRow : formTab.getFormGroups()) {
						ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow, "elements");
						tabItemNodes.add(rowNode);
					}
					tabNode.set("items", tabItemNodes);
					tabArrayNode.add(tabNode);
				}
				tabsNode.set("tabs", tabArrayNode);
				formNode.add(tabsNode);
			}

			if (formGroup instanceof FormSteps) {
				ObjectNode stepsNode = this.objectMapper.createObjectNode();
				stepsNode.put("type", "stepper");
				ArrayNode stepArrayNode = this.objectMapper.createArrayNode();
				for (FormStep formStep : ((FormSteps) formGroup).getSteps()) {
					ObjectNode stepNode = this.objectMapper.createObjectNode();
					stepNode.put("title", formStep.getStepTitle());
					ArrayNode stepItemNodes = this.objectMapper.createArrayNode();
					for (BaseFormGroup formRow : formStep.getFormGroups()) {
						ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow, "elements");
						stepItemNodes.add(rowNode);
					}
					stepNode.set("items", stepItemNodes);
					stepArrayNode.add(stepNode);
				}
				stepsNode.set("steps", stepArrayNode);
				formNode.add(stepsNode);
			}
			// at.getFormControls()
			// type ace, enum

		}
		return formNode;
	}

	private ObjectNode toJsonFormField(RestPropertyType restProperty) {
		ObjectNode objectNode = this.objectMapper.createObjectNode();
		if (restProperty.isMultiple()) {
			objectNode.put("type", "array");
			ObjectNode itemNode = this.objectMapper.createObjectNode();
			itemNode.put("type", "string");//restProperty.getRequiredType());
			objectNode.set("items", itemNode);
		} else {
			objectNode.put("type", "string");//restProperty.getRequiredType());
		}
		return objectNode;
	}

	private ObjectNode toTypeDefinition(HttpServletRequest request, String repository, String workspace,
			RestChildType restChildType) throws RepositoryException {
		return this.toTypeDefinition(request, repository, workspace, restChildType.getRequiredPrimaryTypeNames()[0],
				"*".equals(restChildType.getName()));
	}

	private String fieldNameFromNodeTypeName(String typeName) {
		return typeName.startsWith("bpw:") ? typeName.substring("bpw:".length()) : typeName;
	}

	private ObjectNode toTypeDefinition(HttpServletRequest request, String repository, String workspace,
			String dataType, boolean multiple) throws RepositoryException {
		String baseUrl = RestHelper.repositoryUrl(request);
		RestNodeType restNodeType = nodeTypeHandler.getNodeType(baseUrl, repository, workspace, dataType);

		ObjectNode definition = this.objectMapper.createObjectNode();
		ObjectNode itemsNode;

		if (multiple) {
			definition.put("type", "array");
			itemsNode = this.objectMapper.createObjectNode();
			itemsNode.put("type", "object");
			definition.set("items", itemsNode);
		} else {
			definition.put("type", "object");
			itemsNode = definition;
		}

		ObjectNode properties = this.objectMapper.createObjectNode();
		itemsNode.set("properties", properties);
		for (RestPropertyType restProperty : restNodeType.getPropertyTypes()) {
			ObjectNode fieldNode = this.toJsonFormField(restProperty);
			String fieldName = this.fieldNameFromNodeTypeName(restProperty.getName());
			properties.set(fieldName, fieldNode);
		}
		List<RestChildType> childTypes = restNodeType.getChildTypes();
		if (childTypes != null && childTypes.size() > 0) {
			for (RestChildType restChildType : childTypes) {
				String fieldName = "*".equals(restChildType.getName())
						? this.fieldNameFromNodeTypeName(restChildType.getDeclaringNodeTypeName())
						: restChildType.getName();
				ObjectNode childType = this.toTypeDefinition(request, repository, workspace, restChildType);
				properties.set(fieldName, childType);
			}
		}
		return definition;
		
	}
	
	private void popluateFormControls(
			HttpServletRequest request, 
			String repository, 
			String workspace,
			ObjectNode properties,
			ObjectNode definitions,
			Map<String, FormControl> formControls) throws RepositoryException {
		
		for (String key : formControls.keySet()) {
			FormControl formControl = formControls.get(key);
			if ("keyValue".equals(formControl.getControlName())) {
				ObjectNode definition = this.toTypeDefinition(request, repository, workspace, "bpw:keyValues", false);
				definitions.set("keyValues", definition);
			} else if ("customField".equals(formControl.getControlName())) {
				ObjectNode definition = this.toTypeDefinition(request, repository, workspace, formControl.getDataType(),
						false);
				String fieldName = this.fieldNameFromNodeTypeName(formControl.getDataType());
				definitions.set(fieldName, definition);
			}
			ObjectNode objectNode = this.toPropertyNode(formControl, definitions);
			properties.set(key, objectNode);
		}
	}
	
	private JsonForm toJsonForm(HttpServletRequest request, String repository, 
			String workspace, AuthoringTemplate at) throws WcmRepositoryException {
		try {
			JsonForm jsonForm = new JsonForm();
			jsonForm.setRepository(repository);
			jsonForm.setWorkspace(workspace);
			jsonForm.setLibrary(at.getLibrary());
			jsonForm.setResourceType(at.getName());
			ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
			ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();
			jsonNode.set("schema", schemaNode);
			schemaNode.put("type", "object");

			//TODO: 
			if (StringUtils.hasText(at.getName())) {
				schemaNode.put("name", at.getName());
			}
			
			if (StringUtils.hasText(at.getTitle())) {
				schemaNode.put("title", at.getTitle());
			}
	
			if (StringUtils.hasText(at.getDescription())) {
				schemaNode.put("description", at.getDescription());
			}
	
			ObjectNode properties = this.objectMapper.createObjectNode();
			ObjectNode definitions = this.objectMapper.createObjectNode();
	
			ObjectNode propertyPropertiesNode = this.objectMapper.createObjectNode();
			ObjectNode elementPropertiesNode = this.objectMapper.createObjectNode();
			propertyPropertiesNode.put("type", "object");
			elementPropertiesNode.put("type", "object");
			
			ObjectNode propertyProperties = this.objectMapper.createObjectNode();
			propertyPropertiesNode.set("properties", propertyProperties);
			ObjectNode elementProperties = this.objectMapper.createObjectNode();
			elementPropertiesNode.set("properties", elementProperties);
			
			properties.set("properties", propertyPropertiesNode);
			properties.set("elements", elementPropertiesNode);
			
			this.popluateFormControls(
					request, 
					repository, 
					workspace,
					propertyProperties,
					definitions,
					at.getProperties());
			
			this.popluateFormControls(
					request, 
					repository, 
					workspace,
					elementProperties,
					definitions,
					at.getElements());
			
			// properties.addAll(propertyNodes);
			schemaNode.set("properties", properties);
			schemaNode.set("definitions", definitions);
			String requiredElements[] = at.getElements().entrySet().stream().map(entry -> entry.getValue())
					.filter(formControl -> formControl.isMandatory()).map(formControl -> formControl.getName())
					.toArray(String[]::new);
	
			String requiredProperties[] = at.getProperties().entrySet().stream().map(entry -> entry.getValue())
					.filter(formControl -> formControl.isMandatory()).map(formControl -> formControl.getName())
					.toArray(String[]::new);
			String required[] = Stream.concat(Arrays.stream(requiredElements), Arrays.stream(requiredProperties))
					.toArray(String[]::new);
			if (required != null && required.length > 0) {
				ArrayNode reqiuriedNode = this.objectMapper.createArrayNode();
				for (String name : required) {
					reqiuriedNode.add(name);
				}
				schemaNode.set("required", reqiuriedNode);
			}
	
			jsonNode.set("layout", this.toFormLayoutNode(at));
			// "params" node
			// "validate": false
	
			// data: object to populate the form with default or previously submitted values
			// options: object to set any global options for the form
			// widgets: object to add custom widgets
			// language: string to set the error message language (currently supports 'en'
			// and 'fr')
			// framework: string or object to set which framework to use
			jsonForm.setFormSchema(jsonNode);
	
			return jsonForm;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

//	private void addSidePaneNode(Node sidenavNode, SidePane sidenav) throws RepositoryException {
//		sidenavNode.setProperty("bpw:isLeft", sidenav.isLeft());
//		sidenavNode.setProperty("bpw:width", sidenav.getWidth());
//		this.addResourceViewers(sidenavNode, sidenav.getViewers());
//	}

//	private void addPageLayoutNodes(Node pageLayoutNode, LayoutRow[] rows) throws RepositoryException {
//		int rowCount = 0;
//		for (LayoutRow row : rows) {
//			Node rowNode = pageLayoutNode.addNode("row" + rowCount++, "bpw:layoutRow");
//			int columnCount = 0;
//			for (LayoutColumn column : row.getColumns()) {
//				Node columnNode = rowNode.addNode("column" + columnCount++, "bpw:layoutColumn");
//				columnNode.setProperty("bpw:width", column.getWidth());
//				this.addResourceViewers(columnNode, column.getViewers());
//			}
//		}
//	}

//	private void addResourceViewers(Node restNode, ResourceViewer viewers[]) throws RepositoryException {
//		int viewerCount = 0;
//		for (ResourceViewer viewer : viewers) {
//			Node viewerNode = restNode.addNode("viewer" + viewerCount++, "bpw:resourceViewer");
//			viewerNode.setProperty("bpw:renderTemplayeName", viewer.getRenderTemplate());
//			viewerNode.setProperty("bpw:title", viewer.getTitle());
//			if (viewer.getContentPath() != null && viewer.getContentPath().length > 0) {
//				viewerNode.setProperty("bpw:contentPath", viewer.getContentPath());
//			}
//		}
//	}

//	private void addSteps(Node stepsNode, FormStep[] steps) throws RepositoryException {
//		for (FormStep step : steps) {
//			Node stepNode = stepsNode.addNode(step.getStepName(), "bpw:formStep");
//			String stepTitle = StringUtils.hasText(step.getStepTitle()) ? step.getStepTitle() : step.getStepName();
//			if (StringUtils.hasText(stepTitle)) {
//				stepNode.setProperty("bpw:stepName", stepTitle);
//			}
//			int rowCount = 0;
//			for (BaseFormGroup row : step.getFormGroups()) {
//				this.addRow(stepNode, (FormRow) row, "row" + rowCount++);
//			}
//		}
//	}
//
//	private void addTabs(Node tabsNode, FormTab[] tabs) throws RepositoryException {
//		for (FormTab tab : tabs) {
//			Node tabNode = tabsNode.addNode(tab.getTabName(), "bpw:formTab");
//			String tabTitle = StringUtils.hasText(tab.getTabTitle()) ? tab.getTabTitle() : tab.getTabName();
//			if (StringUtils.hasText(tabTitle)) {
//				tabNode.setProperty("bpw:tabName", tabTitle);
//			}
//			int rowCount = 0;
//			for (BaseFormGroup row : tab.getFormGroups()) {
//				this.addRow(tabNode, (FormRow) row, "row" + rowCount++);
//			}
//		}
//	}
//
//	private void addRows(Node rowsNode, FormRow[] rows) throws RepositoryException {
//		int rowCount = 0;
//		for (FormRow row : rows) {
//			this.addRow(rowsNode, (FormRow) row, "row" + rowCount++);
//		}
//	}

//	private void addRow(Node groupNode, FormRow row, String rowName) throws RepositoryException {
//		
//		Node rowNode = groupNode.addNode(rowName, "bpw:formRow");
//		String rowTitle = StringUtils.hasText(row.getRowTitle()) ? row.getRowTitle() : row.getRowName();
//		if (StringUtils.hasText(rowTitle)) {
//			rowNode.setProperty("bpw:rowName", rowTitle);
//		}
//		int colCount = 0;
//		for (FormColumn col : row.getColumns()) {
//			String columnName = StringUtils.hasText(col.getId()) ? col.getId() : "column" + colCount;
//			this.addColumn(rowNode, col, columnName);
//		}
//	}

//	private void addColumn(Node rowNode, FormColumn column, String columnNameName) throws RepositoryException {
//		Node columnNode = rowNode.addNode(columnNameName, "bpw:formColumn");
//		columnNode.setProperty("bpw:fxFlex", column.getFxFlex());
//		columnNode.setProperty("bpw:id", columnNameName);
//		if (column.getFormControls() != null && column.getFormControls().length > 0) {
//			columnNode.setProperty("bpw:fieldNames", column.getFormControls());
//		}
//	}
//
//	private void addControl(Node elementFolder, FormControl control) throws RepositoryException {
//		// Node controlNode = elementFolder.addNode(control.getName(),
//		// "bpw:formControl");
//		Node controlNode = elementFolder.addNode(control.getName(), "bpw:formControl");
//		if (StringUtils.hasText(control.getTitle())) {
//			controlNode.setProperty("bpw:title", control.getTitle());
//		}
//
//		if (StringUtils.hasText(control.getFieldPath())) {
//			controlNode.setProperty("bpw:fieldPath", control.getFieldPath());
//		}
//		
//		if (StringUtils.hasText(control.getControlName())) {
//			controlNode.setProperty("bpw:controlName", control.getControlName());
//		}
//
//		if ((control.getValues() != null) && (control.getValues().length > 0)) {
//			controlNode.setProperty("bpw:value", control.getValues());
//		}
//
//		if ((control.getOptions() != null) && (control.getOptions().length > 0)) {
//			controlNode.setProperty("bpw:options", control.getOptions());
//		}
//
//		if (StringUtils.hasText(control.getDefaultValue())) {
//			controlNode.setProperty("bpw:defaultValue", control.getDefaultValue());
//		}
//
//		if (StringUtils.hasText(control.getHint())) {
//			controlNode.setProperty("bpw:hint", control.getHint());
//		}
//
//		if (StringUtils.hasText(control.getDataType())) {
//			controlNode.setProperty("bpw:dataType", control.getDataType());
//		}
//
//		if (StringUtils.hasText(control.getRelationshipType())) {
//			controlNode.setProperty("bpw:relationshipType", control.getRelationshipType());
//		}
//
//		if (StringUtils.hasText(control.getRelationshipCardinality())) {
//			controlNode.setProperty("bpw:relationshipCardinality", control.getRelationshipCardinality());
//		}
//
//		if (StringUtils.hasText(control.getValditionRegEx())) {
//			controlNode.setProperty("bpw:valditionRegEx", control.getValditionRegEx());
//		}
//
//		controlNode.setProperty("bpw:mandatory", control.isMandatory());
//		controlNode.setProperty("bpw:userSearchable", control.isUserSearchable());
//		controlNode.setProperty("bpw:systemIndexed", control.isSystemIndexed());
//		controlNode.setProperty("bpw:showInList", control.isShowInList());
//		controlNode.setProperty("bpw:unique", control.isUnique());
//	}

	private Theme toThemeWithLibrary(RestNode node, String repository, String workspace) {
		Theme themeWithLibrary = new Theme();
		themeWithLibrary.setRepositoryName(repository);
		themeWithLibrary.setWorkspace(workspace);
		themeWithLibrary.setLibrary(node.getName());
		return themeWithLibrary;
	}

	private ContentAreaLayout toContentAreaLayoutWithLibrary(RestNode node, String repository, String workspace) {
		ContentAreaLayout contentAreaLayoutWithLibrary = new ContentAreaLayout();
		contentAreaLayoutWithLibrary.setRepository(repository);
		contentAreaLayoutWithLibrary.setWorkspace(workspace);
		contentAreaLayoutWithLibrary.setLibrary(node.getName());
		return contentAreaLayoutWithLibrary;
	}

	private RenderTemplate toRenderTemplateWithLibrary(RestNode node, String repository, String workspace) {
		RenderTemplate rtWithLibrary = new RenderTemplate();
		rtWithLibrary.setRepository(repository);
		rtWithLibrary.setWorkspace(workspace);
		rtWithLibrary.setLibrary(node.getName());
		return rtWithLibrary;
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

	private RenderTemplate toRenderTemplate(RestNode node, String repository, String workspace, String library) {
		RenderTemplate rt = new RenderTemplate();
		rt.setRepository(repository);
		rt.setWorkspace(workspace);
		rt.setLibrary(library);
		rt.setName(node.getName());
		this.wcmUtils.resolveResourceNode(rt, node);
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:code".equals(property.getName())) {
				rt.setCode(property.getValues().get(0));
			} else if ("bpw:preloop".equals(property.getName())) {
				rt.setPreloop(property.getValues().get(0));
			} else if ("bpw:postloop".equals(property.getName())) {
				rt.setPostloop(property.getValues().get(0));
			} else if ("bpw:maxEntries".equals(property.getName())) {
				rt.setMaxEntries(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:note".equals(property.getName())) {
				rt.setNote(property.getValues().get(0));
			} else if ("bpw:resourceName".equals(property.getName())) {
				rt.setResourceName(property.getValues().get(0));
			} else if ("bpw:isQuery".equals(property.getName())) {
				rt.setQuery(Boolean.parseBoolean(property.getValues().get(0)));
			} 
		}
		
		List<RenderTemplateLayoutRow> rows = new ArrayList<>();
		for (RestNode rowNode: node.getChildren()) {
			if (this.wcmUtils.checkNodeType(rowNode, "bpw:RenderTemplateLayoutRow")) {
				RenderTemplateLayoutRow row = new RenderTemplateLayoutRow();
				rows.add(row);
				List<RenderTemplateLayoutColumn> columns = new ArrayList<>();
				for (RestNode columnNode: rowNode.getChildren()) {
					if (this.wcmUtils.checkNodeType(columnNode, "bpw:RenderTemplateLayoutColumn")) {
						RenderTemplateLayoutColumn column = new RenderTemplateLayoutColumn();
						columns.add(column);	
						for (RestProperty property : columnNode.getJcrProperties()) {
							if ("bpw:id".equals(property.getName())) {
								column.setId(property.getValues().get(0));
							} else if ("bpw:widtd".equals(property.getName())) {
								column.setWidth(Integer.parseInt(property.getValues().get(0)));
							} 
						}
						List<ResourceElementRender> elements = new ArrayList<>();
						for (RestNode elementNode: columnNode.getChildren()) {
							if (this.wcmUtils.checkNodeType(elementNode, "bpw:ResourceElementRender")) {
								ResourceElementRender element = new ResourceElementRender();
								elements.add(element);
								element.setName(elementNode.getName());
							}
						}
						column.setElements(elements.toArray(new ResourceElementRender[elements.size()]));
						
					}
				}
				row.setColumns(columns.toArray(new RenderTemplateLayoutColumn[columns.size()]));
			}
		}
		
		if (rows.size() > 0) {
			rt.setRows(rows.toArray(new RenderTemplateLayoutRow[rows.size()]));
		}
		return rt;
	}

	private ContentAreaLayout toContentAreaLayout(RestNode node, ContentAreaLayout layout) {
		ContentAreaLayout contentAreaLayout = new ContentAreaLayout();
		contentAreaLayout.setRepository(layout.getRepository());
		contentAreaLayout.setWorkspace(layout.getWorkspace());
		contentAreaLayout.setLibrary(layout.getLibrary());
		contentAreaLayout.setName(node.getName());
		this.wcmUtils.resolveResourceNode(contentAreaLayout, node);
		for (RestProperty property : node.getJcrProperties()) {
			if (" bpw:contentWidth".equals(property.getName())) {
				contentAreaLayout.setContentWidth(Integer.parseInt(property.getValues().get(0)));
				break;
			} 
		}

		List<LayoutRow> rows = new ArrayList<>();

		node.getChildren().forEach(childNode -> {
			if (this.wcmUtils.checkNodeType(childNode, "bpw:contentAreaSidePanel")) {
				SidePane sidepane = new SidePane();
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("bpw:isLeft".equals(property.getName())) {
						sidepane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
					} else if ("bpw:width".equals(property.getName())) {
						sidepane.setWidth(Integer.parseInt(property.getValues().get(0)));
					}
				}
				sidepane.setViewers(this.resolveResourceViewer(childNode));
				contentAreaLayout.setSidePane(sidepane);
			} else if (this.wcmUtils.checkNodeType(childNode, "bpw:layoutRow")) {
				LayoutRow row = this.resolveLayoutRow(childNode);
				rows.add(row);
			}
		});
		contentAreaLayout.setRows(rows.toArray(new LayoutRow[rows.size()]));
		return contentAreaLayout;
	}

	private LayoutRow resolveLayoutRow(RestNode restNode) {
		LayoutRow row = new LayoutRow();
		LayoutColumn columns[] = restNode.getChildren().stream()
				.filter(node -> this.wcmUtils.checkNodeType(node, "bpw:layoutColumn")).map(this::toLayoutColumn)
				.toArray(LayoutColumn[]::new);
		row.setColumns(columns);
		return row;
	}

	private LayoutColumn toLayoutColumn(RestNode node) {
		LayoutColumn column = new LayoutColumn();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:width".equals(property.getName())) {
				column.setWidth(Integer.parseInt(property.getValues().get(0)));
			}
		}
		column.setViewers(this.resolveResourceViewer(node));
		return column;
	}

	private ResourceViewer[] resolveResourceViewer(RestNode restNode) {
		List<ResourceViewer> viewers = new ArrayList<>();
		
		restNode.getChildren().forEach(viewerNode -> {
			if (this.wcmUtils.checkNodeType(viewerNode, "bpw:resourceViewer")) {
				ResourceViewer viewer = new ResourceViewer();
				for (RestProperty property : viewerNode.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						viewer.setTitle(property.getValues().get(0));
					} else if ("bpw:renderTemplayeName".equals(property.getName())) {
						viewer.setRenderTemplate(property.getValues().get(0));
					} else if ("bpw:contentPath".equals(property.getName())) {
						viewer.setContentPath(property.getValues().toArray(new String[property.getValues().size()]));
					}
				}
				viewers.add(viewer);
			}
		});
		return viewers.toArray(new ResourceViewer[viewers.size()]);
	}

	private ControlFieldMetadata toControlFieldMetaData(RestNode node) {
		ControlFieldMetadata metadata = new ControlFieldMetadata();
		metadata.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				metadata.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:controlType".equals(restProperty.getName())) {
				metadata.setControlType(restProperty.getValues().get(0));
			} else if ("bpw:hintText".equals(restProperty.getName())) {
				metadata.setHintText(restProperty.getValues().get(0));
			} else if ("bpw:required".equals(restProperty.getName())) {
				metadata.setRequired(Boolean.parseBoolean(restProperty.getValues().get(0)));
			} else if ("bpw:readOnly".equals(restProperty.getName())) {
				metadata.setReadonly(Boolean.parseBoolean(restProperty.getValues().get(0)));
			} else if ("bpw:selectOptions".equals(restProperty.getName())) {
				metadata.setSelectOptions(
						restProperty.getValues().toArray(new String[restProperty.getValues().size()]));
			}
		}
		return metadata;
	}

	private ControlField toControlField(RestNode node) {
		ControlField controlField = new ControlField();
		controlField.setName(node.getName());
		for (RestProperty restProperty : node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				controlField.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:icon".equals(restProperty.getName())) {
				controlField.setIcon(restProperty.getValues().get(0));
			}
		}
		ControlFieldMetadata[] controlFieldMetadata = node.getChildren().stream().filter(this::isControlFieldMetaData)
				.map(this::toControlFieldMetaData).toArray(ControlFieldMetadata[]::new);
		controlField.setControlFieldMetaData(controlFieldMetadata);
		return controlField;
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

	private Stream<ContentAreaLayout> getContentAreaLayoutLibraries(String repository, String workspace, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toContentAreaLayoutWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
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

	private Stream<RenderTemplate> getRenderTemplateLibraries(String repository, String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toRenderTemplateWithLibrary(node, repository, workspace));
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

	private Stream<RenderTemplate> getRenderTemplates(RenderTemplate rt, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			RestNode themeNode = (RestNode) this.itemHandler.item(baseUrl, rt.getRepository(), rt.getWorkspace(),
					"/bpwizard/library/" + rt.getLibrary() + "/renderTemplate", 5);
			return themeNode.getChildren().stream().filter(this::isRenderTemplate)
					.map(node -> this.toRenderTemplate(node, rt.getRepository(), rt.getWorkspace(), rt.getLibrary()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<ContentAreaLayout> getContentArealayouts(ContentAreaLayout contentAreaLayout, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode contentAreaLayoutNode = (RestNode) this.itemHandler.item(baseUrl, contentAreaLayout.getRepository(),
					contentAreaLayout.getWorkspace(), "/bpwizard/library/" + contentAreaLayout.getLibrary() + "/contentAreaLayout", 4);
			return contentAreaLayoutNode.getChildren().stream().filter(this::isContentAreaLayout)
					.map(node -> this.toContentAreaLayout(node, contentAreaLayout));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private boolean isControlField(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlField");
	}

	private boolean isSiteArea(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:siteArea");
	}
	
	private boolean isControlFieldMetaData(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlFieldMetaData");
	}

	private boolean isLibrary(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:library");
	}

	private boolean notSystemLibrary(RestNode node) {
		return !"system".equalsIgnoreCase(node.getName());
	}
	
	private boolean isTheme(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:themeType");
	}

	private boolean isRenderTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:renderTemplate");
	}

	private boolean isContentAreaLayout(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:contentAreaLayout");
	}

	private boolean isAuthortingTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:authoringTemplate");
	}

	
	
	private Stream<AuthoringTemplate> getAuthoringTemplateLibraries(String repository, String workspace,
			String baseUrl) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary)
					.map(node -> toAuthoringTemplateWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<AuthoringTemplate> getAuthoringTemplates(AuthoringTemplate at, String baseUrl)
			throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, at.getRepository(), at.getWorkspace(),
					"/bpwizard/library/" + at.getLibrary() + "/authoringTemplate", 7);
			return atNode.getChildren().stream().filter(this::isAuthortingTemplate)
					.map(node -> this.wcmUtils.toAuthoringTemplate(node, at.getRepository(), at.getWorkspace(), at.getLibrary()));
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new WcmRepositoryException(e);
		}
	}

	private AuthoringTemplate toAuthoringTemplateWithLibrary(RestNode node, String repository, String workspace) {
		AuthoringTemplate authoringTemplateWithLibrary = new AuthoringTemplate();
		authoringTemplateWithLibrary.setRepository(repository);
		authoringTemplateWithLibrary.setWorkspace(workspace);
		authoringTemplateWithLibrary.setLibrary(node.getName());
		return authoringTemplateWithLibrary;
	}

	private void resolveWorkflowNode(WorkflowNode workflowNode, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				workflowNode.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				workflowNode.setDescription(property.getValues().get(0));
			} else if ("bpw:publishDate".equals(property.getName())) {
				workflowNode.setPublishDate(property.getValues().get(0));
			} else if ("bpw:expireDate".equals(property.getName())) {
				workflowNode.setExpireDate(property.getValues().get(0));
			} else if ("bpw:workflow".equals(property.getName())) {
				workflowNode.setWorkflow(property.getValues().get(0));
			} else if ("bpw:workflowStage".equals(property.getName())) {
				workflowNode.setWorkflowStage(property.getValues().get(0));
			} else if ("jcr:lockOwner".equals(property.getName())) {
				workflowNode.setLockOwner(property.getValues().get(0));
			} else if ("bpw:currentLifecycleState".equals(property.getName())) {
				workflowNode.setCurrentLifecycleState(property.getValues().get(0));
			}				
		}
	}

	private SiteConfig getSiteConfig(RestNode siteConfigNode) throws RepositoryException {
		SiteConfig siteConfig = new SiteConfig();
		this.wcmUtils.resolveResourceNode(siteConfig, siteConfigNode);
		for (RestProperty property : siteConfigNode.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				siteConfig.setName(property.getValues().get(0));
			} else if ("bpw:colorTheme".equals(property.getName())) {
				siteConfig.setColorTheme(property.getValues().get(0));
			} else if ("bpw:customScrollbars".equals(property.getName())) {
				siteConfig.setCustomScrollbars(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:rootSiteArea".equals(property.getName())) {
				siteConfig.setRootSiteArea(property.getValues().get(0));
			} 
		}
		
		for (RestNode layoutNode: siteConfigNode.getChildren()) {
			if ("layout".equals(layoutNode.getName())) {
				PageLayout layout = new PageLayout();
				siteConfig.setLayout(layout);
				for (RestProperty property : layoutNode.getJcrProperties()) {
					if ("bpw:style".equals(property.getName())) {
						layout.setStyle(property.getValues().get(0));
					} else if ("bpw:width".equals(property.getName())) {
						layout.setWidth(property.getValues().get(0));
					} 
				}
				for (RestNode node: layoutNode.getChildren()) {
					if ("navbar".equals(node.getName())) {
						NavBar navbar = new NavBar();
						layout.setNavbar(navbar);						
						for (RestProperty property : node.getJcrProperties()) {
							if ("folded".equals(property.getName())) {
								navbar.setFolded(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("primaryBackground".equals(property.getName())) {
								navbar.setPrimaryBackground(property.getValues().get(0));
							} else if ("secondaryBackground".equals(property.getName())) {
								navbar.setSecondaryBackground(property.getValues().get(0));
							} else if ("variant".equals(property.getName())) {
								navbar.setVariant(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								navbar.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								navbar.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("toolbar".equals(node.getName())) {
						Toolbar toolbar = new Toolbar();
						layout.setToolbar(toolbar);						
						for (RestProperty property : node.getJcrProperties()) {
							if ("customBackgroundColor".equals(property.getName())) {
								toolbar.setCustomBackgroundColor(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("background".equals(property.getName())) {
								toolbar.setBackground(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								toolbar.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								toolbar.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("footer".equals(node.getName())) {
						Footer footer = new Footer();
						layout.setFooter(footer);				
						for (RestProperty property : node.getJcrProperties()) {
							if ("customBackgroundColor".equals(property.getName())) {
								footer.setCustomBackgroundColor(Boolean.getBoolean(property.getValues().get(0)));
							} else if ("background".equals(property.getName())) {
								footer.setBackground(property.getValues().get(0));
							} else if ("position".equals(property.getName())) {
								footer.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								footer.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					} else if ("sidePanel".equals(node.getName())) {
						SidePanel sidePanel = new SidePanel();
						layout.setSidePanel(sidePanel);				
						for (RestProperty property : node.getJcrProperties()) {
							if ("position".equals(property.getName())) {
								sidePanel.setPosition(property.getValues().get(0));
							} else if ("hidden".equals(property.getName())) {
								sidePanel.setHidden(Boolean.getBoolean(property.getValues().get(0)));
							} 
						}
					}
				}
				break;
			} 
		}
		return siteConfig;
	}
	
	private Map<String, SiteArea> getSiteAreas(
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea,
			String baseUrl
			) throws RepositoryException {
		RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
				String.format("/bpwizard/library/%s/%s", library, rootSiteArea), 5);
		Map<String, SiteArea> siteAreas = new HashMap<>();
		for (RestNode node :saNode.getChildren()) {
			if (this.isSiteArea(node)) {
				this.toSiteArea(node, siteAreas);
			}
		}
		return siteAreas;
	}
	
	private Navigation[] getNavigations(
			String baseUrl,
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea
			) throws RepositoryException {
		RestNode siteArea = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
				String.format("/bpwizard/library/%s/%s", library, rootSiteArea), 3);
		
		Navigation[] navigation = siteArea.getChildren().stream().filter(this::isSiteArea)
				.map(node -> this.toNavigation(node)).toArray(Navigation[]::new);
		return navigation;
	}
	
	private SiteArea toSiteArea(RestNode saNode, Map<String, SiteArea> siteAreas) {
		SiteArea sa = new SiteArea();
		for (RestProperty property : saNode.getJcrProperties()) {
			if ("bpw:navigationId".equals(property.getName())) {
				sa.setNavigationId(property.getValues().get(0));
			} else if ("bpw:navigationType".equals(property.getName())) {
				sa.setNavigationType(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				sa.setTitle(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				sa.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				sa.setIcon(property.getValues().get(0));
			} else if ("bpw:url".equals(property.getName())) {
				sa.setUrl(property.getValues().get(0));
			} else if ("bpw:name".equals(property.getName())) {
				sa.setName(property.getValues().get(0));
			} else if ("bpw:friendlyURL".equals(property.getName())) {
				sa.setFriendlyURL(property.getValues().get(0));
			} else if ("bpw:sorderOrder".equals(property.getName())) {
				sa.setSorderOrder(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:sorderOrder".equals(property.getName())) {
				sa.setShowOnMenu(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:allowedFileExtension".equals(property.getName())) {
				sa.setAllowedFileExtension(property.getValues().get(0));
			} else if ("bpw:allowedArtifactTypes".equals(property.getName())) {
				sa.setAllowedArtifactTypes(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				sa.setDescription(property.getValues().get(0));
			} else if ("bpw:defaultContent".equals(property.getName())) {
				sa.setDefaultContent(property.getValues().get(0));
			} else if ("bpw:contentAreaLayout".equals(property.getName())) {
				sa.setContentAreaLayout(property.getValues().get(0));
			} else if ("bpw:siteConfig".equals(property.getName())) {
				sa.setSiteConfig(property.getValues().get(0));
			} else if ("bpw:cacheTTL".equals(property.getName())) {
				sa.setCacheTTL(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:securePage".equals(property.getName())) {
				sa.setSecurePage(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:function".equals(property.getName())) {
				sa.setFunction(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				sa.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				sa.setIcon(property.getValues().get(0));
			} else if ("bpw:classes".equals(property.getName())) {
				sa.setClasses(property.getValues().get(0));
			} else if ("bpw:exactMatch".equals(property.getName())) {
				sa.setExactMatch(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:externalUrl".equals(property.getName())) {
				sa.setExternalUrl(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:openInNewTab".equals(property.getName())) {
				sa.setOpenInNewTab(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("jcr:lockOwner".equals(property.getName())) {
				sa.setLockOwner(property.getValues().get(0));
			}    
			/*
			+ bpw:metaData (bpw:keyValues)
			+ bpw:searchData (bpw:pageSearchData)	
			*/		
		}
		
		siteAreas.put(sa.getUrl().replace("/", "~"), sa);
		for (RestNode node :saNode.getChildren()) {
			if (this.wcmUtils.checkNodeType(node, "bpw:navigationBadge")) {
				NavigationBadge badge = new NavigationBadge();
				for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						badge.setTitle(property.getValues().get(0));
					} else if ("bpw:translate".equals(property.getName())) {
						badge.setTranslate(property.getValues().get(0));
					} else if ("bpw:bg".equals(property.getName())) {
						badge.setBg(property.getValues().get(0));
					} else if ("bpw:fg".equals(property.getName())) {
						badge.setFg(property.getValues().get(0));
					} 
				}
				sa.setBadge(badge);	
			} else if (this.wcmUtils.checkNodeType(node, "bpw:keyValues")) {
				keyValues metadata = new keyValues();
				KeyValue[] keyValues = node.getChildren().stream().filter(n -> this.wcmUtils.checkNodeType(n, "bpw:keyValue"))
						.map(n -> this.toKeyValue(n)).toArray(KeyValue[]::new);
				metadata.setKeyValue(keyValues);
				sa.setMetadata(metadata);
			} else if (this.wcmUtils.checkNodeType(node, "bpw:pageSearchData")) {
				SearchData searchData = new SearchData();
				for (RestProperty property : node.getJcrProperties()) {
					if ("description".equals(property.getName())) {
						searchData.setDescription(property.getValues().get(0));
					} else if ("keywords".equals(property.getName())) {
						searchData.setKeywords(property.getValues().toArray(new String[property.getValues().size()]));
					}
				}
				sa.setSearchData(searchData);
			} else if (this.wcmUtils.checkNodeType(node, "bpw:siteAreaLayout")) {
				SiteAreaLayout siteAreaLayout = new SiteAreaLayout();
				List<LayoutRow> rows = new ArrayList<>();
				for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:contentWidth".equals(property.getName())) {
						siteAreaLayout.setContentWidth(Integer.parseInt(property.getValues().get(0)));
						break;
					} 
				}
				node.getChildren().forEach(childNode -> {
					if (this.wcmUtils.checkNodeType(childNode, "bpw:contentAreaSidePanel")) {
						SidePane sidepane = new SidePane();
						for (RestProperty property : childNode.getJcrProperties()) {
							if ("bpw:isLeft".equals(property.getName())) {
								sidepane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
							} else if ("bpw:width".equals(property.getName())) {
								sidepane.setWidth(Integer.parseInt(property.getValues().get(0)));
							}
						}
						sidepane.setViewers(this.resolveResourceViewer(childNode));
						siteAreaLayout.setSidePane(sidepane);
					} else if (this.wcmUtils.checkNodeType(childNode, "bpw:layoutRow")) {
						LayoutRow row = this.resolveLayoutRow(childNode);
						rows.add(row);
					}
				});
				siteAreaLayout.setRows(rows.toArray(new LayoutRow[rows.size()]));
				sa.setSiteAreaLayout(siteAreaLayout);
			} else if (this.isSiteArea(node)) {
				this.toSiteArea(node, siteAreas);
			}
		}
		return sa;
	}
	
	private KeyValue toKeyValue(RestNode node) {
		KeyValue keyValue = new KeyValue();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				keyValue.setName(property.getValues().get(0));
			} else if ("bpw:value".equals(property.getName())) {
				keyValue.setValue(property.getValues().get(0));
			}
		}
		return keyValue;
	}
	
	private NavigationItem toNavigation(RestNode siteArea) {
		Navigation navigation = new Navigation();
		for (RestProperty property : siteArea.getJcrProperties()) {
			if ("bpw:navigationId".equals(property.getName())) {
				navigation.setId(property.getValues().get(0));
			} else if ("bpw:navigationType".equals(property.getName())) {
				navigation.setType(NavigationType.valueOf(property.getValues().get(0)));
			} else if ("bpw:title".equals(property.getName())) {
				navigation.setTitle(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				navigation.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				navigation.setIcon(property.getValues().get(0));
			} else if ("bpw:url".equals(property.getName())) {
				navigation.setUrl(property.getValues().get(0));
			}
		}
		for (RestNode node: siteArea.getChildren()) {
			if (this.wcmUtils.checkNodeType(node, "bpw:navigationBadge")) {
				NavigationBadge badge = new NavigationBadge();
				for (RestProperty property : node.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						badge.setTitle(property.getValues().get(0));
					} else if ("bpw:translate".equals(property.getName())) {
						badge.setTranslate(property.getValues().get(0));
					} else if ("bpw:bg".equals(property.getName())) {
						badge.setBg(property.getValues().get(0));
					} else if ("bpw:fg".equals(property.getName())) {
						badge.setFg(property.getValues().get(0));
					} 
				}
				navigation.setBadge(badge);
			}
		}
		NavigationItem[] navigationItems = siteArea.getChildren().stream().filter(this::isSiteArea)
				.map(node -> this.toNavigationItem(node, 1)).toArray(NavigationItem[]::new);
		navigation.setChildren(navigationItems);
		
		return navigation;
	}
	
	private NavigationItem toNavigationItem(RestNode siteArea, int level) {
		NavigationItem navigation = new NavigationItem();
		for (RestProperty property : siteArea.getJcrProperties()) {
			if ("bpw:navigationId".equals(property.getName())) {
				navigation.setId(property.getValues().get(0));
			} else if ("bpw:navigationType".equals(property.getName())) {
				navigation.setType(NavigationType.valueOf(property.getValues().get(0)));
			} else if ("bpw:title".equals(property.getName())) {
				navigation.setTitle(property.getValues().get(0));
			} else if ("bpw:translate".equals(property.getName())) {
				navigation.setTranslate(property.getValues().get(0));
			} else if ("bpw:icon".equals(property.getName())) {
				navigation.setIcon(property.getValues().get(0));
			} else if ("bpw:url".equals(property.getName())) {
				navigation.setUrl(property.getValues().get(0));
			}
		}
		if (level <= 2) {
			NavigationItem[] navigationItems = siteArea.getChildren().stream().filter(this::isSiteArea)
					.map(node -> this.toNavigationItem(node, level + 1)).toArray(NavigationItem[]::new);
			navigation.setChildren(navigationItems);
		}
		return navigation;
	}
	
	private String getJcrType(RestNode node) {
		String jcrType = "";
		for (RestProperty property: node.getJcrProperties()) {
			if ("bpw:jcrType".equals(property.getName())) {
				jcrType = property.getValues().get(0);
				break;
			}
		}
		return jcrType;
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
	
	private void doLock(
			String repository,
			String workspace, 
			String absPath) throws RepositoryException {
		
		javax.jcr.lock.LockManager lm = this.repositoryManager.getSession(repository, workspace).getWorkspace().getLockManager();
		if (!lm.isLocked(absPath)) {
			lm.lock(absPath, true, false, Long.MAX_VALUE, ModeshapeUtils.getUserName());
		}
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
    
//	private void doCreateContentItem(ContentItem contentItem, 
//			HttpServletRequest request)
//			throws WcmRepositoryException {
//		if (logger.isDebugEnabled()) {
//			logger.traceEntry();
//		}
//		
//		try {
//			String repositoryName = contentItem.getRepository();
//			String workspaceName = contentItem.getWorkspace();
//	
//			Session session = repositoryManager.getSession(repositoryName, workspaceName);
//			Node parentFolder = session.getNode("/" + contentItem.getNodePath());
//			
//			Map<String, String> properties = contentItem.getProperties();
//			String name = properties.get("name"); 
//			String title = properties.get("title");
//			String description = properties.get("description");
//			String workflow = properties.get("workflow"); 
//			
//			
//			Node contentNode = parentFolder.addNode(name, "bpw:content");
//	
//			//contentNode.setProperty("bpw:name", name);
//			contentNode.setProperty("bpw:authoringTemplate", contentItem.getAuthoringTemplate());
//			if (StringUtils.hasText(workflow)) {
//				contentNode.setProperty("bpw:workflow", workflow);
//			}
//			contentNode.setProperty("bpw:title", StringUtils.hasText(title) ? title : name);
//			if (StringUtils.hasText(description)) {
//				contentNode.setProperty("bpw:description", description);
//			}
//			
//			Node elementsNode = contentNode.addNode("contentElements", "bpw:contentElementFolder");
//			Map<String, String> contentElements = contentItem.getElements();
//			for (String key: contentElements.keySet()) {
//				Node contentElementNode = elementsNode.addNode(key, "bpw:contentElement");
//				contentElementNode.setProperty("bpw:multiple", false);
//				contentElementNode.setProperty("bpw:value", new String[] {contentElements.get(key)});
//			}
//			
//			Node propertiesNode = contentNode.addNode("contentProperties", "bpw:propertyElementFolder");
//			Map<String, String> propertyElements = contentItem.getElements();
//			for (String key: propertyElements.keySet()) {
//				Node contentElementNode = propertiesNode.addNode(key, "bpw:contentElement");
//				contentElementNode.setProperty("bpw:multiple", false);
//				contentElementNode.setProperty("bpw:value", contentElements.get(key));
//			}
//			session.save();
//			if (logger.isDebugEnabled()) {
//				logger.traceExit();
//			}
//	
//			
//		} catch (WcmRepositoryException e ) {
//			throw e;
//		} catch (Throwable t) {
//			throw new WcmRepositoryException(t);
//		}
//	}
}
