package com.hugoviallon.opta.model;

import java.time.Instant;
import java.util.TreeSet;

public class TaskGroup {

    private int id;
    private TreeSet<Task> tasks;

    private Instant dueDate;

    public int getId() {
        return id;
    }

    public TaskGroup setId(int id) {
        this.id = id;
        return this;
    }

    public TreeSet<Task> getTasks() {
        return tasks;
    }

    public TaskGroup setTasks(TreeSet<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public TaskGroup setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }
}
