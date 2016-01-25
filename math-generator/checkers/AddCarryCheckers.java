package mathGenerator.checkers;

public class AddCarryChecker implements Checker {

	public boolean check(double a, double b) {
		boolean bool = true;
		String[] aDigits = Integer.toString(((int) a)).split("");
		String[] bDigits = Integer.toString(((int) b)).split("");
		for (int i = 1; i <= Math.min(aDigits.length, bDigits.length); i++) {
			if (Integer.parseInt(aDigits[aDigits.length - i])
					+ Integer.parseInt(bDigits[bDigits.length - i]) >= 10) {
				bool = false;
			}
		}
		return bool;
	}

}
