package com.bpwizard.spring.boot.commons.security;
import  java.security.Principal;
import java.util.Enumeration;
public interface Group extends Principal {
	/**
	 * Adds the specified member to the group.
	 * @param user the principal to add to this group.
	 * @return true if the member was successfully added, false if the principal was already a member.
	 */
	boolean	addMember​(Principal user);
	
	/**
	 * Returns true if the passed principal is a member of the group. This method does a 
	 * recursive search, so if a principal belongs to a group which is a member of this 
	 * group, true is returned.
	 * @param member the principal whose membership is to be checked.
	 * @return true if the principal is a member of this group, false otherwise.
	 */
	boolean	isMember​(Principal member);

	/**
	 * Returns an enumeration of the members in the group. The returned objects can be 
	 * instances of either Principal or Group (which is a subclass of Principal).
	 * @return an enumeration of the members in the group.
	 */
	Enumeration<? extends Principal>	members();
	
	/**
	 * Removes the specified member from the group.
	 * @param user the principal to remove from this group.
	 * @return true if the principal was removed, or false if the principal was not a member.
	 */
	boolean	removeMember​(Principal user);
}
