package edu.odu.cs.cs350.objects;

import edu.odu.cs.cs350.codeCompCommon.SharedPhrases;

import java.util.ArrayList;

/**
 * The collection of shared Token matches between two
 * students. These are the phrases to be scored.
 * All sub-phrases that are part of larger
 * phrases between these two to be filtered out
 */
public class SharedTokenPhrases {

    public static SharedPhrases allPhrases = new SharedPhrases();

    private Student[] students = new Student[2];
    private ArrayList<String> phraseArrayList = new ArrayList<>();

    /**
     * Takes two Student objects and creates the student-pair's
     * SharedTokenPhrase object
     * @param studentA First Student object to compare
     * @param studentB Second Student object to compare
     */
    public SharedTokenPhrases(Student studentA, Student studentB) {
        students[0] = studentA;
        students[1] = studentB;
    }

    /**
     * Returns the Student array
     * @return students
     */
    public Student[] getStudents() {
        return students;
    }

    /**
     * Sets the phraseArrayList
     * @param phraseArrayList ArrayList of Strings
     */
    public void setPhraseArrayList(ArrayList<String> phraseArrayList) {
        this.phraseArrayList = phraseArrayList;
    }

    /**
     * Gets the phraseArrayList
     * @return phraseArrayList
     */
    public ArrayList<String> getPhraseArrayList() {
        return phraseArrayList;
    }

    /**
     * Deletes all the phrases recorded in the allPhrases collection
     */
    public static void resetAllPhrases() {
        allPhrases = new SharedPhrases();
    }

    /**
     * Outputs info related to this object
     * @return String
     */
    @Override
    public String toString() {
        String sharedPhrases = "";
        if (phraseArrayList.size() > 0) {
            int count =0;
            for (CharSequence phrase : phraseArrayList) {
                sharedPhrases += phrase.toString() + "\n";
            }
        }
        else {
            sharedPhrases = "None";
        }
        return   "\n----------------------------------------------\n" + "SharedPhrases between: " + students[0] + " : " + students[1]
                + "\n----------------------------------------------\n"
                + sharedPhrases + "Total phrases: " + phraseArrayList.size();
    }
}
