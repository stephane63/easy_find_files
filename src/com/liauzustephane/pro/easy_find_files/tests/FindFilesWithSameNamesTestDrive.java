package com.liauzustephane.pro.easy_find_files.tests;

import com.liauzustephane.pro.easy_find_files.bo.FindFilesWithSameNames;

public class FindFilesWithSameNamesTestDrive {

	public static void main(String[] args) {

		FindFilesWithSameNames findFilesWithSameNames = new FindFilesWithSameNames();

		try {
			System.out.println(findFilesWithSameNames.getDirectoryToTestPath());
			
			findFilesWithSameNames.execute();
			
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
