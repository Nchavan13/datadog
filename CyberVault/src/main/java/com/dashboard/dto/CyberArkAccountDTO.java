package com.dashboard.dto;


public class CyberArkAccountDTO {
    private String name;
    private String lastModifiedDate;

    public CyberArkAccountDTO(String serviceAccount1, String date) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

