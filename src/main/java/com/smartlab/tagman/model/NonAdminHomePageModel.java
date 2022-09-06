package com.smartlab.tagman.model;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NonAdminHomePageModel {

	private String sampleId;

	private String isECB;

	private String isMA;
	private String isCM;
	private String isLP;
	private String isIM;

	/**
	 * @return the sampleId
	 */
	public String getSampleId() {
		return sampleId;
	}

	/**
	 * @param sampleId the sampleId to set
	 */
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * @return the isECB
	 */
	public String getIsECB() {
		return isECB;
	}

	/**
	 * @param isECB the isECB to set
	 */
	public void setIsECB(String isECB) {
		this.isECB = isECB;
	}

	/**
	 * @return the isMA
	 */
	public String getIsMA() {
		return isMA;
	}

	/**
	 * @param isMA the isMA to set
	 */
	public void setIsMA(String isMA) {
		this.isMA = isMA;
	}

	/**
	 * @return the isCM
	 */
	public String getIsCM() {
		return isCM;
	}

	/**
	 * @param isCM the isCM to set
	 */
	public void setIsCM(String isCM) {
		this.isCM = isCM;
	}

	/**
	 * @return the isLP
	 */
	public String getIsLP() {
		return isLP;
	}

	/**
	 * @param isLP the isLP to set
	 */
	public void setIsLP(String isLP) {
		this.isLP = isLP;
	}

	/**
	 * @return the isIM
	 */
	public String getIsIM() {
		return isIM;
	}

	/**
	 * @param isIM the isIM to set
	 */
	public void setIsIM(String isIM) {
		this.isIM = isIM;
	}

}
