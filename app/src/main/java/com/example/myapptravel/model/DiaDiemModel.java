package com.example.myapptravel.model;

import java.util.List;

public class DiaDiemModel {
    boolean success;
    String message;
    List<DiaDiem> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DiaDiem> getResult() {
        return result;
    }

    public void setResult(List<DiaDiem> result) {
        this.result = result;
    }
}
