package adhoc;
import nu.xom.*;
import java.io.IOException;
import java.util.Iterator;

public class XLIFFcleaner {

  public static final String namespace = "urn:oasis:names:tc:xliff:document:1.2";
  //public static final XPathContext xpc = new XPathContext("xliff", namespace);
  
  public static void main(String[] args) {
   
   if (args.length < 2) {
     System.err.println("Usage: java -jar XLIFFcleaner <xml-file> <target-element-name>");
     return;
   }      

   String targetElementName = args[1];
   
   Builder builder = new Builder();
   try {
     Document doc = builder.build(args[0]);
     XPathContext xpc = XPathContext.makeNamespaceContext(doc.getRootElement());
     xpc.addNamespace("xliff", namespace);    
     //System.err.println(doc.getChildCount() + " children");
     //System.err.println(doc.getRootElement().getFirstChildElement("file", namespace).getChildCount() + " children");
     Nodes transUnits = doc.query("//xliff:" + targetElementName, xpc);
     if (transUnits != null) {
    	 Iterator<Node> nodes = transUnits.iterator();
    	 while (nodes.hasNext()){
    		 Element transUnit = (Element) nodes.next();
    		 //System.err.println(transUnit.getValue());
    		 //Element source = (Element)transUnit.query("//xliff:source", xpc).get(0);
    		 //Element target = (Element)transUnit.query("//xliff:target", xpc).get(0);
    		 //Element target = transUnit.getFirstChildElement("target");
    		 System.out.println("SRC=" + transUnit.getChildElements().get(0).getValue());
    		 System.out.println("TAR=" + transUnit.getChildElements().get(1).getValue());
    	 }
     }
   }
   catch (NullPointerException ex) {
     System.err.println("Doc does not have a " + targetElementName + " element");     
   }
   catch (ParsingException ex) {
     System.err.println("Doc is malformed.");     
   }
   catch (IOException ex) {
     System.err.println("Could not read doc"); 
   }  
    
  }
  
}