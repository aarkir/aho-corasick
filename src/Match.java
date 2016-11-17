public class Match {
  private String text;
  private int row;
  private int column;
  private int character;

  public Match(String text, int row, int column, int character) {
  	this.text = text;
  	this.row = row;
  	this.column = column;
  	this.character = character;
  }

  @Override
  public String toString() {
  	return ""+row+":"+column+" "+text;
  }
}