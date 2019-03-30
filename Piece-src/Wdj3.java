import java.util.ArrayList;

public class Wdj3 {
	
	private DataPool DP;
	private String ThisPiece="";
	private ArrayList<String> TempPiece;
	private static int FUNCT_TIER=0;
	
	Wdj3(DataPool dp,String thispiece){
		DP=dp;ThisPiece=thispiece;
		TempPiece=new ArrayList<String>();
	}
	
	//函数处理
	public void functOperate(String name,String parame)throws Exception{
		char ch;int 括号=0;
		String str[]=DP.getFunct(name),str1="",str2="";
		String str3="FUNCT_PARAME."+String.valueOf(FUNCT_TIER++)+".";
		Wdj2 wdj=new Wdj2(DP,ThisPiece);
		//处理参数
		for(int i=0,j=0;i<str[0].length()&&j<parame.length();i++,j++){
			while(i<str[0].length()&&str[0].charAt(i)!=',')str1=str1+str[0].charAt(i++);
			while(j<parame.length()&&!((ch=parame.charAt(j))==','&&括号==0)){
				if(ch=='(')括号++;else if(ch==')')括号--;
				str2=str2+ch;j++;
			}
			//参数保存及标记
			if(DP.isHave(str1)){DP.rePlace(str3+str1, str1);TempPiece.add(str3+str1);}
			else TempPiece.add(str1);
			//参数赋值
			wdj.equateOperate(str1, str2);
			str1="";str2="";括号=0;
		}
		//数学函数
		     if(name.equals("sin"))DP.setDigit("sin", Math.sin(DP.getDigit("SIN_PARAME")/180*Math.PI));
		else if(name.equals("cos"))DP.setDigit("cos", Math.cos(DP.getDigit("COS_PARAME")/180*Math.PI));
		else if(name.equals("tan"))DP.setDigit("tan", Math.tan(DP.getDigit("TAN_PARAME")/180*Math.PI));
		else if(name.equals("asin"))DP.setDigit("asin", Math.asin(DP.getDigit("ASIN_PARAME"))/Math.PI*180);
		else if(name.equals("acos"))DP.setDigit("acos", Math.acos(DP.getDigit("ACOS_PARAME"))/Math.PI*180);
		else if(name.equals("atan"))DP.setDigit("atan", Math.atan(DP.getDigit("ATAN_PARAME"))/Math.PI*180);
		else if(name.equals("ln"))DP.setDigit("ln", Math.log(DP.getDigit("LN_PARAME")));
		//字符串函数
		else if(name.equals("strlen"))DP.setDigit("strlen", (double)DP.getStr("STR_PARAME").length());
		else if(name.equals("strind"))DP.setDigit("strind", (double)DP.getStr("STR1_PARAME").indexOf(DP.getStr("STR2_PARAME")));
		else if(name.equals("strequ"))DP.setBool("strequ", DP.getStr("STR1_PARAME").equals(DP.getStr("STR2_PARAME")));
		else if(name.equals("stradd"))DP.setStr("stradd", DP.getStr("STR1_PARAME")+DP.getStr("STR2_PARAME"));
		else if(name.equals("strsub"))DP.setStr("strsub",
				DP.getStr("STR_PARAME").substring(DP.getDigit("STR1_PARAME").intValue(), DP.getDigit("STR2_PARAME").intValue()));
		//外部程序调用函数
		else if(name.equals("runapp")){
			DP.setBool("runapp", false);
			Runtime.getRuntime().exec(DP.getStr("RUNAPP_PARAME"));
			DP.setBool("runapp", true);
		}
		//代码执行函数
		else if(name.equals("runcode"))new Wdj1(DP,"runcode",DP.getStr("RUNCODE_PARAME"));
		//类加载执行函数
		else if(name.equals("runclass")){
			DP.setBool("runclass", false);
			RunClassThread RCT=new RunClassThread(DP.getStr("JAR_PARAME"),DP.getStr("CLASS_PARAME"));
			RCT.setContextClassLoader(null);
			if(DP.getBool("BLOCKorNO_PARAME")) RCT.run();
			else RCT.start();
			DP.setBool("runclass", true);
		}
		//等待函数
		else if(name.equals("wait")){
			DP.setBool("wait", false);
			Thread.sleep(DP.getDigit("WAIT_PARAME").longValue());
			DP.setBool("wait", true);
		}
		//其它函数
		else new Wdj1(DP,name,false);
		//参数恢复及移除
		for(int i=0;i<TempPiece.size();i++){
			if(TempPiece.get(i).indexOf(str3)==0)DP.rePlace(TempPiece.get(i).substring(str3.length()), TempPiece.get(i));
			DP.reMove(TempPiece.get(i));
		}
		FUNCT_TIER--;
	}
}
