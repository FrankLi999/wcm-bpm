package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.camunda.bpm.engine.identity.Tenant;

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
    
	@ManyToMany( mappedBy= "tenent", fetch = FetchType.LAZY)
    private Set<SpringUser> users = new HashSet<>();
	
	@ManyToMany( mappedBy= "tenent", fetch = FetchType.LAZY)
    private Set<SpringGroup> groups = new HashSet<>();
}
