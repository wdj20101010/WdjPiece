import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Wdj1 {
	
	private DialogEnter dialog1;
	private DialogResult dialog2;
	private DataPool DP;
	private String ThisPiece;
	public String NextPiece="";
	private boolean IsProce;
	private Wdj2 wdj;
	private ArrayList<String> TempPiece;
	
	Wdj1(DataPool dp,String thispiece,boolean isproce){
		DP=dp;ThisPiece=thispiece;IsProce=isproce;
		wdj=new Wdj2(DP);TempPiece=new ArrayList<String>();
		String str="";
		//是过程
		if(IsProce)str=DP.getproce(ThisPiece);
		//是函数
		else{str=DP.getfunct(ThisPiece)[1];}
		//进行处理
		try{mainOperate(str);}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		//移除TempPiece
		for(int i=0;i<TempPiece.size();i++)DP.remove(TempPiece.get(i));
	}
	//流程处理
	//处理enter{},处理 result{},处理todo{}
	//处理entfrom(){},处理 resulto(){},处理if(){},处理  while(){}
	//处理等式语句
	private void mainOperate(String str)throws Exception{
		char ch;String str1="",str2,str3,str4;int 括号;
		if(str.length()>0)str=str+' ';
		for(int i=0;i<str.length();i++){
			if((ch=str.charAt(i))!=' ')str1=str1+ch;
		    //处理enter{},处理 result{},处理todo{}
			else if(str1.indexOf("enter{")==0||str1.indexOf("result{")==0||str1.indexOf("todo{")==0){
				if(str1.indexOf("enter{")==0)i=i-str1.length()+6;
				else if(str1.indexOf("result{")==0)i=i-str1.length()+7;
				else i=i-str1.length()+5;
				//读取{}中的语句
				括号=1;str2="";
				while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str2=str2+ch;}
		        if(括号!=0){
		        	if(str1.indexOf("enter{")==0)throw new Exception("\nenter{}语句错误\n"+str1);
		        	else if(str1.indexOf("result{")==0)throw new Exception("\nresult{}语句错误\n"+str1);
		        	else throw new Exception("\ntodo{}语句错误\n"+str1);
		        }
				//enter{}语句执行
		        if(str1.indexOf("enter{")==0){
		        	try{
				        dialog1=new DialogEnter(str2);dialog1.setVisible(true);
				        if(dialog1.输入内容.length()>0)mainOperate(pretreatOperate(dialog1.输入内容));
		        	}
		    		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		    		finally{dialog1.dispose();}
		        }
		        //result{}语句执行
		        else if(str1.indexOf("result{")==0){
			        String 结果="";str2=str2+' ';str3="";
			        for(int j=0;j<str2.length();j++)
			        	if((ch=str2.charAt(j))!=' ')str3=str3+ch;
			        	else{
			        		if(DP.gettype(str3)==null)throw new Exception("\nresult{}语句错误\n未知变量: "+str3);
			        		else if(DP.gettype(str3).equals("bool"))结果=结果+str3+"="+DP.getbool(str3)+"\n";
			        		else if(DP.gettype(str3).equals("digit"))结果=结果+str3+"="+DP.getdigit(str3)+"\n";
			        		else if(DP.gettype(str3).equals("str"))结果=结果+str3+"="+DP.getstr(str3)+"\n";
			        		str3="";
			        	}
			        dialog2=new DialogResult(结果);dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			        dialog2.setVisible(true);
		        }
		        //todo{}语句执行
		        else{
		        	if(!IsProce)throw new Exception("\ntodo{}语句错误  "+str1+"\ntodo{}语句只能用在proce{}中");
		        	NextPiece=str2;break;
		        }
		        str1="";
			}
		    //处理entfrom(){},处理 resulto(){},处理if(){},处理  while(){}
			else if(str1.indexOf("entfrom(")==0||str1.indexOf("resulto(")==0||str1.indexOf("if(")==0||str1.indexOf("while(")==0){
				if(str1.indexOf("entfrom(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("resulto(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("if(")==0)i=i-str1.length()+3;
				else i=i-str1.length()+6;
			    //读取()中语句和{}中语句
				括号=1;str2="";
			    while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str2=str2+ch;}
		        if(括号!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}语句错误\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}语句错误\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}语句错误\n"+str1);
		        	else throw new Exception("\nwhile(){}语句错误\n"+str1);
		        }
			    i++;括号=1;str3="";
			    while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
			    if(括号!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}语句错误\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}语句错误\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}语句错误\n"+str1);
		        	else throw new Exception("\nwhile(){}语句错误\n"+str1);
		        }
		        //entfrom(){}语句执行
		        if(str1.indexOf("entfrom(")==0){
		        	int chx;str3=str3+' ';str4="";String str5="",str6="";
		        	FileReader filein=new FileReader(str2);
		        	while((chx=filein.read())!=-1)str4=str4+(char)chx;filein.close();
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str5=str5+ch;
		        	    else{
				            for(int k=0;k<str4.length();k++)
				        	    if((ch=str4.charAt(k))!='\n')str6=str6+ch;
				        	    else{
				        	    	if(str6.substring(0,str6.indexOf("=")).equals(str5)){
				        	    		wdj.equateOperate(str5,str6.substring(str6.indexOf("=")+1));
				        	    	}
				        	    	str6="";
				        	    }
		        	    	str5="";
		        	    }
		        }
		        //resulto(){}语句执行
		        else if(str1.indexOf("resulto(")==0){
				    String 结果="";str3=str3+' ';str4="";
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str4=str4+ch;
		        	    else{
		        		    if(DP.gettype(str4)==null)throw new Exception("\nresulto{}语句错误\n未知变量: "+str4);
		        		    else if(DP.gettype(str4).equals("bool"))结果=结果+str4+"="+DP.getbool(str4)+"\n";
		        		    else if(DP.gettype(str4).equals("digit"))结果=结果+str4+"="+DP.getdigit(str4)+"\n";
		        		    else if(DP.gettype(str4).equals("str"))结果=结果+str4+"="+DP.getstr(str4)+"\n";
		        		    str4="";
		        	    }
		            FileWriter fileout=new FileWriter(str2);
	    		    fileout.write(结果);fileout.close();
		        }
    		    //if(){}语句执行
		        else if(str1.indexOf("if(")==0){
					DataBase XYZ=wdj.baseOperate(str2);
					if(XYZ.type!=2)throw new Exception("\nif(){}语句错误\n"+str1);
					if(XYZ.getbool())mainOperate(str3);
		        }
				//while(){}语句执行
		        else{
					DataBase XYZ=wdj.baseOperate(str2);
					if(XYZ.type!=2)throw new Exception("\nwhile(){}语句错误\n"+str1);
					while(XYZ.getbool()){mainOperate(str3);XYZ=wdj.baseOperate(str2);}
				}
		        str1="";
			}
		    //处理等式语句
			else if(str1.length()>0){
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("\n语句错误\n"+str1);
				else{
					str2=str1.substring(0, str1.indexOf('='));
					str3=str1.substring(str1.indexOf('=')+1);
					str1="";
					if(ThisPiece.length()>0){
						if(str2.equals("this"))str2=ThisPiece;
						else if(str2.indexOf("this.")==0)str2=ThisPiece+"."+str2.substring(5);
						if(str3.equals("this"))str3=ThisPiece;
						else if(str3.indexOf("this.")==0)str3=ThisPiece+"."+str3.substring(5);
					}
					if(!DP.ishave(str2))TempPiece.add(str2);
					wdj.equateOperate(str2,str3);
				}
			}
		}
	}
	//预先处理
	//代码规整化
	private String pretreatOperate(String str)throws Exception{
		if(str.length()==0)return "";
		//去除制表符
		str=" "+str.replace('\t', ' ')+" ";
		//处理enter{}语句
		int X=-1;
		int A=str.indexOf(" enter "),B=str.indexOf(" enter\n"),C=str.indexOf(" enter{");
		int D=str.indexOf("\nenter "),E=str.indexOf("\nenter\n"),F=str.indexOf("\nenter{");
		if(A>X)X=A;if(B>X)X=B;if(C>X)X=C;if(D>X)X=D;if(E>X)X=E;if(F>X)X=F;
		if(A!=-1&&A<X)X=A;if(B!=-1&&B<X)X=B;if(C!=-1&&C<X)X=C;
		if(D!=-1&&D<X)X=D;if(E!=-1&&E<X)X=E;if(F!=-1&&F<X)X=F;
		if(X!=-1){
			char ch;int Y=X;
			while(Y<str.length()&&(ch=str.charAt(Y++))!='{');
			if(Y==str.length())throw new Exception("\nenter{}语句错误\n"+str);
			int Z=Y,大括号=1;
			while(Z<str.length()&&大括号!=0){ch=str.charAt(Z++);if(ch=='{')大括号++;else if(ch=='}')大括号--;}
			if(大括号!=0)throw new Exception("\nenter{}语句错误\n"+str);
			String strx=str.substring(0, X);
			String stry=str.substring(Y, Z-1);
			String strz=str.substring(Z, str.length());
			return pretreatOperate(strx)+" enter{"+stry+"} "+pretreatOperate(strz);
		}
		//去除回车符和注释
		char ch;String str1="",str2="",str3="";
		if(str.length()>0)str=str+'\n';
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!='\n')str1=str1+ch;
			else{if(str1.indexOf("//")!=-1)str1=str1.substring(0, str1.indexOf("//"));
				 str2=str2+" "+str1;str1="";}
		//将连续空格变为单一空格
		while(str2.indexOf("  ")!=-1)str2=str2.substring(0, str2.indexOf("  "))+" "+str2.substring(str2.indexOf("  ")+2);
		if(str2.equals(" "))str2="";
		if(str2.length()>1&&str2.charAt(0)==' ')str2=str2.substring(1, str2.length());
        if(str2.length()>1&&str2.charAt(str2.length()-1)==' ')str2=str2.substring(0, str2.length()-1);
		//去除+ - * / ^ > < = & | ! . , ( [ {前后的空格  去除) ] }前的空格
		for(int i=0;i<str2.length();i++)
			if(i<str2.length()-1&&str2.charAt(i)==' '&&
			  (str2.charAt(i+1)=='+'||str2.charAt(i+1)=='-'||str2.charAt(i+1)=='*'||str2.charAt(i+1)=='/'||str2.charAt(i+1)=='^'||
			   str2.charAt(i+1)=='>'||str2.charAt(i+1)=='<'||str2.charAt(i+1)=='='||str2.charAt(i+1)=='&'||str2.charAt(i+1)=='|'||
			   str2.charAt(i+1)=='!'||str2.charAt(i+1)=='('||str2.charAt(i+1)==')'||str2.charAt(i+1)=='['||str2.charAt(i+1)==']'||
			   str2.charAt(i+1)=='{'||str2.charAt(i+1)=='}'||str2.charAt(i+1)=='.'||str2.charAt(i+1)==','));
			else if(i<str2.length()-1&&str2.charAt(i+1)==' '&&
				   (str2.charAt(i)=='+'||str2.charAt(i)=='-'||str2.charAt(i)=='*'||str2.charAt(i)=='/'||str2.charAt(i)=='^'||
				    str2.charAt(i)=='>'||str2.charAt(i)=='<'||str2.charAt(i)=='='||str2.charAt(i)=='&'||str2.charAt(i)=='|'||
				    str2.charAt(i)=='!'||str2.charAt(i)=='('||str2.charAt(i)=='['||str2.charAt(i)=='{'||
				    str2.charAt(i+1)=='.'||str2.charAt(i+1)==','))str3=str3+str2.charAt(i++);
			else str3=str3+str2.charAt(i);
		return str3;
	}
}
