import java.util.ArrayList;

public class Result implements Comparable<Result> {
  protected ArrayList<Match> matches;
  private int score;

  public Result(ArrayList<Match> matches) {
    this.matches = matches;
    setScore();
  }

  // if score should be overriden for some reason
  public Result(ArrayList<Match> matches, int score) {
    this(matches);
    this.score = score;
  }

  // score Result
  private void setScore() {
    score = matches.size();
  }

  @Override
  public String toString() {
  	return "score: "+score+"\n"+matches;
  }

  @Override
  public int compareTo(Result other) {
    return matches.size() - other.matches.size();
  }
}