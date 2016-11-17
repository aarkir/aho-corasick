import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class State {
  // move links
  private Map<Character, State> m = new HashMap<>();
  // goto links
  private Map<Character, State> g = new HashMap<>();
  private Character value;
  private ArrayList<String> outputStrings = new ArrayList<>();

  private State f;
  private State root;

  public State() {

  }

  public State(State root) {
  	this();
  	this.root = root;
  }

  public State(State root, Character a) {
  	this(root);
  	value = a;
  }

  // adds a <K, V> pair to m
  public void addMove(Character a, State r) {
  	m.put(a, r);
  }

  public State move(Character a) {
  	// should I add a pointer to root for each State
  	// and do getOrDefault to root?
  	State s = m.getOrDefault(a, root);
  	return (s == null && root == null) ? this : s;
  }

  public Iterator getGIterator() {
  	return g.entrySet().iterator();
  }

  public Iterator getMIterator() {
  	return m.entrySet().iterator();
  }

  public State g(Character a) {
  	State s = g.get(a);
  	return (s == null && value == null) ? this : s;
  }

  // used when the root should not map to itself
  public State gNoDefault(Character a) {
  	return g.get(a);
  }

  public void setF(State f) {
  	this.f = f;
  }

  public ArrayList<String> getOutputStrings() {
  	return outputStrings;
  }

  public void addOutput(String output) {
  	outputStrings.add(output);
  }

  public void addOutput(ArrayList<String> outputStrings) {
  	this.outputStrings.addAll(outputStrings);
  }

  public State f() {
  	return f;
  }

  // used to save g function values before computing move operations;
  // necessary if adding keywords later is desired
  public void saveGoToMove() {
  	m = new HashMap<>(g);
  }

  public void addG(Character a, State s) {
  	g.put(a, s);
  }

  // toString for testing
  @Override
  public String toString() {
  	String str = "";
  	str += value + " " + hashCode();
  	for (Map.Entry<Character, State> e : g.entrySet()) {
      str += " [ " + e.getKey()+" : "+e.getValue().hashCode() + " ] ";
	}
  	return str;
  }
}