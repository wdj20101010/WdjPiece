import javax.swing.JOptionPane;

public class RunProce {
	public RunProce(String proce) {
		String str1=Piece.DP.getProce(proce)[0];
		String str2=Piece.DP.getProce(proce)[1];
		if(str1!=null)
			try {
				DataPool.DataBase XYZ=RunCodeToos.base(str1, proce);
				if(XYZ.getbool()&&str2!=null)new RunCode(proce,str2,false);
			}
		    catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
	}
}
