package com.hugoviallon.opta.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator;

public class PlanificationIncrementalScoreCalculator implements IncrementalScoreCalculator<Planification> {

    private Planification planification;
    private HardMediumSoftLongScore score = HardMediumSoftLongScore.ZERO;

    private Map<Machine, List<Task>> taskByMachine;
    private Map<Integer, List<Task>> taskByGroup;

    @Override
    public void resetWorkingSolution(Planification workingSolution) {
        taskByMachine = new HashMap<>();
        this.planification = workingSolution;

        score = HardMediumSoftLongScore.ZERO;

        taskByMachine = new HashMap<>();
        taskByGroup = new HashMap<>();

        for (Task task : planification.getTasks()) {
            if (!taskByMachine.containsKey(task.getMachine()))
                taskByMachine.put(task.getMachine(), new ArrayList<>());

            taskByMachine.get(task.getMachine()).add(task);

            if (!taskByGroup.containsKey(task.getGroup().getId()))
                taskByGroup.put(task.getGroup().getId(), new ArrayList<>());

            taskByGroup.get(task.getGroup().getId()).add(task);
        }

        taskByGroup.values().forEach(e -> e.sort(Comparator.comparing(Task::getId)));

        for (Task task : planification.getTasks()) {
            score = score.subtract(ruleNoOverlap(task));
            score = score.subtract(ruleForcePlanningOfTask(task));
            score = score.subtract(rulePrecedence(task));
            score = score.subtract(ruleLatePlanningHard(task));
            score = score.subtract(ruleLatePlanning(task));
        }
    }

    @Override
    public void beforeEntityAdded(Object entity) {
    }

    @Override
    public void afterEntityAdded(Object entity) {
    }

    @Override
    public void beforeVariableChanged(Object entity, String variableName) {
        Task task = (Task)entity;

        if (!taskByMachine.containsKey(task.getMachine()))
            taskByMachine.put(task.getMachine(), new ArrayList<>());
        if (variableName.equals("machine"))
            taskByMachine.get(task.getMachine()).remove(task);

        score = score.add(ruleNoOverlap(task));
        score = score.add(ruleForcePlanningOfTask(task));
        score = score.add(rulePrecedence(task));
        score = score.add(ruleLatePlanningHard(task));
        score = score.add(ruleLatePlanning(task));
    }

    @Override
    public void afterVariableChanged(Object entity, String variableName) {
        Task task = (Task)entity;

        if (!taskByMachine.containsKey(task.getMachine()))
            taskByMachine.put(task.getMachine(), new ArrayList<>());
        if (variableName.equals("machine"))
            taskByMachine.get(task.getMachine()).add(task);

        score = score.subtract(ruleNoOverlap(task));
        score = score.subtract(ruleForcePlanningOfTask(task));
        score = score.subtract(rulePrecedence(task));
        score = score.subtract(ruleLatePlanningHard(task));
        score = score.subtract(ruleLatePlanning(task));
    }

    @Override
    public void beforeEntityRemoved(Object entity) {

    }

    @Override
    public void afterEntityRemoved(Object entity) {

    }

    @Override
    public Score calculateScore() {
        return score;
    }

    /////////////////////////////////////////////////////
    ///
    ///     RULES
    ///
    /////////////////////////////////////////////////////

    private HardMediumSoftLongScore ruleNoOverlap(Task task) {
        if (!task.isPlanified()) return HardMediumSoftLongScore.ZERO;

        int hs = 0;

        for (Task otherTask : taskByMachine.get(task.getMachine())) {
            if (!task.isPlanified() || !otherTask.isPlanified() || task.equals(otherTask)) continue;
            if (otherTask.getStart().isBefore(task.getEnd()) && otherTask.getEnd().isAfter(task.getStart()))
               hs++;
        }

        return HardMediumSoftLongScore.of(hs, 0, 0);
    }

    private HardMediumSoftLongScore ruleForcePlanningOfTask(Task task) {
        if (!task.isPlanified() || task.getMachine() == null)
            return HardMediumSoftLongScore.of(0, 1,0 );
        return HardMediumSoftLongScore.ZERO;
    }

    private HardMediumSoftLongScore rulePrecedence(Task task) {
        Task previousTask = null;
        for (Task currentTask : taskByGroup.get(task.getGroup().getId())) {
            if(previousTask != null && currentTask.isPlanified()) {
                if (!previousTask.isPlanified() || currentTask.getStart().isBefore(previousTask.getEnd())) {
                    return HardMediumSoftLongScore.ofHard(1);
                }
            }
            previousTask = currentTask;
        }

        return HardMediumSoftLongScore.ZERO;
    }

    private HardMediumSoftLongScore ruleLatePlanningHard(Task task) {
        if (task.isPlanified() && task.getEnd().isAfter(task.getGroup().getDueDate())) return HardMediumSoftLongScore.ofHard(1);
        return HardMediumSoftLongScore.ZERO;
    }

    private HardMediumSoftLongScore ruleLatePlanning(Task task) {
        if (!task.isPlanified()) return HardMediumSoftLongScore.ZERO;
        return HardMediumSoftLongScore.ofSoft(Duration.between(task.getEnd(), task.getGroup().getDueDate()).toMinutes());
    }
}
