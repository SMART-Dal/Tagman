package com.smartlab.tagman.model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "annotation")
public class Annotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sampleId;
	
	private boolean isECB;
	
	private boolean isMA;
	
	private boolean isCM;
	
	private boolean isLP;
	
	private boolean isIM;

	@Temporal(TemporalType.TIMESTAMP)
	Date creationDateTime;

	/**
	 * @return the creationDateTime
	 */
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	/**
	 * @param creationDateTime the creationDateTime to set
	 */
	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	/**
	 * @return the isECB
	 */
	public boolean isECB() {
		return isECB;
	}

	/**
	 * @param isECB the isECB to set
	 */
	public void setECB(boolean isECB) {
		this.isECB = isECB;
	}

	/**
	 * @return the isMA
	 */
	public boolean isMA() {
		return isMA;
	}

	/**
	 * @param isMA the isMA to set
	 */
	public void setMA(boolean isMA) {
		this.isMA = isMA;
	}

	/**
	 * @return the isCM
	 */
	public boolean isCM() {
		return isCM;
	}

	/**
	 * @param isCM the isCM to set
	 */
	public void setCM(boolean isCM) {
		this.isCM = isCM;
	}

	/**
	 * @return the isLP
	 */
	public boolean isLP() {
		return isLP;
	}

	/**
	 * @param isLP the isLP to set
	 */
	public void setLP(boolean isLP) {
		this.isLP = isLP;
	}

	/**
	 * @return the isIM
	 */
	public boolean isIM() {
		return isIM;
	}

	/**
	 * @param isIM the isIM to set
	 */
	public void setIM(boolean isIM) {
		this.isIM = isIM;
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

	private Long userId;

	/**
	 * @return the sampleId
	 */
	public Long getSampleId() {
		return sampleId;
	}

	/**
	 * @param sampleId the sampleId to set
	 */
	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the smellId
	 */
	public Long getSmellId() {
		return smellId;
	}

	/**
	 * @param smellId the smellId to set
	 */
	public void setSmellId(Long smellId) {
		this.smellId = smellId;
	}

	/**
	 * @return the isSmell
	 */
	public boolean isSmell() {
		return isSmell;
	}

	/**
	 * @param isSmell the isSmell to set
	 */
	public void setSmell(boolean isSmell) {
		this.isSmell = isSmell;
	}

	private Long smellId;

	private boolean isSmell;

	/**
	 * @param id
	 * @param sampleId
	 * @param userId
	 * @param smellId
	 * @param isSmell
	 */
	public Annotation(Long id, Long sampleId, Long userId, Long smellId, boolean isSmell) {
		this.id = id;
		this.sampleId = sampleId;
		this.userId = userId;
		this.smellId = smellId;
		this.isSmell = isSmell;
	}

	public Annotation() {
	}

}