package DictionaryRead;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.print.Doc;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

public class MongoDBquery {
    public static void main(String[] args) {
        MongoClient mongoClient= new MongoClient();// localhost use no need to declare  URI
        MongoDatabase test= mongoClient.getDatabase("test");
        MongoCollection<Document> dict = test.getCollection("dict");
        while(true) {
            Scanner scan = new Scanner(System.in);
            System.out.print("Word (type C to break):   ");
            String word = scan.nextLine().trim();

            if (word.equals("C"))break;

            FindIterable<Document> result = dict.find(new Document("Key", word));
            Iterator<Document> it = result.iterator();
            if (it.hasNext()) {
                Document returnvalue = it.next();
                Object pronun = returnvalue.get("Pronun");
                System.out.println("Pronunciation:    " + pronun);
                Collection<Document> meaning = (Collection<Document>) returnvalue.get("Meaning");
                for (Object s : meaning) {
                    System.out.println(s);
                }
            } else {
                System.out.println("No word found...");
                Iterator<Document> possible = dict.find(Filters.regex("Key", word)).iterator();
                if (!possible.hasNext()) continue;
                int count = 0;
                System.out.println("You may mean:");
                while (possible.hasNext() && count < 10) {
                    System.out.println(possible.next().get("Key"));
                    count++;
                }
            }
        }
    }
}
