package test;

/*
 * Test for the ElementsParser class (for user-input when building a new set)
 */


import java.util.TreeSet;

import model.sets.elements.ElementsParser;


public class SetsParserTest {

	@Test
	public void test() throws Exception {
		
		TreeSet<String> actual;
		TreeSet<String> expected;
		
		ElementsParser parser = new ElementsParser("a b {c}");
		
		actual = parser.parse();
		expected = new TreeSet<String>();
		expected.add("a");
		expected.add("b");
		expected.add("{c}");
		
		assertEquals(expected, actual);
		
		
		parser = new ElementsParser("{},{1}{2}{5}");
		actual = parser.parse();
		expected = new TreeSet<String>();
		expected.add("{}");
		expected.add("{1}");
		expected.add("{2}");
		expected.add("{5}");
		
		assertEquals(expected, actual);
		
		
		parser = new ElementsParser("v, 3, {5, {7}");
		try {
			parser.parse();
		} catch (Exception e) {
			
		}
		
	}

}
