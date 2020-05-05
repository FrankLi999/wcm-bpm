package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentItemProperties extends ResourceMixin {

	private String authoringTemplate;
	private String nodeType;
	private String workflow;
	private String[] categories;
	
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
	public String getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	
	protected void toJson(ObjectNode jsonNode, ObjectNode jsonNodeChildren) {
		super.toJson(jsonNode);
		if (StringUtils.hasText(this.getAuthoringTemplate())) {
			jsonNode.put("bpw:authoringTemplate", this.getAuthoringTemplate());
		}
		
		if (StringUtils.hasText(this.getWorkflow())) {
			jsonNode.put("bpw:workflow", this.getWorkflow());
		}
		if (categories != null && categories.length > 0) {
			ArrayNode valueArray = WcmUtils.toArrayNode(categories);
			jsonNode.set("bpw:categories", valueArray);
		}
	}
	
	@Override
	public String toString() {
		return "ContentItemProperties [authoringTemplate=" + authoringTemplate + ", nodeType=" + nodeType
				+ ", workflow=" + workflow + ", categories=" + Arrays.toString(categories) + ", getName()=" + getName()
				+ ", getTitle()=" + getTitle() + ", getDescription()=" + getDescription() + "]";
	}
}
