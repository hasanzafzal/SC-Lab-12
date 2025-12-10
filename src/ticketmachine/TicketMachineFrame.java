package ticketmachine;

import javax.swing.*;
import java.awt.*;

public class TicketMachineFrame extends JFrame {

    private final TicketMachine ticketMachine;

    // THEME
    private boolean darkMode = false;

    // PANELS
    private JPanel backgroundPanel;
    private JPanel containerPanel;

    // CONTROLS
    private JComboBox<String> typeCombo;
    private JComboBox<String> zoneCombo;
    private JTextField ticketPriceField;
    private JTextField amountInsertedField;
    private JLabel statusLabel;
    private JLabel totalCollectedLabel;

    private JButton btnInsert10;
    private JButton btnInsert50;
    private JButton btnInsertCustom;
    private JButton btnPrint;
    private JButton btnRefund;
    private JToggleButton darkModeToggle;

    public TicketMachineFrame() {
        ticketMachine = new TicketMachine();

        setTitle("Ticket Machine");
        setSize(650, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(550, 420));

        initComponents();
        registerListeners();
        updatePrice();
        updateBalance();
        updateTotalCollected();
        applyTheme();
    }

    // ---------------------------------------------------------------------
    // UI CREATION
    // ---------------------------------------------------------------------
    private void initComponents() {

        backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setOpaque(true);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
        Font headingFont = new Font("Segoe UI", Font.BOLD, 16);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Title row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel titleLabel = new JLabel("EV Bus Ticket Machine by Hasan");
        titleLabel.setFont(titleFont);

        darkModeToggle = new JToggleButton("🌙 Dark");
        darkModeToggle.setFocusPainted(false);

        titleRow.add(titleLabel, BorderLayout.WEST);
        titleRow.add(darkModeToggle, BorderLayout.EAST);

        containerPanel.add(titleRow);
        containerPanel.add(Box.createVerticalStrut(10));

        // CONFIG
        JPanel configPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        configPanel.setOpaque(false);

        JLabel typeLabel = new JLabel("Ticket Type:");
        typeLabel.setFont(labelFont);

        typeCombo = new JComboBox<>(new String[]{
                "Standard", "Executive", "Student/Elderly (Subsidy)"
        });
        typeCombo.setFont(textFont);

        JLabel zoneLabel = new JLabel("Zone:");
        zoneLabel.setFont(labelFont);

        zoneCombo = new JComboBox<>(new String[]{"Islamabad ICT", "Chaklala Cantt.", "Rawalpindi"});
        zoneCombo.setFont(textFont);

        configPanel.add(typeLabel);
        configPanel.add(typeCombo);
        configPanel.add(zoneLabel);
        configPanel.add(zoneCombo);

        containerPanel.add(configPanel);
        containerPanel.add(Box.createVerticalStrut(10));

        // PRICE + BALANCE
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.setOpaque(false);

        JLabel lblTicketPrice = new JLabel("Ticket Price:");
        lblTicketPrice.setFont(headingFont);

        ticketPriceField = new JTextField(8);
        ticketPriceField.setEditable(false);
        ticketPriceField.setFont(textFont);

        row1.add(lblTicketPrice);
        row1.add(ticketPriceField);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setOpaque(false);

        JLabel lblAmountInserted = new JLabel("Amount Inserted:");
        lblAmountInserted.setFont(headingFont);

        amountInsertedField = new JTextField(8);
        amountInsertedField.setEditable(false);
        amountInsertedField.setFont(textFont);

        row2.add(lblAmountInserted);
        row2.add(amountInsertedField);

        containerPanel.add(row1);
        containerPanel.add(row2);
        containerPanel.add(Box.createVerticalStrut(10));

        // BUTTONS
        JPanel btnRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnRow1.setOpaque(false);
        JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnRow2.setOpaque(false);

        btnInsert10 = new JButton("💰 Insert 10");
        btnInsert50 = new JButton("💵 Insert 50");
        btnInsertCustom = new JButton("⌨ Insert Custom");
        btnPrint = new JButton("🖨 Print Ticket");
        btnRefund = new JButton("↩ Refund");

        // Style all buttons
        styleModernButton(btnInsert10, buttonFont);
        styleModernButton(btnInsert50, buttonFont);
        styleModernButton(btnInsertCustom, buttonFont);
        styleModernButton(btnPrint, buttonFont);
        styleModernButton(btnRefund, buttonFont);

        btnRow1.add(btnInsert10);
        btnRow1.add(btnInsert50);
        btnRow1.add(btnInsertCustom);

        btnRow2.add(btnPrint);
        btnRow2.add(btnRefund);

        containerPanel.add(btnRow1);
        containerPanel.add(btnRow2);
        containerPanel.add(Box.createVerticalStrut(10));

        // STATUS + TOTAL
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        statusRow.setOpaque(false);

        JLabel lblStatus = new JLabel("Status:");
        JLabel lblStatusBold = lblStatus;
        lblStatusBold.setFont(labelFont);

        statusLabel = new JLabel("Insert money to buy a ticket.");
        statusLabel.setFont(textFont);

        statusRow.add(lblStatus);
        statusRow.add(statusLabel);

        JPanel totalRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        totalRow.setOpaque(false);

        totalCollectedLabel = new JLabel();
        totalCollectedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        totalRow.add(totalCollectedLabel);

        containerPanel.add(statusRow);
        containerPanel.add(totalRow);

        backgroundPanel.add(containerPanel);
        setContentPane(backgroundPanel);
    }

    // ---------------------------------------------------------------------
    // EVENT HANDLERS
    // ---------------------------------------------------------------------
    private void registerListeners() {

        typeCombo.addActionListener(e -> {
            switch (typeCombo.getSelectedIndex()) {
                case 1 -> ticketMachine.setTicketType(TicketMachine.TicketType.executive);
                case 2 -> ticketMachine.setTicketType(TicketMachine.TicketType.STUDENT);
                default -> ticketMachine.setTicketType(TicketMachine.TicketType.STANDARD);
            }
            updatePrice();
        });

        zoneCombo.addActionListener(e -> {
            ticketMachine.setZone(zoneCombo.getSelectedIndex() + 1);
            updatePrice();
        });

        btnInsert10.addActionListener(e -> {
            ticketMachine.insertMoney(10);
            updateBalance();
            statusLabel.setText("Inserted 10.");
        });

        btnInsert50.addActionListener(e -> {
            ticketMachine.insertMoney(50);
            updateBalance();
            statusLabel.setText("Inserted 50.");
        });

        btnInsertCustom.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                    "Enter amount to insert:",
                    "Insert Custom Amount",
                    JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.isBlank()) {
                try {
                    int value = Integer.parseInt(input.trim());
                    if (value <= 0) {
                        statusLabel.setText("Amount must be positive.");
                    } else {
                        ticketMachine.insertMoney(value);
                        updateBalance();
                        statusLabel.setText("Inserted " + value + ".");
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Invalid amount.");
                }
            }
        });

        btnPrint.addActionListener(e -> handlePrintTicket());

        btnRefund.addActionListener(e -> {
            int refunded = ticketMachine.refund();
            updateBalance();
            statusLabel.setText(refunded > 0 ? "Refunded " + refunded + "." : "Nothing to refund.");
        });

        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            applyTheme();
        });
    }

    // ---------------------------------------------------------------------
    // PRINT TICKET POPUP
    // ---------------------------------------------------------------------
    private void handlePrintTicket() {

        if (!ticketMachine.canPrintTicket()) {
            int needed = ticketMachine.getCurrentTicketPrice() - ticketMachine.getBalance();
            statusLabel.setText("Need " + needed + " more to print ticket.");
            statusLabel.setForeground(new Color(180, 40, 40));
            new Timer(900, e -> applyTheme()).start();
            return;
        }

        String receipt = ticketMachine.printTicket();
        updateBalance();
        updateTotalCollected();
        statusLabel.setText("Ticket printed!");

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setEditable(false);

        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "Ticket Printed",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePrice() {
        ticketPriceField.setText(String.valueOf(ticketMachine.getCurrentTicketPrice()));
    }

    private void updateBalance() {
        amountInsertedField.setText(String.valueOf(ticketMachine.getBalance()));
    }

    private void updateTotalCollected() {
        totalCollectedLabel.setText("Total collected: " + ticketMachine.getTotalCollected());
    }

    // ---------------------------------------------------------------------
    // BUTTON STYLE (Neutral / Professional)
    // ---------------------------------------------------------------------
    private void styleModernButton(JButton button, Font font) {

        button.setFont(font);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 38));

        // Light mode button
        Color lightBtn = new Color(230, 230, 230);
        // Dark mode button
        Color darkBtn = new Color(70, 70, 70);

        button.setBackground(darkMode ? darkBtn : lightBtn);
        button.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        // Thin border
        button.setBorder(BorderFactory.createLineBorder(
                darkMode ? new Color(120, 120, 120) : new Color(180, 180, 180), 1
        ));
    }

    // ---------------------------------------------------------------------
    // THEME SYSTEM
    // ---------------------------------------------------------------------
    private void applyTheme() {

        Color bgLight = new Color(242, 242, 245);
        Color cardLight = new Color(255, 255, 255);
        Color textLight = new Color(32, 32, 32);

        Color bgDark = new Color(27, 27, 29);
        Color cardDark = new Color(36, 36, 40);
        Color textDark = new Color(236, 236, 236);

        if (darkMode) {
            backgroundPanel.setBackground(bgDark);
            containerPanel.setBackground(cardDark);
            setForegroundRecursive(containerPanel, textDark);
            darkModeToggle.setText("☀ Light");
        } else {
            backgroundPanel.setBackground(bgLight);
            containerPanel.setBackground(cardLight);
            setForegroundRecursive(containerPanel, textLight);
            darkModeToggle.setText("🌙 Dark");
        }

        // Fix text fields + combos
        Color fieldLight = Color.WHITE;
        Color fieldDark = new Color(60, 60, 65);

        ticketPriceField.setBackground(darkMode ? fieldDark : fieldLight);
        ticketPriceField.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        amountInsertedField.setBackground(darkMode ? fieldDark : fieldLight);
        amountInsertedField.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        typeCombo.setBackground(darkMode ? fieldDark : fieldLight);
        typeCombo.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        zoneCombo.setBackground(darkMode ? fieldDark : fieldLight);
        zoneCombo.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        // Refresh buttons with new theme
        JButton[] buttons = {btnInsert10, btnInsert50, btnInsertCustom, btnPrint, btnRefund};
        for (JButton b : buttons) {
            styleModernButton(b, b.getFont());
        }

        // Reset status text color
        statusLabel.setForeground(darkMode ? textDark : textLight);

        repaint();
    }

    private void setForegroundRecursive(Component comp, Color color) {
        if (comp instanceof JLabel || comp instanceof JButton || comp instanceof JTextField) {
            comp.setForeground(color);
        }
        if (comp instanceof JTextField tf) {
            tf.setCaretColor(color);
        }
        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                setForegroundRecursive(child, color);
            }
        }
    }

    // ---------------------------------------------------------------------
    // BACKGROUND GRADIENT
    // ---------------------------------------------------------------------
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color top = new Color(245, 245, 245);
            Color bottom = new Color(225, 225, 225);

            GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }
    }
}
