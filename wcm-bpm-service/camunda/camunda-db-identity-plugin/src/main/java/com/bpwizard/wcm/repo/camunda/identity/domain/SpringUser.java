package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.camunda.bpm.engine.identity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usr")
@Setter @Getter
@NoArgsConstructor
public class SpringUser implements User {

    @Id
    protected String id;

    protected String firstName;
    protected String lastName;

    protected String email;
    protected String password;

    @ManyToMany()
	@JoinTable(name = "user_role", 
	    joinColumns = @JoinColumn(name = "user_id"), 
	    inverseJoinColumns = @JoinColumn(name = "role_id"))
	protected Set<SpringGroup> groups = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tenant_membership",  
    joinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id", nullable=true) },
    inverseJoinColumns = { @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable=true) })
	protected Set<SpringTenant> tenants;
	
}
