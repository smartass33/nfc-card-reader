package pointeuse;


import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

public interface CardReader {

	

	public String identifyUser(CardTerminal terminal) throws CardException, InterruptedException , Exception;



    
//	private SimpleEmployee createEntry(String userName) throws Exception;

	

}