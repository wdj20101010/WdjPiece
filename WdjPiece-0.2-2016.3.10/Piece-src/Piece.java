import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class Piece {
	public static void main(String[] args) {
		new Piece(args);
	}
	public Piece(String[] args){
		String str="";int ch;
		try{
			if(args.length>0){
				FileReader fr=new FileReader(args[0]);
				while((ch=fr.read())!=-1)str=str+(char)ch;
				fr.close();
			}
			else{
				InputStream is=this.getClass().getResourceAsStream("bin/0000");
				InputStreamReader isr=new InputStreamReader(is);
				while((ch=isr.read())!=-1)str=str+(char)ch;
				is.close();isr.close();
			}	
		}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		new WdjOperate(str);
	}
}
