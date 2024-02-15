package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import net.barrage.tegridy.http.form.FormBody;
import net.barrage.tegridy.http.form.FormData;
import org.junit.jupiter.api.Test;

class FormBodyTests {

  public static class TestPayload implements FormBody {
    @FormData String foo;

    @FormData int bar;

    @FormData("my_file")
    String myFile;

    @FormData("my_file")
    String myOtherFile;

    @FormData List<String> list;

    @FormData String[] array;
  }

  @Test
  void formDataWorks() {
    var payload = new TestPayload();

    payload.foo = "foo";
    payload.bar = 2;
    payload.myFile = "awesomejpeg";
    payload.myOtherFile = "otherjpeg";

    var map = payload.intoFormParts();

    var fooMap = map.get("foo");
    var barMap = map.get("bar");
    var fileMap = map.get("my_file");

    assertEquals(fooMap.get(0), "foo");
    assertEquals(barMap.get(0), 2);
    assertEquals(fileMap.get(0), "awesomejpeg");
    assertEquals(fileMap.get(1), "otherjpeg");
  }

  @Test
  void formListWorks() {
    var payload = new TestPayload();
    payload.list = List.of("Hello", "World");

    var map = payload.intoFormParts();

    var listMap = map.get("list");

    assertEquals("Hello", listMap.get(0));
    assertEquals("World", listMap.get(1));
  }

  @Test
  void formArrayWorks() {
    var payload = new TestPayload();
    payload.array = new String[] {"Hello", "World"};

    var map = payload.intoFormParts();

    var listMap = map.get("array");

    assertEquals("Hello", listMap.get(0));
    assertEquals("World", listMap.get(1));
  }

  @Test
  void ignoresNullValues() {
    var payload = new TestPayload();
    payload.foo = "foo";

    var map = payload.intoFormParts();

    var fooMap = map.get("foo");
    var barMap = map.get("bar");
    var fileMap = map.get("my_file");

    assertEquals("foo", fooMap.get(0));
    // Primitives cannot be null
    assertEquals(0, barMap.get(0));
    assertNull(fileMap);
  }
}
