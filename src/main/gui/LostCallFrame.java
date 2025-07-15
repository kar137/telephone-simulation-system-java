package main.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import main.model.*;
import java.util.Iterator;

public class LostCallFrame extends JFrame {

    private JLabel clockLabel;
    private int currentTime = 100;
    private Timer simulationTimer;

    private JTextField[] lineFields;
    private JTextField fromField, toField, lengthField, arrivalField;
    private JTextField maxLinks, usedLinks, clockTime;
    private JTextField blockedField, busyField, completedField, processedField;

    private DefaultTableModel callTableModel;
    private JTable callTable;
    private List<Call> activeCalls = new ArrayList<>();
    private Switchboard switchboard;

    public LostCallFrame() {
        setTitle("Telephone Simulation System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        switchboard = new Switchboard(8, 3); // 8 lines, 3 switch links

        // Clock label
        clockLabel = new JLabel("<html>&#x23F0; Time: 0</html>", JLabel.CENTER);
        clockLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        add(clockLabel, BorderLayout.NORTH);

        // Left Panel with lines, info and next call
        JPanel leftPanel = new JPanel(new GridLayout(1, 3));
        leftPanel.add(createLinesPanel());
        leftPanel.add(createInfoPanel());
        leftPanel.add(createNextCallPanel());
        add(leftPanel, BorderLayout.CENTER);

        // Call IN PROGRESS Table setup
        String[] columns = {"From", "To", "End"};
        callTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        callTable = new JTable(callTableModel);
        JScrollPane callScrollPane = new JScrollPane(callTable);
        callScrollPane.setBorder(BorderFactory.createTitledBorder("Call IN PROGRESS"));
        add(callScrollPane, BorderLayout.EAST);

        // Bottom panel with controls and stats
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton nextCallBtn = new JButton("Next Call Arrive");
        nextCallBtn.addActionListener(e -> handleNextCall());
        bottomPanel.add(nextCallBtn, BorderLayout.WEST);

        JPanel processedPanel = new JPanel(new GridLayout(2, 4));
        processedPanel.setBorder(BorderFactory.createTitledBorder("Call Processed"));
        processedPanel.add(new JLabel("Blocked:")); blockedField = new JTextField("0"); processedPanel.add(blockedField);
        processedPanel.add(new JLabel("Busy:")); busyField = new JTextField("0"); processedPanel.add(busyField);
        processedPanel.add(new JLabel("Completed:")); completedField = new JTextField("0"); processedPanel.add(completedField);
        processedPanel.add(new JLabel("Processed:")); processedField = new JTextField("0"); processedPanel.add(processedField);
        bottomPanel.add(processedPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Timer to advance simulation time and update UI
        simulationTimer = new Timer(1000, e -> {
            currentTime++;
            clockLabel.setText("⏰ Time: " + currentTime);
            clockTime.setText(String.valueOf(currentTime));
            updateCalls();
        });
        simulationTimer.start();

        setVisible(true);
    }

    private JPanel createLinesPanel() {
        JPanel linesPanel = new JPanel(new GridLayout(9, 2));
        linesPanel.setBorder(BorderFactory.createTitledBorder("Lines"));
        linesPanel.add(new JLabel("Line")); linesPanel.add(new JLabel("Status"));
        lineFields = new JTextField[8];
        for (int i = 0; i < 8; i++) {
            linesPanel.add(new JLabel("" + (i + 1)));
            lineFields[i] = new JTextField("0");
            lineFields[i].setEditable(false);
            linesPanel.add(lineFields[i]);
        }
        return linesPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(3, 2));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Links / Clock"));
        infoPanel.add(new JLabel("Links Max:"));
        maxLinks = new JTextField("3");
        maxLinks.setEditable(false);
        infoPanel.add(maxLinks);

        infoPanel.add(new JLabel("Links in Use:"));
        usedLinks = new JTextField("0");
        usedLinks.setEditable(false);
        infoPanel.add(usedLinks);

        infoPanel.add(new JLabel("Clock:"));
        clockTime = new JTextField(String.valueOf(currentTime));
        clockTime.setEditable(false);
        infoPanel.add(clockTime);
        return infoPanel;
    }

    private JPanel createNextCallPanel() {
        JPanel nextCallPanel = new JPanel(new GridLayout(4, 2));
        nextCallPanel.setBorder(BorderFactory.createTitledBorder("Next Call"));
        nextCallPanel.add(new JLabel("From:"));
        fromField = new JTextField("1");
        nextCallPanel.add(fromField);

        nextCallPanel.add(new JLabel("To:"));
        toField = new JTextField("7");
        nextCallPanel.add(toField);

        nextCallPanel.add(new JLabel("Length:"));
        lengthField = new JTextField("20");
        nextCallPanel.add(lengthField);

        nextCallPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField("157");
        nextCallPanel.add(arrivalField);

        return nextCallPanel;
    }

    private void handleNextCall() {
        try {
            int from = Integer.parseInt(fromField.getText());
            int to = Integer.parseInt(toField.getText());
            int length = Integer.parseInt(lengthField.getText());
            int arrival = Integer.parseInt(arrivalField.getText());

            if (from < 1 || from > 8 || to < 1 || to > 8) {
                JOptionPane.showMessageDialog(this, "Line numbers must be between 1 and 8");
                return;
            }

            if (arrival > currentTime) {
                JOptionPane.showMessageDialog(this, "Call hasn't arrived yet.");
                return;
            }

            Call call = new Call(from, to, arrival, length);
            boolean connected = switchboard.connectCall(call);

            if (connected) {
                activeCalls.add(call);
                addCallToTable(call);
                usedLinks.setText(String.valueOf(switchboard.getUsedLinks()));
                updateLineStatus(from, 1);
                updateLineStatus(to, 1);
            } else {
                int blocked = Integer.parseInt(blockedField.getText());
                blockedField.setText(String.valueOf(blocked + 1));
            }

            int processed = Integer.parseInt(processedField.getText());
            processedField.setText(String.valueOf(processed + 1));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers only.");
        }
    }

    private void addCallToTable(Call call) {
        int endTime = call.getStartTime() + call.getDuration();
        callTableModel.addRow(new Object[]{call.getFromLine(), call.getToLine(), endTime});
    }

    private void updateCalls() {
        int completedCount = 0;
        int current = currentTime; // or pass currentTime as parameter

        Iterator<Call> iterator = activeCalls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next();
            int callEnd = call.getStartTime() + call.getDuration();
            if (current >= callEnd) {
                switchboard.disconnectCall(call);
                updateLineStatus(call.getFromLine(), 0);
                updateLineStatus(call.getToLine(), 0);
                iterator.remove();
                completedCount++;
            }
        }

        // ✅ update UI inside invokeLater (no completedCount here)
        SwingUtilities.invokeLater(() -> {
            callTableModel.setRowCount(0);
            for (Call call : activeCalls) {
                int callEnd = call.getStartTime() + call.getDuration();
                callTableModel.addRow(new Object[]{call.getFromLine(), call.getToLine(), callEnd});
            }
            usedLinks.setText(String.valueOf(switchboard.getUsedLinks()));
        });

        // ✅ Update completedField outside the lambda
        if (completedCount > 0) {
            int oldCompleted = Integer.parseInt(completedField.getText());
            completedField.setText(String.valueOf(oldCompleted + completedCount));
        }
    }




    private void updateLineStatus(int line, int status) {
        if (line >= 1 && line <= 8) {
            lineFields[line - 1].setText(String.valueOf(status));
        }
    }
}
