import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class WdjPiece extends JFrame {

	static WdjPiece frame;
	static JTextPane textPane;
	public static Grammar grammar;
	private UserInputWords uiw=new UserInputWords();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new WdjPiece();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try{
			File file1=new File("bin/");
			File file2=new File("lang/");
			File file3=new File("res/");
			File file4=new File("src/");
			if(!file1.isDirectory())file1.mkdir();
			if(!file2.isDirectory())file2.mkdir();
			if(!file3.isDirectory())file3.mkdir();
			if(!file4.isDirectory())file4.mkdir();
		}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
	}

	public WdjPiece() {
		setTitle("WdjPiece");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		JPanel contentPane = new JPanel();
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
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("\u6253\u5305");
		mnNewMenu_1.add(mntmNewMenuItem_5);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Monospaced", Font.BOLD, 16));
		grammar=new Grammar(textPane.getStyledDocument());
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(textPane);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPopupMenu popupMenu = new JPopupMenu();
		textPane.setComponentPopupMenu(popupMenu);
		
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
		        chooser.setCurrentDirectory(new File("src/"));
		        int returnVal=chooser.showOpenDialog(null);
		        if(chooser.getSelectedFile()!=null&&returnVal==JFileChooser.APPROVE_OPTION)
		        	try{
		        		int ch;String str="";
		        		FileReader filein=new FileReader(chooser.getSelectedFile());
		        		while((ch=filein.read())!=-1)str=str+(char)ch;
		        		filein.close();textPane.setText(str);textPane.setCaretPosition(0);
		        	}
		            catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		    }
		});

		mntmNewMenuItem_1.addActionListener(new ActionListener() {  //保存
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(new FileNameExtensionFilter("piece", "piece"));
		        chooser.setCurrentDirectory(new File("src/"));
		        int returnVal=chooser.showSaveDialog(null);
		        if(chooser.getSelectedFile()!=null&&returnVal==JFileChooser.APPROVE_OPTION)
		        	try{
		        		String str=chooser.getSelectedFile().toString();
		        		if(str.lastIndexOf(".")==-1)str=str+".piece";
		        		FileWriter fileout=new FileWriter(str);
		        		fileout.write(textPane.getText());fileout.close();
		        	}
		            catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		    }
		});
		
		mntmNewMenuItem_2.addActionListener(new ActionListener() {  //清除
			public void actionPerformed(ActionEvent e) {textPane.setText("");}
		});
		
		mntmNewMenuItem_3.addActionListener(new ActionListener() {  //帮助
			public void actionPerformed(ActionEvent e) {
				DialogHelp dialog = new DialogHelp();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		
		mntmNewMenuItem_4.addActionListener(new ActionListener() {  //运算
			public void actionPerformed(ActionEvent e) {
				try{if(textPane.getText().length()>0){
					new PrePiece(textPane.getText().replace('\r', ' '));
	        		Runtime.getRuntime().exec("javaw -jar Piece.jar bin/0");
				}}
				catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
			}
		});
		
		mntmNewMenuItem_5.addActionListener(new ActionListener() {  //打包
			public void actionPerformed(ActionEvent e) {
				try{if(textPane.getText().length()>0){
					new PrePiece(textPane.getText());
					
					JarInputStream jis=new JarInputStream(new FileInputStream("Piece.jar"));
					Manifest mf=jis.getManifest();
					JarOutputStream jos=new JarOutputStream(new FileOutputStream("NewPiece.jar"),mf);
					JarEntry je;int ch,x;String str="";
					while((je=jis.getNextJarEntry())!=null){
						jos.putNextEntry(je);
						while((ch=jis.read())!=-1)jos.write(ch);
					}
					
					FileReader filein=new FileReader("bin/0");
		            while((ch=filein.read())!=-1)str=str+(char)ch;filein.close();
		            x=Integer.valueOf(str);
		            jos.putNextEntry(new JarEntry("bin/0"));
		            jos.write(str.getBytes());str="";
		            for(int i=1;i<=x;i++){
		            	filein=new FileReader("bin/"+i);
			            while((ch=filein.read())!=-1)str=str+(char)ch;filein.close();
			            jos.putNextEntry(new JarEntry("bin/"+i));
			            jos.write(str.getBytes());str="";
		            }
					jis.close();jos.close();
					JOptionPane.showMessageDialog(null,"打包完成   生成可执行文件NewPiece.jar");
				}}
				catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
			}
		});
		
		mntmNewMenuItem_6.addActionListener(new ActionListener() {  //剪切
			public void actionPerformed(ActionEvent e) {textPane.cut();}
		});
		
		mntmNewMenuItem_7.addActionListener(new ActionListener() {  //复制
			public void actionPerformed(ActionEvent e) {textPane.copy();}
		});
		
		mntmNewMenuItem_8.addActionListener(new ActionListener() {  //粘贴
			public void actionPerformed(ActionEvent e) {textPane.paste();}
		});

		textPane.addCaretListener(new CaretListener() {  //语法提示
			public void caretUpdate(CaretEvent e) {uiw.setVisible(false);grammar.run(textPane.getCaretPosition());}
		});
		
		textPane.addKeyListener(new KeyListener() {  //语法提示
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				int X=0,Y=0;
				try {
					X=(int) (frame.getLocation().x+15+textPane.modelToView2D(textPane.getCaretPosition()).getCenterX());
					Y=(int) (frame.getLocation().y+55+textPane.modelToView2D(textPane.getCaretPosition()).getCenterY());
				}catch(Exception ex) {}
				
				uiw.setVisible(false);
				uiw=new UserInputWords();
				if(e.getKeyChar()>='0'&&e.getKeyChar()<='9'||e.getKeyChar()>='A'&&e.getKeyChar()<='Z'||e.getKeyChar()>='a'&&e.getKeyChar()<='z') {
					String KeySymbols=" \n\r\t\b+-*/^><=&|!.,\"()[]{}",str=null;
					try{str=textPane.getText(0, textPane.getCaretPosition());}catch(Exception ex) {}
					int i=str.length();while(i-1>-1&&KeySymbols.indexOf(str.charAt(i-1))==-1)i--;
					uiw.run(X,Y,str.substring(i));
					uiw.setVisible(true);
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
}
