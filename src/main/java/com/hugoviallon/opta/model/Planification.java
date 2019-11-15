package com.hugoviallon.opta.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

@PlanningSolution()
public class Planification {

    private List<Task> tasks;
    private List<Machine> machines;
    private HardMediumSoftLongScore score;
    private CountableValueRange<Instant> startPeriodRange;
    private Parameters parameters;


    private Instant start;
    private Instant end;

    public Planification() {

    }

    public Planification(Instant start, Instant end) {
        this.startPeriodRange = ValueRangeFactory.createTemporalValueRange(start, end, 1L, ChronoUnit.MINUTES);
        this.start = start;
        this.end = end;
    }

    @ValueRangeProvider(id = "timeRange")
    public CountableValueRange<Instant> getStartPeriodRange() {
        return this.startPeriodRange;
    }

    @PlanningEntityCollectionProperty
    public List<Task> getTasks() {
        return tasks;
    }

    @PlanningScore
    public HardMediumSoftLongScore getScore() {
        return score;
    }

    @ValueRangeProvider(id = "machineRange")
    public List<Machine> getMachines() {
        return machines;
    }

    public void setScore(HardMediumSoftLongScore score) {
        this.score = score;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Planification setParameters(Parameters parameters) {
        this.parameters = parameters;
        return this;
    }
}
