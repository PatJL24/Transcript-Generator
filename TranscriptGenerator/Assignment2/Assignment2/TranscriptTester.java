/**
 * 
 */
package Assignment2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;

/**
 * @author EECS2030 authors
 *
 */
/*
*-- 25% on the correctness of buildStudentArry()
--30% on the correct implementation of class Student.
5%: weightedGPA()
5% : addGrade()
5%: addCours()
5%: encapsulation
5%: correctness of composition
5%: exception handling
--15% on the correct implementation of class Course.
5%: equals() 5%: encapsulation
5%: correctness of composition
--10% on the correct implementation of class Assessment.
5%: encapsulation
5%: getInstance()
--20% on documentation 
 */
class TranscriptTester {
	final int RUBRIC = 12;
	TranscriptSol expected_tr = new TranscriptSol("input.txt", "output.txt");
	Transcript actual_tr = new Transcript("input.txt", "output.txt");
	TranscriptSol expected_wrong_tr = new TranscriptSol("wrongInput.txt", "wrongOutput.txt"); // Note to myself: make wrongInput wrong!
	TranscriptSol actual_wrong_tr = new TranscriptSol("wrongInput.txt", "wrongOutput.txt"); 
	int [] grade = new int[RUBRIC];

	//___________________ test buildArray 25 points ___________________//
	// 5 points
	
	@Test
	void buildStudentArray1() throws InvalidTotalException {
		ArrayList<StudentSol> expected = expected_tr.buildStudentArray();
		ArrayList<Student> actual = actual_tr.buildStudentArray();

		boolean equal = studentEqual(actual, expected);
		
		assertTrue(equal, "buildStudentArray 1 is not correct.");
		// check for deep copy of courseTaken
	}
	private static boolean studentEqual(ArrayList<Student> actual, ArrayList<StudentSol> expected) {
		boolean equal = true; 
		
		if (actual.size() == expected.size()) {
			for (int i = 0; i < expected.size(); i++) {
				// find the element in st, whose student id is the same as st[i]
				int j = findStudent(expected.get(i).getStudentId(), actual);
				// student were not found
				// Note to myself: all the expected use i as its index, all the actuals use j as its index.
				if (j == -1) {
					equal = false;
					break;
				}
				else { // student was found, check for the equality of other components.
					// are the names the same?
					equal = equal && (expected.get(i).getName().compareTo(actual.get(j).getName()) == 0); 
					// are the courses the same? Here I only check the size, If the courses are not correct, it will show itself in other testers. 
					equal = equal && expected.get(i).getCourseTaken().size() == actual.get(j).getCourseTaken().size();
					// Are the grades the same?
					//Note to myself: if getFinalGrade or getCourseTaken has not been implemented, encapsulation grade should be deducted. 
					ArrayList<Double> expectedGrade = expected.get(i).getFinalGrade();
					ArrayList<Double> actualGrade = actual.get(j).getFinalGrades();
					equal = equal && (expectedGrade.size() == actualGrade.size()) && expectedGrade.containsAll(actualGrade);
					if (!equal)
						break;
				}	
			}
		}
		else equal = false; // The arrays are not equal in size
		return equal;
	}
	// 5 points
	@Test
	void buildStudentArray2() throws InvalidTotalException {
		ArrayList<StudentSol> expected = expected_tr.buildStudentArray();
		ArrayList<Student> actual = actual_tr.buildStudentArray();

		boolean equal = studentEqual(actual, expected);
		
		assertTrue(equal, "buildStudentArray 1 is not correct.");
		// check for deep copy of courseTaken
	}
	// 5 points
	@Test
	void buildStudentArray3() throws InvalidTotalException {
		ArrayList<StudentSol> expected = expected_tr.buildStudentArray();
		ArrayList<Student> actual = actual_tr.buildStudentArray();

		boolean equal = studentEqual(actual, expected);
		
		assertTrue(equal, "buildStudentArray 1 is not correct.");
		// check for deep copy of courseTaken
	}
	// 5 points
	@Test
	void buildStudentArray4() throws InvalidTotalException {
		ArrayList<StudentSol> expected = expected_tr.buildStudentArray();
		ArrayList<Student> actual = actual_tr.buildStudentArray();

		boolean equal = studentEqual(actual, expected);
		
		assertTrue(equal, "buildStudentArray 1 is not correct.");
		// check for deep copy of courseTaken
	}
	// 5 points
	@Test
	void buildStudentArray5() throws InvalidTotalException {
		ArrayList<StudentSol> expected = expected_tr.buildStudentArray();
		ArrayList<Student> actual = actual_tr.buildStudentArray();

		boolean equal = studentEqual(actual, expected);
		
		assertTrue(equal, "buildStudentArray 1 is not correct.");
		// check for deep copy of courseTaken
	}

	private static int findStudent(String stID, ArrayList<Student> actual) {
		for (int i = 0; i < actual.size(); i++) {
			if (actual.get(i).getStudentID().compareTo(stID) == 0)
				return i; 
		}
		return -1;
	}
	//__________________ Test Student  ___________________//
	// WeightedGPA -- 3 points
	@Test
	public void testweightedGPA1() throws InvalidTotalException {
		TranscriptSol expected = new TranscriptSol("myInput1.txt", "outputTranscript.txt");
		StudentSol stExpected = expected.buildStudentArray().get(0);

		Transcript actual = new Transcript("myInput1.txt", "outputTranscript.txt");
		Student stActual = actual.buildStudentArray().get(0);

		assertEquals(stExpected.weightedGPA(), stActual.weightedGPA(), 0.1 , "weighted GPA is not correct");
	}
	// WeightedGPA -- 2 points
	@Test
	public void testweightedGPA2() throws InvalidTotalException {
		TranscriptSol expected = new TranscriptSol("myInput2.txt", "outputTranscript.txt");
		StudentSol stExpected = expected.buildStudentArray().get(1);

		Transcript actual = new Transcript("myInput2.txt", "outputTranscript.txt");
		Student stActual = actual.buildStudentArray().get(1);

		assertEquals(stExpected.weightedGPA(), stActual.weightedGPA(), 0.1 , "weighted GPA is not correct");
	}
	// 2 points
	@Test
	public void testaddGrade1() throws InvalidTotalException {
		ArrayList<Double> grade = new ArrayList<Double>();
		ArrayList<Integer> weight = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			grade.add(Double.valueOf(2*i+ 50));
			weight.add(10);
		}
		double finalGrade =  0;
		for (int i = 0; i < grade.size(); i++) {
			finalGrade += grade.get(i) *(weight.get(i)/100.0);
		}
		finalGrade = Math.round(finalGrade*10)/10.0;

		Student st_actual = new Student();
		st_actual.addGrade(grade, weight);
		//System.out.println(grade);
		//System.out.println(st_actual.getFinalGrade());
		
		assertEquals(finalGrade, st_actual.getFinalGrades().get(0), 0.1 , "addGrade failure: either the computation is not correct or other dependent required method is not correct/ implemented.");
	}
	// 2 points
	@Test
	public void testaddGrade2() throws InvalidTotalException {
		ArrayList<Double> grade = new ArrayList<Double>();
		ArrayList<Integer> weight = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			grade.add(Double.valueOf(3*i+ 60));
			weight.add(20);
		}
		double finalGrade =  0;
		for (int i = 0; i < grade.size(); i++) {
			finalGrade += grade.get(i) *(weight.get(i)/100.0);
		}
		finalGrade = Math.round(finalGrade*10)/10.0;

		Student st_actual = new Student();
		st_actual.addGrade(grade, weight);
		//System.out.println(grade);
		//System.out.println(st_actual.getFinalGrade());
		
		assertEquals(finalGrade, st_actual.getFinalGrades().get(0), 0.1 , "addGrade failure: either the computation is not correct or other dependent required method is not correct/ implemented.");
	}
	// 1 points

	@Test
	public void testaddGrade3() throws InvalidTotalException {
		ArrayList<Double> grade = new ArrayList<Double>();
		ArrayList<Integer> weight = new ArrayList<Integer>();
		grade.add(100.0);
		weight.add(100);
		double finalGrade =  0;
		for (int i = 0; i < grade.size(); i++) {
			finalGrade += grade.get(i) *(weight.get(i)/100.0);
		}
		finalGrade = Math.round(finalGrade*10)/10.0;

		Student st_actual = new Student();
		st_actual.addGrade(grade, weight);
		//System.out.println(grade);
		//System.out.println(st_actual.getFinalGrade());
		
		assertEquals(finalGrade, st_actual.getFinalGrades().get(0), 0.1 , "addGrade failure: either the computation is not correct or other dependent required method is not correct/ implemented.");
	}

	/**
	// 3 points
	@Test 
	public void testAddCourse1() {
		ArrayList<AssessmentSol> as_expected = new ArrayList<AssessmentSol>(); 
		as_expected.add(AssessmentSol.getInstance('p', 50));
		as_expected.add(AssessmentSol.getInstance('e', 50));
		ArrayList<Assessment> as_actual = new ArrayList<Assessment>(); 
		as_actual.add(Assessment.getInstance('p', 50));
		as_actual.add(Assessment.getInstance('e', 50));
		CourseSol crs_expected = new CourseSol("E", as_expected, 3.0);
		Course crs_actual = new Course("E", as_actual, 3.0);
		StudentSol expected_st = new StudentSol(); 
		Student actual_st = new Student();
		expected_st.addCourse(crs_expected);
		actual_st.addCourse(crs_actual);
		ArrayList<CourseSol> crsList_expected = expected_st.getCourseTaken();
		ArrayList<Course> crsList_actual = actual_st.getCourseTaken();
		boolean equal = true; 
		equal = equal && crsList_expected.size() == crsList_actual.size();
		if(equal) {
			for (int i = 0; i < crsList_expected.size(); i++) {
				if (!(crsList_expected.get(i).getCode() == crsList_actual.get(i).getCode() && 
						crsList_expected.get(i).getCredit() == crsList_actual.get(i).getCredit())){
					equal = false; 
					break;
				}
			}
		}
		assertTrue(equal);
	}
	// 2 points
	@Test 
	public void testAddCourse2() {
		ArrayList<AssessmentSol> as_expected = new ArrayList<AssessmentSol>(); 
		as_expected.add(AssessmentSol.getInstance('p', 50));
		as_expected.add(AssessmentSol.getInstance('e', 40));
		as_expected.add(AssessmentSol.getInstance('p', 10));
		
		ArrayList<Assessment> as_actual = new ArrayList<Assessment>();
		as_actual.add(Assessment.getInstance('p', 50));
		as_actual.add(Assessment.getInstance('e', 50));
		
		CourseSol crs_expected = new CourseSol("E", as_expected, 3.0);
		Course crs_actual = new Course("E", as_actual, 3.0);
		
		StudentSol expected_st = new StudentSol(); 
		Student actual_st = new Student();
		
		expected_st.addCourse(crs_expected);
		actual_st.addCourse(crs_actual);
		
		ArrayList<CourseSol> crsList_expected = expected_st.getCourseTaken();
		ArrayList<Course> crsList_actual = actual_st.getCourseTaken();
		boolean equal =  crsList_expected.size() == crsList_actual.size();
		equal = equal && crsList_expected.get(0).getAssignment().size() == crsList_expected.size();
		assertFalse(equal);
	}
	

	// Cuorse: encapsulation 
	// 3 points
	@Test 
	public void testencapsulation1() {
		
		ArrayList<Assessment> as_actual = new ArrayList<Assessment>(); 
		as_actual.add(Assessment.getInstance('p', 50));
		as_actual.add(Assessment.getInstance('e', 50));
		
		Course crs_actual = new Course("E", as_actual, 3.0);
		ArrayList<Course> crs = new ArrayList<Course>(); 
		crs.add(crs_actual);
		Student actual_st = new Student("1", "John", crs);
		assertEquals(actual_st.getName(), "John", "Encapsulation failed for Course Class. No getter or incorrect getter for Name attribute");
	}
	// 2 points
	@Test 
	public void testencapsulation2() {
		
		ArrayList<Assessment> as_actual = new ArrayList<Assessment>(); 
		as_actual.add(Assessment.getInstance('p', 50));
		as_actual.add(Assessment.getInstance('e', 50));
		
		Course crs_actual = new Course("E", as_actual, 3.0);
		ArrayList<Course> crs = new ArrayList<Course>(); 
		crs.add(crs_actual);
		Student actual_st = new Student("1", "John", crs);
		assertEquals(actual_st.getStudentID(), "1", "Encapsulation failed for Course Class. No getter or incorrect getter for studentID attribute");
	}
	@Test
	// correctness of composition 
	public void testComposition() throws InvalidTotalException {
		Transcript actual = new Transcript("myInput1.txt", "outputTranscript.txt");
		Student stActual = actual.buildStudentArray().get(0);
		Student newStudent = new Student("1", "John", stActual.getCourseTaken());
		assertNotSame(stActual.getCourseTaken(), newStudent.getCourseTaken() , "Class Student:The relationship is not Composition!");
	}
	//_______________ Test Course___________________
	// this is to test equals- 2 point
	@Test
	public void testCourseEquals1() {
		ArrayList<Assessment> as1 = new ArrayList<Assessment>(); 
		as1.add(Assessment.getInstance('p', 50));
		as1.add(Assessment.getInstance('e', 50));
		ArrayList<Assessment> as2 = new ArrayList<Assessment>(); 
		as2.add(Assessment.getInstance('e', 30));
		as2.add(Assessment.getInstance('p', 30));
		as2.add(Assessment.getInstance('p', 40));
		Course crs1 = new Course("E", as1, 3.0);
		Course crs2 = new Course("E", as2, 3.0);
		assertFalse(crs1.equals(crs2), "Class Course: equals method is not correct for two differfent objects!");
	}
	// this is to test equals - 1 point
	@Test
	public void testCourseEquals2() {
		ArrayList<Assessment> as1 = new ArrayList<Assessment>(); 
		as1.add(Assessment.getInstance('p', 50));
		as1.add(Assessment.getInstance('e', 50));
		ArrayList<Assessment> as2 = new ArrayList<Assessment>(); 
		as2.add(Assessment.getInstance('e', 30));
		as2.add(Assessment.getInstance('p', 70));
		Course crs1 = new Course("E", as1, 3.0);
		Course crs2 = new Course("E", as2, 3.0);
		assertFalse(crs1.equals(crs2), "Class Course: equals method is not correct for two differfent objects!");
	}
	// this is to test equals - 2 point
	@Test
	public void testCourseEquals3() {
		ArrayList<Assessment> as1 = new ArrayList<Assessment>(); 
		as1.add(Assessment.getInstance('p', 50));
		as1.add(Assessment.getInstance('e', 50));
		ArrayList<Assessment> as2 = new ArrayList<Assessment>(); 
		as2.add(Assessment.getInstance('p', 50));
		as2.add(Assessment.getInstance('e', 50));
		Course crs1 = new Course("E", as1, 3.0);
		Course crs2 = new Course("E", as2, 3.0);
		assertTrue(crs1.equals(crs2), "Class Course: equals method is not correct for two same objects!");
	}

	// this is to test the encapsulation of Course:code - 3 point
	@Test
	public void testCourseEncapsulationCode() {
		ArrayList<Assessment> as = new ArrayList<Assessment>(); 
		as.add(Assessment.getInstance('p', 50));
		as.add(Assessment.getInstance('e', 50));
		Course crs = new Course("E", as, 3.0);
		assertEquals("E", crs.getCode(), "Class Course: Encpsulation failed for code attribute");
	}
	// this is to test the encapsulation of Course:credit - 2 point
	@Test
	public void testCourseEncapsulationCredit() {
		ArrayList<Assessment> as = new ArrayList<Assessment>(); 
		as.add(Assessment.getInstance('p', 50));
		as.add(Assessment.getInstance('e', 50));
		Course crs = new Course("E", as, 3.0);
		assertEquals(3.0, crs.getCredit(), 0.01, "Class course: Encapsulation failed for credit attribute");
	}
	// this is to test the composition relationship - 5 point
	@Test
	public void testCourseComposition1() {
		ArrayList<Assessment> as1 = new ArrayList<Assessment>(); 
		as1.add(Assessment.getInstance('p', 50));
		as1.add(Assessment.getInstance('e', 50));
		ArrayList<Assessment> as2 = new ArrayList<Assessment>(); 
		as2.add(Assessment.getInstance('p', 50));
		as2.add(Assessment.getInstance('e', 50));
		Course crs1 = new Course("E", as1, 3.0);
		Course crs2 = new Course("E", as2, 3.0);
		assertTrue(crs1.equals(crs2), "Class Course: Composition relationship is not correct!");
	}
	*/
	//______________ test Assessemnt _________________//
	// This is to tests both getInstance and encapsulation - 5 points
	@Test
	public void testGetInstanceAssessment1() {
		Assessment as = Assessment.getInstance('p', 10);
		assertTrue(as.getType()=='p', "Assessemnt Class: wrong object is created or no getter method was implemented.");
	}
	// This is to tests both getInstance and encapsulation - 5 points
	@Test
	public void testGetInstanceAssessment2() {
		Assessment as = Assessment.getInstance('p', 10);
		assertTrue(as.getWeight()==10, "Assessemnt Class: wrong object is created or no getter method was implemented.");
	}

}

 