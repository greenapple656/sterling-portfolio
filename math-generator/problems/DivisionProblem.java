package mathGenerator.problems;

public class DivisionProblem extends Problem {
	private double rem;
	private String userRem = "0";

	public DivisionProblem(double f, double s, double a, double r) {
		super(f, s, a);
		rem = r;

	}

	public void answer(String ans, String rem) {
		answer(ans);
		userRem = rem;
	}

	public void answerRem(String rem) {
		userRem = rem;
	}

	public boolean checkAnswer() {
		boolean correct = super.checkAnswer();
		return correct && Double.parseDouble(userRem) == rem;
	}

}
