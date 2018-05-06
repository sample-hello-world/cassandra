package com.ge.cassandra.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;


//https://github.com/eugenp/tutorials/tree/master/persistence-modules/java-cassandra
public class CassandraConnector {

	private Cluster cluster;

	private Session session;

	public void connect(String node, Integer port) {
		Builder b = Cluster.builder().addContactPoint(node);
		if (port != null) {
			b.withPort(port);
		}
		cluster = b.build();

		session = cluster.connect();
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

	/**
	 * Create keyspace in cassandra
	 * @param keyspaceName
	 * @param replicationStrategy
	 * @param replicationFactor
	 */
	public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
		//for local testing - replication class = SimpleStrategy and replication_factor=1 
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append(keyspaceName)
				.append(" WITH replication = {").append("'class':'").append(replicationStrategy)
				.append("','replication_factor':").append(replicationFactor).append("};");

		String query = sb.toString();
		session.execute(query);
	}

}
