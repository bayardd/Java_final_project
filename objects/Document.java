package edu.odu.cs.cs350.objects;

import edu.odu.cs.cs350.jflex.CppScanner;
import edu.odu.cs.cs350.jflex.JavaScanner;
import edu.odu.cs.cs350.jflex.UnicodeEscapes;
import edu.odu.cs.cs350.jflex.sym;
import java_cup.runtime.Symbol;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 *Represents a Document in a Student directory
 */
public class Document {

    private String title;
    private File file;

    /*
     * One of these attributes will be initilized and the other
     * will remain null
     */
    private JavaScanner javaScanner = null;
    private CppScanner cppScanner = null;
    private String tokenPhrase = "";
    
    /**
     * Default Constructor for Document Objects
     */
    public Document() {
    	this.title = null;
        this.file = null;
    	
    }
    /**
     * Non-Default Constructor for Document Objects
     * @param title Title of the Document 
     * @param file File Object representing a File in a student directory
     */
    public Document(String title, File file) {
        this.title = title;
        this.file = file;
    }

    /**
     * Sets title
     * @param title String representing document name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets Document title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets file
     * @param file File object representing File in student directory
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * Gets Document File attribute
     * @return file 
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the JavaScanner if this is a Java document
     * @param scanner JavaScanner
     */
    public void setJavaScanner(JavaScanner scanner) {
        javaScanner = scanner;
        this.setTokenPhraseFromScanner();
    }

    /**
     * Sets the CppScanner if this is a C++ document
     * @param scanner CPPScanner
     */
    public void setCppScanner(CppScanner scanner) {
        cppScanner = scanner;
        this.setTokenPhraseFromScanner();
    }
    /**
     * Returns True if JavaScanner is set, False otherwise
     * @return boolean
     */
    public boolean hasJavaScanner() {
        if (javaScanner != null) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Returns True if CPPScanner is set, False otherwise
     * @return boolean
     */
    public boolean hasCppScanner() {
        if (cppScanner != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets a char String representation of all the tokens in
     * this document from the Scanner, represented by s.sym + 256, which
     * shows Latin script characters
     */
    void setTokenPhraseFromScanner() {
        String tString = "";
        if (this.javaScanner != null) {
            try {
                Symbol s;
                do {
                    s = javaScanner.next_token();
                    char c = (char)(s.sym+256);
                    //System.out.println(s.sym + " " + c);
                    if (s.sym != sym.EOF) {
                        tString += c;
                    }
                }
                while (s.sym != sym.EOF);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (this.cppScanner != null) {
            try {
                Symbol s;
                do {
                    s = cppScanner.next_token();
                    char c = (char)(s.sym+256);
                    //System.out.println(s.sym + " " + c);
                    if (s.sym != sym.EOF) {
                        tString += c;
                    }

                }
                while (s.sym != sym.EOF);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.tokenPhrase = tString;
    }

    /**
     * Sets the token phrase manually, mainly to be used for testing
     * @param tp token phrase to set
     */
    public void setTokenPhrase(String tp) {
        this.tokenPhrase = tp;
    }
    
    /**
     * Gets the token phrase
     * @return tokenPhrase
     */
    public String getTokenPhrase() {
        return tokenPhrase;
    }

    /**
     * Documents are equal if they contain the same token string and same file name
     * @param o Object to compare for equality
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;

        return this.title.equals(document.getTitle()) && this.tokenPhrase.equals(document.getTokenPhrase());
    }
    
    /**
     * Hash code based on Document hashCode and tokenPhrase hashCode
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hc = 0;

        hc = this.title.hashCode() + this.tokenPhrase.hashCode();
        return hc;
    }
    
    /**
     * Display for Document object
     */
    @Override
    public String toString() {
        String returnString = "";

        try {
            returnString = "Title: " + title  + " File: ";
            if (file == null) {
                returnString += "null";
            }
            else {
                returnString += file.getCanonicalPath();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (javaScanner == null) {
            returnString += ", JavaScanner: NO";
        }
        else {
            returnString += ", JavaScanner: YES";
        }

        if (cppScanner == null) {
            returnString += ", CppScanner: NO";
        }
        else {
            returnString += ", CppScanner: YES";
        }

        if (file != null) {
            returnString += ", Ext: " + FilenameUtils.getExtension(file.getName());
        }

        return returnString;
    }
    
    /**
     * Counts number of lines in the File attribute of a Document object
     * @return Number of lines in File object
     * @throws IOException
     */
    public int countLines() throws IOException {
    	int lineCount = 0;
    	BufferedReader reader = new BufferedReader(new FileReader(this.file));
    	
    	while (reader.readLine() != null) {
    		lineCount++;
    	}
    	reader.close();
    	
    	return lineCount;
    }
    
    
}
