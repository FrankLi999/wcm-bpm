package com.bpwizard.wcm.repo.rest.modeshape.model;

import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.OnParentVersionAction;

public class RestChildType {
	
//    private final Set<String> superTypesLinks;
//    private final Set<String> subTypesLinks;
	private final String declaringNodeTypeName;
	private String name;
	private String defaultPrimaryTypeName;
	private final String onParentVersion;
    private final boolean isAutoCreated;
    private final boolean isMandatory;
    private final boolean isProtected;
    private String requiredPrimaryTypeNames[];
    
	public RestChildType(NodeDefinition childNode) {
		this.name = childNode.getName();
		this.defaultPrimaryTypeName = childNode.getDefaultPrimaryTypeName();
		this.isAutoCreated = childNode.isAutoCreated();
		this.isMandatory = childNode.isProtected();
		this.isProtected = childNode.isMandatory();
		this.onParentVersion = OnParentVersionAction.nameFromValue(childNode.getOnParentVersion());
		
		NodeType declaringNodeType = childNode.getDeclaringNodeType();
        this.declaringNodeTypeName = declaringNodeType == null ? null : declaringNodeType.getName();
        this.requiredPrimaryTypeNames = childNode.getRequiredPrimaryTypeNames();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultPrimaryTypeName() {
		return defaultPrimaryTypeName;
	}

	public void setDefaultPrimaryTypeName(String defaultPrimaryTypeName) {
		this.defaultPrimaryTypeName = defaultPrimaryTypeName;
	}

	public String getDeclaringNodeTypeName() {
		return declaringNodeTypeName;
	}

	public String[] getRequiredPrimaryTypeNames() {
		return requiredPrimaryTypeNames;
	}

	public String getOnParentVersion() {
		return onParentVersion;
	}

	public boolean isAutoCreated() {
		return isAutoCreated;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public boolean isProtected() {
		return isProtected;
	}
}
