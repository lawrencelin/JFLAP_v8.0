package model.languages;


public class Tuple<T extends Comparable<T>, S extends Comparable<S>> implements
		Comparable<Tuple<T, S>> {
	private T myT;
	private S myS;

	public Tuple(T t, S s) {
		myT = t;
		myS = s;
	}

	@Override
	public int compareTo(Tuple<T, S> o) {
		if (myT.equals(o.myT)) {
			return myS.compareTo(o.myS);
		}
		return myT.compareTo(o.myT);
	}

	public String toString() {
		return "(" + myT + "," + myS + ")";
	}

}
