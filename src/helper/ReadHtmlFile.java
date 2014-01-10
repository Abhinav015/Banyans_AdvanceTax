package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ReadHtmlFile {

    private  double totIntEarned = 0d, totTaxDed = 0d;

    public HashMap getDataFromHtml(String bk_Cust_ID) {
        HashMap hm=new HashMap();
        try {
            File file = new File("d://htmlFile//"+bk_Cust_ID+".html");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Total Interest earned")) {
                    totIntEarned = Double.valueOf(line.split(":")[1].split(" ")[1]);
                    hm.put("totIntEarned",totIntEarned);
                }
                if (line.contains("Total Tax deducted")) {
                    totTaxDed = Double.valueOf(line.split(":")[1].split(" ")[1]);
                    hm.put("totTaxDed", totTaxDed);
                }
            }
            fileReader.close();
            System.out.println("Total Interest earned==>" + totIntEarned);
            System.out.println("Total Tax deducted==>" + totTaxDed);
        } catch (IOException e) {
            hm.put("totIntEarned",0);
             hm.put("totTaxDed", 0);
        }
       return hm;
    }

}
