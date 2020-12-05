package com.bpwizard.wcm_bpm;

import java.util.List;

public class ItemNode {
	private List<String> removedNodeIds;
	private List<String> updatedNodeIds;
	private JcrNode jcrNode;
	
	public List<String> getRemovedNodeIds() {
		return removedNodeIds;
	}
	
	public void setRemovedNodeIds(List<String> removedNodeIds) {
		this.removedNodeIds = removedNodeIds;
	}
	
	public List<String> getUpdatedNodeIds() {
		return updatedNodeIds;
	}
	
	public void setUpdatedNodeIds(List<String> updatedNodeIds) {
		this.updatedNodeIds = updatedNodeIds;
	}
	
	public JcrNode getJcrNode() {
		return jcrNode;
	}

	public void setJcrNode(JcrNode jcrNode) {
		this.jcrNode = jcrNode;
	}

	@Override
	public String toString() {
		return "ItemNode [removedNodeIds=" + removedNodeIds + ", updatedNodeIds=" + updatedNodeIds + ", jcrNode="
				+ jcrNode + "]";
	}
}
