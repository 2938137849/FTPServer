package bin.servlet;

import bin.pojo.FileInfo;
import bin.pojo.PageBean;
import bin.utils.FTPUtil;
import bin.utils.JsonUtils;
import bin.utils.ObjectUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author bin
 * @version 1.0.0
 */
@WebServlet("/FTP/*")
public class FTPServlet extends HttpServlet {
  @Override protected void service(HttpServletRequest req, HttpServletResponse resp)
     throws IOException {

    /* 允许跨域的主机地址 */
    resp.setHeader("Access-Control-Allow-Origin", "*");
    /* 允许跨域的请求方法GET, POST, HEAD 等 */
    resp.setHeader("Access-Control-Allow-Methods", "POST");

    req.setCharacterEncoding("utf-8");
    resp.setContentType("application/json;charset=utf-8");
    String url = req.getRequestURI().substring(4);
    url = URLDecoder.decode(url, "UTF-8");

    String json = ObjectUtil.getBodyInResqust(req);
    PageBean page = JsonUtils.jsonToPojo(json, PageBean.class);
    if (page == null) {
      page = new PageBean(1, 10, 1, null);
    }

    FTPClient ftp = FTPUtil.connect();

getFile:
    if (FTPUtil.isOpen(ftp)) {
      boolean isExist = FTPUtil.changeWorkingDirectory(ftp, url);
      if (!isExist) {
        break getFile;
      }
      FTPFile[] ftpFiles = ftp.listFiles();
      ArrayList<FileInfo> list = new ArrayList<>();
      int rows = page.getRows();
      int currentPage = page.getCurrentPage();
      int count = (int)Math.ceil(ftpFiles.length * 1.0 / rows);
      if (currentPage > count) {
        currentPage = count;
        page.setCurrentPage(count);
      } else if (currentPage < 1) {
        currentPage = 1;
        page.setCurrentPage(1);
      }
      currentPage = (currentPage - 1) * rows;
      Arrays.stream(ftpFiles).skip(currentPage).limit(rows).forEach(
         file -> list.add(new FileInfo(file.getName(), file.getTimestamp().getTime(), file.isFile(), file.getSize())));
      page.setTotalPage(count);
      page.setList(list);
    }

    FTPUtil.close(ftp);

    resp.getWriter().write(page.toJSON());
  }

  @Override public void destroy() {
    super.destroy();
  }

}
