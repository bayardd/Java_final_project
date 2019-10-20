package edu.odu.cs.cs350.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.odu.cs.cs350.CodeComp;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.odu.cs.cs350.objects.Document;
import edu.odu.cs.cs350.objects.Score;
import edu.odu.cs.cs350.objects.Student;
/**
 *A collection of static methods responsible for creating output to the terminal and to an Excel file.
 */
public class OutputHandler {
	
	/**
	 * Produce Excel Spreadsheet containing Student Pair, Raw Score, and Z-Score.
	 * The spreadsheet will fall back to the default template if the user fails to provide one.
	 * @param scores The sorted ArrayList of Score objects
	 * @param template The template taken from the CLI as a String
	 * @param spreadsheetName The spreadsheet name taken from the CLI as a String
	 * @param outputDirectory The output directory taken from the CLI as a String, the Excel file will be stored here
	 * @throws Exception
	 */
	public static void produceSpreadsheet(ArrayList<Score> scores, String template, String spreadsheetName, File outputDirectory) throws Exception {
	    		
		// Check if user provides template or spreadsheet name
		// Load default template "Empty (Except for header)" or user specified template
		// create spreadsheet based on name provided, or "Report" if none is specified
		// Loop through score objects and write them to template
		// Provide proper formatting if desired
		
		File templateFile = null;
		InputStream inputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet DefaultSheet = null;
		FileOutputStream fileOut = null;
		boolean usingDefaultTemplate = false;
		//File defaultTemplate = new File("./src/test/data/testExcel/DefaultTemplate.xlsx");
		InputStream defaultTempStream = CodeComp.class.getResourceAsStream("/DefaultTemplate.xlsx");
	
		try {
			templateFile = new File(template);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Provided spreadsheetName is not entered, resort to default
		if(spreadsheetName.isEmpty()) {
			spreadsheetName = "Report";
		}
		
		// Provided a file that does not exist, revert to default
		if(templateFile.isFile() != true) {
			//templateFile = defaultTemplate;
			usingDefaultTemplate = true;
		}

		// Provided invalid extensions, revert back to default file
		String extension = FilenameUtils.getExtension(templateFile.getName());

		if(extension.equals("xlsx") != true) {
			System.out.println("Invalid File Extension For Excel Spreadsheet");
			
			//templateFile = defaultTempStream;
			usingDefaultTemplate = true;
		}
				
		
		// Assuming that the file location is correct
		try {
			if (!usingDefaultTemplate) {
				inputStream = new FileInputStream(templateFile);
			}
			else {
				inputStream = defaultTempStream;
			}
			workbook = new XSSFWorkbook(inputStream);
		    DefaultSheet = workbook.getSheetAt(0);
		    
		} catch(Exception e) {
			e.getSuppressed();
		}
				
		// Set spreadsheet name
		workbook.setSheetName(0, spreadsheetName);
		
		
		// Making precision to two significant figures
		XSSFCellStyle sigFigs = workbook.createCellStyle();
		XSSFCellStyle Alignment = workbook.createCellStyle();
		
		sigFigs.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.000000"));
		sigFigs.setAlignment(HorizontalAlignment.CENTER);
		sigFigs.setVerticalAlignment(VerticalAlignment.BOTTOM);
		
		Alignment.setAlignment(HorizontalAlignment.CENTER);
		Alignment.setVerticalAlignment(VerticalAlignment.BOTTOM);
		
		DefaultSheet.setDefaultColumnStyle(0, Alignment);
		DefaultSheet.setDefaultColumnStyle(1, Alignment);
		// Columns 2 and 3 have 
		DefaultSheet.setDefaultColumnStyle(2, sigFigs);
		DefaultSheet.setDefaultColumnStyle(3, sigFigs);
		
		// We know that the first row always contains the header, and that column ordering will not change
		// Iterate through scores, 1 row per score, 4 cells per score
		int counter = 0;
		
		for(Score score : scores) {
			counter ++;
			
			Row row = DefaultSheet.createRow(counter);
			
			row.createCell(0).setCellValue(score.getStudents()[0].getName());
			row.createCell(1).setCellValue(score.getStudents()[1].getName());
			row.createCell(2).setCellValue(score.getRawScore());
			row.createCell(3).setCellValue(score.getZScore());
			
		}
		
		for(int i = 0; i < 4; i++) {
			DefaultSheet.autoSizeColumn(i);	
		}
		
		
		try{
			fileOut = new FileOutputStream(outputDirectory.getCanonicalPath() + "/"  + "Report.xlsx"); 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		workbook.write(fileOut);
		
		fileOut.close();
	}
	
	/**
	 * Print final output to terminal
	 * @param students ArrayList of Student objects
	 * @throws IOException
	 */
	public static void produceOutput(ArrayList<Student> students) throws IOException {
		StringBuffer stringBuilder = new StringBuffer();

		for(Student student : students) {
			int totalLines = 0;
			
			stringBuilder.append("Student Directory: " + student.getName() + "    ");
			ArrayList<Document> documents = student.getDocumentArrayList();
			
			for(Document document : documents) {
				totalLines += document.countLines();
			}
			
			stringBuilder.append("Files: " + documents.size() + "    ");
			stringBuilder.append("LC: " + totalLines + '\n');
		}	
		
		System.out.println(stringBuilder);

	}
}
