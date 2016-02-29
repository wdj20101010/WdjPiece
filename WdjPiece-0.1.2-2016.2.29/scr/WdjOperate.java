import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

//运算包
public class WdjOperate {
	
	//变量池
	DataPool DP=new DataPool();
	
	//构造函数,总的运算处理过程
	WdjOperate(String str){
		try{mainOperate(pretreatOperate(str));}
		catch(Exception ex){new JOptionPane(ex.getMessage()).createDialog(null).setVisible(true);}
	}
	
	//--------------------------------------------------
	//代码预先处理，代码规整化
	String pretreatOperate(String str)throws Exception{
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
			if(Y==str.length())throw new Exception("enter{}语句错误");
			int Z=Y,大括号=1;
			while(Z<str.length()&&大括号!=0){ch=str.charAt(Z++);if(ch=='{')大括号++;else if(ch=='}')大括号--;}
			if(大括号!=0)throw new Exception("enter{}语句错误");
			String strx=str.substring(0, X);
			String stry=str.substring(Y, Z-1);
			String strz=str.substring(Z, str.length());
			return pretreatOperate(strx)+"enter{"+stry+"} "+pretreatOperate(strz);
		}
		//去除回车符和注释
		char ch;String str1="",str2="",str3="";
		if(str.length()>0)str=str+'\n';
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!='\n')str1=str1+ch;
			else{if(str1.indexOf("//")!=-1)str1=str1.substring(0, str1.indexOf("//"));
				 str2=str2+" "+str1;str1="";}
		//将连续空格变为单一空格
		while(str2.length()>1&&str2.charAt(0)==' ')str2=str2.substring(1, str2.length());
        while(str2.length()>1&&str2.charAt(str2.length()-1)==' ')str2=str2.substring(0, str2.length()-1);
		while(str2.indexOf("  ")!=-1)str2=str2.substring(0, str2.indexOf("  "))+" "+str2.substring(str2.indexOf("  ")+2,str2.length());
        if(str2.equals(" "))str2="";
		//去除+ - * / ^ > < = & | ! ( [ {前后的空格,去除) ] }前的空格
		for(int i=0;i<str2.length();i++)
			if(i<str2.length()-1&&str2.charAt(i)==' '&&
			  (str2.charAt(i+1)=='+'||str2.charAt(i+1)=='-'||str2.charAt(i+1)=='*'||str2.charAt(i+1)=='/'||str2.charAt(i+1)=='^'||
			   str2.charAt(i+1)=='>'||str2.charAt(i+1)=='<'||str2.charAt(i+1)=='='||str2.charAt(i+1)=='&'||str2.charAt(i+1)=='|'||
			   str2.charAt(i+1)=='!'||str2.charAt(i+1)=='('||str2.charAt(i+1)==')'||str2.charAt(i+1)=='['||str2.charAt(i+1)==']'||
			   str2.charAt(i+1)=='{'||str2.charAt(i+1)=='}'));
			else if(i<str2.length()-1&&str2.charAt(i+1)==' '&&
				   (str2.charAt(i)=='+'||str2.charAt(i)=='-'||str2.charAt(i)=='*'||str2.charAt(i)=='/'||str2.charAt(i)=='^'||
				    str2.charAt(i)=='>'||str2.charAt(i)=='<'||str2.charAt(i)=='='||str2.charAt(i)=='&'||str2.charAt(i)=='|'||
				    str2.charAt(i)=='!'||str2.charAt(i)=='('||str2.charAt(i)=='['||str2.charAt(i)=='{'))str3=str3+str2.charAt(i++);
			else str3=str3+str2.charAt(i);
		return str3;
	}
	
	//--------------------------------------------------
	//代码流程处理,处理  if(){},处理  while(){},处理enter{},处理  result{},处理  变量=算式.
	void mainOperate(String str)throws Exception{
		char ch;String str1="";
		if(str.length()>0)str=str+' ';
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!=' ')str1=str1+ch;
		    //处理 if(){}语句
			else if(str1.indexOf("if(")==0){
				String strx="",stry="";int x=1,y=1;
				i=i-str1.length()+3;str1="";
				//读取()中语句和{}中语句
				while(i<str.length()&&x!=0){ch=str.charAt(i++);if(ch=='(')x++;else if(ch==')')x--;if(x!=0)strx=strx+ch;}
				i++;
				while(i<str.length()&&y!=0){ch=str.charAt(i++);if(ch=='{')y++;else if(ch=='}')y--;if(y!=0)stry=stry+ch;}
	        	if(x!=0||y!=0||strx.length()==0||stry.length()==0)throw new Exception("if(){}语句错误");
	        	//if(){}语句执行
				DataBase XYZ=baseOperate(strx);
				if(XYZ.type!=2)throw new Exception("if(){}语句错误");
				if(XYZ.getbool())mainOperate(stry);
			}
	        //处理 while(){}语句
			else if(str1.indexOf("while(")==0){
				String strx="",stry="";int x=1,y=1;
				i=i-str1.length()+6;str1="";
				//读取()中语句和{}中语句
				while(i<str.length()&&x!=0){ch=str.charAt(i++);if(ch=='(')x++;else if(ch==')')x--;if(x!=0)strx=strx+ch;}
				i++;
				while(i<str.length()&&y!=0){ch=str.charAt(i++);if(ch=='{')y++;else if(ch=='}')y--;if(y!=0)stry=stry+ch;}
				if(x!=0||y!=0||strx.length()==0||stry.length()==0)throw new Exception("while(){}语句错误");
				//while(){}语句执行
				DataBase XYZ=baseOperate(strx);
				if(XYZ.type!=2)throw new Exception("while(){}语句错误");
				while(XYZ.getbool()){mainOperate(stry);XYZ=baseOperate(strx);}
			}
		    //处理enter{}语句
			else if(str1.indexOf("enter{")==0){
				String strx="";int x=1;
				i=i-str1.length()+6;str1="";
				//读取{}中的语句
				while(i<str.length()&&x!=0){ch=str.charAt(i++);if(ch=='{')x++;else if(ch=='}')x--;if(x!=0)strx=strx+ch;}
		        if(x!=0||strx.length()==0)throw new Exception("enter{}语句错误");
		        //enter{}语句执行
		        DialogEnter dialog=new DialogEnter(strx);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		        dialog.setVisible(true);
		        if(dialog.输入内容.length()>0)mainOperate(pretreatOperate(dialog.输入内容));
			}
		    //处理result{}语句
			else if(str1.indexOf("result{")==0){
				String strx="";int x=1;
				i=i-str1.length()+7;str1="";
				//读取{}中的语句
				while(i<str.length()&&x!=0){ch=str.charAt(i++);if(ch=='{')x++;else if(ch=='}')x--;if(x!=0)strx=strx+ch;}
		        if(x!=0||strx.length()==0)throw new Exception("result{}语句错误");
		        //result{}语句执行
		        String 结果="###RESULT###";
		        strx=strx+' ';String stry="";
		        for(int j=0;j<strx.length();j++)
		        	if((ch=strx.charAt(j))!=' ')stry=stry+ch;
		        	else{
		        		if(DP.gettype(stry)==null)throw new Exception("result{}语句错误\n未知变量: "+stry);
		        		else if(DP.gettype(stry).equals("bool"))结果=结果+"\n"+stry+"="+DP.getbool(stry);
		        		else if(DP.gettype(stry).equals("digit"))结果=结果+"\n"+stry+"="+DP.getdigit(stry);
		        		else if(DP.gettype(stry).equals("str"))结果=结果+"\n"+stry+"="+DP.getstr(stry);
		        		stry="";
		        	}
		        DialogResult dialog=new DialogResult(结果);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		        dialog.setVisible(true);
			}
		    //处理  变量=算式  语句
			else{
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("语句错误\n"+str1);
				else{
					String strx=str1.substring(0, str1.indexOf('='));
					String stry=str1.substring(str1.indexOf('=')+1, str1.length());
					//变量=变量
					if(DP.gettype(stry)!=null){
						DP.setbool(strx, DP.getbool(stry));
						DP.setdigit(strx, DP.getdigit(stry));
						DP.setstr(strx, DP.getstr(stry));
						if(DP.gettype(stry)=="bool")DP.DPtype.put(strx, "bool");
						else if(DP.gettype(stry)=="digit")DP.DPtype.put(strx, "digit");
						else if(DP.gettype(stry)=="str")DP.DPtype.put(strx, "str");
					}
					//变量=布尔
					else if(stry.equals("true"))DP.setbool(strx, true);
					else if(stry.equals("false"))DP.setbool(strx, false);
					//变量=数字
					else try{DP.setdigit(strx, Double.valueOf(stry));}
					catch(Exception ex){
						//变量=算式
						DataBase XYZ=new DataBase();
						XYZ=baseOperate(stry);
						if(XYZ.type==2)DP.setbool(strx, XYZ.getbool());
						else if(XYZ.type==1)DP.setdigit(strx, XYZ.getdigit());
						else if(XYZ.type==0)DP.setstr(strx, XYZ.getstr());
					}
				}
		        str1="";
			}
	}
	
	//--------------------------------------------------
	//基本运算处理,运算含有变量的单个算式字符串，运算类型包括数学、比较、布尔和括号
	//+ - * / ^ sin cos tan asin acos atan ln    > < == >= <=    & | !    ( )
	//其中==运算，可用于布尔和字符串类型
	//优先级12    运算符错误,比如单个=的出现
	//优先级11 (
	//优先级10 sin cos tan asin acos atan ln
	//优先级9  ^
	//优先级8  * /
	//优先级7  + -
	//优先级6  > < == >= <=
	//优先级5  !
	//优先级4  & |
	//优先级3  )
	//0 1 2  用于表示  字符串  数字  和  布尔
	DataBase baseOperate(String str)throws Exception{
		String str1="",str2="";int 大括号数=0,中括号数=0,小括号数=0;
		ArrayList<DataBase> 算式=new ArrayList<DataBase>();
		ArrayList<DataBase> 数据栈=new ArrayList<DataBase>();
		ArrayList<DataBase> 符号栈=new ArrayList<DataBase>();
		if(str.length()>0)str1="("+str+")";
		//算式
		for(int i=0;i<str1.length();i++)
			if(str1.charAt(i)!='+'&&str1.charAt(i)!='-'&&str1.charAt(i)!='*'&&str1.charAt(i)!='/'&&str1.charAt(i)!='^'&&
			   str1.charAt(i)!='>'&&str1.charAt(i)!='<'&&str1.charAt(i)!='='&&str1.charAt(i)!='&'&&str1.charAt(i)!='|'&&
			   str1.charAt(i)!='!'&&str1.charAt(i)!='('&&str1.charAt(i)!=')'&&str1.charAt(i)!='['&&str1.charAt(i)!=']'&&
			   str1.charAt(i)!='{'&&str1.charAt(i)!='}')str2=str2+str1.charAt(i);
			else{
				if(str2.length()>0){DataBase X=new DataBase();X.setstr(str2);算式.add(X);str2="";}
				
				DataBase Y=new DataBase();
				     if(str1.charAt(i)=='{'){大括号数++;Y.setstr("(");}
				else if(str1.charAt(i)=='['){中括号数++;Y.setstr("(");}
				else if(str1.charAt(i)=='('){小括号数++;Y.setstr("(");}
				else if(str1.charAt(i)=='}'){大括号数--;Y.setstr(")");}
				else if(str1.charAt(i)==']'){中括号数--;Y.setstr(")");}
				else if(str1.charAt(i)==')'){小括号数--;Y.setstr(")");}
				else Y.setstr(String.valueOf(str1.charAt(i)));
				算式.add(Y);
				
				if(i<str1.length()-1&&
				  (str1.charAt(i)=='('||str1.charAt(i)=='['||str1.charAt(i)=='{'||
				   str1.charAt(i)=='>'||str1.charAt(i)=='<'||str1.charAt(i)=='=')&&
				  (str1.charAt(i+1)=='+'||str1.charAt(i+1)=='-')){DataBase Z=new DataBase();Z.setstr("0");算式.add(Z);}
			}
		if(大括号数!=0)throw new Exception("大括号{}错误\n"+str);
		if(中括号数!=0)throw new Exception("中括号[]错误\n"+str);
		if(小括号数!=0)throw new Exception("小括号()错误\n"+str);
		
		//识别
		for(int i=0;i<算式.size();i++){
			str2=算式.get(i).getstr();
			//识别运算符和函数
			if(str2.equals("("))算式.get(i).type=11;
			else if(str2.equals("sin")||str2.equals("cos")||str2.equals("tan")||str2.equals("asin")||
					str2.equals("acos")||str2.equals("atan")||str2.equals("ln"))算式.get(i).type=10;
			else if(str2.equals("^"))算式.get(i).type=9;
			else if(str2.equals("*")||str2.equals("/"))算式.get(i).type=8;
			else if(str2.equals("+")||str2.equals("-"))算式.get(i).type=7;
			else if(i<算式.size()-1&&(str2.equals(">")||str2.equals("<")||str2.equals("="))&&
					算式.get(i+1).getstr().equals("=")){算式.get(i).setstr(str2+"=");算式.remove(i+1);算式.get(i).type=6;}
			else if(str2.equals(">")||str2.equals("<"))算式.get(i).type=6;
			else if(str2.equals("!"))算式.get(i).type=5;
			else if(str2.equals("&")||str2.equals("|"))算式.get(i).type=4;
			else if(str2.equals(")"))算式.get(i).type=3;
			else if(str2.equals("="))算式.get(i).type=12;//单个=的出现,是运算符错误
		    //识别布尔类型
		    else if(str2.equals("true"))算式.get(i).setbool(true);
		    else if(str2.equals("false"))算式.get(i).setbool(false);
			else{
				//识别数字
				try{算式.get(i).setdigit(Double.valueOf(str2));}
				//识别变量
				catch(Exception ex){
					if(DP.gettype(str2)!=null){
						if(DP.getbool(str2)!=null)算式.get(i).setbool(DP.getbool(str2));
						if(DP.getdigit(str2)!=null)算式.get(i).setdigit(DP.getdigit(str2));
						if(DP.getstr(str2)!=null)算式.get(i).setstr(DP.getstr(str2));
						
						if(DP.gettype(str2).equals("bool"))算式.get(i).type=2;
		        		else if(DP.gettype(str2).equals("digit"))算式.get(i).type=1;
		        		else if(DP.gettype(str2).equals("str"))算式.get(i).type=0;
					}
				}
			}
		}
		
		//运算
		for(int i=0;i<算式.size();i++){
			//数据进栈
			if(算式.get(i).type<3)数据栈.add(算式.get(i));
			else do{
				//运算符和函数进栈
				if(符号栈.size()==0||符号栈.size()>0&&算式.get(i).type>符号栈.get(符号栈.size()-1).type){
					符号栈.add(算式.get(i));
					if(符号栈.get(符号栈.size()-1).getstr().equals("("))符号栈.get(符号栈.size()-1).type=3;
					break;
				}
				//出栈运算
				else{
					str1=符号栈.get(符号栈.size()-1).getstr();
					if(str1.equals("+")){
						数据栈.get(数据栈.size()-2).setdigit(数据栈.get(数据栈.size()-2).getdigit()+数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("-")){
						数据栈.get(数据栈.size()-2).setdigit(数据栈.get(数据栈.size()-2).getdigit()-数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("*")){
						数据栈.get(数据栈.size()-2).setdigit(数据栈.get(数据栈.size()-2).getdigit()*数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("/")){
						数据栈.get(数据栈.size()-2).setdigit(数据栈.get(数据栈.size()-2).getdigit()/数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("^")){
						数据栈.get(数据栈.size()-2).setdigit(Math.pow(数据栈.get(数据栈.size()-2).getdigit(),数据栈.get(数据栈.size()-1).getdigit()));
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("sin")){
						数据栈.get(数据栈.size()-1).setdigit(Math.sin(数据栈.get(数据栈.size()-1).getdigit()/180*Math.PI));
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("cos")){
						数据栈.get(数据栈.size()-1).setdigit(Math.cos(数据栈.get(数据栈.size()-1).getdigit()/180*Math.PI));
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("tan")){
						数据栈.get(数据栈.size()-1).setdigit(Math.tan(数据栈.get(数据栈.size()-1).getdigit()/180*Math.PI));
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("asin")){
						数据栈.get(数据栈.size()-1).setdigit(Math.asin(数据栈.get(数据栈.size()-1).getdigit())/Math.PI*180);
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("acos")){
						数据栈.get(数据栈.size()-1).setdigit(Math.acos(数据栈.get(数据栈.size()-1).getdigit())/Math.PI*180);
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("atan")){
						数据栈.get(数据栈.size()-1).setdigit(Math.atan(数据栈.get(数据栈.size()-1).getdigit())/Math.PI*180);
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("ln")){
						数据栈.get(数据栈.size()-1).setdigit(Math.log(数据栈.get(数据栈.size()-1).getdigit()));
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals(">")){
						if(数据栈.get(数据栈.size()-2).getdigit()>数据栈.get(数据栈.size()-1).getdigit())数据栈.get(数据栈.size()-2).setbool(true);
						else 数据栈.get(数据栈.size()-2).setbool(false);
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("<")){
						if(数据栈.get(数据栈.size()-2).getdigit()<数据栈.get(数据栈.size()-1).getdigit())数据栈.get(数据栈.size()-2).setbool(true);
						else 数据栈.get(数据栈.size()-2).setbool(false);
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("==")){
						if(数据栈.get(数据栈.size()-2).type==0)
							数据栈.get(数据栈.size()-2).setbool(数据栈.get(数据栈.size()-2).getstr().equals(数据栈.get(数据栈.size()-1).getstr()));
						else if(数据栈.get(数据栈.size()-2).type==1)
							数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()==(double)数据栈.get(数据栈.size()-1).getdigit());
						else if(数据栈.get(数据栈.size()-2).type==2)
							数据栈.get(数据栈.size()-2).setbool(数据栈.get(数据栈.size()-2).getbool()==数据栈.get(数据栈.size()-1).getbool());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals(">=")){
						if(数据栈.get(数据栈.size()-2).getdigit()>=数据栈.get(数据栈.size()-1).getdigit())数据栈.get(数据栈.size()-2).setbool(true);
						else 数据栈.get(数据栈.size()-2).setbool(false);
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("<=")){
						if(数据栈.get(数据栈.size()-2).getdigit()<=数据栈.get(数据栈.size()-1).getdigit())数据栈.get(数据栈.size()-2).setbool(true);
						else 数据栈.get(数据栈.size()-2).setbool(false);
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("&")){
						数据栈.get(数据栈.size()-2).setbool(数据栈.get(数据栈.size()-2).getbool()&&数据栈.get(数据栈.size()-1).getbool());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("|")){
						数据栈.get(数据栈.size()-2).setbool(数据栈.get(数据栈.size()-2).getbool()||数据栈.get(数据栈.size()-1).getbool());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("!")){
						数据栈.get(数据栈.size()-1).setbool(!数据栈.get(数据栈.size()-1).getbool());
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("(")){符号栈.remove(符号栈.size()-1);break;}
					else throw new Exception("运算错误\n"+str);
				}
			}while(符号栈.size()>0);
		}
		return 数据栈.get(0);
	}
}
