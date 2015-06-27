package ru.gov.data.opendatasearch.dto;

import ru.gov.data.opendatasearch.datasource.Passport;

import java.util.ArrayList;
import java.util.List;

public class RawSearchResult implements SearchResult {

    public RawSearchResult() {
    }

    private List<Passport> docs = new ArrayList<>();

    public String getType() {
        return "data";
    }

    public void addDoc(Passport element) {
        this.docs.add(element);
    }

    public List<Passport> getDocs() {
        return this.docs;
    }

}
