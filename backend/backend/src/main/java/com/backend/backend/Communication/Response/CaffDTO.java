package com.backend.backend.Communication.Response;

import java.util.ArrayList;

public class CaffDTO {

    private ArrayList<Ciff> ciffs;

    public CaffDTO(ArrayList<Ciff> ciffs) {
        this.ciffs = ciffs;
    }

    public ArrayList<Ciff> getCiffs() {
        return ciffs;
    }
}
