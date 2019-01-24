package adhoc;
import nu.xom.*;
import java.io.IOException;
import java.util.Iterator;

public class TMXcleaner {

  public static final String namespace = "http://www.w3.org/XML/1998/namespace";
  
  public static void main(String[] args) {
   
   if (args.length < 2) {
     System.err.println("Usage: java -jar TMXcleaner.jar <xml-file> <target-element-name: tu>");
     return;
   }      

   String targetElementName = args[1];
   
   Builder builder = new Builder();
   try {
	 System.err.println("Building XML doc...");
     Document doc = builder.build(args[0]);
	 //NB: For XPath queries to work: We need to add xml: to the namespace manager and associate it with the xml prefix
	 XPathContext xpc = XPathContext.makeNamespaceContext(doc.getRootElement());
	 xpc.addNamespace("xml", namespace);
	 System.err.println("Finding target elemements in doc...");
     //Nodes transUnits = doc.query("//" + targetElementName);
     //If huge document, process only the first X nodes!
	 Element body = doc.getRootElement().getFirstChildElement("body");
	 //Structure: <body><tu>
	 //<tuv xml:lang="da-dk"><prop...><seg></tuv>
	 //<tuv xml:lang="en-us"><prop...><seg></tuv>
	 //</tu>
	 Element tuv1_src = body.getChildElements().get(0).getChildElements().get(0);
	 Element tuv1_tar = body.getChildElements().get(0).getChildElements().get(1);
	 Element tuv2_src = body.getChildElements().get(1).getChildElements().get(0);
	 Element tuv2_tar = body.getChildElements().get(1).getChildElements().get(1);
	 Element tuv3_src = body.getChildElements().get(2).getChildElements().get(0);
	 Element tuv3_tar = body.getChildElements().get(2).getChildElements().get(1);
	 //TODO: Check target for <prop> on <tuv> with the value <prop type="reviewed">false</prop>
	 System.out.println(tuv1_src.query("@xml:lang").get(0).getValue() + ";" + tuv1_src.getChildElements("seg").get(0).getValue());
	 Nodes reviewStatus = tuv1_tar.query("prop[@type='reviewed']");
	 if (reviewStatus.size() > 0) {
		 if (reviewStatus.get(0).getValue().contains("false")){
			 System.out.println("NOT REVIEWED;" + tuv1_tar.query("@xml:lang").get(0).getValue() + ";" + tuv1_tar.getChildElements("seg").get(0).getValue());
		 }
		 else {
			 System.out.println("REVIEWED;" + tuv1_tar.query("@xml:lang").get(0).getValue() + ";" + tuv1_tar.getChildElements("seg").get(0).getValue());
		 }
	 }
	 System.out.println(tuv2_src.query("@xml:lang").get(0).getValue() + ";" + tuv2_src.getChildElements("seg").get(0).getValue());
	 System.out.println(tuv2_tar.query("@xml:lang").get(0).getValue() + ";" + tuv2_tar.getChildElements("seg").get(0).getValue());
	 System.out.println(tuv3_src.query("@xml:lang").get(0).getValue() + ";" + tuv3_src.getChildElements("seg").get(0).getValue());
	 System.out.println(tuv3_tar.query("@xml:lang").get(0).getValue() + ";" + tuv3_tar.getChildElements("seg").get(0).getValue());
	 //System.out.println(tuv3_tar.getAttributeValue("xml:lang") + ";" + tuv3_tar.getChildElements("seg").get(0).getValue());
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