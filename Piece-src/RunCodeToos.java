import java.util.ArrayList;

public class RunCodeToos {
	//-----------------------------------------------------------------------------------
	//预先处理 代码规整化
	public static String pretreat(String str)throws Exception{
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
			int Z=Y,括号=1;
			while(Z<str.length()&&括号!=0){ch=str.charAt(Z++);if(ch=='{')括号++;else if(ch=='}')括号--;}
			if(括号!=0)throw new Exception("\nenter{}语句错误\n"+str);
			String strx=str.substring(0, X);
			String stry=str.substring(Y, Z-1);
			String strz=str.substring(Z);
			return pretreat(strx)+" enter{"+stry+"} "+pretreat(strz);
		}
		//处理双引号" "语句
		if(str.indexOf("\"")!=-1){
			String strx="",stry="",strz="";
			strx=str.substring(0,str.indexOf("\""));
			stry=str.substring(str.indexOf("\"")+1);
			if(stry.indexOf("\"")!=-1){
				strz=stry.substring(stry.indexOf("\"")+1);
				stry=stry.substring(0,stry.indexOf("\""));
			}
			return pretreat(strx)+"\""+stry+"\" "+pretreat(strz);
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
		if(str2.length()>1&&str2.charAt(0)==' ')str2=str2.substring(1);
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
				    str2.charAt(i)=='.'||str2.charAt(i)==','))str3=str3+str2.charAt(i++);
			else str3=str3+str2.charAt(i);
		return str3;
	}
	
	//-----------------------------------------------------------------------------------
	//this.处理  super.处理  .index()处理
	public static String thisSuperIndex(String str,String ThisPiece)throws Exception{
		char ch;int i,括号;String str1;
		if(ThisPiece.length()>0)
			if(str.equals("this"))str=ThisPiece;
			else if(str.indexOf("this.")==0)str=ThisPiece+"."+str.substring(5);
		if(ThisPiece.lastIndexOf('.')>0)
			if(str.equals("super"))str=ThisPiece.substring(0, ThisPiece.lastIndexOf('.'));
			else if(str.indexOf("super.")==0)str=ThisPiece.substring(0, ThisPiece.lastIndexOf('.'))+"."+str.substring(6);
		while(str.indexOf(".index(")!=-1){
			i=str.indexOf(".index(")+7;括号=1;str1="";
			while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str1=str1+ch;}
			if(括号!=0)throw new Exception("\nindex()函数错误\n"+str);
			DataPool.DataBase XYZ=base(str1,ThisPiece);
			if(XYZ.gettype().equals("digit"))str1=String.valueOf(Math.abs(XYZ.getdigit().intValue()));
			else str1=XYZ.getstr();
			if(str1.length()==0)str1="0";
			str=str.substring(0, str.indexOf(".index("))+"."+str1+str.substring(i);
		}
		return str;
	}
	
	//-----------------------------------------------------------------------------------
	//等式处理
	public static void equate(String str1,String str2,String ThisPiece)throws Exception{
		str1=thisSuperIndex(str1,ThisPiece);
		str2=thisSuperIndex(str2,ThisPiece);
		while(true){
			//是变量
			if(Piece.DP.isHave(str2)){
				String strx=str1;if(strx.indexOf('.')!=-1)strx=strx.substring(0, strx.indexOf('.'));
			    String stry=str2;if(stry.indexOf('.')!=-1)stry=stry.substring(0, stry.indexOf('.'));
			    if(strx.equals(stry)){Piece.DP.rePlace("DP_PIECE_TEMP", str2);Piece.DP.rePlace(str1, "DP_PIECE_TEMP");break;}
			    else {Piece.DP.rePlace(str1, str2);break;}
		    }
		    //是函数
		    else if(str2.indexOf('(')!=-1&&Piece.DP.isHave(str2.substring(0, str2.indexOf('(')))&&
				    str2.indexOf('+')==-1&&str2.indexOf('-')==-1&&str2.indexOf('*')==-1&&str2.indexOf('/')==-1&&
				    str2.indexOf('^')==-1&&str2.indexOf('>')==-1&&str2.indexOf('<')==-1&&str2.indexOf('=')==-1&&
				    str2.indexOf('&')==-1&&str2.indexOf('|')==-1&&str2.indexOf('!')==-1){
			    String strx=str2.substring(0, str2.indexOf('('));String stry=str2.substring(str2.indexOf('(')+1);
			    int 括号=1,i=0;while(i<stry.length()&&括号!=0){if(stry.charAt(i)=='(')括号++;else if(stry.charAt(i)==')')括号--;i++;}
			    if(括号==0){new RunFunct(ThisPiece,strx,stry.substring(0, i-1));str2=strx;}
			    if(i<stry.length()&&stry.charAt(i)=='.')str2=strx+stry.substring(i);
		    }
		    else break;
		}
		//是 null Null NULL
		if(str2.equals("null")||str2.equals("Null")||str2.equals("NULL"))Piece.DP.reMove(str1);
		//是布尔
		else if(str2.equals("true"))Piece.DP.setBool(str1, true);
		else if(str2.equals("false"))Piece.DP.setBool(str1, false);
		//是数字
		else try{Piece.DP.setDigit(str1, Double.valueOf(str2));}
		catch(Exception ex){
			//是字符串
			if(str2.charAt(0)=='\"'&&str2.charAt(str2.length()-1)=='\"')
				Piece.DP.setStr(str1, str2.substring(1, str2.length()-1));
			//是算式
			else{
				DataPool.DataBase XYZ=base(str2,ThisPiece);
				if(XYZ.gettype().equals("bool"))Piece.DP.setBool(str1, XYZ.getbool());
				else if(XYZ.gettype().equals("digit"))Piece.DP.setDigit(str1, XYZ.getdigit());
				else if(XYZ.gettype().equals("str"))Piece.DP.setStr(str1, XYZ.getstr());
			}
		}
	}
	
	//-----------------------------------------------------------------------------------
	//算式计算
	//数学运算   + - * / ^
	//比较运算   > < == >= <=
	//布尔运算   & | !
	//括号运算  ()
	//优先级8  (
	//优先级7  ^
	//优先级6  * /
	//优先级5  + -
	//优先级4  > < == >= <=
	//优先级3  !
	//优先级2  & |
	//优先级1  )
	public static DataPool.DataBase base(String str,String ThisPiece)throws Exception{
		String str1="",str2="";int 大括号数=0,中括号数=0,小括号数=0;
		ArrayList<DataPool.DataBase> 算式=new ArrayList<DataPool.DataBase>();
		ArrayList<DataPool.DataBase> 数据栈=new ArrayList<DataPool.DataBase>();
		ArrayList<DataPool.DataBase> 符号栈=new ArrayList<DataPool.DataBase>();
		if(str.length()>0)str1="("+thisSuperIndex(str,ThisPiece)+")";
		//算式
		for(int i=0;i<str1.length();i++)
			if(str1.charAt(i)!='+'&&str1.charAt(i)!='-'&&str1.charAt(i)!='*'&&str1.charAt(i)!='/'&&str1.charAt(i)!='^'&&
			   str1.charAt(i)!='>'&&str1.charAt(i)!='<'&&str1.charAt(i)!='='&&str1.charAt(i)!='&'&&str1.charAt(i)!='|'&&
			   str1.charAt(i)!='!'&&str1.charAt(i)!='('&&str1.charAt(i)!=')'&&str1.charAt(i)!='['&&str1.charAt(i)!=']'&&
			   str1.charAt(i)!='{'&&str1.charAt(i)!='}')str2=str2+str1.charAt(i);
			else{
				if(str2.length()>0){DataPool.DataBase X=new DataPool.DataBase();X.setstr(str2);算式.add(X);str2="";}
				
				DataPool.DataBase Y=new DataPool.DataBase();
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
				  (str1.charAt(i+1)=='+'||str1.charAt(i+1)=='-')){DataPool.DataBase Z=new DataPool.DataBase();Z.setstr("0");算式.add(Z);}
			}
		if(大括号数!=0)throw new Exception("\n大括号{}错误\n"+str);
		if(中括号数!=0)throw new Exception("\n中括号[]错误\n"+str);
		if(小括号数!=0)throw new Exception("\n小括号()错误\n"+str);
		
		//识别
		for(int i=0;i<算式.size();i++){
			str1=算式.get(i).getstr();
			str1=thisSuperIndex(str1,ThisPiece);
			//识别
			while(Piece.DP.isHave(str1)){
				//是函数
				if(i+2<算式.size()&&算式.get(i+1).getstr().equals("(")){
					str2="";int j=i+2,括号=1;
					while(j<算式.size()&&括号!=0){
						if(算式.get(j).getstr().equals("("))括号++;else if(算式.get(j).getstr().equals(")"))括号--;
						if(括号!=0)str2=str2+算式.get(j++).getstr();
					}
					if(括号!=0)throw new Exception("\n算式错误  "+str+"\n函数错误  "+str1);
					while(j>i){算式.remove(i+1);j--;}
					new RunFunct(ThisPiece,str1,str2);
				}
				if(i+1<算式.size()&&算式.get(i+1).getstr().indexOf('.')==0){
					str1=str1+算式.get(i+1).getstr();算式.remove(i+1);continue;
				}
				//是变量
				算式.get(i).setbool(Piece.DP.getBool(str1));
				算式.get(i).setdigit(Piece.DP.getDigit(str1));
				算式.get(i).setstr(Piece.DP.getStr(str1));
				算式.get(i).settype(Piece.DP.getType(str1));
				break;
			}
			//是运算符
			if(str1.equals("("))算式.get(i).settype("8");
			else if(str1.equals("^"))算式.get(i).settype("7");
			else if(str1.equals("*")||str1.equals("/"))算式.get(i).settype("6");
			else if(str1.equals("+")||str1.equals("-"))算式.get(i).settype("5");
			else if(i<算式.size()-1&&(str1.equals(">")||str1.equals("<")||str1.equals("="))&&
					算式.get(i+1).getstr().equals("=")){算式.get(i).setstr(str1+"=");算式.remove(i+1);算式.get(i).settype("4");}
			else if(str1.equals(">")||str1.equals("<"))算式.get(i).settype("4");
			else if(str1.equals("!"))算式.get(i).settype("3");
			else if(str1.equals("&")||str1.equals("|"))算式.get(i).settype("2");
			else if(str1.equals(")"))算式.get(i).settype("1");
		    //是布尔
		    else if(str1.equals("true"))算式.get(i).setbool(true);
		    else if(str1.equals("false"))算式.get(i).setbool(false);
			//是数字
			else try{算式.get(i).setdigit(Double.valueOf(str1));}
			catch(Exception ex){}
		}
		
		//运算
		for(int i=0;i<算式.size();i++){
			//数据进栈
			if(算式.get(i).gettype().equals("bool")||算式.get(i).gettype().equals("digit")||算式.get(i).gettype().equals("str"))数据栈.add(算式.get(i));
			else do{
				//运算符进栈
				if(符号栈.size()==0||符号栈.size()>0&&Integer.valueOf(算式.get(i).gettype())>Integer.valueOf(符号栈.get(符号栈.size()-1).gettype())){
					符号栈.add(算式.get(i));
					if(符号栈.get(符号栈.size()-1).getstr().equals("("))符号栈.get(符号栈.size()-1).settype("1");
					break;
				}
				//出栈运算
				else{
					str1=符号栈.get(符号栈.size()-1).getstr();
					//数学运算
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
					//比较运算
					else if(str1.equals(">")){
						数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()>(double)数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("<")){
						数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()<(double)数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("==")){
						数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()==(double)数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals(">=")){
						数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()>=(double)数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("<=")){
						数据栈.get(数据栈.size()-2).setbool((double)数据栈.get(数据栈.size()-2).getdigit()<=(double)数据栈.get(数据栈.size()-1).getdigit());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					//布尔运算
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
					//括号运算
					else if(str1.equals("(")){符号栈.remove(符号栈.size()-1);break;}
					else throw new Exception("\n算式错误  "+str+"\n未知表达  "+str1);
				}
			}while(符号栈.size()>0);
		}
		return 数据栈.get(0);
	}
	//-----------------------------------------------------------------------------------
}
