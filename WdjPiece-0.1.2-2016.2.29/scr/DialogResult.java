import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DialogResult extends JDialog {

	/**
	 * Create the dialog.
	 */
	public DialogResult(String str) {
		setTitle("\u7ED3\u679C\u8FD4\u56DE");
		setBounds(100, 100, 640, 480);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setText(str);
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);
		
		JPopupMenu popupMenu = new JPopupMenu();
        addPopup(textArea, popupMenu);
        JMenuItem menuItem = new JMenuItem("\u590D\u5236");
        menuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		textArea.copy();
        	}
        });
        popupMenu.add(menuItem);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
