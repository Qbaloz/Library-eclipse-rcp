package com.library.app.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;

import com.library.app.model.BookTo;
import com.library.app.rest.BookRestService;

public enum BookProvider {

	INSTANCE;
	
	private BookRestService bookRestService = new BookRestService();
	private WritableList writableList = new WritableList();

	private BookProvider() {
//			refreshWritableList("");
	}
	
	public BookTo addBook(String jsonString){
		BookTo book = bookRestService.sendPOST(jsonString);
		refreshWritableList("");
		return book;
	}

	public void deleteBook(Long id){
		bookRestService.sendDELETE(id);
		refreshWritableList("");
	}
	
	public IObservableList getBooks(String titlePrefix){
		bookRestService.sendGET(titlePrefix);
		refreshWritableList(titlePrefix);
		return writableList;
	}
	
	private void refreshWritableList(String titlePrefix){
		writableList.clear();
		writableList.addAll(bookRestService.sendGET(titlePrefix));
	}
	

	public void sortByName(){
		List<BookTo> sortedList = new ArrayList<BookTo>();
		BookTo book = new BookTo();
		for(int i = 0; i < writableList.size(); i++){
			book = (BookTo) writableList.get(i);
			sortedList.add(book);
		}
		Collections.sort(sortedList);
		writableList.clear();
		writableList.addAll(sortedList);
	}
	
}
