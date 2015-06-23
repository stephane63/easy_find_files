package com.liauzustephane.pro.easy_find_files.main;

import com.liauzustephane.pro.easy_find_files.views.EasyFindFilesFrame;

public class EasyFindFiles {

	public static void main(String[] args) {
		
		try{
			EasyFindFilesFrame easyFindFilesFrame = new EasyFindFilesFrame();
			easyFindFilesFrame.pack();
			easyFindFilesFrame.setVisible(true);
		}catch(Exception e){
			System.out.println(e);
		}

	}

}
