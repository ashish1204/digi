package com.cg.digi.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cg.digi.model.Device;

public class DeviceDetails {

/*	public static void main(String args[]) throws IOException {
		 getSeetestDevices("Android", "4.4");
		//reserveSeetestDevice("118");

	}
*/
	@SuppressWarnings("unchecked")
	public static JSONArray getSeetestDevices(String os, String version)
			throws IOException {
		JSONArray finalDevices = new JSONArray();
		String host = "10.102.22.86";
		String webPage = "http://" + host + "/";
		String authStringEnc;
		String name = "digiassure"; // <== update the log in name
		String password = "Digiassure123"; // <== update log in password

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);

		URL url = new URL(webPage + "/api/v1/devices");
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic "
				+ authStringEnc);
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		int numCharsRead;
		char[] charArray = new char[1024];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0) {
			sb.append(charArray, 0, numCharsRead);
		}
		String result = sb.toString();
		JSONParser parser = new JSONParser();
	/*	List<Device> deviceDetails = new ArrayList<Device>();
		List<Device> deviceList = new ArrayList<Device>();*/
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(result);
		

			JSONArray data = (JSONArray) jsonObject.get("data");
			for (int i = 0; i < data.size(); i++) {
				JSONObject object = (JSONObject) data.get(i);
				if(object.get("deviceOs").toString().trim().equals(os)){
					if(object.get("osVersion").toString().contains(version)){
						finalDevices.add(object);
					}
					
				}
				
				/*Device device = new Device((String) objects.get("deviceName"),
						(String) objects.get("id"),
						(String) objects.get("displayStatus"),
						(String) objects.get("deviceOs"),
						(String) objects.get("osVersion"));
				deviceDetails.add(device);*/
			}
			
			//if(finalDevices.size()<10){
				for (int i = 0; i < data.size(); i++) {
					int requiredVersion=Integer.parseInt(version.trim());
					int recommendedVersion=requiredVersion-1;
					JSONObject object = (JSONObject) data.get(i);
					if(object.get("deviceOs").toString().trim().equals(os)){
						if(object.get("osVersion").toString().contains(recommendedVersion+"")){
							finalDevices.add(object);
						}
						
					}
					
				//}
			}

			/*for (Device obj : deviceDetails) {
				if ((obj.getDeviceOs().equals(os))
						&& obj.getOsVersion().contains(version)) {
					Device device1 = new Device(obj.getDeviceName(),
							obj.getDeviceID(), obj.getDisplayStatus(),
							obj.getDeviceOs(), obj.getOsVersion());
					deviceList.add(device1);
				}
			}*/

		} catch (Exception e) {
			System.out.println("===========" + e.getMessage());
		}
return finalDevices;
	}

	public static String reserveSeetestDevice(String deviceID)
			throws IOException, MalformedURLException {
		String host = "10.102.22.86"; // <== udpate your server here
		String webPage = "http://" + host + "/";
		String authStringEnc;
		String name = "digiassure"; // <== update the log in name
		String password = "Digiassure123"; // <== update log in password

		

		String authString = name + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		authStringEnc = new String(authEncBytes);
		// int Device3 = Integer.parseInt(deviceID);
		URL url = new URL(webPage + "/api/v1/devices/" + deviceID
				+ "/reservations/new");

		// URL url = new URL(webPage +
		// "/api/v1/devices/4df18a343eeb4f81/reservations/new");
		URLConnection urlConnection = url.openConnection();

		urlConnection.setRequestProperty("Authorization", "Basic "
				+ authStringEnc);

		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Accept", "application/json");
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset="
						+ StandardCharsets.UTF_8.name());
		urlConnection.setRequestProperty("Authorization", "Basic "
				+ authStringEnc);

		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		Date dateobj = new Date();

		// System.out.println("current date is :" + df.format(dateobj));

		final long hour = 3600 * 1000;
		Date newDate = new Date(dateobj.getTime() + 2 * hour);
		// System.out.println("current date after 2 hour is :" +
		// df.format(newDate));

		String urlParameters = "clientCurrentTimestamp="
				+ df.format(dateobj).toString() + "&start="
				+ df.format(dateobj).toString() + "&end="
				+ df.format(newDate).toString();
		System.out.println(urlParameters);
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		urlConnection.setRequestProperty("Content-Length",
				Integer.toString(postDataLength));

		try (DataOutputStream wr = new DataOutputStream(
				urlConnection.getOutputStream())) {
			wr.write(postData);
			// System.out.println("url is : "+ wr);
		}

		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

		InputStream stream = null;

		if (httpURLConnection.getResponseCode() >= 400) {
			stream = httpURLConnection.getErrorStream();

		} else {
			stream = httpURLConnection.getInputStream();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		// System.out.println("Response Buffer "+responseBuffer.toString());
		String msg = "";
		if (httpURLConnection.getResponseCode() < 300) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) parser
						.parse(responseBuffer.toString());
				JSONObject data = (JSONObject) jsonObject.get("data");
				msg = (String) data.get("reservations");

				// System.out.println(msg);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				// System.out.println("unable to parse json : "+e.getMessage());;
			}

		} else {
			throw new RuntimeException(responseBuffer.toString());
		}
		return msg;
		// return authString;

	}

}
