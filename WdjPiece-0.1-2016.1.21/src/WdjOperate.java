import java.util.ArrayList;

//运算包
public class WdjOperate {
	
	//重要变量
	ArrayList<BaseData> 变量=new ArrayList(0);
	String 结果="";
	
	//构造函数,总的运算处理过程
	WdjOperate(String str)throws Exception{
		mainOperate(pretreatOperate(str));
	}
	
	//--------------------------------------------------
	//代码预先处理，代码规整化
	String pretreatOperate(String str){
		char ch;String str1="",str2="",str3="";
		if(str.length()>0)str=str+'\n';
		//将制表符和回车符变为空格，去除注释
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!='\n'){if(ch=='\t')ch=' ';str1=str1+ch;}
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
	//代码流程处理,处理  if(){},处理  while(){},处理  result{},处理  变量=算式.
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
				BaseData XYZ=baseOperate(strx);
				if(XYZ.类型!=2)throw new Exception("if(){}语句错误");
				if(XYZ.布值)mainOperate(stry);
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
				BaseData XYZ=baseOperate(strx);
				if(XYZ.类型!=2)throw new Exception("while(){}语句错误");
				while(XYZ.布值){mainOperate(stry);XYZ=baseOperate(strx);}
			}
		    //处理result{}语句
			else if(str1.indexOf("result{")==0){
				String strx="";int x=1;
				i=i-str1.length()+7;str1="";
				//读取{}中的语句
				while(i<str.length()&&x!=0){ch=str.charAt(i++);if(ch=='{')x++;else if(ch=='}')x--;if(x!=0)strx=strx+ch;}
		        if(x!=0||strx.length()==0)throw new Exception("result{}语句错误");
		        //写入结果字符串
        		结果=结果+"\n###RESULT###";
		        strx=strx+' ';String stry="";
		        for(int j=0;j<strx.length();j++)
		        	if((ch=strx.charAt(j))!=' ')stry=stry+ch;
		        	else if(stry.length()>0){
		        		int k=0;
		        		while(k<变量.size()&&stry.equals(变量.get(k).名称)==false)k++;
		        		if(k==变量.size())throw new Exception("未知变量\n"+stry);
		        		else if(变量.get(k).类型==0)结果=结果+"\n"+变量.get(k).名称+"="+变量.get(k).串值;
		        		else if(变量.get(k).类型==1)结果=结果+"\n"+变量.get(k).名称+"="+变量.get(k).数值;
		        		else 结果=结果+"\n"+变量.get(k).名称+"="+变量.get(k).布值;
		        		stry="";
		        		}
			}
		    //处理  变量=算式  语句
			else{
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("语句错误\n"+str1);
				else{
					int j=0;String strx=str1.substring(0, str1.indexOf('='));
					BaseData XYZ=baseOperate(str1.substring(str1.indexOf('=')+1, str1.length()));
					while(j<变量.size()&&strx.equals(变量.get(j).名称)==false)j++;
					if(j==变量.size()){XYZ.名称=strx;变量.add(XYZ);}
					else{
						变量.get(j).类型=XYZ.类型;
						if(XYZ.类型==0)变量.get(j).串值=XYZ.串值;
						else if(XYZ.类型==1)变量.get(j).数值=XYZ.数值;
						else 变量.get(j).布值=XYZ.布值;
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
	BaseData baseOperate(String str)throws Exception{
		String str1="",str2="";int 大括号数=0,中括号数=0,小括号数=0;
		ArrayList<BaseData> 算式=new ArrayList(0);
		ArrayList<BaseData> 数据栈=new ArrayList(0);
		ArrayList<BaseData> 符号栈=new ArrayList(0);
		if(str.length()>0)str1="("+str+")";
		//算式
		for(int i=0;i<str1.length();i++)
			if(str1.charAt(i)!='+'&&str1.charAt(i)!='-'&&str1.charAt(i)!='*'&&str1.charAt(i)!='/'&&str1.charAt(i)!='^'&&
			   str1.charAt(i)!='>'&&str1.charAt(i)!='<'&&str1.charAt(i)!='='&&str1.charAt(i)!='&'&&str1.charAt(i)!='|'&&
			   str1.charAt(i)!='!'&&str1.charAt(i)!='('&&str1.charAt(i)!=')'&&str1.charAt(i)!='['&&str1.charAt(i)!=']'&&
			   str1.charAt(i)!='{'&&str1.charAt(i)!='}')str2=str2+str1.charAt(i);
			else{
				if(str2.length()>0){BaseData X=new BaseData();X.名称=str2;算式.add(X);str2="";}
				
				BaseData Y=new BaseData();
				     if(str1.charAt(i)=='{'){大括号数++;Y.名称="(";}
				else if(str1.charAt(i)=='['){中括号数++;Y.名称="(";}
				else if(str1.charAt(i)=='('){小括号数++;Y.名称="(";}
				else if(str1.charAt(i)=='}'){大括号数--;Y.名称=")";}
				else if(str1.charAt(i)==']'){中括号数--;Y.名称=")";}
				else if(str1.charAt(i)==')'){小括号数--;Y.名称=")";}
				else Y.名称=String.valueOf(str1.charAt(i));
				算式.add(Y);
				
				if(i<str1.length()-1&&
				  (str1.charAt(i)=='('||str1.charAt(i)=='['||str1.charAt(i)=='{'||
				   str1.charAt(i)=='>'||str1.charAt(i)=='<'||str1.charAt(i)=='=')&&
				  (str1.charAt(i+1)=='+'||str1.charAt(i+1)=='-')){BaseData Z=new BaseData();Z.名称="0";算式.add(Z);}
			}
		if(大括号数!=0)throw new Exception("大括号{}错误\n"+str);
		if(中括号数!=0)throw new Exception("中括号[]错误\n"+str);
		if(小括号数!=0)throw new Exception("小括号()错误\n"+str);
		
		//识别
		for(int i=0;i<算式.size();i++){
			str2=算式.get(i).名称;
			//识别运算符和函数
			if(str2.equals("("))算式.get(i).类型=11;
			else if(str2.equals("sin")||str2.equals("cos")||str2.equals("tan")||str2.equals("asin")||
					str2.equals("acos")||str2.equals("atan")||str2.equals("ln"))算式.get(i).类型=10;
			else if(str2.equals("^"))算式.get(i).类型=9;
			else if(str2.equals("*")||str2.equals("/"))算式.get(i).类型=8;
			else if(str2.equals("+")||str2.equals("-"))算式.get(i).类型=7;
			else if(i<算式.size()-1&&(str2.equals(">")||str2.equals("<")||str2.equals("="))&&
					算式.get(i+1).名称.equals("=")){算式.get(i).类型=6;算式.get(i).名称=str2+"=";算式.remove(i+1);}
			else if(str2.equals(">")||str2.equals("<"))算式.get(i).类型=6;
			else if(str2.equals("!"))算式.get(i).类型=5;
			else if(str2.equals("&")||str2.equals("|"))算式.get(i).类型=4;
			else if(str2.equals(")"))算式.get(i).类型=3;
			else if(str2.equals("="))算式.get(i).类型=12;//单个=的出现,是运算符错误
		    //识别布尔类型
		    else if(str2.equals("true")||str2.equals("false")){
		    	算式.get(i).类型=2;算式.get(i).布值=(boolean)Boolean.valueOf(str2);算式.get(i).名称="";
		    }
			else{
				//识别数字
				char ch;int 点数=0;boolean 是否数字=true;
				for(int j=0;j<str2.length();j++){
					ch=str2.charAt(j);
					if(!(ch>='0'&&ch<='9'||ch=='.')){是否数字=false;break;}
					if((j==0||j==str2.length()-1)&&ch=='.'){是否数字=false;break;}
					if(ch=='.')点数++;
				}
				if(点数>1)是否数字=false;
				if(是否数字){算式.get(i).类型=1;算式.get(i).数值=(double)Double.valueOf(str2);算式.get(i).名称="";}
				else{
	        		int k=0;
	        		while(k<变量.size()&&str2.equals(变量.get(k).名称)==false)k++;
					//识别字符串
	        		if(k==变量.size()){算式.get(i).类型=0;算式.get(i).串值=str2;算式.get(i).名称="";}
					//识别变量
	        		else{算式.get(i).类型=变量.get(k).类型;算式.get(i).布值=变量.get(k).布值;
	        		     算式.get(i).数值=变量.get(k).数值;算式.get(i).串值=变量.get(k).串值;算式.get(i).名称="";}
				}
			}
		}
		
		//运算
		for(int i=0;i<算式.size();i++){
			//数据进栈
			if(算式.get(i).类型<3)数据栈.add(算式.get(i));
			else do{
				//运算符和函数进栈
				if(符号栈.size()==0||符号栈.size()>0&&算式.get(i).类型>符号栈.get(符号栈.size()-1).类型){
					符号栈.add(算式.get(i));
					if(符号栈.get(符号栈.size()-1).名称.equals("("))符号栈.get(符号栈.size()-1).类型=3;
					break;
				}
				//出栈运算
				else{
					if(符号栈.get(符号栈.size()-1).名称.equals("+")){
						数据栈.get(数据栈.size()-2).数值=数据栈.get(数据栈.size()-2).数值+数据栈.get(数据栈.size()-1).数值;
						数据栈.get(数据栈.size()-2).类型=1;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("-")){
						数据栈.get(数据栈.size()-2).数值=数据栈.get(数据栈.size()-2).数值-数据栈.get(数据栈.size()-1).数值;
						数据栈.get(数据栈.size()-2).类型=1;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("*")){
						数据栈.get(数据栈.size()-2).数值=数据栈.get(数据栈.size()-2).数值*数据栈.get(数据栈.size()-1).数值;
						数据栈.get(数据栈.size()-2).类型=1;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("/")){
						数据栈.get(数据栈.size()-2).数值=数据栈.get(数据栈.size()-2).数值/数据栈.get(数据栈.size()-1).数值;
						数据栈.get(数据栈.size()-2).类型=1;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("^")){
						数据栈.get(数据栈.size()-2).数值=Math.pow(数据栈.get(数据栈.size()-2).数值,数据栈.get(数据栈.size()-1).数值);
						数据栈.get(数据栈.size()-2).类型=1;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("sin")){
						数据栈.get(数据栈.size()-1).数值=Math.sin(数据栈.get(数据栈.size()-1).数值/180*Math.PI);
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("cos")){
						数据栈.get(数据栈.size()-1).数值=Math.cos(数据栈.get(数据栈.size()-1).数值/180*Math.PI);
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("tan")){
						数据栈.get(数据栈.size()-1).数值=Math.tan(数据栈.get(数据栈.size()-1).数值/180*Math.PI);
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("asin")){
						数据栈.get(数据栈.size()-1).数值=Math.asin(数据栈.get(数据栈.size()-1).数值)/Math.PI*180;
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("acos")){
						数据栈.get(数据栈.size()-1).数值=Math.acos(数据栈.get(数据栈.size()-1).数值)/Math.PI*180;
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("atan")){
						数据栈.get(数据栈.size()-1).数值=Math.atan(数据栈.get(数据栈.size()-1).数值)/Math.PI*180;
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("ln")){
						数据栈.get(数据栈.size()-1).数值=Math.log(数据栈.get(数据栈.size()-1).数值);
						数据栈.get(数据栈.size()-1).类型=1;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals(">")){
						if(数据栈.get(数据栈.size()-2).数值>数据栈.get(数据栈.size()-1).数值)数据栈.get(数据栈.size()-2).布值=true;
						else 数据栈.get(数据栈.size()-2).布值=false;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("<")){
						if(数据栈.get(数据栈.size()-2).数值<数据栈.get(数据栈.size()-1).数值)数据栈.get(数据栈.size()-2).布值=true;
						else 数据栈.get(数据栈.size()-2).布值=false;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("==")){
						if(数据栈.get(数据栈.size()-2).类型==0)
							数据栈.get(数据栈.size()-2).布值=数据栈.get(数据栈.size()-2).串值.equals(数据栈.get(数据栈.size()-1).串值);
						else if(数据栈.get(数据栈.size()-2).类型==1)
							数据栈.get(数据栈.size()-2).布值=(数据栈.get(数据栈.size()-2).数值==数据栈.get(数据栈.size()-1).数值);
						else 数据栈.get(数据栈.size()-2).布值=(数据栈.get(数据栈.size()-2).布值==数据栈.get(数据栈.size()-1).布值);
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals(">=")){
						if(数据栈.get(数据栈.size()-2).数值>=数据栈.get(数据栈.size()-1).数值)数据栈.get(数据栈.size()-2).布值=true;
						else 数据栈.get(数据栈.size()-2).布值=false;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("<=")){
						if(数据栈.get(数据栈.size()-2).数值<=数据栈.get(数据栈.size()-1).数值)数据栈.get(数据栈.size()-2).布值=true;
						else 数据栈.get(数据栈.size()-2).布值=false;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("&")){
						数据栈.get(数据栈.size()-2).布值=数据栈.get(数据栈.size()-2).布值&&数据栈.get(数据栈.size()-1).布值;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("|")){
						数据栈.get(数据栈.size()-2).布值=数据栈.get(数据栈.size()-2).布值||数据栈.get(数据栈.size()-1).布值;
						数据栈.get(数据栈.size()-2).类型=2;数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("!")){
						数据栈.get(数据栈.size()-1).布值=!数据栈.get(数据栈.size()-1).布值;
						数据栈.get(数据栈.size()-1).类型=2;符号栈.remove(符号栈.size()-1);
					}
					else if(符号栈.get(符号栈.size()-1).名称.equals("(")){符号栈.remove(符号栈.size()-1);break;}
					else throw new Exception("运算错误\n"+str);
				}
			}while(符号栈.size()>0);
		}
		return 数据栈.get(0);
	}
}
