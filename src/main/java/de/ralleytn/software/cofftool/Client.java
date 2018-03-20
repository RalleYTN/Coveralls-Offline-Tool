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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Represents an abstract client for an API.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public abstract class Client {

	/**
	 * Reads the response of a HTTP/1.1 request.
	 * @param connection the connection that performed a request
	 * @return the response as a string
	 * @throws IOException if the response could not be read
	 * @since 1.0.0
	 */
	protected String readResponse(HttpURLConnection connection) throws IOException {
		
		int status = connection.getResponseCode();
		System.out.println("[INFO] " + status + " " + connection.getResponseMessage());
		StringBuilder builder = new StringBuilder();
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(status >= 400 ? connection.getErrorStream() : connection.getInputStream()))) {
			
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				builder.append(line);
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Creates a new instance of {@linkplain HttpURLConnection}.
	 * @param url the target URL
	 * @param method the request method
	 * @return the created connection
	 * @throws IOException if no connection could be opened
	 * @since 1.0.0
	 */
	protected HttpURLConnection createConnection(String url, String method) throws IOException {
		
		HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
		connection.setRequestMethod(method);
		connection.setReadTimeout(60000);
		connection.setConnectTimeout(10000);
		connection.setAllowUserInteraction(false);
		connection.setDefaultUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput("POST".equals(method));
		connection.setInstanceFollowRedirects(false);
		connection.setUseCaches(false);
		
		return connection;
	}
}
