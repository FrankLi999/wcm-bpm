package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.BaseFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlField;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlFieldMetadata;
import com.bpwizard.wcm.repo.rest.jcr.model.FormColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRow;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRows;
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormSteps;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTabs;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.LayoutRow;
import com.bpwizard.wcm.repo.rest.jcr.model.PageLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.RenderTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceNode;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceViewer;
import com.bpwizard.wcm.repo.rest.jcr.model.SideNav;
import com.bpwizard.wcm.repo.rest.jcr.model.Theme;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestRepositories.Repository;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestWorkspaces.Workspace;

@RestController
@RequestMapping(WcmRestController.BASE_URI)
public class WcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmRestController.class);
	public static final String BASE_URI = "/wcm/api/rest";
	
	@Autowired
    private RestServerHandler serverHandler;
	
	@Autowired
	private RestRepositoryHandler repositoryHandler;
	
	@Autowired
	private RestItemHandler itemHandler;
	
	@Autowired 
	protected RepositoryManager repositoryManager;
	
	@GetMapping(path="/theme", produces= MediaType.APPLICATION_JSON_VALUE)
	public Theme[] getTheme(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		RestRepositories respositories = this.serverHandler.getRepositories(request);
		Theme[] themes = respositories.getRepositories().stream()
			.map(repo -> this.toTheme(repo))
			.flatMap(theme -> this.getWorkspaces(theme, request))
			.flatMap(theme -> this.getLibraries(theme, request))
		    .flatMap(theme -> this.getThemes(theme, request))
		    .toArray(Theme[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return themes;
	}
	
	@GetMapping(path="/at", produces= MediaType.APPLICATION_JSON_VALUE)
	public AuthoringTemplate[] getAuthoringTemplate(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		RestRepositories respositories = this.serverHandler.getRepositories(request);
		AuthoringTemplate[] authoringTemplates = respositories.getRepositories().stream()
			.map(repo -> this.toAuthoringTemplate(repo))
			.flatMap(at -> this.getWorkspaces(at, request))
			.flatMap(at -> this.getLibraries(at, request))
		    .flatMap(at -> this.getAuthoringTemplates(at, request))
		    .toArray(AuthoringTemplate[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return authoringTemplates;
	}
	
	@GetMapping(path="/{repository}/{workspace}/control", produces= MediaType.APPLICATION_JSON_VALUE)
	public ControlField[] getControlField(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace,
			HttpServletRequest request) throws RepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		RestNode controlFieldFolder = (RestNode) this.itemHandler.item(request, repository, workspace, "/bpwizard/controlField", 2);
		ControlField[] ControlFileds = controlFieldFolder.getChildren().stream()
			.filter(this::isControlField)
			.map(this::toControlField)
		    .toArray(ControlField[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return ControlFileds;
	}
	
	@PostMapping(path="/at", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(

			@RequestBody AuthoringTemplate at,
			HttpServletRequest request) throws RepositoryException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String repositoryName = at.getRepository();
		String workspaceName = at.getWorkspace();
		
		Session session = repositoryManager.getSession(request, repositoryName, workspaceName);
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
					: (g instanceof FormRows) ? "bpw:formRows"
					: "bpw:formRow";
			Node groupNode = atNode.addNode(groupName, primaryFormGroupType);
			
			if (g instanceof FormSteps) {
				this.addSteps(groupNode, ((FormSteps)g).getSteps());
			} else if (g instanceof FormTabs) {
				this.addTabs(groupNode, ((FormTabs)g).getTabs());
			} else if (g instanceof FormRows) {
				this.addRows(groupNode, ((FormRows)g).getRows());
			}
		}
		Node elementFolder = atNode.addNode("elements", "bpw:elementFolder");
		for (String controlName: at.getFormControls().keySet()) {
			this.addControl(elementFolder, at.getFormControls().get(controlName));
		}
		session.save();
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(path="/rt", produces= MediaType.APPLICATION_JSON_VALUE)
	public RenderTemplate[] getRenderTemplate(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		RestRepositories respositories = this.serverHandler.getRepositories(request);
		RenderTemplate[] renderTemplates = respositories.getRepositories().stream()
			.map(repo -> this.toRenderTemplate(repo))
			.flatMap(rt -> this.getWorkspaces(rt, request))
			.flatMap(rt -> this.getLibraries(rt, request))
		    .flatMap(rt -> this.getRenderTemplates(rt, request))
		    .toArray(RenderTemplate[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return renderTemplates;
	}
	
	@PostMapping(path="/rt", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createRenderTemplate(
			@RequestBody RenderTemplate rt,
			HttpServletRequest request) throws RepositoryException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String repositoryName = rt.getRepository();
		String workspaceName = rt.getWorkspace();
		
		Session session = repositoryManager.getSession(request, repositoryName, workspaceName);
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
	}
	
	@GetMapping(path="/pagelayout", produces= MediaType.APPLICATION_JSON_VALUE)
	public PageLayout[] getPagelayout(HttpServletRequest request) throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		RestRepositories respositories = this.serverHandler.getRepositories(request);
		PageLayout[] pageLayouts = respositories.getRepositories().stream()
			.map(repo -> this.toPageLayout(repo))
			.flatMap(layout -> this.getWorkspaces(layout, request))
			.flatMap(layout -> this.getLibraries(layout, request))
		    .flatMap(layout -> this.getPagelayouts(layout, request))
		    .toArray(PageLayout[]::new);

		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
		return pageLayouts;
	}
	
	@PostMapping(path="/pagelayout", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createPageLayout(
			@RequestBody PageLayout pageLayout,
			HttpServletRequest request) throws RepositoryException, IOException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		String repositoryName = pageLayout.getRepository();
		String workspaceName = pageLayout.getWorkspace();
		
		Session session = repositoryManager.getSession(request, repositoryName, workspaceName);
		Node pageLayoutFolder = session.getNode(String.format("/bpwizard/library/%s/pageLayout", pageLayout.getLibrary()));
		Node pageLayoutNode = pageLayoutFolder.addNode(pageLayout.getName(), "bpw:pageLayout");
		pageLayoutNode.setProperty("bpw:name", pageLayout.getName());
		pageLayoutNode.setProperty("bpw:headerEnabled", pageLayout.isHeaderEnabled());
		pageLayoutNode.setProperty("bpw:footerEnabled", pageLayout.isFooterEnabled());
		pageLayoutNode.setProperty("bpw:theme", pageLayout.getTheme());
		pageLayoutNode.setProperty("bpw:contentWidth", 80);

		Node sidenavNode = pageLayoutNode.addNode("sidenav", "bpw:layoutSidenav");
		this.addSidenavNode(sidenavNode, pageLayout.getSidenav());
		this.addContentLayoutNodes(pageLayoutNode, pageLayout.getRows());
		session.save();
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	private void addSidenavNode(Node sidenavNode, SideNav sidenav) throws RepositoryException {
		sidenavNode.setProperty("bpw:isLeft", sidenav.isLeft());
		sidenavNode.setProperty("bpw:width", sidenav.getWidth());
		this.setResourceViewer(sidenavNode, sidenav.getViewers());
	}
	
	private void addContentLayoutNodes(Node pageLayoutNode, LayoutRow[] rows) throws RepositoryException {
		int rowCount = 0;
		for (LayoutRow row: rows) {
			Node rowNode = pageLayoutNode.addNode("row" + rowCount++, "bpw:layoutRow");
			int columnCount = 0;
			for (LayoutColumn column: row.getColumns()) {
				Node columnNode = rowNode.addNode("column" + columnCount++, "bpw:layoutColumn");
				columnNode.setProperty("bpw:width", column.getWidth());
				this.setResourceViewer(columnNode, column.getViewers());
			}
		}
	}
	
	private void setResourceViewer(Node restNode, ResourceViewer viewers[]) throws RepositoryException {
		for (ResourceViewer viewer: viewers) {
			Node viewerNode = restNode.addNode("viewer", "bpw:resourceViewer");
			viewerNode.setProperty("bpw:renderTemplayeName", viewer.getRenderTemplate());
		}
	}
	
	private void addSteps(Node stepsNode, FormStep[] steps) throws RepositoryException {
		for (FormStep step: steps) {
			Node stepNode = stepsNode.addNode(step.getStepName(), "bpw:formStep");
			String stepTitle = StringUtils.hasText(step.getStepTitle()) ? step.getStepTitle() : step.getStepName();
			if (StringUtils.hasText(stepTitle)) {
				stepNode.setProperty("bpw:stepName", stepTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row: step.getFormGroups()) {
				this.addRow(stepNode, (FormRow)row, "row" + rowCount++);
			}
		}
	}
	
	private void addTabs(Node tabsNode, FormTab[] tabs) throws RepositoryException {
		for (FormTab tab: tabs) {
			Node tabNode = tabsNode.addNode(tab.getTabName(), "bpw:formTab");
			String tabTitle = StringUtils.hasText(tab.getTabTitle()) ? tab.getTabTitle() : tab.getTabName();
			if (StringUtils.hasText(tabTitle)) {
				tabNode.setProperty("bpw:tabName", tabTitle);
			}
			int rowCount = 0;
			for (BaseFormGroup row: tab.getFormGroups()) {
				this.addRow(tabNode, (FormRow)row, "row" + rowCount++);
			}
		}

	}
	
	private void addRows(Node rowsNode, FormRow[] rows) throws RepositoryException {
		int rowCount = 0;
		for (FormRow row: rows) {
			this.addRow(rowsNode, (FormRow)row, "row" + rowCount++);
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
		//Node controlNode = elementFolder.addNode(control.getName(), "bpw:formControl");
		Node controlNode = elementFolder.addNode(control.getName(), "bpw:formControl");
		if (StringUtils.hasText(control.getTitle())) {
			controlNode.setProperty("bpw:title", control.getTitle());
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
	
	private Theme toTheme(Repository repo) {
		Theme theme = new Theme();
		theme.setRepositoryName(repo.getName());
		return theme;
	}
	
	private PageLayout toPageLayout(Repository repo) {
		PageLayout pageLayout = new PageLayout();
		pageLayout.setRepository(repo.getName());
		return pageLayout;
	}
	
	private Theme toTheme(Workspace workspace, String repo) {
		Theme theme = new Theme();
		theme.setRepositoryName(repo);
		theme.setWorkspace(workspace.getName());
		return theme;
	}
	
	private PageLayout toPageLayout(Workspace workspace, String repo) {
		PageLayout pageLayout = new PageLayout();
		pageLayout.setRepository(repo);
		pageLayout.setWorkspace(workspace.getName());
		return pageLayout;
	}
	
	private RenderTemplate toRenderTemplate(Workspace workspace, String repo) {
		RenderTemplate rt = new RenderTemplate();
		rt.setRepository(repo);
		rt.setWorkspace(workspace.getName());
		return rt;
	}
	
	private Theme toThemeWithLibrary(RestNode node, Theme theme) {
		Theme themeWithLibrary = new Theme();
		themeWithLibrary.setRepositoryName(theme.getRepositoryName());
		themeWithLibrary.setWorkspace(theme.getWorkspace());
		themeWithLibrary.setLibrary(node.getName());
		return themeWithLibrary;
	}
	
	private PageLayout toPageLayoutWithLibrary(RestNode node, PageLayout pageLayout) {
		PageLayout pagelayoutWithLibrary = new PageLayout();
		pagelayoutWithLibrary.setRepository(pageLayout.getRepository());
		pagelayoutWithLibrary.setWorkspace(pageLayout.getWorkspace());
		pagelayoutWithLibrary.setLibrary(node.getName());
		return pagelayoutWithLibrary;
	}
	
	private RenderTemplate toRenderTemplateWithLibrary(RestNode node, RenderTemplate rt) {
		RenderTemplate rtWithLibrary = new RenderTemplate();
		rtWithLibrary.setRepository(rt.getRepository());
		rtWithLibrary.setWorkspace(rt.getWorkspace());
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
		for (RestProperty property: node.getJcrProperties()) {
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
	
	private PageLayout toPageLayout(RestNode node, PageLayout layout) {
		PageLayout result = new PageLayout();
		result.setRepository(layout.getRepository());
		result.setWorkspace(layout.getWorkspace());
		result.setLibrary(layout.getLibrary());
		result.setName(node.getName());
		for (RestProperty property: node.getJcrProperties()) {
			if ("bpw:headerEnabled".equals(property.getName())) {
				result.setHeaderEnabled(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:footerEnabled".equals(property.getName())) {
				result.setFooterEnabled(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:theme".equals(property.getName())) {
				result.setTheme(property.getValues().get(0));
			} else if (" bpw:contentWidth".equals(property.getName())) {
				result.setContentWidth(Integer.parseInt(property.getValues().get(0)));
			} 
		}

		List<LayoutRow> rows = new ArrayList<>();
		
		node.getChildren().forEach(childNode -> {
			if (this.checkNodeType(childNode, "bpw:layoutSidenav")) {
				SideNav sidenav = new SideNav();
				for (RestProperty property: childNode.getJcrProperties()) {
					if ("bpw:isLeft".equals(property.getName())) {
						sidenav.setLeft(Boolean.parseBoolean(property.getValues().get(0)));
					} else if ("bpw:width".equals(property.getName())) {
						sidenav.setWidth(Integer.parseInt(property.getValues().get(0)));
					} 
				}
				sidenav.setViewers(this.resolveResourceViewer(childNode));
				result.setSidenav(sidenav);
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
				.filter(node -> this.checkNodeType(node, "bpw:layoutColumn"))
				.map(this::toLayoutColumn)
				.toArray(LayoutColumn[]::new);
		row.setColumns(columns);
		return row;
	}
	
	private LayoutColumn toLayoutColumn(RestNode node) {
		LayoutColumn column = new LayoutColumn();
		for (RestProperty property: node.getJcrProperties()) {
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
				for (RestProperty property: restNode.getJcrProperties()) {
					if ("bpw:renderTemplayeName".equals(property.getName())) {
						viewer.setRenderTemplate(property.getValues().get(0));
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
		for (RestProperty restProperty: node.getJcrProperties()) {
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
				metadata.setSelectOptions(restProperty.getValues().toArray(new String[restProperty.getValues().size()]));
			}
		}
		return metadata;
	}
	
	private ControlField toControlField(RestNode node) {
		ControlField controlField = new ControlField();
		controlField.setName(node.getName());
		for (RestProperty restProperty: node.getJcrProperties()) {
			if ("bpw:title".equals(restProperty.getName())) {
				controlField.setTitle(restProperty.getValues().get(0));
			} else if ("bpw:icon".equals(restProperty.getName())) {
				controlField.setIcon(restProperty.getValues().get(0));
			}
		}
		ControlFieldMetadata[] controlFieldMetadata = node.getChildren().stream()
				.filter(this::isControlFieldMetaData)
				.map(this::toControlFieldMetaData)
				.toArray(ControlFieldMetadata[]::new);
		controlField.setControlFieldMetaData(controlFieldMetadata);
		return controlField;
	}
	
	private Stream<PageLayout> getWorkspaces(PageLayout pageLayout, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestWorkspaces workspaces = this.repositoryHandler.getWorkspaces(request, pageLayout.getRepository());
			return workspaces.getWorkspaces().stream().map(workspace -> this.toPageLayout(workspace, pageLayout.getRepository()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<Theme> getWorkspaces(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestWorkspaces workspaces = this.repositoryHandler.getWorkspaces(request, theme.getRepositoryName());
			return workspaces.getWorkspaces().stream().map(workspace -> this.toTheme(workspace, theme.getRepositoryName()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<RenderTemplate> getWorkspaces(RenderTemplate rt, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestWorkspaces workspaces = this.repositoryHandler.getWorkspaces(request, rt.getRepository());
			return workspaces.getWorkspaces().stream().map(workspace -> this.toRenderTemplate(workspace, rt.getRepository()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<Theme> getLibraries(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, theme.getRepositoryName(), theme.getWorkspace(), "/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).map(node -> toThemeWithLibrary(node, theme));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<PageLayout> getLibraries(PageLayout pageLayout, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, pageLayout.getRepository(), pageLayout.getWorkspace(), "/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).map(node -> toPageLayoutWithLibrary(node, pageLayout));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<RenderTemplate> getLibraries(RenderTemplate rt, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, rt.getRepository(), rt.getWorkspace(), "/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).map(node -> toRenderTemplateWithLibrary(node, rt));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<Theme> getThemes(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(request, theme.getRepositoryName(), theme.getWorkspace(), "/bpwizard/library/" + theme.getLibrary() + "/theme", 1);
			return themeNode.getChildren().stream().filter(this::isTheme).map(node -> this.toTheme(node, theme));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<RenderTemplate> getRenderTemplates(RenderTemplate rt, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(request, rt.getRepository(), rt.getWorkspace(), "/bpwizard/library/" + rt.getLibrary() + "/renderTemplate", 1);
			return themeNode.getChildren().stream().filter(this::isRenderTemplate).map(node -> this.toRenderTemplate(node, rt));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<PageLayout> getPagelayouts(PageLayout pageLayout, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode pageLayoutNode = (RestNode) this.itemHandler.item(request, pageLayout.getRepository(), pageLayout.getWorkspace(), "/bpwizard/library/" + pageLayout.getLibrary() + "/pageLayout", 4);
			return pageLayoutNode.getChildren().stream().filter(this::isPageLayout).map(node -> this.toPageLayout(node, pageLayout));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private boolean isControlField(RestNode node) {		
		return this.checkNodeType(node, "bpw:controlField");
	}

	private boolean isControlFieldMetaData(RestNode node) {		
		return this.checkNodeType(node, "bpw:controlFieldMetaData");
	}
	
	private boolean isLibrary(RestNode node) {
		return this.checkNodeType(node, "bpw:library");
	}
	
	private boolean isTheme(RestNode node) {
		return this.checkNodeType(node, "bpw:themeType");
	}

	
	private boolean isRenderTemplate(RestNode node) {
		return this.checkNodeType(node, "bpw:renderTemplate");
	}
	
	private boolean isPageLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:pageLayout");
	}
	
	private boolean isAuthortingTemplate(RestNode node) {
		return this.checkNodeType(node, "bpw:authoringTemplate");
	}
	
	private boolean isElementNode(RestNode node) {
		return this.checkNodeType(node, "bpw:elementFolder");
	}
	
	private boolean checkNodeType(RestNode node, String nodeType) {
		return node.getJcrProperties().stream().anyMatch(property-> "jcr:primaryType".equals(property.getName()) && property.getValues().contains(nodeType));
	}
	
	private AuthoringTemplate toAuthoringTemplate(Repository repo) {
		AuthoringTemplate at = new AuthoringTemplate();
		at.setRepository(repo.getName());
		return at;
	}
	
	private RenderTemplate toRenderTemplate(Repository repo) {
		RenderTemplate rt = new RenderTemplate();
		rt.setRepository(repo.getName());
		return rt;
	}
	
	private Stream<AuthoringTemplate> getWorkspaces(AuthoringTemplate at, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestWorkspaces workspaces = this.repositoryHandler.getWorkspaces(request, at.getRepository());
			return workspaces.getWorkspaces().stream().map(workspace -> this.toAuthoringTemplate(workspace, at.getRepository()));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private AuthoringTemplate toAuthoringTemplate(Workspace workspace, String repo) {
		AuthoringTemplate at = new AuthoringTemplate();
		at.setRepository(repo);
		at.setWorkspace(workspace.getName());
		return at;
	}
	
	private Stream<AuthoringTemplate> getLibraries(AuthoringTemplate at, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode bpwizardNode = (RestNode) this.itemHandler.item(request, at.getRepository(), at.getWorkspace(), "/bpwizard/library", 1);
			return bpwizardNode.getChildren().stream().filter(this::isLibrary).map(node -> toAuthoringTemplateWithLibrary(node, at));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private Stream<AuthoringTemplate> getAuthoringTemplates(AuthoringTemplate at, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode atNode = (RestNode) this.itemHandler.item(request, at.getRepository(), at.getWorkspace(), "/bpwizard/library/" + at.getLibrary() + "/authoringTemplate", 5);
			return atNode.getChildren().stream().filter(this::isAuthortingTemplate).map(node -> this.toAuthoringTemplate(node, at));
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new WcmRepositoryException(e);
		}
	}
	
	private AuthoringTemplate toAuthoringTemplateWithLibrary(RestNode node, AuthoringTemplate at) {
		AuthoringTemplate authoringTemplateWithLibrary = new AuthoringTemplate();
		authoringTemplateWithLibrary.setRepository(at.getRepository());
		authoringTemplateWithLibrary.setWorkspace(at.getWorkspace());
		authoringTemplateWithLibrary.setLibrary(node.getName());
		return authoringTemplateWithLibrary;
	}
	
	private AuthoringTemplate toAuthoringTemplate(RestNode node, AuthoringTemplate at) {
		AuthoringTemplate result = new AuthoringTemplate();
		result.setRepository(at.getRepository());
		result.setWorkspace(at.getWorkspace());
		result.setLibrary(at.getLibrary());
		result.setName(node.getName());
		for (RestProperty properties: node.getJcrProperties()) {
			if ("bpw:baseResourceType".equals(properties.getName())) {
				result.setBaseResourceType(properties.getValues().get(0));
			}
		}
		this.populateAuthoringTemplate(result, node);
		return result;
	}
	
	private void resolveResourceNode(ResourceNode resourceNode, RestNode restNode) {
		for (RestProperty property: restNode.getJcrProperties()) {
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
			} 
		}
		return formControl;
	}
	
	private void populateFormControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
			.filter(node -> this.checkNodeType(node, "bpw:formControl"))
			.map(this::toFormControl).collect(Collectors.toMap(FormControl::getName, Function.identity()));
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
		FormTab tabs[] = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formTab"))
				.map(this::populateFormTab)
				.toArray(FormTab[]::new);
		formTabs.setTabs(tabs);
		return formTabs;
	}

	private FormRows populateFormRows(RestNode restNode) {
		FormRows formRows = new FormRows();
		populateBaseFormGroup(formRows, restNode);
		FormRow rows[] = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formRow"))
				.map(this::populateFormRow)
				.toArray(FormRow[]::new);
		formRows.setRows(rows);
		return formRows;
	}
	
	private FormStep populateFormStep(RestNode restNode) {
		FormStep formStep = new FormStep();
		formStep.setStepName(restNode.getName());
		for (RestProperty property: restNode.getJcrProperties()) {
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
		for (RestProperty property: restNode.getJcrProperties()) {
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
		for (RestProperty property: restNode.getJcrProperties()) {
			if ("bpw:rowName".equals(property.getName())) {
				formRow.setRowTitle(property.getValues().get(0));
				break;
			}
		}
		FormColumn columns[] = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formColumn"))
				.map(this::populateFormColumn)
				.toArray(FormColumn[]::new);
		formRow.setColumns(columns);		
		return formRow;
	}
	
	private FormSteps populateFormSteps(RestNode restNode) {
		FormSteps formSteps = new FormSteps();
		populateBaseFormGroup(formSteps, restNode);
		FormStep steps[]= restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formStep"))
				.map(this::populateFormStep)
				.toArray(FormStep[]::new);
		formSteps.setSteps(steps);
		return formSteps;
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
}
