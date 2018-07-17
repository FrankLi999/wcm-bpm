package com.bpwizard.myresources.resource.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Data     
// @AllArgsConstructor     
// @NoArgsConstructor 
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {
	private static final long serialVersionUID = 709944017998214715L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // @Setter(AccessLevel.PROTECTED)
	private Long id;
    
	@Column(name="email", length=255, unique=true)
	private String email;
	
	@Column(name="username", length=255)
    private String username;
	
	@Column(name="password", length=255)
    private String password;
    
	@Column(name="bio", columnDefinition="text")
	private String bio;
    
	@Column(name="image", length=511)
	private String image;

	private String[] roles; 
	
    public User(String email, String username, String password, String bio, String image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public void update(String email, String username, String password, String bio, String image) {
        if (!"".equals(email)) {
            this.email = email;
        }

        if (!"".equals(username)) {
            this.username = username;
        }

        if (!"".equals(password)) {
            this.password = password;
        }

        if (!"".equals(bio)) {
            this.bio = bio;
        }

        if (!"".equals(image)) {
            this.image = image;
        }
    }
}
