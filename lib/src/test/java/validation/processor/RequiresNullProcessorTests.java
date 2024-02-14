package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.RequiresNullProcessor;
import org.junit.jupiter.api.Test;

public class RequiresNullProcessorTests {

  @Test
  public void testPass() {
    JavaFileObject source = JavaFileObjects.forResource("processor/requiresnull/CorrectClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNullProcessor())
        .compile(source);

    assertTrue(compilation.errors().isEmpty(), "Expected no compilation errors");
  }

  @Test
  public void testErrorMissingMainField() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/requiresnull/MissingMainFieldClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNullProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString("is missing required field by @RequiresNull: 'mainField'"));
  }

  @Test
  public void testErrorMissingRequiredFields() {
    JavaFileObject source =
        JavaFileObjects.forResource("processor/requiresnull/MissingRequiredFieldsClass.java");

    Compilation compilation = javac()
        .withProcessors(new RequiresNullProcessor())
        .compile(source);

    assertThat(compilation.errors().toString(),
        containsString("is missing required field by @RequiresNull: 'missingRelatedField2'"));
  }
}
