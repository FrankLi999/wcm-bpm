package com.bpwizard.spring.boot.commons.service.domain;

import java.util.HashSet;
import java.util.Set;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bpwizard.spring.boot.commons.jdbc.SpringEntity;
import com.bpwizard.spring.boot.commons.util.UserUtils;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name="tenant", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Getter @Setter 
@NoArgsConstructor
public class Tenant extends SpringEntity<Long> {

	private static final long serialVersionUID = 753572271264759941L;
	public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 50;
    
	@JsonView(UserUtils.SignupInput.class)
	@NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Column(nullable = false, length = NAME_MAX)
    protected String name;
	
	// @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	// @ManyToMany( mappedBy= "tenants", fetch = FetchType.LAZY)
    private Set<String> users = new HashSet<>();
	
	// @ManyToMany( mappedBy= "tenants", fetch = FetchType.LAZY)
    private Set<String> roles = new HashSet<>();
}
