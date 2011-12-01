package util;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JavaExtensions {

  /**
   * Joiuns the list of strings together into a single string with the provided delimiter between them
   * 
   * @param s
   * @param delimiter
   * @return
   */
  public static String join(List<String> s, String delimiter) {
    if (s.isEmpty()) return "";
    Iterator<String> iter = s.iterator();
    StringBuffer buffer = new StringBuffer(iter.next());
    while (iter.hasNext())
      buffer.append(delimiter).append(iter.next());
    return buffer.toString();
  }

  /**
   * Return string representations of the fields in the provided class
   * 
   * @param class1
   * @return
   */
  public static List<String> itemsIn(Class<?> class1) {
    List<String> strings = new LinkedList<String>();
    for (Field f : class1.getDeclaredFields()) {
      strings.add(f.getName());
    }
    return strings;
  }
}