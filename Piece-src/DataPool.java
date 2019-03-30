import java.util.ArrayList;
import java.util.HashMap;

public class DataPool {
	//内置函数
	public DataPool() {
		//数学函数
		setFunct("sin",new String[]{"SIN_PARAME",null});
		setFunct("cos",new String[]{"COS_PARAME",null});
		setFunct("tan",new String[]{"TAN_PARAME",null});
		setFunct("asin",new String[]{"ASIN_PARAME",null});
		setFunct("acos",new String[]{"ACOS_PARAME",null});
		setFunct("atan",new String[]{"ATAN_PARAME",null});
		setFunct("ln",new String[]{"LN_PARAME",null});
		//字符串函数
		setFunct("strlen",new String[]{"STR_PARAME",null});
		setFunct("strind",new String[]{"STR1_PARAME,STR2_PARAME",null});
		setFunct("strequ",new String[]{"STR1_PARAME,STR2_PARAME",null});
		setFunct("stradd",new String[]{"STR1_PARAME,STR2_PARAME",null});
		setFunct("strsub",new String[]{"STR_PARAME,STR1_PARAME,STR2_PARAME",null});
		//外部程序调用函数
		setFunct("runapp",new String[]{"RUNAPP_PARAME",null});
		//代码执行函数
		setFunct("runcode",new String[]{"RUNCODE_PARAME",null});
		//类加载执行函数
		setFunct("runclass",new String[]{"JAR_PARAME,CLASS_PARAME,BLOCKorNO_PARAME",null});
		//等待函数
		setFunct("wait",new String[]{"WAIT_PARAME",null});
	}
	//数据
	private ArrayList<String> Name=new ArrayList<String>();
	private ArrayList<Integer> NameID=new ArrayList<Integer>();
	private DataPoolID IDBuilder=new DataPoolID();
	private HashMap<Integer,String> Type=new HashMap<Integer,String>();
	private HashMap<Integer,Boolean> Bool=new HashMap<Integer,Boolean>();
	private HashMap<Integer,Double> Digit=new HashMap<Integer,Double>();
	private HashMap<Integer,String> Str=new HashMap<Integer,String>();
	private int ProceCount=0,FunctCount=0;
	private HashMap<Integer,Integer> ProceID=new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> FunctID=new HashMap<Integer,Integer>();
	private ArrayList<String> Proce=new ArrayList<String>();
	private ArrayList<String[]> Funct=new ArrayList<String[]>();
	//set
	public void setType(String name,String type) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		Type.put(x, type);
	}
	public void setBool(String name,Boolean bool) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		Type.put(x, "bool");Bool.put(x, bool);
	}
	public void setDigit(String name,Double digit) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		Type.put(x, "digit");Digit.put(x, digit);
	}
	public void setStr(String name,String str) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		Type.put(x, "str");Str.put(x, str);
	}
	public void setProce(String name,String str) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		ProceID.put(x, ProceCount++);Proce.add(str);
	}
	public void setFunct(String name,String str[]) {
		int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}
		else x=NameID.get(i);
		FunctID.put(x, FunctCount++);Funct.add(str);
	}
	//get
	public String getType(String name){return Type.get(NameID.get(Name.indexOf(name)));}
	public Boolean getBool(String name){return Bool.get(NameID.get(Name.indexOf(name)));}
	public Double getDigit(String name){return Digit.get(NameID.get(Name.indexOf(name)));}
	public String getStr(String name){return Str.get(NameID.get(Name.indexOf(name)));}
	public String getProce(String name){return Proce.get(ProceID.get(NameID.get(Name.indexOf(name))));}
	public String[] getFunct(String name){return Funct.get(FunctID.get(NameID.get(Name.indexOf(name))));}
	//其它
	public boolean isHave(String name) {
		int i=0;while(i<Name.size()&&!(Name.get(i).equals(name)||
				Name.get(i).length()>name.length()&&Name.get(i).indexOf(name)==0&&
				Name.get(i).charAt(name.length())=='.'))i++;
		if(i==Name.size())return false;
		else return true;
	}
	public void reMove(String name) {
		for(int i=0;i<Name.size();i++) {
			if(Name.get(i).equals(name)||Name.get(i).length()>name.length()&&
			   Name.get(i).indexOf(name)==0&&Name.get(i).charAt(name.length())=='.') {
				int x=NameID.get(i);
				Name.remove(i);NameID.remove(i);IDBuilder.free(x);
				Type.remove(x);Bool.remove(x);Digit.remove(x);Str.remove(x);
				ProceID.remove(x);FunctID.remove(x);
				i--;
			}
		}
	}
	public void rePlace(String target,String source) {
		if(isHave(target)&&isHave(source)) {
			reMove(target);String STRTEMP;
			for(int i=0;i<Name.size();i++) {
				String str=Name.get(i);int x=NameID.get(i);
				if(str.equals(source)||str.length()>source.length()&&
				   str.indexOf(source)==0&&str.charAt(source.length())=='.') {
					if(str.equals(source))STRTEMP=target;
					else STRTEMP=target+str.substring(source.length());
					int y=IDBuilder.get();Name.add(STRTEMP);NameID.add(y);
					Type.put(y, Type.get(x));Bool.put(y, Bool.get(x));Digit.put(y, Digit.get(x));
					Str.put(y, Str.get(x));ProceID.put(y, ProceID.get(x));FunctID.put(y, FunctID.get(x));
				}
			}
		}
	}
	//执行队列
	private ArrayList<String> RunList=new ArrayList<String>();
	public String Run(){
		String str=null;
		if(RunList.size()>0){str=RunList.get(0);RunList.remove(0);}
		return str;
	}
	public void RunAddHead(ArrayList<String> runlist){RunList.addAll(0, runlist);}
	public void RunAddTail(ArrayList<String> runlist){RunList.addAll(runlist);}
}
