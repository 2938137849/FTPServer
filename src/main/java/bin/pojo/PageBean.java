package bin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
  private int currentPage;
  private int rows;
  private int totalPage;
  private List<FileInfo> list;
}
