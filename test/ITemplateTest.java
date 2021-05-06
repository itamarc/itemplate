import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import org.gjt.itemplate.EmptyTemplateException;
import org.gjt.itemplate.ITemplate;
import org.gjt.itemplate.ParameterException;
import org.gjt.itemplate.TokensDontMatchException;
import org.junit.jupiter.api.Test;

public class ITemplateTest {
    @Test
    void simpleStringTemplateTest() throws ParameterException, EmptyTemplateException, TokensDontMatchException {
        String template = "First [#test1 #] third [# test2#] fifth [# test3 #] [#test4#]";
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
        assertEquals("Two opening tokens without a closing one.", exception.getMessage());
    }
    @Test
    void tokensDontMatchExceptionClosingTest() {
        String template = "First [#test1 #] third test2#] fifth [# test3 #] [#test4#]";

        Exception exception = assertThrows(TokensDontMatchException.class, () ->
            new ITemplate(template,"string"));
        assertEquals("Closing token without an opening one.", exception.getMessage());
       }
}
