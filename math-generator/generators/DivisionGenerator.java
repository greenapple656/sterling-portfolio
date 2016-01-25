package mathGenerator.generators;

import mathGenerator.checkers.Checker;
import mathGenerator.problems.DivisionProblem;

public class DivisionGenerator extends Generator {
	private char remainderType = 'n';
	private int ansDecimalPlaces = 0;

	public DivisionGenerator(String param, Checker ch) {
		super(param, ch);
		setParam(param);
	}

	public void setAnsDecPlaces(int places) {
		ansDecimalPlaces = places;
	}

	// /#0:100##0:10.0.3d
	public void setParam(String param) {
		int pos = 1;
		int index = 0;
		operator = '/';
		int[] nums = new int[6];
		while (pos < param.length()) {
			if (Character.isDigit(param.charAt(pos))) {
				int num = Character.getNumericValue(param.charAt(pos));
				int pos2 = pos + 1;
				while (pos2 < param.length()
						&& Character.isDigit(param.charAt(pos2))) {
					num = num * 10
							+ Character.getNumericValue(param.charAt(pos2));
					pos2++;
				}
				nums[index] = num;
				index++;
				pos++;
			}
			pos++;
		}
		fst_low = nums[0];
		fst_high = nums[1];
		snd_low = nums[2];
		if (snd_low == 0) {
			snd_low = 1;
		}
		snd_high = nums[3];
		decimalPlaces = nums[4];
		ansDecimalPlaces = nums[5];
		remainderType = param.charAt(param.length() - 1);

	}

	public char getRemainderType() {
		return remainderType;
	}

	public String getParam() {
		return "/" + "#" + fst_low + ":" + fst_high + "##" + snd_low + ":"
				+ snd_high + "." + decimalPlaces + "." + ansDecimalPlaces
				+ remainderType;
	}

	public String getGeneralParam() {
		operator = '/';
		return super.getParam();
	}

	public DivisionProblem next() {
		double fst = 0;
		double snd = 0;
		double ans = 0;
		double rem = 0;

		do {
			if (remainderType != 'd') {
				fst = (int) (fst_low + (fst_high - fst_low) * Math.random());
				snd = (int) (snd_low + (snd_high - snd_low) * Math.random());

				switch (remainderType) {
				case 'n':
					do {
						snd = (int) (snd_low + (snd_high - snd_low)
								* Math.random());
					} while (((double) fst / (double) snd) % 1 != 0);
					ans = fst / snd;
					rem = 0;
					break;
				case 'i':
					ans = ((int) fst) / ((int) snd);
					rem = fst % snd;
					break;
				default:
					ans = fst / snd;
					break;
				}
			} else {
				fst = Math.round(fst_low + (fst_high - fst_low) * Math.random()
						* Math.pow(10, decimalPlaces))
						/ Math.pow(10, decimalPlaces);
				snd = Math.round(snd_low + (snd_high - snd_low) * Math.random()
						* Math.pow(10, decimalPlaces))
						/ Math.pow(10, decimalPlaces);
				ans = Math.round((fst / snd) * Math.pow(10, ansDecimalPlaces))
						/ Math.pow(10, ansDecimalPlaces);
			}
		} while (!checker.check(fst, snd));
		return new DivisionProblem(fst, snd, ans, rem);
	}
}
