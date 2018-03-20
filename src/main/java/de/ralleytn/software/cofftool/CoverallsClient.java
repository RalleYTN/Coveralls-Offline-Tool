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

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Represents the client that communicates with the Coveralls API.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public class CoverallsClient extends Client {

	/**
	 * Submits a coverage report to Coveralls.
	 * @param file the file that should be submitted
	 * @return {@code true} if the action was successful, else {@code false}
	 * @throws IOException if there was an I/O error in the request
	 * @since 1.0.0
	 */
	public boolean submitCoverageReport(File report) throws IOException {
		
		String url = "https://coveralls.io/api/v1/jobs";
		System.out.println("[INFO] Do a POST to " + url);
		HttpURLConnection connection = this.createConnection(url, "POST");
		
		try(MultipartWriter writer = new MultipartWriter(connection)) {
			
			writer.writeFile("json_file", report);
		}
		
		int status = connection.getResponseCode();
		System.out.println("[INFO] " + status + " " + connection.getResponseMessage());
		boolean success = (status == 200);
		return success;
	}
}
