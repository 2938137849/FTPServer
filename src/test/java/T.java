import bin.utils.FTPUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 */
public class T {
  private FTPClient ftp;

  @Before
  public void before() throws Exception {
    ftp = FTPUtil.connect();
  }

  @After
  public void after() throws Exception {
    FTPUtil.close(ftp);
  }

  @Test
  public void test1() {
    ftp.enterLocalPassiveMode();
    try {
      boolean b = ftp.changeWorkingDirectory("/视频");
      System.out.println("b = " + b);
      FTPFile[] ftpFiles = ftp.listFiles("/");
      Arrays.stream(ftpFiles).forEach(ftpFile -> System.out.println("ftpFile.getName() = " + ftpFile.getName()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test2() {
    ArrayList<String> list1 = new ArrayList<>();
    try {
      list("/", "", list1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 递归遍历目录下面指定的文件名
   *
   * @param pathName 需要遍历的目录，必须以"/"开始和结束
   * @param ext 文件的扩展名
   * @throws IOException error
   *
   * changeWorkingDirectory(new String(pathname.getBytes(),FTP.DEFAULT_CONTROL_ENCODING));
   */
  public void list(String pathName, String ext, List<String> arFiles) throws IOException {
    ftp.enterLocalPassiveMode();
    if (pathName.startsWith("/") && pathName.endsWith("/")) {
      //更换目录到当前目录
      boolean b = this.ftp.changeWorkingDirectory(new String(pathName.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
      FTPFile[] files = this.ftp.listFiles();
      FTPFile file = files[0];
      System.out.println(file.getName());
      if (file.isDirectory()) {
        System.out.println(pathName+file.getName());
        list(pathName + file.getName() + "/", ext, arFiles);
      }
    }
  }

}
