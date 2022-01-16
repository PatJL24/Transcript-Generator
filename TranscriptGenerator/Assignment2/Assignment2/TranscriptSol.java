/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
 READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
 the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name:
Student Number:
Course Section:
*/

package Assignment2;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;

/**
 * This class generates a transcript for each student, whose information is in the text file.
 * 
 *
 */

public class TranscriptSol {
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;
	
	
	public TranscriptSol(String inFile, String outFile) {
		inputFile = new File(inFile);	
		outputFile = outFile;	
		grade = new ArrayList<Object>();
		this.readFile();
	}
	/** 
	 * This method reads a text file and add each line as 
	 * an entry of grade ArrayList.
	 * @exception It throws FileNotFoundException if the file is not found.
	 */
	private void readFile() {
		Scanner sc = null; 
		try {
			sc = new Scanner(inputFile);	
			while(sc.hasNextLine()){
				grade.add(sc.nextLine());
	        }      
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			sc.close();
		}		
	} // end of readFile
	
	/**
	 * This method creates an ArrayList, whose element is an object of Student.
	 * The object at each element is created by aggragating ALL the information, that is 
	 * found for one student in the this.grade Arraylist. (i.e. if the text file 
	 * contains information about 9 students, then the array list as 9 elements.
	 * 
	 */
	public ArrayList<StudentSol> buildStudentArray() {
		ArrayList<StudentSol> student = new ArrayList<StudentSol>();
		ArrayList<String> oneRow = new ArrayList<String>();
		String studentId = "";
		// removes each item from grade one by one and add it to the student ArrayList
		while(!grade.isEmpty()) {
			StringTokenizer st = new StringTokenizer((String) (grade.remove(0)), ",");
		    while (st.hasMoreTokens()) {
		    	oneRow.add(st.nextToken());
		    }
		    studentId = oneRow.remove(2); // get the studentId
		    addStudent(student, studentId, oneRow);		    
		}
		return student;
		
	}
	
	private void addStudent(ArrayList<StudentSol> student, String studentId, ArrayList<String> row) {
		StudentSol studentObject = new StudentSol();
		ArrayList<AssessmentSol> assignment = new ArrayList<AssessmentSol>();
		ArrayList<Double> marks = new ArrayList<Double>();
		ArrayList<Integer> weights = new ArrayList<Integer>();
		String code = row.remove(0); // get the course code
	    double credit = Double.parseDouble(row.remove(0)); // get the course credit worth
	    // get all the assessments 
	    while (row.get(0).contains("(")) {
	    	String assessTemp = row.remove(0);
	    	assignment.add(AssessmentSol.getInstance(assessTemp.charAt(0), Integer.parseInt(assessTemp.substring(1, assessTemp.indexOf("(")))));
	    	marks.add(Double.parseDouble(assessTemp.substring(assessTemp.indexOf('(')+ 1, assessTemp.indexOf(')'))));
	    	weights.add(Integer.parseInt(assessTemp.substring(1, assessTemp.indexOf('('))));
	    }
    	// makes the course
    	CourseSol course = new CourseSol(code, assignment, credit);
    	// if the student is in the arrayList, add its course and grade to CourseTaken and finalGrade ArrayList of the student ArrayList, 
    	// otherwise add the student as a new entry of the student ArrayList
       	boolean found = false;
       	int foundIndex = 0;
        for (int i= 0; i < student.size(); i++) {
        	if (student.get(i).getStudentId().equalsIgnoreCase(studentId)) {
    			found = true;
    			foundIndex = i;
    			break;
    		}
    	}
    	if (found) {
    		student.get(foundIndex).addCourse(course);
       		student.get(foundIndex).addGrade(marks, weights);   
       		row.remove(0);
    	}
    	else {
    	    studentObject.setStudentID(studentId);
    	    studentObject.setName(row.remove(0));
    	    studentObject.addCourse(course);
    	    studentObject.addGrade(marks, weights);
    	    student.add(studentObject);
    	}
	} // end of addStudent
	/** 
	 * This method prints transcripts for all the students, who are in the 
	 * input parameter. The format of printing is like follows: 
	 *stName	stID
	 *--------------------
	 *courseCode 	grade
	 *CourseCode 	grade
	 *.
	 *.
	 *.
	 *--------------------
	 *GPA: gpa's value
	 * 
	 */
	public void printTranscript(ArrayList<StudentSol> studentArray) {
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(outputFile);			
		    for (StudentSol obj: studentArray)
		    	generateTranscript(obj, outputStream);
		}catch (Exception e) {
			System.out.println("Output file cannot be created");
		}
		finally {
			outputStream.close();
		}

	}
	private void generateTranscript(StudentSol obj, PrintWriter out) {
		out.println( obj.getName() + "\t" + obj.getStudentId());
		out.println("--------------------");
		for (int i = 0; i < obj.getCourseTaken().size(); i++) 
			out.println(obj.getCourseTaken().get(i).getCode() + " \t" + obj.getFinalGrade().get(i));
		out.println("--------------------");
		out.println("GPA: " + obj.weightedGPA());
		out.println();
	}
	
} // end of Transcript


class StudentSol{
	private String studentID;
	private String name; 
	private ArrayList<CourseSol> courseTaken;
	private ArrayList<Double> finalGrade;

	public StudentSol(){
		studentID = "";
		name = ""; 
		courseTaken = new ArrayList<CourseSol>();
		finalGrade = new ArrayList<Double>();		
	}
	public StudentSol(String studentID, String name, ArrayList<CourseSol> courseTaken){
		this.studentID = studentID;
		this.name = name; 
		this.courseTaken = new ArrayList<CourseSol>();		
		for (int i = 0; i < courseTaken.size(); i++) 
			this.courseTaken.add(new CourseSol(courseTaken.get(i)));
		
	}
	/**
	 * This method gets an array list of the grades and their weights, computes the true 
	 * value of the grade based on its weight and add it to finalGrade attribute.
	 * @param grade 
	 * @param weight
	 */
	public void addGrade(ArrayList<Double> grade, ArrayList<Integer> weight) {
		try {
			double sum = 0;
			for (int i = 0; i < weight.size(); i++) {
				sum += weight.get(i);
			}
			if ( sum != 100 ) 
				throw new InvalidTotalExceptionSol("The weight of the assessments does not add up to 100"); 			
			sum = 0; 
			for (int i = 0; i < grade.size(); i++) {
				sum += grade.get(i) *(weight.get(i)/100.0);
			}
			if ( sum > 100 ) 
				throw new InvalidTotalExceptionSol("The total mark is greater than 100"); 						
			this.finalGrade.add(Math.round(sum*10)/10.0);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		
	}
	public double weightedGPA() {
		double totalCredit = 0; 
		double totalGrade = 0;
		for (int i = 0; i < finalGrade.size(); i++) {
			double grade = finalGrade.get(i);
			if (grade >= 90) grade = 9;
			else if (grade>=80) grade = 8;
			else if (grade>=75) grade = 7;
			else if (grade>=70) grade = 6;
			else if (grade>=65) grade = 5;
			else if (grade>=60) grade = 4;
			else if (grade>=55) grade = 3;
			else if (grade>=50) grade = 2;
			else if (grade>=47) grade = 1;
			else  grade = 0;
			totalGrade += grade * courseTaken.get(i).credit;
			totalCredit += courseTaken.get(i).credit;
		}
		return Math.round(((totalGrade/totalCredit)*10))/10.0;
	}
	public void setStudentID(String id) {
		this.studentID = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public ArrayList<CourseSol> getCourseTaken() {
		ArrayList<CourseSol> course = new ArrayList<CourseSol>();
		for (int i = 0; i < courseTaken.size(); i++)
			course.add(new CourseSol(courseTaken.get(i)));
		return course;
	}
	public ArrayList<Double> getFinalGrade() {
		ArrayList<Double> grade = new ArrayList<Double>();
		for (int i = 0; i < finalGrade.size(); i++)
			grade.add(finalGrade.get(i));
		return grade;
	}
	public String getStudentId() {
		return this.studentID;
	}
	public void setCourse(ArrayList<CourseSol> course) {
		this.courseTaken = new ArrayList<CourseSol>();
		for (CourseSol item: course) {
			courseTaken.add(new CourseSol(item));
		}
		
	}
	public void addCourse(CourseSol course) {
		this.courseTaken.add(course);
	}
}
class CourseSol{
	private String code;
	private ArrayList <AssessmentSol> assignment;
	double credit;
	public CourseSol () {
		code = "";
		assignment = new ArrayList<AssessmentSol>();
		credit = 0;	
	}
	public CourseSol (String code, ArrayList <AssessmentSol> assignment, double creditWorth) {
		this.code = code;
		this.assignment = new ArrayList<AssessmentSol>();
		this.assignment = assignment;
		this.credit = creditWorth;	
	}
	public CourseSol( CourseSol course) {
		this.code = course.code;
		this.assignment = new ArrayList<AssessmentSol>();
		for (int i = 0; i < course.assignment.size(); i++)
			this.assignment.add(course.assignment.get(i));
		this.credit = course.credit;	
	}
	public double getCredit() {
		return this.credit;
	}
	public ArrayList <AssessmentSol> getAssignment(){
		return this.assignment;
	}

	public String getCode() {
		return this.code;
	}

	public boolean equals(Object object) {
		CourseSol obj = (CourseSol) object;
		boolean equal = (obj != null && this.assignment.size() == obj.assignment.size() && 
				         this.code == obj.code && this.credit == obj.credit);
		if (equal)
			for (int i = 0; i < obj.assignment.size(); i++) 
				if (! this.assignment.get(i).equals(obj.assignment.get(i))) {
					equal = false; 
					break;
				}
		return equal;
		
	}
}
class AssessmentSol{
	private char type; 
	private int weight;
	private AssessmentSol() {
		type = ' ';
		weight = 0;
	}
	private AssessmentSol(char type,  int weight) {
		this.type = type;
		this.weight = weight;
	}
	public static AssessmentSol getInstance(char type, int weight) {
		return new AssessmentSol(type, weight);
	}
	public int getType() {
		return this.type;
	}
	public int getWeight() {
		return this.weight;
	}
	public boolean equals(Object obj) {
		AssessmentSol object = (AssessmentSol) obj;	
		return (object.type == this.type && object.weight == this.weight);
	}
}

class InvalidTotalExceptionSol extends Exception{
	public InvalidTotalExceptionSol() {
		super();
	}
	public InvalidTotalExceptionSol(String message) {
		super(message);
	}
}
