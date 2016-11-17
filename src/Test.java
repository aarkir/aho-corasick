public class Test {
  public static void main(String[] args) {
    final String[] patterns = {"he", "she", "his", "hers"};
    final String test = "ushers";

    Trie trie = new Trie(patterns);
    
    for (int i = 0; i < test.length(); ++i) {
      System.out.println(test.charAt(i) + " " + trie.search(test.charAt(i)));
    }
  }
}