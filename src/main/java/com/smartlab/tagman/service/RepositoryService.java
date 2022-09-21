package com.smartlab.tagman.service;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.smartlab.tagman.model.Repository;
import com.smartlab.tagman.repository.RepoRepository;
import com.smartlab.tagman.util.Constants;
import com.smartlab.tagman.util.TagmanUtil;

@Component
public class RepositoryService {

	@Autowired
	RepoRepository repoRepository;

	@Autowired
	ImportService importService;

	public void storeRepository(MultipartFile file) throws Exception {

		System.out.println("File:" + file.getOriginalFilename());
		byte[] buffer = new byte[1024];
		CSVReader reader = new CSVReader(new FileReader(TagmanUtil.multipartToFile(file, "/temp.csv")));

		List<String[]> lines = reader.readAll();

		lines.forEach(line -> parseLine(line));

		System.out.println("-- Processing python File 1");
//		File fp = new File("/Users/himesh/git/springboot-security-login-thymeleaf/src/main/resources/download.py");
//		// URL filePath = RepositoryService.class.getResource("download.py");
//		System.out.println(fp.getPath());
		int retVal = 0;
		//TagmanUtil.callSubProc(Constants.python_download_script, TagmanUtil.getBaseFile(), 5);
		//retVal = TagmanUtil.runDesignite(TagmanUtil.getBaseFile(), Constants.DesigniteJarPath, 5);

		//TagmanUtil.callSubProc(
		//		Constants.DataCurationPythonFile,
		//		TagmanUtil.getBaseFile(), 5);

		System.out.println("importing folder");
		//importService.importFolder(TagmanUtil.getBaseFile() + File.separator + "codesplit_java_class2",
		//		TagmanUtil.getBaseFile() + File.separator + "designite_out_java2", true);
		 importService.importFolder(TagmanUtil.getBaseFile()+File.separator+Constants.codeSplitClass,
				 TagmanUtil.getBaseFile() + File.separator + "designite_out_java", true);
		 importService.importFolder(TagmanUtil.getBaseFile()+File.separator+Constants.codeSplitMethod,
				 TagmanUtil.getBaseFile() + File.separator + "designite_out_java", false);

		System.out.println("Ret Val:" + retVal);

	}

	private void parseLine(String[] line) {

		//repoRepository.save(new Repository(line[0], line[1], Integer.parseInt(line[2]), line[3]));
	}

}
