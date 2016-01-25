package mathGenerator.generators;

import mathGenerator.checkers.Checker;
import mathGenerator.problems.Problem;

public class Generator

{
	protected int fst_low;
	protected int fst_high;
	protected int snd_low;
	protected int snd_high;
	protected char operator;
	protected int decimalPlaces;
	protected Checker checker;

	public Generator(String param, Checker ch) {
		setParam(param);
		checker = ch;
	}

	// +#0:10##10:100.0
	public void setParam(String param) {
		int pos = 1;
		int index = 0;
		operator = param.charAt(0);
		int[] nums = new int[5];
		while (pos < param.length()) {
			if (Character.isDigit(param.charAt(pos))) {
				int num = Character.getNumericValue(param.charAt(pos));
				int pos2 = pos + 1;
				while (pos2 < param.length()
						&& Character.isDigit(param.charAt(pos2))) {
					num = num * 10
							+ Character.getNumericValue(param.charAt(pos2));
					pos++;
					pos2++;
				}
				nums[index] = num;
				index++;
			}
			pos++;
		}
		fst_low = nums[0];
		fst_high = nums[1];
		snd_low = nums[2];
		snd_high = nums[3];
		decimalPlaces = nums[4];
	}

	public String getParam() {
		return operator + "#" + fst_low + ":" + fst_high + "##" + snd_low + ":"
				+ snd_high + "." + decimalPlaces;
	}

	public Problem next() {
		double fst = 0;
		double snd = 0;
		double ans = 0;
		boolean checked = true;
		do {
			fst = Math.round(fst_low + (fst_high - fst_low) * Math.random()
					* Math.pow(10, decimalPlaces))
					/ Math.pow(10, decimalPlaces);
			snd = Math.round(snd_low + (snd_high - snd_low) * Math.random()
					* Math.pow(10, decimalPlaces))
					/ Math.pow(10, decimalPlaces);
			switch (operator) {
			case '+':
				ans = fst + snd;
				break;
			case '-':
				ans = fst - snd;
				checked = fst >= snd;
				break;
			case '*':
				ans = fst * snd;
				break;
			default:
				ans = 0;
				break;
			}
			checked &= checker.check(fst, snd);
		} while (!checked);
		return new Problem(fst, snd, ans);
	}

	public Checker getChecker() {
		return checker;
	}

	public int[] ranges() {
		return new int[] { fst_low, fst_high, snd_low, snd_high };
	}

	public void setRanges(int[] nums) {
		fst_low = nums[0];
		fst_high = nums[1];
		snd_low = nums[2];
		snd_high = nums[3];
	}

	public void setDecimalPlaces(int places) {
		decimalPlaces = places;
	}
}
