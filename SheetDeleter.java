import java.awt.event.*;

import org.jopendocument.dom.spreadsheet.*;

import java.io.*;


class SheetDeleter implements ActionListener {
	
	SpreadSheet toClean = null;
	File theFile = null;
	
	SheetDeleter(String pathToFile)
	{
		try {
			this.theFile=new File(pathToFile);
			this.toClean = SpreadSheet.createFromFile(this.theFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No file found to clean");
			e.printStackTrace();
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{		
		int sheetCount=toClean.getSheetCount();
		
		if(sheetCount>1)
		{
			for(int i=sheetCount-1; i>0; i--)
			{
				toClean.getSheet(i).detach();
			}
		}
		try {
			this.toClean.saveAs(this.theFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

}
