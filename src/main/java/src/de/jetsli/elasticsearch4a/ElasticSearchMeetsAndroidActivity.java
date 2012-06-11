package de.jetsli.elasticsearch4a;

import java.util.Random;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilders;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ElasticSearchMeetsAndroidActivity extends Activity {

	private static ElasticNode node;
	private Client client;
	private String indexName = "testindex";
	private Handler mHandler;
	private int run = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mHandler = new Handler();

		final TextView outText = (TextView) findViewById(R.id.out_text);

		Button startButton = (Button) findViewById(R.id.button_start);
		startButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new Thread() {
					public void run() {
						if (node == null) {
							node = new ElasticNode().start("es");
							node.waitForYellow();
							client = node.client();
						}

						mHandler.post(new Runnable() {
							public void run() {
								outText.setText(node.getInfo());
							}
						});
					}
				}.start();
			}
		});

		Button feedButton = (Button) findViewById(R.id.button_feed);
		feedButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						int counter = 0;
						BulkRequest br = new BulkRequest();
						for (; counter < 1000; counter++) {
							String content = createRandomWord(100);
							IndexRequest indexReq = Requests
									.indexRequest(indexName).type("testobj")
									.id(run + "-" + counter).source(createDoc(content));
							br.add(indexReq);
						}

						run++;
						client.bulk(br);
						client.admin().indices()
								.refresh(new RefreshRequest(indexName))
								.actionGet();

						final int docs = counter;
						mHandler.post(new Runnable() {

							public void run() {
								outText.setText("feed " + docs
										+ " documents. index has "
										+ countAll(indexName) + " documents.");
							}
						});
					}
				}.start();
			}
		});
	}

	Random rand = new Random(0);

	String createRandomWord(int chars) {
		String word = "";
		for (int i = 0; i < chars; i++) {
			word = word + (char) (rand.nextInt(58) + 65);
		}
		return word;
	}

	XContentBuilder createDoc(String content) {
		try {
			XContentBuilder b = JsonXContent.contentBuilder().startObject();
			b.field("content", content);
			b.endObject();
			return b;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	long countAll(String... indices) {
		CountResponse response = client.prepareCount(indices)
				.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
		return response.getCount();
	}
}