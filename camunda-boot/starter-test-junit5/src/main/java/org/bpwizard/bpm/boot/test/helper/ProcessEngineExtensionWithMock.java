package org.bpwizard.bpm.boot.test.helper;

import org.camunda.bpm.engine.ProcessEngine;

public class ProcessEngineExtensionWithMock extends ProcessEngineExtension {
    public ProcessEngineExtensionWithMock() {
        super();
    }
    
    protected ProcessEngine getNewProcessEngine() {
    	final ProcessEngine processEngine = new StandaloneInMemoryTestConfigurationWithMock().buildProcessEngine();
    	return processEngine;
    }
}
