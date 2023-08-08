package Reports;

public class Tuple implements Comparable<Tuple> {
	int appearance;
	String nounPhrase;
	
	public Tuple(String noun, int app) {
		appearance = app;
		nounPhrase = noun;
	}
	
	public void Increment() {
		appearance++;
	}
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (! (o instanceof Tuple)) {
			return false;
		}
		Tuple t = (Tuple) o;
		if (nounPhrase.equals(t.nounPhrase)) {
			return true;
		}
		return false;
	}
	@Override
	public int compareTo(Tuple o) {
		Tuple t = (Tuple) o;
		return t.appearance - appearance;
	}
}
