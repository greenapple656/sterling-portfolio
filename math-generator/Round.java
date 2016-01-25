package mathGenerator;

import mathGenerator.generators.DivisionGenerator;
import mathGenerator.generators.Generator;
import mathGenerator.problems.DivisionProblem;
import mathGenerator.problems.Problem;

public class Round {
	private Generator generator;
	private Problem[] problems;
	private int numProblems;
	private int numCorrect;

	public Round(Generator generator, int numProblems) {
		this.generator = generator;
		this.numProblems = numProblems;
		problems = new Problem[numProblems];
		for (int i = 0; i < numProblems; i++) {
			problems[i] = generator.next();
		}
	}

	public void reset() {
		for (int i = 0; i < numProblems; i++) {
			problems[i] = generator.next();
		}
	}

	public Problem getProblem(int index) {
		return problems[index];
	}

	public void setAnswerAt(int index, String ans) {
		problems[index].answer(ans);
	}

	public void setAnswerAt(int index, String ans, String rem) {
		if (generator.getClass() == DivisionGenerator.class) {
			((DivisionProblem) problems[index]).answer(ans, rem);
		}
	}

	public boolean[] checkAll() {
		boolean[] corrs = new boolean[problems.length];
		for (int i = 0; i < corrs.length; i++) {
			corrs[i] = problems[i].checkAnswer();
		}
		return corrs;
	}

	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public int getNumProblems() {
		return numProblems;
	}

	public int getNumCorrect() {
		return numCorrect;
	}

	public int getNumAnswered() {
		int total = 0;
		for (Problem p : problems) {
			if (p.getAnswered()) {
				total++;
			}
		}
		return total;
	}
}
