package Assignment2;

import java.io.IOException;
import java.util.ArrayList;

public class testCode {

	public static void main(String[] args) throws IOException, InvalidTotalException {
		Transcript ts = new Transcript("input.txt", "transcript.txt");
		ts.printTranscript(ts.buildStudentArray());
	}
}
