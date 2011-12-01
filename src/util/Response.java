package util;

public enum Response {
  C("cooperate"),
  D("defect");
  
  private String displayName;
  
  Response(String name) {
    this.displayName = name;
  }
  
  public String toString() {
    return displayName;
  }
}
