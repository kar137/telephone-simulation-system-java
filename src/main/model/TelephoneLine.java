package main.model;

public class TelephoneLine {
    private int lineNumber;
    private boolean busy;

    public TelephoneLine(int lineNumber) {
        this.lineNumber = lineNumber;
        this.busy = false;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
