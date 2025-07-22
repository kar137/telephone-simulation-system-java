package main.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import model.*;

public class DelayedCallFrame extends JFrame {

    private JLabel clockLabel;
    private JTextArea logArea;

    private int currentTime = 0;
    private Timer simulationTimer;

    private Switchboard switchboard;
    private ArrayList<Call> activeCalls = new ArrayList<>();
    private Queue<DelayedCall> delayedQueue = new LinkedList<>();

    public DelayedCallFrame() {
        setTitle("📞 Delayed Call Simulation");
        setSize(850, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        switchboard = new Switchboard(8, 3); // 8 lines, 3 connections

        // Clock label
        clockLabel = new JLabel("⏰ Time: 0", JLabel.CENTER);
        clockLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        add(clockLabel, BorderLayout.NORTH);

        // Log output
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        // Stop button
        JPanel controlPanel = new JPanel();
        JButton stopBtn = new JButton("🛑 Stop");
        controlPanel.add(stopBtn);
        add(controlPanel, BorderLayout.SOUTH);

        stopBtn.addActionListener(e -> simulationTimer.stop());

        // Timer-based simulation
        simulationTimer = new Timer(1000, e -> tick());
        simulationTimer.start();

        setVisible(true);
    }

    private void tick() {
        currentTime++;
        clockLabel.setText("⏰ Time: " + currentTime);

        // Step 1: Generate a new random call
        Call call = Call.generateRandomCall(currentTime, 4);
        boolean success = switchboard.connectCall(call);

        if (success) {
            activeCalls.add(call);
            log("✅ Connected: " + call);
        } else {
            // Push to delayed queue
            delayedQueue.offer(new DelayedCall(call, currentTime + 2, 1));
            log("⏳ Delayed (Try 1): " + call);
        }

        // Step 2: Retry eligible delayed calls
        Queue<DelayedCall> tempQueue = new LinkedList<>();
        while (!delayedQueue.isEmpty()) {
            DelayedCall delayed = delayedQueue.poll();
            if (delayed.retryTime <= currentTime) {
                boolean retrySuccess = switchboard.connectCall(delayed.call);
                if (retrySuccess) {
                    activeCalls.add(delayed.call);
                    log("🔁 Retry Success: " + delayed.call);
                } else if (delayed.attempt < 2) {
                    delayed.retryTime = currentTime + 2;
                    delayed.attempt++;
                    log("🔁 Retrying Again (Try " + delayed.attempt + "): " + delayed.call);
                    tempQueue.offer(delayed);
                } else {
                    log("❌ Dropped after 2 tries: " + delayed.call);
                }
            } else {
                tempQueue.offer(delayed);
            }
        }
        delayedQueue = tempQueue;

        // Step 3: End calls whose duration has expired
        ArrayList<Call> toRemove = new ArrayList<>();
        for (Call c : activeCalls) {
            if (currentTime - c.getStartTime() >= c.getDuration()) {
                switchboard.disconnectCall(c);
                log("🔌 Disconnected: " + c);
                toRemove.add(c);
            }
        }
        activeCalls.removeAll(toRemove);
    }

    private void log(String msg) {
        logArea.append("[" + currentTime + "] " + msg + "\n");
    }

    // Inner class for retry logic
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
