package model.sets.elements;

import java.util.Stack;
import java.util.TreeSet;

import model.sets.operations.SetOperationException;

public class ElementsParser {

	private String myInput;
	private int myIndex;
	private boolean isBalanced;
	private Stack<Character> myStack;
	private TreeSet<Element> myElements;

	public ElementsParser (String input) {
		myInput = clean(input);
		myIndex = 0;
		isBalanced = true;

		myStack = new Stack<Character>();
		myElements = new TreeSet<Element>();
	}


	public String clean (String str) {
		return str.trim().replaceAll("( )+", " ");
	}

	public TreeSet<Element> parse () throws Exception {

		String running = "";

		while (myIndex < myInput.length()) {
			char current = currentChar();

			isBalanced = myStack.size() == 0;

			if (isDelimiter(current) && isBalanced) {
				if (running.length() > 0) {
					myElements.add(new Element(running));
					running = "";
				}
				myIndex++;
				continue;
			}

			else if (current == '{') {
				myStack.push(current);
			}

			else if (current == '}') {
//				try {
//					myStack.pop();
//				} catch (EmptyStackException e) {
//					throw new SetOperationException("Invalid formatting: not enough }'s");
//				}
				if (myStack.pop() == null)
					throw new SetOperationException("Invalid formatting: not enough }'s");
				
			}

			running += current;
		
			isBalanced = myStack.size() == 0;
			if (atEnd() && isBalanced) {
				myElements.add(new Element(running));
			}

			myIndex++;
		}

		if (!isBalanced) {
			throw new SetOperationException("Invalid formatting: unequal # of { and }");
		}

//		JFLAPDebug.print("Parsed elements: "  + myElements.toString());

		return myElements;
	}


	private char currentChar () {
		return myInput.charAt(myIndex);
	}


	private boolean isDelimiter (char c) {
		return Character.isWhitespace(c) || c == ',';
	}

	private boolean atEnd () {
		return myIndex + 1 == myInput.length();
	}
	
}
