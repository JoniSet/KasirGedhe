package com.example.kasirroti.DataSqlite;

public class UpdateInfoSqlite {
    private String id, status;

    public UpdateInfoSqlite() {
    }

    public UpdateInfoSqlite(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
