package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.camunda.bpm.engine.identity.Tenant;

import com.bpwizard.wcm.repo.domain.Role;
import com.bpwizard.wcm.repo.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tenant")
@Setter @Getter
@NoArgsConstructor
public class SpringTenant implements Tenant {
    @Id
    private String id;
    private String name;
    
	// @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@OneToMany( mappedBy= "tenent", fetch = FetchType.LAZY)
    private Set<SpringUser> users = new HashSet<>();
	
	@OneToMany( mappedBy= "tenent", fetch = FetchType.LAZY)
    private Set<SpringGroup> groups = new HashSet<>();
}
