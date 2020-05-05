package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import javax.validation.Valid;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContentAreaLayout extends ResourceNode implements HasName {

	private static final long serialVersionUID = 1L;
	String name;
	String repository;
	String workspace;
	String library;
	int contentWidth;
	SidePane sidePane;
	@Valid
	@NotNull
	LayoutRow rows[];
	private String lockOwner;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SidePane getSidePane() {
		return sidePane;
	}
	public void setSidePane(SidePane sidePane) {
		this.sidePane = sidePane;
	}
	public LayoutRow[] getRows() {
		return rows;
	}
	public void setRows(LayoutRow[] rows) {
		this.rows = rows;
	}
	public int getContentWidth() {
		return contentWidth;
	}
	public void setContentWidth(int contentWidth) {
		this.contentWidth = contentWidth;
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
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}	
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		super.toJson(jsonNode, children);
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);

		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentAreaLayout");
		jsonNode.put("bpw:name", this.name);
		jsonNode.put("bpw:contentWidth", 80);
		
		ObjectNode sidePaneNode = JsonUtils.createObjectNode();
		children.set("sidePane", sidePaneNode);
		
		sidePaneNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentAreaSidePanel");
		sidePaneNode.put("bpw:isLeft", this.sidePane.isLeft());
		sidePaneNode.put("bpw:width", this.sidePane.getWidth());
		this.addResourceViewers(this.sidePane.getViewers(), sidePaneNode);
		
		int rowCount = 0;
		for (LayoutRow row : rows) {
			ObjectNode rowNode = JsonUtils.createObjectNode();
			children.set("row" + rowCount++, rowNode);
			ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
			rowNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, rowNodeChildren);
			rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutRow");
			int columnCount = 0;
			
			for (LayoutColumn column : row.getColumns()) {
				ObjectNode columnNode = JsonUtils.createObjectNode();
				rowNodeChildren.set("column" + columnCount++, columnNode);
				columnNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutColumn");
				columnNode.put("bpw:width", column.getWidth());
				this.addResourceViewers(column.getViewers(), columnNode);
			}
		}
		
		return jsonNode;
	}
	
	private void addResourceViewers(ResourceViewer[] viewers, ObjectNode columnNode) {
		int viewerCount = 0;
		if (viewers.length > 0) {
			ObjectNode sidePaneChildren = JsonUtils.createObjectNode(); 
			columnNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, sidePaneChildren);
			for (ResourceViewer viewer : viewers) {
				ObjectNode viewerNode = JsonUtils.createObjectNode();
				sidePaneChildren.set("viewer" + viewerCount++, viewerNode);
				
				viewerNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:resourceViewer");
				viewerNode.put("bpw:renderTemplateName", viewer.getRenderTemplate());
				viewerNode.put("bpw:title", viewer.getTitle());
				if (viewer.getContentPath() != null && viewer.getContentPath().length > 0) {
					ArrayNode contentArray = JsonUtils.creatArrayNode();
					for (String cp : viewer.getContentPath()) {
						contentArray.add(cp);
					}
					viewerNode.set("bpw:contentPath", contentArray);
				}
			}
		}
	}
	@Override
	public String toString() {
		return "ContentAreaLayout [name=" + name + ", repository=" + repository + ", workspace=" + workspace
				+ ", library=" + library + ", contentWidth=" + contentWidth + ", sidePane=" + sidePane + ", rows="
				+ Arrays.toString(rows) + ", lockOwner=" + lockOwner + "]";
	}
}
