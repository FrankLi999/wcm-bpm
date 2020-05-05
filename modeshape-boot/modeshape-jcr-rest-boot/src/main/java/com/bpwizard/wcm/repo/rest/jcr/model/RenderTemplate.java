package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RenderTemplate extends ResourceNode implements HasName {

	private static final long serialVersionUID = 1L;
	private String repository;
	private String workspace;
	private String library;
	private String name;
	private String code;
	private String preloop;
	private String postloop;
	private int maxEntries;
	private String note;
	private boolean query;
	private String resourceName;
	private RenderTemplateLayoutRow rows[];
	
	private String lockOwner;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPreloop() {
		return preloop;
	}
	public void setPreloop(String preloop) {
		this.preloop = preloop;
	}
	public String getPostloop() {
		return postloop;
	}
	public void setPostloop(String postloop) {
		this.postloop = postloop;
	}
	public int getMaxEntries() {
		return maxEntries;
	}
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isQuery() {
		return query;
	}
	public void setQuery(boolean query) {
		this.query = query;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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
	public RenderTemplateLayoutRow[] getRows() {
		return rows;
	}
	public void setRows(RenderTemplateLayoutRow[] rows) {
		this.rows = rows;
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
		
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:renderTemplate");
		super.toJson(jsonNode, children);
		jsonNode.put("bpw:name", this.getName());
		jsonNode.put("bpw:title", StringUtils.hasText(this.getTitle()) ? this.getTitle() : this.getName());
		if (StringUtils.hasText(this.getDescription())) {
			jsonNode.put("bpw:description", this.getDescription());
		}
		if (StringUtils.hasText(this.getCode())) {
			jsonNode.put("bpw:code", this.getCode());
		}
		if (StringUtils.hasText(this.getPreloop())) {
			jsonNode.put("bpw:preloop", this.getPreloop());
		}
		if (StringUtils.hasText(this.getPostloop())) {
			jsonNode.put("bpw:postloop", this.getPostloop());
		}
		jsonNode.put("bpw:maxEntries", this.getMaxEntries());
		if (StringUtils.hasText(this.getNote())) {
			jsonNode.put("bpw:note", this.getNote());
		}
		if (StringUtils.hasText(this.getResourceName())) {
			jsonNode.put("bpw:resourceName", this.getResourceName());
		}
		jsonNode.put("bpw:isQuery", this.isQuery());
		
		if (this.getRows() != null && this.getRows().length > 0) {
			int rowCount = 1;
			for(RenderTemplateLayoutRow row: this.getRows()) {
				ObjectNode rowNode = JsonUtils.createObjectNode();
				ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
				children.set("row" + rowCount++, rowNode);
				rowNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, rowNodeChildren);
				rowNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:RenderTemplateLayoutRow");

				int columnCount = 1;
				for (RenderTemplateLayoutColumn column: row.getColumns()) {
					
					ObjectNode columnNode = JsonUtils.createObjectNode();
					ObjectNode columnNodeChildren = JsonUtils.createObjectNode();
					rowNodeChildren.set("column" + columnCount++, columnNode);
					columnNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, columnNodeChildren);
					columnNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:RenderTemplateLayoutColumn");
					
					if (StringUtils.hasText(column.getId())) {
						columnNode.put("bpw:id", column.getId());
					}
					if (column.getWidth() > 0) {
						columnNode.put("bpw:width", column.getWidth());
					}
					for (ResourceElementRender element: column.getElements()) {
						ObjectNode elementNode = JsonUtils.createObjectNode();
						columnNodeChildren.set(element.getName(), elementNode);
						elementNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:ResourceElementRender");
						elementNode.put("bpw:name", element.getName());
						elementNode.put("bpw:source", element.getSource());
					}				    
				}
			}
		}
		return jsonNode;
	}
	@Override
	public String toString() {
		return "RenderTemplate [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", name=" + name + ", code=" + code + ", preloop=" + preloop + ", postloop=" + postloop
				+ ", maxEntries=" + maxEntries + ", note=" + note + ", query=" + query + ", resourceName="
				+ resourceName + ", rows=" + Arrays.toString(rows) + ", lockOwner=" + lockOwner + ", toString()="
				+ super.toString() + "]";
	}
}
