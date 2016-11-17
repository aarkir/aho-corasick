import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

public class Trie {
  // used to hold current state of FSM
  private State state;
  // holds all states in Trie
  private ArrayList<State> states = new ArrayList<>();
  // contains ID number of next state
  // used so additional states can be added after initialization
  // private int newState = 0;
  private State root;

  public Trie() {
  	state = root = new State();
  	states.add(state);
  }
  
  public Trie(String[] patterns) {
  	this();
    for (String pattern : patterns) {
      enter(pattern);
    }
    // for (int i = 0; i < states.size(); i++) {
    //   System.out.println(i + " " + states.get(i));
    // }
    constructFailures();

    makeDeterministic();

    //
    // shows g, f, and o functions
    //
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
      // state.saveGoToMove();
    }

    // for (State state : states) {
    //   Iterator i = state.getMIterator();
    //   while (i.hasNext()) {
    //   	System.out.println(i.next());
    //   }
    //   System.out.println();
    // }
  }

  public void addPattern(String pattern) {
  	enter(pattern);
  }

  // enter patterns.
  // only needs to be run once for each pattern
  // works
  private void enter(String pattern) {
    State state = states.get(0);
    State next = state.gNoDefault(pattern.charAt(0));
    int j = 1;
    for (; next != null; ++j) {
      state = next;
      next = state.gNoDefault(pattern.charAt(j));
    }
    --j;
    for (; j < pattern.length(); ++j) {
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
  	Iterator i = states.get(0).getGIterator();
  	while (i.hasNext()) {
  	  State s = (State) ((Map.Entry) i.next()).getValue();  	  
  	  queue.add(s);
  	  s.setF(states.get(0));
  	}
  	// initial queue generated successfully
  	// System.out.println("\n" + queue + "\n");
  	// while queue is not empty
  	for (State r = queue.poll(); r != null; r = queue.poll()) {
  		// System.out.println(r);
  	  // reuse i
  	  i = r.getGIterator();
  	  while (i.hasNext()) {
  	  	Map.Entry e = (Map.Entry) i.next();
  	  	Character a = (Character) e.getKey();
  	    State s = (State) e.getValue();
  	    queue.add(s);
  	    State state = r.f();
  	    // while g(state, a) == null
  	    // System.out.println(states.indexOf(r) + " " + states.indexOf(state.g(a)));
  	    // System.out.println("a: " + a);
  	    while (state.g(a) == null) {
  	      // System.out.println("hap");
  	      // state.f() is null
  	      // System.out.println(states.indexOf(state) + " " + states.indexOf(state.f()));
  	      state = state.f();
  	      // System.out.println("py\n" + states.indexOf(state));
  	    }
  	    s.setF(state.g(a));
  	    s.addOutput(s.f().getOutputStrings());
  	  }
  	}
  }

  private void makeDeterministic() {
  	Queue<State> queue = new LinkedList<>();
  	// we skip setting move function because it is already goto
  	// this may be changed to support adding new keywords
  	Iterator i = states.get(0).getGIterator();
  	while (i.hasNext()) {
  	  queue.add((State) ((Map.Entry) i.next()).getValue());
  	}
  	// for each element in the queue
  	for (State r = queue.poll(); r != null; r = queue.poll()) {
	  // reuse i to save creating a new Iterator
	  i = r.getGIterator();
	  // for each e s.t. s != fail
	  while (i.hasNext()) {
	  	Map.Entry e = (Map.Entry) i.next();
        Character a = (Character) e.getKey();
        State s = (State) e.getValue();
        if (r.g(a) != null) {
          queue.add(s);
          r.addMove(a, s);
          // System.out.println("g(" + states.indexOf(r) + "," + a + ")=" + states.indexOf(s));
        }
        else {
          r.addMove(a, r.f().move(a));
          // System.out.println("g(" + states.indexOf(r) + "," + a + ")=" + states.indexOf(r.f().move(a)));
        }
	  }
  	}
  }

  public ArrayList<String> search(Character a) {
  	while (state.g(a) == null) {
  	  state = state.f();
  	}
  	state = state.g(a);
  	return state.getOutputStrings();
  }

  public ArrayList<String> search2(Character a) {
  	while (state.g(a) == null) {
  	  state = state.f();
  	}
  	state = state.g(a);
  	return state.getOutputStrings();
  }
}