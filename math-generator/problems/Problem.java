package mathGenerator.problems;

public class Problem {
	private double fst;
	private double snd;
	protected double ans;
	protected String userAns;
	protected boolean answered = false;

	public Problem(double f, double s, double a) {
		fst = f;
		snd = s;
		ans = a;
	}

	public boolean checkAnswer() {
		double parseAnswer = Double.parseDouble(userAns);
		if (answered)
			return ans == parseAnswer;
		return false;
	}

	public void answer(String answer) {
		answered = true;
		if (answer.equals("")) {
			answered = false;
		}
		userAns = answer;
	}

	public boolean getAnswered() {
		return answered;
	}

	public void setAnswered(boolean bool) {
		answered = bool;
	}

	public double getSecond() {
		return snd;
	}

	public double getFirst() {
		return fst;
	}
}
