package com.library.app.mapper;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.app.model.BookTo;

public class Mapper {

	private Gson gson = new Gson();
	
	public List<BookTo> json2BookToList(String json) {
		List<BookTo> bookToList = new Gson().fromJson(json, new TypeToken<ArrayList<BookTo>>() {
		}.getType());
		return bookToList;
	}
	
	public BookTo json2BookTo(String json){
		BookTo book = gson.fromJson(json, BookTo.class);
		return book;
	}

}
