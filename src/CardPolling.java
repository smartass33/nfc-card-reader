import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.TimerTask;

import javax.swing.Timer;


public class CardPolling implements ActionListener {

	Timer timer;
	public CardPolling(){
	
	}
	
	
	
    public static void main(String args[]) {
    	java.util.Timer t = new java.util.Timer();
    	t.schedule(new TimerTask() {

    	            @Override
    	            public void run() {
    	                System.out.println("date" +new Date());

    	            }
    	        }, 200, 200);
    }


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
