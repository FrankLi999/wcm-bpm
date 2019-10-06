package com.bpwizard.wcm.repo.hazelcast.demo.model;

import java.io.Serializable;

public class HazelcastUser implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
    private String name;
    private String email;

    public HazelcastUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public HazelcastUser setName(String name) {
        this.name = name;
        return this;
    }

    public HazelcastUser setEmail(String email) {
        this.email = email;
        return this;
    }

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "HazelcastUser [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}