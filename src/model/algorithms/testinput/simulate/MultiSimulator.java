package model.algorithms.testinput.simulate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import model.automata.Automaton;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * A simple class designed to save the results for multiple input
 * simulations. Runs simulations in multiple threads.
 * 
 * NOTE: This may never be used, since a direct mvc relationship
 * is no longer necessary. 
 *  
 * @author Julian
 *
 */
public class MultiSimulator extends AutomatonSimulator {

	private int mySpecialCase;
	private LinkedList<SimulatorThread> myThreads;
	private boolean[] myResultArray;

	public MultiSimulator(Automaton a, int specialCase) {
		super(a);
		mySpecialCase = specialCase;
	}

	@Override
	public String getDescriptionName() {
		return "Multiple Simulate";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Object copy() {
		return new MultiSimulator(getAutomaton(), getSpecialAcceptCase());
	}

	@Override
	public void beginSimulation(SymbolString... input) {
		myResultArray = new boolean[input.length];
		int i = 0;
//		for (SymbolString[] inputArray: input){
//			myThreads.add(new SimulatorThread(i++, inputArray));
//		}
	}

	public boolean[] doAllSimulations(){
		for (SimulatorThread thread: myThreads){
			thread.run();
		}
		return myResultArray;
	}
	
	@Override
	public int getSpecialAcceptCase() {
		return mySpecialCase;
	}
	
	
	public synchronized void signalDone(int id) {
		myResultArray[id] = myThreads.get(id).isAccept();
	}
	
	
	private class SimulatorThread extends Thread{
		private AutoSimulator mySimulator;
		private boolean amAccept;
		private boolean amDone;
		private int myID;

		public SimulatorThread(int id, SymbolString ... input){
			mySimulator = new AutoSimulator(getAutomaton(), 
					mySpecialCase);
			mySimulator.beginSimulation(input);
			myID = id;
		}
		
		@Override
		public void run() {
			amAccept = mySimulator.getNextAccept() != null;
			signalDone(myID);
		}
		
		public synchronized boolean isAccept(){
			return amAccept;
		}
	}







	
}
