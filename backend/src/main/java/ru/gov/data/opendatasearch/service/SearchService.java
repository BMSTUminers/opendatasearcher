package ru.gov.data.opendatasearch.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.springframework.stereotype.Service;
import ru.gov.data.opendatasearch.Indexer;
import ru.gov.data.opendatasearch.datasource.Record;
import ru.gov.data.opendatasearch.dto.KMLSearchResult;
import ru.gov.data.opendatasearch.dto.RawSearchResult;
import ru.gov.data.opendatasearch.dto.RawSearchResult.RawSearchElement;
import ru.gov.data.opendatasearch.dto.SearchResult;
import ru.gov.data.opendatasearch.dto.TableSearchResult;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    public SearchService() {
    }

    public SearchResult query(String query) {
        if (query.equals("kml"))
            return dummyKML();
        else if (query.equals("raw"))
            return dummyRaw();
        return searchTable(query);
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

    public SearchResult searchTable(String query) {
        try {
            Indexer indexer;
            indexer = new Indexer("../indexer/index/data");
            List<Record> list;
            // list = indexer.search("филиал", true);
            list = indexer.search(query, true);
            // list = indexer.search("kizlar", true);
            Set<String> headers = new LinkedHashSet<>();
            List<Map<String, String>> maps = list.stream()
                    .map(record -> {
                        Gson gson = new Gson();
                        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
                        Map<String,String> map =  gson.fromJson(record.getJson(), stringStringMap);
                        return map;
                    }).collect(Collectors.toList());
            for (Map<String, String> map : maps) {
                map.entrySet().forEach(en -> headers.add(en.getKey()));
            }

            List<String> hdrList =  headers.stream().collect(Collectors.toList());
            TableSearchResult result = new TableSearchResult(hdrList);
            for (Map<String, String> map : maps) {
                List<String> row = new ArrayList<>(hdrList.size());
                for (int i = 0; i<hdrList.size(); i++) {
                    if (map.containsKey(hdrList.get(i))) {
                        row.add(map.get(hdrList.get(i)));
                    } else {
                        row.add(    "---");
                    }
                }
                result.addRow(row);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
