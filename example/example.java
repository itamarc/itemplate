import org.gjt.itemplate.*;
import java.io.*;
import java.util.*;

public class example {
	public static void main (String[] args) {
		try {
			// Use the file example.txt as the template source
			ITemplate t = new ITemplate("example.html","path");
			// Define the substitutions that will be made
			Hashtable h = new Hashtable();
			h.put("chave1","blabla 1");
			h.put("chave2","blabla 2");
			h.put("chave3","blabla 3");
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

