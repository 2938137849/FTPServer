package bin.service;

import bin.pojo.FileInfo;
import bin.pojo.PageBean;
import bin.utils.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 */
@Service
public class FTPService {
  @FunctionalInterface
  interface Consumer {
    List<FileInfo> run(FTPFile[] arr, int skip, int limit);
  }


  public void findAll(String url, PageBean page) {
    find(url, page, (arr, skip, limit) -> {
      List<FileInfo> list = new ArrayList<>();
      Arrays.stream(arr)
         .skip(skip)
         .limit(limit)
         .forEach(file -> list.add(new FileInfo(file)));
      return list;
    });
  }

  public void findByName(String url, PageBean page, String regex) {
    find(url, page, (arr, skip, limit) -> {
      List<FileInfo> list = new ArrayList<>();
      Arrays.stream(arr)
         .filter(file -> file.getName().matches(regex))
         .skip(skip)
         .limit(limit)
         .forEach(file -> list.add(new FileInfo(file)));
      return list;
    });
  }


  public void find(String url, PageBean page, Consumer fun) {
    if ("".equals(url)) url = "/";
    FTPClient ftp = FTPUtil.connect();
TRY:
    try {
      if (FTPUtil.isOpen(ftp)) {
        boolean isExist = FTPUtil.changeWorkingDirectory(ftp, url);
        if (!isExist) break TRY;
        FTPFile[] ftpFiles = ftp.listFiles();
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
        List<FileInfo> list = fun.run(ftpFiles, currentPage, rows);
        page.setTotalPage(count);
        page.setList(list);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    FTPUtil.close(ftp);
  }
}
