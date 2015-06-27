package ru.gov.data.opendatasearch;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ru.gov.data.opendatasearch.datasource.Passport;
import ru.gov.data.opendatasearch.datasource.Record;

public class Indexer {
	public static final Version luceneVersion = Version.LUCENE_4_9;

	protected Analyzer analyzer;
	protected Directory index_storage;
	protected IndexSearcher searcher = null;
	protected IndexWriter writer = null;
	protected final int hitsPerPage = 1000;

	/**
	 * Path to Lucene's index
	 */
	protected String indexDir = "";

	public Indexer(String indexDir) throws IOException {
		if (indexDir != null) {
			this.indexDir = indexDir;
		}

		// persistant storage
		index_storage = FSDirectory.open(new File(indexDir));

		// analyzer = new StandardAnalyzer(luceneVersion);
		analyzer = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName,
					Reader reader) {
				StandardTokenizer source = new StandardTokenizer(luceneVersion,
						reader);
				TokenStream filter = new LowerCaseFilter(luceneVersion, source);
				return new TokenStreamComponents(source, filter);
			}
		};
	}

	/**
	 * Create writable index with default storage
	 * 
	 * @throws IOException
	 */
	public void createIndexWriter() throws IOException {
		IndexWriterConfig luceneConfig = new IndexWriterConfig(luceneVersion,
				analyzer);
		writer = new IndexWriter(index_storage, luceneConfig);
	}

	/**
	 * Close writable index
	 * 
	 * @throws IOException
	 */
	public void closeIndexWriter() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}

	/**
	 * Index concept with URI and label
	 * 
	 * @param concept
	 * @throws IOException
	 */
	public void addPassport(Passport passport) throws IOException {
		Document doc = new Document();
		doc.add(new StoredField("url", passport.getUrl()));
		doc.add(new Field("creator", passport.getCreator(),
				TextField.TYPE_STORED));
		doc.add(new Field("subject", passport.getSubject(),
				TextField.TYPE_STORED));
		doc.add(new Field("title", passport.getTitle(), TextField.TYPE_STORED));
		doc.add(new Field("description", passport.getDescription(),
				TextField.TYPE_STORED));

		writer.addDocument(doc);
	}

	public void addRecord(Record record) throws IOException {
		Document doc = new Document();
		doc.add(new StoredField("id", record.getId()));
		doc.add(new Field("json", record.getJson(), TextField.TYPE_STORED));
		doc.add(new StoredField("geo", record.getGeo()));
		doc.add(new StoredField("phone", record.getPhone()));
		doc.add(new StoredField("email", record.getEmail()));
		writer.addDocument(doc);
	}

	public List<Record> search(String query, boolean close_index_on_return) {
		// see http://www.lucenetutorial.com/lucene-in-5-minutes.html
		Query q;
		List<Record> result = new ArrayList<Record>();
		// QueryBuilder qbuilder = new QueryBuilder(analyzer);
		// q = qbuilder.createMinShouldMatchQuery("contents", query, 0.9f);
		try {
			QueryParser qp = new QueryParser(luceneVersion, "json", analyzer);
			qp.setDefaultOperator(QueryParser.Operator.AND);
			q = qp.parse(query
					.replaceAll("[/\'\"()]", " "));

			// query = query.toLowerCase();
			IndexReader reader = null;
			try {
				if (searcher == null) {
					reader = DirectoryReader.open(index_storage);
					searcher = new IndexSearcher(reader);
				}

				TopScoreDocCollector collector = TopScoreDocCollector.create(
						hitsPerPage, true);

				searcher.search(q, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				// System.out.println("Found " + hits.length + " hits.");

				for (int i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					Record record = new Record(d.get("json"), d.get("id"));
					record.setGeo(d.get("geo"));
					record.setPhone(d.get("phone"));
					record.setEmail(d.get("email"));

					result.add(record);
				}

				if (close_index_on_return) {
					if (reader != null)
						reader.close();
					searcher = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (org.apache.lucene.queryparser.classic.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}

	public List<Passport> searchPassport(Map<String, String> query,
			boolean close_index_on_return) {
		// see http://www.lucenetutorial.com/lucene-in-5-minutes.html
		Query q;
		List<Passport> result = new ArrayList<Passport>();

		try {
			// q = new QueryParser(luceneVersion, "json", analyzer)
			// .parse(query.replaceAll("[/\'\"()]", " "));
			// q = new QueryParser(luceneVersion, "description",
			// analyzer).parse("Список отделений");

			String[] fields = new String[query.size()];
			String[] values = new String[query.size()];

			int i = 0;
			for (Entry<String, String> entry : query.entrySet()) {
				fields[i] = entry.getKey();
				values[i] = entry.getValue();
				i++;
			}

			q = MultiFieldQueryParser.parse(luceneVersion, values, fields,
					analyzer);

			// query = query.toLowerCase();

			IndexReader reader = null;
			try {
				if (searcher == null) {
					reader = DirectoryReader.open(index_storage);
					searcher = new IndexSearcher(reader);
				}

				TopScoreDocCollector collector = TopScoreDocCollector.create(
						hitsPerPage, true);

				searcher.search(q, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				// System.out.println("Found " + hits.length + " hits.");

				for (i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					Passport record = new Passport(d.get("url"),
							d.get("creator"), d.get("subject"), d.get("title"),
							d.get("description"));
					result.add(record);
				}

				if (close_index_on_return) {
					if (reader != null)
						reader.close();
					searcher = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (org.apache.lucene.queryparser.classic.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
}
