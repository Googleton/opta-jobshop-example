package com.hugoviallon.opta.model;

import java.time.Instant;
import java.util.List;

/**
 * Passed to testbed for display
 */
public class Parameters {

    private Instant start;
    private Instant end;
    private List<Machine> machines;

    public Instant getStart() {
        return start;
    }

    public Parameters setStart(Instant start) {
        this.start = start;
        return this;
    }

    public Instant getEnd() {
        return end;
    }

    public Parameters setEnd(Instant end) {
        this.end = end;
        return this;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public Parameters setMachines(List<Machine> machines) {
        this.machines = machines;
        return this;
    }
}
