package com.smartlab.tagman.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.InstructorMarking;

public class MarkerAnnotationCache {

	private static Map<Long, List<Annotation>> markerAnnotationCache;

	private static Map<Long, List<InstructorMarking>> markingFormatCache;

	/**
	 * @return the markingFormatCache
	 */
	public static List<InstructorMarking> getMarkingFormatCacheForUser(Long userId) {
		return markingFormatCache.get(userId);
	}

	private static Map<Long, Instant> LastUpdated;

	private static MarkerAnnotationCache cacheObject;

	/**
	 * 
	 */
	private MarkerAnnotationCache() {
		markerAnnotationCache = new HashMap<Long, List<Annotation>>();
		LastUpdated = new HashMap<Long, Instant>();
		markingFormatCache = new HashMap<Long, List<InstructorMarking>>();
	}

	public static MarkerAnnotationCache getAnnotationCacheObject() {

		if (cacheObject == null)
			cacheObject = new MarkerAnnotationCache();
		return cacheObject;
	}

	public void updateMarkerFormat(Long userId, Long annotationId) {
		markingFormatCache.get(userId).forEach(str -> {
			if (str.getAnnotationId().equals(annotationId))
				str.setMarked(true);
		});
	}

	public void addCacheForUser(Long userId, List<Annotation> entries) {
		markerAnnotationCache.put(userId, entries);
		List<InstructorMarking> markList = new ArrayList<InstructorMarking>();
		entries.forEach(entry -> {
			InstructorMarking marking = new InstructorMarking();
			marking.setAnnotationId(entry.getId());
			marking.setUserId(userId);
			marking.setMarked(false);
			markList.add(marking);
		});
		markingFormatCache.put(userId, markList);
		LastUpdated.put(userId, Instant.now());

	}

	public List<Annotation> getCacheForUser(Long userId) {
		return markerAnnotationCache.get(userId);
	}

	public boolean checkUpdateNeededForUser(Long userId) {
		if (LastUpdated.get(userId) == null)
			return true;
		else {
			if (Duration.between(LastUpdated.get(userId), Instant.now()).toMinutes() > 15)
				return true;
			else
				return false;
		}
	}
}
