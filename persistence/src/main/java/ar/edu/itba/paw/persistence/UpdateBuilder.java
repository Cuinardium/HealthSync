package ar.edu.itba.paw.persistence;

// Just like my class QueryBuilder, this class is used to build a query. However, this class is used
// to build an update query.

import java.util.ArrayList;
import java.util.List;

public class UpdateBuilder {

  private String table;
  private List<String> columns;
  private List<String> values;
  private List<String> whereConditions;

  public UpdateBuilder() {
    this.columns = new ArrayList<>();
    this.values = new ArrayList<>();
    this.whereConditions = new ArrayList<>();
  }

  public UpdateBuilder update(String table) {
    this.table = table;
    return this;
  }

  public UpdateBuilder set(String column, String value) {
    this.columns.add(column);
    this.values.add(value);
    return this;
  }

  public UpdateBuilder where(String condition) {
    this.whereConditions.add(condition);
    return this;
  }

  public String build() {
    StringBuilder update = new StringBuilder();

    update.append("UPDATE ");
    update.append(this.table);

    update.append(" SET ");

    for (int i = 0; i < columns.size(); i++) {
      update.append(columns.get(i)).append(" = ").append(values.get(i));
      if (i < columns.size() - 1) {
        update.append(", ");
      }
    }
    
    if (!whereConditions.isEmpty()) {
      update.append(" WHERE ").append(String.join(" AND ", whereConditions));
    }

    update.append(";");
    return update.toString();
  }
}
