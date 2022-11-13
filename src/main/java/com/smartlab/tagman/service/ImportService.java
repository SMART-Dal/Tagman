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
import java.util.Random;

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
	DesigniteService designiteService;

	@Autowired
	DesigniteCSVMethodRepository designiteMethodRepository;
	@Autowired
	TagmanUtil tagmanUtil;

	public boolean importFolder(String folderName, String designFolder, boolean isClass, boolean designFile)
			throws IOException {

		if (designFile) {
			Path designDir = Paths.get(designFolder);
			System.out.println("Starting design file parse. Class - " + isClass);

			Files.walk(designDir).forEach(path -> {
				try {
					processDesignFile(path.toFile(), isClass);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}

		else {
			Path dir = Paths.get(folderName);

			System.out.println("Starting code file parse. Class is " + isClass);
			Files.walk(dir).forEach(path -> {
				// System.out.println("starting file parse");
				processFile(path.toFile(), isClass);
			});
		}
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
			// } else
			if (isClass) {
				if (file.getAbsolutePath().endsWith(".csv")
						&& !file.getAbsolutePath().toLowerCase().contains("__macosx")
						&& file.getAbsolutePath().toLowerCase().contains("typemetrics")) {
					String folder = parseFolderName(file.getAbsolutePath(), "designiteOutput");

					List<DesigniteCSV> lines = parseCSVFile(file);

				}
			} else {
				if (file.getAbsolutePath().endsWith(".csv")
						&& !file.getAbsolutePath().toLowerCase().contains("__macosx")
						&& file.getAbsolutePath().toLowerCase().contains("methodmetrics")) {
					String folder = parseFolderName(file.getAbsolutePath(), "designiteOutput");

					List<DesigniteCSVMethod> lines = parseCSVFileMethod(file);

				}
			}
		}
	}

	private List<DesigniteCSVMethod> parseCSVFileMethod(File file) throws IllegalStateException, FileNotFoundException {

		// System.out.println("Parsing csv");
		List<DesigniteCSVMethod> beans = new CsvToBeanBuilder<DesigniteCSVMethod>(new FileReader(file))
				.withType(DesigniteCSVMethod.class).build().parse();
		for (DesigniteCSVMethod bean : beans) {
			designiteMethodRepository.save(bean);
			// System.out.println(bean);
		}
		return null;
	}

	private List<DesigniteCSV> parseCSVFile(File file) throws IllegalStateException, FileNotFoundException {

		// System.out.println("Parsing csv");
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

				// for windows
				String folder = parseFolderNameWindows(file.getAbsolutePath(), isClass);
				// for linux String folder = parseFolderName(file.getAbsolutePath(),
				// "tagUtilOutput");
				// System.out.println("Processing File: " + file.getAbsolutePath());
				String fileName = file.getAbsolutePath();
				// for windows
				String packageOrClassName = fileName.substring(nthLastIndexOf(2, File.separator, fileName) + 1,
						nthLastIndexOf(1, File.separator, fileName));

				String typeName = fileName.substring(fileName.lastIndexOf(File.separator) + 1,
						fileName.lastIndexOf("."));
				List designiteEntries;
				String smells = "";
				Long id = (long) -1.00;
				if (!isClass) {
					String packageName = fileName.substring(nthLastIndexOf(3, File.separator, fileName) + 1,
							nthLastIndexOf(2, File.separator, fileName));
					designiteEntries = designiteService.getdesigniteMethodEntries(packageName, packageOrClassName,
							folder, typeName);
					if (designiteEntries.size() > 0) {
						DesigniteCSVMethod methodFromDes = (DesigniteCSVMethod) designiteEntries.get(0);
						System.out.println("result -- " + methodFromDes.getId());
						id = methodFromDes.getId();
						smells = parseSmellsMethod(methodFromDes);

					} else {
						System.out.println("ERROR COULD NOT FIND DES METHOD");
					}
				} else {
					designiteEntries = designiteService.getdesigniteEntries(packageOrClassName, typeName, folder);

					if (designiteEntries.size() > 0) {
						DesigniteCSV methodFromDes = (DesigniteCSV) designiteEntries.get(0);
						System.out.println("result -- " + methodFromDes.getId());
						id = methodFromDes.getId();
						smells = parseSmells(methodFromDes);
					} else {
						System.out.println("ERROR COULD NOT FIND DES METHOD");
					}
				}

				// for linux String typeName =
				// fileName.substring(fileName.lastIndexOf("/")-1,fileName.lastIndexOf(".")-1);
				// System.out.println("packageName" + packageOrClassName);
				// System.out.println(file.getAbsolutePath().substring(Constants.tempFileLocation.length()
				// - 1));
				// List<String> smells = tagmanUtil.calculateSmells(file, folder);
				importRepository
						.save(new Sample(null, file.getAbsolutePath().substring(Constants.tempFileLocation.length()),
								isClass, folder, id, !smells.equals(""), smells, new ArrayList<String>()));
//				importRepository
//						.save(new Sample(null, file.getAbsolutePath().substring(Constants.tempFileLocation.length()),
//								isClass, folder, new ArrayList<String>(), id));
			}
		}
	}

	private String parseSmellsMethod(DesigniteCSVMethod methodFromDes) {
		int cc = Integer.parseInt(methodFromDes.getCC());
		int pc = Integer.parseInt(methodFromDes.getPC());
		System.out.println("cc" + cc + " pc:" + pc);

		String smells = "";
		if (cc > Constants.CCLow && cc <= Constants.CCHigh)
			smells += "1,";
		if (pc > Constants.PCLow && pc < Constants.PCHigh)
			smells += "2,";

		if (smells.length() > 1)
			if (smells.indexOf(",") > 0) {
				smells = smells.substring(0, smells.length() - 1);
			}
		System.out.println(smells);
		return smells;
	}

	private String parseSmells(DesigniteCSV methodFromDes) {

		String smells = "";
		Double multipleAbs = Double.parseDouble(methodFromDes.getLCOM());
		System.out.println("abs" + multipleAbs);
		if (multipleAbs > Constants.AbsLow && multipleAbs < Constants.AbsHigh) {
			System.out.println("Found smell 3");
			smells += "3,";
		}
		if (smells.length() > 1)
			if (smells.indexOf(",") > 0) {
				smells = smells.substring(0, smells.length() - 1);
			}
		System.out.println(smells);

		return smells;
	}

	private String parseFolderNameWindows(String absName, boolean isClass) {
		String processedName = isClass ? absName.substring(
				(Constants.tempFileLocation + File.separator + Constants.codeSplitClass + File.separator).length())
				: absName.substring(
						(Constants.tempFileLocation + File.separator + Constants.codeSplitMethod + File.separator)
								.length());

		return processedName.substring(0, processedName.indexOf(File.separator));
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

	public int nthLastIndexOf(int nth, String ch, String string) {
		if (nth <= 0)
			return string.length();
		return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
	}
}
