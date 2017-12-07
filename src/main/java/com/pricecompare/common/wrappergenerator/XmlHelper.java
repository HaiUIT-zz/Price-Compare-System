package com.pricecompare.common.wrappergenerator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlHelper
{
    public static HashMap<String, String> getFormattingTags(String filePath)
    {
        HashMap<String, String> formatTags = new HashMap<>();
        try
        {
            Resource resource = new ClassPathResource(filePath);
            File xmlFile = resource.getFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("TAG");
            
            if (nList != null)
            {
                for (int i = 0; i < nList.getLength(); i++)
                {
                    formatTags.put(nList.item(i).getTextContent(), nList.item(i).getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return  formatTags;
    }

    public static List<String> getObjects(String filePath)
    {
        List<String> objects = new ArrayList<>();
        try
        {
            Resource resource = new ClassPathResource(filePath);
            File xmlFile = resource.getFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("OBJECT");

            if (nList != null)
            {
                for (int i = 0; i < nList.getLength(); i++)
                {
                    objects.add(nList.item(i).getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return  objects;
    }

    public static HashMap<String, List<String>> getKnowledgeDetail(String filePath, String objectName)
    {
        HashMap<String, List<String>> knowledgeDetail = new HashMap<>();
        List<String> formats = new ArrayList<>();
        List<String> terms = new ArrayList<>();
        try
        {
            Resource resource = new ClassPathResource(filePath);
            File xmlFile = resource.getFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(objectName);

            if (nList.getLength() > 1)
            {
                throw new Exception("Find more than one define for same object");
            }
            else
            {
                Element element = (Element)nList.item(0);
                NodeList nTerms = element.getElementsByTagName("TERM");
                NodeList nFormats = element.getElementsByTagName("FORMAT");

                String format = "";
                String term = "";
                for (int i = 0; i < nTerms.getLength(); i++)
                {
                    HashMap<String, String> map = new HashMap<>();
                    term = nTerms.item(i).getTextContent();
                    map.put("term", term);
                    terms.add(term);
                    for(int j = 0; j < nFormats.getLength(); j++)
                    {
                        format = nFormats.item(j).getTextContent();
                        format = StrSubstitutor.replace(format, map);
                        formats.add(format);
                    }
                }
                knowledgeDetail.put("terms", terms);
                knowledgeDetail.put("formats", formats);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return knowledgeDetail;
    }

    public static DOMSource exportToXml(List<LogicalLine> logicalLines)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("WRAPPER");
            doc.appendChild(rootElement);


            for(LogicalLine line: logicalLines)
            {
                Element object = doc.createElement(line.getObject());

                Element format = doc.createElement("FORMAT");
                format.appendChild(doc.createTextNode(line.getFormat()));
                object.appendChild(format);

                rootElement.appendChild(object);
            }
            return new DOMSource(doc);
        } catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }

        return null;
    }

    public static void saveWrapper(DOMSource dom, String filePath, String fileName)
    {
        try
        {
            String path =  new  ClassPathResource(filePath).getPath();
            path = FilenameUtils.getPath(path);
            path = URLDecoder.decode(path, "utf8") + fileName;
            File xmlFile = new File(path);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(xmlFile);

            transformer.transform(dom, result);
        } catch (UnsupportedEncodingException | TransformerException e)
        {
            e.printStackTrace();
        }

    }

    public static  Wrapper getWrapper(String filePath)
    {
        Wrapper wrapper = new Wrapper();
        try
        {
            Resource resource = new ClassPathResource(filePath);
            File xmlFile = resource.getFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getChildNodes();

            if (nList != null)
            {
                for (int i = 0; i < nList.getLength(); i++)
                {
                    LogicalLine line = new LogicalLine();
                    line.setObject(nList.item(i).getNodeName());
                    Node format = nList.item(i).getFirstChild();
                    line.setFormat(format.getTextContent());
                    //line.stringToId();
                    wrapper.getPattern().add(line);
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return  wrapper;
    }
}
