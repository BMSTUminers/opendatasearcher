package ru.gov.data.opendatasearch.dto;

public class KMLSearchResult implements SearchResult {
    private String kmlRef;

    public String getType() {
        return "KML";
    }

    @Override
    public String getTitle() {
        return "Карта";
    }

    public KMLSearchResult(String kmlRef) {
        this.kmlRef = kmlRef;
    }

    public String getKmlRef() {
        return this.kmlRef;
    }
}
