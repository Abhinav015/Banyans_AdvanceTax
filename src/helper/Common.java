package helper;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pratikmac
 */
public class Common {

    ReadHtmlFile rhf = new ReadHtmlFile();

    private static String GetFileExtension(String fname2) {
        String fileName = fname2;
        String fname = "";
        String ext = "";
        int mid = fileName.lastIndexOf(".");
        fname = fileName.substring(0, mid);
        ext = fileName.substring(mid + 1, fileName.length());
        return ext;
    }

    public HashMap getImportExcelData(String filename, String docTempFile) throws IOException {
        HashMap Hm = new HashMap();
        HashMap getname = new HashMap();
        ArrayList clientName = new ArrayList();
        HashMap hmDocTemNames = (HashMap) getImportExcelData2(docTempFile);
        ArrayList docTempDet = (ArrayList) hmDocTemNames.get("docTempDet");
        HashMap getNames = (HashMap) hmDocTemNames.get("getNames");

        String fileExtn = GetFileExtension(filename);
        InputStream file = new FileInputStream(filename);
        Workbook wb_xssf;
        Workbook wb_hssf;
        Sheet sheet = null;
        if (fileExtn.equalsIgnoreCase("xlsx")) {
            wb_xssf = new XSSFWorkbook(file);
            sheet = wb_xssf.getSheetAt(0);
        }
        if (fileExtn.equalsIgnoreCase("xls")) {
            POIFSFileSystem fs = new POIFSFileSystem(file);
            wb_hssf = new HSSFWorkbook(fs);
            sheet = wb_hssf.getSheetAt(0);
        }

        int tempCount = 0;
        try {
            Iterator<Row> tempIteraotr22 = sheet.rowIterator();
            boolean checkHearderName = true;
            while (tempIteraotr22.hasNext()) {
                Row row22 = tempIteraotr22.next();

                if (row22.getRowNum() == 0) {
                    if (!row22.getCell(0).toString().equalsIgnoreCase("Sno")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(1).toString().trim().equalsIgnoreCase("Bank Customer ID")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(2).toString().trim().equalsIgnoreCase("Name")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(3).toString().trim().equalsIgnoreCase("Field manager")) {
                        System.out.println("Field manager");
                        checkHearderName = false;
                    } else if (!row22.getCell(4).toString().trim().equalsIgnoreCase("Salutation")) {
                        System.out.println("Salutation");
                        checkHearderName = false;
                    } else if (!row22.getCell(5).toString().trim().equalsIgnoreCase("Email")) {
                        System.out.println("Email");
                        checkHearderName = false;
                    } else if (!row22.getCell(6).toString().trim().equalsIgnoreCase("Fund manager email id")) {
                        System.out.println("Fund manager email id");
                        checkHearderName = false;
                    } else if (!row22.getCell(7).toString().trim().equalsIgnoreCase("MobileNo")) {
                        System.out.println("MobileNo");
                        checkHearderName = false;
                    } else if (row22.getCell(8) != null) {
                        checkHearderName = false;
                        System.out.println("extra row" + row22.getCell(7));
                    }
                } else {
                    break;
                }
            }
            if (checkHearderName == true) {
                Iterator<Row> tempIteraotr = sheet.rowIterator();
                List<BanyanClientsBean> lst = new ArrayList<BanyanClientsBean>();
                List data = new ArrayList();
                HashMap extraData = new HashMap();
                int j = 1;
                while (tempIteraotr.hasNext()) {
                    Row row = tempIteraotr.next();
                    tempCount++;
                    boolean isRowValid = true;
                    if ((row.getCell(0) == null || row.getCell(0).toString().trim().length() == 0)
                            && (row.getCell(1) == null || row.getCell(1).toString().trim().length() == 0)
                            && (row.getCell(2) == null || row.getCell(2).toString().trim().length() == 0)
                            && (row.getCell(3) == null || row.getCell(3).toString().trim().length() == 0)
                            && (row.getCell(4) == null || row.getCell(4).toString().trim().length() == 0)
                            && (row.getCell(5) == null || row.getCell(5).toString().trim().length() == 0)
                            && (row.getCell(6) == null || row.getCell(6).toString().trim().length() == 0)
                            && (row.getCell(7) == null || row.getCell(7).toString().trim().length() == 0)) {
                        isRowValid = false;
                        tempCount--;
                    }
                    if (row.getRowNum() == 0 || !isRowValid) {
                        continue;
                    }

                    if (docTempDet.contains(row.getCell(2).toString().trim().replaceAll(" ", "")) && isUpperCase(row.getCell(2).toString().trim())) {
                        BanyanClientsBean tempBean = new BanyanClientsBean();
                        row.getCell(0).setCellType((Cell.CELL_TYPE_STRING));
                        row.getCell(1).setCellType((Cell.CELL_TYPE_STRING));

                        clientName.add(row.getCell(2).toString().trim().replaceAll(" ", ""));

                        tempBean.setSno(String.valueOf(j));
                        tempBean.setBanId(row.getCell(1).toString().replace(String.valueOf((char) 160), " ").trim());
                        tempBean.setName(row.getCell(2).toString().trim());
                        tempBean.setFieldManager(row.getCell(3).toString().trim());
                        tempBean.setSalutation(row.getCell(4).toString().trim());
                        tempBean.setEmail(row.getCell(5).toString().trim());
                        tempBean.setFmEmailId(row.getCell(6).toString().trim());
                        tempBean.setMobNo((long) row.getCell(7).getNumericCellValue());
                        tempBean.setChk(Boolean.TRUE);
                        extraData.put(row.getCell(2).toString().trim().replaceAll(" ", ""), tempBean);

                        data.add(String.valueOf(j));
                        data.add(row.getCell(1).toString().trim());
                        data.add(row.getCell(2).toString().trim());
                        data.add(row.getCell(5).toString().trim());
                        data.add(tempBean.isChk());

                        lst.add(tempBean);
                        j++;
                    } else if (docTempDet.contains(row.getCell(2).toString().trim().replaceAll(" ", "")) && isLowerCase(row.getCell(2).toString().trim())) {
                        BanyanClientsBean tempBean = new BanyanClientsBean();
                        row.getCell(0).setCellType((Cell.CELL_TYPE_STRING));
                        String Id = "";
                        clientName.add(row.getCell(2).toString().trim().replaceAll(" ", ""));

                        tempBean.setSno(String.valueOf(j));
                        if (row.getCell(1) == null) {
                            Id = "-";
                        } else {
                            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                            Id = row.getCell(1).toString().replace(String.valueOf((char) 160), " ").trim();;
                        }
                        tempBean.setBanId(Id);
                        tempBean.setName(row.getCell(2).toString().trim());
                        tempBean.setFieldManager(row.getCell(3).toString().trim());
                        tempBean.setSalutation(row.getCell(4).toString().trim());
                        tempBean.setEmail(row.getCell(5).toString().trim());
                        tempBean.setFmEmailId(row.getCell(6).toString().trim());
                        tempBean.setMobNo((long) row.getCell(7).getNumericCellValue());
                        tempBean.setChk(Boolean.TRUE);
                        extraData.put(row.getCell(2).toString().trim().replaceAll(" ", ""), tempBean);

                        data.add(String.valueOf(j));
                        data.add(Id);
                        data.add(row.getCell(2).toString().trim());
                        data.add(row.getCell(5).toString().trim());
                        data.add(tempBean.isChk());

                        lst.add(tempBean);
                        j++;
                    }

                }
                ArrayList extra = new ArrayList(clientName);
                extra.addAll(docTempDet);
                extra.removeAll(clientName);
                Hm.put("getname", getNames);
                Hm.put("errList", extra);
                Hm.put("list", lst);
                Hm.put("temcount", tempCount - 1);
                Hm.put("data", data);
                Hm.put("extraData", extraData);
            }

        } catch (Exception ee) {
            System.out.println("//[Exception] in getImportExcelData() in while-" + ee);
        } finally {
            file.close();
        }

        return Hm;
    }

    public HashMap getImportExcelData1(String filename, String clientFileName, JLabel lblstatus) throws IOException {
        HashMap hmClientData = getImportExcelData(clientFileName, filename);
        HashMap hm = (HashMap) hmClientData.get("extraData");
        HashMap Hm = new HashMap();
        String fileExtn = GetFileExtension(filename);
        InputStream file = new FileInputStream(filename);
        Workbook wb_xssf;
        Workbook wb_hssf;
        Sheet sheet = null;
        if (fileExtn.equalsIgnoreCase("xlsx")) {
            wb_xssf = new XSSFWorkbook(file);
            sheet = wb_xssf.getSheetAt(0);
        }
        if (fileExtn.equalsIgnoreCase("xls")) {
            POIFSFileSystem fs = new POIFSFileSystem(file);
            wb_hssf = new HSSFWorkbook(fs);
            sheet = wb_hssf.getSheetAt(0);
        }

        int tempCount = 0;
        try {
            Iterator<Row> tempIteraotr22 = sheet.rowIterator();
            boolean checkHearderName = true;
            while (tempIteraotr22.hasNext()) {
                Row row22 = tempIteraotr22.next();

                if (row22.getRowNum() == 0) {
                    if (!row22.getCell(0).toString().trim().equalsIgnoreCase("Sl")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(1).toString().trim().equalsIgnoreCase("Client Name")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(2).toString().trim().equalsIgnoreCase("STATUS OF THE CLIENT")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(3).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss Equity")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(4).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss on MF")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(5).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss on Derivatives")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(6).toString().trim().equalsIgnoreCase("Total Short  Term Capital Gain/Loss")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(7).toString().trim().equalsIgnoreCase("Bank Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(8).toString().trim().equalsIgnoreCase("FD Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(9).toString().trim().equalsIgnoreCase("Total Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(10).toString().trim().equalsIgnoreCase("TDS On Bank Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(11).toString().trim().equalsIgnoreCase("TDS on FD Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(12).toString().trim().equalsIgnoreCase("TDS on Sale Proceeds")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(13).toString().trim().equalsIgnoreCase("Total Tax Deducted at Source")) {
                        checkHearderName = false;
                    } else if (row22.getCell(14) != null) {
                        checkHearderName = false;
                        System.out.println("extra row" + row22.getCell(14));
                    }
                } else {
                    break;
                }
            }
            if (checkHearderName == true) {
                Iterator<Row> tempIteraotr = sheet.rowIterator();
                HashMap lst = new HashMap();

                while (tempIteraotr.hasNext()) {
                    Row row = tempIteraotr.next();
                    tempCount++;
                    boolean isRowValid = true;
                    if ((row.getCell(0) == null || row.getCell(0).toString().trim().length() == 0)
                            && (row.getCell(1) == null || row.getCell(1).toString().trim().length() == 0)
                            && (row.getCell(2) == null || row.getCell(2).toString().trim().length() == 0)
                            && (row.getCell(3) == null || row.getCell(3).toString().trim().length() == 0)
                            && (row.getCell(4) == null || row.getCell(4).toString().trim().length() == 0)
                            && (row.getCell(5) == null || row.getCell(5).toString().trim().length() == 0)
                            && (row.getCell(6) == null || row.getCell(6).toString().trim().length() == 0)
                            && (row.getCell(7) == null || row.getCell(7).toString().trim().length() == 0)
                            && (row.getCell(8) == null || row.getCell(8).toString().trim().length() == 0)
                            && (row.getCell(9) == null || row.getCell(9).toString().trim().length() == 0)
                            && (row.getCell(10) == null || row.getCell(10).toString().trim().length() == 0)
                            && (row.getCell(11) == null || row.getCell(11).toString().trim().length() == 0)
                            && (row.getCell(12) == null || row.getCell(12).toString().trim().length() == 0)
                            && (row.getCell(13) == null || row.getCell(13).toString().trim().length() == 0)) {
                        isRowValid = false;
                        tempCount--;
                    }
                    if (row.getRowNum() == 0 || !isRowValid) {
                        continue;
                    }

                    BanyanDocTempBean tempBean = new BanyanDocTempBean();
                    row.getCell(0).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(1).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(2).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(3).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(4).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(5).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(6).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(7).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(8).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(9).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(10).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(11).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(12).setCellType((Cell.CELL_TYPE_STRING));
                    row.getCell(13).setCellType((Cell.CELL_TYPE_STRING));

                    tempBean.setSl(row.getCell(0).toString().trim());
                    tempBean.setClientName(row.getCell(1).toString().trim());
                    BanyanClientsBean bcb = (BanyanClientsBean) hm.get(row.getCell(1).toString().trim().replaceAll(" ", ""));

                    tempBean.setClientStat(row.getCell(2).toString().trim());
                    tempBean.setSht_trm_Cap_Gain_Loss_Eq(getFormatedAmount(row.getCell(3).toString().trim()));
                    tempBean.setSht_trm_Cap_Gain_Loss_Mf(getFormatedAmount(row.getCell(4).toString().trim()));
                    tempBean.setSht_trm_Cap_Gain_Loss_Der(getFormatedAmount(row.getCell(5).toString().trim()));
                    String tot_Short_TrmCap_Gain_Loss = String.valueOf(Double.parseDouble(row.getCell(3).toString().trim()) + Double.parseDouble(row.getCell(4).toString().trim()) + Double.parseDouble(row.getCell(5).toString().trim()));
                    tempBean.setTot_Short_TrmCap_Gain_Loss(getFormatedAmount(tot_Short_TrmCap_Gain_Loss));
                    tempBean.setBnk_Int(getFormatedAmount(row.getCell(7).toString().trim()));

                    String bkId = "";
                    try {
                        bkId = bcb.getBanId().replace(String.valueOf((char) 160), " ").trim();
                    } catch (NullPointerException npe) {
                        lblstatus.setForeground(Color.red);
                        lblstatus.setText(row.getCell(1).toString().trim() + " does not found in Client CSV File.");
                        npe.printStackTrace();
                    }
                    HashMap data = new HashMap();
                    if (!bkId.toString().trim().equals("-")) {
                        data = rhf.getDataFromHtml(bkId.toString().trim(), lblstatus);
                    } else {
                        data.put("totIntEarned", Double.valueOf("0"));
                        data.put("totTaxDed", Double.valueOf("0"));
                    }

                    if (tempBean.getClientStat().equals("DOMESTIC")) {
                        tempBean.setfDIntst(getFormatedAmount(data.get("totIntEarned").toString().trim()));
                    } else {
                        tempBean.setfDIntst(getFormatedAmount("0"));
                    }

                    String totIntrst = "";
                    if (tempBean.getClientStat().equals("DOMESTIC")) {
                        totIntrst = String.valueOf(Double.valueOf(row.getCell(7).toString().trim()) + Double.valueOf(data.get("totIntEarned").toString()));
                    } else {
                        totIntrst = String.valueOf(Double.valueOf(row.getCell(7).toString().trim()) + Double.valueOf("0"));
                    }
                    tempBean.setTotIntrst(getFormatedAmount(totIntrst));
                    tempBean.setTds_Bank_Intrst(getFormatedAmount(row.getCell(10).toString().trim()));

                    if (tempBean.getClientStat().equals("DOMESTIC")) {
                        tempBean.setTds_FD_Interst(getFormatedAmount(data.get("totTaxDed").toString().trim()));
                    } else {
                        tempBean.setTds_FD_Interst(getFormatedAmount("0"));
                    }

                    String tds_On_sales_Proceeds = "";
                    if (row.getCell(12) == null || row.getCell(12).toString().trim().equals("")) {
                        tds_On_sales_Proceeds = "0";
                    } else {
                        tds_On_sales_Proceeds = getFormatedAmount(row.getCell(12).toString().trim());
                    }

                    tempBean.setTds_Sale_Proceeds(tds_On_sales_Proceeds);
                    if (tempBean.getClientStat().equals("DOMESTIC")) {
                        tempBean.setTot_Tax_Ded_Source(tempBean.getTds_FD_Interst());
                    } else {
                        String tot_Tax_Ded_Source = String.valueOf(Double.valueOf(row.getCell(10).toString().trim()) + Double.valueOf(row.getCell(12).toString().trim()));
                        tempBean.setTot_Tax_Ded_Source(getFormatedAmount(tot_Tax_Ded_Source));
                    }

                    tempBean.setBkId(bkId);
                    tempBean.setFieldManager(bcb.getFieldManager());
                    tempBean.setSalutation(bcb.getSalutation());
                    tempBean.setEmalId(bcb.getEmail());
                    tempBean.setFieldManagerEmail(bcb.getFmEmailId());
                    tempBean.setMobileNo(bcb.getMobNo());

                    lst.put(tempBean.getClientName().replaceAll(" ", ""), tempBean);
                }
                Hm.put("clientDet", lst);
                Hm.put("temcount", tempCount - 1);
            }

        } catch (Exception ee) {
        } finally {
            file.close();
        }

        return Hm;
    }

    public String getFormatedAmount(String amt) {
        try {
            if (amt.equals("0")) {
                amt = "-";
            } else if (amt.equals("0.0")) {
                amt = "-";
            } else if (Long.valueOf(amt) < 0) {
                amt = "-" + Math.abs(Long.valueOf(amt));
            } else {
                long amnt = Long.parseLong(amt);
                NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("EN", "IN"));
                amt = numberFormatter.format(amnt);
            }

        } catch (NumberFormatException ex) {
            if (Double.valueOf(amt).longValue() < 0) {
                NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("EN", "IN"));
                amt = "-" + numberFormatter.format((long) Math.abs(Math.round(Double.valueOf(amt))));
            } else {
                NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("EN", "IN"));
                amt = numberFormatter.format((long) Math.abs(Math.round(Double.valueOf(amt))));
            }

        }

        return amt;
    }

    public HashMap getImportExcelData2(String filename) throws IOException {
        HashMap Hm = new HashMap();
        HashMap getNames = new HashMap();
        ArrayList lst = new ArrayList();
        String fileExtn = GetFileExtension(filename);
        InputStream file = new FileInputStream(filename);
        Workbook wb_xssf;
        Workbook wb_hssf;
        Sheet sheet = null;
        if (fileExtn.equalsIgnoreCase("xlsx")) {
            wb_xssf = new XSSFWorkbook(file);
            sheet = wb_xssf.getSheetAt(0);
        }
        if (fileExtn.equalsIgnoreCase("xls")) {
            POIFSFileSystem fs = new POIFSFileSystem(file);
            wb_hssf = new HSSFWorkbook(fs);
            sheet = wb_hssf.getSheetAt(0);
        }

        int tempCount = 0;
        try {
            Iterator<Row> tempIteraotr22 = sheet.rowIterator();
            boolean checkHearderName = true;
            while (tempIteraotr22.hasNext()) {
                Row row22 = tempIteraotr22.next();

                if (row22.getRowNum() == 0) {
                    if (!row22.getCell(0).toString().trim().equalsIgnoreCase("Sl")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(1).toString().trim().equalsIgnoreCase("Client Name")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(2).toString().trim().equalsIgnoreCase("STATUS OF THE CLIENT")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(3).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss Equity")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(4).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss on MF")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(5).toString().trim().equalsIgnoreCase("Short Term Capital Gain/Loss on Derivatives")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(6).toString().trim().equalsIgnoreCase("Total Short  Term Capital Gain/Loss")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(7).toString().trim().equalsIgnoreCase("Bank Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(8).toString().trim().equalsIgnoreCase("FD Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(9).toString().trim().equalsIgnoreCase("Total Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(10).toString().trim().equalsIgnoreCase("TDS On Bank Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(11).toString().trim().equalsIgnoreCase("TDS on FD Interest")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(12).toString().trim().equalsIgnoreCase("TDS on Sale Proceeds")) {
                        checkHearderName = false;
                    } else if (!row22.getCell(13).toString().trim().equalsIgnoreCase("Total Tax Deducted at Source")) {
                        checkHearderName = false;
                    } else if (row22.getCell(14) != null) {
                        checkHearderName = false;
                        System.out.println("extra row" + row22.getCell(14));
                    }
                } else {
                    break;
                }
            }
            if (checkHearderName == true) {
                Iterator<Row> tempIteraotr = sheet.rowIterator();

                while (tempIteraotr.hasNext()) {
                    Row row = tempIteraotr.next();
                    tempCount++;
                    boolean isRowValid = true;
                    if ((row.getCell(0) == null || row.getCell(0).toString().trim().length() == 0)
                            && (row.getCell(1) == null || row.getCell(1).toString().trim().length() == 0)
                            && (row.getCell(2) == null || row.getCell(2).toString().trim().length() == 0)
                            && (row.getCell(3) == null || row.getCell(3).toString().trim().length() == 0)
                            && (row.getCell(4) == null || row.getCell(4).toString().trim().length() == 0)
                            && (row.getCell(5) == null || row.getCell(5).toString().trim().length() == 0)
                            && (row.getCell(6) == null || row.getCell(6).toString().trim().length() == 0)
                            && (row.getCell(7) == null || row.getCell(7).toString().trim().length() == 0)
                            && (row.getCell(8) == null || row.getCell(8).toString().trim().length() == 0)
                            && (row.getCell(9) == null || row.getCell(9).toString().trim().length() == 0)
                            && (row.getCell(10) == null || row.getCell(10).toString().trim().length() == 0)
                            && (row.getCell(11) == null || row.getCell(11).toString().trim().length() == 0)
                            && (row.getCell(12) == null || row.getCell(12).toString().trim().length() == 0)
                            && (row.getCell(13) == null || row.getCell(13).toString().trim().length() == 0)) {
                        isRowValid = false;
                        tempCount--;
                    }
                    if (row.getRowNum() == 0 || !isRowValid) {
                        continue;
                    }
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    getNames.put(row.getCell(1).toString().trim().replaceAll(" ", ""), row.getCell(1).toString().trim());
                    lst.add(row.getCell(1).toString().trim().replaceAll(" ", ""));
                }
                Hm.put("docTempDet", lst);
                Hm.put("getNames", getNames);
                Hm.put("temcount", tempCount - 1);
            }

        } catch (Exception ee) {
        } finally {
            file.close();
        }

        return Hm;
    }

    public static boolean isUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLowerCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 65 && c <= 90) {
                return false;
            }
        }
        return true;
    }

}
