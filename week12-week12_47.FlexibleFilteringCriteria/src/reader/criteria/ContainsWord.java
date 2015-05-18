package reader.criteria;

public class ContainsWord implements Criterion {

    private String word;

    public ContainsWord(String word) {
        this.word = word;
    }

    @Override
    public boolean complies(String line) {
        return line.contains(word);
    }


}
