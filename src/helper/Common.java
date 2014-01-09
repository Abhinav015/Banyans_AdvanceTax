/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pratikmac
 */
public class Common {

    private static String GetFileExtension(String fname2) {
        String fileName = fname2;
        String fname = "";
        String ext = "";
        int mid = fileName.lastIndexOf(".");
        fname = fileName.substring(0, mid);
        ext = fileName.substring(mid + 1, fileName.length());
        return ext;
    }

    public HashMap getImportExcelData(String filename) throws IOException {
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
        System.out.println("getImportExcelData()");

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
                List extraData = new ArrayList();
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

                    BanyanClientsBean tempBean = new BanyanClientsBean();

                    tempBean.setSno(row.getCell(0).toString().trim());
                    tempBean.setBanId((long) row.getCell(1).getNumericCellValue());
                    tempBean.setName(row.getCell(2).toString().trim());
                    tempBean.setFieldManager(row.getCell(3).toString().trim());
                    tempBean.setSalutation(row.getCell(4).toString().trim());
                    tempBean.setEmail(row.getCell(5).toString().trim());
                    tempBean.setFmEmailId(row.getCell(6).toString().trim());
                    tempBean.setMobNo((long) row.getCell(7).getNumericCellValue());
                    tempBean.setChk(Boolean.TRUE);

                    data.add((long) row.getCell(0).getNumericCellValue());
                    data.add((long) row.getCell(1).getNumericCellValue());
                    data.add(row.getCell(2).toString().trim());
                    data.add(row.getCell(5).toString().trim());
                    data.add(tempBean.isChk());

                    extraData.add((long) row.getCell(1).getNumericCellValue());
                    extraData.add(row.getCell(3).toString().trim());
                    extraData.add(row.getCell(4).toString().trim());
                    extraData.add(row.getCell(5).toString().trim());
                    extraData.add(row.getCell(6).toString().trim());
                    extraData.add((long) row.getCell(7).getNumericCellValue());

                    lst.add(tempBean);

                }
                Hm.put("list", lst);
                Hm.put("temcount", tempCount - 1);
                Hm.put("data", data);
                Hm.put("extraData", extraData);
            }

        } catch (Exception ee) {
            System.out.println("Exception in getImportExcelData() in while-" + ee);
            ee.printStackTrace();
        } finally {
            file.close();
        }

        return Hm;
    }

    public HashMap getImportExcelData1(String filename, String clientFileName) throws IOException {
        HashMap hmClientData = getImportExcelData(clientFileName);
        ArrayList<BanyanClientsBean> ls = (ArrayList) hmClientData.get("extraData");
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
        System.out.println("getImportExcelData()");

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
                List<BanyanDocTempBean> lst = new ArrayList<BanyanDocTempBean>();
                Iterator itr = ls.iterator();
                while (tempIteraotr.hasNext() && itr.hasNext()) {
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
                    System.out.println("Getting information for doc templete excel ...........");

                    tempBean.setSl(row.getCell(0).toString().trim());
                    System.out.println("Sl===>" + tempBean.getSl());
                    tempBean.setClientName(row.getCell(1).toString().trim());
                    System.out.println("ClientName===>" + tempBean.getClientName());
                    tempBean.setClientStat(row.getCell(2).toString().trim());
                    System.out.println("ClientStat===>" + tempBean.getClientStat());
                    tempBean.setSht_trm_Cap_Gain_Loss_Eq((long) row.getCell(3).getNumericCellValue());
                    System.out.println("Sht_trm_Cap_Gain_Loss_Eq===>" + tempBean.getSht_trm_Cap_Gain_Loss_Eq());
                    tempBean.setSht_trm_Cap_Gain_Loss_Mf((long) row.getCell(4).getNumericCellValue());
                    System.out.println("Sht_trm_Cap_Gain_Loss_Mf===>" + tempBean.getSht_trm_Cap_Gain_Loss_Mf());
                    tempBean.setSht_trm_Cap_Gain_Loss_Der((long) row.getCell(5).getNumericCellValue());
                    System.out.println("Sht_trm_Cap_Gain_Loss_Der===>" + tempBean.getSht_trm_Cap_Gain_Loss_Der());
                    tempBean.setTot_Short_TrmCap_Gain_Loss((long) row.getCell(6).getNumericCellValue());
                    System.out.println("Tot_Short_TrmCap_Gain_Loss===>" + tempBean.getTot_Short_TrmCap_Gain_Loss());
                    tempBean.setBnk_Int((long) row.getCell(7).getNumericCellValue());
                    System.out.println("Bnk_Int===>" + tempBean.getBnk_Int());
                    tempBean.setfDIntst((long) row.getCell(8).getNumericCellValue());
                    System.out.println("fDIntst===>" + tempBean.getfDIntst());
                    tempBean.setTotIntrst((long) tempBean.getBnk_Int() + tempBean.getfDIntst());
                    System.out.println("TotIntrst===>" + tempBean.getTotIntrst());
                    tempBean.setTds_Bank_Intrst((long) row.getCell(10).getNumericCellValue());
                    System.out.println("Tds_Bank_Intrst===>" + tempBean.getTds_Bank_Intrst());
                    tempBean.setTds_FD_Interst((long) row.getCell(11).getNumericCellValue());
                    System.out.println("Tds_FD_Interst===>" + tempBean.getTds_FD_Interst());
                    tempBean.setTds_Sale_Proceeds((long) row.getCell(12).getNumericCellValue());
                    System.out.println("Tds_Sale_Proceeds===>" + tempBean.getTds_Sale_Proceeds());
                    if (tempBean.getClientStat().equals("DOMESTIC")) {
                        tempBean.setTot_Tax_Ded_Source(tempBean.getTds_FD_Interst());
                    } else {
                        tempBean.setTot_Tax_Ded_Source(tempBean.getTds_Bank_Intrst() + tempBean.getTds_Sale_Proceeds());
                    }
                    System.out.println("Tot_Tax_Ded_Source===>" + tempBean.getTot_Tax_Ded_Source());

                    tempBean.setBkId((long) itr.next());
                    System.out.println("BkId===>" + tempBean.getBkId());
                    tempBean.setFieldManager(itr.next().toString().trim());
                    System.out.println("FieldManager===>" + tempBean.getFieldManager());
                    tempBean.setSalutation(itr.next().toString().trim());
                    System.out.println("Salutation===>" + tempBean.getSalutation());
                    tempBean.setEmalId(itr.next().toString().trim());
                    System.out.println("EmalId===>" + tempBean.getEmalId());
                    tempBean.setFieldManagerEmail(itr.next().toString().trim());
                    System.out.println("FieldManagerEmail===>" + tempBean.getFieldManagerEmail());
                    tempBean.setMobileNo((long) itr.next());
                    System.out.println("MobileNo===>" + tempBean.getMobileNo());
                    System.out.println("****************************************************************************************\n\n\n");
                    lst.add(tempBean);

                }
                Hm.put("list", lst);
                Hm.put("temcount", tempCount - 1);
            }

        } catch (Exception ee) {
            System.out.println("Exception in getImportExcelData() in while-" + ee);
            ee.printStackTrace();
        } finally {
            file.close();
        }

        return Hm;
    }
}
