package modification;

import static com.google.testing.compile.Compiler.javac;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import net.barrage.tegridy.modification.ModificationProcessor;
import org.junit.jupiter.api.Test;

public class ModifyProcessorTests {
  @Test
  public void mismatchedTypesInFieldAndModifierFailCompilation() {
    Compilation compilation = compile("processor/modification/MismatchedTypes.java");
    assertThat(
        compilation.errors().toString(),
        containsString(
            "Field 'custom' of type (java.lang.String) does not match the modifier type (java.lang.Integer)."));
  }

  @Test
  public void mismatchedStringTypesFailCompilation() {
    Compilation compilation = compile("processor/modification/NotAString.java");

    assertThat(
        compilation.errors().toString(),
        containsString("Field 'foo' must be of type String. Required because of String modifier."));
    assertThat(
        compilation.errors().toString(),
        containsString("Field 'bar' must be of type String. Required because of String modifier."));
    assertThat(
        compilation.errors().toString(),
        containsString("Field 'baz' must be of type String. Required because of String modifier."));
    assertThat(
        compilation.errors().toString(),
        containsString("Field 'qux' must be of type String. Required because of String modifier."));
  }

  @Test
  public void nestedElementNoModifyFailCompilation() {
    Compilation compilation = compile("processor/modification/NestedNoModify.java");
    assertThat(
        compilation.errors().toString(),
        containsString(
            "@NestedModify is valid only on fields that are classes implementing `Modify`. Found "
                + "'NestedNoModify.Child'."));
  }

  @Test
  public void notFieldCustomFailCompilation() {
    Compilation compilation = compile("processor/modification/NotFieldCustom.java");
    assertThat(
        compilation.errors().toString(),
        containsString("annotation type not applicable to this kind of declaration"));
  }

  @Test
  public void notFieldStringFailCompilation() {
    Compilation compilation = compile("processor/modification/NotFieldString.java");
    assertThat(
        compilation.errors().toString(),
        containsString("annotation type not applicable to this kind of declaration"));
  }

  @Test
  public void notFieldNestedFailCompilation() {
    Compilation compilation = compile("processor/modification/NotFieldNested.java");
    assertThat(
        compilation.errors().toString(),
        containsString("annotation type not applicable to this kind of declaration"));
  }

  Compilation compile(String path) {

    JavaFileObject source = JavaFileObjects.forResource(path);

    return javac().withProcessors(new ModificationProcessor()).compile(source);
  }
}
