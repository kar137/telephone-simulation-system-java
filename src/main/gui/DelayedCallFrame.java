package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.Queue;
import javax.swing.Timer;
import main.model.*;

public class DelayedCallFrame extends JFrame {

    private JLabel clockLabel;
    private JTextArea logArea;
    private JTextField[] lineFields;
    private JTextField fromField, toField, lengthField, arrivalField;
    private JTextField usedLinks, completedField, delayedField, retrySuccessField, droppedField, totalField;
    private DefaultTableModel callTableModel;

    private int currentTime = 0;
    private Timer simulationTimer;

    private Switchboard switchboard;
    private ArrayList<Call> activeCalls = new ArrayList<>();
    private Queue<DelayedCall> delayedQueue = new LinkedList<>();

    public DelayedCallFrame() {
        setTitle("📞 Delayed Call Simulation");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        switchboard = new Switchboard(8, 3); // 8 lines, 3 connections

        // Top clock label
        clockLabel = new JLabel("<html>&#x23F0; Time: 0</html>", JLabel.CENTER);
        clockLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24)); 
        add(clockLabel, BorderLayout.NORTH);

        // Center panel layout
        JPanel centerPanel = new JPanel(new GridLayout(2, 2));

        // Line status panel
        JPanel linePanel = new JPanel(new GridLayout(9, 2));
        linePanel.setBorder(BorderFactory.createTitledBorder("Lines"));
        linePanel.add(new JLabel("Line")); linePanel.add(new JLabel("Status"));
        lineFields = new JTextField[8];
        for (int i = 0; i < 8; i++) {
            linePanel.add(new JLabel("" + (i + 1)));
            lineFields[i] = new JTextField("0");
            lineFields[i].setEditable(false);
            linePanel.add(lineFields[i]);
        }

        // Next call info
        JPanel nextCallPanel = new JPanel(new GridLayout(4, 2));
        nextCallPanel.setBorder(BorderFactory.createTitledBorder("Last Attempted Call"));
        fromField = new JTextField(); toField = new JTextField();
        lengthField = new JTextField(); arrivalField = new JTextField();
        fromField.setEditable(false); toField.setEditable(false);
        lengthField.setEditable(false); arrivalField.setEditable(false);
        nextCallPanel.add(new JLabel("From:")); nextCallPanel.add(fromField);
        nextCallPanel.add(new JLabel("To:")); nextCallPanel.add(toField);
        nextCallPanel.add(new JLabel("Length:")); nextCallPanel.add(lengthField);
        nextCallPanel.add(new JLabel("Arrival Time:")); nextCallPanel.add(arrivalField);

        // Link usage and summary
        JPanel infoPanel = new JPanel(new GridLayout(2, 2));
        infoPanel.setBorder(BorderFactory.createTitledBorder("System Info"));
        infoPanel.add(new JLabel("Links in Use:"));
        usedLinks = new JTextField("0");
        usedLinks.setEditable(false);
        infoPanel.add(usedLinks);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 4));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Call Summary"));
        delayedField = new JTextField("0");
        retrySuccessField = new JTextField("0");
        droppedField = new JTextField("0");
        completedField = new JTextField("0");
        totalField = new JTextField("0");
        JTextField[] summaryFields = {delayedField, retrySuccessField, droppedField, completedField, totalField};
        for (JTextField tf : summaryFields) tf.setEditable(false);

        summaryPanel.add(new JLabel("Delayed:")); summaryPanel.add(delayedField);
        summaryPanel.add(new JLabel("Retry Success:")); summaryPanel.add(retrySuccessField);
        summaryPanel.add(new JLabel("Dropped:")); summaryPanel.add(droppedField);
        summaryPanel.add(new JLabel("Completed:")); summaryPanel.add(completedField);
        summaryPanel.add(new JLabel("Total Processed:")); summaryPanel.add(totalField);

        // Calls in progress table
        String[] cols = {"From", "To", "End"};
        callTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(callTableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Call IN PROGRESS"));

        // Add to center grid
        centerPanel.add(linePanel);
        centerPanel.add(nextCallPanel);
        centerPanel.add(infoPanel);
        centerPanel.add(summaryPanel);
        add(centerPanel, BorderLayout.CENTER);
        add(tableScroll, BorderLayout.EAST);

        // Log area and control buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Simulation Log"));

        JButton stopBtn = new JButton("🛑 Stop");
        stopBtn.addActionListener(e -> simulationTimer.stop());

        JButton retryBtn = new JButton("🔄 Retry Delayed Calls");
        retryBtn.addActionListener(e -> retryDelayedCalls());

        bottomPanel.add(logScroll, BorderLayout.CENTER);
        bottomPanel.add(retryBtn, BorderLayout.NORTH);
        bottomPanel.add(stopBtn, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Timer ticks every second
        simulationTimer = new Timer(1000, e -> tick());
        simulationTimer.start();

        setVisible(true);
    }

    private void tick() {
        currentTime++;
        clockLabel.setText("⏰ Time: " + currentTime);
        usedLinks.setText(String.valueOf(switchboard.getUsedLinks()));

        // Generate new call
        Call call = Call.generateRandomCall(currentTime, 8);
        boolean connected = switchboard.connectCall(call);

        // Update call display
        fromField.setText(String.valueOf(call.getFromLine()));
        toField.setText(String.valueOf(call.getToLine()));
        lengthField.setText(String.valueOf(call.getDuration()));
        arrivalField.setText(String.valueOf(call.getStartTime()));

        increment(totalField);

        if (connected) {
            activeCalls.add(call);
            updateLineStatus(call.getFromLine(), 1);
            updateLineStatus(call.getToLine(), 1);
            callTableModel.addRow(new Object[]{call.getFromLine(), call.getToLine(), currentTime + call.getDuration()});
            log("✅ Connected: " + call);
        } else {
            delayedQueue.offer(new DelayedCall(call, currentTime + 2, 1));
            increment(delayedField);
            log("⏳ Delayed (Try 1): " + call);
        }

        // Note: NO automatic retry here — manual via button

        // Disconnect expired calls safely
        for (int i = activeCalls.size() - 1; i >= 0; i--) {
            Call c = activeCalls.get(i);
            if (currentTime >= c.getStartTime() + c.getDuration()) {
                switchboard.disconnectCall(c);
                updateLineStatus(c.getFromLine(), 0);
                updateLineStatus(c.getToLine(), 0);
                log("🔌 Disconnected: " + c);

                if (i < callTableModel.getRowCount()) {
                    callTableModel.removeRow(i);
                }

                increment(completedField);
                activeCalls.remove(i);
            }
        }
    }

    private void retryDelayedCalls() {
        Queue<DelayedCall> tempQueue = new LinkedList<>();
        while (!delayedQueue.isEmpty()) {
            DelayedCall delayed = delayedQueue.poll();
            if (delayed.retryTime <= currentTime) {
                boolean retrySuccess = switchboard.connectCall(delayed.call);
                if (retrySuccess) {
                    activeCalls.add(delayed.call);
                    updateLineStatus(delayed.call.getFromLine(), 1);
                    updateLineStatus(delayed.call.getToLine(), 1);
                    callTableModel.addRow(new Object[]{delayed.call.getFromLine(), delayed.call.getToLine(), currentTime + delayed.call.getDuration()});
                    log("🔁 Retry Success: " + delayed.call);
                    increment(retrySuccessField);
                } else if (delayed.attempt < 2) {
                    delayed.retryTime = currentTime + 2;
                    delayed.attempt++;
                    tempQueue.offer(delayed);
                    log("🔁 Retrying Again (Try " + delayed.attempt + "): " + delayed.call);
                } else {
                    log("❌ Dropped after 2 tries: " + delayed.call);
                    increment(droppedField);
                }
            } else {
                tempQueue.offer(delayed);
            }
        }
        delayedQueue = tempQueue;
    }

    private void updateLineStatus(int line, int status) {
        if (line >= 1 && line <= 8) {
            lineFields[line - 1].setText(String.valueOf(status));
        }
    }

    private void increment(JTextField field) {
        try {
            int value = Integer.parseInt(field.getText());
            field.setText(String.valueOf(value + 1));
        } catch (NumberFormatException e) {
            field.setText("1");
        }
    }

    private void log(String msg) {
        logArea.append("[" + currentTime + "] " + msg + "\n");
    }

    static class DelayedCall {
        Call call;
        int retryTime;
        int attempt;

        DelayedCall(Call call, int retryTime, int attempt) {
            this.call = call;
            this.retryTime = retryTime;
            this.attempt = attempt;
        }
    }
}
