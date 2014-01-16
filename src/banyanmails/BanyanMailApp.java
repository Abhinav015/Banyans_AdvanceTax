package banyanmails;

import helper.MyTableModel;
import helper.DatePicker;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BanyanMailApp extends JFrame implements ActionListener, Runnable {

    Thread showClient;
    JButton b1, b2, b3, b4, b5, jbutton;
    private JLabel l1, l2, l3, jlabel;
    private JTextField tf1, tf2, jdate;
    private String choosertitle;
    private SendMailApp sma = new SendMailApp("Banyan Mail Application");
    private SelectClients sc = new SelectClients("Banyan Mail Application");
    private MyTableModel model;
    private JFileChooser chooser = new JFileChooser();
    private ImageIcon xls = new ImageIcon((getClass().getResource("/images/XLS.png")));
    private ImageIcon dt = new ImageIcon((getClass().getResource("/images/date.png")));
    private String flClients, flDocTemp;
    private BufferedImage image;

    public BanyanMailApp(String msg) {
        super(msg);

        super.setIconImage(image);
        l1 = new JLabel(" Select Document Template", xls, JLabel.LEFT);
        l2 = new JLabel(" Select Client File", xls, JLabel.LEFT);
        l3 = new JLabel("Date of Report", JLabel.LEFT);

        tf1 = new JTextField(40);
        tf1.setBackground(Color.decode("#FFFFE0"));
        tf2 = new JTextField(40);
        tf2.setBackground(Color.decode("#FFFFE0"));
        b1 = new JButton("Send");
        b2 = new JButton("...");
        b3 = new JButton("...");
        b4 = new JButton("View Selection");
        b5 = new JButton("close");

        jlabel = new JLabel("Report Date ");
        jdate = new JTextField(40);
        jdate.setBackground(Color.decode("#FFFFE0"));
        jbutton = new JButton(dt);

        l1.setBounds(10, 50, 200, 30);
        l2.setBounds(10, 90, 180, 30);
        tf1.setBounds(210, 55, 300, 22);
        tf2.setBounds(210, 95, 300, 22);
        b1.setBounds(250, 150, 100, 27);
        b2.setBounds(520, 52, 30, 22);
        b3.setBounds(520, 92, 30, 22);
        b4.setBounds(90, 150, 130, 27);
        b5.setBounds(390, 150, 100, 27);
        jlabel.setBounds(10, 220, 100, 27);
        jdate.setBounds(80, 220, 130, 24);
        jbutton.setBounds(210, 217, 30, 27);

        Container cp = getContentPane();
        cp.add(l1);
        cp.add(tf1);
        cp.add(l2);
        cp.add(tf2);
        cp.add(b1);
        cp.add(b2);
        cp.add(b3);
        cp.add(b4);
        cp.add(b5);
        cp.add(jlabel);
        cp.add(jdate);
        cp.add(jbutton);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        jbutton.addActionListener(this);
        setLayout(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b1) {
            if (tf1.getText().equals("") || tf1.getText() == null || tf2.getText().equals("") || tf2.getText() == null
                    || jdate.getText().equals("") || jdate.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please select attachments folder path, client file(xlsx or xls) and report date.");
            } else {
                sma.setFlDocTemp(getFlDocTemp() == null ? tf1.getText() : getFlDocTemp());
                sma.setFlClients(getFlClients() == null ? tf2.getText() : getFlClients());
                sma.setIconImage(new javax.swing.ImageIcon("tree.png").getImage());
                sma.setModel(model);
                sma.setLocation(350, 150);
                sma.setSize(600, 500);
                sma.setVisible(true);
                sma.setResizable(false);
                this.hide();
            }
        }

        if (e.getSource() == b2) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("XLSX files", "xlsx");
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            chooser.setFileFilter(filter1);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                tf1.setText(chooser.getSelectedFile().toString());
                setFlDocTemp(chooser.getSelectedFile().toString());
                System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            } else {
                System.out.println("No Selection");
            }
        }

        if (e.getSource() == b3) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("XLSX files", "xlsx");
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            chooser.setFileFilter(filter1);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                tf2.setText(chooser.getSelectedFile().toString());
                setFlClients(chooser.getSelectedFile().toString());
                System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
                b4.setEnabled(true);
            } else {
                System.out.println("No Selection");
            }
        }

        if (e.getSource() == b4) {
            if (tf1.getText().equals("") || tf1.getText() == null || tf2.getText().equals("") || tf2.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please select attachments folder path and client file(xlsx or xls).");
            } else {
                showClient = new Thread(this);
                showClient.start();
            }
        }

        if (e.getSource() == b5) {
            System.exit(0);
        }

        if (e.getSource() == jbutton) {
            jdate.setText(new DatePicker(this).setPickedDate());
        }
    }

    public MyTableModel getModel() {
        return model;
    }

    public void setModel(MyTableModel model) {
        this.model = model;
    }

    public void setData(String flDoc, String flCl) {
        tf1.setText(flDoc);
        tf2.setText(flCl);
        b1.setEnabled(true);
        b4.setEnabled(true);
    }

    public void setButtonDisbled() {
        b1.setEnabled(false);
        b4.setEnabled(false);
    }

    public void setButtonDisbled1() {
        b1.setEnabled(false);
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

    @Override
    public void run() {
        try {
            sc.setFlDocTemp(getFlDocTemp() == null ? tf1.getText() : getFlDocTemp());
            sc.setFlClients(getFlClients() == null ? tf2.getText() : getFlClients());
            sc.init();
            sc.setIconImage(new javax.swing.ImageIcon("tree.png").getImage());
            sc.setLocation(350, 200);
            sc.setSize(600, 350);
            sc.setVisible(true);
            sc.setResizable(false);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "You have selected wrong client CSV/XLS file.");
        }
    }

}
