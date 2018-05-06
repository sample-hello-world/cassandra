package com.ge.cassandra.dto;

import java.util.UUID;

/**
 * @author 212395738
 *
 */
public class Book {

	private UUID id;
	private String publisher;
	private String title;
	private String subject;
	

	public Book(UUID id, String title, String subject, String publisher){
		this.id=id;
		this.title=title;
		this.subject=subject;
		this.publisher=publisher;
	}
	
	public Book(UUID id, String title, String subject){
		this.id=id;
		this.title=title;
		this.subject=subject;
	}
	 

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
	
	
}
