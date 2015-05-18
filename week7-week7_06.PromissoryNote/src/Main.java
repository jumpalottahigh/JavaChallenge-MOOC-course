public class Main {
    public static void main(String[] args) {
        // Test your program here
        PromissoryNote mattisNote = new PromissoryNote();
  mattisNote.setLoan("Arto", 51.5);
  mattisNote.setLoan("Mikael", 30);

  System.out.println(mattisNote.howMuchIsTheDebt("Arto"));
  System.out.println(mattisNote.howMuchIsTheDebt("Joel"));
    }
}
