package ar.edu.itba.paw.persistence.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

  private String table;
  private final List<String> columns;

  private final List<String> innerJoinTables;
  private final List<String> innerJoinConditions;

  private final List<String> leftJoinTables;
  private final List<String> leftJoinConditions;

  private final List<String> whereConditions;
  private final List<String> groupByColumns;
  private final List<String> havingConditions;

  private final List<String> orderByColumns;
  private final List<Boolean> orderByDirections;

  private boolean distinct;
  private final List<String> distinctColumns;

  private int limit = -1;
  private int offset = -1;

  public QueryBuilder() {
    this.columns = new ArrayList<>();
    this.innerJoinTables = new ArrayList<>();
    this.innerJoinConditions = new ArrayList<>();
    this.leftJoinTables = new ArrayList<>();
    this.leftJoinConditions = new ArrayList<>();
    this.whereConditions = new ArrayList<>();
    this.groupByColumns = new ArrayList<>();
    this.havingConditions = new ArrayList<>();
    this.orderByColumns = new ArrayList<>();

    // True = ASC, False = DESC
    this.orderByDirections = new ArrayList<>();
    this.distinctColumns = new ArrayList<>();
  }

  public QueryBuilder select(String... columns) {
    this.columns.addAll(Arrays.asList(columns));
    return this;
  }

  public QueryBuilder distinct() {
    this.distinct = true;
    return this;
  }

  public QueryBuilder distinctOn(String... columns) {
    this.distinct = true;
    this.distinctColumns.addAll(Arrays.asList(columns));
    return this;
  }

  public QueryBuilder limit(int limit) {
    this.limit = limit;
    return this;
  }

  public QueryBuilder offset(int offset) {
    this.offset = offset;
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

  public QueryBuilder leftJoin(String table, String condition) {
    this.leftJoinTables.add(table);
    this.leftJoinConditions.add(condition);
    return this;
  }

  public QueryBuilder where(String condition) {
    this.whereConditions.add(condition);
    return this;
  }

  public QueryBuilder groupBy(String... columns) {
    this.groupByColumns.addAll(Arrays.asList(columns));
    return this;
  }

  public QueryBuilder having(String condition) {
    this.havingConditions.add(condition);
    return this;
  }

  public QueryBuilder orderByAsc(String column) {
    this.orderByColumns.add(column);
    this.orderByDirections.add(Boolean.TRUE);
    return this;
  }

  public QueryBuilder orderByDesc(String column) {
    this.orderByColumns.add(column);
    this.orderByDirections.add(Boolean.FALSE);
    return this;
  }

  public String build() {
    StringBuilder query = new StringBuilder();

    query.append("SELECT");

    if (distinct) {
      query.append(" DISTINCT");
      if (!distinctColumns.isEmpty()) {
        query.append(" ON (").append(String.join(",", distinctColumns)).append(")");
      }
    }

    if (columns.isEmpty()) {
      query.append(" *");
    } else {
      query.append(" ").append(String.join(",", columns));
    }

    query.append(" FROM ").append(table);

    for (int i = 0; i < innerJoinTables.size(); i++) {
      query
          .append(" INNER JOIN ")
          .append(innerJoinTables.get(i))
          .append(" ON ")
          .append(innerJoinConditions.get(i));
    }

    for (int i = 0; i < leftJoinTables.size(); i++) {
      query
          .append(" LEFT JOIN ")
          .append(leftJoinTables.get(i))
          .append(" ON ")
          .append(leftJoinConditions.get(i));
    }

    if (!whereConditions.isEmpty()) {
      query.append(" WHERE ").append(String.join(" AND ", whereConditions));
    }
    if (!groupByColumns.isEmpty()) {
      query.append(" GROUP BY ").append(String.join(",", groupByColumns));
    }

    if (!havingConditions.isEmpty()) {
      query.append(" HAVING ").append(String.join(" AND ", havingConditions));
    }

    if (!orderByColumns.isEmpty()) {
      query.append(" ORDER BY ");
      for (int i = 0; i < orderByColumns.size(); i++) {
        query.append(orderByColumns.get(i));

        if (orderByDirections.get(i)) {
          query.append(" ASC");
        } else {
          query.append(" DESC");
        }

        if (i != orderByColumns.size() - 1) {
          query.append(", ");
        }
      }
    }

    if (offset >= 0) {
      query.append(" OFFSET ").append(offset);
    }

    if (limit >= 0) {
      query.append(" LIMIT ").append(limit);
    }

    return query.toString();
  }
}
