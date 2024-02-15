package http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import net.barrage.tegridy.http.queryparams.QueryParam;
import net.barrage.tegridy.http.queryparams.QueryParams;
import org.junit.jupiter.api.Test;

class QueryParamsTests {

  @Test
  void testQueryParamsStringNormalCase() {
    TestClass testObj = new TestClass();
    testObj.param1 = "testValue1";
    testObj.param2 = "testValue2";
    testObj.param3 = "VALUE_ONE";

    String expected = "param1=testValue1&customName=testValue2&myparam3%5B%5D=VALUE_ONE";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testDifferentEnumVariants() {
    TestClass testObj = new TestClass();
    testObj.param3 = "value";

    assertEquals("myparam3%5B%5D=value", testObj.toQueryParams());
  }

  @Test
  void tesAnnotationsValueNull() {
    TestClass testObj = new TestClass();
    String expected = "";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testNoAnnotations() {
    TestClassNull testObj = new TestClassNull();
    String expected = "";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testSpecialCharacters() {
    TestClass testObj = new TestClass();
    testObj.specialCharParam = "value with spaces & symbols!";

    String expected = "specialCharParam=value%20with%20spaces%20%26%20symbols!";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testWithNumbers() {
    TestClass testObj = new TestClass();
    testObj.numberParam = 123;

    String expected = "numberParam=123";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testReplaceKey() {
    TestClass testObj = new TestClass();
    testObj.replaceParam1 = "Hello";
    testObj.replaceParam2 = "World";

    String expected = "superSpecialKey=Hello&superSpecialKey=World";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testList() {
    TestClass testObj = new TestClass();
    testObj.list = List.of("Hello", "World");

    String expected = "listKey=Hello&listKey=World";
    assertEquals(expected, testObj.toQueryParams());
  }

  @Test
  void testArray() {
    TestClass testObj = new TestClass();
    testObj.array = new String[] {"Hello", "World"};

    String expected = "arrayKey=Hello&arrayKey=World";
    assertEquals(expected, testObj.toQueryParams());
  }

  public static class TestClass implements QueryParams {
    @QueryParam String param1;

    @QueryParam(value = "customName")
    String param2;

    @QueryParam(formatKey = "my%s[]")
    String param3;

    @QueryParam String specialCharParam;

    @QueryParam Integer numberParam;

    @QueryParam("superSpecialKey")
    String replaceParam1;

    @QueryParam("superSpecialKey")
    String replaceParam2;

    @QueryParam("listKey")
    List<String> list;

    @QueryParam("arrayKey")
    String[] array;
  }

  public static class TestClassNull implements QueryParams {
    String param1;

    String param2;

    String param3;
  }
}
