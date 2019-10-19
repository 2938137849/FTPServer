package bin.utils;

//import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.jetbrains.annotations.Contract;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author bin
 * @version 1.0.0
 */
public class ObjectUtil {
  /**
   * 判断字符串是否为空
   *
   * @param string 要判断的字符串
   * @return 当且仅当字符串为 {@code null} 或长度为0时返回 {@code true}
   */
  @Contract(value = "null -> true", pure = true) public static boolean isEmpty(String string) {
    return string == null || "".equals(string);
  }

  /**
   * 判断字符串是否不为空
   *
   * @param string 要判断的字符串
   * @return 当且仅当字符串长度大于0时返回 {@code true}
   */
  @Contract(value = "null -> false", pure = true) public static boolean notEmpty(String string) {
    return string != null && string.length() > 0;
  }

  /**
   * @param bean 对象实例
   * @param map 要转换的表
   * @param <T> pojo
   * @return bean
   */
  public static <T> T getBean4Map(T bean, Map<String, String[]> map) {
    try {
      BeanUtils.populate(bean, map);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return bean;
  }

  public static String getBodyInResqust(HttpServletRequest req) {
    String line = null;
    try {
      BufferedReader reader = req.getReader();
      StringBuilder sb = new StringBuilder();
      while (ObjectUtil.notEmpty((line = reader.readLine()))) {
        sb.append(line);
      }
      line = sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return line;
  }
}
