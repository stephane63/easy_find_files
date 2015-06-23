package com.liauzustephane.pro.easy_find_files.bo;

/**
 * @author stephane This class is used to return a good name for the result
 *         file.
 */
public class ResultFile {

	private String resultFilePath; // Path = Parent/Name (for information, here,
								   // a Path is the composition of a Parent
								   // followed by a Name)
	private String resultFileName;
	private String resultFileParent;

	public ResultFile(String resultFileName, String resultFileParent) {
		setResultFileName(resultFileName);
		this.resultFileParent = resultFileParent;
		this.resultFilePath = resultFileParent + "/" + getResultFileName();
	}

	public String getResultFileName() {
		return resultFileName;
	}

	public void setResultFileName(String resultFileName) {
		if (resultFileName.endsWith(".html")) {
			this.resultFileName = resultFileName;
		} else {
			this.resultFileName = resultFileName + ".html";
		}
	}

	public String getResultFileParent() {
		return resultFileParent;
	}

	public void setResultFileParent(String resultFileParent) {
		this.resultFileParent = resultFileParent;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

}
