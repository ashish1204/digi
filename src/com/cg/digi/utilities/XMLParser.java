/******************************************************************************
Copyright © 2016 Capgemini Group of companies. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF Capgemini GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND Capgemini GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
******************************************************************************/

package com.cg.digi.utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cg.digi.model.Handset;

public class XMLParser {
	
	public static void main(String[] args) {
		XMLParser.editXMLNode("D:\\digi_workspace\\DigitalQAServices\\DigiAssure\\Deepika_Govindaiah\\CGPF_Neoload\\Neo1\\Neoload" + "/repository1.xml", "D:\\digi_workspace\\DigitalQAServices\\DigiAssure\\Deepika_Govindaiah\\CGPF_Neoload\\Neo1\\Neoload" + "/repository1.xml", "value", ".bat", "D:\\digi_workspace\\DigitalQAServices\\DigiAssure\\Deepika_Govindaiah\\CGPF_Neoload\\Neo11"+"/NeoSeeTest1.bat");
		
	}
	
	public  List<Handset> getDeviceList(String xmlString) {
		List<Handset> handsetDetailList= new ArrayList<Handset>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF8")));
			doc.getDocumentElement().normalize();
			
			NodeList handsetList = doc.getElementsByTagName("handset");
			handsetDetailList = new ArrayList<Handset>(handsetList.getLength());
			for (int temp = 0; temp < handsetList.getLength(); temp++) {
				Node nNode = handsetList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Handset hs = new Handset();
					String deviceId = eElement.getElementsByTagName("deviceId").item(0).getTextContent();
					String modelName = eElement.getElementsByTagName("model").item(0).getTextContent();
					String os = eElement.getElementsByTagName("os").item(0).getTextContent();
					String version = eElement.getElementsByTagName("osVersion").item(0).getTextContent();
					String available = eElement.getElementsByTagName("available").item(0).getTextContent();
					String reservation =eElement.getElementsByTagName("reserved").item(0).getTextContent();
					if(available.equals("true")){
						hs.setSrNo(temp);
						hs.setDeviceId(deviceId);
						hs.setDeviceName(modelName);
						hs.setOs(os);
						hs.setStatus("Available");
						hs.setVersion(version);
						hs.setAvailable(available);
						hs.setReservation(reservation);
						if(reservation.equals("true"))
							hs.setStatus("In Use");
						hs.setReservationId("null");
						handsetDetailList.add(hs);
					}
				}
			}
			

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("issue in fetch device list"+e);
			e.printStackTrace();
		}
		return handsetDetailList;
	}
	
	
	public  String  getreserveId(String xmlString) {
		String reservationId="";
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF8")));
			
			if(xmlString.contains("reservationIds")){
				NodeList reservationList = doc.getElementsByTagName("reservationIds");
				for (int temp = 0; temp < reservationList.getLength(); temp++) {
					Node nNode = reservationList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						reservationId = eElement.getElementsByTagName("reservationId").item(0).getTextContent();
						}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("issue in fetch device list" + e);
			e.printStackTrace();
		}
		return reservationId;

	}
	
	public static void editXMLNode(String fileIn, String fileOut,
			String attributeName, String attributeValue,String attributeReplace) {
		//String fileIn = "NeoloadProject/New folder/repository1.xml";
		//String fileOut = "NeoloadProject/New folder/repository1.xml";
		//attributeName = value
		//attributeValue = .bat
		//attributeReplace = "new path"
				
		try {
			// 1- Build the doc from the XML file
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(fileIn));

			// 2- Locate the node(s) with xpath
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(
					"//*[contains(@"+attributeName+", '"+attributeValue+"')]", doc,
					XPathConstants.NODESET);

			// 3- Make the change on the selected nodes
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				Node value = nodes.item(idx).getAttributes()
						.getNamedItem("value");
				String val = value.getNodeValue();
				value.setNodeValue(attributeReplace);
				// value.setNodeValue(val.replaceAll("!Here", "What?"));
			}

			// 4- Save the result to a new XML doc
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File(
					fileOut)));
		} catch (Exception e) {
			System.out.println("error " + e);
		}

	}
	
}
