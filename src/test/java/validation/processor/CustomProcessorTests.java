package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.CustomProcessor;
import org.junit.jupiter.api.Test;

public class CustomProcessorTests {

  @Test
  public void testPass() {
    JavaFileObject source = JavaFileObjects.forResource("processor/custom/CorrectClass.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testErrorNoMethod() {
    JavaFileObject source = JavaFileObjects.forResource("processor/custom/MissingMethod.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate not found in class validation.processor.custom.testClasses.MissingMethod required by @net.barrage.tegridy.validation.annotation.Custom"));
  }

  @Test
  public void testErrorMethodNotBoolean() {
    JavaFileObject source = JavaFileObjects.forResource("processor/custom/MethodNotBoolean.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate2 in class validation.processor.custom.testClasses.MethodNotBoolean must return boolean required by @net.barrage.tegridy.validation.annotation.Custom"));
  }

  @Test
  public void testErrorMethodMultipleParams() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/custom/MethodMultipleParams.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate in class validation.processor.custom.testClasses.MethodMultipleParams must have exactly one parameter required by @net.barrage.tegridy.validation.annotation.Custom"));
  }

  @Test
  public void testErrorMethodWrongParam() {
    JavaFileObject source = JavaFileObjects.forResource("processor/custom/MethodWrongParam.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate in class validation.processor.custom.testClasses.MethodWrongParam must accept a parameter of type java.lang.Integer to match @net.barrage.tegridy.validation.annotation.Custom field"));
  }

  @Test
  public void testMultipleAnnotationsFail() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/custom/FailMultipleAnnotations.java");

    Compilation compilation = javac().withProcessors(new CustomProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate in class validation.processor.custom.testClasses.FailMultipleAnnotations must return boolean required by @net.barrage.tegridy.validation.annotation.Custom"));
    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method validate1 not found in class validation.processor.custom.testClasses.FailMultipleAnnotations required by @net.barrage.tegridy.validation.annotation.Custom"));
  }
}
