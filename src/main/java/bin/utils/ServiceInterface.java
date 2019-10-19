package bin.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bin
 * @version 1.0.0
 */
@FunctionalInterface
public interface ServiceInterface {
  /**
   * 回调函数
   *
   * @param req req
   * @param resp resp
   * @return <ul>
   * <li>null 或 "": 不做处理</li>
   * <li>"^R:(URL)": 重定向至URL</li>
   * <li>"^L:(URL)": 转发请求至URL</li>
   * <li>其他: 发送返回值</li>
   * </ul>
   */
  String run(HttpServletRequest req, HttpServletResponse resp);
}