package com.smartlab.tagman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.opencsv.bean.CsvBindByName;

@Entity
@Table(name = "DesigniteCSVData")
public class DesigniteCSV {

	@Override
	public String toString() {
		return "DesigniteCSV [projectName=" + projectName + ", packageName=" + packageName + ", typeName=" + typeName
				+ ", NOF=" + NOF + ", NOPF=" + NOPF + ", NOM=" + NOM + ", NOPM=" + NOPM + ", LOC=" + LOC + ", WMC="
				+ WMC + ", NC=" + NC + ", DIT=" + DIT + ", LCOM=" + LCOM + ", FANIN=" + FANIN + ", FANOUT=" + FANOUT
				+ "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@CsvBindByName(column = "Project Name")
	private String projectName;

	@CsvBindByName(column = "Package Name")
	private String packageName;

	@CsvBindByName(column = "Type Name")
	private String typeName;

	@CsvBindByName(column = "NOF")
	private String NOF;

	@CsvBindByName(column = "NOPF")
	private String NOPF;

	@CsvBindByName(column = "NOM")
	private String NOM;

	@CsvBindByName(column = "NOPM")
	private String NOPM;

	@CsvBindByName(column = "LOC")
	private String LOC;

	@CsvBindByName(column = "WMC")
	private String WMC;

	@CsvBindByName(column = "NC")
	private String NC;

	@CsvBindByName(column = "DIT")
	private String DIT;

	@CsvBindByName(column = "LCOM")
	private String LCOM;

	@CsvBindByName(column = "FANIN")
	private String FANIN;

	@CsvBindByName(column = "FANOUT")
	private String FANOUT;

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
	 * @return the nOF
	 */
	public String getNOF() {
		return NOF;
	}

	/**
	 * @param nOF the nOF to set
	 */
	public void setNOF(String nOF) {
		NOF = nOF;
	}

	/**
	 * @return the nOPF
	 */
	public String getNOPF() {
		return NOPF;
	}

	/**
	 * @param nOPF the nOPF to set
	 */
	public void setNOPF(String nOPF) {
		NOPF = nOPF;
	}

	/**
	 * @return the nOM
	 */
	public String getNOM() {
		return NOM;
	}

	/**
	 * @param nOM the nOM to set
	 */
	public void setNOM(String nOM) {
		NOM = nOM;
	}

	/**
	 * @return the nOPM
	 */
	public String getNOPM() {
		return NOPM;
	}

	/**
	 * @param nOPM the nOPM to set
	 */
	public void setNOPM(String nOPM) {
		NOPM = nOPM;
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
	 * @return the wMC
	 */
	public String getWMC() {
		return WMC;
	}

	/**
	 * @param wMC the wMC to set
	 */
	public void setWMC(String wMC) {
		WMC = wMC;
	}

	/**
	 * @return the nC
	 */
	public String getNC() {
		return NC;
	}

	/**
	 * @param nC the nC to set
	 */
	public void setNC(String nC) {
		NC = nC;
	}

	/**
	 * @return the dIT
	 */
	public String getDIT() {
		return DIT;
	}

	/**
	 * @param dIT the dIT to set
	 */
	public void setDIT(String dIT) {
		DIT = dIT;
	}

	/**
	 * @return the lCOM
	 */
	public String getLCOM() {
		return LCOM;
	}

	/**
	 * @param lCOM the lCOM to set
	 */
	public void setLCOM(String lCOM) {
		LCOM = lCOM;
	}

	/**
	 * @return the fANIN
	 */
	public String getFANIN() {
		return FANIN;
	}

	/**
	 * @param fANIN the fANIN to set
	 */
	public void setFANIN(String fANIN) {
		FANIN = fANIN;
	}

	/**
	 * @return the fANOUT
	 */
	public String getFANOUT() {
		return FANOUT;
	}

	/**
	 * @param fANOUT the fANOUT to set
	 */
	public void setFANOUT(String fANOUT) {
		FANOUT = fANOUT;
	}

}
