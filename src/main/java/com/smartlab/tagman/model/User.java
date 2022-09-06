package com.smartlab.tagman.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String username;
    
    public String bannerId;
    
    public int samplesAnswered;

    /**
	 * @return the bannerId
	 */
	public String getBannerId() {
		return bannerId;
	}

	/**
	 * @param bannerId the bannerId to set
	 */
	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	private String password;
    
    public boolean isAdmin;
    
    public boolean isInstructor;

    /**
	 * @return the isInstructor
	 */
	public boolean isInstructor() {
		return isInstructor;
	}

	/**
	 * @param isInstructor the isInstructor to set
	 */
	public void setInstructor(boolean isInstructor) {
		this.isInstructor = isInstructor;
	}

	@Transient
    private String passwordConfirm;

    @ManyToMany
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getSamplesAnswered() {
		return samplesAnswered;
	}

	public void setSamplesAnswered(int samplesAnswered) {
		this.samplesAnswered = samplesAnswered;
	}
}
