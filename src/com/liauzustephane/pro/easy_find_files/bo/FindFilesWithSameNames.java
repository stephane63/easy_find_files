package com.liauzustephane.pro.easy_find_files.bo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.liauzustephane.pro.easy_find_files.exceptions.EasyFindFilesExceptions;
import com.liauzustephane.pro.easy_find_files.utils.FileTools;

/**
 * @author stephane This class is used to find files with same names in a given
 *         directory.
 */
public class FindFilesWithSameNames {

	private String directoryToTestPath;

	public FindFilesWithSameNames() {
	}

	public FindFilesWithSameNames(String directoryToTestPath) {
		this.directoryToTestPath = directoryToTestPath;
	}

	public String getDirectoryToTestPath() {
		return directoryToTestPath;
	}

	public void setDirectoryToTestPath(String directoryToTestPath) {
		this.directoryToTestPath = directoryToTestPath;
	}

	/**
	 * Method which return a Hashtable where the keys are the names of the files
	 * and the values a List which contains all the paths where this file has
	 * been found.
	 * 
	 * @return
	 * @throws EasyFindFilesExceptions
	 * @throws IOException
	 */
	private Hashtable<Path, List<Path>> searchSameNameFiles() throws EasyFindFilesExceptions, IOException {
		Hashtable<Path, List<Path>> hashFilesList = new Hashtable<Path, List<Path>>();
		List<Path> fileList = FileTools.listFilesInDirectory(directoryToTestPath);
		fileList = FileTools.removeHiddenFiles(fileList);
		
		// For all the files found in the directory we fill the Hashtable.
		for (Path path : fileList) {
			Path pathName = path.getFileName();
			if (!hashFilesList.containsKey(pathName)) {
				List<Path> pathsList = new ArrayList<Path>();
				pathsList.add(path.getParent());
				hashFilesList.put(pathName, pathsList);
			} else {
				List<Path> pathsList = hashFilesList.get(pathName);
				pathsList.add(path.getParent());
			}
		}

		// Now, we fill a second Hashtable with the help of the precedent but
		// only with the files which have at list two locations.
		Hashtable<Path, List<Path>> sameNameFilesList = new Hashtable<Path, List<Path>>();
		Set<Path> fileNames = hashFilesList.keySet();
		for (Path pathName : fileNames) {
			List<Path> pathsList = hashFilesList.get(pathName);
			if (pathsList.size() > 1) {
				sameNameFilesList.put(pathName, pathsList);
			}
		}
		return sameNameFilesList;
	}

	/**
	 * Method which creates the String which will be written in the HTML result file : the list of all the file found.
	 * 
	 * @param hashFilesList
	 * @return
	 * @throws IOException
	 */
	private String fillHtmlFile(Hashtable<Path, List<Path>> hashFilesList) throws IOException {
		String htmlFile = beginnig() + ARRAY_BEGINNING;
		Set<Path> fileNames = hashFilesList.keySet();
		for (Path pathName : fileNames) {
			htmlFile += "<tr>\n" + "<td>" + pathName.toString() + "</td>\n";
			htmlFile += "<td><ul>\n";
			List<Path> pathsList = hashFilesList.get(pathName);
			for (Path path : pathsList) {
				htmlFile += "<li><a href=\"" + path.toString() + "\">" + path.toString() + "</a></li>";
			}
			htmlFile += "</ul></td>\n";
		}
		htmlFile += "</tr>\n" + "</table>\n";
		return htmlFile;
	}

	/**
	 * Creates the Hashtable with the list of the files having several locations and generates the HTML result file.
	 * 
	 * @throws EasyFindFilesExceptions
	 * @throws IOException
	 */
	public String execute() throws EasyFindFilesExceptions, IOException {
		Hashtable<Path, List<Path>> hashFilesList = searchSameNameFiles();
		return fillHtmlFile(hashFilesList);
	}

	private String beginnig() {
		return "<h1>Liste des fichiers qui se nomment de la même manière</h1>\n"
				+ "<p>La recherche a été effectuée dans le répertoire : <span style=\"color:blue\">\n"
				+ directoryToTestPath
				+ "\n"
				+ "</span></p>\n"
				+ "<p style=\"color:red\">ATTENTION : 2 fichiers peuvent avoir le même nom MAIS ne pas être identiques ! Il est important de vérifier le contenu avant toute suppression !</p>\n";
	}

	private final String ARRAY_BEGINNING = "<table style=\"width:100%\">\n" + "<tr>\n" + "<th>Nom du fichier</th>\n"
			+ "<th>Emplacements trouvés où se trouve un fichier ayant ce nom</th>\n" + "</tr>\n";

}
