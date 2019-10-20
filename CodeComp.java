package edu.odu.cs.cs350;

import edu.odu.cs.cs350.helpers.InputHandler;
import edu.odu.cs.cs350.helpers.OutputHandler;
import edu.odu.cs.cs350.helpers.ScoreHandler;
import edu.odu.cs.cs350.objects.Document;
import edu.odu.cs.cs350.objects.Score;
import edu.odu.cs.cs350.objects.SharedTokenPhrases;
import edu.odu.cs.cs350.objects.Student;

import java.io.IOException; 		//input

import java.io.File;
import java.util.ArrayList;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * The main class, responsible for running the entire program within the main function.
 */
public class CodeComp {
	/**
	 * Main driver for the program
	 * @param args List of arguments to parse from CLI
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException
	{
		String[] parsedParameters = InputHandler.commandLineManager(args);
    	
		String template = parsedParameters[0];
		String report = parsedParameters[1];
		String asstDirString = parsedParameters[2];
		String outputDirString = parsedParameters[3];

		//If either asstDir or outputDir were not passed we quit.. Error messages have already been displayed
		if(asstDirString == null || outputDirString == null) 
			{
				System.exit(1);
			}
		
		///validateDirectories pass in (asstDir and outputDir).
		File asstDir = new File(asstDirString);
		File outputDir = new File(outputDirString);
		File[] files = InputHandler.validateDirectory(asstDir, outputDir);
		
		//Check after verification.  If File[] has a length of 0, then the files were not verified
		if(files.length == 0) 
			{
				System.exit(1);
			}
		
		//At this point, assignment directory and output directory have been verified
		File assignmentDir = files[0];
		File outputDirectory = files[1];

		//InputHandler
		ArrayList<Student> studentArrayList = InputHandler.initStudents(assignmentDir);
		InputHandler.initDocuments(studentArrayList);
		InputHandler.tokenizeDocuments(studentArrayList);

		//ScoreHandler
		ArrayList<Score> scoreArrayList = ScoreHandler.initScores(studentArrayList);
		ArrayList<SharedTokenPhrases> stpArray = ScoreHandler.initSharedTokenPhrases(studentArrayList);
		ScoreHandler.scorePhrases(stpArray, scoreArrayList);
		ScoreHandler.sortScores(scoreArrayList);

		//OutputStream
		OutputHandler.produceOutput(studentArrayList);

		//OutputHandler
		try 
			{
				OutputHandler.produceSpreadsheet(scoreArrayList, template, report, outputDirectory);
			}
		catch (Exception e) 
			{
				e.printStackTrace();
			}
	}
}
