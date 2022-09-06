package com.smartlab.tagman.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.model.DesigniteCSVMethod;
import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.model.Smell;
import com.smartlab.tagman.repository.DesigniteCSVMethodRepository;
import com.smartlab.tagman.repository.DesigniteCSVRepository;
import com.smartlab.tagman.repository.ImportRepository;
import com.smartlab.tagman.repository.SmellRepository;
import com.smartlab.tagman.util.Constants;
import com.smartlab.tagman.util.TagmanUtil;

@Component
public class ImportService {

	@Autowired
	ImportRepository importRepository;

	@Autowired
	SmellRepository smellRepository;

	@Autowired
	DesigniteCSVRepository designiteRepository;

	@Autowired
	DesigniteCSVMethodRepository designiteMethodRepository;
	@Autowired
	TagmanUtil tagmanUtil;

	public boolean importFolder(String folderName, String designFolder, boolean isClass) throws IOException {

		Path designDir = Paths.get(designFolder);
//		Files.walk(designDir).forEach(path -> {
//			try {
//				processDesignFile(path.toFile(), isClass);
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
		Path dir = Paths.get(folderName);
//
		Files.walk(dir).forEach(path -> {
			System.out.println("starting file parse");
			processFile(path.toFile(), isClass);
		});

		return true;
	}

	public void processDesignFile(File file, boolean isClass) throws IllegalStateException, FileNotFoundException {

		if (file.isDirectory()) {
			System.out.println("Processing Directory: " + file.getAbsolutePath());
		} else {
//			if (file.getAbsolutePath().endsWith(".csv") && !file.getAbsolutePath().toLowerCase().contains("__macosx")
//					&& file.getAbsolutePath().toLowerCase().contains("methodmetrics")) {
//				String folder = parseFolderName(file.getAbsolutePath(), "designiteOutput");
//
//				List<DesigniteCSV> lines = parseCSVFile(file);
//				// System.out.println("Processing File: " + file.getAbsolutePath());

				// importRepository.save(new Sample(file.getAbsolutePath(), isClass));
		//	} else 
				if (file.getAbsolutePath().endsWith(".csv")
					&& !file.getAbsolutePath().toLowerCase().contains("__macosx")
					&& file.getAbsolutePath().toLowerCase().contains("typemetrics")) {
				String folder = parseFolderName(file.getAbsolutePath(), "designiteOutput");

				List<DesigniteCSV> lines = parseCSVFile(file);

			}
		}
	}

	private List<DesigniteCSVMethod> parseCSVFileMethod(File file) throws IllegalStateException, FileNotFoundException {

		System.out.println("Parsing csv");
		List<DesigniteCSVMethod> beans = new CsvToBeanBuilder<DesigniteCSVMethod>(new FileReader(file))
				.withType(DesigniteCSVMethod.class).build().parse();
		for (DesigniteCSVMethod bean : beans) {
			designiteMethodRepository.save(bean);
			// System.out.println(bean);
		}
		return null;
	}

	private List<DesigniteCSV> parseCSVFile(File file) throws IllegalStateException, FileNotFoundException {

		System.out.println("Parsing csv");
		List<DesigniteCSV> beans = new CsvToBeanBuilder<DesigniteCSV>(new FileReader(file)).withType(DesigniteCSV.class)
				.build().parse();
		for (DesigniteCSV bean : beans) {
			designiteRepository.save(bean);
			// System.out.println(bean);
		}
		return null;
	}

	private void processFile(File file, boolean isClass) {
		if (file.isDirectory()) {
			// System.out.println("Processing Directory: " + file.getAbsolutePath());
		} else {
			if (file.getAbsolutePath().endsWith(".code")
					&& !file.getAbsolutePath().toLowerCase().contains("__macosx")) {
				String folder = parseFolderName(file.getAbsolutePath(), "tagUtilOutput");
				System.out.println("Processing File: " + file.getAbsolutePath());

				System.out.println(file.getAbsolutePath().substring(Constants.tempFileLocation.length() - 1));
				// List<String> smells = tagmanUtil.calculateSmells(file, folder);
				
				importRepository
						.save(new Sample(null, file.getAbsolutePath().substring(Constants.tempFileLocation.length()),
								isClass, folder, new ArrayList<String>(),new Long(0)));
			}
		}
	}

	private String parseFolderName(String absolutePath, String outputFolder) {

		int index = absolutePath.indexOf(System.getProperty("user.home") + File.separator + outputFolder)
				+ (System.getProperty("user.home") + File.separator + "designiteOutput").length();
		String processedStr = absolutePath.substring(index + 1);
		processedStr = processedStr.substring(processedStr.indexOf(File.separator) + 1);
		processedStr = processedStr.substring(0, processedStr.indexOf(File.separator));

		// processedStr = processedStr.substring(processedStr.indexOf(File.separator) +
		// 1);

		// System.out.println("indexx" + processedStr);
		return processedStr;
	}

	public List<Sample> getAllSamples() {
		return importRepository.findAll();
	}

	public void importSmellsFile(MultipartFile file) throws IOException, IOException, CsvException {

		try {
			CSVReader reader = new CSVReader(new FileReader(multipartToFile(file, "smells.csv")));
			List<String[]> r = reader.readAll();
			r.forEach(token -> {
				if (!(token.length >= 3)) {
					System.out.println("Found invalid entry. Skipping");

				} else
					smellRepository.save(new Smell(token[0], token[1], Boolean.parseBoolean(token[2])));
			});
		} catch (Exception e) {
			System.out.println("Exception occured while parsing");
			e.printStackTrace();
		}
	}

	public static File multipartToFile(MultipartFile multipart, String fileName)
			throws IllegalStateException, IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
		multipart.transferTo(convFile);
		return convFile;
	}

}
