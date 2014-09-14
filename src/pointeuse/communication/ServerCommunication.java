package pointeuse.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

import pointeuse.pojo.SimpleEmployee;

import com.google.gson.Gson;

public class ServerCommunication {

	private final String USER_AGENT = "Mozilla/5.0";


	/**
	 * @param args
	 */
	public SimpleEmployee createEntry(String userName) throws Exception {
		SimpleEmployee employee = null;
		String outputString = "";
		//String url = "http://localhost:8080/pointeuse/employee/logEmployee/?username="+ userName;
		String url = "http://192.168.1.15:8080/pointeuse/employee/logEmployee/?username="+ userName;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		if (responseCode == 200) {
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder builder = new StringBuilder();

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				outputString = inputLine;
				builder.append(inputLine).append("\n");
			}
			Gson gson = new Gson();
			employee = gson.fromJson(outputString, SimpleEmployee.class);
			reader.close();
		}
		return employee;
	}
	
	
	public String retrieveContent(String userName) throws IOException{
		String outputString = "";
		StringBuilder builder = new StringBuilder();

		//String url = "http://localhost:8080/pointeuse/list";
		String url = "http://localhost:8080/pointeuse/employee/logEmployee/?username="+ userName;

		URL oracle = new URL(url);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
			//outputString = inputLine;
			builder.append(inputLine).append("\n");

            
        }
        in.close();
        System.out.println(builder.toString());
        return builder.toString();
	}
	

	public String send(byte[] cmd, CardChannel channel) {
		String res = "";
		int output = 0;
		byte[] baResp = new byte[258];
		ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
		ByteBuffer bufResp = ByteBuffer.wrap(baResp);

		// output = The length of the received response APDU

		try {
			output = channel.transmit(bufCmd, bufResp);
		} catch (CardException ex) {
			ex.printStackTrace();
		}
		for (int i = 0; i < output; i++) {
			res += String.format("%02X", baResp[i]);
			// The result is formatted as a hexadecimal integer
		}
		return res;
	}

}
