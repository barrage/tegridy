package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.RequiresNotNullProcessor;
import org.junit.jupiter.api.Test;

public class RequiresNotNullProcessorTests {

  @Test
  public void testPass() {
    JavaFileObject source = JavaFileObjects.forResource(
        "processor/requiresnotnull/CorrectClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNotNullProcessor())
        .compile(source);

    assertTrue(compilation.errors().isEmpty(), "Expected no compilation errors");
  }

  @Test
  public void testErrorMissingMainField() {
    JavaFileObject source = JavaFileObjects.forResource(
        "processor/requiresnotnull/MissingMainFieldClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNotNullProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString("is missing required field by @RequiresNotNull: 'mainField'"));
  }

  @Test
  public void testErrorMissingRequiredFields() {
    JavaFileObject source = JavaFileObjects.forResource(
        "processor/requiresnotnull/MissingRequiredFieldsClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNotNullProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString("is missing required field by @RequiresNotNull: 'missingRelatedField2'"));
  }
}
