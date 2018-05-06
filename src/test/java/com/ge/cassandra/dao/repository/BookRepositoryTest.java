package com.ge.cassandra.dao.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.utils.UUIDs;
import com.ge.cassandra.dao.CassandraConnector;
import com.ge.cassandra.dao.KeyspaceRepository;
import com.ge.cassandra.dto.Book;
import com.ge.cassandra.repository.BookRepository;

public class BookRepositoryTest {

	private BookRepository bookRepository;
	private KeyspaceRepository keyspaceRepository;
	private Session session;
	private final String KEYSPACE_NAME = "library";
	@Before
	public void connect() {
	    CassandraConnector client = new CassandraConnector();
	    client.connect("localhost", 9042);
	    this.session = client.getSession();
	    keyspaceRepository = new KeyspaceRepository(session);
	    keyspaceRepository.useKeyspace("library");
	    bookRepository = new BookRepository(session);
	}
	
	@Test
	public void whenCreatingATable_thenCreatedCorrectly() {
	    bookRepository.createTable();	    
	    ResultSet result = session.execute(
	      "SELECT * FROM " + KEYSPACE_NAME + ".books;");
	 
	    List<String> columnNames = 
	      result.getColumnDefinitions().asList().stream()
	      .map(cl -> cl.getName())
	      .collect(Collectors.toList());
	         
	    assertEquals(columnNames.size(), 4);
	    assertTrue(columnNames.contains("id"));
	    assertTrue(columnNames.contains("title"));
	    assertTrue(columnNames.contains("subject"));
	}
	
	
	@Test
	public void whenAlteringTable_thenAddedColumnExists() {
	    bookRepository.createTable();
	 
	    ResultSet result = session.execute(
	      "SELECT * FROM " + KEYSPACE_NAME + "." + "books" + ";");
	 
	    boolean columnExists = result.getColumnDefinitions().asList().stream()
	      .anyMatch(cl -> cl.getName().equals("publisher"));
	         
	    if(!columnExists){
	    	bookRepository.alterTablebooks("publisher", "text");
	    	columnExists = result.getColumnDefinitions().asList().stream()
	    		      .anyMatch(cl -> cl.getName().equals("publisher"));
	    }
	    
	    assertTrue(columnExists);
	}
	
	
	@Test
	public void whenAddingANewBook_thenBookExists() {
	    bookRepository.createTableBooksByTitle();
	 
	    String title = "Effective Java";
	    Book book = new Book(UUIDs.timeBased(), title, "Programming");
	    bookRepository.insertbookByTitle(book);
	    
	    Book savedBook = bookRepository.selectByTitle(title);
	    assertEquals(book.getTitle(), savedBook.getTitle());
	}
	
	@Test
	public void whenSelectingAll_thenReturnAllRecords() {	         
	    Book book = new Book(
	      UUIDs.timeBased(), "Effective Java", "Programming", "Publisher1");
	    bookRepository.insertbook(book);
	       
	    book = new Book(
	      UUIDs.timeBased(), "Clean Code", "Programming","Publisher2");
	    bookRepository.insertbook(book);
	         
	    List<Book> books = bookRepository.selectAll(); 
	    
	    //assertEquals(2, books.size());
	    assertTrue(books.stream().anyMatch(b -> b.getTitle()
	      .equals("Effective Java")));
	    assertTrue(books.stream().anyMatch(b -> b.getTitle()
	      .equals("Clean Code")));
	}
	
	
	@Test
	public void whenAddingANewBookBatch_ThenBookAddedInAllTables() {
	    bookRepository.createTable();
	         
	    bookRepository.createTableBooksByTitle();
	     
	    String title = "Effective Java";
	    Book book = new Book(UUIDs.timeBased(), title, "Programming");
	    bookRepository.insertBookBatch(book);
	     
	    List<Book> books = bookRepository.selectAll();
	     
	    assertTrue(books.size()>=1);
	    assertTrue(
	      books.stream().anyMatch(
	        b -> b.getTitle().equals("Effective Java")));
	         
	    List<Book> booksByTitle = bookRepository.selectAllBookByTitle();
	     
	    //assertEquals(1, booksByTitle.size());
	    assertTrue(
	      booksByTitle.stream().anyMatch(
	        b -> b.getTitle().equals("Effective Java")));
	}
	
	
	
	@Test(expected = InvalidQueryException.class)
	public void whenDeletingATable_thenUnconfiguredTable() {
	    bookRepository.createTable();
	    bookRepository.deleteTable();
	        
	    session.execute("SELECT * FROM " + KEYSPACE_NAME + ".books;");
	}

	
}
