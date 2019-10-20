package edu.odu.cs.cs350.objects;

/**
 * Represents the score between a student-pair. After raw score and z-score have been calculated
 * they are recorded in this object
 */
public class Score implements Comparable<Score>{
	
	private Student[] students;
	private float rawScore;
	private float zScore;

	/**
	 * Constructor with no arguments creates generic, empty Score object
	 */
	public Score() {
		this.students = new Student[2];
		this.rawScore = 0;
		this.zScore = 0;	
	}

	/**
	 * Creates a Score object with 2 student objects
	 * @param students Array of size 2 representing a Student pair
	 */
	public Score(Student[] students) {
		this.students = students;
		this.rawScore = 0;
		this.zScore = 0;
	}

	/**
	 * Sets raw score
	 * @param setScore score to set
	 */
	public void setRawScore(float setScore) {
		this.rawScore = setScore;
	}

	/**
	 * Gets raw score
	 * @return rawScore
	 */
	public float getRawScore() {
		return this.rawScore;
	}

	/**
	 * Sets Z-score
	 * @param setZScore Z-Score to set
	 */
	public void setZScore(float setZScore) {
		this.zScore = setZScore;
	}

	/**
	 * Gets z-score
	 * @return zScore
	 */
	public float getZScore() {
		return this.zScore;
	}

	/**
	 * Sets the Student array
	 * @param students Array of Student objects to set
	 */
	public void setStudents(Student[] students) {
		for(int i = 0; i < 2; i++) {
			this.students[i] = students[i];
		}
	}

	/**
	 * Gets the student array
	 * @return students
	 */
	public Student[] getStudents() {
		return this.students;
	}

	/**
	 * Tests two Score objects for equality
	 * @param obj object to test for equality
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj){
		
		if (!(obj instanceof Score)) {
	          return false;
	        }
	        Score rhs = (Score)obj;

		return (students[0].equals(rhs.getStudents()[0]) && students[1] == rhs.getStudents()[1]);

	}

	/**
	 * Outputs info related to this object
	 * @return toReturn 
	 */
	@Override
	public String toString() {
		
		String toReturn = "";
		

		toReturn = "Student 1: " + students[0].getName() + "; Student 2: " + students[1].getName()
				+ "\nRaw Score: " + this.rawScore + ", ZScore: " + this.zScore
				+ "\n-------------------------------------------------------\n";

		
		return toReturn;
	}

	/**
	 * Compares this Score object to another, testing for zScore equality
	 * @param rhs Score object to compare with
	 * @return boolean
	 */
	public int compareTo (Score rhs)
	{
        //If zScore is the same, they are equal
        if(this.zScore == rhs.zScore) 
        {
        	return 0;
        }
        return(this.zScore > rhs.getZScore()? -1 : 1);
    } 
	
}
