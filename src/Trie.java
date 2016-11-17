import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

public class Trie {
  // used to hold current state of FSM
  private State state;
  // holds all states in Trie
  private ArrayList<State> states = new ArrayList<>();
  private State root;
  private Set<Character> alphabet = new HashSet<>();

  public Trie() {
  	state = root = new State();
  	states.add(state);
  }
  
  public Trie(String[] patterns) {
  	this();
    for (String pattern : patterns) {
      enter(pattern);
    }
    constructFailures();
    makeDeterministic();
    // verify();
  }

  public void addPattern(String pattern) {
  	enter(pattern);
  	constructFailures();
  	makeDeterministic();
  }

  // enter patterns.
  // only needs to be run once for each pattern
  // works
  private void enter(String pattern) {
    State state = root;
    State next = state.gNoDefault(pattern.charAt(0));
    int j = 1;
    for (; next != null; ++j) {
      state = next;
      next = state.gNoDefault(pattern.charAt(j));
    }
    --j;
    for (; j < pattern.length(); ++j) {
      alphabet.add(pattern.charAt(j));
      // reuse next
      next = new State(root, pattern.charAt(j));
      state.addG(pattern.charAt(j), next);
      states.add(next);
      state = next;
    }
    state.addOutput(pattern);
  }

  // construct failure must be rerun for each new pattern added
  // works
  private void constructFailures() {
  	Queue<State> queue = new LinkedList<>();
  	Iterator i = root.getGIterator();
  	while (i.hasNext()) {
  	  State s = (State) ((Map.Entry) i.next()).getValue();  	  
  	  queue.add(s);
  	  s.setF(root);
  	}
  	// while queue is not empty
  	for (State r = queue.poll(); r != null; r = queue.poll()) {
  	  // reuse i
  	  i = r.getGIterator();
  	  while (i.hasNext()) {
  	  	Map.Entry e = (Map.Entry) i.next();
  	  	Character a = (Character) e.getKey();
  	    State s = (State) e.getValue();
  	    queue.add(s);
  	    State state = r.f();
  	    while (state.g(a) == null) {
  	      state = state.f();
  	    }
  	    s.setF(state.g(a));
  	    s.addOutput(s.f().getOutputStrings());
  	  }
  	}
  }

  private void makeDeterministic() {
  	// maybe remove saveGoToMove and put copies into while loop
  	root.saveGoToMove();
  	Queue<State> queue = new LinkedList<>();
  	// we skip setting move function because it is already goto
  	// this may be changed to support adding new keywords
  	Iterator i = root.getGIterator();
  	while (i.hasNext()) {
  	  queue.add((State) ((Map.Entry) i.next()).getValue());
  	}
  	// for each element in the queue
  	for (State r = queue.poll(); r != null; r = queue.poll()) {
	  for (Character a : alphabet) {
        State s = r.g(a);
        if (s != null) {
          queue.add(s);
          r.addMove(a, s);
        }
        else {
          r.addMove(a, r.f().move(a));
        }
	  }
  	}
  }

  // NFA using Goto and Failure functions
  public ArrayList<String> searchGF(Character a) {
  	while (state.g(a) == null) {
  	  state = state.f();
  	}
  	state = state.g(a);
  	return state.getOutputStrings();
  }

  // DFA using Move
  public ArrayList<String> search(Character a) {
  	state = state.move(a);
  	return state.getOutputStrings();
  }

  // shows g, f, o, and m functions
  private void verify() {
    for (State state : states) {
  	  Iterator i = state.getGIterator();
  	  while (i.hasNext()) {
  	  	Map.Entry e = (Map.Entry) i.next();
  	  	Character a = (Character) e.getKey();
  	    State s = (State) e.getValue();
  	  	System.out.println("g("+states.indexOf(state)+","+a+")="+states.indexOf(s));
  	  }
      System.out.println("f("+states.indexOf(state)+")="+states.indexOf(state.f()));
      System.out.println("o("+states.indexOf(state)+")="+state.getOutputStrings());
      
      Iterator j = state.getMIterator();
      while (j.hasNext()) {
      	Map.Entry e = (Map.Entry) j.next();
  	  	Character a = (Character) e.getKey();
  	    State s = (State) e.getValue();
  	  	System.out.println("m("+states.indexOf(state)+","+a+")="+states.indexOf(s));
      }

      System.out.println();
    }
  }
}