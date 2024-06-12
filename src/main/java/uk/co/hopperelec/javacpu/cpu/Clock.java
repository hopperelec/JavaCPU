package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.lang.IllegalStateException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

class Clock {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture = null;
    private final ControlUnit controlUnit;
    final int hertz;

    Clock(ControlUnit controlUnit, int hertz) {
        this.hertz = hertz;
        this.controlUnit = controlUnit;
    }
    
    void start() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled())
            throw new IllegalStateException("Tried to start clock, but clock already started");
        scheduledFuture = executor.scheduleAtFixedRate(() -> {
            try {
                Utils.println("Clock", "Starting new cycle\n", true);
                controlUnit.startNewCycle();
            } catch (Exception e) {
                Utils.println("Error", e.toString());
            }
        }, 0, 1000000000/hertz, TimeUnit.NANOSECONDS);
    }
    
    void halt() {
        if (scheduledFuture == null)
            throw new IllegalStateException("Tried to halt clock, but clock hasn't started yet");
        scheduledFuture.cancel(false);
    }
}