import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class RunCode {
	private DialogEnter dialog1;
	private DialogResult dialog2;
	private DataPool DP=Piece.DP;
	private String ThisPiece;
	private boolean isFunct;
	private ArrayList<String> TempPiece=new ArrayList<String>();
	
	public RunCode(String thispiece,String code,boolean isfunct) {
		ThisPiece=thispiece;isFunct=isfunct;
		try{mainOperate(RunCodeToos.pretreat(code));}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		for(int i=0;i<TempPiece.size();i++)DP.reMove(TempPiece.get(i));
	}
	
	//流程处理
	//处理enter{},处理 result{}
	//处理entfrom(){},处理resulto(){},处理if(){},处理  while(){}
	//处理等式语句
	private void mainOperate(String str)throws Exception{
		char ch;String str1="",str2,str3,str4;int 括号;
		if(str.length()>0)str=str+' ';
		for(int i=0;i<str.length();i++){
			if((ch=str.charAt(i))!=' ')str1=str1+ch;
		    //处理enter{},处理 result{}
			else if(str1.indexOf("enter{")==0||str1.indexOf("result{")==0){
				if(str1.indexOf("enter{")==0)i=i-str1.length()+6;
				else i=i-str1.length()+7;
				//读取{}中的语句
				括号=1;str2="";
				while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str2=str2+ch;}
		        if(括号!=0){
		        	if(str1.indexOf("enter{")==0)throw new Exception("\nenter{}语句错误\n"+str1);
		        	else if(str1.indexOf("result{")==0)throw new Exception("\nresult{}语句错误\n"+str1);
		        }
				//enter{}语句执行
		        if(str1.indexOf("enter{")==0){
		        	try{
				        dialog1=new DialogEnter(str2);dialog1.setVisible(true);
				        if(dialog1.输入内容.length()>0)mainOperate(RunCodeToos.pretreat(dialog1.输入内容));
		        	}
		    		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		    		finally{dialog1.dispose();}
		        }
		        //result{}语句执行
		        else if(str1.indexOf("result{")==0){
			        String 结果="";str2=str2+' ';str3="";
			        for(int j=0;j<str2.length();j++)
			        	if((ch=str2.charAt(j))!=' ')str3=str3+ch;
			        	else{
			        		str3=RunCodeToos.thisSuperIndex(str3,ThisPiece);
			        		if(DP.getType(str3)==null)throw new Exception("\nresult{}语句错误\n未知变量: "+str3);
			        		else if(DP.getType(str3).equals("bool"))结果=结果+str3+"="+DP.getBool(str3)+"\n";
			        		else if(DP.getType(str3).equals("digit"))结果=结果+str3+"="+DP.getDigit(str3)+"\n";
			        		else if(DP.getType(str3).equals("str"))结果=结果+str3+"=\""+DP.getStr(str3)+"\"\n";
			        		str3="";
			        	}
			        dialog2=new DialogResult(结果);dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			        dialog2.setVisible(true);
		        }
		        str1="";
			}
		    //处理entfrom(){},处理 resulto(){},处理if(){},处理  while(){}
			else if(str1.indexOf("entfrom(")==0||str1.indexOf("resulto(")==0||str1.indexOf("if(")==0||str1.indexOf("while(")==0){
				if(str1.indexOf("entfrom(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("resulto(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("if(")==0)i=i-str1.length()+3;
				else i=i-str1.length()+6;
			    //读取()中语句和{}中语句
				括号=1;str2="";
			    while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='(')括号++;else if(ch==')')括号--;if(括号!=0)str2=str2+ch;}
		        if(括号!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}语句错误\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}语句错误\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}语句错误\n"+str1);
		        	else throw new Exception("\nwhile(){}语句错误\n"+str1);
		        }
			    i++;括号=1;str3="";
			    while(i<str.length()&&括号!=0){ch=str.charAt(i++);if(ch=='{')括号++;else if(ch=='}')括号--;if(括号!=0)str3=str3+ch;}
			    if(括号!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}语句错误\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}语句错误\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}语句错误\n"+str1);
		        	else throw new Exception("\nwhile(){}语句错误\n"+str1);
		        }
		        //entfrom(){}语句执行
		        if(str1.indexOf("entfrom(")==0){
		        	int chx;str3=str3+' ';str4="";String str5="",str6="";
		        	FileReader filein=new FileReader(str2);
		        	while((chx=filein.read())!=-1)str4=str4+(char)chx;filein.close();
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str5=str5+ch;
		        	    else{
		        	    	str5=RunCodeToos.thisSuperIndex(str5,ThisPiece);
				            for(int k=0;k<str4.length();k++)
				        	    if((ch=str4.charAt(k))!='\n')str6=str6+ch;
				        	    else{
			        	    		if(str6.indexOf('\"')!=-1&&str6.indexOf('\"')==str6.lastIndexOf('\"')){
			        	    			str6=str6+"\n";
			        	    			while(k+1<str4.length()&&(ch=str4.charAt(++k))!='\"')str6=str6+ch;
			        	    			str6=str6+"\"";k++;
			        	    		}
				        	    	if(str6.substring(0,str6.indexOf("=")).equals(str5))
				        	    		RunCodeToos.equate(str5,str6.substring(str6.indexOf("=")+1),ThisPiece);
				        	    	str6="";
				        	    }
		        	    	str5="";
		        	    }
		        }
		        //resulto(){}语句执行
		        else if(str1.indexOf("resulto(")==0){
				    String 结果="";str3=str3+' ';str4="";
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str4=str4+ch;
		        	    else{
		        	    	str4=RunCodeToos.thisSuperIndex(str4,ThisPiece);
		        		    if(DP.getType(str4)==null)throw new Exception("\nresulto(){}语句错误\n未知变量: "+str4);
		        		    else if(DP.getType(str4).equals("bool"))结果=结果+str4+"="+DP.getBool(str4)+"\n";
		        		    else if(DP.getType(str4).equals("digit"))结果=结果+str4+"="+DP.getDigit(str4)+"\n";
		        		    else if(DP.getType(str4).equals("str"))结果=结果+str4+"=\""+DP.getStr(str4)+"\"\n";
		        		    str4="";
		        	    }
		            FileWriter fileout=new FileWriter(str2);
	    		    fileout.write(结果);fileout.close();
		        }
    		    //if(){}语句执行
		        else if(str1.indexOf("if(")==0){DataPool.DataBase XYZ=RunCodeToos.base(str2,ThisPiece);if(XYZ.getbool())mainOperate(str3);}
				//while(){}语句执行
		        else{DataPool.DataBase XYZ=RunCodeToos.base(str2,ThisPiece);while(XYZ.getbool()){mainOperate(str3);XYZ=RunCodeToos.base(str2,ThisPiece);}}
		        str1="";
			}
		    //处理等式语句
			else if(str1.length()>0){
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("\n语句错误\n"+str1);
				else{
					str2=str1.substring(0, str1.indexOf('='));str3=str1.substring(str1.indexOf('=')+1);str1="";
					//双引号" "处理
					if(str3.indexOf("\"")!=-1&&str3.indexOf("\"")==str3.lastIndexOf("\"")){
						while(i<str.length()&&(ch=str.charAt(i))!='\"'){str3=str3+ch;i++;}
						str3=str3+"\"";
					}
					//标记TempPiece
					str4="";
					if(str2.indexOf('.')!=-1)str4=str2.substring(0, str2.indexOf('.'));
					if(str4.length()>0&&!DP.isHave(str4))TempPiece.add(str2);
					else if(str4.length()==0&&!DP.isHave(str2))TempPiece.add(str2);
					//等式处理
					if(str2.equals("this")&&isFunct){
						RunCodeToos.equate("DP_PIECE_TEMP",str3,ThisPiece);
						DP.setBool(ThisPiece, DP.getBool("DP_PIECE_TEMP"));
						DP.setDigit(ThisPiece, DP.getDigit("DP_PIECE_TEMP"));
						DP.setStr(ThisPiece, DP.getStr("DP_PIECE_TEMP"));
						DP.setType(ThisPiece, DP.getType("DP_PIECE_TEMP"));
					}
					else RunCodeToos.equate(str2,str3,ThisPiece);
				}
			}
		}
	}
}
