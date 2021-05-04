import org.gjt.itemplate.*;
import java.util.*;

public class example {
	public static void main (String[] args) {
		try {
			// Use the file example.txt as the template source
			ITemplate t = new ITemplate("example.html","path");
			// Define the substitutions that will be made
			Hashtable<String, String> h = new Hashtable<String, String>();
			h.put("key1","alpha 1");
			h.put("key2","beta 2");
			h.put("key3","charlie 3");
			// Fill in the template
			String tmpl = t.fill(h);
			// Print the result
			System.out.println("FILLED TEMPLATE:\n"+tmpl);
		} catch (TokensDontMatchException e) {
			System.out.println("Tokens don't match...");
		} catch (EmptyTemplateException e) {
			System.out.println("Empty template...");
		} catch (ParameterException e) {
			System.out.println("Parameter error...");
		} catch (NoClassDefFoundError e) {
			System.out.println(e.getMessage());
		}
	}
}

