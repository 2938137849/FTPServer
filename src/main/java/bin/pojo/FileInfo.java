package bin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author bin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class FileInfo {
  private String name;
  private Date editDate;
  private boolean fileType;
  private long size;
}
