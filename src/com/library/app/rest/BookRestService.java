package com.library.app.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.library.app.mapper.Mapper;
import com.library.app.model.BookTo;

@SuppressWarnings("restriction")
public class BookRestService {

	private Mapper mapper = new Mapper();
	
	public BookTo sendPOST(String jsonString) throws ClientProtocolException, IOException {
		String url = "http://localhost:9721/workshop/services/books/book/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "Application/JSON");
		post.setEntity(new StringEntity(jsonString));
		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}
		BookTo book = mapper.json2BookTo(result.toString());
		System.out.println(result.toString());
		return book;
	}

	public List<BookTo> sendGET(String titlePrefix) throws IOException {
		String url = "http://localhost:9721/workshop/services/books/books-by-title?titlePrefix=" + titlePrefix;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		request.setHeader("Content-Type", "Application/JSON");
		HttpResponse response = client.execute(request);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}

		List<BookTo> bookToList = mapper.json2BookToList(result.toString());
		System.out.println(result.toString());
		return bookToList;
	}
	
	public void sendDELETE(Long id) throws IOException {
		String url = "http://localhost:9721/workshop/services/books/book/" + id;

		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete request = new HttpDelete(url);

		request.setHeader("Content-Type", "Application/JSON");
		HttpResponse response = client.execute(request);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}
	}

}
