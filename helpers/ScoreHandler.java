package edu.odu.cs.cs350.helpers;

import java.util.*;

import edu.odu.cs.cs350.codeCompCommon.SharedPhrases;
import edu.odu.cs.cs350.objects.Document;
import edu.odu.cs.cs350.objects.Score;
import edu.odu.cs.cs350.objects.SharedTokenPhrases;
import edu.odu.cs.cs350.objects.Student;

/**
 * A collection of static functions that deal with scoring
 */
public class ScoreHandler {

	/**
	 * Creates an ArrayList of Score objects; 1 for each possible student-pair in the
	 * ArrayList of Students passed to it
	 * @param students ArrayList of Student objects
	 * @return
	 */
	public static ArrayList<Score> initScores(ArrayList<Student> students) {
		ArrayList<Score> scoretoreturn = new ArrayList<>(); ///This create a Array list of [Score].

		///This algorithm will end up creating the number of Score objects required
		for (int i = 0; i < students.size(); i++) {
			for (int ii = i + 1; ii < students.size(); ii++) //starts at student index + 1
			{
				Student[] pair = {students.get(i), students.get(ii)}; ///This create Student[student A, studentB].

				Score toAdd = new Score(pair); ///This create Score object.

				scoretoreturn.add(toAdd); ///Add pair to the Score ArrayList.
			}
		}
		sortScores(scoretoreturn);
		return scoretoreturn; /// This return ArrayList of score. <studentA studentB rawScore zScore>.
	}

	///F7 ETO-1.

	/**
	 * Takes Score arrayList and sorts it based on z-score descending
	 *
	 * @param scores ArrayList of Score objects to sort
	 */
	public static void sortScores(ArrayList<Score> scores)  ///Sort the ArrayList of Score base on Descending order.
	{
		Collections.sort(scores);
	}

	/**
	 * Creates a SharedTokenPhrase object for each student-pair
	 * @param students ArrayList of Student objects 
	 * @return ArrayList</SharedTokenPhrases>
	 */
	public static ArrayList<SharedTokenPhrases> initSharedTokenPhrases(ArrayList<Student> students) {
		System.out.println("Processing shared token phrases...");

		ArrayList<SharedTokenPhrases> stpArray = new ArrayList<>();
		SharedPhrases allPhrases = new SharedPhrases();

		//Create and populate allPhrases with all tokenized documents
		for (Student s : students) {
			for (Document doc : s.getDocumentArrayList()) {
				allPhrases.addSentence(doc.getTokenPhrase(), s.getName());
			}
		}
		SharedTokenPhrases.allPhrases = allPhrases;

		int numberPairs = students.size() * (students.size() - 1) / 2;
		int count = 1;
		//create a sharedTokenPhrase object for each student
		for (int i = 0; i < students.size(); i++) {
			for (int ii = i + 1; ii < students.size(); ii++) {
				System.out.println("Processing student-pair " + count + "/" + numberPairs);
				count++;
				SharedTokenPhrases stp = new SharedTokenPhrases(students.get(i), students.get(ii));
				populateSharedPhrases(stp);
				stpArray.add(stp);
			}
		}
		return stpArray;
	}

	/**
	 * Populates the SharedTokenPhrases object with a list of all filtered shared
	 * phrases between a student-pair
	 *
	 * @param sharedTokenPhrases SharedTokenPhrases object 
	 */
	static void populateSharedPhrases(SharedTokenPhrases sharedTokenPhrases) {
		String studentA = sharedTokenPhrases.getStudents()[0].getName();
		String studentB = sharedTokenPhrases.getStudents()[1].getName();

		ArrayList<String> filteredPhrases = new ArrayList<>();

		//filters phrases from 2 students being compared.
		for (CharSequence cs : SharedTokenPhrases.allPhrases.allPhrases()) {
			String s = cs.toString();
			Set<String> sources = SharedTokenPhrases.allPhrases.sourcesOf(s);
			if (sources.contains(studentA) && sources.contains(studentB)) {
				filteredPhrases.add(s);
			}
		}

		//sort shared phrases by length descending.
		System.out.println("Sorting " + filteredPhrases.size() + " shared phrases...");

		for (int i = 0; i < filteredPhrases.size(); i++) {

			int highestIndex = i;
			for (int ii = i + 1; ii < filteredPhrases.size(); ii++) {
				if (filteredPhrases.get(ii).length() > filteredPhrases.get(highestIndex).length()) {
					highestIndex = ii;
				}
			}
			String temp = filteredPhrases.get(i);
			filteredPhrases.set(i, filteredPhrases.get(highestIndex));
			filteredPhrases.set(highestIndex, temp);
		}

		//filter out phrases enclosed in longer phrases
		System.out.println("Filtering...");
		for (int i = filteredPhrases.size() - 1; i >= 0; i--) {
			for (int ii = i - 1; ii >= 0; ii--) {
				if (filteredPhrases.get(i).length() < filteredPhrases.get(ii).length()) {
					if (filteredPhrases.get(ii).contains(filteredPhrases.get(i))) {
						filteredPhrases.remove(i);
						break;
					}
				}
			}
		}

		sharedTokenPhrases.setPhraseArrayList(filteredPhrases);
	}

	/**
	 * Used to compare to floats with a precision of 1 * 10^-5
	 * @param a first float number to compare
	 * @param b second float number to compare
	 * @return boolean
	 */
	public static boolean isFloatEqual(float a, float b) {
		float compareFloat = b - a;

		if (compareFloat > 0.00001f || compareFloat < -0.0001f) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Calculates the raw score and z-score for every student-pair and records those scores in
	 * the student = pair's Score object
	 * @param stpArray ArrayList of SharedTokenPhrases objects
	 * @param scores ArrayList of Score objects
	 */
	public static void scorePhrases(ArrayList<SharedTokenPhrases> stpArray, ArrayList<Score> scores) {

		//this will be important, it’s stored in a static attribute
		SharedPhrases allPhrases = SharedTokenPhrases.allPhrases;
		
		//cycle through every stp object… each one represents a student-pair
		for (SharedTokenPhrases stp : stpArray) {
			//System.out.println(stp);
			
			//pull these to make it easier to find the score object
			String studentA = stp.getStudents()[0].getName();
			String studentB = stp.getStudents()[1].getName();
			Score score = new Score();
			score.setRawScore(0f);
			
			for (Score iterateScore : scores) {
//				once you find the score object that has studentA & studentB…
//				score = this loops score object && break;\
				
				if(iterateScore.getStudents()[0].getName().equals(studentA) && iterateScore.getStudents()[1].getName().equals(studentB) || 
						iterateScore.getStudents()[0].getName().equals(studentB) && iterateScore.getStudents()[1].getName().equals(studentA)){
					score = iterateScore;
					break;
				}
				
			}
			Set<String> traversedPhrases = new HashSet<String>();
			//running T in formula
			float T = 0f;
			
//			*//cycle through every shared phrase in the stp object
			for (String phrase : stp.getPhraseArrayList()) {
				
				String currentPhrase = "";
				int lenP = 0;
				int k = 0;
//				**//cycle through every letter in the phrase
				for (int i = 0; i < phrase.length(); i++) {
					currentPhrase += phrase.charAt(i);

					if(traversedPhrases.contains(currentPhrase)) {
						continue;
					}
					else {
						traversedPhrases.add(currentPhrase);
						lenP = currentPhrase.length();
						
					    k = (allPhrases.sourcesOf(currentPhrase).size());
						T+=((lenP) / (Math.pow((k-1), 2.0)));
					}
				}
			}
			int L1 = 0;
			for (Document doc : stp.getStudents()[0].getDocumentArrayList()) {
				L1 += doc.getTokenPhrase().length();
			}
			//System.out.println(stp.getStudents()[0].getName() + " L1: " + L1);
			int L2 = 0;
			for (Document doc : stp.getStudents()[1].getDocumentArrayList()) {
				L2 += doc.getTokenPhrase().length();
			}

 			float finalScore = ((T * 4f) / (float)Math.pow((L1 + L2), 2f));
			score.setRawScore(finalScore);
		}
		//call z-score method
		setZscore(scores);
	}

	/**
	 * Sets the z-score of a Score object
	 * @param scores ArrayList of Score objects used to set Z-Score
	 */
	private static void setZscore(ArrayList<Score> scores) {
		/*
		 * z-score = (x - u) / s
		 * where:
		 * 	x = raw score
		 * 	u = mean avg. of all scores
		 * 	s = sample standard deviation of scores
		 */
		//calculate mean of all score
		float total = 0f;
		for (int i = 0; i < scores.size(); i++) {
			total += scores.get(i).getRawScore();
		}
		float mean = total / scores.size();

		//calculate standard deviation
		float sigmaTotal = 0f;
		for (Score s : scores) {
			sigmaTotal += Math.pow((s.getRawScore() - mean), 2.0);
		}
		float standardDev = (float) Math.sqrt((1.0 / (scores.size() - 1)) * sigmaTotal);

		//calculate and set z-score for each Score object
		for (Score s : scores) {
			s.setZScore((s.getRawScore() - mean) / standardDev);
		}
	}
}

