package com.bpwizard.wcm.repo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.BaseFormGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.FieldLayout;
import com.bpwizard.wcm.repo.rest.jcr.model.FormColumn;
import com.bpwizard.wcm.repo.rest.jcr.model.FormControl;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRow;
import com.bpwizard.wcm.repo.rest.jcr.model.FormRows;
import com.bpwizard.wcm.repo.rest.jcr.model.FormStep;
import com.bpwizard.wcm.repo.rest.jcr.model.FormSteps;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTab;
import com.bpwizard.wcm.repo.rest.jcr.model.FormTabs;
import com.bpwizard.wcm.repo.rest.jcr.model.ResourceNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;

@Component
public class WcmUtils {
	private static final Logger logger = LogManager.getLogger(WcmUtils.class);
	@Autowired
	private RestItemHandler itemHandler;
	@Autowired
	protected RepositoryManager repositoryManager;
	
	public AuthoringTemplate getAuthoringTemplate(
			String repository,
			String workspace, 
			String wcmAtPath,
			String baseUrl) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String library = WcmUtils.library(wcmAtPath);
			String absPath = WcmUtils.nodePath(wcmAtPath);
			RestNode atNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					absPath, 8);
			
			AuthoringTemplate at = this.toAuthoringTemplate(atNode, repository, workspace, library);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return at;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	public AuthoringTemplate toAuthoringTemplate(RestNode node, String repository, String workspace, String library) {
		AuthoringTemplate at = new AuthoringTemplate();
		at.setRepository(repository);
		at.setWorkspace(workspace);
		at.setLibrary(library);
		at.setName(node.getName());
		this.resolveResourceNode(at, node);
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:baseResourceType".equals(property.getName())) {
				at.setBaseResourceType(property.getValues().get(0));
			} else if ("bpw:contentWorkflow".equals(property.getName())) {
				at.setContentItemWorkflow(property.getValues().get(0));
			} 
		}
		this.populateAuthoringTemplate(at, node);
		return at;
	}
	
	public void resolveResourceNode(ResourceNode resourceNode, RestNode restNode) {
		for (RestProperty property : restNode.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				resourceNode.setTitle(property.getValues().get(0));
			} else if ("bpw:description".equals(property.getName())) {
				resourceNode.setDescription(property.getValues().get(0));
			} else if ("jcr:lockOwner".equals(property.getName())) {
				resourceNode.setLockOwner(property.getValues().get(0));
			}				
		}
	}
	
	public boolean checkNodeType(RestNode node, String nodeType) {
		return node.getJcrProperties().stream().anyMatch(
				property -> "jcr:primaryType".equals(property.getName()) && property.getValues().contains(nodeType));
	}
	
	public void unlock(
			String repository,
			String workspace, 
			String absPath) throws RepositoryException {
		
		javax.jcr.lock.LockManager lm = this.repositoryManager.getSession(repository, workspace).getWorkspace().getLockManager();
		
		if (lm.isLocked(absPath)) {
			lm.unlock(absPath);
		}
	}
	
	private boolean isCustomeFieldLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:fieldLayout");
	}

	private boolean isFieldLayout(RestNode node) {
		return this.checkNodeType(node, "bpw:fieldLayout");
	}

	
	private void populateAuthoringTemplate(AuthoringTemplate at, RestNode restNode) {
		List<BaseFormGroup> formGroups = new ArrayList<>();
		restNode.getChildren().forEach(node -> {
			if (this.isElementFolderNode(node)) {
				this.populateElementControls(at, node);
			} else if (this.isPropertyFolderNode(node)) {
				this.populatePropertyControls(at, node);
			} else if (this.isPropertyGroupNode(node)) {
				at.setPropertyRow(this.populateFormRow(node));
			} else if (this.isElementGroupNode(node)) {
				node.getChildren().forEach(groupNode -> {
					Optional<BaseFormGroup> group = this.populateFormGroups(groupNode);
					if (group.isPresent()) {
						formGroups.add(group.get());
					}
				});
				at.setElementGroups(formGroups.toArray(new BaseFormGroup[formGroups.size()]));
			}
		});
		
	}
	
	private boolean isElementFolderNode(RestNode node) {
		return this.checkNodeType(node, "bpw:elementFolder");
	}

	private boolean isPropertyFolderNode(RestNode node) {
		return this.checkNodeType(node, "bpw:propertyFolder");
	}
	
	private boolean isPropertyGroupNode(RestNode node) {
		return this.checkNodeType(node, "bpw:formRow") && "propertyGroup".equals(node.getName());
	}
	
	private boolean isElementGroupNode(RestNode node) {
		return this.checkNodeType(node, "bpw:formGroupFoler");
	}
	
	private void populateElementControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		at.setElements(formControls);
	}

	private void populatePropertyControls(AuthoringTemplate at, RestNode restNode) {
		Map<String, FormControl> formControls = restNode.getChildren().stream()
				.filter(node -> this.checkNodeType(node, "bpw:formControl")).map(this::toFormControl)
				.collect(Collectors.toMap(FormControl::getName, Function.identity()));
		at.setProperties(formControls);
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
	
	private FormControl toFormControl(RestNode node) {
		FormControl formControl = new FormControl();
		formControl.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:title".equals(property.getName())) {
				formControl.setTitle(property.getValues().get(0));
			} else if ("bpw:jsonPath".equals(property.getName())) {
				formControl.setJsonPath(property.getValues().get(0));
			} else if ("bpw:controlType".equals(property.getName())) {
				formControl.setControlType(property.getValues().get(0));
			} else if ("bpw:format".equals(property.getName())) {
				formControl.setFormat(property.getValues().get(0));
			} else if ("bpw:enum".equals(property.getName())) {
				formControl.setEnumeration(property.getValues().toArray(new String[property.getValues().size()]));
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
			} else if ("bpw:valdition".equals(property.getName())) {
				formControl.setValdition(property.getValues().get(0));
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
			} else if ("bpw:jcrDataType".equals(property.getName())) {
				formControl.setJcrDataType(property.getValues().get(0));
			} else if ("bpw:editable".equals(property.getName())) {
				formControl.setEditable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:expandable".equals(property.getName())) {
				formControl.setExpandable(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:richText".equals(property.getName())) {
				formControl.setRichText(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:rows".equals(property.getName())) {
				formControl.setRows(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:flex".equals(property.getName())) {
				formControl.setFlex(property.getValues().get(0));
			} else if ("bpw:placeholder".equals(property.getName())) {
				formControl.setPlaceholder(property.getValues().get(0));
			} 
		}
		return formControl;
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
					.map(this::toAssociationLayout).toArray(FieldLayout[]::new);

			column.setFieldLayouts(customeFieldLayouts);
		}

		return column;
	}
	
	private FieldLayout toAssociationLayout(RestNode node) {
		FieldLayout associationLayout = new FieldLayout();
		associationLayout.setName(node.getName());
		for (RestProperty property : node.getJcrProperties()) {
			if ("bpw:multiple".equals(property.getName())) {
				associationLayout.setMultiple(Boolean.parseBoolean(property.getValues().get(0)));
			} else if ("bpw:key".equals(property.getName())) {
				associationLayout.setKey(property.getValues().get(0));
			} else if ("bpw:name".equals(property.getName())) {
				associationLayout.setName(property.getValues().get(0));
			} else if ("bpw:title".equals(property.getName())) {
				associationLayout.setTitle(property.getValues().get(0));
			} else if ("bpw:items".equals(property.getName())) {
				associationLayout.setItems(property.getValues().get(0));
			} 
		}
		if (StringUtils.isEmpty(associationLayout.getKey())) {
			associationLayout.setKey(associationLayout.getName());
		}

		FieldLayout[] fieldLayouts = node.getChildren().stream().filter(this::isFieldLayout).map(this::toFieldLayout)
				.toArray(FieldLayout[]::new);

		associationLayout.setFieldLayouts(fieldLayouts);

		return associationLayout;
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
	
	public static String nodePath(String wcmPath) {
		return String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, 
				wcmPath);
	}
	
	public static String nodePath(String wcmPath, String name) {
		return String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_PATH_PATTERN : WcmConstants.NODE_REL_PATH_PATTERN, 
				wcmPath, name);
	}

	public static String library(String wcmPath) {
		return wcmPath.startsWith("/") ? wcmPath.split("/", 3)[1] : wcmPath.split("/", 2)[0];	
	}
}
