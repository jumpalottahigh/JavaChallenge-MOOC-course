package reader;

import reader.criteria.Criterion;
import reader.criteria.ContainsWord;

public class Main {

    public static void main(String[] args) {
        String address = "http://www.gutenberg.myebook.bg/2/5/5/2554/2554-8.txt";
        GutenbergReader book = new GutenbergReader(address);

        Criterion criterion = new ContainsWord("beer");

        for (String line : book.linesWhichComplyWith(criterion)) {
            System.out.println(line);
        }
    }
}
