package repository;

import java.io.*;

public class FileStorage implements Repository<String>{

    @Override
    public void save(String text) {
        try(BufferedWriter fw =new BufferedWriter(new FileWriter("src/main/java/test.txt", true))){
            fw.write(text);
            fw.newLine();
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String load() {
        StringBuilder sb = new StringBuilder();
        try( BufferedReader fr = new BufferedReader(new FileReader("src/main/java/test.txt"))){
            String line;
            while((line = fr.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(sb);
    }
}
