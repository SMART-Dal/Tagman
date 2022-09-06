package com.smartlab.tagman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instructormarking")
public class InstructorMarking {

	@Override
	public String toString() {
		return "InstructorMarking [insId=" + insId + ", userId=" + userId + ", markerId=" + markerId + ", annotationId="
				+ annotationId + ", isMarkedCorrect=" + isMarkedCorrect + ", isMarked=" + isMarked + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long insId;

	private Long userId;

	private Long markerId;

	private Long annotationId;

	private boolean isMarkedCorrect;

	private boolean isMarked;

	/**
	 * @return the insId
	 */
	public Long getInsId() {
		return insId;
	}

	/**
	 * @param insId the insId to set
	 */
	public void setInsId(Long insId) {
		this.insId = insId;
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
	 * @return the markerId
	 */
	public Long getMarkerId() {
		return markerId;
	}

	/**
	 * @param markerId the markerId to set
	 */
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}

	/**
	 * @return the annotationId
	 */
	public Long getAnnotationId() {
		return annotationId;
	}

	/**
	 * @param annotationId the annotationId to set
	 */
	public void setAnnotationId(Long annotationId) {
		this.annotationId = annotationId;
	}

	/**
	 * @return the isMarkedCorrect
	 */
	public boolean isMarkedCorrect() {
		return isMarkedCorrect;
	}

	/**
	 * @param isMarkedCorrect the isMarkedCorrect to set
	 */
	public void setMarkedCorrect(boolean isMarkedCorrect) {
		this.isMarkedCorrect = isMarkedCorrect;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

}
