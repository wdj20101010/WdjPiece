import java.util.ArrayList;

public class RunFunct {
	private DataPool DP=Piece.DP;
	private ArrayList<String> TempPiece=new ArrayList<String>();
	private static int FUNCT_TIER=0;
	
	public RunFunct(String thispiece,String funct,String parame) throws Exception{
		String str1=DP.getFunct(funct)[0];
		String str2=DP.getFunct(funct)[1];
		String str3="",str4="";
		String str5="FUNCT_PARAME."+String.valueOf(FUNCT_TIER++)+".";
		int 括号=0;
		//处理参数
		for(int i=0,j=0;i<str1.length()&&j<parame.length();i++,j++) {
			//参数读取
			while(i<str1.length()&&str1.charAt(i)!=',')str3=str3+str1.charAt(i++);
			while(j<parame.length()&&!(parame.charAt(j)==','&&括号==0)){
				if(parame.charAt(j)=='(')括号++;else if(parame.charAt(j)==')')括号--;
				str4=str4+parame.charAt(j++);
			}
			//参数标记及保留
			if(DP.isHave(str3)){DP.rePlace(str5+str3, str3);TempPiece.add(str5+str3);}
			else TempPiece.add(str3);
			//参数赋值
			RunCodeToos.equate(str3,str4,thispiece);
			str3="";str4="";括号=0;
		}
		//数学函数
		     if(funct.equals("sin"))DP.setDigit("sin", Math.sin(DP.getDigit("SIN_PARAME")/180*Math.PI));
		else if(funct.equals("cos"))DP.setDigit("cos", Math.cos(DP.getDigit("COS_PARAME")/180*Math.PI));
		else if(funct.equals("tan"))DP.setDigit("tan", Math.tan(DP.getDigit("TAN_PARAME")/180*Math.PI));
		else if(funct.equals("asin"))DP.setDigit("asin", Math.asin(DP.getDigit("ASIN_PARAME"))/Math.PI*180);
		else if(funct.equals("acos"))DP.setDigit("acos", Math.acos(DP.getDigit("ACOS_PARAME"))/Math.PI*180);
		else if(funct.equals("atan"))DP.setDigit("atan", Math.atan(DP.getDigit("ATAN_PARAME"))/Math.PI*180);
		else if(funct.equals("ln"))DP.setDigit("ln", Math.log(DP.getDigit("LN_PARAME")));
		//字符串函数
		else if(funct.equals("strlen"))DP.setDigit("strlen", (double)DP.getStr("STR_PARAME").length());
		else if(funct.equals("strind"))DP.setDigit("strind", (double)DP.getStr("STR1_PARAME").indexOf(DP.getStr("STR2_PARAME")));
		else if(funct.equals("strequ"))DP.setBool("strequ", DP.getStr("STR1_PARAME").equals(DP.getStr("STR2_PARAME")));
		else if(funct.equals("stradd"))DP.setStr("stradd", DP.getStr("STR1_PARAME")+DP.getStr("STR2_PARAME"));
		else if(funct.equals("strsub"))DP.setStr("strsub",
				DP.getStr("STR_PARAME").substring(DP.getDigit("STR1_PARAME").intValue(), DP.getDigit("STR2_PARAME").intValue()));
		//代码执行函数
		else if(funct.equals("runcode")){
			DP.setBool("runcode", false);
			new RunCode(thispiece,DP.getStr("RUNCODE_PARAME"),false);
			DP.setBool("runcode", true);
		}
		//类加载执行函数
		else if(funct.equals("runclass")){
			DP.setBool("runclass", false);
			RunClassThread RCT=new RunClassThread(DP.getStr("JAR_PARAME"),DP.getStr("CLASS_PARAME"));
			RCT.setContextClassLoader(null);RCT.start();
			DP.setBool("runclass", true);
		}
		//外部程序调用函数
		else if(funct.equals("runapp")){
			DP.setBool("runapp", false);
			Runtime.getRuntime().exec(DP.getStr("RUNAPP_PARAME"));
			DP.setBool("runapp", true);
		}
		//程序出口函数
		else if(funct.equals("exit")){
			DP.setBool("exit", false);
			System.exit(0);
			DP.setBool("exit", true);
		}
		//其它函数
		else new RunCode(thispiece,str2,true);
		//参数恢复及移除
		for(int i=0;i<TempPiece.size();i++){
			if(TempPiece.get(i).indexOf(str5)==0)DP.rePlace(TempPiece.get(i).substring(str5.length()), TempPiece.get(i));
			DP.reMove(TempPiece.get(i));
		}
		FUNCT_TIER--;
	}
}
