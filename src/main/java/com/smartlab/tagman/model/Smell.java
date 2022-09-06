package com.smartlab.tagman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "smell")
public class Smell {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private boolean isDesignSmell;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isDesignSmell
	 */
	public boolean isDesignSmell() {
		return isDesignSmell;
	}

	/**
	 * @param isDesignSmell the isDesignSmell to set
	 */
	public void setDesignSmell(boolean isDesignSmell) {
		this.isDesignSmell = isDesignSmell;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param isDesignSmell
	 */
	public Smell(String name, String description, boolean isDesignSmell) {
		this.name = name;
		this.description = description;
		this.isDesignSmell = isDesignSmell;
	}

	/**
	 * 
	 */
	public Smell() {
	}

}
