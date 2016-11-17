import java.util.ArrayList;

public class Result implements Comparable<Result> {
  protected ArrayList<Match> matches;
  protected String name;
  protected int score;

  public Result(String name, ArrayList<Match> matches) {
    this.name = name;
    this.matches = matches;
    setScore();
  }

  // score Result
  private void setScore() {
    score = matches.size();
    // first, compare number of matches
    // arbitrary weight value of 100
    // int comp = 100 * matches.size();
    // if (comp == 0) {
    //   // arbitrary weight value of 10
    //   comp += 10 * matches.get(0).getCharacter();
    //   if (comp == 0) {
    //     int nameComp = name.compareTo(other.name);
    //     if (nameComp > 0) {
    //       score = ++comp;
    //       return;
    //     }
    //     else if (nameComp < 0) {
    //       score = --comp;
    //       return;
    //     }
    //     score = comp;
    //     return;
    //   }
    //   else {
    //     score = comp;
    //     return;
    //   }
    // }
    // score = comp;
  }

  public void printMatches() {
    System.out.println(matches);
  }

  @Override
  public String toString() {
  	return "name: "+name+"\nscore: "+score+"\n";
  }

  @Override
  public int compareTo(Result other) {
    return score - other.score;
  }
}