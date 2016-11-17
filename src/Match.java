public class Match {
  private String text;
  // row, column, character of final character
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

  public int getCharacter() {
  	return character;
  }
}