package com.liauzustephane.pro.easy_find_files.tests;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.liauzustephane.pro.easy_find_files.bo.FindFilesWithSameNames;
import com.liauzustephane.pro.easy_find_files.bo.FindIdenticalFiles;

public class FindIdenticalFilesTestDrive {

	public static void main(String[] args) {
		//Path directoryToTestPath = Paths.get("/Users/stephane/Desktop/tmp");
		FindIdenticalFiles findIdenticalFiles = new FindIdenticalFiles("/Users/stephane/Desktop/tmp");

		try {
			
			String htmlResultString = findIdenticalFiles.execute();
			System.out.println(htmlResultString);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
