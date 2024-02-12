package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.CompareProcessor;
import org.junit.jupiter.api.Test;

public class CompareProcessorTests {

  @Test
  public void testPass() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/CorrectClass.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testErrorNoBaseField() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/MissingBaseField.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString("The class 'MissingBaseField' is missing required fields by @Compare:"));
  }

  @Test
  public void testErrorNoComparisonField() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/MissingComparisonField.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString(
            "The class 'MissingComparisonField' is missing required fields by @Compare:"));
  }

  @Test
  public void testErrorNoComparisonMethod() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/MissingComparisonMethod.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString(
            "Method 'isAfter' required by @Compare not found in class 'MissingComparisonMethod'."));
  }

  @Test
  public void testErrorIllegalReturnType() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/IllegalReturnType.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString(
            "Method 'isAfter' in class 'IllegalReturnType' must return boolean as required by @Compare."));
  }

  @Test
  public void testErrorIllegalParametersType() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/IllegalParameters.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString(
            "Method 'isAfter' in class 'IllegalParameters' must have exactly 2 parameters matching types of 'java.time.LocalDateTime' and 'java.time.LocalDateTime' as required by @Compare."));
  }

  @Test
  public void testMultipleAnnotationsPass() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/PassMultipleAnnotations.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testMultipleAnnotationsFail() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/compare/FailMultipleAnnotations.java");

    Compilation compilation = javac()
        .withProcessors(new CompareProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString(
            "Method 'isAfter' in class 'FailMultipleAnnotations' must have exactly 2 parameters matching types of 'java.lang.Integer' and 'java.lang.Integer' as required by @Compare."));
  }
}
