package org.camunda.bpm.runtime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CamundaRuntimeBootstrap { //implements ApplicationListener<ApplicationReadyEvent> {

	// private static final Logger LOGGER = LogManager.getLogger(CamundaRuntimeBootstrap.class);
	private CamundaRuntimeEnvironment environment;
	@Autowired
	private ProcessEngine processEngine;

	@PostConstruct
	public void postConstruct() {
		environment = createCamundaRuntimeEnvironment();
		environment.setup();
		
	}
	
	@PreDestroy
	public void preDestroy() {
		environment.tearDown();
	}

	protected CamundaRuntimeEnvironment createCamundaRuntimeEnvironment() {
		return new CamundaRuntimeEnvironment(this.processEngine);
	}

	protected static class CamundaRuntimeEnvironment {
		private ProcessEngine processEngine;
		CamundaRuntimeEnvironment(ProcessEngine processEngine) {
			this.processEngine = processEngine;
		}
		public void tearDown() {
			CamundaRuntime.setCamundaRuntimeDelegate(null);
		}

		public void setup() {
			CamundaRuntime.setCamundaRuntimeDelegate(new DefaultCamundaRuntimeDelegate(this.processEngine));
		}

		protected RuntimeContainerDelegate getContainerRuntimeDelegate() {
			return RuntimeContainerDelegate.INSTANCE.get();
		}
	}

}
