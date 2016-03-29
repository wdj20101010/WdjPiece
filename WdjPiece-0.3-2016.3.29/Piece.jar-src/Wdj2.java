import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Wdj2 {
	private DataPool DP;
	Wdj2(DataPool dp){DP=dp;}
	//等式处理
	public void equateOperate(String str1,String str2){
		//是变量
		if(DP.ishave(str2))DP.replace(str1, str2);
		//是布尔
		else if(str2.equals("true")){DP.setbool(str1, true);}
		else if(str2.equals("false")){DP.setbool(str1, false);}
		//是数字
		else try{DP.setdigit(str1, Double.valueOf(str2));}
		//是算式
		catch(Exception ex){
			try{
				DataBase XYZ=baseOperate(str2);
				if(XYZ.type==2)DP.setbool(str1, XYZ.getbool());
				else if(XYZ.type==1)DP.setdigit(str1, XYZ.getdigit());
				else if(XYZ.type==0)DP.setstr(str1, XYZ.getstr());
			}
			catch(Exception excep){JOptionPane.showMessageDialog(null, excep);}
		}
	}
	//--------------------------------------------------------------------------------
	//算式计算
	//数学运算      + - * / ^
	//比较运算      > < == >= <=
	//布尔运算      & | !
	//括号运算     ()
	//优先级10 (
	//优先级9  ^
	//优先级8  * /
	//优先级7  + -
	//优先级6  > < == >= <=
	//优先级5  !
	//优先级4  & |
	//优先级3  )
	//0 1 2  用于表示  字符串  数字  和  布尔
	public DataBase baseOperate(String str)throws Exception{
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
		if(大括号数!=0)throw new Exception("\n大括号{}错误\n"+str);
		if(中括号数!=0)throw new Exception("\n中括号[]错误\n"+str);
		if(小括号数!=0)throw new Exception("\n小括号()错误\n"+str);
		
		//识别
		for(int i=0;i<算式.size();i++){
			str1=算式.get(i).getstr();
			if(DP.ishave(str1)){
				//是函数
				if(i+2<算式.size()&&算式.get(i+1).getstr().equals("(")){
					str2="";int j=i+2,括号=1;
					while(j<算式.size()&&括号!=0){
						if(算式.get(j).getstr().equals("("))括号++;else if(算式.get(j).getstr().equals(")"))括号--;
						if(括号!=0)str2=str2+算式.get(j++).getstr();
					}
					if(括号!=0)throw new Exception("\n算式错误  "+str+"\n函数错误  "+str1);
					while(j>i){算式.remove(i+1);j--;}
					new Wdj3(DP).functOperate(str1,str2);
				}
				//是变量
				算式.get(i).setbool(DP.getbool(str1));
				算式.get(i).setdigit(DP.getdigit(str1));
				算式.get(i).setstr(DP.getstr(str1));
				if(DP.gettype(str1).equals("bool"))算式.get(i).type=2;
        		else if(DP.gettype(str1).equals("digit"))算式.get(i).type=1;
        		else 算式.get(i).type=0;
			}
			//是运算符
			else if(str1.equals("("))算式.get(i).type=10;
			else if(str1.equals("^"))算式.get(i).type=9;
			else if(str1.equals("*")||str1.equals("/"))算式.get(i).type=8;
			else if(str1.equals("+")||str1.equals("-"))算式.get(i).type=7;
			else if(i<算式.size()-1&&(str1.equals(">")||str1.equals("<")||str1.equals("="))&&
					算式.get(i+1).getstr().equals("=")){算式.get(i).setstr(str1+"=");算式.remove(i+1);算式.get(i).type=6;}
			else if(str1.equals(">")||str1.equals("<"))算式.get(i).type=6;
			else if(str1.equals("!"))算式.get(i).type=5;
			else if(str1.equals("&")||str1.equals("|"))算式.get(i).type=4;
			else if(str1.equals(")"))算式.get(i).type=3;
		    //是布尔
		    else if(str1.equals("true"))算式.get(i).setbool(true);
		    else if(str1.equals("false"))算式.get(i).setbool(false);
			//是数字
			else try{算式.get(i).setdigit(Double.valueOf(str1));}
			//是字符串
			catch(Exception ex){}
		}
		
		//运算
		for(int i=0;i<算式.size();i++){
			//数据进栈
			if(算式.get(i).type<3)数据栈.add(算式.get(i));
			else do{
				//运算符进栈
				if(符号栈.size()==0||符号栈.size()>0&&算式.get(i).type>符号栈.get(符号栈.size()-1).type){
					符号栈.add(算式.get(i));
					if(符号栈.get(符号栈.size()-1).getstr().equals("("))符号栈.get(符号栈.size()-1).type=3;
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
}
