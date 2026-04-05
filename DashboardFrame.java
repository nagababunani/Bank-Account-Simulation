package bank;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private AccountDAO dao = new AccountDAO();
    private String accountNo;
    private JLabel balanceLabel;
    private JLabel statusLabel;
    private JPanel contentPanel;

    public DashboardFrame(String accountNo) {
        this.accountNo = accountNo;
        setTitle("SecureBank — Dashboard");
        setSize(800, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(15, 23, 42));

        buildSidebar();
        buildHeader();
        contentPanel = new JPanel(null);
        contentPanel.setBounds(200, 80, 595, 470);
        contentPanel.setBackground(new Color(15, 23, 42));
        add(contentPanel);

        showHome();
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private void buildSidebar() {
        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 195, 580);
        sidebar.setBackground(new Color(30, 41, 59));

        JLabel bankName = new JLabel("🏦 SecureBank", SwingConstants.CENTER);
        bankName.setFont(new Font("Arial", Font.BOLD, 14));
        bankName.setForeground(new Color(99, 179, 237));
        bankName.setBounds(0, 20, 195, 30);
        sidebar.add(bankName);

        JLabel accLabel = new JLabel("Acc: " + accountNo, SwingConstants.CENTER);
        accLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        accLabel.setForeground(new Color(148, 163, 184));
        accLabel.setBounds(0, 52, 195, 20);
        sidebar.add(accLabel);

        JSeparator sep = new JSeparator();
        sep.setBounds(15, 80, 165, 2);
        sep.setForeground(new Color(51, 65, 85));
        sidebar.add(sep);

        String[][] menuItems = {
            {"🏠", " Home"},
            {"💰", " Deposit"},
            {"💸", " Withdraw"},
            {"🔄", " Transfer"},
            {"📊", " History"},
            {"👤", " My Profile"},
            {"🚪", " Logout"}
        };

        int y = 95;
        for (String[] item : menuItems) {
            JButton btn = new JButton(item[0] + item[1]);
            btn.setBounds(10, y, 175, 42);
            btn.setBackground(new Color(30, 41, 59));
            btn.setForeground(new Color(203, 213, 225));
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(new Color(51, 65, 85));
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(new Color(30, 41, 59));
                }
            });

            String label = item[1].trim();
            btn.addActionListener(e -> {
                switch (label) {
                    case "Home": showHome(); break;
                    case "Deposit": showDeposit(); break;
                    case "Withdraw": showWithdraw(); break;
                    case "Transfer": showTransfer(); break;
                    case "History": showHistory(); break;
                    case "My Profile": showProfile(); break;
                    case "Logout":
                        dispose();
                        new LoginFrame();
                        break;
                }
            });

            sidebar.add(btn);
            y += 48;
        }

        add(sidebar);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private void buildHeader() {
        JPanel header = new JPanel(null);
        header.setBounds(195, 0, 605, 78);
        header.setBackground(new Color(30, 41, 59));

        String[] info = dao.getCustomerInfo(accountNo);
        String name = info != null ? info[1] : "Customer";

        JLabel welcome = new JLabel("Welcome, " + name + "!");
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        welcome.setForeground(Color.WHITE);
        welcome.setBounds(20, 15, 350, 25);
        header.add(welcome);

        balanceLabel = new JLabel("Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(52, 211, 153));
        balanceLabel.setBounds(20, 42, 300, 22);
        header.add(balanceLabel);

        String status = info != null ? info[5] : "ACTIVE";
        statusLabel = new JLabel("● " + status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(status.equals("ACTIVE") ? new Color(52, 211, 153) : new Color(239, 68, 68));
        statusLabel.setBounds(400, 28, 150, 25);
        header.add(statusLabel);

        add(header);
    }

    // ── Refresh Balance ───────────────────────────────────────────────────────
    private void refreshBalance() {
        balanceLabel.setText("Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)));
    }

    // ── Show Home ─────────────────────────────────────────────────────────────
    private void showHome() {
        contentPanel.removeAll();

        double balance = dao.getBalance(accountNo);
        double minBal = dao.getMinimumBalance();

        // Balance Card
        JPanel balCard = createCard(20, 20, 270, 120, new Color(37, 99, 235));
        JLabel balTitle = new JLabel("Current Balance");
        balTitle.setForeground(new Color(191, 219, 254));
        balTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        balTitle.setBounds(15, 15, 240, 20);
        balCard.add(balTitle);
        JLabel balAmt = new JLabel("Rs." + String.format("%.2f", balance));
        balAmt.setForeground(Color.WHITE);
        balAmt.setFont(new Font("Arial", Font.BOLD, 22));
        balAmt.setBounds(15, 42, 240, 30);
        balCard.add(balAmt);
        JLabel balNote = new JLabel("Min Required: Rs." + String.format("%.2f", minBal));
        balNote.setForeground(new Color(191, 219, 254));
        balNote.setFont(new Font("Arial", Font.PLAIN, 11));
        balNote.setBounds(15, 80, 240, 20);
        balCard.add(balNote);
        contentPanel.add(balCard);

        // Status Card
        String status = dao.getCustomerInfo(accountNo)[5];
        JPanel statCard = createCard(305, 20, 270, 120, status.equals("ACTIVE") ? new Color(5, 150, 105) : new Color(185, 28, 28));
        JLabel statTitle = new JLabel("Account Status");
        statTitle.setForeground(new Color(209, 250, 229));
        statTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        statTitle.setBounds(15, 15, 240, 20);
        statCard.add(statTitle);
        JLabel statVal = new JLabel("● " + status);
        statVal.setForeground(Color.WHITE);
        statVal.setFont(new Font("Arial", Font.BOLD, 20));
        statVal.setBounds(15, 42, 240, 30);
        statCard.add(statVal);
        contentPanel.add(statCard);

        // Warning if low balance
        if (balance < minBal) {
            JPanel warn = createCard(20, 155, 555, 60, new Color(146, 64, 14));
            JLabel warnText = new JLabel("⚠ Balance below minimum! Please deposit to avoid deactivation.");
            warnText.setForeground(new Color(254, 243, 199));
            warnText.setFont(new Font("Arial", Font.BOLD, 12));
            warnText.setBounds(15, 18, 525, 25);
            warn.add(warnText);
            contentPanel.add(warn);
        }

        // Recent Transactions
        JLabel recentTitle = new JLabel("Recent Transactions");
        recentTitle.setForeground(Color.WHITE);
        recentTitle.setFont(new Font("Arial", Font.BOLD, 14));
        recentTitle.setBounds(20, 230, 200, 25);
        contentPanel.add(recentTitle);

        List<String[]> txns = dao.getTransactionHistory(accountNo);
        String[] cols = {"Type", "Amount (Rs.)", "Description", "Date & Time"};
        String[][] data = txns.stream().limit(5).map(t -> t).toArray(String[][]::new);

        JTable table = new JTable(data, cols);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 262, 555, 180);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        contentPanel.add(scroll);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Show Deposit ──────────────────────────────────────────────────────────
    private void showDeposit() {
        contentPanel.removeAll();

        JPanel card = createCard(100, 50, 380, 280, new Color(30, 41, 59));

        JLabel title = new JLabel("💰 Deposit Money", SwingConstants.CENTER);
        title.setForeground(new Color(52, 211, 153));
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(0, 20, 380, 30);
        card.add(title);

        JLabel curBal = new JLabel("Current Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)), SwingConstants.CENTER);
        curBal.setForeground(new Color(148, 163, 184));
        curBal.setFont(new Font("Arial", Font.PLAIN, 12));
        curBal.setBounds(0, 58, 380, 20);
        card.add(curBal);

        JLabel amtLabel = new JLabel("Enter Amount:");
        amtLabel.setForeground(new Color(148, 163, 184));
        amtLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        amtLabel.setBounds(50, 100, 130, 25);
        card.add(amtLabel);

        JTextField amtField = new JTextField();
        amtField.setBounds(50, 130, 280, 35);
        amtField.setBackground(new Color(51, 65, 85));
        amtField.setForeground(Color.WHITE);
        amtField.setCaretColor(Color.WHITE);
        amtField.setBorder(BorderFactory.createLineBorder(new Color(52, 211, 153), 1));
        amtField.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(amtField);

        JButton depositBtn = new JButton("DEPOSIT");
        depositBtn.setBounds(50, 185, 280, 42);
        depositBtn.setBackground(new Color(5, 150, 105));
        depositBtn.setForeground(Color.WHITE);
        depositBtn.setFont(new Font("Arial", Font.BOLD, 14));
        depositBtn.setBorder(BorderFactory.createEmptyBorder());
        depositBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(depositBtn);

        depositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) { showMsg("Enter valid amount.", false); return; }
                if (dao.deposit(accountNo, amount)) {
                    refreshBalance();
                    showMsg("Rs." + String.format("%.2f", amount) + " deposited successfully!", true);
                    amtField.setText("");
                    curBal.setText("Current Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)));
                } else {
                    showMsg("Deposit failed!", false);
                }
            } catch (NumberFormatException ex) {
                showMsg("Enter a valid number.", false);
            }
        });

        contentPanel.add(card);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Show Withdraw ─────────────────────────────────────────────────────────
    private void showWithdraw() {
        contentPanel.removeAll();

        JPanel card = createCard(100, 50, 380, 300, new Color(30, 41, 59));

        JLabel title = new JLabel("💸 Withdraw Money", SwingConstants.CENTER);
        title.setForeground(new Color(251, 191, 36));
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(0, 20, 380, 30);
        card.add(title);

        JLabel curBal = new JLabel("Current Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)), SwingConstants.CENTER);
        curBal.setForeground(new Color(148, 163, 184));
        curBal.setFont(new Font("Arial", Font.PLAIN, 12));
        curBal.setBounds(0, 58, 380, 20);
        card.add(curBal);

        JLabel minBal = new JLabel("Min balance Rs.1000 must be maintained", SwingConstants.CENTER);
        minBal.setForeground(new Color(239, 68, 68));
        minBal.setFont(new Font("Arial", Font.ITALIC, 11));
        minBal.setBounds(0, 78, 380, 20);
        card.add(minBal);

        JLabel amtLabel = new JLabel("Enter Amount:");
        amtLabel.setForeground(new Color(148, 163, 184));
        amtLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        amtLabel.setBounds(50, 115, 130, 25);
        card.add(amtLabel);

        JTextField amtField = new JTextField();
        amtField.setBounds(50, 145, 280, 35);
        amtField.setBackground(new Color(51, 65, 85));
        amtField.setForeground(Color.WHITE);
        amtField.setCaretColor(Color.WHITE);
        amtField.setBorder(BorderFactory.createLineBorder(new Color(251, 191, 36), 1));
        amtField.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(amtField);

        JButton withdrawBtn = new JButton("WITHDRAW");
        withdrawBtn.setBounds(50, 205, 280, 42);
        withdrawBtn.setBackground(new Color(180, 83, 9));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFont(new Font("Arial", Font.BOLD, 14));
        withdrawBtn.setBorder(BorderFactory.createEmptyBorder());
        withdrawBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(withdrawBtn);

        withdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amtField.getText().trim());
                if (amount <= 0) { showMsg("Enter valid amount.", false); return; }
                if (dao.withdraw(accountNo, amount)) {
                    refreshBalance();
                    showMsg("Rs." + String.format("%.2f", amount) + " withdrawn successfully!", true);
                    amtField.setText("");
                    curBal.setText("Current Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)));
                } else {
                    showMsg("Cannot withdraw! Minimum balance of Rs.1000 must be maintained.", false);
                }
            } catch (NumberFormatException ex) {
                showMsg("Enter a valid number.", false);
            }
        });

        contentPanel.add(card);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Show Transfer ─────────────────────────────────────────────────────────
    private void showTransfer() {
        contentPanel.removeAll();

        JPanel card = createCard(80, 40, 420, 320, new Color(30, 41, 59));

        JLabel title = new JLabel("🔄 Transfer Money", SwingConstants.CENTER);
        title.setForeground(new Color(167, 139, 250));
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(0, 20, 420, 30);
        card.add(title);

        JLabel curBal = new JLabel("Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)), SwingConstants.CENTER);
        curBal.setForeground(new Color(148, 163, 184));
        curBal.setFont(new Font("Arial", Font.PLAIN, 12));
        curBal.setBounds(0, 55, 420, 20);
        card.add(curBal);

        JLabel toLabel = new JLabel("To Account No:");
        toLabel.setForeground(new Color(148, 163, 184));
        toLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        toLabel.setBounds(50, 90, 140, 25);
        card.add(toLabel);

        JTextField toField = new JTextField();
        toField.setBounds(50, 118, 310, 32);
        toField.setBackground(new Color(51, 65, 85));
        toField.setForeground(Color.WHITE);
        toField.setCaretColor(Color.WHITE);
        toField.setBorder(BorderFactory.createLineBorder(new Color(167, 139, 250), 1));
        card.add(toField);

        JLabel amtLabel = new JLabel("Amount:");
        amtLabel.setForeground(new Color(148, 163, 184));
        amtLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        amtLabel.setBounds(50, 165, 140, 25);
        card.add(amtLabel);

        JTextField amtField = new JTextField();
        amtField.setBounds(50, 193, 310, 32);
        amtField.setBackground(new Color(51, 65, 85));
        amtField.setForeground(Color.WHITE);
        amtField.setCaretColor(Color.WHITE);
        amtField.setBorder(BorderFactory.createLineBorder(new Color(167, 139, 250), 1));
        card.add(amtField);

        JButton transferBtn = new JButton("TRANSFER NOW");
        transferBtn.setBounds(50, 248, 310, 42);
        transferBtn.setBackground(new Color(109, 40, 217));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFont(new Font("Arial", Font.BOLD, 14));
        transferBtn.setBorder(BorderFactory.createEmptyBorder());
        transferBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(transferBtn);

        transferBtn.addActionListener(e -> {
            String toAcc = toField.getText().trim();
            try {
                double amount = Double.parseDouble(amtField.getText().trim());
                if (toAcc.isEmpty() || amount <= 0) { showMsg("Fill all fields correctly.", false); return; }
                if (toAcc.equals(accountNo)) { showMsg("Cannot transfer to own account.", false); return; }
                if (dao.transfer(accountNo, toAcc, amount)) {
                    refreshBalance();
                    showMsg("Rs." + String.format("%.2f", amount) + " transferred to " + toAcc + " successfully!", true);
                    toField.setText("");
                    amtField.setText("");
                    curBal.setText("Balance: Rs." + String.format("%.2f", dao.getBalance(accountNo)));
                } else {
                    showMsg("Transfer failed! Check balance or receiver account.", false);
                }
            } catch (NumberFormatException ex) {
                showMsg("Enter a valid number.", false);
            }
        });

        contentPanel.add(card);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Show History ──────────────────────────────────────────────────────────
    private void showHistory() {
        contentPanel.removeAll();

        JLabel title = new JLabel("📊 Transaction History");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(20, 15, 300, 30);
        contentPanel.add(title);

        List<String[]> txns = dao.getTransactionHistory(accountNo);
        String[] cols = {"Type", "Amount (Rs.)", "Description", "Date & Time"};
        String[][] data = txns.toArray(new String[0][]);

        JTable table = new JTable(data, cols);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 55, 565, 390);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        scroll.getViewport().setBackground(new Color(15, 23, 42));
        contentPanel.add(scroll);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Show Profile ──────────────────────────────────────────────────────────
    private void showProfile() {
        contentPanel.removeAll();

        JLabel title = new JLabel("👤 My Profile");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(20, 15, 300, 30);
        contentPanel.add(title);

        String[] info = dao.getCustomerInfo(accountNo);
        if (info == null) return;

        JPanel card = createCard(20, 55, 555, 350, new Color(30, 41, 59));

        String[] fieldLabels = {"Account No:", "Full Name:", "Email:", "Phone:", "Balance:", "Status:", "Member Since:", "Last Active:"};
        int y = 20;
        for (int i = 0; i < fieldLabels.length; i++) {
            JLabel lbl = new JLabel(fieldLabels[i]);
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            lbl.setBounds(30, y, 150, 25);
            card.add(lbl);

            JLabel val = new JLabel(info[i]);
            val.setForeground(Color.WHITE);
            val.setFont(new Font("Arial", Font.BOLD, 13));
            if (i == 4) val.setForeground(new Color(52, 211, 153));
            if (i == 5) val.setForeground(info[5].equals("ACTIVE") ? new Color(52, 211, 153) : new Color(239, 68, 68));
            val.setBounds(200, y, 320, 25);
            card.add(val);

            JSeparator sep = new JSeparator();
            sep.setBounds(30, y + 28, 495, 1);
            sep.setForeground(new Color(51, 65, 85));
            card.add(sep);

            y += 38;
        }

        contentPanel.add(card);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JPanel createCard(int x, int y, int w, int h, Color bg) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, w, h);
        card.setBackground(bg);
        card.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        return card;
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(new Color(51, 65, 85));
        table.setSelectionBackground(new Color(37, 99, 235));
        table.getTableHeader().setBackground(new Color(51, 65, 85));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Color rows by type
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(new Color(30, 41, 59));
                setForeground(Color.WHITE);
                if (col == 0) {
                    String type = t.getValueAt(row, 0).toString();
                    if (type.contains("DEPOSIT") || type.contains("IN"))
                        setForeground(new Color(52, 211, 153));
                    else if (type.contains("WITHDRAW") || type.contains("OUT"))
                        setForeground(new Color(239, 68, 68));
                    else
                        setForeground(new Color(167, 139, 250));
                }
                if (sel) setBackground(new Color(37, 99, 235));
                return this;
            }
        });
    }

    private void showMsg(String msg, boolean success) {
        JOptionPane.showMessageDialog(this, msg,
            success ? "Success" : "Error",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}