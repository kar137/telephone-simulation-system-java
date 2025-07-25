# Telephone Simulation System (Java)

A departmental project for Computer Science and Engineering 

**Project Report:** https://drive.google.com/file/d/10auj67BA5MGRc80B0ezojyXjdNBIaRgv/view?usp=sharing

**Submitted by:** Karan Bista  
**CRN:** 022-328

---

## Overview

This project simulates a telephone network using Java and Swing GUI. It models telephone lines, a switchboard, and call handling scenarios using visual frames and tables. Two main simulation modes are provided:

- **Lost Call Simulation:** Calls that cannot be connected (due to busy lines or limited links) are blocked and counted as lost.
- **Delayed Call Simulation:** Busy or blocked calls are queued and retried; statistics for delayed, dropped, and successfully retried calls are displayed.

The system visually shows line status, call progress, and simulation statistics in real-time.

---

## Features

- **Telephone Network Simulation:** Model a fixed number of lines and maximum simultaneous connections.
- **Switchboard Logic:** Handles connection, disconnection, line and link status tracking.
- **Call Generation:** Randomly generate calls with random lines, duration, and arrival times.
- **Lost Call Handling:** Block and count calls that cannot be connected.
- **Delayed Call Handling:** Retry failed calls up to twice before dropping.
- **Live GUI:** Swing-based windows show line status, call progress, and summary statistics.
- **User Controls:** Start, stop, and retry operations in the GUI.

---

## How It Works

- **Welcome Screen:** Shows project details and lets you choose the simulation mode.
- **Lost Call Frame:** Each call either connects or is lost if lines/links are busy.
- **Delayed Call Frame:** Failed calls enter a delayed queue and are retried; success/failure is tracked.
- **Statistics:** Displays completed, lost, delayed, dropped, and total processed calls.

---

## Project Directory Structure

```
src/
  main/
    gui/
      WelcomePage.java          // Main menu & info
      LostCallFrame.java        // Lost call simulation
      DelayedCallFrame.java     // Delayed call simulation
    model/
      Call.java                 // Call data & generation logic
      Switchboard.java          // Switchboard logic
      TelephoneLine.java        // Line status
    Main.java                   // Entry point
```

---

## How to Run

1. **Requirements:** Java 8+.
2. **Clone and Compile:**
   ```bash
   git clone https://github.com/kar137/telephone-simulation-system-java.git
   cd telephone-simulation-system-java/src
   javac main/Main.java
   java main.Main
   ```
   Or open in any Java IDE and run `Main.java`.

---

## Screenshots

<img width="843" height="727" alt="Image" src="https://github.com/user-attachments/assets/fd48bb9c-b3fd-4f6d-9c50-d9a4f408c109" />
<br><br>
<img width="1101" height="736" alt="Image" src="https://github.com/user-attachments/assets/6d1253cd-f3a1-4a18-8cd6-61f405ce8a30" />
<br><br>
<img width="1166" height="789" alt="Image" src="https://github.com/user-attachments/assets/cabd3b30-0419-4bb0-a976-877259b3e6a8" />

---

## References

- All code and logic are original for departmental submission.
- For further details, see source files in `src/main/gui` and `src/main/model`.

---

## Author

- Karan Bista  
- CRN: 022-328  
- Department: Computer Science and Engineering
