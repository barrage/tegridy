package validation.processor;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.validation.processor.EnumListProcessor;
import org.junit.jupiter.api.Test;

public class EnumListProcessorTests {
  @Test
  public void testPass() {
    JavaFileObject source = JavaFileObjects.forResource("processor/enumlist/CorrectClass.java");

    Compilation compilation = javac().withProcessors(new EnumListProcessor()).compile(source);

    assertTrue(compilation.errors().isEmpty());
  }

  @Test
  public void testErrorNoBaseField() {
    JavaFileObject source = JavaFileObjects.forResource("processor/enumlist/WrongTypeClass.java");

    Compilation compilation = javac().withProcessors(new EnumListProcessor()).compile(source);

    assertThat(
        compilation.errors().toString(),
        containsString("Annotation @EnumList is not applicable to type"));
  }
}
