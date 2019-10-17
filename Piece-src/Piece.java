import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Piece {	
	public static DataPool DP=new DataPool();
	private String MainProce;
	
	public static void main(String[] args) {
		new Piece(args);
	}
	
	public Piece(String[] args){
		//读取源码
		String str="";int ch,x;
		try{
			if(args.length>0){
				FileReader fr=new FileReader("bin/0");
				while((ch=fr.read())!=-1)str=str+(char)ch;fr.close();
				x=Integer.valueOf(str);str="";
				for(int i=1;i<=x;i++){
	            	fr=new FileReader("bin/"+i);
		            while((ch=fr.read())!=-1)str=str+(char)ch;fr.close();
		            Init("",str);str="";
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
					Init("",str);str="";
				}
			}	
		}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		//执行main过程
		new RunProce(MainProce);
		//整体运行
		Run();
	}
	
	//装载代码
	private void Init(String thispiece,String str)throws Exception{
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
					str2=RunCodeToos.thisSuperIndex(str2,thispiece);
					//是piece
					if(str3.indexOf("piece{")==0){
						i=i-str3.length()+6;str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\npiece{}语句错误\n"+str);
						Init(str2,str3);
					}
					//是proce
					else if(str3.indexOf("proce(")==0){
						i=i-str3.length()+6;str3="";括号=1;
						String str4[]={"",""};
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nproce(){}语句错误\n"+str);
						str4[0]=str3;
						while(i<str.length()&&(ch=str.charAt(i++))!='{');if(i==str.length())throw new Exception("\nproce(){}语句错误\n"+str);
						str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nproce(){}语句错误\n"+str);
						str4[1]=str3;
						DP.setProce(str2, str4);
						if(str2.equals("main")||str2.length()>5&&str2.substring(str2.length()-5).equals(".main"))MainProce=str2;
					}
					//是newproce
					else if(str3.indexOf("newproce(")==0){
						i=i-str3.length()+9;str3="";括号=1;
						String str4[]={"",""};
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nnewproce(){}语句错误\n"+str);
						str4[0]=str3;
						while(i<str.length()&&(ch=str.charAt(i++))!='{');if(i==str.length())throw new Exception("\nnewproce(){}语句错误\n"+str);
						str3="";括号=1;
						while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
						if(括号!=0)throw new Exception("\nnewproce(){}语句错误\n"+str);
						str4[1]=str3;
						DP.setNewProce(str2, str4);
					}
					//是funct
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
						RunCodeToos.equate(str2,str3,thispiece);
					}
				}
	}
	
	//整体运行
	private void Run() {
		ArrayList<String> Proce=new ArrayList<String>();
		ArrayList<String> NewProce=new ArrayList<String>();
		for(int i=0;i<DP.Name.size();i++) {
			if(DP.getProce(DP.Name.get(i))!=null&&
			  (!DP.Name.get(i).equals("main")||DP.Name.get(i).length()>5&&DP.Name.get(i).lastIndexOf(".main")!=DP.Name.get(i).length()-5))
				Proce.add(DP.Name.get(i));
			if(DP.getNewProce(DP.Name.get(i))!=null&&
			  (!DP.Name.get(i).equals("main")||DP.Name.get(i).length()>5&&DP.Name.get(i).lastIndexOf(".main")!=DP.Name.get(i).length()-5))
				NewProce.add(DP.Name.get(i));
		}
		int ProceCount,NewProceCount,i,j;
		do {ProceCount=0;NewProceCount=0;i=0;j=0;
			try {
				while(i<Proce.size())
					if(RunCodeToos.base(DP.getProce(Proce.get(i))[0], Proce.get(i)).getbool()) {
						ProceCount++;new RunProce(Proce.get(i));Proce.remove(i);
					}else i++;
				while(j<NewProce.size())
					if(RunCodeToos.base(DP.getNewProce(NewProce.get(j))[0], NewProce.get(j)).getbool()) {
						NewProceCount++;new RunProceThread(NewProce.get(j));NewProce.remove(j);
					}else j++;
			}
		    catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		}while(ProceCount>0||NewProceCount>0);
	}
}
