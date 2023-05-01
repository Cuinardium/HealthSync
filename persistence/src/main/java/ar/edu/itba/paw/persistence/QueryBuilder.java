package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {


  private String table;
  private final List<String> columns;
  
  private final List<String> innerJoinTables;
  private final List<String> innerJoinConditions;

  private final List<String> whereConditions;


  public QueryBuilder() {
    this.columns = new ArrayList<>();
    this.innerJoinTables = new ArrayList<>();
    this.innerJoinConditions = new ArrayList<>();
    this.whereConditions = new ArrayList<>();
  }

  public QueryBuilder select(String... columns) {
    this.columns.addAll(Arrays.asList(columns));
    return this;
  }

  public QueryBuilder from(String table) {
    this.table = table;
    return this;
  }

  public QueryBuilder innerJoin(String table, String condition) {
    this.innerJoinTables.add(table);
    this.innerJoinConditions.add(condition);
    return this;
  }

  public QueryBuilder where(String condition) {
    this.whereConditions.add(condition);
    return this;
  }

  public String build() {
    StringBuilder query = new StringBuilder();

    query.append("SELECT");

    if (columns.isEmpty()) {
      query.append(" *");
    } else {
      query.append(" ").append(String.join(",", columns));
    }

    query.append(" FROM ").append(table);

    for (int i = 0; i < innerJoinTables.size(); i++) {
      query.append(" INNER JOIN ").append(innerJoinTables.get(i)).append(" ON ").append(innerJoinConditions.get(i));
    }


    if (!whereConditions.isEmpty()) {
      query.append(" WHERE ").append(String.join(" AND ", whereConditions));
    }

    return query.toString();
  }
}

