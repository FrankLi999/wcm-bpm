package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.camunda.bpm.engine.identity.Group;

import com.bpwizard.wcm.repo.domain.User;

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
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinTable(name = "tenant_membership",  
    joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id", nullable=true) },
    inverseJoinColumns = { @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable=true) })
	protected SpringTenant tenant;
	
}