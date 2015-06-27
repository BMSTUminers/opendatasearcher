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
	public static void main(String[] args) {
		final String json_data_path = "/data";

	    File dir = new File(System.getProperty("user.dir") + json_data_path);
	    File[] files = dir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.matches(".*\\.content\\.json$");
	        }
	    });

	    if (true) {
	    try {
			Indexer indexer = new Indexer("index/data");
			indexer.createIndexWriter();

			for (File content_file : files) {
//				final String filename = "7710349494-mfclist.content.json";
//				final String passport_filename = "7710349494-mfclist.passport.json";
//				final String id = "7710349494-mfclist";

				String filename = content_file.getName();
				String passport_filename = "";
				String geo_filename = "";

				String id = "";
				int pos;	
				if ((pos = filename.lastIndexOf(".content.json")) != -1 ) {
					id = System.getProperty("user.dir") + json_data_path + "/" + filename.substring(0, pos);
					passport_filename = id + ".passport.json";
					geo_filename = id + ".geo.json";
				}

				String[] geo_arr = new String[0];
				Path path;

				File geo_file = new File(geo_filename);
				if (geo_file.exists()) {
					path = geo_file.toPath();
					try (BufferedReader reader = Files.newBufferedReader(path,
							StandardCharsets.UTF_8)) {
						final Gson gson = new Gson();
						Object list[] = gson.fromJson(reader, Object[].class);
						geo_arr = new String[list.length];
						for (int i = 0; i < list.length; i++) {
							String json = gson.toJson(list[i]);
							geo_arr[i] = json;
						}
					}
				}

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
						for (int i = 0; i < list.length; i++) {
							String json = gson.toJson(list[i]);
							Record record = new Record(json, id);
							if (add_geo)
								record.setGeo(geo_arr[i]);
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
		}}
	    

	    // SEARCH TEST
		try {
			Indexer indexer;
			indexer = new Indexer("index/data");
			List<Record> list;
			// list = indexer.search("филиал", true);
			//list = indexer.search("адыгейск телефон мфц", true);
			list = indexer.search("телефон", true);
			// list = indexer.search("kizlar", true);
			for (Record record : list) {
				System.out.println(record.getJson());
			}

			System.out.println("---------------------------------------");

			Map<String, String> query2 = new HashMap<String, String>();
			query2.put("title","МФЦ");
//			query2.put("description","Список отделений");
			List<Passport> list2 = indexer.searchPassport(query2, true);
			for (Passport record : list2) {
				System.out.println(record.getTitle());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
