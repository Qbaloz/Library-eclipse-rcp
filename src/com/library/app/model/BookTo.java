package com.library.app.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class BookTo{

	private Long id;
	private String title;
	private Set<AuthorTo> authors = new HashSet<>();
	
	public BookTo(){
		
	}
	
	public BookTo(Long id, String title, Set<AuthorTo> authors) {
		this.id = id;
		this.title = title;
		this.authors = authors;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<AuthorTo> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<AuthorTo> authors) {
		this.authors = authors;
	}
	
	@Override
	public String toString(){
		return new StringBuffer(" id : ").append(this.id)
				.append(" title : ").append(this.title)
				.append(" authors :").append(this.authors)
				.toString();
	}
	
    public static Comparator<BookTo> COMPARE_BY_TITLE = new Comparator<BookTo>() {
        public int compare(BookTo one, BookTo other) {
            return one.title.compareTo(other.title);
        }
    };
    
    public static Comparator<BookTo> COMPARE_BY_ID = new Comparator<BookTo>() {
        public int compare(BookTo one, BookTo other) {
            return one.id.compareTo(other.id);
        }
    };

}
