package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.io.IOException;
import java.util.Optional;
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
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.RowsFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.StepsFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.TabsFormGroup;
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
	
	@PostMapping(path="/{repository}/{workspace}/at", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthoringTemplate(
			@PathVariable("repository") String repositoryName,
			@PathVariable("workspace") String workspaceName,
			@RequestBody AuthoringTemplate at,
			HttpServletRequest request) throws RepositoryException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}

		Session session = repositoryManager.getSession(request, repositoryName, workspaceName);
		Node atFolder = session.getNode(String.format("/bpwizard/library/%s/authoringTemplate", "design"));
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
			String primaryFormGroupType = (g instanceof StepsFormGroup) ? "bpw:formSteps" 
					: (g instanceof TabsFormGroup) ? "bpw:formTabs"
					: (g instanceof RowsFormGroup) ? "bpw:formRows"
					: "bpw:formRow";
			Node groupNode = atNode.addNode(groupName, primaryFormGroupType);
			
			if (g instanceof StepsFormGroup) {
				this.addSteps(groupNode, ((StepsFormGroup)g).getSteps());
			} else if (g instanceof TabsFormGroup) {
				this.addTabs(groupNode, ((TabsFormGroup)g).getTabs());
			} else if (g instanceof RowsFormGroup) {
				this.addRows(groupNode, ((RowsFormGroup)g).getRows());
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
	
	private void addSteps(Node stepsNode, FormStep[] steps) throws RepositoryException {
		for (FormStep step: steps) {
			Node stepNode = stepsNode.addNode(step.getStepName(), "bpw:formStep");
			int rowCount = 0;
			for (BaseFormGroup row: step.getFormGroups()) {
				this.addRow(stepNode, (FormRow)row, "row" + rowCount++);
			}
		}
	}
	
	private void addTabs(Node tabsNode, FormTab[] tabs) throws RepositoryException {
		for (FormTab tab: tabs) {
			Node tabNode = tabsNode.addNode(tab.getTabName(), "bpw:formTab");
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
		int colCount = 0;
		for (FormColumn col : row.getColumns()) {
			String columnName = StringUtils.hasText(col.getId()) ? col.getId() : "column" + colCount; 
			this.addColumn(rowNode, col, columnName);
		}
	}
	
	private void addColumn(Node rowNode, FormColumn column, String columnNameName) throws RepositoryException {
		Node columnNode = rowNode.addNode(columnNameName, "bpw:formColumn");
		columnNode.setProperty("bpw:fxFlex", column.getFxFlex());
		if (StringUtils.hasText(column.getId())) {
			columnNode.setProperty("bpw:id", column.getId());
		}
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
	
	private Theme toTheme(Workspace workspace, String repo) {
		Theme theme = new Theme();
		theme.setRepositoryName(repo);
		theme.setWorkspace(workspace.getName());
		return theme;
	}
	
	private Theme toThemeWithLibrary(RestNode node, Theme theme) {
		Theme themeWithLibrary = new Theme();
		themeWithLibrary.setRepositoryName(theme.getRepositoryName());
		themeWithLibrary.setWorkspace(theme.getWorkspace());
		themeWithLibrary.setLibrary(node.getName());
		return themeWithLibrary;
	}
	
	private Theme toTheme(RestNode node, Theme theme) {
		Theme result = new Theme();
		result.setRepositoryName(theme.getRepositoryName());
		result.setWorkspace(theme.getWorkspace());
		result.setLibrary(theme.getLibrary());
		result.setName(node.getName());
		result.setTitle(this.getThemeTitle(node).orElse(node.getName()));
		return result;
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
	
	private boolean isControlField(RestNode node) {		
		return node.getJcrProperties().stream().anyMatch(property-> "jcr:primaryType".equals(property.getName()) && property.getValues().contains("bpw:controlField"));
	}

	private boolean isControlFieldMetaData(RestNode node) {		
		return node.getJcrProperties().stream().anyMatch(property-> "jcr:primaryType".equals(property.getName()) && property.getValues().contains("bpw:controlFieldMetaData"));
	}
	
	private Optional<String> getThemeTitle(RestNode node) {
		return node.getJcrProperties().stream().filter(property -> "bpw:title".equals(property.getName())).map(property -> property.getValues().get(0)).findFirst();
	}
	
	private Stream<Theme> getWorkspaces(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestWorkspaces workspaces = this.repositoryHandler.getWorkspaces(request, theme.getRepositoryName());
			return workspaces.getWorkspaces().stream().map(workspace -> this.toTheme(workspace, theme.getRepositoryName()));
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
	
	private Stream<Theme> getThemes(Theme theme, HttpServletRequest request) throws WcmRepositoryException {
		try {
			RestNode themeNode = (RestNode) this.itemHandler.item(request, theme.getRepositoryName(), theme.getWorkspace(), "/bpwizard/library/" + theme.getLibrary() + "/theme", 1);
			return themeNode.getChildren().stream().filter(this::isTheme).map(node -> this.toTheme(node, theme));
		} catch (RepositoryException e) {
			throw new WcmRepositoryException(e);
		}
	}
	
	private boolean isLibrary(RestNode node) {
		return node.getJcrProperties().stream().anyMatch(property-> "jcr:primaryType".equals(property.getName()) && property.getValues().contains("bpw:library"));
	}
	
	private boolean isTheme(RestNode node) {
		return node.getJcrProperties().stream().anyMatch(property-> "jcr:primaryType".equals(property.getName()) && property.getValues().contains("bpw:themeType"));
	}
}
