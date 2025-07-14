package main.model;

import java.util.HashSet;
import java.util.Set;

public class Switchboard {
    private int totalLines;
    private int maxLinks;

    // Tracks busy lines
    private Set<Integer> busyLines;

    // Tracks current number of links in use
    private int usedLinks;

    public Switchboard(int totalLines, int maxLinks) {
        this.totalLines = totalLines;
        this.maxLinks = maxLinks;
        this.busyLines = new HashSet<>();
        this.usedLinks = 0;
    }

    // Try to connect a call, return true if success, false if busy or blocked
    public boolean connectCall(Call call) {
        int from = call.getFromLine();
        int to = call.getToLine();

        // Check if lines are valid
        if (from < 1 || from > totalLines || to < 1 || to > totalLines || from == to) {
            return false;
        }

        // If lines are busy or max links reached, can't connect
        if (busyLines.contains(from) || busyLines.contains(to) || usedLinks >= maxLinks) {
            return false;
        }

        // Mark lines as busy and increment links used
        busyLines.add(from);
        busyLines.add(to);
        usedLinks++;

        return true;
    }

    // Disconnect a call, freeing lines and reducing link usage
    public void disconnectCall(Call call) {
        int from = call.getFromLine();
        int to = call.getToLine();

        // Remove lines from busy set if present
        busyLines.remove(from);
        busyLines.remove(to);

        // Reduce used links count but ensure it doesn't go below zero
        if (usedLinks > 0) {
            usedLinks--;
        }
    }

    // Getters
    public int getUsedLinks() {
        return usedLinks;
    }

    public int getMaxLinks() {
        return maxLinks;
    }

    public Set<Integer> getBusyLines() {
        return busyLines;
    }
}
