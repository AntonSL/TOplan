import java.awt.EventQueue;


public class MainClass {

	public static void main(String[] args)
	{
		
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				//create main program frame
				MyFrame mainFrame = new MyFrame();
				//and display it
				mainFrame.setVisible(true);
			}
		});
		


	}
	
}
