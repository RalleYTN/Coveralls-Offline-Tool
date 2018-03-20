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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Writes a multipart form on the output stream of a HTTP request.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public class MultipartWriter implements AutoCloseable {

	private BufferedWriter writer;
	private String boundary;
	
	/**
	 * 
	 * @param connection
	 * @throws IOException
	 * @since 1.2.0
	 */
	public MultipartWriter(HttpURLConnection connection) throws IOException {
		
		this.boundary = Util.createMD5(Util.getTimestampAsByteArray());
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
		this.writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
	}
	
	/**
	 * 
	 * @param name
	 * @param file
	 * @throws IOException
	 * @since 1.2.0
	 */
	public void writeFile(String name, File file) throws IOException {
		
		this.writer.write("--");
		this.writer.write(this.boundary);
		this.writer.write("\r\n");
		this.writer.write("Content-Disposition: form-data; name=\"");
		this.writer.write(name);
		this.writer.write("\"; filename=\"");
		this.writer.write(file.getName());
		this.writer.write("\"\r\nContent-Type: application/json; charset=UTF-8\r\n");
		this.writer.write("Content-Transfer-Encoding: binary\r\n\r\n");
		this.writer.write(new String(Util.readFile(file), StandardCharsets.UTF_8));
		this.writer.write("\r\n");
	}
	
	@Override
	public void close() throws IOException {
		
		this.writer.write("--");
		this.writer.write(this.boundary);
		this.writer.write("--");
		this.writer.close();
	}
}
