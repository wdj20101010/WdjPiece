import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DialogEnter extends JFrame {

	String 输入内容="";
	DialogEnterComponent DIALOG;

	public DialogEnter(String str) {
		
		setExtendedState(JFrame.ICONIFIED);setResizable(false);setVisible(true);
		
		DIALOG=new DialogEnterComponent(str);输入内容=DIALOG.输入内容;
		
		if(输入内容.equals(""))System.exit(0);
		else {DIALOG.dispose();dispose();}
	}
}
