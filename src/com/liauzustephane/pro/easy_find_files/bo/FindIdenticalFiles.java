package com.liauzustephane.pro.easy_find_files.bo;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.liauzustephane.pro.easy_find_files.exceptions.EasyFindFilesExceptions;
import com.liauzustephane.pro.easy_find_files.utils.FileTools;

/**
 * @author stephane This class is used to find same files in a given directory
 *         (even if the files have not the same name).
 */
public class FindIdenticalFiles {

	private String directoryToTestPath;

	public FindIdenticalFiles() {
	}

	public FindIdenticalFiles(String directoryToTestPath) {
		this.directoryToTestPath = directoryToTestPath;
	}

	public String getDirectoryToTestPath() {
		return directoryToTestPath;
	}

	public void setDirectoryToTestPath(String directoryToTestPath) {
		this.directoryToTestPath = directoryToTestPath;
	}

	/**
	 * Method which return a Hashtable where the keys are checksums and the
	 * values a List which contains all the paths of the files corresponding to
	 * this checksum.
	 * 
	 * @return
	 * @throws EasyFindFilesExceptions
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private Hashtable<Long, List<Path>> searchIdenticalFiles() throws EasyFindFilesExceptions, IOException,
			NoSuchAlgorithmException {
		Hashtable<Long, List<Path>> hashChecksumList = new Hashtable<Long, List<Path>>();
		List<Path> fileList = FileTools.listFilesInDirectory(directoryToTestPath);
		fileList = FileTools.removeHiddenFiles(fileList);

		// For all the files found in the directory we fill the Hashtable.
		for (Path path : fileList) {
			Long checksum = FileTools.getFileChecksum(path);
			if (!hashChecksumList.containsKey(checksum)) {
				List<Path> pathsList = new ArrayList<Path>();
				pathsList.add(path);
				hashChecksumList.put(checksum, pathsList);
			} else {
				List<Path> pathsList = hashChecksumList.get(checksum);
				pathsList.add(path);
			}
		}

		// Now, we fill a second Hashtable with the help of the precedent but
		// only with the checksum which have at list two locations (It means
		// that there is at least two identical files for a given checksum).
		Hashtable<Long, List<Path>> identicalFilesList = new Hashtable<Long, List<Path>>();
		Set<Long> checksums = hashChecksumList.keySet();
		for (Long checksum : checksums) {
			List<Path> pathsList = hashChecksumList.get(checksum);
			if (pathsList.size() > 1) {
				identicalFilesList.put(checksum, pathsList);
			}
		}
		return identicalFilesList;
	}
	
	/**
	 * Method which creates the String which will be written in the HTML result file : the list of all the file found.
	 * 
	 * @param hashFilesList
	 * @return
	 * @throws IOException
	 */
	private String fillHtmlFile(Hashtable<Long, List<Path>> hashFilesList) throws IOException {
		String htmlFile = beginnig() + ARRAY_BEGINNING;
		Set<Long> fileChecksums = hashFilesList.keySet();
		for (Long checksum : fileChecksums) {
			htmlFile += "<tr>\n" + "<td>" + checksum.toString() + "</td>\n";
			htmlFile += "<td><ul>\n";
			List<Path> pathsList = hashFilesList.get(checksum);
			for (Path path : pathsList) {
				htmlFile += "<li><a href=\"" + path.toString() + "\">" + path.toString() + "</a></li>";
			}
			htmlFile += "</ul></td>\n";
		}
		htmlFile += "</tr>\n" + "</table>\n";
		return htmlFile;
	}

	/**
	 * Creates the Hashtable with the list of identical files and return the HTML result String.
	 * 
	 * @throws EasyFindFilesExceptions
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public String execute() throws EasyFindFilesExceptions, IOException, NoSuchAlgorithmException {
		Hashtable<Long, List<Path>> hashFilesList = searchIdenticalFiles();
		return fillHtmlFile(hashFilesList);
	}

	private String beginnig() {
		return "<h1>Liste des fichiers qui sont susceptibles d'être identiques</h1>\n"
				+ "<p>La recherche a été effectuée dans le répertoire : <span style=\"color:blue\">\n"
				+ directoryToTestPath
				+ "\n"
				+ "</span></p>\n"
				+ "<p style=\"color:red\">ATTENTION : les fichiers indiqués sont fortements suceptibles d'être identiques. MAIS il existe de rares cas où ils pourront être différents ! Il est donc important de vérifier le contenu avant toute suppression !</p>\n";
	}
	
	private final String ARRAY_BEGINNING = "<table style=\"width:100%\">\n" + "<tr>\n" + "<th>Checksum</th>\n"
			+ "<th>Emplacements de fichiers susceptibles d'être identiques</th>\n" + "</tr>\n";

}
