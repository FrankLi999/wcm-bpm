package com.bpwizard.wcm.modeshape.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVM {
	@NotEmpty
	@Size(min = 1, max = 50)
	private String email;

	@NotEmpty
	private String password;
}
