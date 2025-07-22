package main.model;

import java.util.Random;

public class Call {
    private int fromLine;
    private int toLine;
    private int startTime;
    private int duration;

    public Call(int fromLine, int toLine, int startTime, int duration) {
        this.fromLine = fromLine;
        this.toLine = toLine;
        this.startTime = startTime;
        this.duration = duration;
    }

    // Getters
    public int getFromLine() {
        return fromLine;
    }

    public int getToLine() {
        return toLine;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    // Generate random call with constraints:
    // fromLine and toLine between 1 and maxLines, and from != to
    public static Call generateRandomCall(int currentTime, int maxLines) {
        Random rand = new Random();
        int from = rand.nextInt(maxLines) + 1;
        int to;
        do {
            to = rand.nextInt(maxLines) + 1;
        } while (to == from);

        // Duration between 1 to 4 units of time
        int dur = rand.nextInt(4) + 1;

        return new Call(from, to, currentTime, dur);
    }

    @Override
    public String toString() {
        return "Call[from=" + fromLine + ", to=" + toLine + ", duration=" + duration + "]";
    }
}
