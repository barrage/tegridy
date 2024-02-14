package net.barrage.tegridy.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.Serializable;

/**
 * Utilities for [de]serializing objects.
 */
public class Serde {

  /**
   * Deserialize the input to the given class.
   *
   * @param input The string to deserialize.
   * @param out   The class to which to deserialize to.
   * @param <T>   The type.
   * @return The deserialized class.
   * @throws JsonProcessingException Propagated from Jackson.
   */
  public static <T> T deserialize(String input, Class<T> out) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper.readValue(input, out);
  }

  /**
   * Serialize the input to a JSON string.
   *
   * @param input The object to serialize.
   * @param <T>   The type.
   * @return A JSON representation of the object.
   * @throws JsonProcessingException Propagated from Jackson.
   */
  public static <T extends Serializable> String serialize(T input) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper.writeValueAsString(input);
  }
}
