package bank;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private AccountDAO dao = new AccountDAO();
    private String accountNo;

    public MainMenu(String accountNo) {
        this.accountNo = accountNo;
        setTitle("Bank Simulation System");
        setSize(400, 530);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(220, 220, 220));

        // Get customer name from database
        String[] info = dao.getCustomerInfo(accountNo);
        String fullName = (info != null && info[1] != null) ? info[1] : "Customer";

        // Welcome greeting panel
        JPanel greetPanel = new JPanel(null);
        greetPanel.setBounds(0, 0, 400, 75);
        greetPanel.setBackground(new Color(200, 200, 200));
        greetPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        // Customer emoji
        JLabel emoji = new JLabel("👤", SwingConstants.CENTER);
        emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        emoji.setBounds(20, 12, 45, 45);
        greetPanel.add(emoji);

        // Welcome text
        JLabel welcomeLbl = new JLabel("Welcome back,");
        welcomeLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeLbl.setForeground(new Color(80, 80, 80));
        welcomeLbl.setBounds(75, 12, 300, 20);
        greetPanel.add(welcomeLbl);

        // Customer name
        JLabel nameLbl = new JLabel(fullName);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 16));
        nameLbl.setForeground(new Color(30, 30, 30));
        nameLbl.setBounds(75, 34, 300, 25);
        greetPanel.add(nameLbl);

        // Account number small text
        JLabel accLbl = new JLabel("Account: " + accountNo);
        accLbl.setFont(new Font("Arial", Font.PLAIN, 10));
        accLbl.setForeground(new Color(120, 120, 120));
        accLbl.setBounds(75, 56, 200, 16);
        greetPanel.add(accLbl);

        add(greetPanel);

        // Buttons
        Font btnFont = new Font("Arial", Font.PLAIN, 14);
        int btnX = 100, btnW = 200, btnH = 40, gap = 50;

        JButton depositBtn  = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton balanceBtn  = new JButton("Check Balance");
        JButton transferBtn = new JButton("Transfer");
        JButton historyBtn  = new JButton("History");
        JButton profileBtn  = new JButton("My Profile");
        JButton logoutBtn   = new JButton("Logout");

        JButton[] btns = {depositBtn, withdrawBtn, balanceBtn,
                          transferBtn, historyBtn, profileBtn, logoutBtn};

        int y = 88;
        for (JButton btn : btns) {
            btn.setBounds(btnX, y, btnW, btnH);
            btn.setFont(btnFont);
            add(btn);
            y += gap;
        }

        depositBtn.addActionListener(e -> {
            dispose();
            new DepositFrame(accountNo).setVisible(true);
        });

        withdrawBtn.addActionListener(e -> {
            dispose();
            new WithdrawFrame(accountNo).setVisible(true);
        });

        balanceBtn.addActionListener(e -> {
            double bal = dao.getBalance(accountNo);
            JOptionPane.showMessageDialog(this,
                "Balance: Rs." + String.format("%.2f", bal));
        });

        transferBtn.addActionListener(e -> {
            dispose();
            new TransferFrame(accountNo).setVisible(true);
        });

        historyBtn.addActionListener(e -> {
            new HistoryFrame(accountNo).setVisible(true);
        });

        profileBtn.addActionListener(e -> {
            new ProfileFrame(accountNo).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}