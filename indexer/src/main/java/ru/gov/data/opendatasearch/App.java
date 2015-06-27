package ru.gov.data.opendatasearch;

import com.google.gson.Gson;
import ru.gov.data.opendatasearch.datasource.JSONPassport;
import ru.gov.data.opendatasearch.datasource.Passport;
import ru.gov.data.opendatasearch.datasource.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	public static void main(String[] args) {
		final String filename = "content.json";
		final String passport_filename = "7710349494-mfclist.json";

		final String id = "7710349494-mfclist";
		File file = new File(filename);
		Path path = file.toPath();

		try {
			Indexer indexer = new Indexer("index/data");
			indexer.createIndexWriter();

			// Add content
			try (BufferedReader reader = Files.newBufferedReader(path,
					StandardCharsets.UTF_8)) {
				final Gson gson = new Gson();
				Object list[] = gson.fromJson(reader, Object[].class);
				for (Object obj : list) {
					String json = gson.toJson(obj);
					Record record = new Record(json, id);
					indexer.addRecord(record);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// passport indexing
			file = new File(passport_filename);
			path = file.toPath();
			try (BufferedReader reader = Files.newBufferedReader(path,
					StandardCharsets.UTF_8)) {
				final Gson gson = new Gson();
				JSONPassport jpassport = gson.fromJson(reader,
						JSONPassport.class);
				Passport record = new Passport(id, jpassport.getCreator(),
						jpassport.getSubject(), jpassport.getTitle(),
						jpassport.getDescription());
				indexer.addPassport(record);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			indexer.closeIndexWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Indexer indexer;
			indexer = new Indexer("index/data");
			List<Record> list;
			// list = indexer.search("филиал", true);
			list = indexer.search("адыгейск телефон мфц", true);
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
