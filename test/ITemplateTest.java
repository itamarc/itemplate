import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.gjt.itemplate.EmptyTemplateException;
import org.gjt.itemplate.ITemplate;
import org.gjt.itemplate.ParameterException;
import org.gjt.itemplate.TokensDontMatchException;
import org.junit.jupiter.api.Test;

public class ITemplateTest {
    @Test
    void simpleStringTemplateTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException {
        String template = "First [#test1#] third [# test2#] fifth [# test3 #] [#test4#]";
        String result = "First second third fourth fifth sixth seventh";
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("test1","second");
        h.put("test2","fourth");
        h.put("test3","sixth");
        h.put("test4","seventh");
        ITemplate it = new ITemplate(template,"string");
        assertEquals(result, it.fill(h));
    }
    @Test
    void tokensDontMatchException1OpenTest() {
        String template = "First [#test1 #] third [# test2#] fifth [# test3 #] [#test4";

        assertThrows(TokensDontMatchException.class, () ->
            new ITemplate(template,"string"));
    }
    @Test
    void tokensDontMatchException2OpenTest() {
        String template = "First [#test1 third [# test2#] fifth [# test3 #] [#test4#]";

        Exception exception = assertThrows(TokensDontMatchException.class, () ->
            new ITemplate(template,"string"));
        assertEquals("Two opening tokens without a closing one at position 20.", exception.getMessage());
    }
    @Test
    void tokensDontMatchExceptionClosingTest() {
        String template = "First [#test1 #] third test2#] fifth [# test3 #] [#test4#]";

        Exception exception = assertThrows(TokensDontMatchException.class, () ->
            new ITemplate(template,"string"));
        assertEquals("Closing token without an opening one at position 28.", exception.getMessage());
    }
    /**
     * For a key not found in the HashMap, it should be changed for an empty string.
     * @throws ParameterException
     * @throws EmptyTemplateException
     * @throws TokensDontMatchException
     */
    @Test
    void inexistentKeyTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException {
        String template = "First [#test1 #] third [# test2#] fifth [# test3 #] [#test4#] [#test5#]";
        String expectedResult = "First second third fourth fifth sixth seventh ";
        ITemplate tmpl = new ITemplate(template,"string");
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("test1","second");
        h.put("test2","fourth");
        h.put("test3","sixth");
        h.put("test4","seventh");
        String result = tmpl.fill(h);
        assertEquals(expectedResult, result);
    }
    /**
     * 
     * @throws EmptyTemplateException
     * @throws TokensDontMatchException
     */
    @Test
    void unknownTypeTest() throws EmptyTemplateException, TokensDontMatchException {
        assertThrows(ParameterException.class, () -> {
            new ITemplate("text", "type"); // supported types are "string" and "path"
        });
    }
    /**
     * 
     * @throws ParameterException
     * @throws TokensDontMatchException
     */
    @Test
    void emptyTemplateTest() throws ParameterException, TokensDontMatchException {
        assertThrows(EmptyTemplateException.class, () -> {
            new ITemplate("", "string");
        });
    }
    @Test
    // Just to increase test coverage
    void setTokensTest() {
        ITemplate.setTokens("{open", "}close");
        ITemplate.setTokens("[#", "#]");
    }

    @Test
    void templateFileTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException, IOException {
        String resourceName = "template.html";

        ITemplate tmpl = new ITemplate(getResourceFile(resourceName).getAbsolutePath(), "path");
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("pagetitle", "ITemplate");
        h.put("liburl", "https://github.com/itamarc/itemplate/");
        h.put("linktitle", "ITemplate at GitHub");
        String result = tmpl.fill(h);
        String expectedResult = readFile(getResourceFile("templatefilled.html"));
        assertEquals(expectedResult, result);
    }

    @Test
    void templateMdFileTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException, IOException {
        String resourceName = "template.md";

        ITemplate tmpl = new ITemplate(getResourceFile(resourceName).getAbsolutePath(), "path");
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("onlykey", "This is a footer.");
        String result = tmpl.fill(h);
        String expectedResult = readFile(getResourceFile("templatefilled.md"));
        assertEquals(expectedResult, result);
    }

    @Test
    void templateOnelinePathTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException {
        String resourceName = "oneline.txt";
        ITemplate tmpl = new ITemplate(getResourceFile(resourceName).getAbsolutePath(), "path");
        String expectedResult = "[First second third fourth fifth sixth seventh";
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("test1", "second");
        h.put("test2", "fourth");
        h.put("test#3", "sixth");
        h.put("test4", "seventh");
        String result = tmpl.fill(h);
        assertEquals(expectedResult, result);
    }

    @Test
    void templateOnelineFileTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException {
        String resourceName = "oneline.txt";
        File tmplFile = getResourceFile(resourceName);
        ITemplate tmpl = new ITemplate(tmplFile);
        String expectedResult = "[First second third fourth fifth sixth seventh";
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("test1", "second");
        h.put("test2", "fourth");
        h.put("test#3", "sixth");
        h.put("test4", "seventh");
        String result = tmpl.fill(h);
        assertEquals(expectedResult, result);
    }

    private File getResourceFile(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        return file;
    }

    private String readFile(File file) throws IOException {
        int c;
        StringBuffer buf = new StringBuffer();
        FileReader in = new FileReader(file);
        while ((c = in.read()) != -1) {
            buf.append((char) c);
        }
        in.close();
        return buf.toString();
    }

    @Test
    void fileNotFoundTest() {
        assertThrows(EmptyTemplateException.class, () -> {
            new ITemplate(new File("ThisFileIsNotSupposedToExist.txt"));
        });
    }

    @Test
    void filePathNotFoundTest() {
        assertThrows(EmptyTemplateException.class, () -> {
            new ITemplate("ThisFileIsNotSupposedToExist.txt", "path");
        });
    }
}
