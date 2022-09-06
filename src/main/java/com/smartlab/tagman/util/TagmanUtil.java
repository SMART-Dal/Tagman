package com.smartlab.tagman.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.service.DesigniteService;

@Component
public class TagmanUtil {
	private static SessionFactory sessionFactory = null;
	private boolean initDone = false;
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private DesigniteService designiteService;

	public void init() {
		if (!initDone) {
			if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
				throw new NullPointerException("factory is not a hibernate factory");
			}
			sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
			initDone = true;
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static List<String> readFile(String file) throws IOException {

		Path path = Paths.get(Constants.tempFileLocation + file);
		System.out.println(Files.readAllLines(path).toString());
		return Files.readAllLines(path);
	}

	public static String getBaseFile() {
		System.out.println(System.getProperty("java.io.tmpdir") + "output4" + "/");
		// return System.getProperty("java.io.tmpdir") + "output4" + "/";
		return Constants.tempFileLocation;
	}

	public static File multipartToFile(MultipartFile multipart, String fileName)
			throws IllegalStateException, IOException {
		File convFile = new File(TagmanUtil.getBaseFile() + fileName);
		multipart.transferTo(convFile);
		return convFile;
	}

	public static int callSubProc(String file, String base, int numRepo) throws IOException, InterruptedException {

		ProcessBuilder pb = new ProcessBuilder("python3", file, "" + base, "" + numRepo);
		Map<String, String> envVar = pb.environment();
		envVar.put("JAVA_HOME", Constants.JavaHomePath);
		envVar.put("GRADLE_HOME", Constants.GradlePath);
		envVar.put("PATH", "$GRADLE_HOME/bin:$PATH");

		// pb.redirectErrorStream(true);
		// pb.redirectOutput(System.out);
		Process p = pb.inheritIO().start();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

		System.out.println("value is : " + in.readLine());
		return p.waitFor();
	}

	public static int runDesignite(String baseFile, String designiteJar, int i)
			throws IOException, InterruptedException {

		File[] files = new File(baseFile + "/output").listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
		for (File file : files) {
			System.out.println("Processing : " + file.getName());
			File gradle = new File(file + File.separator + "build.gradle");
			File maven = new File(file + File.separator + "pom.xml");

			int result = maven.exists() ? processMaven(file) : processGradle(file);

			// java -jar Designite.jar -i <path of the input source folder> -o <path of the
			// output folder>
//
//			ProcessBuilder pb = new ProcessBuilder("/opt/homebrew/opt/openjdk/bin/java", "-jar", designiteJar, "-i",
//					baseFile + File.separator + "output", "-o", baseFile + File.separator + "designite"+File.separator+file.getName());
//			pb.directory(file);
//			Process p = pb.inheritIO().start();
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			System.out.println("Process Builder designite val is " + in.readLine());
//			int resultP = p.waitFor();
		}
		return 0;
	}

	private static int processMaven(File file) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(Constants.MvnPath, "clean", "install", "-DskiptTests");
		pb.directory(file);
		Process p = pb.inheritIO().start();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println("Process Builder val is " + in.readLine());
		return p.waitFor();
	}

	private static int processGradle(File file) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(Constants.GradlePath, "build", "-x", "test");
		pb.directory(file);
		Process p = pb.inheritIO().start();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println("Process Builder val is " + in.readLine());
		return p.waitFor();
	}

	public List<String> calculateSmells(File file, String projectsName) {

		/**
		 * /Users/himesh/Documents/temp/codesplit_java_class/flipkart-incubator_databuilderframework
		 * /flipkart-incubator_databuilderframework/com.flipkart.databuilderframework.databuilderinstancetest/DataFlowBuilderTest.code
		 **/
		String absolutePath = file.getAbsolutePath();

		String packageNameLong = absolutePath.substring(0, absolutePath.lastIndexOf("/"));

		String packageName = packageNameLong.substring(packageNameLong.lastIndexOf("/"));

		String fileName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1, absolutePath.lastIndexOf("."));

		String projectNameLong = absolutePath.substring(absolutePath.indexOf(Constants.tempFileLocation + "/"));
		String projectName = projectNameLong.substring(projectNameLong.indexOf("/"));

		System.out.println("Package Name" + packageName + " fileName" + fileName + " projectName" + projectName);
		List<DesigniteCSV> designiteEntries = designiteService.getdesigniteEntries(packageName, fileName, projectName);

		List<String> designiteSmells = calculateDesigniteSmells(designiteEntries);

		return designiteSmells;
	}

	private List<String> calculateDesigniteSmells(List<DesigniteCSV> designiteEntries) {

		List<String> smells = new ArrayList<String>();
		double loc = 0;
		double lcom = 0;
		for (DesigniteCSV entry : designiteEntries) {
			lcom += Double.parseDouble(entry.getLCOM());
			loc += Double.parseDouble(entry.getLOC());
		}

		if (loc > 50)
			smells.add("Long Class");
		if (lcom > 0.12)
			smells.add("Multifaceted Abstraction");

		return smells;
	}

	public static int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	
}