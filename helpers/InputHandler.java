package edu.odu.cs.cs350.helpers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.odu.cs.cs350.jflex.CppScanner;
import edu.odu.cs.cs350.jflex.JavaScanner;
import edu.odu.cs.cs350.jflex.UnicodeEscapes;
import edu.odu.cs.cs350.objects.Document;
import edu.odu.cs.cs350.objects.Student;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.exception.NullArgumentException;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * A collection of static methods responsible for all input. This ranges from reading from the CLI to reading the assignment directory
 * and it's sub directories in order to create Student and Document objects.
 * 
 */
public class InputHandler {
	/**
	 * Takes the Assignment Directory and Output Directory as Strings, 
	 * then validates that File objects can be made of these strings. 
	 * @param asstDir The assignment directory
	 * @param outputDir The output directory
	 * @return File[] 
	 * @throws IOException
	 */
	public static File[] validateDirectory(File asstDir, File outputDir) throws IOException  ///pass in (assignment directory and  output directory)
    {
    
    	boolean canReadAsstDir = false;
    	boolean canWriteAsstDir = false;
    	boolean asstDirExists = false;
    	boolean isAsstDirectory = false;
    	boolean canReadOutputDir = false;
    	boolean canWriteOutputDir = false;
    	boolean outputDirExists = false;
    	boolean isOutputDirectory = false;
    	
    	//First, try to make File form assignment directory
		canReadAsstDir = asstDir.canRead();
		canWriteAsstDir = asstDir.canWrite();
		asstDirExists = asstDir.exists();
		isAsstDirectory = asstDir.isDirectory();
    	
		// if the directory does not exist display an error
		if(isAsstDirectory == false) {
			if (asstDirExists) {
				System.out.println("Error: Not a valid directory.");
				return new File[0];
			}
			else {
				System.out.println("Error: Assignment directory does not exist");
				return new File[0];
			}
		}
		
    	// File AsstDir must be read, write, it must exist, and it must be a directory, quit otherwise
    	if (canReadAsstDir == false || canWriteAsstDir == false)
    		{
    			System.out.println("Error: Assignment Directory does not have correct read/write permissions");
    			return new File[0];
    		}
    	
    	
    	// Try to make file object from outputDir path. 
		File outputDir1 = outputDir;
		outputDir1.mkdir();
    	
    	if (!outputDir1.canRead() || !outputDir1.canWrite()) ///if not exists.
			{
				System.out.println("Error: output directory does not have correct read/write permissions");
				return new File[0];
			}
    	
    	File file[] = new File[2];
    	file[0] = asstDir;
    	file[1] = outputDir1;
    
    	return file;
    }
    
	/**
	 * Takes the arguments input into the command line. 
	 * This method is responsible for parsing the command line input,
	 * and validating that the Assignment directory and Output Directory are provided.
	 * This method will also recognize any flags, and display messages to the terminal.
	 * @param String[] The string array to be parsed
	 * @return String[] - String array containing parsed command line arguments
	 */
    public static String[] commandLineManager(String[] parameters) {
    	
    	
    	JSAP jsap = new JSAP();
    	//test
    	FlaggedOption template = new FlaggedOption("templateSpreadsheet")
    			.setStringParser(JSAP.STRING_PARSER)
    			.setDefault("DefaultTemplate.xlsx")
    			.setRequired(false)
    			.setShortFlag('t')
    			.setLongFlag("template");
    	
    	FlaggedOption report = new FlaggedOption("sheetname")
    			.setStringParser(JSAP.STRING_PARSER)
    			.setDefault("Report")
    			.setRequired(false)
    			.setShortFlag('r')
    			.setLongFlag("report");
    	
    	UnflaggedOption asstDir = new UnflaggedOption("assignmentDirectory")
    			.setStringParser(JSAP.STRING_PARSER)
    			.setRequired(true);
    	
    	UnflaggedOption outputDir = new UnflaggedOption("outputDirectory")
    			.setStringParser(JSAP.STRING_PARSER)
    			.setRequired(true);
    	
    	
    	//Switch for -help flag
    	Switch helpFlag = new Switch("help")
    						.setShortFlag('h')
    						.setLongFlag("help");
    	
   	
    	//Set help messages
    	template.setHelp("Replaces the default spreadsheet template with one provided");
    	report.setHelp("Replaces the sheet name used to identify where to place the report data scores");
    	asstDir.setHelp("The assignmentDirectory is a path to the root directory of the assignment, "
    			+ "containing a number of submission directories. The assignmentDirectory parameter is required");
    	helpFlag.setHelp("List of available parameters");
    	outputDir.setHelp("The outputSpreadsheet is a path to where the output (an Excel spreadsheet) should be stored."
    			+ " The outputSpreadsheet parameter is required");
    	
    
    	try {
			jsap.registerParameter(template);
			jsap.registerParameter(report);
			jsap.registerParameter(asstDir);
			jsap.registerParameter(outputDir);
			jsap.registerParameter(helpFlag);
		} catch (JSAPException e) {
			// TODO Auto-generated catch block
			System.out.println("TEST");
		}
    	jsap.setUsage("java -jar CodeComp.jar [options] assignmentDirectory outputSpreadsheet" + '\n');
    	
    	
    	// Parses args
    	JSAPResult parsedCMI = jsap.parse(parameters);
    	
    	if(parsedCMI.getBoolean("help") == true) {
    		System.out.println(jsap.getUsage());
    		System.out.println(jsap.getHelp());
    	}
    	
    	//If failed to parse and help flag was not set
    	if (!parsedCMI.success() && parsedCMI.getBoolean("help") == false) {
    		
    		if(parsedCMI.getString("assignmentDirectory") == null) {
    			System.out.println("Error: Assignment directory parameter was not provided");
    		}
    		if(parsedCMI.getString("outputDirectory") == null) {
    			System.out.println("Error: Output directory was not provided");
    		}
    		
            System.out.println();
            System.out.println("Usage: java ");
            System.out.println(jsap.getUsage());
            System.out.println();
            System.out.println(jsap.getHelp());
        }

        String setTemplate = parsedCMI.getString("templateSpreadsheet");
        String setReport = parsedCMI.getString("sheetname");
    	String setAsstDir = parsedCMI.getString("assignmentDirectory");
    	String setOutputDir = parsedCMI.getString("outputDirectory");

    	String[] parsedParameters = {setTemplate, setReport, setAsstDir, setOutputDir};
    	
    	return parsedParameters;
    	
    }
	
	
	// Creates an ArrayList of Students, representing each filtered Student directory
	
    /**
     * Takes a File object representing the assignment directory and creates Student objects for each unique student in the assignment directory
     * @param File The assignment directory as a File object
     * @return ArrayList<Student> - An ArrayList of Student objects representing each directory in the assignment directory.
     */
    public static ArrayList<Student> initStudents(File asstDir){
		
		ArrayList<Student> students = new ArrayList<Student>();
		ArrayList<Student> tempArray = new ArrayList<Student>();
		File[] subDirectories = asstDir.listFiles();
		Boolean found = false;
		int oldDirectory = 0;
		int newDirectory = 0;
		Iterator<Student> studentItr;
		
		// Loop through student subdirectories and identify basename and extension
		for(File studentDir : subDirectories) {
			found = false;
			String basename = FilenameUtils.getBaseName(studentDir.getName());	
			String extension = FilenameUtils.getExtension(studentDir.getName());
			
			
			// Clear old values from Array
			tempArray.clear();
			
			//Every time iteration restarts, update array that is being iterated through
			for(Student element : students) {
				tempArray.add(element);
			}
			
			
			// Initialize ArrayList with first student
			if(tempArray.isEmpty() == true) {
				
				Student firstStudent = new Student(basename, studentDir);
				students.add(firstStudent);
				found = true;

			}
			
			else {
				
				studentItr = tempArray.iterator();
				while(studentItr.hasNext()) {
					Student nextStudent = studentItr.next();
					String fileExtension = FilenameUtils.getExtension(nextStudent.getFile().getName());
					
					
					if(basename.equalsIgnoreCase(nextStudent.getName())) {
	
						found = true;

						// If extension from list of files does not exist
						if(extension == "") {
							oldDirectory = 1;
							newDirectory = 0;
						}
						// If one of the directories in the array does not have an extension
						else if(fileExtension == "") {
							break;
						}
						
						else {
							oldDirectory = Integer.parseInt(extension);
							newDirectory = Integer.parseInt(fileExtension);
						}
						
						if(oldDirectory > newDirectory){
							
							//Remove old directory
//							System.out.println("Removing old directory " + nextStudent.getName() + " " + FilenameUtils.getExtension(nextStudent.getFile().getName()));
							students.remove(nextStudent);
							
							Student newestDirectory = new Student(basename, studentDir);
//							System.out.println("Adding New Directory" + newestDirectory.getName() + " " + FilenameUtils.getExtension(newestDirectory.getFile().getName()));
							students.add(newestDirectory);
						}
					}
				}
			}

			if(found == false) {
				
				Student toAdd = new Student(basename, studentDir);
				students.add(toAdd);
			}
		}
		
		return students;
	}
	
	/**
	 * Takes the ArrayList of Student objects and initializes each Student with an ArrayList of Document objects.
	 * @param students ArrayList of Student objects
	 */
	public static void initDocuments(ArrayList<Student> students) {
		
		int len = students.size();
		
		for (int z=0;z<len;z++)																	//Loop through each of the Students in the students ArrayList
		{
			Student Sorey = students.get(z) ;
			File currentF = Sorey.getFile();
			ArrayList<Document> init = new ArrayList<Document>() ;

			Sorey.setDocumentArrayList(fileingDocs(currentF,init));        //* For each student, recursively scan the directory until the .java or .cpp files are found
		}																								
	}
	
	/**
	 * Loops through each File object in the student directory, accumulating files with valid extensions.
	 * @param dir The Student object
	 * @param sFiles empty ArrayList of Document objects
	 * @return ArrayList<Document> - ArrayList containing all of the documents in the student directory
	 */
	public static ArrayList<Document> fileingDocs(File dir , ArrayList<Document> sFiles) 
	{
//		if(dir.exists())
//		{
			File[] arrFiles = dir.listFiles();
			
			for (File doc : arrFiles) 
			{
				
				if (doc.isDirectory()) 
				{
				//	System.out.println("this is a dir: " + doc.toString());
					fileingDocs(doc, sFiles);
				} 
				else 
				{

					if ( FilenameUtils.getExtension(doc.toString()).equalsIgnoreCase("java")|| FilenameUtils.getExtension(doc.toString()).equalsIgnoreCase("cpp")
							|| FilenameUtils.getExtension(doc.toString()).equalsIgnoreCase("h") )//check for type
					{
						if(doc.canRead() && doc.canWrite())
						{
							// System.out.println("  this is a file" + doc.toString() + " of type " + FilenameUtils.getExtension(doc.toString()) );
							Document toAdd = new Document(doc.getName() , doc); 					//Create doc obj 
							sFiles.add(toAdd);														//set it to trasferlist
							
						}																							//** loop through all of the .cpp, or .java files and make File objects for each of them
						else 
						{
							System.out.println("\r The File: " + doc.toString() + " Does not have correct permissions \r");
						}
						
					}
					else
					{
						System.out.println("\r The Directory: " + doc.toString() + " Does not contain valid file types. Dir= Empty \r");
					}

				}
			}
		return sFiles;
	}

	
    /**
     * Examines every document of every student, and translates that document into
     * its "tokenized" form
     * @param students ArrayList of student objects
     */
    public static void tokenizeDocuments(ArrayList<Student> students) {

		ArrayList<Student> studentArrayList = students;

		for (int i = 0; i < studentArrayList.size(); i++) {

			for (int ii = 0; ii < studentArrayList.get(i).getDocumentArrayList().size(); ii++) {

				Document doc = studentArrayList.get(i).getDocumentArrayList().get(ii);
				String extension = FilenameUtils.getExtension(doc.getFile().getName());

				if (extension.equalsIgnoreCase("java")) {
					try {
						UnicodeEscapes unicodeEscapes = new UnicodeEscapes(new FileReader(doc.getFile()));
						JavaScanner js = new JavaScanner(unicodeEscapes);
						doc.setJavaScanner(js);
					}
					catch (Exception e) {e.printStackTrace();}

				}

				else if (extension.equalsIgnoreCase("cpp") || extension.equalsIgnoreCase("h")) {
					try {
						UnicodeEscapes unicodeEscapes = new UnicodeEscapes(new FileReader(doc.getFile()));
						CppScanner cs = new CppScanner(unicodeEscapes);
						doc.setCppScanner(cs);
					}
					catch (Exception e) {e.printStackTrace();}
				}
			}
		}
    }
}
