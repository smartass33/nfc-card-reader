package pointeuse.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class PopUp extends JFrame  {




    public void createPane(String event){
    	
		try {
	
			JEditorPane editorPane = new JEditorPane("text/html", event);
			this.add(editorPane);

	    	Timer timer = new Timer(3000, new AbstractAction() {
	    	    @Override
	    	    public void actionPerformed(ActionEvent ae) {
	    	    	dispose();
	    	    }
	    	});
		    
	    	timer.setRepeats(false);//the timer should only go off once
	    
	    	//start timer to close JDialog as dialog modal we must start the timer before its visible
	    	timer.start();
			setVisible(true);
			setSize(450, 240);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some problem has occured" + e.getMessage());
		}
    }
    
 
}
