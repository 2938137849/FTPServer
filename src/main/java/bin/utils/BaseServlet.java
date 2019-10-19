package bin.utils;

import bin.utils.ServiceInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bin
 * @version 1.0.0
 */
public class BaseServlet extends HttpServlet {
  private Map<String, ServiceInterface> map = new HashMap<>();

  public ServiceInterface addListener(String methodName, ServiceInterface function) {
    return map.put(methodName, function);
  }

  public ServiceInterface removeListener(String methodName) {
    return map.remove(methodName);
  }

  public int ListenerSize() {
    return map.size();
  }

  public boolean isListenerEmpty() {
    return map.isEmpty();
  }

  public ServiceInterface getListener(String key) {
    return map.get(key);
  }

  public void clearListener() {
    map.clear();
  }

  /**
   * 通过{@code method}字段自动调用对应方法
   * <p>方法格式:</p>
   * {@code public String ${Function_Name}(HttpServletRequest req, HttpServletResponse resp){ return ""; }}
   * <p>返回值处理:</p>
   * <ul>
   * <li>null 或 "": 不做处理</li>
   * <li>"^R:(URL)": 重定向至URL</li>
   * <li>"^L:(URL)": 转发请求至URL</li>
   * <li>其他: 发送返回值</li>
   * </ul>
   *
   * @param req request
   * @param resp response
   * @throws javax.servlet.ServletException ServletException
   * @throws java.io.IOException IOException
   */
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException {
    req.setCharacterEncoding("utf-8");
    resp.setContentType("text/html;charset=utf-8");

    String methodName = req.getParameter("method");
    if (ObjectUtil.isEmpty(methodName)) {
      resp.sendError(400, "EmptyMethodName");
      return;
    }
    try {
      Method method =
         this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
      String path = (String)method.invoke(this, req, resp);

      retPath(path, req, resp);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      resp.sendError(400, "MethodNotFound");
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      resp.sendError(400, "IllegalAccessException");
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      resp.sendError(400, "InvocationTargetException");
    }
  }

  protected void retPath(String path, HttpServletRequest req, HttpServletResponse resp)
     throws IOException, ServletException {
    if (ObjectUtil.isEmpty(path)) {
      return;
    }
    if (path.startsWith("R:")) {
      path = path.substring(2);
      resp.sendRedirect(path);
    } else if (path.startsWith("L:")) {
      path = path.substring(2);
      req.getRequestDispatcher(path).forward(req, resp);
    } else {
      resp.getWriter().write(path);
    }
  }
}
