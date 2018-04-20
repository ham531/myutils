package com.exam.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParser {
	public static void main(String[] args) throws Exception {
		String tagName = "merAcct";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(
				getXmlStr())));
		NodeList nodeList = doc.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String textContent = node.getTextContent();
			System.out.println(textContent);
		}
	}

	private static String getXmlStr() {
		StringBuffer xmlBuff = new StringBuffer();
		xmlBuff.append("<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?>");
		xmlBuff.append("<B2CRes>");
		xmlBuff.append("<interfaceName>ICBC_PERBANK_B2C</interfaceName>");
		xmlBuff.append("<interfaceVersion>1.0.0.11</interfaceVersion>");
		xmlBuff.append("<orderInfo>");
		xmlBuff.append("<orderDate>20161101163724</orderDate>");
		xmlBuff.append("<curType>001</curType>");
		xmlBuff.append("<merID>3100EE20022039</merID>");
		xmlBuff.append("<subOrderInfo>");
		xmlBuff.append("<orderid>2016110416173267</orderid>");
		xmlBuff.append("<amount>5</amount>");
		xmlBuff.append("<installmentTimes>1</installmentTimes>");
		xmlBuff.append("<merAcct>3100022209026427501</merAcct>");
		xmlBuff.append("</subOrderInfo>");
		xmlBuff.append("</orderInfo>");
		xmlBuff.append("<custom>");
		xmlBuff.append("<verifyJoinFlag>0</verifyJoinFlag>");
		xmlBuff.append("<JoinFlag></JoinFlag>");
		xmlBuff.append("<UserNum></UserNum>");
		xmlBuff.append("</custom>");
		xmlBuff.append("<bank>");
		xmlBuff.append("<TranSerialNo></TranSerialNo>");
		xmlBuff.append("<TranBatchNo>201611031050149</TranBatchNo>");
		xmlBuff.append("<notifyDate>20161101173724</notifyDate>");
		xmlBuff.append("<tranStat>1</tranStat>");
		xmlBuff.append("<comment>交易成功！</comment>");
		xmlBuff.append("</bank>");
		xmlBuff.append("</B2CRes>");
		return xmlBuff.toString();
	}
}
