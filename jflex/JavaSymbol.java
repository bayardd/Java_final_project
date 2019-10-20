package edu.odu.cs.cs350.jflex;

/* The following code was created by JFlex 1.7.0 */

public class JavaSymbol extends java_cup.runtime.Symbol {
  private int line;
  private int column;

  public JavaSymbol(int type, int line, int column) {
    this(type, line, column, -1, -1, null);
  }

  public JavaSymbol(int type, int line, int column, Object value) {
    this(type, line, column, -1, -1, value);
  }

  public JavaSymbol(int type, int line, int column, int left, int right, Object value) {
    super(type, left, right, value);
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public String toString() {   
    return "line "+line+", column "+column+", sym: "+sym+(value == null ? "" : (", value: '"+value+"'"));
  }

  public static String getTokenName(int token) {
    try {
      java.lang.reflect.Field [] classFields = sym.class.getFields();
      for (int i = 0; i < classFields.length; i++) {
        if (classFields[i].getInt(null) == token) {
          return classFields[i].getName();
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return "UNKNOWN TOKEN";
  }
}
