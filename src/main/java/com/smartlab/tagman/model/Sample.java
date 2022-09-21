package com.smartlab.tagman.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sample")
public class Sample {

	@Override
	public String toString() {
		return "Sample [id=" + id + ", pathToFile=" + pathToFile + ", isClass=" + isClass + ", projectName="
				+ projectName + ", designiteSmells=" + designiteSmells + "]";
	}

	public Sample() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String pathToFile;

	private boolean isClass;

	private String projectName;

	private int sampleConstraints;

	private Long designiteId;
	
	private boolean hasSmell;
	
	private String smells;

	public boolean isHasSmell() {
		return hasSmell;
	}

	public void setHasSmell(boolean hasSmell) {
		this.hasSmell = hasSmell;
	}

	public String getSmells() {
		return smells;
	}

	public void setSmells(String smells) {
		this.smells = smells;
	}

	public void setClass(boolean isClass) {
		this.isClass = isClass;
	}

	@ElementCollection
	private List<String> designiteSmells;

	/**
	 * @param id
	 * @param pathToFile
	 * @param isClass
	 * @param projectName
	 * @param designiteSmells
	 */
	public Sample(Long id, String pathToFile, boolean isClass, String projectName, List<String> designiteSmells,
			Long designiteId) {
		this.id = id;
		this.pathToFile = pathToFile;
		this.isClass = isClass;
		this.projectName = projectName;
		this.designiteSmells = designiteSmells;
		this.sampleConstraints = 0;
		this.designiteId = designiteId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public Sample(Long id, String pathToFile, boolean isClass, String projectName,
			Long designiteId, boolean hasSmell, String smells, List<String> designiteSmells) {
		super();
		this.id = id;
		this.pathToFile = pathToFile;
		this.isClass = isClass;
		this.projectName = projectName;
		this.sampleConstraints = 0;
		this.designiteId = designiteId;
		this.hasSmell = hasSmell;
		this.smells = smells;
		this.designiteSmells = designiteSmells;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the pathToFile
	 */
	public String getPathToFile() {
		return pathToFile;
	}

	/**
	 * @param pathToFile the pathToFile to set
	 */
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	/**
	 * @return the isClass
	 */
	public boolean getIsClass() {
		return isClass;
	}

	/**
	 * @param isClass the isClass to set
	 */
	public void setIsClass(boolean isClass) {
		this.isClass = isClass;
	}

	public List<String> getDesigniteSmells() {
		return designiteSmells;
	}

	public void setDesigniteSmells(List<String> designiteSmells) {
		this.designiteSmells = designiteSmells;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getSampleConstraints() {
		return sampleConstraints;
	}

	public void setSampleConstraints(int sampleConstraints) {
		this.sampleConstraints = sampleConstraints;
	}

	public Long getDesigniteId() {
		return designiteId;
	}

	public void setDesigniteId(Long designiteId) {
		this.designiteId = designiteId;
	}

}
