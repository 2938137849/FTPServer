package bin.servlet;

import bin.pojo.FileInfo;
import bin.pojo.PageBean;
import bin.utils.FTPUtil;
import bin.utils.JsonUtils;
import bin.utils.ObjectUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    req.setCharacterEncoding("utf-8");
    resp.setContentType("text/html;charset=utf-8");
    String url = req.getRequestURI().substring(4);
    url = URLDecoder.decode(url, "UTF-8");

    String fileName = req.getParameter("file");
    if (ObjectUtil.notEmpty(fileName)) {
      resp.setHeader(
         "Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
      resp.setContentType("application/octet-stream");
      sendFile(resp.getOutputStream(), url, fileName);
      return;
    }

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
      Arrays.stream(ftpFiles).skip(currentPage).limit(rows).forEach(file -> {
        list.add(new FileInfo(file.getName(), file.getTimestamp().getTime(), file.isFile(), file.getSize()));
      });
      page.setTotalPage(count);
      page.setList(list);
    }

    FTPUtil.close(ftp);
    resp.getWriter().write(page.toJSON());
  }

  private void sendFile(ServletOutputStream os, String url, String fileName) throws IOException {
    FTPClient ftp = FTPUtil.connect();
    if (FTPUtil.isOpen(ftp)) {
      FTPUtil.changeWorkingDirectory(ftp, url);
      FTPFile[] ftpFiles = ftp.listFiles();
      for (FTPFile ftpFile : ftpFiles) {
        if (ftpFile.getName().equals(fileName)) {
          try {
            FTPUtil.retrieveFile(ftp, ftpFile, os);
          } catch (CopyStreamException e) {
            System.err.println("============ Force disconnect ============");
          }
          System.out.println("a");
          os.flush();
          return;
        }
      }
    }
  }

  @Override public void destroy() {
    super.destroy();
  }

}
