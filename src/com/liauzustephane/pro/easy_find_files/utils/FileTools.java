package com.liauzustephane.pro.easy_find_files.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import com.liauzustephane.pro.easy_find_files.exceptions.EasyFindFilesExceptions;

public class FileTools {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	private static final String HTML_HEAD = "<html lang=\"fr\">" + "<head>\n" + "<meta charset=\"utf-8\">\n"
			+ "<title>Easy Find Files</title>\n" + "<style>\n" + "table, th, td {\n" + "    border: 1px solid black;\n"
			+ "    border-collapse: collapse;\n" + "}\n" + "th, td {\n" + "    padding: 5px;\n" + "}\n" + "th {\n"
			+ "    text-align: left;\n" + "}\n" + "</style>\n" + "</head>\n"
			+ "<body>\n";
	
	private static final String BODY_END = "</body>";
	
	/**
	 * To list recursively the files in a directory. This method will not return
	 * the list of the directories themselves but only the files contained in
	 * them.
	 * 
	 * @param directoryPathString
	 * @return
	 * @throws EasyFindFilesExceptions
	 * @throws IOException
	 */
	public static List<Path> listFilesInDirectory(String directoryPathString) throws EasyFindFilesExceptions,
			IOException {
		List<Path> list = new ArrayList<Path>();
		Path directoryPath = Paths.get(directoryPathString);
		if (Files.isDirectory(directoryPath)) {
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
				for (Path path : directoryStream) {
					if (Files.isDirectory(path)) {
						List<Path> listTmp = FileTools.listFilesInDirectory(path.toString());
						list.addAll(listTmp);
					} else {
						list.add(path);
					}
				}
			}
			return list;
		} else {
			throw new EasyFindFilesExceptions("The given path is not a valid directory");
		}
	}

	/**
	 * Remove hidden files from a list. Here, the hidden files are supposed to
	 * be the files beginning with a '.'. For example ".bash_profile" is
	 * considered to be a hidden file.
	 * 
	 * @param fileList
	 * @return
	 */
	public static List<Path> removeHiddenFiles(List<Path> fileList) {
		List<Path> hiddenFileList = new ArrayList<Path>();
		for (Path path : fileList) {
			Path pathName = path.getFileName();
			String pathNameString = pathName.toString();
			if (pathNameString.charAt(0) == '.') {
				hiddenFileList.add(path);
			}
		}
		for (Path path : hiddenFileList) {
			fileList.remove(path);
		}
		return fileList;
	}

	/**
	 * Take a path of a file and return an Adler-32 checksum of a file.
	 * 
	 * @param filePath
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static Long getFileChecksum(Path filePath) throws NoSuchAlgorithmException, IOException {
		Adler32 checksum = new Adler32();
		try( InputStream is = new CheckedInputStream( Files.newInputStream(filePath), checksum ) ){
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    		while (is.read(buffer) >= 0) {
    			/* EMPTY */
    		}
        }
		Long result = new Long(checksum.getValue());
        return result;
	}
	
	/**
	 * This method creates the file (resultFilePath) fills with the given String.
	 * 
	 * @param hashFilesList
	 * @throws IOException
	 */
	public static void writeResultFile(String htmlString, String resultFilePath) throws IOException {
		// To finalize the given HTML String
		String result = new String();
		result += HTML_HEAD;
		result += htmlString;
		result += BODY_END;
		
		// Creation of the result file
		Path path = Paths.get(resultFilePath);
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("UTF-8");
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
			writer.write(result, 0, result.length());
		}
	}

}
