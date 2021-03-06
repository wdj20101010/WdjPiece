import java.io.FileReader;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Font;

@SuppressWarnings("serial")
public class DialogHelp extends JDialog {

	public DialogHelp() {
		setTitle("\u5E2E\u52A9");
		setBounds(100, 100, 640, 480);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);

		try{
            int ch;String str="";
            FileReader filein=new FileReader("lang/WdjPieceLang.txt");
            while((ch=filein.read())!=-1)str=str+(char)ch;
            filein.close();textArea.setText(str);textArea.setCaretPosition(0);
            }
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
	}

}
