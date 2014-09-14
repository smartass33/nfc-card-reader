import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import pointeuse.CardReader;
import pointeuse.CardReader;
import pointeuse.gui.PopUp;


public class Execute {

	
    private static final Logger logger = LogManager.getLogger("Execute");

    /**
     * @param args
     */
    public static void main(String[] args) {

    	for (String s: args) {
            System.out.println(s);
        }
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
              String key = (String)keys.nextElement();
              String value = (String)p.get(key);
         //     logger.debug(key + ": " + value);
            }

            CardReader cardReader = new CardReader();

            /*
			TerminalFactory factory = TerminalFactory.getDefault();
			CardTerminal ACRTerminal = factory.terminals().getTerminal("ACS ACR122U 00 00");
			byte[] baReadLED = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
			//byte[] baSetBlinkingLED = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x40, (byte) 0x01, (byte) 0x04, (byte) 0x05, (byte) 0x05, (byte) 0x010, (byte) 0x00 };
			byte[] baSetBlinkingLED = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x40, (byte) 0xD0, (byte) 0x04, (byte) 0x03, (byte) 0x03, (byte) 0x03, (byte) 0x01 };
			 byte[] baRead = new byte[] { (byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) 0x04, (byte) 0x10 };
			CardChannel channel;
			CommandAPDU commandAPDU;
			ResponseAPDU ledStateAPDU;
			ResponseAPDU ledBlinkAPDU;
			ResponseAPDU cardContent;

			Card card;
			try {
				card = ACRTerminal.connect("*");
				channel = card.getBasicChannel();
				commandAPDU = new CommandAPDU(baReadLED);
				ledStateAPDU = channel.transmit(commandAPDU);
				
				commandAPDU = new CommandAPDU(baSetBlinkingLED);
				ledBlinkAPDU = channel.transmit(commandAPDU);

				
				
				cardContent = channel.transmit(commandAPDU);

			} catch (CardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
			
			System.out.println("tttt");
			
    
    }
}
