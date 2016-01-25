package mathGenerator.checkers;

public class SubCarryChecker implements Checker {
	public boolean check(double a, double b) {
		boolean bool = true;
		if (a < b) {
			return false;
		}
		String[] aDigits = Integer.toString(((int) a)).split("");
		String[] bDigits = Integer.toString(((int) b)).split("");
		String[] ansDigits = Integer.toString(((int) (a + b))).split("");
		for (int i = 1; i <= Math.min(ansDigits.length, bDigits.length); i++) {
			if ((Integer.parseInt(aDigits[aDigits.length - i])
					- Integer.parseInt(bDigits[bDigits.length - i]) < 0 || (Integer
					.parseInt(aDigits[aDigits.length - i])
					- Integer.parseInt(bDigits[bDigits.length - i]) != Integer
						.parseInt(ansDigits[ansDigits.length - i])))) {
				bool = false;
			}
		}
		return bool;
	}
}
