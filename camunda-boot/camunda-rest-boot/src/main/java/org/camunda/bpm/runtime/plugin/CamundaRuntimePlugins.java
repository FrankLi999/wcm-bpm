package org.camunda.bpm.runtime.plugin;

import java.util.Arrays;
import java.util.List;

public class CamundaRuntimePlugins extends AbstractCamundaRuntimePlugin {

	  public static final String ID = "cockpitPlugins";

	  private static final String[] MAPPING_FILES = {
	    "org/camunda/bpm/runtime/plugin/queries/processDefinition.xml",
	    "org/camunda/bpm/runtime/plugin/queries/processInstance.xml",
	    "org/camunda/bpm/runtime/plugin/queries/incident.xml"
	  };

	  @Override
	  public List<String> getMappingFiles() {
	    return Arrays.asList(MAPPING_FILES);
	  }

	  @Override
	  public String getId() {
	    return ID;
	  }

//	  @Override
//	  public Set<Class<?>> getResourceClasses() {
//	    HashSet<Class<?>> classes = new HashSet<Class<?>>();
//
//	    classes.add(CockpitPluginsRootResource.class);
//	    classes.add(BaseRootResource.class);
//
//	    return classes;
//	  }
//
//	  @Override
//	  public String getAssetDirectory() {
//	    return "plugin/cockpit";
//	  }

	}
