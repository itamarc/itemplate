import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.gjt.itemplate.ITemplate;
import org.gjt.itemplate.ITemplatePiece;
import org.gjt.itemplate.ParameterException;
import org.junit.jupiter.api.Test;

public class ITemplatePieceTest {
    @Test
    void parameterExceptionTest() {
        assertThrows(ParameterException.class, () -> {
            new ITemplatePiece("text", 0);
        });
        assertThrows(ParameterException.class, () -> {
            new ITemplatePiece("text", 3);
        });
        assertThrows(ParameterException.class, () -> {
            new ITemplatePiece("text", -1);
        });
    }

    @Test
    void toStringTextTest() throws ParameterException {
        ITemplatePiece piece = new ITemplatePiece("Hello, world!", ITemplate.TEXT);
        String result = piece.toString();
        String expectedResult = "Type 'text' - Text: «Hello, world!»";
        assertEquals(expectedResult, result);
    }

    @Test
    void toStringKeyTest() throws ParameterException {
        ITemplatePiece piece = new ITemplatePiece("Hello, world!", ITemplate.KEY);
        String result = piece.toString();
        String expectedResult = "Type 'key' - Text: «Hello, world!»";
        assertEquals(expectedResult, result);
    }
}
