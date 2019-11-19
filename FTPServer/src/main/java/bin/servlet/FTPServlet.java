package bin.servlet;

import bin.pojo.PageBean;
import bin.service.FTPService;
import bin.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bin
 * @version 1.0.0
 */
@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 86400, methods = RequestMethod.POST, allowedHeaders = "*")
public class FTPServlet {
  @Autowired
  private FTPService service;


  @RequestMapping(value = "/FTP/{url}")
  @ResponseBody
  public PageBean findByName(@PathVariable String url, @RequestBody String json, @RequestParam("regex") String regex) {
    PageBean page = JsonUtils.jsonToPojo(json, PageBean.class);
    if (page == null) {
      page = new PageBean(1, 10, 1, null);
    }
    if (regex.trim().equals(""))
      service.findAll(url, page);
    else
      service.findByName(url, page, regex);
    return page;
  }

}
