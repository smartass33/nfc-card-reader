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

import pointeuse.communication.ServerCommunication;
import pointeuse.pojo.SimpleEmployee;
import pointeuse.pojo.SimpleInAndOut;
import pointeuse.util.Converter;

import com.google.gson.Gson;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class CardReaderImpl implements CardReader {


    private static final Logger logger = LogManager.getLogger("CardReaderImpl");

	public String identifyUser(CardTerminal terminal) throws CardException, InterruptedException , Exception{
		Converter converter = new Converter();
		ServerCommunication communicator = new ServerCommunication();
		String convertedString = converter.asciiToHex("PATFILLER");
		byte[] wantedId = DatatypeConverter.parseHexBinary(convertedString);
		byte[] writeCommand =  new byte[]{ (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x04, (byte) 0x10};
		byte[] mergedWriteCommand = ArrayUtils.addAll(writeCommand,wantedId);
		String hexRes = null;
		String event = "";
		
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
		communicator.send(baAuth, channel);
	//	System.out.println("AUTHENTICATE: " + send(baAuth, channel));
		// If successfull, will output 9000
		//String reee = send(mergedWriteCommand, channel);

		// Read Serial
		byte[] baRead = new byte[6];

		baRead = new byte[] { (byte) 0xFF, (byte) 0xB0, (byte) 0x00,
				(byte) 0x04, (byte) 0x10 };

		
		
		////
		
		
		
		String result = communicator.send(baRead, channel);
		hexRes = converter.convertHexToString(result);
		String[] resTable = hexRes.split("FILLER");

		logger.info("USER ID: " + resTable[0]);
		SimpleEmployee employee = communicator.createEntry(resTable[0]);
		SimpleInAndOut inOrOut;
		if (employee != null && employee.inOrOuts != null){
			inOrOut = employee.inOrOuts.get(0);
	    	SimpleDateFormat formater = null;
	    	formater = new SimpleDateFormat("HH:m 'le' dd/MM/yyyy");	    		
		    String eventType = (inOrOut.type.equals('E')) ? "ENTREE":"SORTIE";
		    event = employee.firstName+ " "+employee.lastName +": "+eventType+" effectuée "+formater.format(inOrOut.time);
		}

		
		logger.debug("USER ID: " + userId);

	   Thread.sleep(3000);
		// System.out.println("READ: " + send(baRead, channel));

		// If successfull, the output will end with 900
		//card.endExclusive();
		//card.disconnect(true);

		return event;
	}



	


    

    

	

}