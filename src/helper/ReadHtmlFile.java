package helper;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JLabel;

public class ReadHtmlFile {

    private double totIntEarned = 0d, totTaxDed = 0d;

    public HashMap getDataFromHtml(String bk_Cust_ID, JLabel lblstatus) {
        HashMap hm = new HashMap();
        try {
            bk_Cust_ID = bk_Cust_ID.replaceAll(" ", "").trim();
            File file = new File("d://htmlFile//" + bk_Cust_ID.trim() + ".html");

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Total Interest earned")) {
                    totIntEarned = Double.valueOf(line.split(":")[1].split(" ")[1]);
                    hm.put("totIntEarned", totIntEarned);
                }
                if (line.contains("Total Tax deducted")) {
                    totTaxDed = Double.valueOf(line.split(":")[1].split(" ")[1]);
                    hm.put("totTaxDed", totTaxDed);
                }
            }
            fileReader.close();
        } catch (IOException e) {
            lblstatus.setForeground(Color.RED);
            lblstatus.setText("[HTML-FILE MISSING]File or Folder(d:/htmlFile) not found. Bank Customer ID -" + bk_Cust_ID.trim());
        }
        return hm;
    }

}
