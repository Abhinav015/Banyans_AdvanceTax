package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadHtmlFile {

    private static double totIntEarned = 0d, totTaxDed = 0d;

    public static void main(String[] args) {
        try {
            File file = new File("d://htmlFile//12345678.html");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Total Interest earned")) {
                    totIntEarned = Double.valueOf(line.split(":")[1].split(" ")[1]);
                }
                if (line.contains("Total Tax deducted")) {
                    totTaxDed = Double.valueOf(line.split(":")[1].split(" ")[1]);
                }
            }
            fileReader.close();
            System.out.println("Total Interest earned==>" + totIntEarned);
            System.out.println("Total Tax deducted==>" + totTaxDed);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
