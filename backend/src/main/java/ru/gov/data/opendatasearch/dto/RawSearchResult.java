package ru.gov.data.opendatasearch.dto;

import java.util.ArrayList;
import java.util.List;

public class RawSearchResult implements SearchResult {
    private final List<RawSearchResult.RawSearchElement> docs = new ArrayList();

    public RawSearchResult() {
    }

    public String getType() {
        return "data";
    }

    public void addDoc(RawSearchResult.RawSearchElement element) {
        this.docs.add(element);
    }

    public List<RawSearchResult.RawSearchElement> getDocs() {
        return this.docs;
    }

    public static class RawSearchElement {
        private String uri;
        private String title;
        private String text;

        public RawSearchElement() {
        }

        public String getUri() {
            return this.uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
