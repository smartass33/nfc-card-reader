package pointeuse;


import com.google.gson.Gson;

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
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import pointeuse.communication.ServerCommunication;
import pointeuse.gui.PopUp;
import pointeuse.pojo.SimpleEmployee;
import pointeuse.pojo.SimpleInAndOut;
import pointeuse.util.Converter;


public class CardReader {


    private static final Logger logger = LogManager.getLogger("CardReaderImpl");
	byte[] baAuth  = new byte[] { (byte) 0xFF, (byte) 0x88, (byte) 0x00,
			(byte) 0x04, (byte) 0x60, (byte) 0x00 };
	 byte[] baRead = new byte[] { (byte) 0xFF, (byte) 0xB0, (byte) 0x00,
				(byte) 0x04, (byte) 0x10 };
	byte[] writeCommand =  new byte[]{ (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x04, (byte) 0x10};

	byte[] baSetBlinkingLED = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x40, (byte) 0xD0, (byte) 0x04, (byte) 0x01, (byte) 0x01, (byte) 0x02, (byte) 0x01 };

	

	
        public CardReader(){

        	PopUp popup = new PopUp();
        	String username;
            try {
			Boolean isProcessed = false;
			String result = null;
           
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminal ACRTerminal = factory.terminals().getTerminal("ACS ACR122U 00 00");

			// Establish a connection with the card
			logger.info("Waiting for a card..");

			Boolean isCardPresent = ACRTerminal.waitForCardPresent(0);
			if (isCardPresent && !isProcessed){
				ServerCommunication communicator = new ServerCommunication();
				isProcessed = true;
				logger.info("card is present at: "+new Date());
				
			//	username = this.identifyUser(ACRTerminal);
				
				username = this.writeUser(ACRTerminal,"username");
				logger.info("userName: "+username);
			//	result = communicator.retrieveContent(username);
            //    popup.createPane(result);
				isProcessed = false;                            
			}
				new CardReader();
			}             
            catch (CardException e) {
				// TODO Auto-generated catch block
	 			popup.createPane("erreur de lecture de la carte. Veuillez reessayer");
            	logger.error("CardException");
                e.printStackTrace();
            } 
            catch (InterruptedException e) {
				// TODO Auto-generated catch block
	 			popup.createPane(e.getMessage());
            	logger.error("InterruptedException");

                e.printStackTrace();
            } 
            
            catch (Exception e) {
				// TODO Auto-generated catch block
		 			//popup.createPane(e.getMessage());
            	logger.error("Exception");

	                e.printStackTrace();
			} finally{		

				new CardReader();
			}      
        }       
    
	public String identifyUser(CardTerminal terminal) throws CardException, InterruptedException,Exception{
		Converter converter = new Converter();
		ServerCommunication communicator = new ServerCommunication();
		String convertedString = converter.asciiToHex("PATFILLER");
		byte[] wantedId = DatatypeConverter.parseHexBinary(convertedString);
		byte[] mergedWriteCommand = ArrayUtils.addAll(writeCommand,wantedId);
		String hexRes = null;
		String event = "";
		CommandAPDU commandAPDU;
		ResponseAPDU ledBlinkAPDU;

		
		logger.debug("FILLER: " + convertedString);
		String userId = null;
		Card card = terminal.connect("*");
		card.beginExclusive();
		//card.beginExclusive();
		CardChannel channel = card.getBasicChannel();
/*
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
*/
		// Authenticate
		//byte[] baAuth = new byte[7];

	//	byte[] baAuth  = new byte[] { (byte) 0xFF, (byte) 0x88, (byte) 0x00,
	//			(byte) 0x04, (byte) 0x60, (byte) 0x00 };
	//	System.out.println("AUTHENTICATE: " + send(baAuth, channel));
		// If successfull, will output 9000
		//String reee = send(mergedWriteCommand, channel);

		// Read Serial
	//	 = new byte[6];


		communicator.send(baAuth, channel);
		String result = communicator.send(baRead, channel);
		
		commandAPDU = new CommandAPDU(baSetBlinkingLED);
		ledBlinkAPDU = channel.transmit(commandAPDU);
		hexRes = converter.convertHexToString(result);
		String[] resTable = hexRes.split("FILLER");

		logger.info("USER ID: " + resTable[0]);
		//Thread.sleep(2000);
		card.endExclusive();
		card.disconnect(true);

		Boolean isCardAbsent = terminal.waitForCardAbsent(0);
		
		// System.out.println("READ: " + send(baRead, channel));

		// If successfull, the output will end with 900
		//card.endExclusive();
		//card.disconnect(true);

		return resTable[0];
	}


	public String writeUser(CardTerminal terminal,String newUserName) throws CardException, InterruptedException,Exception{
		Converter converter = new Converter();
		ServerCommunication communicator = new ServerCommunication();
		byte[] filler = DatatypeConverter.parseHexBinary(converter.asciiToHex("FILLER"));

		String username = converter.asciiToHex(newUserName);
		byte[] wantedId = DatatypeConverter.parseHexBinary(username);
		byte[] mergedWriteCommand = ArrayUtils.addAll(writeCommand,wantedId);
		mergedWriteCommand = ArrayUtils.addAll(mergedWriteCommand,filler);
		String hexRes = null;
		String event = "";
		CommandAPDU commandAPDU;
		ResponseAPDU ledBlinkAPDU;

		
		logger.debug("mergedWriteCommand: " + mergedWriteCommand);
		String userId = null;
		Card card = terminal.connect("*");
		card.beginExclusive();
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


	//	System.out.println("LOAD KEY: " + send(baLoadKey, channel));
		// If successfull, will output 9000

		// Authenticate
		//byte[] baAuth = new byte[7];

		byte[] baAuth  = new byte[] { (byte) 0xFF, (byte) 0x88, (byte) 0x00, (byte) 0x04, (byte) 0x60, (byte) 0x00 };
		communicator.send(baAuth, channel);
	//	System.out.println("AUTHENTICATE: " + send(baAuth, channel));
		// If successfull, will output 9000
		String reee = communicator.send(mergedWriteCommand, channel);

		// Read Serial
	//	 = new byte[6];


		communicator.send(baAuth, channel);
		String result = communicator.send(baRead, channel);
		
		commandAPDU = new CommandAPDU(baSetBlinkingLED);
		ledBlinkAPDU = channel.transmit(commandAPDU);
		hexRes = converter.convertHexToString(result);
		String[] resTable = hexRes.split("FILLER");

		logger.info("USER ID: " + resTable[0]);
		//Thread.sleep(2000);
		card.endExclusive();
		card.disconnect(true);

		Boolean isCardAbsent = terminal.waitForCardAbsent(0);
		
		// System.out.println("READ: " + send(baRead, channel));

		// If successfull, the output will end with 900
		//card.endExclusive();
		//card.disconnect(true);

		return resTable[0];
	}
	


    

    

	

}