package com.smartlab.tagman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "repository")
public class Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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

	private String repoPath;
	private String repoName;
	private int stars;
	private String dpId;

	/**
	 * 
	 */
	public Repository() {
	}

	/**
	 * @param repoPath
	 * @param repoName
	 * @param stars
	 * @param dpId
	 */
	public Repository(String repoPath, String repoName, int stars, String dpId) {
		this.repoPath = repoPath;
		this.repoName = repoName;
		this.stars = stars;
		this.dpId = dpId;
	}

	@Override
	public String toString() {
		return "Repository [repoPath=" + repoPath + ", repoName=" + repoName + ", stars=" + stars + ", dpId=" + dpId
				+ "]";
	}

	/**
	 * @return the repoPath
	 */
	public String getRepoPath() {
		return repoPath;
	}

	/**
	 * @param repoPath the repoPath to set
	 */
	public void setRepoPath(String repoPath) {
		this.repoPath = repoPath;
	}

	/**
	 * @return the repoName
	 */
	public String getRepoName() {
		return repoName;
	}

	/**
	 * @param repoName the repoName to set
	 */
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	/**
	 * @return the stars
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * @param stars the stars to set
	 */
	public void setStars(int stars) {
		this.stars = stars;
	}

	/**
	 * @return the dpId
	 */
	public String getDpId() {
		return dpId;
	}

	/**
	 * @param dpId the dpId to set
	 */
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

}
