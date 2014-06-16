package model.algorithms.testinput.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import oldnewstuff.main.JFLAP;
import debug.JFLAPDebug;
import model.automata.Automaton;
import model.automata.State;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.symbols.SymbolString;

public class SingleInputSimulator extends AutomatonSimulator {

	private LinkedHashSet<ConfigurationChain> myChains;
	private int mySpecialCase;
	private Configuration myInitialConfiguration;
	private boolean closure;

	public static final int DEFAULT = 0, ACCEPT_BY_EMPTY_STACK = 1;

	public SingleInputSimulator(Automaton a, int specialCase, boolean closure) {
		super(a);
		myChains = new LinkedHashSet<ConfigurationChain>();
		mySpecialCase = specialCase;
		this.closure = closure;
	}

	public SingleInputSimulator(Automaton a, boolean closure) {
		this(a, DEFAULT, closure);
	}

	public boolean canStep() {
		for (ConfigurationChain chain : myChains) {
			if (!chain.isHalted())
				return true;
		}
		return false;
	}

	public ConfigurationChain[] step() {

		LinkedList<ConfigurationChain> copy = new LinkedList<ConfigurationChain>(
				myChains);
		myChains.clear();
		for (ConfigurationChain chain : copy) {
			if (chain.isFrozen())
				myChains.add(chain);
			else if (chain.isFinished()) {
				 myChains.remove(chain);
				// As myChains is clear, don't do anything, just ignore it
			} else {
				myChains.addAll(stepAndFork(chain));
			}
		}

//		updateSelectedStates();
		// JFLAPDebug.print(myChains);

		return myChains.toArray(new ConfigurationChain[0]);
	}

	public Collection<? extends ConfigurationChain> stepAndFork(
			ConfigurationChain chain) {
		ArrayList<ConfigurationChain> chains = new ArrayList<ConfigurationChain>();
		LinkedList<Configuration> nextConfigs = chain.getCurrentConfiguration()
				.getNextConfigurations();
		ConfigurationChain clone = chain.clone();

		if (closure) {
			Configuration next = nextConfigs.pollFirst();
			
			while (next != null){
				Transition trans = next.getTransitionTo();
				if(trans == null || !trans.isLambdaTransition() || trans.isLoop())
					break;
				next = nextConfigs.pollFirst();
			}
			if (next != null) {
				chain.add(next);
				chains.add(chain);

				Set<State> seen = new HashSet<State>();
				seen.add(next.getState());
				addClosure(chain, chains, seen);
			}

			for (Configuration c : nextConfigs) {
				Transition trans = c.getTransitionTo();
				
				if (trans == null || !trans.isLambdaTransition()) {
					String nextID = chain.getID() + chain.getNumChildren();
					
					ConfigurationChain newChain = new ConfigurationChain(c,
							clone, nextID);
					chains.add(newChain);
					chain.incrementNumChildren();

					Set<State> seen = new HashSet<State>();
					seen.add(c.getState());
					addClosure(newChain, chains, seen);
				}
			}
		} else {
			Configuration next = nextConfigs.pollFirst();
			chain.add(next);
			chains.add(chain);

			for (Configuration c : nextConfigs) {
				String nextID = chain.getID() + chain.getNumChildren();
				chains.add(new ConfigurationChain(c, clone, nextID));
				chain.incrementNumChildren();
			}
		}
		return chains;
	}

	private List<ConfigurationChain> getAllAcceptChains() {
		List<ConfigurationChain> toReturn = new ArrayList<ConfigurationChain>();
		for (ConfigurationChain chain : myChains) {
			if (chain.isAccept())
				toReturn.add(chain);
		}
		return toReturn;
	}

	public void updateSelectedStates() {
		// this.getAutomaton().clearSelection();
		// for (ConfigurationChain chain : myChains){
		// if (!chain.isFrozen())
		// chain.getCurrentConfiguration().getState().setSelected(true);
		// }
	}

	public ConfigurationChain[] reverse() {
		Set<ConfigurationChain> toRemove = new HashSet<ConfigurationChain>();
		Set<ConfigurationChain> toAdd = new HashSet<ConfigurationChain>();
		for (ConfigurationChain chain : myChains) {
			if (chain.isFrozen())
				continue;
			chain.reverse();
			if (chain.isEmpty()) {
				// Fix reverse
				toAdd.add(chain.getParent());
				toRemove.add(chain);
			}
		}

		myChains.removeAll(toRemove);
		myChains.addAll(toAdd);
		updateSelectedStates();
		return myChains.toArray(new ConfigurationChain[0]);
	}

	@Override
	public String getDescriptionName() {
		return "Simulate input on " + this.getAutomaton().getDescriptionName();
	}

	public void clear() {
		myChains.clear();
		myInitialConfiguration = null;
		this.updateSelectedStates();
	}

	@Override
	public void beginSimulation(SymbolString... input) {
		Configuration init = createInitConfig(input);
		beginSimulation(init);
	}

	private Configuration createInitConfig(SymbolString... input) {
		return createInitConfig(getAutomaton().getStartState(), input);
	}

	private Configuration createInitConfig(State s, SymbolString... input) {
		return ConfigurationFactory.createInitialConfiguration(
				this.getAutomaton(), s, input);
	}

	public void beginSimulation(Configuration c) {
		this.clear();
		myInitialConfiguration = c;
		ConfigurationChain chain = new ConfigurationChain(
				myInitialConfiguration, null, "0");
		myChains.add(chain);
		if (closure) {
			Set<State> seen = new HashSet<State>();
			seen.add(c.getState());
			addClosure(chain, myChains, seen);
		}
		this.updateSelectedStates();
	}

	public void removeConfigurationChain(ConfigurationChain chain) {
		myChains.remove(chain);
		updateSelectedStates();
	}

	public LinkedHashSet<ConfigurationChain> getChains() {
		return myChains;
	}

	public void freezeAll() {
		for (ConfigurationChain chain : getChains()) {
			chain.freeze();
		}
		updateSelectedStates();
	}

	public void thawAll() {
		for (ConfigurationChain chain : getChains()) {
			chain.thaw();
		}
		updateSelectedStates();
	}

	public boolean canReverse() {
		for (ConfigurationChain chain : this.getChains()) {
			if (!chain.isFrozen()
					&& (chain.size() > 1 || chain.getParent() != null))
				return true;
		}
		return false;
	}

	public boolean isRunning() {
		return myInitialConfiguration != null;
	}

	public int getSpecialAcceptCase() {
		return mySpecialCase;
	}

	public void reset() {
		beginSimulation(myInitialConfiguration);
	}

	public void freezeConfigurationChain(ConfigurationChain chain) {
		chain.freeze();
		this.updateSelectedStates();
	}

	public void thawConfigurationChain(ConfigurationChain chain) {
		chain.thaw();
		this.updateSelectedStates();
	}

	@Override
	public SingleInputSimulator copy() {
		return new SingleInputSimulator(getAutomaton(), getSpecialAcceptCase(),
				closure);
	}

	@Override
	public String getDescription() {
		return null;
	}

	private void addClosure(ConfigurationChain chain,
			Collection<ConfigurationChain> chains, Set<State> seen) {
		int numLambda = 0;
		boolean allLambda = true;

		Configuration current = chain.getCurrentConfiguration();
		LinkedList<Configuration> next = current.getNextConfigurations();

		int size = next.size();
		ConfigurationChain clone = chain.clone();

		// by default, we want non-lambda transitions to continue the current
		// chain
		for (Configuration config : next) {
			Transition trans = config.getTransitionTo();
			if (trans == null || !trans.isLambdaTransition() || trans.isLoop())
				allLambda = false;
		}

		if (allLambda) {
			Configuration config = next.pollFirst();

			while (config != null) {
				Transition trans = config.getTransitionTo();

				// This is going to continue the current chain. Only increment
				// numLambda if you are staying on the current state (we don't
				// want to delete the current chain if it goes on)
				if (trans.isLambdaTransition()) {
					State s = config.getState();

					if (trans.isLoop())
						numLambda++;
					else if (!seen.contains(s)) {
						chain.add(config);
						seen.add(s);
						addClosure(chain, chains, seen);
					}
					break;
				}
				config = next.pollFirst();
			}
		}

		for (Configuration nextConfig : next) {
			Transition trans = nextConfig.getTransitionTo();

			if (trans != null && trans.isLambdaTransition()) {
				State s = nextConfig.getState();

				if (!trans.isLoop() && !seen.contains(s)) {
					String nextID = chain.getID() + chain.getNumChildren();
					ConfigurationChain newChain = new ConfigurationChain(
							nextConfig, clone, nextID);

					chain.incrementNumChildren();
					chains.add(newChain);

					seen.add(s);
					addClosure(newChain, chains, seen);
				}
				numLambda++;
			}
		}
		seen.remove(current.getState());
		if (numLambda != 0 && allLambda && !chain.isHalted())
			chains.remove(chain);
	}
}
