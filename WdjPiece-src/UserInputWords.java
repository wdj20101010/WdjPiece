import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class UserInputWords extends JWindow {
	private JList<Object> list;
	public UserInputWords() {}
	public void run(int x,int y,String str) {
		list=new JList<Object>(WdjPiece.grammar.getUserWords(str));
		getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		if(list.getModel().getSize()>0)setBounds(x,y,100,100);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String str1=list.getSelectedValue().toString().substring(str.length())+" ";
				WdjPiece.textPane.replaceSelection(str1);
				WdjPiece.textPane.select(WdjPiece.textPane.getCaretPosition()-str1.length(), WdjPiece.textPane.getCaretPosition());
			}
		});
	}
}
