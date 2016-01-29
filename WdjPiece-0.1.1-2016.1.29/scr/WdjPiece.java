import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WdjPiece extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WdjPiece frame = new WdjPiece();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WdjPiece() {
		setTitle("WdjPiece");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("\u6587\u4EF6");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("\u6253\u5F00");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("\u4FDD\u5B58");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("\u6E05\u9664");
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenu mnNewMenu_1 = new JMenu("\u8FD0\u884C");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("\u5E2E\u52A9");
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("\u8FD0\u7B97");
		mnNewMenu_1.add(mntmNewMenuItem_4);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("\u7F16\u8BD1");
		mnNewMenu_1.add(mntmNewMenuItem_5);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);
		
		JPopupMenu popupMenu = new JPopupMenu();
		textArea.setComponentPopupMenu(popupMenu);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("\u526A\u8D34");
		popupMenu.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("\u590D\u5236");
		popupMenu.add(mntmNewMenuItem_7);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("\u7C98\u8D34");
		popupMenu.add(mntmNewMenuItem_8);
		
		mntmNewMenuItem.addActionListener(new ActionListener() {  //打开
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(new FileNameExtensionFilter("piece", "piece"));
		        chooser.setCurrentDirectory(new File("."));
		        int returnVal=chooser.showOpenDialog(null);
		        if(chooser.getSelectedFile()!=null&&returnVal==JFileChooser.APPROVE_OPTION)
		        	try{
		        		int ch;String str="";
		        		FileReader filein=new FileReader(chooser.getSelectedFile());
		        		while((ch=filein.read())!=-1)str=str+(char)ch;
		        		filein.close();textArea.setText(str);textArea.setCaretPosition(0);
		        		}
		        catch(Exception ex){new JOptionPane(ex.getMessage()).createDialog(null).show();}
		        }
		});

		mntmNewMenuItem_1.addActionListener(new ActionListener() {  //保存
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(new FileNameExtensionFilter("piece", "piece"));
		        chooser.setCurrentDirectory(new File("."));
		        int returnVal=chooser.showSaveDialog(null);
		        if(chooser.getSelectedFile()!=null&&returnVal==JFileChooser.APPROVE_OPTION)
		        	try{
		        		String str=chooser.getSelectedFile().toString();
		        		if(str.lastIndexOf(".")==-1)str=str+".piece";
		        		FileWriter fileout=new FileWriter(str);
		        		fileout.write(textArea.getText());fileout.close();
		        		}
		        catch(Exception ex){new JOptionPane(ex.getMessage()).createDialog(null).show();}
		        }
		});
		
		mntmNewMenuItem_2.addActionListener(new ActionListener() {  //清除
			public void actionPerformed(ActionEvent e) {textArea.setText("");}
		});
		
		mntmNewMenuItem_3.addActionListener(new ActionListener() {  //帮助
			public void actionPerformed(ActionEvent e) {
				DialogHelp dialog = new DialogHelp();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		
		mntmNewMenuItem_4.addActionListener(new ActionListener() {  //计算
			public void actionPerformed(ActionEvent e) {
				try{
					WdjOperate wdj=new WdjOperate(textArea.getText());
				}
				catch(Exception ex){new JOptionPane(ex.getMessage()).createDialog(null).show();}
			}
		});
		
		mntmNewMenuItem_5.addActionListener(new ActionListener() {  //编译
			public void actionPerformed(ActionEvent e) {
				//
				//...
				//
			}
		});
		
		mntmNewMenuItem_6.addActionListener(new ActionListener() {  //剪切
			public void actionPerformed(ActionEvent e) {textArea.cut();}
		});
		
		mntmNewMenuItem_7.addActionListener(new ActionListener() {  //复制
			public void actionPerformed(ActionEvent e) {textArea.copy();}
		});
		
		mntmNewMenuItem_8.addActionListener(new ActionListener() {  //粘贴
			public void actionPerformed(ActionEvent e) {textArea.paste();}
		});
	}

}
