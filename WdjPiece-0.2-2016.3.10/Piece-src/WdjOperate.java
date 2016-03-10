}语句执行
				String 结果="";
		        stry=stry+' ';String strz="";
		        for(int j=0;j<stry.length();j++)
		        	if((ch=stry.charAt(j))!=' ')strz=strz+ch;
		        	else{
		        		if(DP.gettype(strz)==null)throw new Exception("resultto{}语句错误\n未知变量: "+strz);
		        		else if(DP.gettype(strz).equals("bool"))结果=结果+strz+"="+DP.getbool(strz)+"\n";
		        		else if(DP.gettype(strz).equals("digit"))结果=结果+strz+"="+DP.getdigit(strz)+"\n";
		        		else if(DP.gettype(strz).equals("str"))结果=结果+strz+"="+DP.getstr(strz)+"\n";
		        		strz="";
		        	}
		        FileWriter fileout=new FileWriter(strx);
        		fileout.write(结果);fileout.close();
			}
		    //处理  变量=算式  语句
			else if(str1.length()>0){
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
	//基本运算处理,运算含有变量的单个算式字符串
	//数学运算      + - * / ^ sin cos tan asin acos atan ln
	//比较运算      > < == >= <=
	//布尔运算      & | !
	//字符串运算  strlen strequ strind stradd strsub
	//括号运算      ( )
	//优先级12    运算符错误,比如单个=的出现
	//优先级11 (
	//优先级10 sin cos tan asin acos atan ln strlen strequ strind stradd strsub
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
			   str1.charAt(i)!='{'&&str1.charAt(i)!='}'&&str1.charAt(i)!=',')str2=str2+str1.charAt(i);
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
			if(str2.equals(","))算式.remove(i--);
			//识别运算符和函数
			else if(str2.equals("("))算式.get(i).type=11;
			else if(str2.equals("sin")||str2.equals("cos")||str2.equals("tan")||str2.equals("asin")||
					str2.equals("acos")||str2.equals("atan")||str2.equals("ln")||str2.equals("strlen")||
					str2.equals("strequ")||str2.equals("strind")||str2.equals("stradd")||str2.equals("strsub"))算式.get(i).type=10;
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
					//字符串运算
					else if(str1.equals("strlen")){
						数据栈.get(数据栈.size()-1).setdigit((double)数据栈.get(数据栈.size()-1).getstr().length());
						符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("strequ")){
						数据栈.get(数据栈.size()-2).setbool(数据栈.get(数据栈.size()-2).getstr().equals(数据栈.get(数据栈.size()-1).getstr()));
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("strind")){
						数据栈.get(数据栈.size()-2).setdigit((double)数据栈.get(数据栈.size()-2).getstr().indexOf(数据栈.get(数据栈.size()-1).getstr()));
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("stradd")){
						数据栈.get(数据栈.size()-2).setstr(数据栈.get(数据栈.size()-2).getstr()+数据栈.get(数据栈.size()-1).getstr());
						数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					else if(str1.equals("strsub")){
						数据栈.get(数据栈.size()-3).setstr(数据栈.get(数据栈.size()-3).getstr().substring
								(数据栈.get(数据栈.size()-2).getdigit().intValue(), 数据栈.get(数据栈.size()-1).getdigit().intValue()));
						数据栈.remove(数据栈.size()-1);数据栈.remove(数据栈.size()-1);符号栈.remove(符号栈.size()-1);
					}
					//括号运算
					else if(str1.equals("(")){符号栈.remove(符号栈.size()-1);break;}
					else throw new Exception("运算错误\n"+str);
				}
			}while(符号栈.size()>0);
		}
		return 数据栈.get(0);
	}
}
