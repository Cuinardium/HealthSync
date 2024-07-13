package ar.edu.itba.paw.webapp.auth;

public enum UserRole {
  ROLE_NULL(null),
  ROLE_PATIENT("ROLE_PATIENT"),
  ROLE_DOCTOR("ROLE_DOCTOR");

  private final String roleName;

  private UserRole(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }

  public String getRoleNameWithoutPrefix() {
    return roleName.substring(roleName.lastIndexOf("_") + 1);
  }
}
