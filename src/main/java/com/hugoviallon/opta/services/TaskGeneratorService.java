package com.hugoviallon.opta.services;

import com.hugoviallon.opta.model.Machine;
import com.hugoviallon.opta.model.Task;

import com.hugoviallon.opta.model.TaskGroup;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class TaskGeneratorService {
    private static List<String> names = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l");

    public static List<Task> generateTasks(List<Machine> machines, int groupCount, int tasksPerGroup, Instant start, Instant end) {
        List<Task> tasks = new ArrayList<>();

        for (int j = 0; j < groupCount; j++) {
            Instant dueDate = Instant.ofEpochMilli(ThreadLocalRandom.current().nextLong(start.toEpochMilli() + 7 * 24 * 60 * 60 * 1000, end.toEpochMilli()));

            TaskGroup group = new TaskGroup();
            group.setId(j);

            TreeSet<Task> tForG = new TreeSet<>();

            for (int i = 0; i < tasksPerGroup; i++) {
                int machine = ThreadLocalRandom.current().nextInt(0, machines.size());
                Task task = new Task(machines.get(machine), names.get(i));
                task.setId(i);
                task.setGroup(group);
                tasks.add(task);
                tForG.add(task);
            }

            group.setTasks(tForG)
                 .setDueDate(dueDate);
        }

        return tasks;
    }
}
