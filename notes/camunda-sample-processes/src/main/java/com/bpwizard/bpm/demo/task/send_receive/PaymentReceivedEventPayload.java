package com.bpwizard.bpm.demo.task.send_receive;

public class PaymentReceivedEventPayload {
	private String refId;

	public String getRefId() {
		return refId;
	}

	public PaymentReceivedEventPayload setRefId(String refId) {
		this.refId = refId;
		return this;
	}
}
