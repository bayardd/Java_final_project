package edu.odu.cs.cs350.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

/**
 * Represents a Student in the assignment directory.
 */
public class Student {

    private String name;
    private File file;
    private ArrayList<Document> documentArrayList;

	/**
	 * Default constructor 
	 */
    public Student() {
        this.name = "";
        this.file = null;
        this.documentArrayList = new ArrayList<>();
    }
    
	/**
	 * Non-Default Constructor which name attribute of Student object
	 * @param string of type String
	 */
    public Student(String name) {
        this.name = name;
        file = null;
        this.documentArrayList = new ArrayList<>();
    }
    
	/**
	 * Non-Default Constructor - Set both name and file attributes
	 * @param name Name of the Student, representing a student directory
	 * @param file File object representing student directory within assignment directory
	 */
    public Student(String name, File file) {
        this.name = name;
        this.file = file;
        this.documentArrayList = new ArrayList<>();
    }
    
	/**
	 * Sets name
	 * @param name String which will set the name attribute 
	 */
    public void setName(String name) {
    	this.name = name;
    }
    
	/**
	 * Get name attribute
	 * @return name
	 */
    public String getName() {
    	return this.name;
    }
    
	/**
	 * Set File attribute
	 * @param fileToSet File object to set 
	 */
    public void setFile(File fileToSet) {
    	this.file = fileToSet;
    }
    
	/**
	 * Get file attribute
	 * @return file 
	 */
    public File getFile() {
    	return this.file;
    }
    
    /**
     * Get ArrayList of Document objects
     * @return ArrayList<Document>
     */
    public ArrayList<Document> getDocumentArrayList() {
        return documentArrayList;
    }

	/**
	 * Set ArrayList<Document> attribute
	 * @param ArrayList<Document> List of documents to set
	 */
    public void setDocumentArrayList(ArrayList<Document> documentArrayList) {
        this.documentArrayList = documentArrayList;
    }
    
    /**
     * Checks if two Student objects are equal
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
    	
        if (!(obj instanceof Student)) {
          return false;
        }
        Student rhs = (Student)obj;
        
        return (this.name.equals(rhs.getName()));
        
    }
    
    /**
     * Hash code for each Student object based on name attribute
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /*
     * Outputs info relating to Students
     * @return returnString
     */
    @Override
    public String toString() {

        String returnString = "";

        returnString += "Name: " + name;

        for (int i = 0; i < documentArrayList.size(); i++) {
            returnString += "\nDoc " + i + ": " + documentArrayList.get(i).toString();
        }

        return returnString;
    }
}

