import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Grammar {
	private StyledDocument D;
	private String[] KeyWords= {"include","enter","entfrom","result","resulto","if","while",
                                "piece","proce","newproce","funct","super","this","index","main","null","true","false",
                                "sin","cos","tan","asin","acos","atan","ln","strlen","strind","strequ","stradd","strsub",
                                "runcode","runclass","runapp","exit"};
	private String KeySymbols=" \n\t+-*/^><=&|!.,\"()[]{}";
	private ArrayList<String> UserWords=new ArrayList<String>();
	
	public Grammar(StyledDocument d) {D=d;for(int i=0;i<KeyWords.length;i++)addUserWords(KeyWords[i]);}
	
	public void run(int position) {
		SimpleAttributeSet attset0=new SimpleAttributeSet();
		SimpleAttributeSet attset1=new SimpleAttributeSet();
		SimpleAttributeSet attset2=new SimpleAttributeSet();
		SimpleAttributeSet attset3=new SimpleAttributeSet();
		SimpleAttributeSet attset4=new SimpleAttributeSet();
		StyleConstants.setForeground(attset0, Color.black);
		StyleConstants.setBackground(attset0, Color.white);
		StyleConstants.setForeground(attset1, Color.gray);
		StyleConstants.setForeground(attset2, new Color(200,0,50));
		StyleConstants.setForeground(attset3, Color.blue);
		StyleConstants.setBackground(attset4, Color.yellow);
		
		try{
			D.setCharacterAttributes(0, D.getLength(), attset0, true);
			String str=D.getText(0, D.getLength()),str1=null,str2;int x,y=0,z=0;
			while(str.length()>0) {
				//逐行读取
				if(str.lastIndexOf("\n")>-1) {str1=str.substring(str.lastIndexOf("\n"));str=str.substring(0, str.lastIndexOf("\n"));}
				else {str1=str;str="";}
				//去除注释
				if(str1.indexOf("//")>-1) {
					D.setCharacterAttributes(str.length()+str1.indexOf("//"), str1.length()-str1.indexOf("//"), attset1, true);
					str1=str1.substring(0, str1.indexOf("//"));
				}
				//识别关键字
				for(int i=0;i<KeyWords.length;i++) {str2=str1;
					while((x=str2.lastIndexOf(KeyWords[i]))>-1) {
						if(x>0 && KeySymbols.indexOf(str2.charAt(x-1))==-1);
						else if(x<str2.length()-KeyWords[i].length() && KeySymbols.indexOf(str2.charAt(x+KeyWords[i].length()))==-1);
						else {D.setCharacterAttributes(str.length()+x, KeyWords[i].length(), attset2, true);}
						str2=str2.substring(0, x);
					}
				}
				//识别用户字
				while(str1.lastIndexOf("=")>-1) {str1=str1.substring(0,str1.lastIndexOf("="));
					while(str1.length()>0&&(str1.charAt(str1.length()-1)==' '||str1.charAt(str1.length()-1)=='\t'))str1=str1.substring(0, str1.length()-1);
					x=-1;for(int i=0;i<KeySymbols.length();i++)if(x<str1.lastIndexOf(KeySymbols.charAt(i)))x=str1.lastIndexOf(KeySymbols.charAt(i));
					if(x+1<str1.length())addUserWords(str1.substring(x+1));
				}
			}
			
			str=D.getText(0, D.getLength());
			//识别双引号
			while((x=str.lastIndexOf("\""))>-1) {
				if(z==0) {y=x;z=1;}
				else if(z==1) {z=0;D.setCharacterAttributes(x, y-x+1, attset3, true);}
				str=str.substring(0, x);
			}
			//标注光标处相同的字符串
			x=position;y=position;str1=str;str2=null;
			while(x>0 && KeySymbols.indexOf(str1.charAt(x-1))==-1)x=x-1;
			while(y<str.length() && KeySymbols.indexOf(str1.charAt(y))==-1)y=y+1;
			if(y>x)str2=str1.substring(x, y);
			while((z=str1.lastIndexOf(str2))>-1) {
				if(z>0 && KeySymbols.indexOf(str1.charAt(z-1))==-1);
				else if(z<str1.length()-str2.length() && KeySymbols.indexOf(str1.charAt(z+str2.length()))==-1);
				else {D.setCharacterAttributes(z, str2.length(), attset4, false);}
				str1=str1.substring(0, z);
			}
			//标注括号中内容
			x=position-1;y=position;z=1;
			if(position>0&&str.charAt(position-1)=='(')
				while(z>0&&y<str.length()) {if(str.charAt(y)=='(')z++;else if(str.charAt(y)==')')z--;y++;}
			else if(position>0&&str.charAt(position-1)=='[')
				while(z>0&&y<str.length()) {if(str.charAt(y)=='[')z++;else if(str.charAt(y)==']')z--;y++;}
			else if(position>0&&str.charAt(position-1)=='{')
				while(z>0&&y<str.length()) {if(str.charAt(y)=='{')z++;else if(str.charAt(y)=='}')z--;y++;}
			else if(position<str.length()&&str.charAt(position)==')')
			{while(z>0&&x>-1) {if(str.charAt(x)=='(')z--;else if(str.charAt(x)==')')z++;x--;}x++;y++;}
			else if(position<str.length()&&str.charAt(position)==']')
			{while(z>0&&x>-1) {if(str.charAt(x)=='[')z--;else if(str.charAt(x)==']')z++;x--;}x++;y++;}
			else if(position<str.length()&&str.charAt(position)=='}') 
			{while(z>0&&x>-1) {if(str.charAt(x)=='{')z--;else if(str.charAt(x)=='}')z++;x--;}x++;y++;}
			if(z==0)D.setCharacterAttributes(x, y-x, attset4, false);
		}
		catch(Exception ex){}
	}
	
	public void addUserWords(String str) {
		int i=0;while(i<UserWords.size()&&UserWords.get(i).compareTo(str)<0)i++;
		if(i==UserWords.size())UserWords.add(str);
		else if(UserWords.get(i).equals(str))return;
		else UserWords.add(i,str);
	}
	public Object[] getUserWords(String str){
		ArrayList<String> list=new ArrayList<String>();
		int i=0;while(i<UserWords.size()&&UserWords.get(i).compareTo(str)<0)i++;
		int j=0;while(i<UserWords.size()&&j<10)if(UserWords.get(i++).indexOf(str)==0) {list.add(UserWords.get(i-1));j++;}
		return list.toArray();
	}
}
