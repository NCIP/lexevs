
package org.LexGrid.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Tool for simple "heap profiling" over long runs where it is inconvenient to
 * use an actual profiling tool.
 * 
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: 6045 $ checked in on $Date: 2007-10-10
 *          12:19:17 +0000 (Wed, 10 Oct 2007) $
 */
public class SimpleMemUsageReporter {
    // private static long lastCheck;
    // private static long lastHeapUsage;
    private static Snapshot lastSnapShot, currentSnapShot;
    private static Runtime runtime = Runtime.getRuntime();

    static {
        init();
    }

    /**
     * Initialize values
     * 
     */
    public static void init() {
        lastSnapShot = new Snapshot(new Date().getTime(), 0);
        currentSnapShot = lastSnapShot;
        // lastCheck = new Date().getTime();
        // lastHeapUsage = 0;
    }

    /**
     * Reinitialize
     * 
     */
    public static void reset() {
        init();
    }

    /**
     * Takes a snapshot of the current heap
     * 
     * @return - A Snapshot object that contains timestamps and heap values at
     *         the current time
     */
    public static Snapshot snapshot() {
        long currentCheck = new Date().getTime();
        runtime.gc();
        long currentHeapUsage = runtime.totalMemory() - runtime.freeMemory();

        lastSnapShot = currentSnapShot;
        currentSnapShot = new Snapshot(currentCheck, currentHeapUsage);

        // lastHeapUsage = currentHeapUsage;
        // lastCheck = currentCheck;

        return currentSnapShot;
    }

    /**
     * Convenience class for holding information about the heap
     * 
     */
    public static class Snapshot {

        private long currentTime, currentHeapUsage;

        public long getTime() {
            return currentTime;
        }

        /**
         * 
         * @param previous
         *            - Optional, if null, will return the delta from the static
         *            Snapshot on SimpleMemUsageReporter
         * @return
         */
        public long getTimeDelta(Snapshot previous) {
            if (previous == null)
                return currentTime - lastSnapShot.getTime();
            else
                return currentTime - previous.getTime();
        }

        public long getHeapUsage() {
            return currentHeapUsage;
        }

        /**
         * 
         * @param previous
         *            - Optional, if null, will return the delta from the static
         *            Snapshot on SimpleMemUsageReporter
         * @return - The difference in heap usage between snapshots
         */
        public long getHeapUsageDelta(Snapshot previous) {
            if (previous == null)
                return currentHeapUsage - lastSnapShot.getHeapUsage();
            else
                return currentHeapUsage - previous.getHeapUsage();
        }

        public Snapshot(long currentTime, long currentHeapUsage) {
            this.currentTime = currentTime;
            this.currentHeapUsage = currentHeapUsage;
        }

    }

    /**
     * Convenience printer
     * 
     * @param snap
     *            - the Snapshot to print to System.out
     */
    public static void print(Snapshot snap) {
        print(snap, "");
    }

    /**
     * Convenience printer with description
     * 
     * @param snap
     *            - the Snapshot to print to System.out
     * @param message
     *            - the message to tack on for later identification
     */
    public static void print(Snapshot snap, String message) {
        System.out.println(new Date(snap.getTime()) + ", " + formatTimeDiff(snap.getTimeDelta(null))
                + " since last check: " + formatMemStat(snap.getHeapUsage()) + " total usage, a change of "
                + formatMemStat(snap.getHeapUsageDelta(null)) + "\t" + message);
    }

    /**
     * Static method for changing a long representing memory size to a human
     * friendly string
     * 
     * @param value
     * @return
     */
    public static String formatMemStat(long value) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String valAsString = new Long(value).toString();
        if ((valAsString.length() >= 10 && value > 0) || valAsString.length() >= 11)
            return formatter.format(value / (float) 1073741824) + " GB";
        else if ((valAsString.length() >= 7 && value > 0) || valAsString.length() >= 8)
            return formatter.format(value / (float) 1048576) + " MB";
        else if ((valAsString.length() >= 4 && value > 0) || valAsString.length() >= 5)
            return formatter.format(value / (float) 1024) + " KB";

        return valAsString + " bytes";
    }

    /**
     * Used to convert the time difference between snapshots to a human readable
     * format
     * 
     * @param timeDiff
     * @return
     */
    public static String formatTimeDiff(long timeDiff) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date(timeDiff));
    }

    /**
     * A simple test to make sure the output is sensible
     * 
     * @param args
     */
    public static void main(String[] args) {
        init();
        print(snapshot());
        Date whatTimeIsIt = new Date();

        print(snapshot());
        Date whatTimeIsIt2 = new Date();
        whatTimeIsIt.compareTo(whatTimeIsIt2);

        print(snapshot());
        ArrayList list = new ArrayList();
        for (int i = 0; i < 20; i++)
            list.add(new Date());
        print(snapshot());
        list = null;
        whatTimeIsIt = null;
        whatTimeIsIt2 = null;
        print(snapshot());
        runtime.gc();
        print(snapshot());
        reset();
        print(snapshot());
    }

}