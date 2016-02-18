package com.library.app.provider;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;

import com.library.app.model.BookTo;
import com.library.app.rest.BookRestService;

public enum BookProvider {

	INSTANCE;
	
	private BookRestService bookRestService = new BookRestService();
	private WritableList writableList = new WritableList();

	private BookProvider() {
		try {
			refreshWritableList("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BookTo addBook(String jsonString) throws ClientProtocolException, IOException{
		BookTo book = bookRestService.sendPOST(jsonString);
		refreshWritableList("");
		return book;
	}

	public void deleteBook(Long id) throws IOException{
		bookRestService.sendDELETE(id);
		refreshWritableList("");
	}
	
	public IObservableList getBooks(String titlePrefix) throws IOException {
		bookRestService.sendGET(titlePrefix);
		refreshWritableList(titlePrefix);
		return writableList;
	}
	
	private void refreshWritableList(String titlePrefix) throws IOException{
		writableList.clear();
		writableList.addAll(bookRestService.sendGET(titlePrefix));
	}
}
