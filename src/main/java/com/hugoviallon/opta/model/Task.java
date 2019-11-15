package com.hugoviallon.opta.model;

import com.hugoviallon.opta.services.TaskDurationService;
import java.time.Duration;
import java.time.Instant;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Task implements Comparable<Task> {

    private Integer id;
    private Instant start;
    private Instant end;
    private Machine machine;
    private String name;
    private Duration duration;
    private TaskGroup group;

    public Task() {
    }

    public Task(Machine machine, String name) {
        this.machine = machine;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }

    @PlanningVariable(valueRangeProviderRefs = {"timeRange"}, nullable = true)
    public Instant getStart() {
        return start;
    }

    @PlanningVariable(valueRangeProviderRefs = {"machineRange"}, nullable = true)
    public Machine getMachine() {
        return machine;
    }

    public Duration getDuration() {
        if (duration == null) duration = TaskDurationService.getTaskDuration();
        return duration;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public void setStart(Instant start) {
        this.start = start;
        if (start != null)
            this.end = start.plus(getDuration());
        else
            this.end = null;
    }

    public String getName() {
        return name;
    }

    public Instant getEnd() {
        return start.plus(getDuration());
    }

    public boolean isPlanified() {
        return this.start != null && this.end != null && this.machine != null;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public Task setGroup(TaskGroup group) {
        this.group = group;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("s:").append(this.start).append("; n:").append(name).append("; m:").append(machine);
        return sb.toString();
    }

    @Override
    public int compareTo(Task o) {
        return id.compareTo(o.getId());
    }
}
