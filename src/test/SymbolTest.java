package test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import model.symbols.Symbol;
import util.JFLAPConstants;

public class SymbolTest extends TestHarness implements JFLAPConstants{

	public static void main(String[] args) {
		new SymbolTest();
	}

	@Override
	public void runTest() {
		Set<Symbol[]> bank[][] = new Set[3][3];
		Symbol test = new Symbol("test");
		Symbol[] testset = new Symbol[3];
		testset[0] = test;
		testset[1] = test;
		testset[2] = test;
		bank[0][0] = new HashSet<Symbol[]>();
		bank[0][0].add(testset);
	}

	@Override
	public String getTestName() {
		return "Symbol Debug Test";
	}

}
