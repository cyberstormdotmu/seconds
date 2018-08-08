package net.java.sip.communicator.impl.gui.main.menus;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportFile
{

    public boolean getExport(List<ExportRosterEntryModel> data, String path){

        boolean isSuccessful = false;
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("import");
            doc.appendChild(rootElement);
            
            for(int i=0;i<data.size();i++){
                
             // Contact elements
                Element Contact = doc.createElement("Contact");
                rootElement.appendChild(Contact);

                // set attribute to Contact element
                Attr attr = doc.createAttribute("id");
                attr.setValue(String.valueOf(i));
                Contact.setAttributeNode(attr);

                // shorten way
                // Contact.setAttribute("id", "1");

                // jid elements
                Element jid = doc.createElement("jid");
                jid.appendChild(doc.createTextNode(data.get(i).getUserId()));
                Contact.appendChild(jid);

                // name elements
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(data.get(i).getUserName()));
                Contact.appendChild(name);

                // group elements
                Element group = doc.createElement("group");
                group.appendChild(doc.createTextNode(data.get(i).getGroupName()));
                Contact.appendChild(group);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            
            if(!path.substring(path.length()-4).equalsIgnoreCase(".xml"))
            {
                path = path+".xml";
            }
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
            isSuccessful = true;
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return isSuccessful;

    }
}
