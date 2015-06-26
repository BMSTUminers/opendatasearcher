package ru.gov.data.opendatasearch.dto;

public class KMLSearchResult implements SearchResult {
    private String kmlRef;

    public String getType() {
        return "KML";
    }

    public KMLSearchResult(String kmlRef) {
        this.kmlRef = kmlRef;
    }

    public String getKmlRef() {
        return this.kmlRef;
    }
}
