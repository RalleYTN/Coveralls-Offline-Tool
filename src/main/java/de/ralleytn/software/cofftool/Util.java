/*
 * MIT License
 * 
 * Copyright (c) 2018 Ralph Niemitz
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.ralleytn.software.cofftool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains some utility methods for the project.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public final class Util {

	private Util() {}
	
	/**
	 * 
	 * @param exception
	 * @since 1.2.0
	 */
	public static final void printException(Exception exception) {
		
		System.err.println("[ERROR]");
		System.err.println("[ERROR] ----");
		System.err.println("[ERROR] " + exception.getClass().getName() + ": " + exception.getMessage());
		
		for(StackTraceElement stackTraceElement : exception.getStackTrace()) {
			
			System.err.println("[ERROR] \t" + stackTraceElement.toString());
		}
		
		System.err.println("[ERROR] ----");
		System.err.println("[ERROR]");
	}
	
	/**
	 * Combines the package name with the source file name.
	 * @param node the node of the source file
	 * @return the complete source file name on default package level
	 * @since 1.0.0
	 */
	public static final String getFullName(Node node) {
		
		Node parent = node.getParentNode();
		String packageName = parent.getAttributes().getNamedItem("name").getNodeValue();
		String fileName = node.getAttributes().getNamedItem("name").getNodeValue();
		
		return packageName + "/" + fileName;
	}
	
	/**
	 * Searches for the corresponding node of the given source file.
	 * @param nodes the node list in which should be searched
	 * @param sourceFile the source file
	 * @return the node or {@code null} if there is no node for the given source file
	 * @since 1.0.0
	 */
	public static final Node getNodeForSourceFile(NodeList nodes, File sourceFile) {
		
		for(int index = 0; index < nodes.getLength(); index++) {
			
			Node node = nodes.item(index);

			if(sourceFile.getAbsolutePath().replace("\\", "/").endsWith(Util.getFullName(node))) {
				
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * Counts how many lines there are in a single source file.
	 * @param file the source file
	 * @return the number of lines in this file
	 * @throws IOException if the file could not be read
	 * @since 1.0.0
	 */
	public static final int getLineCount(File file) throws IOException {
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			int lines = 0;
			
			while(reader.readLine() != null) {
				
				lines++;
			}
			
			return lines;
		}
	}
	
	/**
	 * Crawls through a directory tree recursively.
	 * @param root the tree root
	 * @param callback the callback function that is called for every element in the tree
	 * @since 1.0.0
	 */
	public static final void crawlThroughFileTree(File root, Consumer<File> callback) {
		
		File[] subFiles = root.listFiles();
		
		if(subFiles != null) {
			
			for(File subFile : subFiles) {
				
				if(subFile.isDirectory()) {
					
					Util.crawlThroughFileTree(subFile, callback);
					
				}
				
				callback.accept(subFile);
			}
		}
	}
	
	/**
	 * Parses an XML file.
	 * @param xmlFile the XML file
	 * @return the parsed XML file as DOM
	 * @throws SAXException if the XML is invalid
	 * @throws IOException if the file could not be read
	 * @throws ParserConfigurationException if the XML is invalid
	 * @since 1.0.0
	 */
	public static final Document parseXML(File xmlFile) throws SAXException, IOException, ParserConfigurationException {

		System.out.println("[INFO] Parse " + xmlFile.getAbsolutePath());
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xmlFile);
		document.getDocumentElement().normalize();
			
		return document;
	}
	
	/**
	 * Makes a {@code byte} array out of the current timestamp.
	 * @return the resulting {@code byte} array
	 * @throws IOException will probably never happen
	 * @since 1.2.0
	 */
	public static final byte[] getTimestampAsByteArray() throws IOException {
		
		long timestamp = System.currentTimeMillis();
		int[] unsignedBytes = new int[Long.BYTES];
		
		for(int index = 0; index < unsignedBytes.length; index++) {
			
			unsignedBytes[index] = Util.getOctet(timestamp, index);
		}
		
		return Util.toByteArray(unsignedBytes);
	}
	
	/**
	 * Converts an {@code int} array to a {@code byte} array.
	 * @param integers the {@code int} array
	 * @return the created {@code byte} array
	 * @throws IOException will probably never happen
	 * @since 1.0.0
	 */
	public static final byte[] toByteArray(int[] integers) throws IOException {
		
		try(ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			
			for(int integer : integers) {
				
				buffer.write(integer);
			}
			
			return buffer.toByteArray();
		}
	}
	
	/**
	 * Gets an octet from a bit sequence.
	 * @param integer the bit sequence
	 * @param position the octet position
	 * @return the octet
	 * @since 1.0.0
	 */
	public static final int getOctet(long integer, int position) {
		
		return (int)((integer >> (position * 8)) & 0xFF);
	}
	
	/**
	 * Hashes a set of binary data with MD5.
	 * @param data the binary data
	 * @return a hexadecimal MD5 hash string of the given data
	 * @since 1.0.0
	 */
	public static final String createMD5(byte[] data) {

		String md5 = null;
		
		try {
			
			md5 = Util.toHexString(MessageDigest.getInstance("MD5").digest(data));
			
		} catch(NoSuchAlgorithmException exception) {
			
			// WILL NEVER HAPPEN BECAUSE THE ALGORITHM EXISTS!
		}
		
		return md5;
	}
	
	/**
	 * Converts a set of binary data to a hexadecimal string.
	 * @param binary the binary data
	 * @return the created hexadecimal string
	 * @since 1.0.0
	 */
	public static final String toHexString(byte[] binary) {
		
		StringBuilder hexBuilder = new StringBuilder();
		
		for(byte b : binary) {
			
			hexBuilder.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
		}
		
		return hexBuilder.toString();
	}
	
	/**
	 * Reads the file binary.
	 * @param file the file that should be read
	 * @return the binary data of the given file
	 * @throws IOException if something went wrong while reading the file
	 * @since 1.0.0
	 */
	public static final byte[] readFile(File file) throws IOException {
		
		try(InputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			int read = 0;
			byte[] buffer = new byte[4096];
			
			while((read = in.read(buffer)) != -1) {
				
				out.write(buffer, 0, read);
			}
			
			return out.toByteArray();
		}
	}
}
