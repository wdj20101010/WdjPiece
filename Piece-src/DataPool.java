import java.util.ArrayList;
import java.util.HashMap;

public class DataPool {
	//名称 身份 和身份制造者
	public ArrayList<String> Name=new ArrayList<String>();
	private ArrayList<Integer> NameID=new ArrayList<Integer>();
	private DataPoolID IDBuilder=new DataPoolID();
	private class DataPoolID {
		private int ID=0;
		private ArrayList<Integer> Free=new ArrayList<Integer>();
		private boolean IDorFree=true;
		private int get() {
			if(Free.size()==0)IDorFree=true;
			else if(Free.size()==1000)IDorFree=false;
			if(IDorFree){ID++;return ID-1;}
			else {int x=Free.get(0);Free.remove(0);return x;}
		}
		private void free(int id) {
			Free.add(id);
		}
	}
	
	//基本数据
	public static class DataBase {
		private String DBtype="bool";
		private Boolean DBbool=false;
		private Double DBdigit=0.0;
		private String DBstr="";
		
		public String gettype(){return DBtype;}
		public Boolean getbool(){return DBbool;}
		public Double getdigit(){return DBdigit;}
		public String getstr(){return DBstr;}
		
		public void settype(String type){DBtype=type;}
		public void setbool(Boolean bool){DBbool=bool;DBtype="bool";}
		public void setdigit(Double digit){DBdigit=digit;DBtype="digit";}
		public void setstr(String str){DBstr=str;DBtype="str";}
		
		public DataBase NEW() {
			DataBase db=new DataBase();
			db.setbool(this.getbool());db.setdigit(this.getdigit());
			db.setstr(this.getstr());db.settype(this.gettype());
			return db;
		}
	}
	private HashMap<Integer,DataBase> Data=new HashMap<Integer,DataBase>();
	
	public String getType(String name){return Data.get(NameID.get(Name.indexOf(name))).gettype();}
	public Boolean getBool(String name){return Data.get(NameID.get(Name.indexOf(name))).getbool();}
	public Double getDigit(String name){return Data.get(NameID.get(Name.indexOf(name))).getdigit();}
	public String getStr(String name){return Data.get(NameID.get(Name.indexOf(name))).getstr();}
	
	public void setType(String name,String type) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		if(Data.get(x)!=null) Data.get(x).settype(type);
		else {DataBase db=new DataBase();db.settype(type);Data.put(x, db);}
	}
	public void setBool(String name,Boolean bool) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		if(Data.get(x)!=null) Data.get(x).setbool(bool);
		else {DataBase db=new DataBase();db.setbool(bool);Data.put(x, db);}
	}
	public void setDigit(String name,Double digit) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		if(Data.get(x)!=null) Data.get(x).setdigit(digit);
		else {DataBase db=new DataBase();db.setdigit(digit);Data.put(x, db);}
	}
	public void setStr(String name,String str) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		if(Data.get(x)!=null) Data.get(x).setstr(str);
		else {DataBase db=new DataBase();db.setstr(str);Data.put(x, db);}
	}
	
	//执行代码
	private int ProceCount=0,NewProceCount=0,FunctCount=0;
	private ArrayList<String[]> Proce=new ArrayList<String[]>();
	private ArrayList<String[]> NewProce=new ArrayList<String[]>();
	private ArrayList<String[]> Funct=new ArrayList<String[]>();
	private HashMap<Integer,Integer> ProceID=new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> NewProceID=new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> FunctID=new HashMap<Integer,Integer>();
	
	public String[] getProce(String name) {Integer x=ProceID.get(NameID.get(Name.indexOf(name)));
		if(x==null)return null;else return Proce.get(x);
	}
	public String[] getNewProce(String name) {Integer x=NewProceID.get(NameID.get(Name.indexOf(name)));
		if(x==null)return null;else return NewProce.get(x);
	}
	public String[] getFunct(String name) {Integer x=FunctID.get(NameID.get(Name.indexOf(name)));
		if(x==null)return null;else return Funct.get(x);
	}
	
	public void setProce(String name,String str[]) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		ProceID.put(x, ProceCount++);Proce.add(str);
	}
	public void setNewProce(String name,String str[]) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		NewProceID.put(x, NewProceCount++);NewProce.add(str);
	}
	public void setFunct(String name,String str[]) {int i=Name.indexOf(name),x;
		if(i==-1) {x=IDBuilder.get();Name.add(name);NameID.add(x);}else x=NameID.get(i);
		FunctID.put(x, FunctCount++);Funct.add(str);
	}
	
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
		//代码执行函数
		setFunct("runcode",new String[]{"RUNCODE_PARAME",null});
		//类加载执行函数
		setFunct("runclass",new String[]{"JAR_PARAME,CLASS_PARAME",null});
		//外部程序调用函数
		setFunct("runapp",new String[]{"RUNAPP_PARAME",null});
		//程序出口函数
		setFunct("exit",new String[]{null,null});
	}
	
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
				Data.remove(x);ProceID.remove(x);NewProceID.remove(x);FunctID.remove(x);
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
					int y=IDBuilder.get();
					Name.add(STRTEMP);NameID.add(y);
					Data.put(y, Data.get(x).NEW());
					ProceID.put(y, ProceID.get(x));
					NewProceID.put(y, NewProceID.get(x));
					FunctID.put(y, FunctID.get(x));
				}
			}
		}
	}
}
