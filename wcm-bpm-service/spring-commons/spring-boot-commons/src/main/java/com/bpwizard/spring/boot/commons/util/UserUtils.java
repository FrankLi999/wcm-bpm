package com.bpwizard.spring.boot.commons.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.security.UserDto;

public class UserUtils {

	private static final Logger log = LogManager.getLogger(UserUtils.class);

	public static final int EMAIL_MIN = 4;
	public static final int EMAIL_MAX = 250;
	public static final int UUID_LENGTH = 36;
	public static final int PASSWORD_MAX = 50;
	public static final int PASSWORD_MIN = 6;

	/**
	 * Role constants. To allow extensibility, this couldn't be made an enum
	 */
	public interface Role {

		static final String UNVERIFIED = "UNVERIFIED";
		static final String BLOCKED = "BLOCKED";
		static final String ADMIN = "admin";
		static final String USER = "user";
		static final String READWRITE = "readwrite";
		static final String READONLY = "readonly";
	}

	public interface Permission {

		static final String EDIT = "edit";
	}

	// validation groups
	public interface SignUpValidation {
	}

	public interface UpdateValidation {
	}

	public interface ChangeEmailValidation {
	}

	// JsonView for Sign up
	public interface SignupInput {
	}

	public static <ID> boolean hasPermission(ID id, UserDto currentUser, String permission) {

		log.debug("Computing " + permission + " permission for User " + id + "\n  Logged in user: " + currentUser);

		if (permission.equals("edit")) {

			if (currentUser == null)
				return false;

			boolean isSelf = currentUser.getId().equals(id.toString());
			return isSelf || currentUser.isGoodAdmin(); // self or admin;
		}

		return false;
	}
}
