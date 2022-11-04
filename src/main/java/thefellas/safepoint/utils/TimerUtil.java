package thefellas.safepoint.utils;

public class TimerUtil {
    private long time = -1L;
    long startTime;
    long delay;
    boolean paused;
    public long lastMS = System.currentTimeMillis();

    public TimerUtil() {
        this.startTime = System.currentTimeMillis();
        this.delay = 0L;
        this.paused = false;
    }

    public boolean passedMs(long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long getTime() {
        return this.time;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis()-lastMS > time ) {
            if (reset)
                reset();

            return true;
        }

        return false;
    }
}
