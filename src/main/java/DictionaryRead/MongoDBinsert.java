package DictionaryRead;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MongoDBinsert {


    public static void main(String[] args) {
        MongoClient mongoClient= new MongoClient();// localhost use no need to declare  URI
        MongoDatabase test= mongoClient.getDatabase("test");
        MongoCollection<Document> WordKeyWordValue=test.getCollection("dict");
        WordKeyWordValue.drop();
        int count = 0;
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\ASUS\\Downloads\\SolveThis.output");
            Scanner scan = new Scanner(file);
            Pattern p1 = Pattern.compile("(.+?(?==@))=.*");
            Pattern p2 = Pattern.compile("@(.+?(?=/|- |\\*))/?(.*?(?=/))?");
            Pattern p3 = Pattern.compile("- (.+?(?=- |=|$))");

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String WordKey;
                String Pronun = null;
                ArrayList<String> WordValue = new ArrayList<String>();
                ArrayList<String> Meaning = new ArrayList<String>();
                Matcher m1 = p1.matcher(line);
                Matcher m2 = p2.matcher(line);
                Matcher m3 = p3.matcher(line);
                Document Word = new Document();
                Word.put("_id",count);
                if (m1.find()) {
                    WordKey = m1.group(1).trim().replace(".", " ");
                    Word.put("Key",WordKey);
                }
                while (m2.find()) {
                    WordValue.add(m2.group(1).trim().replace(".", " "));
                    Word.put("Value",WordValue);
                    if ((m2.group(2) != null) && (Pronun == null)) {
                        Pronun = m2.group(2);
                        Word.put("Pronun",Pronun);
                    }
                }
                while (m3.find()) {
                    Meaning.add(m3.group(1).trim());
                }
                Word.put("Meaning",Meaning);

                WordKeyWordValue.insertOne(Word);
                count++;
            }

            scan.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        WordKeyWordValue.createIndex(new Document("Key","text"));
        mongoClient.close();
    }
}
