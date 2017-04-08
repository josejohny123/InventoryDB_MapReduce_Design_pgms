package buildingHierarchical.xmlfiles;

//Created by Jose Johny jose.johny@wipro.com or jose.johny@gmail.com
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NestElements {
	
	public  String nestElements(String productdetails, List<String> inventoryDetails) {
		try {
			
			String prodname = "";
			String prodId = "";
			String WareHouseId = "";
			String Qty_on_Hand = "";
			String Qty_on_Ordered = "";
			String ReOrder_level = "";
			String productArray[] = productdetails.split(",");
			// Create the new document to build the XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder bldr = dbf.newDocumentBuilder();
			Document doc = bldr.newDocument();
			
			prodId=productArray[0];
			prodname =productArray[1];
			// Copy parent node to document
			//Element parentnode = doc.createElement("products");
			//doc.appendChild(parentnode);	  
		
			//Creating product Element
			Element product = doc.createElement("product");
			product.setAttribute("productid", prodId);
			doc.appendChild(product);
			
		// For each product, add inventory  details
			for (String inventoryString : inventoryDetails) {
				String inventorySplit[] = inventoryString.split(",");
				WareHouseId=inventorySplit[1];
				Qty_on_Hand=inventorySplit[2];
				Qty_on_Ordered=inventorySplit[3];
				ReOrder_level=inventorySplit[4];		
				
			
							
				
				Element prodnameel = doc.createElement("prodname");
				prodnameel.appendChild(doc.createTextNode(prodname.toString()));
		
		
				// Add inventory details to Product
				product.appendChild(getCompanyElements(doc, product, "WareHouseId", WareHouseId.toString()));
				product.appendChild(getCompanyElements(doc, product, "QtyonHand", new String(Qty_on_Hand)));
				product.appendChild(getCompanyElements(doc, product, "QtyonOrdered", new String(Qty_on_Ordered)));
				product.appendChild(getCompanyElements(doc, product, "ReOrderlevel", new String(ReOrder_level)));
				
			}

			
			System.out.println("completed xml");
           
		/*	 // output DOM XML to console for testing
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            transformer.transform(source, console);*/
            
			// Transform the document into a String of XML and return
			return transformDocumentToString(doc);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	 // utility method to create text node
	public  Node getCompanyElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
	public  String transformDocumentToString(Document doc) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(
					writer));
			// Replace all new line characters with an empty string to have
			// one record per line.
			System.out.println("from doc");
			return writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (Exception e) {
			System.out.println("existing from doc");
			return null;
		}
	}
	
	/*//for testing it uncomment it
	public static void main(String[] args) {
		
		ArrayList<String> newinventoryDetails = new ArrayList<String>();
		String newproductdetails = "1001,Dell LapTop";
		
		newinventoryDetails.add("1001,WH03,68,25,30");
		newinventoryDetails.add("1001,WH02,68,25,30");
		newinventoryDetails.add("1002,WH02,35,50,15");
		newinventoryDetails.add("1003,WH02,68,25,30");
		NestElements testxml = new NestElements();
		
		System.out.println(testxml.nestElements(newproductdetails, newinventoryDetails));
		System.out.println("last");
	}*/
}
