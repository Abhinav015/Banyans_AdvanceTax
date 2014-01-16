package banyanmails;

import helper.BanyanClientsBean;
import helper.Common;
import helper.MyTableModel;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SelectClients extends JFrame implements ActionListener {

    private JTable table;
    private JButton b1, b2, b3, b4, b5;
    private String flClients, flDocTemp;
    MyTableModel model;
    String name = "";
    Common cmn = new Common();
    HashMap hm = new HashMap();

    public SelectClients(String str) {
        super(str);
    }

    public void init() throws IOException {

        String[] colHeads = {"Sno", "Bank Customer Id", "Name", "Email", "Select"};

        hm = cmn.getImportExcelData(getFlClients(), getFlDocTemp());
        ArrayList errList = (ArrayList) hm.get("errList");

        int cols = 5;
        ArrayList<BanyanClientsBean> ls = (ArrayList) hm.get("list");
        int noRec = ls.size();
        List data = (ArrayList) hm.get("data");
        Iterator itr = data.iterator();
        Object[][] tableData = new Object[noRec][cols];
        int i = 0;
        while (itr.hasNext()) {
            for (int j = 1; j <= cols; j++) {
                tableData[i][j - 1] = itr.next();
            }
            i++;
        }
        b1 = new JButton("Select all");
        b2 = new JButton("Clear all");
        b3 = new JButton("Ok");
        b4 = new JButton("Cancel");
        b5 = new JButton("Show errors");

        b1.setBounds(40, 290, 100, 27);
        b2.setBounds(180, 290, 100, 27);
        b3.setBounds(320, 290, 100, 27);
        b4.setBounds(460, 290, 100, 27);
        b5.setBounds(40, 256, 100, 27);
        b5.setForeground(Color.red);

        model = new MyTableModel(tableData, colHeads);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        JScrollPane tableScroller = new JScrollPane(table);
        tableScroller.setBounds(2, 2, 593, 250);

        if (errList.size() > 0) {
            b5.setVisible(true);
            b1.setVisible(false);
            b2.setVisible(false);
            b3.setVisible(false);
            b4.setVisible(false);

        } else {
            b5.setVisible(false);
            b1.setVisible(true);
            b2.setVisible(true);
            b3.setVisible(true);
            b4.setVisible(true);
        }

        Container cp = getContentPane();
        cp.add(tableScroller);
        cp.add(b1);
        cp.add(b2);
        cp.add(b3);
        cp.add(b4);
        cp.add(b5);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);

        addWindowListener(new MyWindowAdapter1(this));
        setLayout(null);

        table.setRowHeight(22);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Select all")) {
            int rows = model.getRowCount();
            int column = model.getColumnCount();
            for (int i = 0; i < rows; i++) {
                model.setValueAt(Boolean.TRUE, i, column - 1);
            }
        }

        if (e.getActionCommand().equals("Clear all")) {
            int rows = model.getRowCount();
            int column = model.getColumnCount();
            for (int i = 0; i < rows; i++) {
                model.setValueAt(Boolean.FALSE, i, column - 1);
            }
        }

        if (e.getActionCommand().equals("Ok")) {
            BanyanMailApp frm1 = new BanyanMailApp("Banyan Mail Application");
            frm1.setIconImage(new javax.swing.ImageIcon("tree.png").getImage());
            frm1.setModel(model);
            frm1.setData(flDocTemp, flClients);
            frm1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frm1.setResizable(false);
            frm1.setLocation(350, 200);
            frm1.setSize(600, 280);
            frm1.setVisible(true);
            this.dispose();
        }

        if (e.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
            BanyanMails.frmShow(this);
        }

        if (e.getActionCommand().equals("Show errors")) {
            name = "";
            try {
                hm = cmn.getImportExcelData(getFlClients(), getFlDocTemp());
                ArrayList errList = (ArrayList) hm.get("errList");
                HashMap names = (HashMap) hm.get("getname");
                if (errList.size() > 10) {
                    name = name + "- clients name list it large.Please verify client csv and docuemnt templete.";
                } else {
                    for (int j = 0; j < errList.size(); j++) {
                        name = name + "-" + names.get(errList.get(j)).toString() + "\n";
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Client csv File not found ");
            }
            String msg = "Error occured while fetching clients. Following client(s) information does not exists in Client CSV File.\n" + name + "\n";
            msg = msg + "Please provide client information of above clients in Client CSV.";
            JOptionPane.showMessageDialog(this, msg, "Error Orccured : Client CSV", JOptionPane.ERROR_MESSAGE);

        }
    }

    public String getFlClients() {
        return flClients;
    }

    public void setFlClients(String flClients) {
        this.flClients = flClients;
    }

    public String getFlDocTemp() {
        return flDocTemp;
    }

    public void setFlDocTemp(String flDocTemp) {
        this.flDocTemp = flDocTemp;
    }

    class MyWindowAdapter1 extends WindowAdapter {

        SelectClients sc = null;

        MyWindowAdapter1(SelectClients sc) {
            this.sc = sc;
        }

        public void windowClosing(WindowEvent e) {
            sc.setVisible(false);
            BanyanMails.frmShow(sc);
        }

    }

}
