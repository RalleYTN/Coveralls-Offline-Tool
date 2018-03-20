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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import de.ralleytn.simple.json.JSONArray;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * 
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.2.0
 * @since 1.2.0
 */
public class TravisClient extends Client {

	private String token;
	
	public TravisClient(String token) {
		
		this.token = token;
	}
	
	@Override
	protected HttpURLConnection createConnection(String url, String method) throws IOException {
		
		HttpURLConnection connection = super.createConnection(url, method);
		connection.setRequestProperty("Travis-API-Version", "3");
		connection.setRequestProperty("Authorization", "token " + this.token);
		connection.setRequestProperty("User-Agent", "Coveralls Offline Tool");
		
		return connection;
	}
	
	/**
	 * Grabs the latest job id of your latest build of the given repository.
	 * @param repo the repository (User/Repo)
	 * @return the job id
	 * @throws JSONParseException if the response JSON could not be parsed
	 * @throws IOException if there was an I/O error in the request
	 * @since 1.0.0
	 */
	public long getLatestServiceJobId(String repository) throws JSONParseException, IOException {
		
		JSONObject response = this.get("/repo/" + URLEncoder.encode(repository, "UTF-8") + "/builds");
		JSONArray jobs = this.getLatestBuild(response).getArray("jobs");
		JSONObject latestJob = jobs.getObject(jobs.size() - 1);
		long jobId = latestJob.getLong("id");
		System.out.println("[INFO] Latest Job: " + jobId);
		
		return jobId;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 * @since 1.2.0
	 */
	private JSONObject getLatestBuild(JSONObject response) {
		
		JSONArray builds = response.getArray("builds");
		JSONObject latestBuild = builds.getObject(0);
		System.out.println("[INFO] Latest Build: " + latestBuild.getString("id"));
		return latestBuild;
	}
	
	/**
	 * Does a GET request against the Travis CI API.
	 * @param endpoint the API endpoint
	 * @return the response object
	 * @throws JSONParseException if the response JSON could not be parsed
	 * @throws IOException if there was an I/O error in the request
	 * @since 1.0.0
	 */
	private JSONObject get(String endpoint) throws JSONParseException, IOException {
		
		String url = "https://api.travis-ci.org" + endpoint;
		System.out.println("[INFO] Do a GET to " + url);
		return new JSONObject(this.readResponse(this.createConnection(url, "GET")));
	}
}
