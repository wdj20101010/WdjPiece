import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class Piece {
	
	private DataPool DP=new DataPool();
	private String NextPiece=null;
	
	public static void main(String[] args) {
		new Piece(args);
	}
	//读取源码
	public Piece(String[] args){
		String str="";int ch,x;
		try{
			if(args.length>0){
				FileReader fr=new FileReader("bin/0");
				while((ch=fr.read())!=-1)str=str+(char)ch;fr.close();
				x=Integer.valueOf(str);str="";
				for(int i=1;i<=x;i++){
	            	fr=new FileReader("bin/"+i);
		            while((ch=fr.read())!=-1)str=str+(char)ch;fr.close();
		    		pieceOperate("",str);str="";
				}
			}
			else{
				InputStream is=this.getClass().getResourceAsStream("bin/0");
				InputStreamReader isr=new InputStreamReader(is);
				while((ch=isr.read())!=-1)str=str+(char)ch;is.close();isr.close();
				x=Integer.valueOf(str);str="";
				for(int i=1;i<=x;i++){
					is=this.getClass().getResourceAsStream("bin/"+i);
					isr=new InputStreamReader(is);
					while((ch=isr.read())!=-1)str=str+(char)ch;is.close();isr.close();
		    		pieceOperate("",str);str="";
				}
			}	
		}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		while(NextPiece.length()>0)NextPiece=proceOperate(NextPiece);
	}
	//片段处理
	private void pieceOperate(String thispiece,String str)throws Exception{
		char ch;String str1="",str2="",str3="";int 括号;
		if(str.length()>0)str=str+' ';
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!=' ')str1=str1+ch;
			else if(str1.length()>0)
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("\n语句错误\n"+str1);
				else{
					str2=str1.substring(0, str1.indexOf('='));str3=str1.substring(str1.indexOf('=')+1);str1="";
					//str2的this super index()处理
					if(thispiece.length()>0&&!str2.equals("this")&&str2.indexOf("this.")!=0
							&&!str2.equals("super")&&str2.indexOf("super.")!=0)str2=thispiece+"."+str2;
					Wdj2 wdj=new Wdj2(DP,thispiece);str2=wdj.thisSuperIndex(str2);
					//是片段
					if(str3.indexOf("piece{")==0){
						i=i-str3.length()+6;str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\npiece{}语句错误\n"+str);
						pieceOperate(str2,str3);
					}
					//是过程
					else if(str3.indexOf("proce{")==0){
						i=i-str3.length()+6;str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nproce{}语句错误\n"+str);
						DP.setProce(str2, str3);
						if(str2.equals("main")||str2.length()>5&&str2.substring(str2.length()-5).equals(".main"))NextPiece=str2;
					}
					//是函数
					else if(str3.indexOf("funct(")==0){
						i=i-str3.length()+6;str3="";括号=1;
						String str4[]={"",""};
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nfunct(){}语句错误\n"+str);
						str4[0]=str3;
						while(i<str.length()&&(ch=str.charAt(i++))!='{');if(i==str.length())throw new Exception("\nfunct(){}语句错误\n"+str);
						str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nfunct(){}语句错误\n"+str);
						str4[1]=str3;
						DP.setFunct(str2, str4);
					}
					//是等式
					else{
						//双引号" "处理
						if(str3.indexOf("\"")!=-1&&str3.indexOf("\"")==str3.lastIndexOf("\"")){
							while(i<str.length()&&(ch=str.charAt(i))!='\"'){str3=str3+ch;i++;}
							str3=str3+"\"";
						}
						wdj.equateOperate(str2, str3);
					}
				}
	}
	//过程处理
	private String proceOperate(String thispiece){
		new Wdj1(DP,thispiece,true);
		return DP.Run();
	}
}
