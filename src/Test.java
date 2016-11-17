import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Test {
  public static void main(String[] argsp) {
  	help();
  	newCommand();

  	Set<File> files = new HashSet<>();

  	Scanner input = new Scanner(System.in);
  	String[] args;
  	while (!(args = input.nextLine().split(" "))[0].equals("exit")) {
  	  switch (args[0]) {
  	   case "load":
  	    for (int i = 1; i < args.length; ++i) {
  	      File file = new File(args[i]);
  	      if (file.exists()) {
  	     	files.add(file);
  	      }
  	    }
  	    System.out.println(files);
  	    break;
  	   case "unload":
  	   	for (int i = 1; i < args.length; ++i) {
     	  files.remove(new File(args[i]));
  	   	}
  	   	System.out.println(files);
  	   	break;
  	   case "query":
  	    query(files, args);
  	    break;
  	   case "help":
  	    help();
  	    break;
  	  }
  	  newCommand();
  	}
  }

  // reusable print for giving directions
  private static void help() {
	System.out.print(String.join("\n",
	"load [file1] [file2] ...",
	"unload [file1] [file2] ...",
	"query [keyword1] [keyword2] ...",
	"help",
	"exit\n"));
  }

  private static void query(Set<File> files, String[] patterns) {
    Trie trie = new Trie(patterns);
    ArrayList<Result> results = new ArrayList<>(files.size());
    Reader reader;
    // used to keep track of index of results being added
    for (File file : files) {
      // set trie back to initial state
      trie.reset();
      try {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        ArrayList<Match> matches = new ArrayList<>();
        // stores int read from file
        int symbol;
        // keeps track of row
        int row = 1;
        int column = 1;
        int character = 1;
        while ((symbol = reader.read()) != -1) {
      	  Character c = (Character) (char) symbol;
      	  ArrayList<String> outputs = trie.search(c);
      	  for (String output : outputs) {
      	    if (!outputs.isEmpty()) {
      	  	  matches.add(new Match(output, row, column-output.length()+1, character-output.length()));
      	    }
      	  }
      	  ++character;
      	  // increment row and reset column if newline
      	  if (c == '\n') {
      	  	++row;
      	  	column = 1;
      	  }
      	  // if same row, move column over
      	  else {
      	  	++column;
      	  }
        }
        results.add(new Result(file.getPath(), matches));
      } catch (IOException e) {};
      // moving on to next file
    }
	
	Collections.sort(results);
	for (Result result : results) {
	  System.out.println(result);
	}
	results.get(results.size()-1).printMatches();
  }

  // makes entering commands look nice
  private static void newCommand() {
  	System.out.print("\n> ");
  }
}