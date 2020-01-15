package com.bpwizard.wcm.repo.rest;

import java.security.Principal;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bpwizard.wcm.repo.rest.jcr.model.ModeshapeGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.ModeshapePrincipal;

public class ModeshapeUtils {
	public static String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return currentPrincipalName;
	}
	public static void grantPermissions(Session session, String path, com.bpwizard.wcm.repo.rest.jcr.model.AccessControlEntry aclEntry) throws RepositoryException {

		AccessControlManager acm = session.getAccessControlManager();
		
		//String path = contentItem.getNodePath();
		
		String[] readPrivileges = new String[] {
		  Privilege.JCR_READ
		};
		
		String[] editorPrivileges = new String[] {
		  Privilege.JCR_READ,
		  Privilege.JCR_WRITE,
		  Privilege.JCR_REMOVE_NODE,
		  Privilege.JCR_ADD_CHILD_NODES,
		  Privilege.JCR_REMOVE_CHILD_NODES,
		  Privilege.JCR_REMOVE_CHILD_NODES
		};
		
		String[] adminPrivileges = new String[] {
			Privilege.JCR_ALL 
		};
		
		// Convert the privilege strings to Privilege instances ...
		Privilege[] readPermissions = new Privilege[readPrivileges.length];
		for (int i = 0; i < readPrivileges.length; i++) {
			readPermissions[i] = acm.privilegeFromName(readPrivileges[i]);
		}
		 
		Privilege[] editorPermissions = new Privilege[editorPrivileges.length];
		for (int i = 0; i < editorPrivileges.length; i++) {
			editorPermissions[i] = acm.privilegeFromName(editorPrivileges[i]);
		}
		
		Privilege[] adminPermissions = new Privilege[adminPrivileges.length];
		for (int i = 0; i < adminPrivileges.length; i++) {
			adminPermissions[i] = acm.privilegeFromName(adminPrivileges[i]);
		}
		
		AccessControlList acl = null;
		AccessControlPolicyIterator it = acm.getApplicablePolicies(path);
		
		if (it.hasNext()) {
		    acl = (AccessControlList)it.nextAccessControlPolicy();
		} else {
		    acl = (AccessControlList)acm.getPolicies(path)[0];
		}
		
		AccessControlEntry aces[] = acl.getAccessControlEntries();
		for (AccessControlEntry ace: aces) {
			acl.removeAccessControlEntry(ace);
		}
		
		if (aclEntry.getViewers() != null) {
			for (String viewer: aclEntry.getViewers()) {
				Principal p = resolvePrincipal(viewer);
				if (p != null) {
					acl.addAccessControlEntry(p, readPermissions);
				}
			}
		}
		
		if (aclEntry.getReviewers() != null) {
			for (String reviewer: aclEntry.getReviewers()) {
				Principal p = resolvePrincipal(reviewer);
				if (p != null) {
					acl.addAccessControlEntry(p, readPermissions);
				}
			}
		}
		
		if (aclEntry.getEditors() != null) {
			for (String editor: aclEntry.getEditors()) {
				Principal p = resolvePrincipal(editor);
				if (p != null) {
					acl.addAccessControlEntry(p, editorPermissions);
				}
			}
		}
		
		if (aclEntry.getAdmins() != null) {
			for (String admin: aclEntry.getAdmins()) {
				Principal p = resolvePrincipal(admin);
				if (p != null) {
					acl.addAccessControlEntry(p, adminPermissions);
				}
			}
		}
		
		acm.setPolicy(path, acl);
		if (aclEntry.getReviewers() != null) {
			String commentPath = String.format("%s/comments", path);
			it = acm.getApplicablePolicies(commentPath);
			if (it.hasNext()) {
			    acl = (AccessControlList)it.nextAccessControlPolicy();
			} else {
			    acl = (AccessControlList)acm.getPolicies(path)[0];
			}
			
			aces = acl.getAccessControlEntries();
			for (AccessControlEntry ace: aces) {
				acl.removeAccessControlEntry(ace);
			}
			
			
			for (String reviewer: aclEntry.getReviewers()) {
				Principal p = resolvePrincipal(reviewer);
				if (p != null) {
					acl.addAccessControlEntry(p, editorPermissions);
				}
			}
			acm.setPolicy(commentPath, acl);
		}
		session.save();
	}
	
	private static Principal resolvePrincipal(String principalName) {
		if (principalName == null || principalName.length() == 0) {return null;}
		String names[] = principalName.split(":", 2);
		return (names.length == 1) ? new ModeshapeGroup(names[0]) : "groups".equals(names[0]) ? new ModeshapeGroup(names[1]) : new ModeshapePrincipal(names[1]);
	}
}
