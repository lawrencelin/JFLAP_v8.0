package model.algorithms.testinput.simulate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.symbols.SymbolString;

public class AutoSimulator extends AutomatonSimulator {

	private SingleInputSimulator mySimulator;

	public AutoSimulator(Automaton a, int specialCase) {
		super(a);
		mySimulator = new SingleInputSimulator(a, specialCase, false);
	}

	public List<ConfigurationChain> getFirstAccept() {
		List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();

		for (ConfigurationChain chain : mySimulator.getChains())
			if (chain.isAccept())
				toReturn.add(chain);
		if (!toReturn.isEmpty())
			return toReturn;
		return getNextAccept();
	}
	
	public List<ConfigurationChain> getFirstHalt() {
		List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();

		for (ConfigurationChain chain : mySimulator.getChains())
			if (chain.isHalted())
				toReturn.add(chain);
		if (!toReturn.isEmpty())
			return toReturn;
		return getNextHalt();
	}

	public List<ConfigurationChain> getNextAccept() {

		while (mySimulator.canStep()) {
			List<ConfigurationChain> chains = getNextHalt();
			List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();

			for (ConfigurationChain chain : chains) {
				if (chain.isAccept())
					toReturn.add(chain);
			}
			if (!toReturn.isEmpty()) {
				return toReturn;
			}
		}

		return new ArrayList<ConfigurationChain>();

	}

	public List<ConfigurationChain> getNextHalt() {
		while (!mySimulator.getChains().isEmpty()) {
			ConfigurationChain[] chains = mySimulator.step();
			
			List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();
			for (ConfigurationChain chain : chains) {
				if (chain.isFinished())
					toReturn.add(chain);
			}
			if (!toReturn.isEmpty()) {
				return toReturn;
			}
		}

		return new ArrayList<ConfigurationChain>();
	}

	public List<ConfigurationChain> getLastHalt() {
		List<ConfigurationChain> result = new ArrayList<ConfigurationChain>();
		while (!mySimulator.getChains().isEmpty()) {
			ConfigurationChain[] chains = mySimulator.step();
			List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();
			for (ConfigurationChain chain : chains) {
				if (chain.isFinished())
					toReturn.add(chain);
			}
			if (!toReturn.isEmpty()) {
				result.clear();
				result.addAll(toReturn);
			}
		}
		return result;
	}

	private void removeRejectChains() {
		ArrayList<ConfigurationChain> copy = new ArrayList<ConfigurationChain>(
				mySimulator.getChains());
		for (ConfigurationChain chain : copy) {
			if (chain.isFinished()) {
				mySimulator.removeConfigurationChain(chain);
			}
		}

	}

	@Override
	public String getDescriptionName() {
		return "Auto Simulate on " + this.getAutomaton().getDescriptionName();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object copy() {
		return new AutoSimulator(getAutomaton(),
				mySimulator.getSpecialAcceptCase());
	}

	@Override
	public void beginSimulation(SymbolString... input) {
		mySimulator.beginSimulation(input);
	}

	public void beginSimulation(Configuration c) {
		mySimulator.beginSimulation(c);
	}

	@Override
	public int getSpecialAcceptCase() {
		return mySimulator.getSpecialAcceptCase();
	}

	public void beginSimulation(SymbolString input) {
		mySimulator.beginSimulation(input);
	}

}
