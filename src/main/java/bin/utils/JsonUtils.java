package bin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

  // 定义jackson对象
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * 将json结果集转化为对象
   *
   * @param jsonData json数据
   * @param beanType 对象中的object类型
   * @return json
   */
  public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
    try {
      return MAPPER.readValue(jsonData, beanType);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
