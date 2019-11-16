package bin.pojo;

import bin.interfaces.CastToJSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean implements CastToJSON {
  private int currentPage;
  private int rows;
  private int totalPage;
  private List<FileInfo> list;

  public String toJSON() {
    StringBuilder builder = new StringBuilder();
    builder.append("{")
       .append("\"currentPage\":").append(currentPage)
       .append(",\"rows\":").append(rows)
       .append(",\"totalPage\":").append(totalPage);
    builder.append(",\"list\":[");
    if (list.size() > 0) {
      Iterator<FileInfo> iterator = list.iterator();
      CastToJSON info = iterator.next();
      builder.append(info.toJSON());
      while (iterator.hasNext()) {
        info = iterator.next();
        builder.append(",").append(info.toJSON());
      }
    }
    builder.append("]}");
    return builder.toString();
  }
}
