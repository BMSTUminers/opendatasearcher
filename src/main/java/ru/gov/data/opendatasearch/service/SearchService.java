package ru.gov.data.opendatasearch.service;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.springframework.stereotype.Service;
import ru.gov.data.opendatasearch.dto.KMLSearchResult;
import ru.gov.data.opendatasearch.dto.RawSearchResult;
import ru.gov.data.opendatasearch.dto.RawSearchResult.RawSearchElement;
import ru.gov.data.opendatasearch.dto.SearchResult;
import ru.gov.data.opendatasearch.dto.TableSearchResult;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SearchService {
    public SearchService() {
    }

    public SearchResult query(String query) {
        if (query.equals("kml"))
            return dummyKML();
        else if (query.equals("table"))
            return dummyTable();
        return dummyRaw();
    }

    public SearchResult dummyRaw() {
        RawSearchResult result = new RawSearchResult();

        for(int i = 0; i < 10; ++i) {
            RawSearchElement element = new RawSearchElement();
            element.setTitle("Search Result " + i);
            element.setUri("http://google.com");
            element.setText("Sample text " + i);
            result.addDoc(element);
        }

        return result;
    }

    public SearchResult dummyKML() {
        return new KMLSearchResult("http://104.154.47.78:8081/search/kml?query=kml");
    }

    public SearchResult dummyTable() {
        List<String> headers = new ArrayList<>();
        headers.add("1");
        headers.add("2");
        headers.add("3");
        headers.add("4");
        TableSearchResult result = new TableSearchResult(headers);

        for(int i = 0; i < 10; ++i) {
            List<String> row = new ArrayList<>();
            for(int j = 0; j < 4; ++j) {
                row.add("cell" + i + "-" + j);
            }
            result.addRow(row);
        }

        return result;
    }

    public String kml(String query) {
        Random r = new Random();
        Kml kml = new Kml();
        Document d = kml.createAndSetDocument();

        for(int i = 0; i < 10; ++i) {
            d.createAndAddPlacemark()
                    .withName("Test Placemark " + i)
                    .withDescription("Around Red Square")
                    .createAndSetPoint()
                    .addToCoordinates(37.62D + r.nextDouble() / 100.0D, 55.7542D + r.nextDouble() / 100.0D);
        }

        StringWriter writer = new StringWriter();
        kml.marshal(writer);
        return writer.toString();
    }
}
