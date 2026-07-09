package com.avance.marketflow.model;

public class Provider {
    private long id;
    private String name;
    private String ruc;

    public Provider(long id, String name, String ruc) {
        this.id = id;
        this.name = name;
        this.ruc = ruc;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getRuc() { return ruc; }
}

