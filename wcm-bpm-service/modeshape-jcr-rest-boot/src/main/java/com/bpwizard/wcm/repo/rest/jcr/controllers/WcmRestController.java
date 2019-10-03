package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.bpwizard.wcm.repo.rest.jcr.model.PageLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceNode;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceViewer;
import com.bpwizard.wcm.repo.rest.jcr.model.SearchData;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePane;
import com.bpwizard.wcm.repo.rest.jcr.model.SidePanel;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteArea;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteAreaLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteConfig;
import com.bpwizard.wcm.repo.rest.jcr.model.SiteNavigatorFilter;
import com.bpwizard.wcm.repo.rest.jcr.model.Theme;
import com.bpwizard.wcm.repo.rest.jcr.model.Toolbar;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmLibrary;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNode;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmOperation;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmRepository;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmSystem;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmWorkspace;
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
	public static final String BASE_URI = "/wcm/api/rest";

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

	private ObjectMapper objectMapper = new ObjectMapper();
	
    //http://localhost:8080/wcm/api/rest/wcmSystem/bpwizard/default/camunda/bpm
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
			Session session = repositoryManager.getSession(repository, workspace);
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
			wcmSystem.setAuthoringTemplates(this.getAuthoringTemplate(repository, workspace, request));
			Node siteConfigNode = session.getNode(String.format("/bpwizard/library/%s/siteConfig/%s", library, siteConfigName));
			SiteConfig siteConfig = this.getSiteConfig(siteConfigNode);
			siteConfig.setRepository(repository);
			siteConfig.setWorkspace(workspace);
			siteConfig.setLibrary(library);
			String rootSiteArea = siteConfig.getRootSiteArea();
			Navigation[] navigations = this.getNavigations(request, repository, workspace, library, rootSiteArea);
			wcmSystem.setNavigations(navigations);
			wcmSystem.setSiteConfig(siteConfig);
			Map<String, SiteArea> siteAreas = this.getSiteAreas(repository, workspace, library, rootSiteArea, request);
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
			RestRepositories restRepositories = this.serverHandler.getRepositories(request);
			WcmRepository[] wcmRepositories = restRepositories.getRepositories().stream()
					.map(restReoisitory -> this.toWcmRepository(restReoisitory, request)).toArray(WcmRepository[]::new);
			
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
			RestNode operationsNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					"/bpwizard/library/system/configuration/operations", 2);
			Map<String, WcmOperation[]> wcmOperationMap = operationsNode.getChildren().stream().filter(node -> this.checkNodeType(node, "bpw:supportedOpertions"))
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

		Theme[] themes = this.getThemeLibraries(repository, workspace, request)
				.flatMap(theme -> this.getThemes(theme, request)).toArray(Theme[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return themes;
	}

	@GetMapping(path = "/at/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, AuthoringTemplate> getAuthoringTemplate(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, AuthoringTemplate> authoringTemplates = this.getAuthoringTemplateLibraries(repository, workspace, request)
					.flatMap(at -> this.getAuthoringTemplates(at, request))
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

	@GetMapping(path = "/jsonform/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, JsonForm> getAuthoringTemplateAsJsonForm(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, JsonForm> jsonForms = this.getAuthoringTemplateLibraries(repository, workspace, request)
					.flatMap(at -> this.getAuthoringTemplates(at, request))
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
			RestNode controlFieldFolder = (RestNode) this.itemHandler.item(request, repository, workspace,
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

	@PostMapping(path = "/siteConfig", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteConfig(
			@RequestBody SiteConfig siteConfig, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = siteConfig.getRepository();
			String workspaceName = siteConfig.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node siteConfigFolder = session.getNode(String.format("/bpwizard/library/%s/siteConfig", siteConfig.getLibrary()));
			Node siteConfigNode = siteConfigFolder.addNode(siteConfig.getName(), "bpw:siteConfig");
			
			siteConfigNode.setProperty("bpw:name", siteConfig.getName());
			siteConfigNode.setProperty("bpw:colorTheme", siteConfig.getColorTheme());
			siteConfigNode.setProperty("bpw:rootSiteArea", siteConfig.getRootSiteArea());
			siteConfigNode.setProperty("bpw:customScrollbars", siteConfig.isCustomScrollbars());
			
			PageLayout layout = siteConfig.getLayout();
			Node layoutNode = siteConfigNode.addNode("layout", "bpw:pageLayout");
			layoutNode.setProperty("bpw:style", layout.getStyle());
			layoutNode.setProperty("bpw:width", layout.getWidth());
			
			Node navbarNode = layoutNode.addNode("navbar", "bpw:navbar");
			navbarNode.setProperty("primaryBackground", layout.getNavbar().getPrimaryBackground());
			navbarNode.setProperty("secondaryBackground", layout.getNavbar().getSecondaryBackground());
			navbarNode.setProperty("hidden", layout.getNavbar().isHidden());
			navbarNode.setProperty("folded", layout.getNavbar().isFolded());
			navbarNode.setProperty("position", layout.getNavbar().getPosition().name());
			navbarNode.setProperty("variant", layout.getNavbar().getVariant());
	
			
			Node toolbarNode = layoutNode.addNode("toolbar", "bpw:toolbar");
			toolbarNode.setProperty("customBackgroundColor", layout.getToolbar().isCustomBackgroundColor());
			toolbarNode.setProperty("background", layout.getToolbar().getBackground());
			toolbarNode.setProperty("hidden", layout.getToolbar().isHidden());
			toolbarNode.setProperty("position", layout.getToolbar().getPosition().name());
			
			Node footerNode = layoutNode.addNode("footer", "bpw:footer");
			footerNode.setProperty("customBackgroundColor", layout.getFooter().isCustomBackgroundColor());
			footerNode.setProperty("background", layout.getFooter().getBackground());
			footerNode.setProperty("hidden", layout.getFooter().isHidden());
			footerNode.setProperty("position", layout.getFooter().getPosition().name());
	
			Node sidePanelNode = layoutNode.addNode("sidePanel", "bpw:sidePanel");
			sidePanelNode.setProperty("hidden", layout.getSidePanel().isHidden());
			sidePanelNode.setProperty("position", layout.getSidePanel().getPosition().name());
			session.save();
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
	
	@PostMapping(path = "/at", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(
			@RequestBody AuthoringTemplate at, HttpServletRequest request) 
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = at.getRepository();
			String workspaceName = at.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node atFolder = session.getNode(String.format("/bpwizard/library/%s/authoringTemplate", at.getLibrary()));
			Node atNode = atFolder.addNode(at.getName(), "bpw:authoringTemplate");
			if (StringUtils.hasText(at.getBaseResourceType())) {
				atNode.setProperty("bpw:baseResourceType", at.getBaseResourceType());
			}
			if (StringUtils.hasText(at.getTitle())) {
				atNode.setProperty("bpw:title", at.getTitle());
			}
			if (StringUtils.hasText(at.getDescription())) {
				atNode.setProperty("bpw:description", at.getDescription());
			}
	
			int groupCount = 0;
			for (BaseFormGroup g : at.getFormGroups()) {
				groupCount++;
				String groupName = StringUtils.hasText(g.getGroupTitle()) ? g.getGroupTitle() : ("group" + groupCount);
				String primaryFormGroupType = (g instanceof FormSteps) ? "bpw:formSteps"
						: (g instanceof FormTabs) ? "bpw:formTabs"
								: (g instanceof FormRows) ? "bpw:formRows" : "bpw:formRow";
				Node groupNode = atNode.addNode(groupName, primaryFormGroupType);
	
				if (g instanceof FormSteps) {
					this.addSteps(groupNode, ((FormSteps) g).getSteps());
				} else if (g instanceof FormTabs) {
					this.addTabs(groupNode, ((FormTabs) g).getTabs());
				} else if (g instanceof FormRows) {
					this.addRows(groupNode, ((FormRows) g).getRows());
				}
			}
			Node elementFolder = atNode.addNode("elements", "bpw:elementFolder");
			for (String controlName : at.getFormControls().keySet()) {
				this.addControl(elementFolder, at.getFormControls().get(controlName));
			}
			session.save();
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
			String workspaceName = rt.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node rtFolder = session.getNode(String.format("/bpwizard/library/%s/renderTemplate", rt.getLibrary()));
			Node rtNode = rtFolder.addNode(rt.getName(), "bpw:renderTemplate");
			rtNode.setProperty("bpw:name", rt.getName());
			rtNode.setProperty("bpw:title", StringUtils.hasText(rt.getTitle()) ? rt.getTitle() : rt.getName());
			if (StringUtils.hasText(rt.getDescription())) {
				rtNode.setProperty("bpw:description", rt.getDescription());
			}
			if (StringUtils.hasText(rt.getCode())) {
				rtNode.setProperty("bpw:code", rt.getCode());
			}
			if (StringUtils.hasText(rt.getPreloop())) {
				rtNode.setProperty("bpw:preloop", rt.getPreloop());
			}
			if (StringUtils.hasText(rt.getPostloop())) {
				rtNode.setProperty("bpw:postloop", rt.getPostloop());
			}
			rtNode.setProperty("bpw:maxEntries", rt.getMaxEntries());
			if (StringUtils.hasText(rt.getNote())) {
				rtNode.setProperty("bpw:note", rt.getNote());
			}
			if (StringUtils.hasText(rt.getResourceName())) {
				rtNode.setProperty("bpw:resourceName", rt.getResourceName());
			}
			rtNode.setProperty("bpw:isQuery", rt.isQuery());
			session.save();
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

	@GetMapping(path = "/contentAreaLayout/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ContentAreaLayout> getContentAreaLayouts(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, ContentAreaLayout> contentAreaLayouts = this.getContentAreaLayoutLibraries(repository, workspace, request)
					.flatMap(layout -> this.getContentArealayouts(layout, request))
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

	@PostMapping(path = "/contentAreaLayout", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createPageLayout(
			@RequestBody ContentAreaLayout pageLayout, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = pageLayout.getRepository();
			String workspaceName = pageLayout.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node contentAreaLayoutFolder = session
					.getNode(String.format("/bpwizard/library/%s/contentAreaLayout", pageLayout.getLibrary()));
			Node contentAreaLayoutNode = contentAreaLayoutFolder.addNode(pageLayout.getName(), "bpw:contentAreaLayout");
			contentAreaLayoutNode.setProperty("bpw:name", pageLayout.getName());
			contentAreaLayoutNode.setProperty("bpw:contentWidth", 80);
	
			Node sidePaneNode = contentAreaLayoutNode.addNode("sidePane", "bpw:contentAreaSidePanel");
			this.addSidePageNode(sidePaneNode, pageLayout.getSidePane());
			this.addPageLayoutNodes(contentAreaLayoutNode, pageLayout.getRows());
			session.save();
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

	@PostMapping(path = "/sitearea", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createSiteArea(
			@RequestBody SiteArea sa, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = sa.getRepository();
			String workspaceName = sa.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node parentFolder = session.getNode("/" + sa.getNodePath());
			Node saNode = parentFolder.addNode(sa.getName(), "bpw:siteArea");
	
			saNode.setProperty("bpw:name", sa.getName());
			saNode.setProperty("bpw:title", StringUtils.hasText(sa.getTitle()) ? sa.getTitle() : sa.getName());
			if (StringUtils.hasText(sa.getDescription())) {
				saNode.setProperty("bpw:description", sa.getDescription());
			}
			saNode.setProperty("bpw:url", StringUtils.hasText(sa.getUrl()) ? sa.getUrl() : sa.getName());
			if (StringUtils.hasText(sa.getFriendlyURL())) {
				saNode.setProperty("bpw:friendlyURL", sa.getFriendlyURL());
			}
			saNode.setProperty("bpw:sorderOrder", sa.getSorderOrder());
			saNode.setProperty("bpw:showOnMenu", sa.isShowOnMenu());
	
			if (StringUtils.hasText(sa.getDefaultContent())) {
				saNode.setProperty("bpw:defaultContent", sa.getDefaultContent());
			}
			if (StringUtils.hasText(sa.getAllowedArtifactTypes())) {
				saNode.setProperty("bpw:allowedFileExtension", sa.getAllowedArtifactTypes().split(","));
			}
	
			if (StringUtils.hasText(sa.getAllowedFileExtension())) {
				saNode.setProperty("bpw:allowedFileExtension", sa.getAllowedFileExtension().split(","));
			}
	
			saNode.setProperty("bpw:contentAreaLayout", sa.getContentAreaLayout());
			saNode.setProperty("bpw:securePage", sa.isSecurePage());
			saNode.setProperty("bpw:cacheTTL", sa.getCacheTTL());
	
			if (StringUtils.hasText(sa.getNavigationId())) {
				saNode.setProperty("bpw:navigationId", sa.getNavigationId());
			}
			
			if (StringUtils.hasText(sa.getNavigationType())) {
				saNode.setProperty("bpw:navigationType", sa.getNavigationType());
			}
			
			if (StringUtils.hasText(sa.getTranslate())) {
				saNode.setProperty("bpw:translate", sa.getTranslate());
			}
			
			if (StringUtils.hasText( sa.getIcon())) {
				saNode.setProperty("bpw:function", sa.getFunction());
			}
			
			if (StringUtils.hasText(sa.getIcon())) {
				saNode.setProperty("bpw:icon", sa.getIcon());
			}
			
			if (StringUtils.hasText(sa.getClasses())) {
				saNode.setProperty("bpw:classes", sa.getClasses());
			}
	
			saNode.setProperty("bpw:exactMatch", sa.isExactMatch());
			saNode.setProperty("bpw:externalUrl", sa.isExternalUrl());
			saNode.setProperty("bpw:openInNewTab", sa.isOpenInNewTab());
			
			if (sa.getBadge() != null) {
				Node badgeNode = saNode.addNode("bpw:badge", "bpw:navigationBadge");
				if (StringUtils.hasText(sa.getBadge().getTitle())) {
					badgeNode.setProperty("bpw:title", sa.getBadge().getTitle());
				}
				if (StringUtils.hasText(sa.getBadge().getTranslate())) {
					badgeNode.setProperty("bpw:translate", sa.getBadge().getTranslate());
				}
				if (StringUtils.hasText(sa.getBadge().getBg())) {
					badgeNode.setProperty("bpw:bg", sa.getBadge().getBg());
				}
				if (StringUtils.hasText(sa.getBadge().getFg())) {
					badgeNode.setProperty("bpw:fg", sa.getBadge().getFg());
				}
			}
			
			if (sa.getMetadata() != null && sa.getMetadata().getKeyValues() != null) {
				Node metaDataNode = saNode.addNode("bpw:metaData", "bpw:keyValues");
				int count = 0;
				for (KeyValue keyValue: sa.getMetadata().getKeyValues()) {
					Node kvNode = metaDataNode.addNode("kv" + count++, "bpw:keyValue");
					kvNode.setProperty("bpw:name", keyValue.getName());
					kvNode.setProperty("bpw:value", keyValue.getValue());
				}
			}
			
			if (sa.getSearchData() != null) {
				Node searchDataDataNode = saNode.addNode("bpw:searchData", "bpw:pageSearchData");
				if (StringUtils.hasText(sa.getSearchData().getDescription())) {
					searchDataDataNode.setProperty("description", sa.getSearchData().getDescription());
				}
				if (sa.getSearchData().getKeywords() != null) {
					searchDataDataNode.setProperty("keywords", sa.getSearchData().getKeywords());
				}
			}
	
			Node siteAreaLayoutNode = saNode.addNode("siteAreaLayout", "bpw:siteAreaLayout");
			if (sa.getSiteAreaLayout() != null) {
				Node sidePaneNode = siteAreaLayoutNode.addNode("sidePane", "bpw:contentAreaSidePanel");
				this.addSidePageNode(sidePaneNode, sa.getSiteAreaLayout().getSidePane());
				this.addPageLayoutNodes(siteAreaLayoutNode, sa.getSiteAreaLayout().getRows());
			}
			session.save();
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

	@PostMapping(path = "/ContentItem", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContentItem(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {

		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repositoryName = contentItem.getRepository();
			String workspaceName = contentItem.getWorkspace();
	
			Session session = repositoryManager.getSession(repositoryName, workspaceName);
			Node parentFolder = session.getNode("/" + contentItem.getNodePath());
			Map<String, String> contentElements = contentItem.getContentElements();
			String name = contentElements.get("name"); 
			contentElements.remove("name");
			String title = contentElements.get("title");
			contentElements.remove("title");
			String description = contentElements.get("description");
			contentElements.remove("description");
			
			Node contentNode = parentFolder.addNode(name, "bpw:content");
	
			//contentNode.setProperty("bpw:name", name);
			contentNode.setProperty("bpw:authoringTemplate", contentItem.getAuthoringTemplate());
			contentNode.setProperty("bpw:title", StringUtils.hasText(title) ? title : name);
			if (StringUtils.hasText(description)) {
				contentNode.setProperty("bpw:description", description);
			}
			
			for (String key: contentElements.keySet()) {
				Node contentElementNode = contentNode.addNode(key, "bpw:contentElement");
				contentElementNode.setProperty("bpw:multiple", false);
				contentElementNode.setProperty("bpw:value", new String[] {contentElements.get(key)});
			}
			session.save();
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
	
	@GetMapping(path = "/contentItem/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
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
			contentItemPath = contentItemPath.startsWith("/") ? contentItemPath : "/" + contentItemPath;
			RestNode contentItemNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					contentItemPath, 2);
			
			ContentItem contentItem = new ContentItem(); 
			contentItem.setRepository(repository);
			contentItem.setWorkspace(workspace);
			contentItem.setNodePath(contentItemPath);
			Map<String, String> contentElements = new HashMap<>();
			contentElements.put("name", contentItemNode.getName());
			contentItem.setContentElements(contentElements);
			for (RestProperty property: contentItemNode.getJcrProperties()) {
				if ("bpw:authoringTemplate".equals(property.getName())) {
					contentItem.setAuthoringTemplate(property.getValues().get(0));
				} else if ("bpw:title".equals(property.getName())) {
					contentElements.put("title", property.getValues().get(0));
				} else if ("bpw:description".equals(property.getName())) {
					contentElements.put("description", property.getValues().get(0));
				}
			}
			for (RestNode node: contentItemNode.getChildren()) {
				if (this.checkNodeType(node, "bpw:contentElement")) {
					for (RestProperty property: node.getJcrProperties()) {
						if ("bpw:value".equals(property.getName())) {
							contentElements.put(node.getName(), property.getValues().get(0));
							break;
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
	
	@PostMapping(path = "/wcmNodes/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmNode[] getWcmNodes(
			@PathVariable("repository") final String repository, 
			@PathVariable("workspace") final String workspace,
			@RequestBody final SiteNavigatorFilter filter,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			final String siteAreaPath = filter.getNodePath().startsWith("/") ? filter.getNodePath() : "/" + filter.getNodePath();
			RestNode saNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					siteAreaPath, 2);
			System.out.println("..............getWcmNodes:" + filter);
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
	
	private boolean applyFilter(final RestNode node, SiteNavigatorFilter filter) {
		return (filter == null || filter.getNodePath() == null) ?
				node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> nodeType.startsWith("bpw:"))
		    : node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> Arrays.stream(
						filter.getNodeTypes()).anyMatch(nodeType::equals) &&
						this.propertyMatch(node, filter.getFilters().get(nodeType)));
	}
	
	private boolean propertyMatch(RestNode node, Map<String, String> siteAreaPath) {
		return true;
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
		wcmNode.setNodeType(String.format("%s/%s", siteAreaPath, node.getName()));
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

	private ObjectNode getColumnNode(AuthoringTemplate at, FormColumn formColumn) {
		
		ObjectNode columnNode = this.objectMapper.createObjectNode();
		columnNode.put("type", "div");
		columnNode.put("displayFlex", true);
		columnNode.put("flex-direction", "column");
		columnNode.put("fxFlex", formColumn.getFxFlex());

		ArrayNode fieldNodes = this.objectMapper.createArrayNode();
		for (String name : formColumn.getFormControls()) {			
			FormControl formControl = at.getFormControls().get(name);
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
					fieldNode.put("key", fieldLayout.getKey());
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
						fieldNode.put("key", fieldLayout.getName());
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
				fieldNode.put("key", formControl.getName());
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

	private ObjectNode getRowNode(AuthoringTemplate at, FormRow formRow) {
		ObjectNode rowNode = this.objectMapper.createObjectNode();
		rowNode.put("type", "flex");
		rowNode.put("flex-flow", "row wrap");
		ArrayNode columnNodes = this.objectMapper.createArrayNode();
		for (FormColumn column : formRow.getColumns()) {
			ObjectNode columnNode = this.getColumnNode(at, column);
			columnNodes.add(columnNode);
		}
		// { , , "items": [ "first_name", "last_name" ] },
		rowNode.set("items", columnNodes);
		return rowNode;
	}

	private ArrayNode toFormLayoutNode(AuthoringTemplate at) {
		ArrayNode formNode = this.objectMapper.createArrayNode();
		for (BaseFormGroup formGroup : at.getFormGroups()) {
			if (formGroup instanceof FormRow) {
				ObjectNode rowNode = this.getRowNode(at, (FormRow) formGroup);
				formNode.add(rowNode);
			}

			if (formGroup instanceof FormRows) {
				for (FormRow formRow : ((FormRows) formGroup).getRows()) {
					ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
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
						ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
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
						ObjectNode rowNode = this.getRowNode(at, (FormRow) formRow);
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
		
		RestNodeType restNodeType = nodeTypeHandler.getNodeType(request, repository, workspace, dataType);

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
			if (StringUtils.hasText(at.getTitle())) {
				schemaNode.put("title", at.getTitle());
			}
	
			if (StringUtils.hasText(at.getDescription())) {
				schemaNode.put("description", at.getDescription());
			}
	
			ObjectNode properties = this.objectMapper.createObjectNode();
			ObjectNode definitions = this.objectMapper.createObjectNode();
	
			for (String key : at.getFormControls().keySet()) {
				FormControl formControl = at.getFormControls().get(key);
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
	
			// properties.addAll(propertyNodes);
			schemaNode.set("properties", properties);
			schemaNode.set("definitions", definitions);
			String required[] = at.getFormControls().entrySet().stream().map(entry -> entry.getValue())
					.filter(formControl -> formControl.isMandatory()).map(formControl -> formControl.getName())
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

	private void addSidePageNode(Node sidenavNode, SidePane sidenav) throws RepositoryException {
		sidenavNode.setProperty("bpw:isLeft", sidenav.isLeft());
		sidenavNode.setProperty("bpw:width", sidenav.getWidth());
		this.addResourceViewers(sidenavNode, sidenav.getViewers());
	}

	private void addPageLayoutNodes(Node pageLayoutNode, LayoutRow[] rows) throws RepositoryException {
		int rowCount = 0;
		for (LayoutRow row : rows) {
			Node rowNode = pageLayoutNode.addNode("row" + rowCount++, "bpw:layoutRow");
			int columnCount = 0;
			for (LayoutColumn column : row.getColumns()) {
				Node columnNode = rowNode.addNode("column" + columnCount++, "bpw:layoutColumn");
				columnNode.setProperty("bpw:width", column.getWidth());
				this.addResourceViewers(columnNode, column.getViewers());
			}
		}
	}

	private void addResourceViewers(Node restNode, ResourceViewer viewers[]) throws RepositoryException {
		int viewerCount = 0;
		for (ResourceViewer viewer : viewers) {
			Node viewerNode = restNode.addNode("viewer" + viewerCount++, "bpw:resourceViewer");
			viewerNode.setProperty("bpw:renderTemplayeName", viewer.getRenderTemplate());
			viewerNode.setProperty("bpw:title", viewer.getTitle());
			if (viewer.getContentPath() != null && viewer.getContentPath().length > 0) {
				viewerNode.setProperty("bpw:contentPath", viewer.getContentPath());
			}
		}
	}

	private void addSteps(Node stepsNode, FormStep[] steps) throws RepositoryException {
		for (FormStep step : steps) {
			Node stepNode = stepsNode.addNode(step.getStepName(), "bpw:formStep");
			String stepTitle = StringUtils.hasText(step.getStepTitle()) ? step.getStepTitle() : step.getStepName();
			if (StringUtils.hasText(stepTitle)) {
				stepNode.setProperty("bpw:stepName", stepTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row : step.getFormGroups()) {
				this.addRow(stepNode, (FormRow) row, "row" + rowCount++);
			}
		}
	}

	private void addTabs(Node tabsNode, FormTab[] tabs) throws RepositoryException {
		for (FormTab tab : tabs) {
			Node tabNode = tabsNode.addNode(tab.getTabName(), "bpw:formTab");
			String tabTitle = StringUtils.hasText(tab.getTabTitle()) ? tab.getTabTitle() : tab.getTabName();
			if (StringUtils.hasText(tabTitle)) {
				tabNode.setProperty("bpw:tabName", tabTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row : tab.getFormGroups()) {
				this.addRow(tabNode, (FormRow) row, "row" + rowCount++);
			}
		}
	}

	private void addRows(Node rowsNode, FormRow[] rows) throws RepositoryException {
		int rowCount = 0;
		for (FormRow row : rows) {
			this.addRow(rowsNode, (FormRow) row, "row" + rowCount++);
		}
	}

	private void addRow(Node groupNode, FormRow row, String rowName) throws RepositoryException {
		Node rowNode = groupNode.addNode(rowName, "bpw:formRow");
		String rowTitle = StringUtils.hasText(row.getRowTitle()) ? row.getRowTitle() : row.getRowName();
		if (StringUtils.hasText(rowTitle)) {
			rowNode.setProperty("bpw:rowName", rowTitle);
		}
		int colCount = 0;
		for (FormColumn col : row.getColumns()) {
			String columnName = StringUtils.hasText(col.getId()) ? col.getId() : "column" + colCount;
			this.addColumn(rowNode, col, columnName);
		}
	}

	private void addColumn(Node rowNode, FormColumn column, String columnNameName) throws RepositoryException {
		Node columnNode = rowNode.addNode(columnNameName, "bpw:formColumn");
		columnNode.setProperty("bpw:fxFlex", column.getFxFlex());
		columnNode.setProperty("bpw:id", columnNameName);
		if (column.getFormControls() != null && column.getFormControls().length > 0) {
			columnNode.setProperty("bpw:fieldNames", column.getFormControls());
		}
	}

	private void addControl(Node elementFolder, FormControl control) throws RepositoryException {
		// Node controlNode = elementFolder.addNode(control.getName(),
		// "bpw:formControl");
		Node controlNode = elementFolder.addNode(control.getName(), "bpw:formControl");
		if (StringUtils.hasText(control.getTitle())) {
			controlNode.setProperty("bpw:title", control.getTitle());
		}

		if (StringUtils.hasText(control.getFieldPath())) {
			controlNode.setProperty("bpw:fieldPath", control.getFieldPath());
		}
		
		if (StringUtils.hasText(control.getControlName())) {
			controlNode.setProperty("bpw:controlName", control.getControlName());
		}

		if ((control.getValues() != null) && (control.getValues().length > 0)) {
			controlNode.setProperty("bpw:value", control.getValues());
		}

		if ((control.getOptions() != null) && (control.getOptions().length > 0)) {
			controlNode.setProperty("bpw:options", control.getOptions());
		}

		if (StringUtils.hasText(control.getDefaultValue())) {
			controlNode.setProperty("bpw:defaultValue", control.getDefaultValue());
		}

		if (StringUtils.hasText(control.getHint())) {
			controlNode.setProperty("bpw:hint", control.getHint());
		}

		if (StringUtils.hasText(control.getDataType())) {
			controlNode.setProperty("bpw:dataType", control.getDataType());
		}

		if (StringUtils.hasText(control.getRelationshipType())) {
			controlNode.setProperty("bpw:relationshipType", control.getRelationshipType());
		}

		if (StringUtils.hasText(control.getRelationshipCardinality())) {
			controlNode.setProperty("bpw:relationshipCardinality", control.getRelationshipCardinality());
		}

		if (StringUtils.hasText(control.getValditionRegEx())) {
			controlNode.setProperty("bpw:valditionRegEx", control.getValditionRegEx());
		}

		controlNode.setProperty("bpw:mandatory", control.isMandatory());
		controlNode.setProperty("bpw:userSearchable", control.isUserSearchable());
		controlNode.setProperty("bpw:systemIndexed", control.isSystemIndexed());
		controlNode.setProperty("bpw:showInList", control.isShowInList());
		controlNode.setProperty("bpw:unique", control.isUnique());
	}

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
		this.resolveResourceNode(result, node);
		return result;
	}

	private RenderTemplate toRenderTemplate(RestNode node, RenderTemplate rt) {
		RenderTemplate result = new RenderTemplate();
		result.setRepository(rt.getRepository());
		result.setWorkspace(rt.getWorkspace());
		result.setLibrary(rt.getLibrary());
		result.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				result.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				result.setDescription(property.getValues().get(0));
			} else if ("bpw:code".equals(property.getName())) {
				result.setCode(property.getValues().get(0));
			} else if ("bpw:preloop".equals(property.getName())) {
				result.setPreloop(property.getValues().get(0));
			} else if ("bpw:postloop".equals(property.getName())) {
				result.setPostloop(property.getValues().get(0));
			} else if ("bpw:maxEntries".equals(property.getName())) {
				result.setMaxEntries(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:note".equals(property.getName())) {
				result.setNote(property.getValues().get(0));
			} else if ("bpw:resourceName".equals(property.getName())) {
				result.setResourceName(property.getValues().get(0));
			} else if ("bpw:isQuery".equals(property.getName())) {
				result.setQuery(Boolean.parseBoolean(property.getValues().get(0)));
			}
		}
		return result;
	}

	private ContentAreaLayout toContentAreaLayout(RestNode node, ContentAreaLayout layout) {
		ContentAreaLayout result = new ContentAreaLayout();
		result.setRepository(layout.getRepository());
		result.setWorkspace(layout.getWorkspace());
		result.setLibrary(layout.getLibrary());
		result.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
//			if ("bpw:headerEnabled".equals(property.getName())) {
//				result.setHeaderEnabled(Boolean.parseBoolean(property.getValues().get(0)));
//			} else if ("bpw:footerEnabled".equals(property.getName())) {
//				result.setFooterEnabled(Boolean.parseBoolean(property.getValues().get(0)));
//			} else if ("bpw:theme".equals(property.getName())) {
//				result.setTheme(property.getValues().get(0));
//			} else 
			if (" bpw:contentWidth".equals(property.getName())) {
				result.setContentWidth(Integer.parseInt(property.getValues().get(0)));
			}
		}

		List<LayoutRow> rows = new ArrayList<>();

		node.getChildren().forEach(childNode -> {
			if (this.checkNodeType(childNode, "bpw:contentAreaSidePanel")) {
				SidePane sidepane = new SidePane();
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("bpw:isLeft".equals(property.getName())) {
						sidepane.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
					} else if ("bpw:width".equals(property.getName())) {
						sidepane.setWidth(Integer.parseInt(property.getValues().get(0)));
					}
				}
				sidepane.setViewers(this.resolveResourceViewer(childNode));
				result.setSidePane(sidepane);
			} else if (this.checkNodeType(childNode, "bpw:layoutRow")) {
				LayoutRow row = this.resolveLayoutRow(childNode);
				rows.add(row);
			}
		});
		result.setRows(rows.toArray(new LayoutRow[rows.size()]));
		return result;
	}

	private LayoutRow resolveLayoutRow(RestNode restNode) {
		LayoutRow row = new LayoutRow();
		LayoutColumn columns[] = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:layoutColumn")).map(this::toLayoutColumn)
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
			if (this.checkNodeType(viewerNode, "bpw:resourceViewer")) {
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

	private Stream<Theme> getThemeLibraries(String repository, String workspace, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toThemeWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<ContentAreaLayout> getContentAreaLayoutLibraries(String repository, String workspace, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toContentAreaLayoutWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private WcmLibrary[] getWcmLibraries(String repository, String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, repository, workspace,
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
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).filter(this::notSystemLibrary)
					.map(node -> toRenderTemplateWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<Theme> getThemes(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(request, theme.getRepositoryName(),
					theme.getWorkspace(), "/bpwizard/library/" + theme.getLibrary() + "/theme", 1);
			return themeNode.getChildren().stream().filter(this::isTheme).map(node -> this.toTheme(node, theme));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<RenderTemplate> getRenderTemplates(RenderTemplate rt, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(request, rt.getRepository(), rt.getWorkspace(),
					"/bpwizard/library/" + rt.getLibrary() + "/renderTemplate", 1);
			return themeNode.getChildren().stream().filter(this::isRenderTemplate)
					.map(node -> this.toRenderTemplate(node, rt));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<ContentAreaLayout> getContentArealayouts(ContentAreaLayout contentAreaLayout, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			RestNode contentAreaLayoutNode = (RestNode) this.itemHandler.item(request, contentAreaLayout.getRepository(),
					contentAreaLayout.getWorkspace(), "/bpwizard/library/" + contentAreaLayout.getLibrary() + "/contentAreaLayout", 4);
			return contentAreaLayoutNode.getChildren().stream().filter(this::isContentAreaLayout)
					.map(node -> this.toContentAreaLayout(node, contentAreaLayout));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private boolean isControlField(RestNode node) {
		return this.checkNodeType(node, "bpw:controlField");
	}

	private boolean isSiteArea(RestNode node) {
		return this.checkNodeType(node, "bpw:siteArea");
	}
	
	private boolean isControlFieldMetaData(RestNode node) {
		return this.checkNodeType(node, "bpw:controlFieldMetaData");
	}

	private boolean isLibrary(RestNode node) {
		return this.checkNodeType(node, "bpw:library");
	}

	private boolean notSystemLibrary(RestNode node) {
		return !"system".equalsIgnoreCase(node.getName());
	}
	
	private boolean isTheme(RestNode node) {
		return this.checkNodeType(node, "bpw:themeType");
	}

	private boolean isRenderTemplate(RestNode node) {
		return this.checkNodeType(node, "bpw:renderTemplate");
	}

	private boolean isContentAreaLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:contentAreaLayout");
	}

	private boolean isAuthortingTemplate(RestNode node) {
		return this.checkNodeType(node, "bpw:authoringTemplate");
	}

	private boolean isElementNode(RestNode node) {
		return this.checkNodeType(node, "bpw:elementFolder");
	}

	private boolean checkNodeType(RestNode node, String nodeType) {
		return node.getJcrProperties().stream().anyMatch(
				property -> "jcr:primaryType".equals(property.getName()) && property.getValues().contains(nodeType));
	}

	private Stream<AuthoringTemplate> getAuthoringTemplateLibraries(String repository, String workspace,
			HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, repository, workspace,
					"/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary)
					.map(node -> toAuthoringTemplateWithLibrary(node, repository, workspace));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}

	private Stream<AuthoringTemplate> getAuthoringTemplates(AuthoringTemplate at, HttpServletRequest request)
			throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.itemHandler.item(request, at.getRepository(), at.getWorkspace(),
					"/bpwizard/library/" + at.getLibrary() + "/authoringTemplate", 7);
			return atNode.getChildren().stream().filter(this::isAuthortingTemplate)
					.map(node -> this.toAuthoringTemplate(node, at));
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

	private AuthoringTemplate toAuthoringTemplate(RestNode node, AuthoringTemplate at) {
		AuthoringTemplate result = new AuthoringTemplate();
		result.setRepository(at.getRepository());
		result.setWorkspace(at.getWorkspace());
		result.setLibrary(at.getLibrary());
		result.setName(node.getName());
		for (RestProperty properties : node.getJcrProperties()) {
			if ("bpw:baseResourceType".equals(properties.getName())) {
				result.setBaseResourceType(properties.getValues().get(0));
			}
		}
		this.populateAuthoringTemplate(result, node);
		return result;
	}

	private void resolveResourceNode(ResourceNode resourceNode, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				resourceNode.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				resourceNode.setDescription(property.getValues().get(0));
			} else if ("bpw:publishDate".equals(property.getName())) {
				resourceNode.setPublishDate(property.getValues().get(0));
			} else if ("bpw:expireDate".equals(property.getName())) {
				resourceNode.setExpireDate(property.getValues().get(0));
			} else if ("bpw:workflow".equals(property.getName())) {
				resourceNode.setWorkflow(property.getValues().toArray(new String[property.getValues().size()]));
			} else if ("bpw:workflowStage".equals(property.getName())) {
				resourceNode.setWorkflowStage(property.getValues().get(0));
			} else if ("bpw:categories".equals(property.getName())) {
				resourceNode.setCategories(property.getValues().toArray(new String[property.getValues().size()]));
			}
		}
	}

	private FormControl toFormControl(RestNode node) {
		FormControl formControl = new FormControl();
		formControl.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				formControl.setTitle(property.getValues().get(0));
			} else if ("bpw:fieldPath".equals(property.getName())) {
				formControl.setFieldPath(property.getValues().get(0));
			} else if ("bpw:controlName".equals(property.getName())) {
				formControl.setControlName(property.getValues().get(0));
			} else if ("bpw:value".equals(property.getName())) {
				formControl.setValues(property.getValues().toArray(new String[property.getValues().size()]));
			} else if ("bpw:options".equals(property.getName())) {
				formControl.setOptions(property.getValues().toArray(new String[property.getValues().size()]));
			} else if ("bpw:defaultValue".equals(property.getName())) {
				formControl.setDefaultValue(property.getValues().get(0));
			} else if ("bpw:hint".equals(property.getName())) {
				formControl.setHint(property.getValues().get(0));
			} else if ("bpw:dataType".equals(property.getName())) {
				formControl.setDataType(property.getValues().get(0));
			} else if ("bpw:relationshipType".equals(property.getName())) {
				formControl.setRelationshipType(property.getValues().get(0));
			} else if ("bpw:relationshipCardinality".equals(property.getName())) {
				formControl.setRelationshipCardinality(property.getValues().get(0));
			} else if ("bpw:valditionRegEx".equals(property.getName())) {
				formControl.setValditionRegEx(property.getValues().get(0));
			} else if ("bpw:mandatory".equals(property.getName())) {
				formControl.setMandatory(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:systemIndexed".equals(property.getName())) {
				formControl.setSystemIndexed(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:userSearchable".equals(property.getName())) {
				formControl.setUserSearchable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:showInList".equals(property.getName())) {
				formControl.setShowInList(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:unique".equals(property.getName())) {
				formControl.setUnique(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:multiple".equals(property.getName())) {
				formControl.setMultiple(Boolean.parseBoolean(property.getValues().get(0)));
			}

		}
		return formControl;
	}

	private void populateFormControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		at.setFormControls(formControls);
	}

	private void populateBaseFormGroup(BaseFormGroup group, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:groupTitle".equals(property.getName())) {
				group.setGroupTitle(property.getValues().get(0));
			} else if ("bpw:groupName".equals(property.getName())) {
				group.setGroupName(property.getValues().get(0));
			}
		}
	}

	private FormTabs populateFormTabs(RestNode restNode) {
		FormTabs formTabs = new FormTabs();
		populateBaseFormGroup(formTabs, restNode);
		FormTab tabs[] = restNode.getChildren().stream().filter(node -> this.checkNodeType(node, "bpw:formTab"))
				.map(this::populateFormTab).toArray(FormTab[]::new);
		formTabs.setTabs(tabs);
		return formTabs;
	}

	private FormRows populateFormRows(RestNode restNode) {
		FormRows formRows = new FormRows();
		populateBaseFormGroup(formRows, restNode);
		FormRow rows[] = restNode.getChildren().stream().filter(node -> this.checkNodeType(node, "bpw:formRow"))
				.map(this::populateFormRow).toArray(FormRow[]::new);
		formRows.setRows(rows);
		return formRows;
	}

	private FormStep populateFormStep(RestNode restNode) {
		FormStep formStep = new FormStep();
		formStep.setStepName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:stepName".equals(property.getName())) {
				formStep.setStepTitle(property.getValues().get(0));
				break;
			}
		}
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			Optional<BaseFormGroup> group = this.populateFormGroups(node);
			if (group.isPresent()) {
				formGroups.add(group.get());
			}
		});
		formStep.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
		return formStep;
	}

	private FormTab populateFormTab(RestNode restNode) {
		FormTab formTab = new FormTab();
		formTab.setTabName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:tabName".equals(property.getName())) {
				formTab.setTabTitle(property.getValues().get(0));
				break;
			}
		}
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			Optional<BaseFormGroup> group = this.populateFormGroups(node);
			if (group.isPresent()) {
				formGroups.add(group.get());
			}
		});
		formTab.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));

		return formTab;
	}

	private FormRow populateFormRow(RestNode restNode) {
		FormRow formRow = new FormRow();
		formRow.setRowName(restNode.getName());
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:rowName".equals(property.getName())) {
				formRow.setRowTitle(property.getValues().get(0));
				break;
			}
		}
		FormColumn columns[] = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formColumn")).map(this::populateFormColumn)
				.toArray(FormColumn[]::new);
		formRow.setColumns(columns);
		return formRow;
	}

	private FormSteps populateFormSteps(RestNode restNode) {
		FormSteps formSteps = new FormSteps();
		populateBaseFormGroup(formSteps, restNode);
		FormStep steps[] = restNode.getChildren().stream().filter(node -> this.checkNodeType(node, "bpw:formStep"))
				.map(this::populateFormStep).toArray(FormStep[]::new);
		formSteps.setSteps(steps);
		return formSteps;
	}

	private boolean isCustomeFieldLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:fieldLayout");
	}

	private boolean isFieldLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:fieldLayout");
	}

	private FieldLayout toFieldLayout(RestNode node) {
		FieldLayout fieldLayout = new FieldLayout();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:multiple".equals(property.getName())) {
				fieldLayout.setMultiple(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:key".equals(property.getName())) {
				fieldLayout.setKey(property.getValues().get(0));
			} else if ("bpw:name".equals(property.getName())) {
				fieldLayout.setName(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				fieldLayout.setTitle(property.getValues().get(0));
			} else if ("bpw:items".equals(property.getName())) {
				fieldLayout.setItems(property.getValues().get(0));
			}
		}
		if (StringUtils.isEmpty(fieldLayout.getKey())) {
			fieldLayout.setKey(fieldLayout.getName());
		}
		return fieldLayout;
	}
	
	private FieldLayout toCustomeFieldLayout(RestNode node) {
		FieldLayout customFieldLayout = new FieldLayout();
		customFieldLayout.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:multiple".equals(property.getName())) {
				customFieldLayout.setMultiple(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:key".equals(property.getName())) {
				customFieldLayout.setKey(property.getValues().get(0));
			} else if ("bpw:name".equals(property.getName())) {
				customFieldLayout.setName(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				customFieldLayout.setTitle(property.getValues().get(0));
			} else if ("bpw:items".equals(property.getName())) {
				customFieldLayout.setItems(property.getValues().get(0));
			} 
		}
		if (StringUtils.isEmpty(customFieldLayout.getKey())) {
			customFieldLayout.setKey(customFieldLayout.getName());
		}

		FieldLayout[] fieldLayouts = node.getChildren().stream().filter(this::isFieldLayout).map(this::toFieldLayout)
				.toArray(FieldLayout[]::new);

		customFieldLayout.setFieldLayouts(fieldLayouts);

		return customFieldLayout;
	}

	private FormColumn populateFormColumn(RestNode node) {
		FormColumn column = new FormColumn();
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:id".equals(property.getName())) {
				column.setId(property.getValues().get(0));
			} else if ("equals".equals(property.getName())) {
				column.setFxFlex(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:fieldNames".equals(property.getName())) {
				column.setFormControls(property.getValues().toArray(new String[property.getValues().size()]));
			}
		}

		if (node.getChildren() != null) {
			FieldLayout[] customeFieldLayouts = node.getChildren().stream().filter(this::isCustomeFieldLayout)
					.map(this::toCustomeFieldLayout).toArray(FieldLayout[]::new);

			column.setFieldLayouts(customeFieldLayouts);
		}

		return column;
	}

	private void populateAuthoringTemplate(AuthoringTemplate at, RestNode restNode) {
		this.resolveResourceNode(at, restNode);
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			if (this.isElementNode(node)) {
				this.populateFormControls(at, node);
			} else {
				Optional<BaseFormGroup> group = this.populateFormGroups(node);
				if (group.isPresent()) {
					formGroups.add(group.get());
				}
			}
		});
		at.setFormGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
	}

	private Optional<BaseFormGroup> populateFormGroups(RestNode node) {
		BaseFormGroup group = null;
		if (this.checkNodeType(node, "bpw:formTabs")) {
			group = this.populateFormTabs(node);
		} else if (this.checkNodeType(node, "bpw:formSteps")) {
			group = this.populateFormSteps(node);
		} else if (this.checkNodeType(node, "bpw:formRows")) {
			group = this.populateFormRows(node);
		} else if (this.checkNodeType(node, "bpw:formRow")) {
			group = this.populateFormRow(node);
		}
		return group == null ? Optional.empty() : Optional.of(group);
	}
	
	private SiteConfig getSiteConfig(Node siteConfigNode) throws RepositoryException {
		SiteConfig siteConfig = new SiteConfig();
		siteConfig.setName(siteConfigNode.getProperty("bpw:name").getString());
		siteConfig.setColorTheme(siteConfigNode.getProperty("bpw:colorTheme").getString());
		siteConfig.setCustomScrollbars(siteConfigNode.getProperty("bpw:customScrollbars").getBoolean());
		siteConfig.setRootSiteArea(siteConfigNode.getProperty("bpw:rootSiteArea").getString());
		
		Node layoutNode = siteConfigNode.getNode("layout");
		
		PageLayout layout = new PageLayout();
		siteConfig.setLayout(layout);
		layout.setStyle(layoutNode.getProperty("bpw:style").getString());
		layout.setWidth(layoutNode.getProperty("bpw:width").getString());

		Node navbarNode = layoutNode.getNode("navbar");
		NavBar navbar = new NavBar();
		layout.setNavbar(navbar);
		
		navbar.setFolded(navbarNode.getProperty("folded").getBoolean());
		navbar.setPrimaryBackground(navbarNode.getProperty("primaryBackground").getString());
		navbar.setSecondaryBackground(navbarNode.getProperty("secondaryBackground").getString());
		navbar.setVariant(navbarNode.getProperty("variant").getString());
		navbar.setPosition(NavBar.Position.valueOf(navbarNode.getProperty("position").getString()));
		navbar.setHidden(navbarNode.getProperty("hidden").getBoolean());
		
		
		Node toolbarNode = layoutNode.getNode("toolbar");
		Toolbar toolbar = new Toolbar();
		layout.setToolbar(toolbar);
		toolbar.setCustomBackgroundColor(toolbarNode.getProperty("customBackgroundColor").getBoolean());
		toolbar.setBackground(toolbarNode.getProperty("background").getString());
		toolbar.setHidden(toolbarNode.getProperty("hidden").getBoolean());
		toolbar.setPosition(Toolbar.Position.valueOf(toolbarNode.getProperty("position").getString()));
		
		Node footerNode = layoutNode.getNode("footer");
		Footer footer = new Footer();
		layout.setFooter(footer);
		footer.setCustomBackgroundColor(footerNode.getProperty("customBackgroundColor").getBoolean());
		footer.setBackground(footerNode.getProperty("background").getString());
		footer.setHidden(footerNode.getProperty("hidden").getBoolean());
		footer.setPosition(Footer.Position.valueOf(footerNode.getProperty("position").getString()));
		
		Node sidePanelNode = layoutNode.getNode("sidePanel");
		SidePanel sidePanel = new SidePanel();
		layout.setSidePanel(sidePanel);
		sidePanel.setHidden(sidePanelNode.getProperty("hidden").getBoolean());
		sidePanel.setPosition(SidePanel.Position.valueOf(sidePanelNode.getProperty("position").getString()));
		return siteConfig;
	}
	
	private Map<String, SiteArea> getSiteAreas(
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea,
			HttpServletRequest request
			) throws RepositoryException {
		RestNode saNode = (RestNode) this.itemHandler.item(request, repository, workspace,
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
			HttpServletRequest request,
			String repository,			
			String workspace, 
			String library,
			String rootSiteArea
			) throws RepositoryException {
		RestNode siteArea = (RestNode) this.itemHandler.item(request, repository, workspace,
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
			}     
			/*
			+ bpw:metaData (bpw:keyValues)
			+ bpw:searchData (bpw:pageSearchData)	
			*/		
		}
		
		siteAreas.put(sa.getNavigationId(), sa);
		for (RestNode node :saNode.getChildren()) {
			if (this.checkNodeType(node, "bpw:navigationBadge")) {
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
			} else if (this.checkNodeType(node, "bpw:keyValues")) {
				keyValues metadata = new keyValues();
				KeyValue[] keyValues = node.getChildren().stream().filter(n -> this.checkNodeType(n, "bpw:keyValue"))
						.map(n -> this.toKeyValue(n)).toArray(KeyValue[]::new);
				metadata.setKeyValue(keyValues);
				sa.setMetadata(metadata);
			} else if (this.checkNodeType(node, "bpw:pageSearchData")) {
				SearchData searchData = new SearchData();
				for (RestProperty property : node.getJcrProperties()) {
					if ("description".equals(property.getName())) {
						searchData.setDescription(property.getValues().get(0));
					} else if ("keywords".equals(property.getName())) {
						searchData.setKeywords(property.getValues().toArray(new String[property.getValues().size()]));
					}
				}
				sa.setSearchData(searchData);
			} else if (this.checkNodeType(node, "bpw:siteAreaLayout")) {
				SiteAreaLayout siteAreaLayout = new SiteAreaLayout();
				List<LayoutRow> rows = new ArrayList<>();
				node.getChildren().forEach(childNode -> {
					if (this.checkNodeType(childNode, "bpw:contentAreaSidePanel")) {
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
					} else if (this.checkNodeType(childNode, "bpw:layoutRow")) {
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
			if (this.checkNodeType(node, "bpw:navigationBadge")) {
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
		return node.getChildren().stream().filter(n -> this.checkNodeType(n, "bpw:supportedOpertion"))
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
					}
				}
				return wcmOperation;
			}).toArray(WcmOperation[]::new);
	}
	
	private WcmRepository toWcmRepository(Repository restReoisitory, HttpServletRequest request) throws WcmRepositoryException {
		try {
			WcmRepository wcmRepository = new WcmRepository();
			wcmRepository.setName(restReoisitory.getName());
			RestWorkspaces restWorkspaces = this.repositoryHandler.getWorkspaces(request, restReoisitory.getName());
			WcmWorkspace[] wcmWorkspaces = restWorkspaces.getWorkspaces().stream()
					.map(workspace -> toWcmWorkspace(restReoisitory.getName(), workspace, request))
					.toArray(WcmWorkspace[]::new);
			wcmRepository.setWorkspaces(wcmWorkspaces);
			return wcmRepository;
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}		
	}
	
	private WcmWorkspace toWcmWorkspace(String repository, Workspace restWorkspace, HttpServletRequest request) {
		WcmWorkspace wcmWorkspace = new WcmWorkspace();
		wcmWorkspace.setName(restWorkspace.getName());
		wcmWorkspace.setLibraries(this.getWcmLibraries(repository, wcmWorkspace.getName(), request));
		return wcmWorkspace;
	}
}
