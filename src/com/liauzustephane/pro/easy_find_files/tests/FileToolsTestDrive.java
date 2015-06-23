package com.liauzustephane.pro.easy_find_files.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.liauzustephane.pro.easy_find_files.exceptions.EasyFindFilesExceptions;
import com.liauzustephane.pro.easy_find_files.utils.FileTools;

public class FileToolsTestDrive {

	public static void main(String[] args) {

		String directoryPathString = "/Users/stephane/Desktop/tmp";

		
//		try {
//			
//			List<Path> list = FileTools.listFilesInDirectory(directoryPathString);
//			for (Path path : list) {
//				System.out.println(path);
//			}
//			
//			System.out.println("################################");
//			
//			list = FileTools.removeHiddenFiles(list);
//			for (Path path : list) {
//				System.out.println(path);
//			}
//			
//		} catch (EasyFindFilesExceptions e) {
//			System.out.println(e);
//		} catch (IOException e) {
//			System.out.println(e);
//		}

		
		try {
			Path path1 = Paths.get("/Users/stephane/Desktop/tmp/first_file.txt");
			Path path2 = Paths.get("/Users/stephane/Desktop/tmp/images3.jpg");
			long cs1 = FileTools.getFileChecksum(path1);
			long cs2 = FileTools.getFileChecksum(path2);
			System.out.println(cs1);
			System.out.println(cs2);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

}
