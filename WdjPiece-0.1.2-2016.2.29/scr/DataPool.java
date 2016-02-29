import java.util.HashMap;
public class DataPool {
	HashMap<String,String> DPtype=new HashMap<String, String>();
	HashMap<String,Boolean> DPbool=new HashMap<String, Boolean>();
	HashMap<String,Double> DPdigit=new HashMap<String, Double>();
	HashMap<String,String> DPstr=new HashMap<String, String>();
	
	void setbool(String name,Boolean bool){DPtype.put(name, "bool");DPbool.put(name, bool);}
	void setdigit(String name,Double digit){DPtype.put(name, "digit");DPdigit.put(name, digit);}
	void setstr(String name,String str){DPtype.put(name, "str");DPstr.put(name, str);}
	
	String gettype(String name){return DPtype.get(name);}
	Boolean getbool(String name){return DPbool.get(name);}
	Double getdigit(String name){return DPdigit.get(name);}
	String getstr(String name){return DPstr.get(name);}
	
	void remove(String name){DPtype.remove(name);DPbool.remove(name);DPdigit.remove(name);DPstr.remove(name);}
	void clear(){DPtype.clear();DPbool.clear();DPdigit.clear();DPstr.clear();}
}
