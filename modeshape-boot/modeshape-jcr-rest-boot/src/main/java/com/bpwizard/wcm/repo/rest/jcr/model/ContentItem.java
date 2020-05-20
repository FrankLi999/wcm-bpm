package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentItem {
	
	private static final long serialVersionUID = -6271150516003474875L;
	private String id;
	private String repository;
	private String workspace;
	private String wcmPath;
	private String lifeCycleStage;
	private String authoringTemplate;
	private String nodeType;
	private boolean locked;
	private boolean checkedOut;
	private WorkflowNode workflow;
	private AccessControlEntry acl;
	private WcmProperties metadata;
	private SearchData searchData;
	
	private ContentItemProperties properties;
	private ContentItemElements elements;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getWcmPath() {
		return wcmPath;
	}

	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}

	public String getLifeCycleStage() {
		return lifeCycleStage;
	}

	public void setLifeCycleStage(String lifeCycleStage) {
		this.lifeCycleStage = lifeCycleStage;
	}

	public WorkflowNode getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowNode workflow) {
		this.workflow = workflow;
	}

	public AccessControlEntry getAcl() {
		return acl;
	}

	public void setAcl(AccessControlEntry acl) {
		this.acl = acl;
	}

	public WcmProperties getMetadata() {
		return metadata;
	}

	public void setMetadata(WcmProperties metadata) {
		this.metadata = metadata;
	}

	public SearchData getSearchData() {
		return searchData;
	}

	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}

	public ContentItemProperties getProperties() {
		return properties;
	}

	public void setProperties(ContentItemProperties properties) {
		this.properties = properties;
	}

	public ContentItemElements getElements() {
		return elements;
	}

	public void setElements(ContentItemElements elements) {
		this.elements = elements;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	public String getAuthoringTemplate() {
		return authoringTemplate;
	}
	public void setAuthoringTemplate(String authoringTemplate) {
		this.authoringTemplate = authoringTemplate;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	//TODO: super.toJson()
	public JsonNode toJson(AuthoringTemplate at) throws JsonProcessingException {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		// ObjectNode properties = JsonUtils.createObjectNode();
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		// jsonNode.set(WcmConstants.JCR_JSON_NODE_PROPERTIES, properties);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, at.getNodeType());
		
		if (StringUtils.hasText(this.getAuthoringTemplate())) {
			jsonNode.put("bpw:authoringTemplate", this.getAuthoringTemplate());
		}
		
		if (StringUtils.hasText(this.lifeCycleStage)) {
			jsonNode.put("bpw:lifecycleStage", this.lifeCycleStage);
		}
		
		ObjectNode comementFolderNode = JsonUtils.createObjectNode();
		ObjectNode comementFolderChildren = JsonUtils.createObjectNode();
		comementFolderNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, comementFolderChildren);
		comementFolderNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:ContentItemCommentFolder");
		children.set(WcmConstants.WCM_ITEM_COMMENTTS, comementFolderNode);
		
		ObjectNode workflowNode = JsonUtils.createObjectNode();
		workflowNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:workflowNode");
		children.set("bpw:workflow", workflowNode);
		if (this.workflow != null) {
			this.workflow.toJson(workflowNode);
		}
		
		ObjectNode aclNode = JsonUtils.createObjectNode();
		aclNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:AccessControlEntry");
		children.set("bpw:acl", aclNode);

    	if (this.getAcl() != null) {
			ObjectNode aclEntryNode = JsonUtils.createObjectNode();
			children.set("bpw:acl", aclEntryNode);
			acl.toJson(aclEntryNode);
		}

		ObjectNode metaDataNode = JsonUtils.createObjectNode();
		metaDataNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:properties");
		children.set("bpw:metadata", metaDataNode);
		if (this.getMetadata() != null) {
			ObjectNode metaDataNodeChildren = JsonUtils.createObjectNode();
			metaDataNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, metaDataNodeChildren);
			WcmProperties metadata = (WcmProperties)this.getMetadata();
			for (WcmProperty property: metadata.getProperties()) {
				
				ObjectNode kvNode = JsonUtils.createObjectNode();
				metaDataNodeChildren.set(property.getName(), kvNode);
				kvNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:property");
				kvNode.put("bpw:name", property.getName());
				kvNode.put("bpw:value", property.getValue());
			}
		} 
		
		ObjectNode searchDataNode = JsonUtils.createObjectNode();
		searchDataNode.put(JcrConstants.JCR_PRIMARY_TYPE,  "bpw:pageSearchData");
		children.set("bpw:searchData", searchDataNode);
		if (this.getSearchData() != null) {	

			SearchData searchData = (SearchData) this.getSearchData();
			if (StringUtils.hasText(searchData.getDescription())) {
				searchDataNode.put("description", searchData.getDescription());
			}
			if (searchData.getKeywords() != null) {
				String[] keywords = searchData.getKeywords();
				ArrayNode keywordArray = WcmUtils.toArrayNode(searchData.getKeywords());
				for (String keyword : keywords) {
					keywordArray.add(keyword);
				}				
				searchDataNode.set("keywords", keywordArray);
			}	
		}
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		ObjectNode elementsChildren = JsonUtils.createObjectNode();
		elementsNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, elementsChildren);
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
//		ObjectNode elementsProperties = JsonUtils.createObjectNode();
//		elementsNode.set(WcmConstants.JCR_JSON_NODE_PROPERTIES, elementsProperties);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE,  WcmUtils.getElementFolderType(at.getLibrary(), at.getName()));
		children.set(WcmConstants.WCM_ITEM_ELEMENTS, elementsNode);
		
		this.getElements().toJson(elementsNode, elementsChildren, at);
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		ObjectNode propertiesNodeChildren = JsonUtils.createObjectNode();
		propertiesNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, propertiesNodeChildren);
		children.set(WcmConstants.WCM_ITEM_PROPERTIES, propertiesNode);
//		ObjectNode propertiesProperties = JsonUtils.createObjectNode();
//		propertiesNode.set(WcmConstants.JCR_JSON_NODE_PROPERTIES, propertiesProperties);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE,  WcmConstants.JCR_TYPE_PROPERTY_FOLDER);
		
		ContentItemProperties contentItemProperties = this.getProperties();
		contentItemProperties.toJson(propertiesNode, propertiesNodeChildren);
		return jsonNode;
	}

	@Override
	public String toString() {
		return "ContentItem [id=" + id + ", repository=" + repository + ", workspace=" + workspace + ", wcmPath="
				+ wcmPath + ", lifeCycleStage=" + lifeCycleStage + ", authoringTemplate=" + authoringTemplate
				+ ", nodeType=" + nodeType + ", locked=" + locked + ", checkedOut=" + checkedOut + ", workflow="
				+ workflow + ", acl=" + acl + ", metadata=" + metadata + ", searchData=" + searchData + ", properties="
				+ properties + ", elements=" + elements + "]";
	}
}
