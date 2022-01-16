package Assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/*
Full Name: Patrick Jason Li
*/

/**
* This class generates a transcript for each student, whose information is in the text file.
* 
*
*/
public class Transcript extends Student{
	
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;
	
	/**
	 * This the the constructor for Transcript class that 
	 * initializes its instance variables and call readFie private
	 * method to read the file and construct this grade.
	 * @param inFile is the name of the input file.
	 * @param outFile is the name of the output file.
	 * @throws InvalidTotalException 
	 */
	public Transcript(String inFile, String outFile) {
		inputFile = new File(inFile);	
		this.outputFile = outFile;
		grade = new ArrayList<Object>();
		this.readFile();
	}// end of Transcript constructor

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
	 * Converts the ArrayList<Object> to a List of List<Strings>. 
	 * This is to make it easier to manipulate the data.
	 * @param grade - An ArrayList of Objects containing information about students.
	 * @return - Returns a List of List<Strings> containing information about students.
	 */
	private List<List<String>> convertListtoString(ArrayList<Object> grade){
		ArrayList<String> gradeString = new ArrayList<String>();
		
		//Converts the Object ArrayList to a String ArrayList
		for(Object obj: grade) {
			gradeString.add(obj.toString());
		}
		
		// An List which stores List<String>
		List<List<String>> stringList = new ArrayList<>();
		
		//Converts each object into a String split by commas and stores them into a List<String>
		for(Object obj: gradeString) {
			List<String> string = (Arrays.asList(((String) obj).split(",")));
			stringList.add(string);
		}
		return stringList;
	}

	/**
	 * Builds an ArrayList of Student Objects with each Object containing the information of the student.
	 * @return - An ArrayList of Student Objects.
	 * @throws InvalidTotalException - Throws an Exception if the total weight within a Course Object is above or below 100.
	 */
	public ArrayList<Student> buildStudentArray() throws InvalidTotalException{
		ArrayList<Student> students = new ArrayList<Student>();
		List<List<String>> gradesList = convertListtoString(grade); //Converts the object to a List of List<Strings>,
		students = buildStudentObjects(grade, gradesList, students); //Builds an ArrayList of Student Objects.
		for(int index = 0; index < students.size(); index++) {
			Student student = students.get(index);
			calGrades(student, gradesList);
		}
		return students;
	} //end of buildStudentArray

	/**
	 * Builds Student Objects depending on how many students are in the inputFile.
	 * @param grade - An ArrayList containing the grades of Students.
	 * @return - Returns an ArrayList of Student Objects.
	 */
	private ArrayList<Student> buildStudentObjects(ArrayList<Object> grade, List<List<String>> gradesList, ArrayList<Student> students){
	
		//Initializes an ArrayList which keeps tracks of which student has already an object created for them.
		ArrayList<String> namesOfStudent = new ArrayList<String>();
		
		/*
		 * Loops through the gradesList, and creates a Student Object for each student in the gradesList.
		 * The first loop is used to acquire the name and ID of the student.
		 */
		for(int i = 0; i < gradesList.size(); i++) {
			int lastElement = gradesList.get(i).size() - 1; //Acquires the last String in each List.
			String studentName = gradesList.get(i).get(lastElement); //Acquires the name of the student
			String studentID = gradesList.get(i).get(2); //Acquires the ID of the student.
				
			//Initializes an ArrayList containing Course Objects
			ArrayList<Course> coursesTaken = new ArrayList<Course>();
				
			/*
			 * If the student already has a object created for them or if one of the objects is null, it 
			 * doesn't create a student Object. 
			 * Otherwise, it creates a Student Object and adds it to the ArrayList students, and then it 
			 * adds the name of the Student to the ArrayList namesofStudent to keep track that that student 
			 * already has a Student object.
			 */
			if(namesOfStudent.contains(studentName.replaceAll("\\s+","")) == false && grade.get(i) != null) {
				coursesTaken = buildCoursesObjects(gradesList, studentName, studentID);
				Student student = new Student(studentID, studentName, coursesTaken);
				students.add(student);
				namesOfStudent.add(studentName);
			}
		}
		return students;
	}

	/**
	 * Builds Course Objects depending on how many courses a Student Object has.
	 * @param gradesList - Containing a List of students.
	 * @param studentName - The name of A Student.
	 * @param studentID - The ID of a Student.
	 * @return - Returns a Course Object containing information about a course that a student has taken.
	 */
	private ArrayList<Course> buildCoursesObjects(List<List<String>> gradesList, String studentName, String studentID){
		
		//Initializes an ArrayList containing Course objects.
		ArrayList<Course> coursesTaken = new ArrayList<Course>();
		
		//Acquires the number of Lists in gradesList
		int sizeOfList = gradesList.size();
		
		//Initializes variables to contain the code of the course and credit of the course.
		String code = "";
		double credit = 0.0;
		 
		//Initializes an ArrayList to contain Assessment Objects.
		ArrayList<Assessment> assessments = new ArrayList<Assessment>();
		
		//Initializes an ArrayList to contain Courses of one student.
		ArrayList<List<String>> coursesOfStudent = new ArrayList<List<String>>();
		
		// Creates an ArrayList with courses specific to each student
		for(int i = 0; i < sizeOfList; i++) {
			int lastElement = gradesList.get(i).size() - 1;
			String thisStudentName = gradesList.get(i).get(lastElement);
			String thisStudentID = gradesList.get(i).get(2);
			
			if(thisStudentName.equals(studentName) && thisStudentID.equals(studentID)) {
				List<String> aCourse = gradesList.get(i).subList(0, lastElement);
				coursesOfStudent.add(aCourse);
			}	
		}
		
		/*
		 * Builds the Course Object which is then added the to ArrayList coursesTaken.
		 */
		for(int j = 0; j < coursesOfStudent.size(); j++) {
			int lastElement = coursesOfStudent.get(j).size();
			
			code = coursesOfStudent.get(j).get(0);
			credit = Double.parseDouble(coursesOfStudent.get(j).get(1));
			List<String> assessmentList = coursesOfStudent.get(j).subList(3, lastElement);
			
			assessments = buildAssessmentObjects(assessmentList);
			Course course = new Course(code, assessments, credit);
			coursesTaken.add(course);
			}
		
		return coursesTaken;
	}

	/**
	 * Builds Assessments Objects depending on how many assessments there are in a specific Course.
	 * @param assessmentList - A list containing assessments (type and weight). 
	 * @return - Returns an ArrayList of Assessments Objects. 
	 */
	private ArrayList<Assessment> buildAssessmentObjects(List<String> assessmentList) {
		
		//Initializes an ArrayList containing Assessment Objects
		ArrayList<Assessment> assessmentsOfCourses = new ArrayList<Assessment>();
		
		//Initializes variables to contain values for type and weight of an assessment
		char type = 0;		
		int weight = 0;
		
		/*
		 * Loops through the assessmentList, finds the char and weight within the list,  
		 * and creates an Assessment Object.
		 */
		for(int i = 0; i < assessmentList.size(); i++) {
			
			type = assessmentList.get(i).charAt(0);	//Acquires the type of the assessment.
			
			char weightFirst = assessmentList.get(i).charAt(1); //Acquires the tens digit of the weight of the assessment.
			char weightSecond = assessmentList.get(i).charAt(2); //Acquires the singles digit of the weight of the assessment.
			String weightCombine = "" + weightFirst + weightSecond; //Combines the weight in a single string.
			weight = Integer.parseInt(weightCombine); //Converts the weight from string to integer.
			
			Assessment assessments = Assessment.getInstance(type, weight); //Creates a Assessment Object.
			
			assessmentsOfCourses.add(assessments); //Adds the Assessment Object to the ArrayList asssmentsofCourses.
		}
		return assessmentsOfCourses;
	}

	/**
	 * For each student in Students ArrayList, it will calculate the final Grades of each course that the student has taken
	 * @param student - A Student Object containing information about a specific student.
	 * @param gradesList - A list of Lists containing all the grades of the student.
	 * @throws InvalidTotalException - Throws an Exception if the total weight within a Course Object is above or below 100.
	 */
	private void calGrades(Student student, List<List<String>> gradesList) throws InvalidTotalException {
		
		//An ArrayList which holds the all the information about the assessments of each course.
		ArrayList<List<String>> informationOnAssessments = InformationPerStudent(student, gradesList);

		//An ArrayList which holds the Grades of each course.
		ArrayList<ArrayList<Double>> studentGrades = gradesPerStudent(informationOnAssessments);
		
		//An ArrayList which holds the weights of each assignment in each course.
		ArrayList<ArrayList<Integer>> weightsList = createWeightLists(student);
		
		/*
		 * Loops through the studentGrades and weightsLists and calls the 
		 * addGrade() method to acquire the final grade for each course that a student has.
		 */
		for(int index = 0; index < studentGrades.size(); index++) {
			student.addGrade(studentGrades.get(index), weightsList.get(index));
		}
	}
	
	/**
	 * For loop through gradesList, matches StudentName, StudentID, and CourseName/Credit with Student Object.
	 * Creates an ArrayList containing the grades and Information of each assessment for each course in Student Object
	 * @param student - A Student Object which contains information about a specific student.
	 * @param gradesList - A List containing list of grades of all students.
	 * @return - Returns a list of grades for a specific student.
	 */
	private ArrayList<List<String>> InformationPerStudent(Student student, List<List<String>> gradesList){
		
		//Initializes an ArrayList that contains List of Strings which each list contains an information about the asssignments per course.
		ArrayList<List<String>> assessmentsGrades = new ArrayList<List<String>>();
		
		/*
		 * Loops through gradesList, and acquires the information on the assignments for each of the students courses.
		 */
		for(int index = 0; index < gradesList.size(); index++) {
			
			int lastElement = gradesList.get(index).size() - 1; //Acquires the last String in each List.
			String gradesListStudentName = gradesList.get(index).get(lastElement); //Acquires the name of the student.
			String gradesListStudentID = gradesList.get(index).get(2); //Acquires the ID of the student.
			String gradesListCourseName = gradesList.get(index).get(0); //Acquires the Course Name.
			Double gradesListCourseCredit = Double.parseDouble((gradesList.get(index).get(1))); //Acquires the Course Credit.
				
			/*
			 * If the list of students contains a name and id equivalent to the Student Object's name and ID,
			 * It acquires assignment information for each of the student's courses.
			 */
			if(gradesListStudentName.equals(student.getName()) && gradesListStudentID.equals(student.getStudentID())) {
					
				for(int indexCourse = 0; indexCourse < student.getCourseTaken().size(); indexCourse++) {
					Course course = student.getCourseTaken().get(indexCourse);
						
					if(gradesListCourseName.equals(course.getCode()) &&
							gradesListCourseCredit.equals(course.getCredit())) {
						List<String> assessmentGrade = gradesList.get(index).subList(3, lastElement);
						assessmentsGrades.add(assessmentGrade);
					}
				}
			}
		}
		return assessmentsGrades;
	}
	
	/**
	 * Loops through an ArrayList of List of Assessments (grades included), 
	 * acquires the grades for those Assessments and adds them to the an ArrayList containing ArrayLists of student grades.
	 * @param assessmentsGrades - An ArrayList containing information on the grades of each assessment.
	 * @return - Returns an ArrayList containing ArrayLists of student grades.
	 */
	private ArrayList<ArrayList<Double>> gradesPerStudent(ArrayList<List<String>> assessmentsGrades) {
		
		//Initializes an ArrayList containing ARrayLists of student Grades
		ArrayList<ArrayList<Double>> studentGrades = new ArrayList<ArrayList<Double>>();
		
		/*
		 * Loops through the assessmentsGrades and acquires the grades that each student got for 
		 * each assessments, and then adds each assessment grade to an ArrayList. Then that ArrayList
		 * is added to another ArrayList.
		 */
		for(int assessmentIndex = 0; assessmentIndex < assessmentsGrades.size(); assessmentIndex++) {
			ArrayList<Double> gradeList = new ArrayList<Double>();
				for(int assessment = 0; assessment < assessmentsGrades.get(assessmentIndex).size(); assessment++) {
					char firstPGrade = assessmentsGrades.get(assessmentIndex).get(assessment).charAt(4);
					char secondPGrade = assessmentsGrades.get(assessmentIndex).get(assessment).charAt(5);
					String gradeString = "" + firstPGrade + secondPGrade;
					Double gradeDouble = Double.parseDouble(gradeString);
					gradeList.add(gradeDouble);
			}
			studentGrades.add(gradeList); //Adds gradeList to an ArrayList called studentGrades.
		}
		return studentGrades;
	}
	
	/**
	 * Creates an ArrayList of Integer ArrayLists, with each ArrayList containing the weights of a specific course.
	 * @param student - A Student Object containing information about a specific student.
	 * @return - Returns an ArrayList of Integer ArrayLists.
	 */
	private ArrayList<ArrayList<Integer>> createWeightLists(Student student) {
		
		//Initializes an ArrayList containing ArrayLists of weights of a course
		ArrayList<ArrayList<Integer>> weightLists = new ArrayList<ArrayList<Integer>>();
		
		//Acquires the number of Courses a student has.
		int numOfCourses = student.getCourseTaken().size();
		
		/*
		 * Loops through each student's courses and acquires the weight of each assignment in the course.
		 * Then adds it to an ArrayList called weightsOfCourse. 
		 * Then that ArrayList is added to an ArrayList called weightLists.
		 */
		for(int indexCourse = 0; indexCourse < numOfCourses; indexCourse++) {
			ArrayList<Integer> weightsOfCourse = new ArrayList<Integer>();
			for(int indexAssessments = 0; indexAssessments < student.getCourseTaken().get(indexCourse).getAssignment().size(); 				indexAssessments++){
				int weight = student.getCourseTaken().get(indexCourse).getAssignment().get(indexAssessments).getWeight();
				weightsOfCourse.add(weight);
			}
			weightLists.add(weightsOfCourse);
		}
		return weightLists;
	}

	/**
	 * Prints the transcript to the given file (i.e. outputFile attribute)
	 * @param students - An ArrayList containing the grades of Students.
	 * @throws IOException - Throws an exception if a it fails to write the string.
	 */
	public void printTranscript(ArrayList<Student> students) throws IOException {
		//Common Strings to be used in the Transcript
		String studentTranscript = "";
		String dashes = "--------------------";
		
		// Instance Object of BufferredWriter to be more efficient in writing to the file
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		/*
		 * Loops through ArrayList students to acquire a Student's name, ID, course code, course credit,
		 * the final grade for each course, and the gpa of each student.
		 * At the same time, it formats it and outputs it to an output file.
		 */
		for(int student = 0; student < students.size(); student++) {
			//Writes the Student Name, tabs it, StudentID, sets a new line, writes 20 dashes, and sets a new line.
			studentTranscript = students.get(student).getName() + "\t" + students.get(student).getStudentID() + "\n" + dashes + "\n"; 
			
			/*
			 * For loops through ArrayList Students, displays each courses code and the final grades the student got
			 * for each course, then sets a new line.
			 */
			for(int index = 0; index < students.get(student).getCourseTaken().size(); index++) {
				studentTranscript += students.get(student).getCourseTaken().get(index).getCode() 
						+ "\t" + students.get(student).getFinalGrades().get(index) + "\n";
			}
			
			//Writes 20 dashes and sets a new line.
			studentTranscript += dashes + "\n";
			
			/*
			 * Depending on if the the student is the last student or not, it writes a different 
			 * line to the output file.
			 * If the student is the last student in the ArrayList, it doesn't set newlines to the output file.
			 */
			if(student < students.size() - 1) {
				studentTranscript += "GPA\t" + students.get(student).weightedGPA() + "\n"; 
				//Writes to the file the student transcript
			    writer.write(studentTranscript + "\n");
			}
			else {
				studentTranscript += "GPA\t" + students.get(student).weightedGPA(); 
				//Writes to the file the student transcript
			    writer.write(studentTranscript);
			}
		}
		writer.flush(); //Flushes the stream, makes the writing execution as fast as possible.
		writer.close(); //Closes the file.
	}
	
	/**
	 * Acquires the Transcript's ArrayList Grade.
	 * @return - Returns Transcript's ArrayList Grade.
	 */
	public ArrayList<Object> getGrade() {
		return grade;
	}

	/**
	 * Sets the Transcript's ArrayList Grade to a new ArrayList containing the grades of the students.
	 * @param grade - An ArrayList containing the grades of the students.
	 */
	public void setGrade(ArrayList<Object> grade) {
		this.grade = grade;
	}
} // end of Transcript

class Course {
	private String code;
	private ArrayList<Assessment> assignment;
	private double credit;
	
	/**
	 * Default Constructor for Course Objects, sets default values for code, assignment and credit.
	 */
	public Course() {
		code = "";
		assignment = new ArrayList<Assessment>();
		credit = 0.0;
	} // end Course()
	
	/**
	 * Customized Constructor for Course Objects
	 * @param code - A String variable containing the code of the Course.
	 * @param assignment - An ArrayList containing information about the Assessments of the Course.
	 * @param credit - A double variable containing the Credit of the Course.
	 */
	public Course(String code, ArrayList<Assessment> assignment, double credit) {
		this.code = code;
		this.credit = credit;
		//deep copies ArrayList assignment
		ArrayList<Assessment> copyAssignment = new ArrayList<Assessment>();		
		for(int i = 0; i < assignment.size(); i++) {
			copyAssignment.add(assignment.get(i));
		}
		this.assignment = copyAssignment;
	} // end of Course(String, ArrayList, double)
	
	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Sets the Course's code to a new String.
	 * @param code - A string containing a new value for the Course's code.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Acquires the ArrayList containing the Assessments of courses.
	 * @return - Returns the Courses's assignment.
	 */
	public ArrayList<Assessment> getAssignment() {
		return this.assignment;
	}

	/**
	 * Sets an Course's assignment a new to ArrayList containing assessments.
	 * @param assignment - An ArrayList containing the information of assignments.
	 */
	public void setAssignment(ArrayList<Assessment> assignment) {
		this.assignment = assignment;
	}

	/**
	 * Acquires the credit of the course.
	 * @return Returns the credit of the course.
	 */
	public double getCredit() {
		return this.credit;
	}

	/**
	 * Sets the credit value of the Course to a new credit value.
	 * @param credit - A double variable containing the credit of the course
	 */
	public void setCredit(double credit) {
		this.credit = credit;
	}

	/**
	 * An overridden method for Object’s equals() method that returns true, 
	 * if all the instance variables of two objects (Courses) have the same value.
	 * @param o - An Object that is being compared to the current object.
	 * @return True or false depending on if the two objects compared are equivalent or not.
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Course obj = (Course) o;
		return this.getCode().equals(obj.getCode()) && this.getCredit() == obj.getCredit() 
				&& this.getAssignment().equals(obj.getAssignment());
	} // end of equals(Course)
	
} // end of Course

class Student{

	private String studentID;
	private String name;
	private ArrayList<Course> courseTaken;
	private ArrayList<Double> finalGrades = new ArrayList<Double>();
	
	/**
	 * Default Student Constructor with default values.
	 */
	public Student() {
		studentID = "";
		name = "";
		courseTaken = new ArrayList<Course>();
		finalGrades = new ArrayList<Double>();
	} // end of Student()
	
	/**
	 * Customized Student Constructor which creates an object with a specific StudentID, name, courseTaken value(s).
	 * @param studentID
	 * @param name
	 * @param course
	 */
	public Student(String studentID, String name, ArrayList<Course> course) {
		this.studentID = studentID;
		this.name = name;
		addCourse(course);
	} // end of Student(String, String, ArrayList<Course>

	/**
	 * Gets an ArrayList of the grades and their weights, 
	 * computes the true value of the grade based on its weight and add it to finalGrade attribute. 
	 * In case the sum of the weight was not 100, or the sum of grade was greater 100, 
	 * it throws InvalidTotalException, which is an exception that is defined by the user.
	 * @param gradesList - An ArrayList containing the grades of a course.
	 * @param weightsList - An ArrayList containing the weights of each assessments.
	 * @throws InvalidTotalException - An user-defined exception which allows the user to customize the exception message
	 */
	public void addGrade(ArrayList<Double> gradesList, ArrayList<Integer> weightsList) throws InvalidTotalException {
		
		int gradesListSize = gradesList.size(); //Acquires the size of ArrayList gradesList.
		
		//Initializes variables to store the finalGrade and totalWeight of each course
		double finalGrade = 0.0; 
		double totalWeight = 0.0;
		
		/*
		 * Loops through weightLists and gradesList, 
		 * Gets the total weight of a course and the finalGrade of the course.
		 */
		for(int index = 0; index < gradesListSize; index++) {
			if(gradesList.get(index) >= 0) {
				totalWeight += weightsList.get(index); //Keeps track of the total weight
				double convertedWeight = weightsList.get(index) / 100.0;
				double weightedGrade = (gradesList.get(index) * convertedWeight);
				finalGrade += weightedGrade;
			}
			else {
				finalGrade = 0;
			}
		}
		
		finalGrade = roundDecimal(finalGrade, 1); //Rounds the final grade to 1 decimal point.
		
		//If the total weight is below or above 100, it throws an exception.
		if(totalWeight < 100) throw new InvalidTotalException("The Assessments weights are below 100, which should not be possible.");
		if(totalWeight > 100) throw new InvalidTotalException("The Assessments weights are above 100, which should not be possible.");
		if(finalGrade > 100) throw new InvalidTotalException("The Final Grade of this course is above 100, which should not be possible.");
		else finalGrades.add(finalGrade); //adds the final grade to ArrayList finalGrades.

	} // end of addGrade(ArrayList<Double>, ArrayList<Integer>)
	
	/**
	 * Computes the GPA.
	 * @return Returns the GPA of the student.
	 */
	public Double weightedGPA() {
		ArrayList<Integer> gradePointList = convertGradeToGP(finalGrades); //Holds the grade points for courses the student has taken
		int totalPoint = 0; // total points (Points = Grade Point x Credit)
		int totalCredit = 0; // total number of credits
		
		/*
		 * Loops through the gradePointList and courseTaken list, adds all the credits to TotalCredit, 
		 * and calculate the points and adds to totalPoint.
		 */
		for(int index = 0; index < gradePointList.size(); index++) {
			totalCredit += (int) courseTaken.get(index).getCredit();
			totalPoint += (gradePointList.get(index) * (int) courseTaken.get(index).getCredit());
		}
		
		double gpa = ((double) totalPoint) / ((double) totalCredit); //Calculates the gpa of the student
		
		gpa = roundDecimal(gpa, 1); //Rounds the GPA to 1 decimal point
		
		return gpa;
	} // end of weightGPA()
	
	/**
	 * Rounds a double value to 1 decimal point.
	 * @param num - An double variable which contains a value
	 * @param decimalPoint - A integer variable which specifics what decimal point the user wants to round to.
	 * @return Returns the value rounded to 1 decimal point.
	 */
	private static double roundDecimal(double num, int decimalPoint) {
	    int scaling = (int) Math.pow(10, decimalPoint);
	    num = (double) Math.round(num * scaling) / scaling;
	    return num;
	}
	
	/**
	 * Converts the final grades of each course to their equivalent GradePoint
	 * @param finalGrades - An ArrayList containing the final grades of each course
	 * @return Returns An ArrayList containing the GradePoint of each course
	 */
	private ArrayList<Integer> convertGradeToGP(ArrayList<Double> finalGrades){
		
		//An ArrayList which is used to store gradePoints of a course
		ArrayList<Integer> gradePointList = new ArrayList<Integer>();
		
		//Integer variable to store a gradePoint value.
		int gradePoint = 0;
		
		/*
		 * Loops through finalGrades ArrayList.
		 * Depending on the actual grade, the gradePoint variable gets assigned a certain value that is 
		 * equivalent to the grade as assigned by York University. 
		 */
		for(int index = 0; index < finalGrades.size(); index++) {
			double grade = finalGrades.get(index); //Acquires the final grades of each course of the student
			 if(grade >= 90 && grade <= 100) {
				 gradePoint = 9;
			 }
			 else if(grade >= 80 && grade <= 89.99) {
				 gradePoint = 8;
			 }
			 else if(grade >= 75 && grade <= 79.99) {
				 gradePoint = 7;
			 }
			 else if(grade >= 70 && grade <= 75.99) {
				 gradePoint = 6;
			 }
			 else if(grade >= 65 && grade <= 69.99) {
				 gradePoint = 5;
			 }
			 else if(grade >= 60 && grade <= 64.99) {
				 gradePoint = 4;
			 }
			 else if(grade >= 55 && grade <= 59.99) {
				 gradePoint = 3;
			 }
			 else if(grade >= 50 && grade <= 55.99) {
				 gradePoint = 2;
			 }
			 else if(grade >= 47 && grade <= 49.99) {
				 gradePoint = 1;
			 }
			 else {
				 gradePoint = 0;
			 }
			 gradePointList.add(gradePoint); //Adds the gradePoint to the gradePoint ArrayList.
		}
		return gradePointList;
	}
	
	/**
	 * Get a Course Object as an input and add it to courseTaken.
	 * @param newCourse - An ArrayList containing Course objects, or in other words containing courses.
	 */
	public void addCourse(ArrayList<Course> newCourse) {
		this.courseTaken = new ArrayList<Course>();
		for(int i = 0; i < newCourse.size(); i++) {
			this.courseTaken.add(newCourse.get(i));
		}
	} // end of addCourse(ArrayList<Course>) 
	
	/**
	 * Acquires the StudentId of the current Student.
	 * @return Returns the studentID.
	 */
	public String getStudentID() {
		return this.studentID;
	}

	/**
	 * Sets the StudentID of the current Student.
	 * @param studentID
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * Acquires the name of the Student.
	 * @return Returns the name of the Student.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of the Student.
	 * @param name - A string containing a name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Acquires the arrayList CourseTaken.
	 * @return Returns the ArrayList CourseTaken.
	 */
	public ArrayList<Course> getCourseTaken() {
		return this.courseTaken;
	}

	/**
	 * Allows the user to set the CourseTaken for the current Student.
	 * @param courseTaken - An ArrayList containing the courses taken by the student.
	 */
	public void setCourseTaken(ArrayList<Course> courseTaken) {
		this.courseTaken = courseTaken;
	}

	/**
	 * Acquires the FinalGrades.
	 * @return Returns the finalGrades ArrayList.
	 */
	public ArrayList<Double> getFinalGrades() {
		return this.finalGrades;
	}

	/**
	 * Sets the finalGrades of the Student.
	 * @param finalGrades - An ArrayList containing the finalGrades of each Course.
	 */
	public void setFinalGrades(ArrayList<Double> finalGrades) {
		this.finalGrades = finalGrades;
	}
	
} //end of Student

class Assessment {
	private char type;
	private int weight;
	
	/**
	 * The Default constructor for Assessment, type and weight are set with default values.
	 */
	private Assessment() {
		type = ' ';
		weight = 0;
	} //end assessment() 
	
	/**
	 * A Custom Constructor which allows the user to customize the type and weight of each assessment.
	 * @param type - char value which contains the type of the assessment.
	 * @param weight
	 */
	private Assessment(char type, int weight) {
		this.type = type;
		this.weight = weight;
	} // end of Assessment(char, int)
	
	/**
	 * A static factory method which creates instance of the class Assessment.
	 * @return - Returns a instance Assessment Object.
	 */
	public static Assessment getInstance(char type, int weight) {
		return new Assessment(type, weight);
	} // end of getInstance()
	
	/**
	 * An overridden method for Object’s equals() method that returns true, 
	 * if all the instance variables of two objects (Courses) have the same value.
	 * @param o - An Object that is being compared to the current object.
	 * @return - True or false depending on if the two objects compared are equivalent or not.
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Assessment obj = (Assessment) o;
		return this.type == obj.getType() && this.weight == obj.getWeight();
	} // end of equals(Object)
	
	/**
	 * Acquires the type of the assessment.
	 * @return - Returns the type of the assessment.
	 */
	public char getType() {
		return this.type;
	}

	/**
	 * Sets the type of the assessment.
	 * @param type - A character which represents the type of assessment.
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * Gets the weight of an assessment.
	 * @return - Returns the weight of an assessment.
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Sets the weight of assessment.
	 * @param weight - An integer which represents the weight of an assessment
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
} //end of Assessment

/**
 * Exception that is only thrown when the weight of the Course is below 100 or above 100.
 * @author patli
 */
class InvalidTotalException extends Exception{
	
	private static final long serialVersionUID = 5625819505074915789L;

	/**
	 * Default Constructor for InvalidTotalException
	 */
	public InvalidTotalException (){
		super();
	}
	
	/**
	 * Custom Constructor which allows the user to input any message they want displayed when a 
	 * certain error occurs.
	 * @param message - A String in which the user can store a message that they want to be displayed when a error occurs.
	 */
	public InvalidTotalException(String message){
		super(message);
	}
} //end of InvalidTotalException
