/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bpwizard.wcm.repo.rest.modeshape.model;

import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.version.OnParentVersionAction;

/**
 * A REST representation of a {@link PropertyDefinition}
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public final class RestPropertyType { //implements JSONAble {

    private final String declaringNodeTypeName;
    private final boolean isAutoCreated;
    private final boolean isMandatory;
    private final boolean isProtected;
    private final String onParentVersion;
    private final String name;
    private final boolean isMultiple;
    private final boolean isFullTextSearchable;
    private final String requiredType;

    /**
     * Creates a new property definition.
     *
     * @param definition a {@code non-null} {@link PropertyDefinition}
     */
    public RestPropertyType( PropertyDefinition definition ) {
        this.name = definition.getName();
        this.requiredType = org.modeshape.jcr.api.PropertyType.nameFromValue(definition.getRequiredType());
        NodeType declaringNodeType = definition.getDeclaringNodeType();
        this.declaringNodeTypeName = declaringNodeType == null ? null : declaringNodeType.getName();
        this.isAutoCreated = definition.isAutoCreated();
        this.isMandatory = definition.isMandatory();
        this.isProtected = definition.isProtected();
        this.isFullTextSearchable = definition.isFullTextSearchable();
        this.onParentVersion = OnParentVersionAction.nameFromValue(definition.getOnParentVersion());
        this.isMultiple = definition.isMultiple();
    }

	public String getDeclaringNodeTypeName() {
		return declaringNodeTypeName;
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

	public String getOnParentVersion() {
		return onParentVersion;
	}

	public String getName() {
		return name;
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public boolean isFullTextSearchable() {
		return isFullTextSearchable;
	}

	public String getRequiredType() {
		return requiredType;
	}

//    @Override
//    public JSONObject toJSON() throws JSONException {
//        JSONObject content = new JSONObject();
//        content.put("requiredType", this.requiredType);
//        if (!StringUtil.isBlank(declaringNodeTypeName)) {
//            content.put("declaringNodeTypeName", this.declaringNodeTypeName);
//        }
//        content.put("mandatory", isMandatory);
//        content.put("multiple", isMultiple);
//        content.put("autocreated", isAutoCreated);
//        content.put("protected", isProtected);
//        content.put("fullTextSearchable", isFullTextSearchable);
//        content.put("onParentVersion", onParentVersion);
//
//        JSONObject object = new JSONObject();
//        object.put(name, content);
//        return object;
//    }
    
    
}
