package ru.gov.data.opendatasearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.gov.data.opendatasearch.datasource.JSONPassport;
import ru.gov.data.opendatasearch.datasource.Passport;
import ru.gov.data.opendatasearch.datasource.Record;

import com.google.gson.Gson;

public class App {
    private static final String jsonDataPath = System.getProperty("user.dir")
            + "/data/";
    private static final String indexDir = "index/data";

    public void parseAndIndex(String json_data_path, String indexDir) {
        // file names convention:
        //      DATASET_ID.content.json
        //      DATASET_ID.passport.json
        //      DATASET_ID.geo.json
        //      DATASET_ID.phone.json
        //      DATASET_ID.e-mail.json

        File dir = new File(json_data_path);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*\\.content\\.json$");
            }
        });

        try {
            Indexer indexer = new Indexer(indexDir);
            indexer.createIndexWriter();

            for (File content_file : files) {
                String filename = content_file.getName();
                String passport_filename = "";
                String geo_filename = "";
                String phone_filename = "";
                String email_filename = "";

                String id = "";
                int pos;
                if ((pos = filename.lastIndexOf(".content.json")) != -1) {
                    id = filename.substring(0, pos);
                    passport_filename = json_data_path + id + ".passport.json";
                    geo_filename = json_data_path + id + ".geo.json";
                    phone_filename = json_data_path + id + ".phone.json";
                    email_filename = json_data_path + id + ".e-mail.json";
                }

                Path path;
                String[] geo_arr = readJSONFile(geo_filename);
                String[] phone_arr = readJSONFile(phone_filename);
                String[] email_arr = readJSONFile(email_filename);

                if (content_file.exists()) {
                    path = content_file.toPath();

                    // Add content
                    try (BufferedReader reader = Files.newBufferedReader(path,
                            StandardCharsets.UTF_8)) {
                        final Gson gson = new Gson();
                        Object list[] = gson.fromJson(reader, Object[].class);
                        if (list == null)
                            continue;

                        boolean add_geo = (list.length == geo_arr.length);
                        boolean add_phone = (list.length == phone_arr.length);
                        boolean add_email = (list.length == email_arr.length);

                        for (int i = 0; i < list.length; i++) {
                            String json = gson.toJson(list[i]);
                            Record record = new Record(json, id);
                            if (add_geo)
                                record.setGeo(geo_arr[i]);
                            if (add_phone)
                                record.setPhone(phone_arr[i]);
                            if (add_email)
                                record.setEmail(email_arr[i]);
                            indexer.addRecord(record);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                // passport indexing
                File file = new File(passport_filename);
                if (file.exists()) {
                    path = file.toPath();
                    try (BufferedReader reader = Files.newBufferedReader(path,
                            StandardCharsets.UTF_8)) {
                        final Gson gson = new Gson();
                        JSONPassport jpassport = gson.fromJson(reader,
                                JSONPassport.class);
                        Passport record = new Passport(id,
                                jpassport.getCreator(), jpassport.getSubject(),
                                jpassport.getTitle(),
                                jpassport.getDescription());
                        indexer.addPassport(record);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            indexer.closeIndexWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void searchTest(String indexDir, String query) {
        // SEARCH TEST
        try {
            Indexer indexer;
            indexer = new Indexer(indexDir);
            List<Record> list;
            if (query != null && !query.isEmpty()) {
                list = indexer.search(query, true);
            } else {
                list = indexer.search("телефон", true);
            }
            for (Record record : list) {
                System.out.println(record.getId() + "\t" + record.getJson());
            }

            System.out.println("---------------------------------------");

            Map<String, String> query2 = new HashMap<String, String>();
            if (query != null && !query.isEmpty()) {
                query2.put("description", query);
            } else {
                query2.put("title", "МФЦ");
                // query2.put("description","Список отделений");
            }
            List<Passport> list2 = indexer.searchPassport(query2, true);
            for (Passport record : list2) {
                System.out.println(record.getTitle());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String[] readJSONFile(String filename) {
        String[] result = new String[0];

        File file = new File(filename);
        if (file.exists()) {
            Path path = file.toPath();
            try (BufferedReader reader = Files.newBufferedReader(path,
                    StandardCharsets.UTF_8)) {
                final Gson gson = new Gson();
                Object list[] = gson.fromJson(reader, Object[].class);
                result = new String[list.length];
                for (int i = 0; i < list.length; i++) {
                    String json = gson.toJson(list[i]);
                    result[i] = json;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        App app = new App();
        app.parseAndIndex(jsonDataPath, indexDir);
        app.searchTest(indexDir, null);
        // app.searchTest(indexDir, "мфц мытищи");
    }
}
