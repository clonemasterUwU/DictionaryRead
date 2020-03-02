package DictionaryRead;


import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.text
public class RegexToHash {
    static MultiValuedMap<String, String> WordKeyWordValue = new HashSetValuedHashMap<String, String>();
    static MultiValuedMap<String, String> WordValueMeaning = new HashSetValuedHashMap<String, String>();

    public RegexToHash(String FilePath) {
        try {
            FileInputStream file = new FileInputStream(FilePath);
            Scanner scan = new Scanner(file);
            Pattern p1 = Pattern.compile("([^=]*)(=)(.*)");
            //group 1: word
            //definition start with @...
            //group final(3): definition

            Pattern p2 = Pattern.compile("(@)(([^/*-]*)(/[^*]*)?(\\*)?([^-]*)([^@]*))");
            //group 3: word in english
            //group 4(may not exist): pronoun
            //group 6: type of word (N,V,Adj,Adv,...) in Vietnamese
            //group 7: meaning in Vietnamese + pairing example

            Pattern p3 = Pattern.compile("(-)([^=-]*)?([^-]*)");
            //group 2: meaning
            //group 3: pairing example

            //Pattern p4 = Pattern.compile("(=)([^=+]*)(\\+)([^=]*)");
            //group 2: eng example
            //group 4: vne example

            while (scan.hasNextLine()) {
                String s = scan.nextLine();

                Matcher m1 = p1.matcher(s);
                if (m1.find()) // find all definition in a row
                {
                    String WordKey = m1.group(1).trim();
                    String subs = m1.group(3);
                    // remove @
                    Matcher m2 = p2.matcher(subs);
                    while (m2.find()) {
                        String WordValue = m2.group(3).trim();
                        WordKeyWordValue.put(WordKey, WordValue);
                        //System.out.printf("%15s %15s\n",WordKey,WordValue);
                        String meaning_example = m2.group(7);
                        Matcher m3 = p3.matcher(meaning_example);
                        while (m3.find()) {
                            String Meaning = m3.group(2).trim();
                            //System.out.println(Meaning);
                            WordValueMeaning.put(WordValue, Meaning);
                        }
//                            String subsubsubs=m3.group(3);
//                            Matcher m4=p4.matcher(subsubsubs);
//                            while (m4.find()){
//
//
//                            }
//                        }
                    }
                }
            }

            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Collection<String> getMeaning(String word) {

        Collection <String> meaningcollection = new ArrayList<String>();
        Collection<String> wordvalue = WordKeyWordValue.get(word);
        if (wordvalue.isEmpty())
            return null;
        for (String s : wordvalue) {
            Collection<String> meaning = WordValueMeaning.get(s);
            meaningcollection.addAll(meaning);
        }
        return meaningcollection;
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        RegexToHash a = new RegexToHash("C:\\Users\\ASUS\\Downloads\\SolveThis.output");
        System.out.printf("Processed in ... %d ms\n",System.currentTimeMillis()-time);
        while (true) {
            Scanner scan = new Scanner(System.in);
            System.out.print("Word:   ");
            String wordkey = scan.nextLine().trim();
            Collection<String>meaning= RegexToHash.getMeaning(wordkey);
            if (meaning==null){
                System.out.println("Word not found ....");
                break;
            }
            else{
                for (String s: meaning){
                    System.out.println(s);
                }
            }
        }
    }
}
