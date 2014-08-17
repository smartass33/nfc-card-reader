import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pointeuse.CardReader;
import pointeuse.CardReaderImpl;
import pointeuse.gui.PopUp;


public class Execute {

	
    private static final Logger logger = LogManager.getLogger("Execute");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
		  String key = (String)keys.nextElement();
		  String value = (String)p.get(key);
		  logger.debug(key + ": " + value);
		}
		
		try {
			Boolean isProcessed = false;
			String result = null;
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminal ACRTerminal = factory.terminals().getTerminal("ACS ACR122U 00 00");
			CardReader cardReader = new CardReaderImpl();

			// Establish a connection with the card
			logger.info("Waiting for a card..");
			Boolean isCardPresent = ACRTerminal.waitForCardPresent(0);
			if (isCardPresent && !isProcessed){
				isProcessed = true;
				logger.info("DATE: "+new Date());
				result = cardReader.identifyUser(ACRTerminal);
				isProcessed = false;
				new CardReaderImpl();
			}
			
			if (result != null){
				PopUp popup = new PopUp();
				popup.createPane(result);	
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
			new CardReaderImpl();
		}

		
	}
	


	
}
