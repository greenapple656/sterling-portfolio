/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathGenerator.checkers;

/**
 *
 * @author Students
 */
public class ValidDivisionChecker implements Checker {

	@Override
	public boolean check(double a, double b) {
		return (b != 0) && (a >= b);
	}

}
