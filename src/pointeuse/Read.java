package pointeuse;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.ArrayUtils;

import pointeuse.pojo.SimpleEmployee;

import com.google.gson.Gson;

public class Read {

	private final String USER_AGENT = "Mozilla/5.0";

	  
	public Read() {

		try {
			Boolean isProcessed = false;
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminal ACRTerminal = factory.terminals().getTerminal("ACS ACR122U 00 00");

			// Establish a connection with the card
			System.out.println("Waiting for a card..");
			Boolean isCardPresent = ACRTerminal.isCardPresent();//ACRTerminal.waitForCardPresent(0);
			if (isCardPresent && !isProcessed){
				isProcessed = true;
				System.out.println("DATE: "+new Date());
				String userId = getUserId(ACRTerminal);
				System.out.println("USER ID FROM CONSTRUCTOR: " + userId);
				isProcessed = false;

				new Read();
			}
			
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			new Read();

		}

	}

	public String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		// System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	public String getUserId(CardTerminal terminal) throws CardException, InterruptedException , Exception{
		String convertedString = asciiToHex("PATFILLER");
		byte[] wantedId = DatatypeConverter.parseHexBinary(convertedString);
		byte[] writeCommand =  new byte[]{ (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x04, (byte) 0x10};
		byte[] mergedWriteCommand = ArrayUtils.addAll(writeCommand,wantedId);
		String hexRes = null;
		
		
		System.out.println("HEX VALUE: " + convertedString);
		String userId = null;
		Card card = terminal.connect("*");
		//card.beginExclusive();
		CardChannel channel = card.getBasicChannel();

		// Start with something simple, read UID, kinda like Hello
		// World!
		byte[] baReadUID = new byte[5];

		baReadUID = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00,
				(byte) 0x00, (byte) 0x00 };

		//System.out.println("UID: " + send(baReadUID, channel));
		// If successfull, the output will end with 9000

		// OK, now, the real work
		// Get Serial Number
		// Load key
		byte[] baLoadKey = new byte[12];

		baLoadKey = new byte[] { (byte) 0xFF, (byte) 0x82, (byte) 0x00,
				(byte) 0x00, (byte) 0x06, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

	//	System.out.println("LOAD KEY: " + send(baLoadKey, channel));
		// If successfull, will output 9000

		// Authenticate
		byte[] baAuth = new byte[7];

		baAuth = new byte[] { (byte) 0xFF, (byte) 0x88, (byte) 0x00,
				(byte) 0x04, (byte) 0x60, (byte) 0x00 };
		send(baAuth, channel);
	//	System.out.println("AUTHENTICATE: " + send(baAuth, channel));
		// If successfull, will output 9000
		//String reee = send(mergedWriteCommand, channel);

		// Read Serial
		byte[] baRead = new byte[6];

		baRead = new byte[] { (byte) 0xFF, (byte) 0xB0, (byte) 0x00,
				(byte) 0x04, (byte) 0x10 };

		String result = send(baRead, channel);
		hexRes = convertHexToString(result);
		String[] resTable = hexRes.split("FILLER");

		System.out.println("USER ID: " + resTable[0]);
		SimpleEmployee employee = createEntry(resTable[0]);
		// System.out.println("READ: " + send(baRead, channel));

		// If successfull, the output will end with 900
		//card.endExclusive();
		//card.disconnect(true);
		
    	SimpleDateFormat formater = null;
    	formater = new SimpleDateFormat("HH:m 'le' dd/MM/yyyy");	    		
    	Date date = new Date();
	    System.out.println(formater.format(date));
	    String eventType = employee.status?"ENTREE":"SORTIE";
	    String event = employee.firstName+ " "+employee.lastName +": "+eventType+" effectu��e �� "+formater.format(date);
	    
	   if (resTable[0]!=null && resTable[0].length()>1){
		   this.createPane(event);	
	   }        
		Thread.sleep(3000);
		return userId;
	}

	public String send(byte[] cmd, CardChannel channel) {

		String res = "";

		byte[] baResp = new byte[258];
		ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
		ByteBuffer bufResp = ByteBuffer.wrap(baResp);

		// output = The length of the received response APDU
		int output = 0;

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

	public static void main(String[] args) {
		new Read();
	}
	
	

    public void createPane(String event){
    	final JOptionPane optionPane = new JOptionPane(event, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    	final JDialog dialog = new JDialog();
    	dialog.setTitle("Message");
    	dialog.setModal(true);
    	dialog.setLocationRelativeTo(null);
    	dialog.setContentPane(optionPane);
    	dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    	dialog.pack();

    	Timer timer = new Timer(3000, new AbstractAction() {
    	    @Override
    	    public void actionPerformed(ActionEvent ae) {
    	        dialog.dispose();
    	    }
    	});
	    
    	timer.setRepeats(false);//the timer should only go off once
    	//start timer to close JDialog as dialog modal we must start the timer before its visible
    	timer.start();
    	dialog.setVisible(true);
    }
    
    public String asciiToHex(String ascii){
        StringBuilder hex = new StringBuilder();
        
        

        
        
        for (int i=0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }       
        return hex.toString();
    } 
    
	private SimpleEmployee createEntry(String userName) throws Exception {
		SimpleEmployee employee = null;  
		String outputString = "";

		String url = "http://localhost:8080/pointeuse/employee/getUser/?userName="+userName;
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		if (responseCode==200){
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
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
	


}