package com.smartlab.tagman.model;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class InstructorsHomePageModel {

	@Override
	public String toString() {
		return "InstructorsHomePageModel [bannerId=" + bannerId + ", annotation=" + annotation + ", annotationId="
				+ annotationId + ", smells=" + smells + ", isMarkedCorrect=" + isMarkedCorrect + ", users=" + users
				+ ", isUserSubm=" + isUserSubm + "]";
	}

	private String bannerId;

	private File annotation;
	
	private String annotationId;

	private List<String> smells;
	
	private String isMarkedCorrect;

	/**
	 * @return the isMarkedCorrect
	 */
	public String getIsMarkedCorrect() {
		return isMarkedCorrect;
	}

	/**
	 * @param isMarkedCorrect the isMarkedCorrect to set
	 */
	public void setIsMarkedCorrect(String isMarkedCorrect) {
		this.isMarkedCorrect = isMarkedCorrect;
	}

	private List<User> users;

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	private String isUserSubm;

	/**
	 * @return the isUserSubm
	 */
	public String getIsUserSubm() {
		return isUserSubm;
	}

	/**
	 * @param isUserSubm the isUserSubm to set
	 */
	public void setIsUserSubm(String isUserSubm) {
		this.isUserSubm = isUserSubm;
	}

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

	/**
	 * @return the annotation
	 */
	public File getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(File annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the smells
	 */
	public List<String> getSmells() {
		return smells;
	}

	/**
	 * @param smells the smells to set
	 */
	public void setSmells(List<String> smells) {
		this.smells = smells;
	}

	/**
	 * @return the isMarkedCorrect
	 */
	

	public String getAnnotationId() {
		return annotationId;
	}

	public void setAnnotationId(String annotationId) {
		this.annotationId = annotationId;
	}
}
