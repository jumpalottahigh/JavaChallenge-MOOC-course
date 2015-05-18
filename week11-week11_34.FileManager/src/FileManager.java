
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    public List<String> read(String file) throws FileNotFoundException {
        
        Scanner fileReader = new Scanner(new File(file));
        ArrayList<String> list = new ArrayList<String>();
        
        while (fileReader.hasNextLine()){
            list.add(fileReader.nextLine());
        }
        fileReader.close();
        return list;
    }

    public void save(String file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    public void save(String file, List<String> texts) throws IOException {
        FileWriter writer = new FileWriter(file);
        
        for(String i : texts) {
            writer.write(i + "\n");
        }
        
        writer.close();
        
    }
}
