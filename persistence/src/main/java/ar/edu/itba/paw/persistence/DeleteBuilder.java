package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.List;

public class DeleteBuilder {

  private String table;
  private final List<String> whereConditions;

  public DeleteBuilder() {
    this.whereConditions = new ArrayList<>();
  }

  public DeleteBuilder delete(final String table) {
    this.table = table;
    return this;
  }

  public DeleteBuilder where(final String condition) {
    this.whereConditions.add(condition);
    return this;
  }

  public String build() {
    final StringBuilder delete = new StringBuilder();

    delete.append("DELETE FROM ");
    delete.append(table);

    if (!whereConditions.isEmpty()) {
      delete.append(" WHERE ");
      delete.append(String.join(" AND ", whereConditions));
    }
    return delete.toString();
  }
}
