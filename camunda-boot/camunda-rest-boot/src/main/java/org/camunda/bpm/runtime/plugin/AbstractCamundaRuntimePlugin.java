package org.camunda.bpm.runtime.plugin;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCamundaRuntimePlugin extends AbstractPlugin implements CamundaRuntimePlugin {

	public List<String> getMappingFiles() {
		return Collections.emptyList();
	}

}