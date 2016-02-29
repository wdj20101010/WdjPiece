public class DataBase {
	byte type;
	Boolean DBbool;  //type=2
	Double DBdigit;  //type=1
	String DBstr;    //type=0
	boolean isbool=false,isdigit=false,isstr=false;
	
	void setbool(Boolean bool){isbool=true;type=2;DBbool=bool;}
	void setdigit(Double digit){isdigit=true;type=1;DBdigit=digit;}
	void setstr(String str){isstr=true;type=0;DBstr=str;}
	
	Boolean getbool(){if(isbool)return DBbool;else return false;}
	Double getdigit(){if(isdigit)return DBdigit;else return 0.0;}
	String getstr(){if(isstr)return DBstr;else return "";}
}
