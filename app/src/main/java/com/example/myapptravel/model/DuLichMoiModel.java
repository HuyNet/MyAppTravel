package com.example.myapptravel.model;

import java.util.List;

public class DuLichMoiModel {
    boolean success;
    String message;
    List<DuLichMoi> result;

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

    public List<DuLichMoi> getResult() {
        return result;
    }

    public void setResult(List<DuLichMoi> result) {
        this.result = result;
    }
}
