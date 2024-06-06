package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.SchemeProcessor;
import org.junit.jupiter.api.Test;

public class SchemeProcessorTests {

  @Test
  public void testPass() {
    JavaFileObject source = JavaFileObjects.forResource("processor/scheme/CorrectClass.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testErrorNoBaseField() {
    JavaFileObject source = JavaFileObjects.forResource("processor/scheme/MissingBaseField.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "The class 'MissingBaseField' is missing 'baseField' required by @Scheme: 'base'"));
  }

  @Test
  public void testErrorNoArgumentField() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/MissingArgumentField.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "The class 'MissingArgumentField' is missing one of argument fields required by @Scheme: 'argument2'."));
  }

  @Test
  public void testErrorNoValidationMethod() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/MissingValidationMethod.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method 'valid' required by @Scheme not found in class 'MissingValidationMethod'."));
  }

  @Test
  public void testErrorIllegalReturnType() {
    JavaFileObject source = JavaFileObjects.forResource("processor/scheme/IllegalReturnType.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method 'valid' in class 'IllegalReturnType' must return boolean as required by @Scheme."));
  }

  @Test
  public void testErrorIllegalParametersType() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/IllegalParameterTypes.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method 'valid' in class 'IllegalParameterTypes' must have exactly 3 parameters matching types of 'java.lang.Integer', 'java.lang.Integer', 'java.lang.String' as required by @Scheme."));
  }

  @Test
  public void testErrorIllegalParameterNumber() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/IllegalParameterNumber.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method 'valid' in class 'IllegalParameterNumber' must have exactly 3 parameters matching types of 'java.lang.Integer', 'java.lang.Integer', 'java.lang.String' as required by @Scheme."));
  }

  @Test
  public void testMultipleAnnotationsPass() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/PassMultipleAnnotations.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testMultipleAnnotationsFail() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/scheme/FailMultipleAnnotations.java");

    Compilation compilation = javac().withProcessors(new SchemeProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString(
            "Method 'isAfter' in class 'FailMultipleAnnotations' must have exactly 2 parameters matching types of 'java.lang.Integer', 'java.lang.Integer' as required by @Scheme."));
  }
}
