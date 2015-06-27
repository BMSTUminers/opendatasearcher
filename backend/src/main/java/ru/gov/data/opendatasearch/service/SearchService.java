package ru.gov.data.opendatasearch.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.springframework.stereotype.Service;
import ru.gov.data.opendatasearch.Indexer;
import ru.gov.data.opendatasearch.datasource.Passport;
import ru.gov.data.opendatasearch.datasource.Record;
import ru.gov.data.opendatasearch.dto.KMLSearchResult;
import ru.gov.data.opendatasearch.dto.RawSearchResult;
import ru.gov.data.opendatasearch.dto.SearchResult;
import ru.gov.data.opendatasearch.dto.TableSearchResult;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private Indexer indexer;
    private Map<String, String> cash = new HashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    public SearchService() {
        try {
            indexer = new Indexer("../indexer/index/data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SearchResult> query(String query) {
        List<SearchResult> results = new ArrayList<>();
        searchTable(query).forEach(results::add);
        RawSearchResult raw = searchRaw(query);
        if (raw.getDocs().size() > 0) results.add(raw);
        return results;
    }

    public RawSearchResult searchRaw(String query) {
        Map<String, String> query2 = new HashMap<String, String>();
        query2.put("description",query);
//			query2.put("description","Список отделений");
        List<Passport> list2 = indexer.searchPassport(query2, true);
        RawSearchResult results = new RawSearchResult();
        for (Passport record : list2) {
            System.out.println(record.getTitle());
            results.addDoc(record);
        }
        return results;
    }


    public List<SearchResult> searchTable(String query) {
        List<SearchResult> results = new ArrayList<>();
        try {
            List<Record> list;
            // list = indexer.search("филиал", true);
            list = indexer.search(query, true);

            List<Record> geRecords = list.stream().filter(rec ->
                            !rec.getGeo().isEmpty() && !rec.getGeo().equals("{}")
            ).collect(Collectors.toList());
            if (geRecords.size() > 0) {
                String kml = kml(geRecords);
                String key = String.valueOf(index.incrementAndGet());
                results.add(new KMLSearchResult("http://104.154.47.78:8081/search/kml?id=" + key));
                cash.put(key, kml);
            }

            Map<String, List<Record>> groups = new HashMap<>();
            list.forEach(el -> {
                String key = el.getId();
                if (!groups.containsKey(key)) {
                    groups.put(key, new ArrayList<>());
                }
                groups.get(key).add(el);
            });

            groups.entrySet().forEach(recrodList -> {
                Set<String> headers = new LinkedHashSet<>();
                List<Map<String, String>> maps = recrodList.getValue().stream()
                        .map(record -> {
                            Gson gson = new Gson();
                            Type stringStringMap = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> map = gson.fromJson(record.getJson(), stringStringMap);
                            return map;
                        }).collect(Collectors.toList());
                for (Map<String, String> map : maps) {
                    map.entrySet().forEach(en -> headers.add(en.getKey()));
                }

                List<String> hdrList = headers.stream().collect(Collectors.toList());
                TableSearchResult result = new TableSearchResult(hdrList);
                for (Map<String, String> map : maps) {
                    List<String> row = new ArrayList<>(hdrList.size());
                    for (int i = 0; i < hdrList.size(); i++) {
                        if (map.containsKey(hdrList.get(i))) {
                            row.add(map.get(hdrList.get(i)));
                        } else {
                            row.add("---");
                        }
                    }
                    result.addRow(row);
                }
                result.setTitle("Набор " + recrodList.getValue().get(0).getId());
                results.add(result);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String kml(List<Record> records) {
        Kml kml = new Kml();
        Document d = kml.createAndSetDocument();

        int i = 0;
        Iterator<Record> it = records.iterator();
        while (it.hasNext() && i < 100) {
            i++;
            Record record = it.next();
            Gson gson = new Gson();
            Type stringStringMap = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> map = gson.fromJson(record.getGeo(), stringStringMap);
            double x = 0;
            double y = 0;
            y = Double.valueOf(map.get("Y").replace(',', '.'));
            x = Double.valueOf(map.get("X").replace(',', '.'));

            d.createAndAddPlacemark()
                    .createAndSetPoint()
                    .addToCoordinates(y, x);
        }

        StringWriter writer = new StringWriter();
        kml.marshal(writer);
        System.out.println(writer.toString());
        return writer.toString();
    }

    public String kml(String id) {
        return cash.get(id);
    }
}
