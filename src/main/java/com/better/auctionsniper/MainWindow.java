package com.better.auctionsniper;

import javax.swing.*;

import java.awt.*;


public class MainWindow extends JFrame {

    private static final String SNIPERS_TABLE_NAME = "Snipers";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WON = "Won";
    public static final String STAUS_LOST = "Lost";
    private SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow() {
        super("Auction Sniper");
        setName(Application.MAIN_WINDOW_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 100));
        fillContentPane(makeSnipersTable());
        pack();
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        JTable jTable = new JTable(snipers);
        jTable.setName(SNIPERS_TABLE_NAME);
        return jTable;
    }

    public void showStatus(String status) {
        snipers.setStatusText(status);
    }
}
