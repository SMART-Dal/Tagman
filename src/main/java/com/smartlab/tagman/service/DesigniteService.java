package com.smartlab.tagman.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.model.DesigniteCSVMethod;
import com.smartlab.tagman.util.TagmanUtil;

@Component
@Repository
@Transactional
public class DesigniteService {

	@Autowired
	TagmanUtil hibernateUtil;

	public List<DesigniteCSV> getdesigniteEntries(String packageName, String className, String projectName) {
		System.out.println("Getting class designite service entries. ");
		System.out.println("packageName" + packageName + " classname:" + className + " projectname: " + projectName);
		// String userId = userRepository.findByUsername(username).getId().toString();
		hibernateUtil.init();
		SessionFactory sessionFactory = TagmanUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery("select * from designitecsvdata where package_name = \"" + packageName
				+ "\" AND type_name = \"" + className + "\" AND project_name = \"" + projectName + "\"");
		System.out.println("Running query : " + query.toString());
		System.out.println("Running query : " + "select * from designitecsvdata where package_name = \"" + packageName
				+ "\" AND type_name = \"" + className + "\" AND project_name = \"" + projectName + "\"");

		query.addEntity(DesigniteCSV.class);
		List results = query.list();
		System.out.println("Query returned size :" + results.size());
		return results;
	}

	public List<DesigniteCSVMethod> getdesigniteMethodEntries(String packageName, String className, String projectName,
			String methodName) {
		System.out.println("Trying to get method designite service entries. ");
		
		System.out.println("packageName" + packageName + " classname:" + className + " projectname: " + projectName
				+ " methodName: " + methodName);
		hibernateUtil.init();
		SessionFactory sessionFactory = TagmanUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery("select * from designitecsvdata_method where package_name = \""
				+ packageName + "\" AND type_name = \"" + className + "\" AND project_name = \"" + projectName
				+ "\" AND method_name = \"" + methodName + "\"");
		System.out.println("Executing query : "+query.toString());
		System.out.println("Executing query : "+ "select * from designitecsvdata_method where package_name = \""
				+ packageName + "\" AND type_name = \"" + className + "\" AND project_name = \"" + projectName
				+ "\" AND method_name = \"" + methodName + "\"");
		
		query.addEntity(DesigniteCSVMethod.class);
		List results = query.list();
		System.out.println("design Entries size : " + results.size());
		return results;

	}
}
