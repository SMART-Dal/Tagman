package com.smartlab.tagman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.opencsv.bean.CsvBindByName;

@Entity
@Table(name = "DesigniteCSVDataMethod")
public class DesigniteCSVMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CsvBindByName(column = "Project Name")
	private String projectName;

	@CsvBindByName(column = "Package Name")
	private String packageName;

	@CsvBindByName(column = "Type Name")
	private String typeName;

	@CsvBindByName(column = "Method Name")
	private String methodName;

	@CsvBindByName(column = "LOC")

	private String LOC;

	@CsvBindByName(column = "CC")
	private String CC;

	@CsvBindByName(column = "PC")
	private String PC;

	@Override
	public String toString() {
		return "DesigniteCSVMethod [id=" + id + ", projectName=" + projectName + ", packageName=" + packageName
				+ ", typeName=" + typeName + ", methodName=" + methodName + ", LOC=" + LOC + ", CC=" + CC + ", PC=" + PC
				+ "]";
	}

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
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the lOC
	 */
	public String getLOC() {
		return LOC;
	}

	/**
	 * @param lOC the lOC to set
	 */
	public void setLOC(String lOC) {
		LOC = lOC;
	}

	/**
	 * @return the cC
	 */
	public String getCC() {
		return CC;
	}

	/**
	 * @param cC the cC to set
	 */
	public void setCC(String cC) {
		CC = cC;
	}

	/**
	 * @return the pC
	 */
	public String getPC() {
		return PC;
	}

	/**
	 * @param pC the pC to set
	 */
	public void setPC(String pC) {
		PC = pC;
	}

}
