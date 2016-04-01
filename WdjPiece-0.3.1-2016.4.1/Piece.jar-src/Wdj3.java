import java.util.ArrayList;

public class Wdj3 {
	
	private DataPool DP;
	private String ThisPiece="";
	private ArrayList<String> TempPiece;
	
	Wdj3(DataPool dp,String thispiece){
		DP=dp;ThisPiece=thispiece;
		TempPiece=new ArrayList<String>();
	}
	
	//函数处理
	public void functOperate(String name,String parame){
		String str[]=DP.getfunct(name),str1="",str2="";
		//处理参数
		for(int i=0,j=0;i<str[0].length()&&j<parame.length();i++,j++){
			while(i<str[0].length()&&str[0].charAt(i)!=',')str1=str1+str[0].charAt(i++);
			while(j<parame.length()&&parame.charAt(j)!=',')str2=str2+parame.charAt(j++);
			if(!DP.ishave(str1))TempPiece.add(str1);
			new Wdj2(DP,ThisPiece).equateOperate(str1, str2);
			str1="";str2="";
		}
		//数学函数
		     if(name.equals("sin"))DP.setdigit("sin", Math.sin(DP.getdigit("SIN_PARAME")/180*Math.PI));
		else if(name.equals("cos"))DP.setdigit("cos", Math.cos(DP.getdigit("COS_PARAME")/180*Math.PI));
		else if(name.equals("tan"))DP.setdigit("tan", Math.tan(DP.getdigit("TAN_PARAME")/180*Math.PI));
		else if(name.equals("asin"))DP.setdigit("asin", Math.asin(DP.getdigit("ASIN_PARAME"))/Math.PI*180);
		else if(name.equals("acos"))DP.setdigit("acos", Math.acos(DP.getdigit("ACOS_PARAME"))/Math.PI*180);
		else if(name.equals("atan"))DP.setdigit("atan", Math.atan(DP.getdigit("ATAN_PARAME"))/Math.PI*180);
		else if(name.equals("ln"))DP.setdigit("ln", Math.log(DP.getdigit("LN_PARAME")));
		//字符串函数
		else if(name.equals("strlen"))DP.setdigit("strlen", (double)DP.getstr("STR_PARAME").length());
		else if(name.equals("strind"))DP.setdigit("strind", (double)DP.getstr("STR1_PARAME").indexOf(DP.getstr("STR2_PARAME")));
		else if(name.equals("strequ"))DP.setbool("strequ", DP.getstr("STR1_PARAME").equals(DP.getstr("STR2_PARAME")));
		else if(name.equals("stradd"))DP.setstr("stradd", DP.getstr("STR1_PARAME")+DP.getstr("STR2_PARAME"));
		else if(name.equals("strsub"))DP.setstr("strsub",
				DP.getstr("STR_PARAME").substring(DP.getdigit("STR1_PARAME").intValue(), DP.getdigit("STR2_PARAME").intValue()));
		//其它函数
		else new Wdj1(DP,name,false);
		//移除参数
		for(int i=0;i<TempPiece.size();i++)DP.remove(TempPiece.get(i));
	}
}
