package bin.pojo;

import bin.interfaces.CastToJSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;

import java.util.Date;

/**
 * @author bin
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo implements CastToJSON {
  private String name;
  private Date editDate;
  private boolean fileType;
  private long size;

  public FileInfo(FTPFile file) {
    this(file.getName(), file.getTimestamp().getTime(), file.isFile(), file.getSize());
  }

  public String toJSON() {
    return "{" +
           "\"name\":\"" + name +
           "\",\"editDate\":" + editDate.getTime() +
           ",\"fileType\":" + fileType +
           ",\"size\":" + size +
           "}";
  }
}
