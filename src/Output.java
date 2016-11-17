public class Output {
  private String match;
  private int index;

  public Output(String match) {
  	this.match = match;
  }

  public Output(String match, int index) {
  	this(match);
  	this.index = index;
  }
}