package com.liauzustephane.pro.easy_find_files.views;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.liauzustephane.pro.easy_find_files.bo.FindFilesWithSameNames;
import com.liauzustephane.pro.easy_find_files.bo.FindIdenticalFiles;
import com.liauzustephane.pro.easy_find_files.bo.ResultFile;
import com.liauzustephane.pro.easy_find_files.exceptions.EasyFindFilesExceptions;
import com.liauzustephane.pro.easy_find_files.utils.FileTools;

public class EasyFindFilesFrame extends JFrame implements ActionListener {

	private ResultFile resultFile;
	private FindFilesWithSameNames findFilesWithSameNames;
	private FindIdenticalFiles findIdenticalFiles;
	private static final String DEFAULT_RESULT_FILE_NAME = "easy_find_files_result.html";
	private String directoryToTestPath;
	private String resultFileParent;
	private String resultFileName;

	private static final Color COLOR = Color.cyan;
	private Border border;
	private Container container;
	private Box box, box1, box2, box3;
	private JLabel jl1, jl2, jl3, jl4;
	private JTextField tfDirectoryDisplayChoice, tfResultDirectoryDisplayChoice, tfResultFileNameEntry;
	private JButton buttonDirectoryToTest, buttonResultFileParent, submit;
	private JCheckBox cbfindFilesWithSameName;
	private JCheckBox cbfindIdenticalFiles;

	public EasyFindFilesFrame() {

		setTitle("Easy find files");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		border = BorderFactory.createLineBorder(COLOR);

		// Creation of the different elements which will compose the Frame

		jl1 = new JLabel(" --> Sélection du dossier à analyser : ");

		buttonDirectoryToTest = new JButton("Choix du répertoire");
		buttonDirectoryToTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// fileChooser.setAcceptAllFileFilterUsed(false);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					tfDirectoryDisplayChoice.setText(selectedFile.getAbsolutePath());
					directoryToTestPath = selectedFile.getAbsolutePath();
				}
			}
		});

		tfDirectoryDisplayChoice = new JTextField();
		tfDirectoryDisplayChoice.setEditable(false);
		tfDirectoryDisplayChoice.setBackground(COLOR);
		tfDirectoryDisplayChoice.setBorder(border);
		tfDirectoryDisplayChoice.setColumns(50);

		jl2 = new JLabel(" --> Sélection du dossier où mettre le fichier contenant les résultats : ");

		buttonResultFileParent = new JButton("Choix du répertoire");
		buttonResultFileParent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					tfResultDirectoryDisplayChoice.setText(selectedFile.getAbsolutePath());
					resultFileParent = selectedFile.getAbsolutePath();
				}
			}
		});

		tfResultDirectoryDisplayChoice = new JTextField();
		tfResultDirectoryDisplayChoice.setEditable(false);
		tfResultDirectoryDisplayChoice.setBackground(COLOR);
		tfResultDirectoryDisplayChoice.setBorder(border);
		tfResultDirectoryDisplayChoice.setColumns(50);

		jl3 = new JLabel(" --> Entrez le nom souhaité pour le fichier résultat : ");

		tfResultFileNameEntry = new JTextField();
		tfResultFileNameEntry.setEditable(true);
		tfResultFileNameEntry.setBackground(Color.white);
		tfResultFileNameEntry.setBorder(border);
		tfResultFileNameEntry.setText(DEFAULT_RESULT_FILE_NAME);

		jl4 = new JLabel(" --> Choisir les actions à exécuter : ");

		cbfindFilesWithSameName = new JCheckBox("Trouver les fichiers qui ont le même nom.");
		cbfindIdenticalFiles = new JCheckBox("Trouver les fichiers identiques.");

		submit = new JButton("Lancer la recherche");
		submit.addActionListener(this);

		// Setting of the different elements into the Frame
		container = getContentPane();
		container.setBackground(COLOR);
		box = Box.createVerticalBox();
		container.add(box);

		box.add(jl1);

		box1 = Box.createHorizontalBox();
		box1.setAlignmentX(LEFT_ALIGNMENT);
		box.add(box1);
		box1.add(buttonDirectoryToTest);
		box1.add(tfDirectoryDisplayChoice);

		box.add(jl2);

		box2 = Box.createHorizontalBox();
		box2.setAlignmentX(LEFT_ALIGNMENT);
		box.add(box2);
		box2.add(buttonResultFileParent);
		box2.add(tfResultDirectoryDisplayChoice);

		box3 = Box.createHorizontalBox();
		box3.setAlignmentX(LEFT_ALIGNMENT);
		box.add(box3);
		box3.add(jl3);
		box3.add(tfResultFileNameEntry);

		box.add(jl4);
		box.add(cbfindFilesWithSameName);
		box.add(cbfindIdenticalFiles);

		box.add(submit);

	}

	public void actionPerformed(ActionEvent a) {

		String htmlResultString = "";

		resultFileName = tfResultFileNameEntry.getText();
		if (resultFileName.equals("") || directoryToTestPath == null || resultFileParent == null) {
			JOptionPane.showMessageDialog(this,
					"Il manque des informations à remplir concernant les dossiers et fichiers !", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		} else {
			resultFile = new ResultFile(resultFileName, resultFileParent);
			if (!cbfindIdenticalFiles.isSelected() && !cbfindFilesWithSameName.isSelected()) {
				JOptionPane.showMessageDialog(this, "Il faut sélectionner au moins une action !", "Erreur",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					if (cbfindIdenticalFiles.isSelected()) {
						findIdenticalFiles = new FindIdenticalFiles(directoryToTestPath);
						htmlResultString += findIdenticalFiles.execute();
					}
					if (cbfindFilesWithSameName.isSelected()) {
						findFilesWithSameNames = new FindFilesWithSameNames(directoryToTestPath);
						htmlResultString += findFilesWithSameNames.execute();
					}
					FileTools.writeResultFile(htmlResultString, resultFile.getResultFilePath());
					JOptionPane.showMessageDialog(this, "Recherche terminée !", "Message",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (EasyFindFilesExceptions e) {
					JOptionPane.showMessageDialog(this, e, "Erreur", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, e, "Erreur", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}

	}

	public FindFilesWithSameNames getFindFilesWithSameNames() {
		return findFilesWithSameNames;
	}

	public void setFindFilesWithSameNames(FindFilesWithSameNames findFilesWithSameNames) {
		this.findFilesWithSameNames = findFilesWithSameNames;
	}

	public ResultFile getResultFile() {
		return resultFile;
	}

	public void setResultFile(ResultFile resultFile) {
		this.resultFile = resultFile;
	}

	public FindIdenticalFiles getFindIdenticalFiles() {
		return findIdenticalFiles;
	}

	public void setFindIdenticalFiles(FindIdenticalFiles findIdenticalFiles) {
		this.findIdenticalFiles = findIdenticalFiles;
	}

}
