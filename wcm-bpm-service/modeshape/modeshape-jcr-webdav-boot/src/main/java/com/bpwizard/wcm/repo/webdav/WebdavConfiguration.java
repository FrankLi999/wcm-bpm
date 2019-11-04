package com.bpwizard.wcm.repo.webdav;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("webdavConfiguration")
public class WebdavConfiguration {
//  public static final String INIT_CONTENT_PRIMARY_TYPE_NAMES = "org.modeshape.web.jcr.webdav.CONTENT_PRIMARY_TYPE_NAMES";
//  public static final String INIT_RESOURCE_PRIMARY_TYPES_NAMES = "org.modeshape.web.jcr.webdav.RESOURCE_PRIMARY_TYPE_NAMES";
//  public static final String INIT_NEW_FOLDER_PRIMARY_TYPE_NAME = "org.modeshape.web.jcr.webdav.NEW_FOLDER_PRIMARY_TYPE_NAME";
//  public static final String INIT_NEW_RESOURCE_PRIMARY_TYPE_NAME = "org.modeshape.web.jcr.webdav.NEW_RESOURCE_PRIMARY_TYPE_NAME";
//  public static final String INIT_NEW_CONTENT_PRIMARY_TYPE_NAME = "org.modeshape.web.jcr.webdav.NEW_CONTENT_PRIMARY_TYPE_NAME";

	@Value("${org.modeshape.web.jcr.webdav.CONTENT_PRIMARY_TYPE_NAMES:nt:resource,mode:resource}")
	private String contentPrimaryTypeNames;
	@Value("${org.modeshape.web.jcr.webdav.RESOURCE_PRIMARY_TYPES_NAMES:nt:file}")
	private String resourcePrimaryTypeNames;
	@Value("${org.modeshape.web.jcr.webdav.NEW_FOLDER_PRIMARY_TYPE_NAME:nt:folder}")
	private String newFolderPrimaryTypeNames;
	@Value("${org.modeshape.web.jcr.webdav.NEW_RESOURCE_PRIMARY_TYPE_NAME:nt:file}")
	private String newResourcePrimaryTypeNames;
	@Value("${org.modeshape.web.jcr.webdav.NEW_CONTENT_PRIMARY_TYPE_NAME:nt:resource}")
	private String newContentPrimaryTypeNames;
	
	public String getContentPrimaryTypeNames() {
		return contentPrimaryTypeNames;
	}
	public String getResourcePrimaryTypeNames() {
		return resourcePrimaryTypeNames;
	}
	public String getNewFolderPrimaryTypeNames() {
		return newFolderPrimaryTypeNames;
	}
	public String getNewResourcePrimaryTypeNames() {
		return newResourcePrimaryTypeNames;
	}
	public String getNewContentPrimaryTypeNames() {
		return newContentPrimaryTypeNames;
	}

	
}
