package com.bpwizard.spring.boot.commons.domain;
import java.io.Serializable;
import java.util.Set;

public interface SpringUser<ID extends Serializable> {

	void setEmail(String username);
	void setPassword(String password);
	Set<String> getRoleNames();
	String getPassword();
	void setCredentialsUpdatedMillis(long currentTimeMillis);
	ID getId();
	String getEmail();

}