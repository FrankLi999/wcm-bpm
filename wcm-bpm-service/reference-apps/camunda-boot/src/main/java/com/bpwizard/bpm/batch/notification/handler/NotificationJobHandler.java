package com.bpwizard.bpm.batch.notification.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bpwizard.bpm.batch.job.JacksonJobHandlerAdapter;
import com.bpwizard.bpm.batch.notification.client.SendNotificationCommand;
import com.bpwizard.bpm.batch.notification.service.TransmissionService;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationJobHandler extends JacksonJobHandlerAdapter<NotificationJobHandlerConfiguration> {

	private final TransmissionService service;

	public NotificationJobHandler(final ObjectMapper mapper, final TransmissionService service) {
		super(mapper, NotificationJobHandlerConfiguration.class);
		this.service = service;
	}

	@Override
	public void execute(final NotificationJobHandlerConfiguration configuration, final ExecutionEntity execution,
			final CommandContext commandContext, final String tenantId) {
		this.service.transmitNotification(configuration.getRecipient(), configuration.getSubject(),
				configuration.getBody());
	}

	@Override
	public String getType() {
		return SendNotificationCommand.TYPE;
	}
}
