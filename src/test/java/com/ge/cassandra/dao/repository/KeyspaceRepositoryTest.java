package com.ge.cassandra.dao.repository;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.ge.cassandra.dao.CassandraConnector;
import com.ge.cassandra.dao.KeyspaceRepository;

public class KeyspaceRepositoryTest {

 
	KeyspaceRepository keyspaceRepository;
	Session session;
	
	@Before
	public void connect() {
	    CassandraConnector client = new CassandraConnector();
	    client.connect("localhost", 9042);
	    this.session = client.getSession();
	    keyspaceRepository = new KeyspaceRepository(session);
	}
	
	@Test
	public void whenDeletingAKeyspace_thenDoesNotExist() {
	    String keyspaceName = "library";
	    keyspaceRepository.deleteKeyspace(keyspaceName);
	 
	    
	    ResultSet result = 
	      session.execute("SELECT * FROM system_schema.keyspaces;");
	    boolean isKeyspaceCreated = result.all().stream()
	      .anyMatch(r -> r.getString(0).equals(keyspaceName.toLowerCase()));
	         
	    assertFalse(isKeyspaceCreated);
	}
	
}
