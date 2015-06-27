package ru.gov.data.opendatasearch.dto;

import java.util.ArrayList;
import java.util.List;

public class TableSearchResult implements SearchResult {
    private final List<String> headers;
    private final List<List<String>> rows = new ArrayList<>();
    private final int numFields;
    private String title;

    public TableSearchResult(List<String> headers) {
        this.headers = headers;
        this.numFields = headers.size();
    }

    public List<String> getHeaders() {
        return this.headers;
    }

    public List<List<String>> getRows() {
        return this.rows;
    }

    public int getNumFields() {
        return this.numFields;
    }

    public void addRow(List<String> row) {
        if(row.size() != this.numFields) {
            throw new IllegalArgumentException("Number of fields should be " + this.numFields);
        } else {
            this.rows.add(row);
        }
    }

    public String getType() {
        return "table";
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}