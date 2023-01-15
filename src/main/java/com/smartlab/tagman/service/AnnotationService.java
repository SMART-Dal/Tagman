package com.smartlab.tagman.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.AnnotationReturnModel;
import com.smartlab.tagman.model.InstructorMarking;
import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.repository.AnnotationRepository;
import com.smartlab.tagman.repository.ImportRepository;
import com.smartlab.tagman.repository.InstructorRepository;
import com.smartlab.tagman.repository.SampleRepository;
import com.smartlab.tagman.repository.SmellRepository;
import com.smartlab.tagman.repository.UserRepository;
import com.smartlab.tagman.util.Constants;
import com.smartlab.tagman.util.MarkerAnnotationCache;
import com.smartlab.tagman.util.TagmanUtil;

@Component
@EnableTransactionManagement
@Transactional
public class AnnotationService {

	@Autowired
	ImportRepository importRepository;

	@Autowired
	AnnotationRepository annotationRepository;

	@Autowired
	SmellRepository smellRepository;

	@Autowired
	SampleRepository sampleRepository;

	@Autowired
	TagmanUtil hibernateUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	InstructorRepository instructorRepository;

	public AnnotationReturnModel getAnnotationForUser(String username, String smellId, boolean updateAnnotation,
			Long sampleId) {
		return this.getAnnotationForUser(username, smellId, updateAnnotation, sampleId, false);
	}

	public AnnotationReturnModel getAnnotationForUser(String username, String smellId, boolean updateAnnotation,
			Long sampleId, boolean forceMethodSelect) {
		String userId = userRepository.findByUsername(username).getId().toString();
		hibernateUtil.init();
		SessionFactory sessionFactory = TagmanUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		AnnotationReturnModel returnModel = new AnnotationReturnModel();
		NativeQuery queryForSampleSize = session.createSQLQuery("select * from annotation where user_id = " + userId);
		System.out.println("Query for sample size" + queryForSampleSize);
		List resultsForSample = queryForSampleSize.list();
		returnModel.setSize(resultsForSample.size());
		Random random = new Random();
		boolean getClass = true;
		if (random.nextInt(10) < 7 && !forceMethodSelect) {
			getClass = false;
		}
		if (updateAnnotation) {
			System.out.println("Trying to update sample for id" + sampleId);
			SQLQuery query = session.createSQLQuery("select * from sample where id = " + sampleId);
			query.addEntity(Sample.class);
			List results = query.list();
			if (results.size() == 0)
				return null;
			else {
				System.out.println("Size not zero, running update query.");
				Sample sample = (Sample) results.get(0);

				SQLQuery queryn = session.createSQLQuery("UPDATE sample SET sample_constraints = "
						+ (sample.getSampleConstraints() + 1) + " WHERE id = " + sampleId);
				queryn.executeUpdate();
				returnModel.setSample(new Sample());
				return returnModel;
			}
		}

		SQLQuery query = session.createSQLQuery("select * from annotation where user_id = " + userId);
		// .createSQLQuery("select * from annotation where user_id = " + userId + " AND
		// smell_id = " + smellId);
		query.addEntity(Annotation.class);
		List results = query.list();
		List<Long> sampleIds = new ArrayList<>();
		StringBuilder sampleStr = new StringBuilder("");
		if (results.size() >= Constants.MaxUserAnnotations) {
			return null;
		}
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Annotation token = (Annotation) iterator.next();
			sampleIds.add(token.getSampleId());
			sampleStr.append("'" + token.getSampleId() + "',");
		}
		SQLQuery queryForSample;
		if (sampleStr.length() > 0) {
			sampleStr.deleteCharAt(sampleStr.length() - 1);
			queryForSample = session.createSQLQuery(
					"select * from sample where id NOT IN  (" + sampleStr + ") AND sample_constraints = " + 1
							+ " AND is_class = " + getClass + " AND has_smell = true LIMIT 10");

		} else {
			queryForSample = session.createSQLQuery(
					"select * from sample WHERE is_class = " + getClass + "  AND has_smell = true LIMIT 10");
		}
		System.out.println("queryForSample:" + queryForSample);
		queryForSample.addEntity(Sample.class);
		results = queryForSample.list();
		System.out.println("size" + results.size());
		if (results.size() == 0) {
			queryForSample = session.createSQLQuery("select * from sample where id NOT IN  (" + sampleStr
					+ ") AND sample_constraints < " + Constants.sampleConstraintValue + " AND is_class = " + getClass
					+ " AND has_smell = true LIMIT 10");

		}
		queryForSample.addEntity(Sample.class);
		System.out.println("queryForSample:" + queryForSample);

		results = queryForSample.list();
		if(results.size() == 0)
			return this.getAnnotationForUser(username, smellId, updateAnnotation, sampleId, true);
		Random rand = new Random();
		Sample randomResult = (Sample) results.get(rand.nextInt(results.size()));

		System.out.println(randomResult.toString());
		returnModel.setSample(randomResult);

		return returnModel;
	}

	public Annotation saveAnnotation(Annotation annotation) {
		return annotationRepository.save(annotation);
	}

	public Sample getSampleById(Long id) {
		return sampleRepository.findById(id).orElse(null);
	}

	public Annotation getAnnotationById(Long id) {
		return annotationRepository.findById(id).orElse(null);
	}

	public List<Annotation> getAllAnnotationsForUser(Long id) {
		return annotationRepository.findByUserId(id);
	}

	public List<Annotation> getAnnotationForUser(Long userId) {
		hibernateUtil.init();
		SessionFactory sessionFactory = TagmanUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			NativeQuery queryForSampleSize = session
					.createSQLQuery("select * from annotation where user_id = " + userId);
			queryForSampleSize.addEntity(Annotation.class);
			List<Annotation> results = queryForSampleSize.list();
			System.out.println("Query sample size" + results.size());

			NativeQuery queryForMarking = session
					.createSQLQuery("select * from instructormarking where user_id = " + userId);
			queryForMarking.addEntity(InstructorMarking.class);
			List<InstructorMarking> resultsMarker = queryForMarking.list();
			System.out.println("Results marker size" + resultsMarker.size());
			System.out.println(resultsMarker.size());
			resultsMarker.forEach(enty -> System.out.println(enty.toString()));
			List<Annotation> nonMarkedResults;
			if (resultsMarker.size() > 0) {

				List<Long> markedAnnotIds = resultsMarker.stream().map(query -> query.getAnnotationId())
						.collect(Collectors.toList());
				nonMarkedResults = results.stream().filter(query -> !markedAnnotIds.contains(query.getId()))
						.collect(Collectors.toList());
				return nonMarkedResults;
			} else {
				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
//			session.flush();
//			session.clear();
		}

	}

	public InstructorMarking getUnmarkedAnnotation(Long id) {

		MarkerAnnotationCache cache = MarkerAnnotationCache.getAnnotationCacheObject();

//		if (cache.getCacheForUser(id) != null && !cache.checkUpdateNeededForUser(id)) {
//			System.out.println("updating cache");
//			List<InstructorMarking> cacheForUser = cache.getMarkingFormatCacheForUser(id);
//			InstructorMarking user = cacheForUser.stream().filter(ting -> !ting.isMarked()).collect(Collectors.toList())
//					.get(0);
//			 cache.updateMarkerFormat(user.getUserId(), user.getAnnotationId());
//			return user;
//		} else {
		List<Annotation> users = getAnnotationForUser(id);
		System.out.println("Number of annotations for user" + users.size());
		if (users.size() == 0)
			return null;
		cache.addCacheForUser(id, users);
		System.out.println("added cache for user " + id);
		System.out.println("looking for cache");
		List<InstructorMarking> cacheForUser = cache.getMarkingFormatCacheForUser(id);
		List<InstructorMarking> userMarkingObject = cacheForUser.stream().filter(ting -> !ting.isMarked())
				.collect(Collectors.toList());

		// .get(0);
		if (userMarkingObject.size() > 0) {
			InstructorMarking user = userMarkingObject.get(0);
			cache.updateMarkerFormat(user.getUserId(), user.getAnnotationId());
			return user;
		} else {
			InstructorMarking user = cacheForUser.get(0);
			cache.updateMarkerFormat(user.getUserId(), user.getAnnotationId());
			return user;

		}
		// }

	}

}
