package model.algorithms.transform.grammar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.grammar.Grammar;
import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public abstract class ProductionIdentifyAlgorithm extends
		GrammarTransformAlgorithm {

	private Set<IdentifyWrapper> myIdentifyMap;
	private Set<Production> myToAddSet;

	public ProductionIdentifyAlgorithm(Grammar g) {
		super(g);
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myIdentifyMap = new TreeSet<IdentifyWrapper>();
		myToAddSet = new TreeSet<Production>();
		populateIdentifyMap();
		return super.reset();
	}

	private void populateIdentifyMap() {
		for (Production p : this.getOriginalGrammar().getProductionSet()) {
			if (isOfTargetForm(p)) {
				myIdentifyMap.add(new IdentifyWrapper(p, shouldRemove(p)));
			}
		}
	}

	/**
	 * Override in subclasses if some productions that are identified should NOT
	 * be removed. By default, all identified productions will be removed.
	 * 
	 * @param p
	 * @return
	 */
	protected boolean shouldRemove(Production p) {
		return true;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[] { new IdentifyRemovesStep(),
				new AdjustGrammarStep() };
	}

	public int getNumberUnidentifiedTargets() {
		return getUnidentifiedTargets().size();
	}

	public Set<Production> getAllIdentifyTargets() {
		Set<Production> targets = new TreeSet<Production>();
		for (IdentifyWrapper wrap : myIdentifyMap)
			targets.add(wrap.prod);
		return targets;
	}

	public Set<Production> getUnidentifiedTargets() {
		Set<Production> unID = getAllIdentifyTargets();
		for (Production p : unID.toArray(new Production[0])) {
			if (isIdentified(p)) // if has been identified
				unID.remove(p);
		}
		return unID;
	}

	public Set<Production> getIdentifiedTargets() {
		Set<Production> id = new TreeSet<Production>(getAllIdentifyTargets());
		id.removeAll(getUnidentifiedTargets());
		return id;
	}

	private boolean isIdentified(Production p) {
		return getWrapperForProduction(p).hasBeedIDed;
	}

	private IdentifyWrapper getWrapperForProduction(Production p) {
		for (IdentifyWrapper wrap : myIdentifyMap)
			if (wrap.prod.equals(p))
				return wrap;
		return null;
	}

	public BooleanWrapper identifyProductionToBeRemoved(Production p) {
		IdentifyWrapper prod = getWrapperForProduction(p);
		if (prod == null)
			return new BooleanWrapper(false, "The production " + p
					+ "is not of the desired form.");

		if (prod.hasBeedIDed)
			return new BooleanWrapper(false, "The production " + p
					+ " has already been identified.");

		prod.hasBeedIDed = true;

		return new BooleanWrapper(true);
	}

	public int getNumAddsRemaining() {
		return getAddsRemaining().size();
	}

	public Set<Production> getAddsRemaining() {
		return new TreeSet<Production>(myToAddSet);
	}

	public int getNumRemovesRemaining() {
		return getAllRemovesLeft().size();
	}

	public Set<Production> getAllRemovesLeft() {
		Set<Production> toRemove = new TreeSet<Production>();
		for (IdentifyWrapper wrap : myIdentifyMap) {
			toRemove.add(wrap.prod);
		}
		return toRemove;
	}

	private void identifyAllRemaining() {
		for (IdentifyWrapper p : myIdentifyMap)
			identifyProductionToBeRemoved(p.prod);
	}

	private BooleanWrapper doOneFullRemoveAdd() {
		BooleanWrapper bw = new BooleanWrapper(true);
		if (getNumAddsRemaining() == 0) {
			return performRemove(getFirstRemove());
		}
		for (Production p : getAddsRemaining()) {
			bw = performAdd(p);
			if (bw.isError())
				return bw;
		}
		return bw;
	}

	public Production getFirstRemove() {
		IdentifyWrapper p = myIdentifyMap.toArray(new IdentifyWrapper[0])[0];
		return p.prod;
	}
	
	public Production getFirstAdd() {
		Production add = getAddsRemaining().toArray(new Production[0])[0];
		return add;
	}

	public BooleanWrapper performAdd(Production p) {
		if (!myToAddSet.contains(p))
			return new BooleanWrapper(false, "The production " + p
					+ " is not a valid production to be "
					+ "added to the transformed grammar.");
		myToAddSet.remove(p);
		this.getTransformedGrammar().getProductionSet().add(p);
		return new BooleanWrapper(true);
	}

	public BooleanWrapper performRemove(Production p) {
		IdentifyWrapper prod = getWrapperForProduction(p);
		if (prod == null)
			return new BooleanWrapper(false, "The production " + p
					+ " does not need to be removed.");

		myIdentifyMap.remove(prod);
		myToAddSet.addAll(getProductionsToAddForRemoval(p));
		if (prod.shouldRemove)
			this.getTransformedGrammar().getProductionSet().remove(p);
		return new BooleanWrapper(true);
	}

	public abstract boolean isOfTargetForm(Production p);

	/**
	 * Productions that need to be added to complete the process when a specific production is removed. 
	 * @param p
	 * @return
	 */
	public abstract Set<Production> getProductionsToAddForRemoval(Production p);

	public abstract String getIdentifyStepName();

	private class IdentifyWrapper implements Comparable<IdentifyWrapper> {
		public Production prod;
		public boolean hasBeedIDed;
		public boolean shouldRemove;

		public IdentifyWrapper(Production p, boolean remove) {
			prod = p;
			this.hasBeedIDed = false;
			this.shouldRemove = remove;
		}

		@Override
		public int compareTo(IdentifyWrapper o) {
			return this.prod.compareTo(o.prod);
		}
	}

	private class IdentifyRemovesStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return getIdentifyStepName();
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			identifyAllRemaining();
			return true;
		}

		@Override
		public boolean isComplete() {
			return getNumberUnidentifiedTargets() == 0;
		}

	}

	private class AdjustGrammarStep implements AlgorithmStep {

		@Override
		public String getDescriptionName() {
			return "Adjust Grammar";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			BooleanWrapper bw = doOneFullRemoveAdd();
			if (bw.isError())
				throw new AlgorithmException(bw);
			return true;
		}

		@Override
		public boolean isComplete() {
			return getNumRemovesRemaining() + getNumAddsRemaining() == 0;
		}

	}
}
