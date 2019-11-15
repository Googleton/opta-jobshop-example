package com.hugoviallon.opta.model;

public class Machine {
    private Integer id;
    private String name;

    public Machine(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
