import java.awt.event.*;

import javax.swing.JButton;


class ButtonDisabler implements ActionListener {
	
	private JButton button = null;
	private boolean isEnable = true;
	
	ButtonDisabler(JButton button, boolean isEnable)
	{
		this.button=button;
		this.isEnable=isEnable;
	}

	public void actionPerformed(ActionEvent e)
	{
		this.button.setEnabled(isEnable);
	}
}
