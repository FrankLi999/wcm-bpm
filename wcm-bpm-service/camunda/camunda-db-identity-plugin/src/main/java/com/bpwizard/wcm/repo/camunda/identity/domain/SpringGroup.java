package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.camunda.bpm.engine.identity.Group;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="role")
@Setter @Getter
@NoArgsConstructor
public class SpringGroup implements Group {

    @Id
    protected String id;
    protected String name;
    protected String type;
    
	@ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
	protected Set<User> users;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tenant_membership",  
    joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id", nullable=true) },
    inverseJoinColumns = { @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable=true) })
	protected Set<SpringTenant> tenant;
	
}