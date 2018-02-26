import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Wdj1 {
	
	private DialogEnter dialog1;
	private DialogResult dialog2;
	private DataPool DP;
	public String ThisPiece;
	private boolean IsProce;
	private Wdj2 wdj;
	private ArrayList<String> TempPiece;
	
	//ÓÃÓÚproce{}ºÍfunct{}
	Wdj1(DataPool dp,String thispiece,boolean isproce){
		DP=dp;ThisPiece=thispiece;IsProce=isproce;
		wdj=new Wdj2(DP,ThisPiece);
		TempPiece=new ArrayList<String>();
		//»ñÈ¡Ö´ÐÐ´úÂë
		String str=null;
		if(IsProce)str=DP.getproce(ThisPiece);
		else{str=DP.getfunct(ThisPiece)[1];}
		//½øÐÐ´¦Àí
		try{mainOperate(str);}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		//ÒÆ³ýTempPiece
		for(int i=0;i<TempPiece.size();i++)DP.remove(TempPiece.get(i));
	}

	//ÓÃÓÚruncode()
	Wdj1(DataPool dp,String thispiece,String code){
		DP=dp;ThisPiece=thispiece;IsProce=false;
		wdj=new Wdj2(DP,ThisPiece);
		TempPiece=new ArrayList<String>();
		//½øÐÐ´¦Àí
		try{mainOperate(pretreatOperate(code));}
		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		//ÒÆ³ýTempPiece
		for(int i=0;i<TempPiece.size();i++)DP.remove(TempPiece.get(i));
	}
	
	//Á÷³Ì´¦Àí
	//´¦Àíenter{},´¦Àí result{},´¦Àírunnow{},´¦Àírunlater{}
	//´¦Àíentfrom(){},´¦Àí resulto(){},´¦Àíif(){},´¦Àí  while(){}
	//´¦ÀíµÈÊ½Óï¾ä
	private void mainOperate(String str)throws Exception{
		char ch;String str1="",str2,str3,str4;int À¨ºÅ;
		if(str.length()>0)str=str+' ';
		for(int i=0;i<str.length();i++){
			if((ch=str.charAt(i))!=' ')str1=str1+ch;
		    //´¦Àíenter{},´¦Àí result{},´¦Àírunnow{},´¦Àírunlater{}
			else if(str1.indexOf("enter{")==0||str1.indexOf("result{")==0||str1.indexOf("runnow{")==0||str1.indexOf("runlater{")==0){
				if(str1.indexOf("enter{")==0)i=i-str1.length()+6;
				else if(str1.indexOf("result{")==0)i=i-str1.length()+7;
				else if(str1.indexOf("runnow{")==0)i=i-str1.length()+7;
				else if(str1.indexOf("runlater{")==0)i=i-str1.length()+9;
				//¶ÁÈ¡{}ÖÐµÄÓï¾ä
				À¨ºÅ=1;str2="";
				while(i<str.length()&&À¨ºÅ!=0){ch=str.charAt(i++);if(ch=='{')À¨ºÅ++;else if(ch=='}')À¨ºÅ--;if(À¨ºÅ!=0)str2=str2+ch;}
		        if(À¨ºÅ!=0){
		        	if(str1.indexOf("enter{")==0)throw new Exception("\nenter{}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("result{")==0)throw new Exception("\nresult{}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("runnow{")==0)throw new Exception("\nrunnow{}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("runlater{")==0)throw new Exception("\nrunlater{}Óï¾ä´íÎó\n"+str1);
		        }
				//enter{}Óï¾äÖ´ÐÐ
		        if(str1.indexOf("enter{")==0){
		        	try{
				        dialog1=new DialogEnter(str2);dialog1.setVisible(true);
				        if(dialog1.ÊäÈëÄÚÈÝ.length()>0)mainOperate(pretreatOperate(dialog1.ÊäÈëÄÚÈÝ));
		        	}
		    		catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
		    		finally{dialog1.dispose();}
		        }
		        //result{}Óï¾äÖ´ÐÐ
		        else if(str1.indexOf("result{")==0){
			        String ½á¹û="";str2=str2+' ';str3="";
			        for(int j=0;j<str2.length();j++)
			        	if((ch=str2.charAt(j))!=' ')str3=str3+ch;
			        	else{
			        		str3=wdj.thisSuperIndex(str3);
			        		if(DP.gettype(str3)==null)throw new Exception("\nresult{}Óï¾ä´íÎó\nÎ´Öª±äÁ¿: "+str3);
			        		else if(DP.gettype(str3).equals("bool"))½á¹û=½á¹û+str3+"="+DP.getbool(str3)+"\n";
			        		else if(DP.gettype(str3).equals("digit"))½á¹û=½á¹û+str3+"="+DP.getdigit(str3)+"\n";
			        		else if(DP.gettype(str3).equals("str"))½á¹û=½á¹û+str3+"=\""+DP.getstr(str3)+"\"\n";
			        		str3="";
			        	}
			        dialog2=new DialogResult(½á¹û);dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			        dialog2.setVisible(true);
		        }
		        //runnow{}Óï¾äÖ´ÐÐ£¬runlater{}Óï¾äÖ´ÐÐ
		        else if(str1.indexOf("runnow{")==0||str1.indexOf("runlater{")==0){
		        	str2=str2+' ';str3="";
		        	ArrayList<String> runlist=new ArrayList<String>();
		        	for(int j=0;j<str2.length();j++)
			        	if((ch=str2.charAt(j))!=' ')str3=str3+ch;
			        	else{str3=wdj.thisSuperIndex(str3);
			        		if(DP.ishave(str3)&&DP.getproceindex(str3)!=null)runlist.add(str3);
			        	}
		        	if(runlist.size()>0&&str1.indexOf("runnow{")==0)DP.DPrunaddhead(runlist);
		        	else if(runlist.size()>0&&str1.indexOf("runlater{")==0)DP.DPrunaddtail(runlist);
		        }
		        str1="";
			}
		    //´¦Àíentfrom(){},´¦Àí resulto(){},´¦Àíif(){},´¦Àí  while(){}
			else if(str1.indexOf("entfrom(")==0||str1.indexOf("resulto(")==0||str1.indexOf("if(")==0||str1.indexOf("while(")==0){
				if(str1.indexOf("entfrom(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("resulto(")==0)i=i-str1.length()+8;
				else if(str1.indexOf("if(")==0)i=i-str1.length()+3;
				else i=i-str1.length()+6;
			    //¶ÁÈ¡()ÖÐÓï¾äºÍ{}ÖÐÓï¾ä
				À¨ºÅ=1;str2="";
			    while(i<str.length()&&À¨ºÅ!=0){ch=str.charAt(i++);if(ch=='(')À¨ºÅ++;else if(ch==')')À¨ºÅ--;if(À¨ºÅ!=0)str2=str2+ch;}
		        if(À¨ºÅ!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}Óï¾ä´íÎó\n"+str1);
		        	else throw new Exception("\nwhile(){}Óï¾ä´íÎó\n"+str1);
		        }
			    i++;À¨ºÅ=1;str3="";
			    while(i<str.length()&&À¨ºÅ!=0){ch=str.charAt(i++);if(ch=='{')À¨ºÅ++;else if(ch=='}')À¨ºÅ--;if(À¨ºÅ!=0)str3=str3+ch;}
			    if(À¨ºÅ!=0){
		        	if(str1.indexOf("entfrom(")==0)throw new Exception("\nentfrom(){}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("resulto(")==0)throw new Exception("\nresulto(){}Óï¾ä´íÎó\n"+str1);
		        	else if(str1.indexOf("if(")==0)throw new Exception("\nif(){}Óï¾ä´íÎó\n"+str1);
		        	else throw new Exception("\nwhile(){}Óï¾ä´íÎó\n"+str1);
		        }
		        //entfrom(){}Óï¾äÖ´ÐÐ
		        if(str1.indexOf("entfrom(")==0){
		        	int chx;str3=str3+' ';str4="";String str5="",str6="";
		        	FileReader filein=new FileReader(str2);
		        	while((chx=filein.read())!=-1)str4=str4+(char)chx;filein.close();
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str5=str5+ch;
		        	    else{
		        	    	str5=wdj.thisSuperIndex(str5);
				            for(int k=0;k<str4.length();k++)
				        	    if((ch=str4.charAt(k))!='\n')str6=str6+ch;
				        	    else{
			        	    		if(str6.indexOf('\"')!=-1&&str6.indexOf('\"')==str6.lastIndexOf('\"')){
			        	    			str6=str6+"\n";
			        	    			while(k+1<str4.length()&&(ch=str4.charAt(++k))!='\"')str6=str6+ch;
			        	    			str6=str6+"\"";k++;
			        	    		}
				        	    	if(str6.substring(0,str6.indexOf("=")).equals(str5))
				        	    		wdj.equateOperate(str5,str6.substring(str6.indexOf("=")+1));
				        	    	str6="";
				        	    }
		        	    	str5="";
		        	    }
		        }
		        //resulto(){}Óï¾äÖ´ÐÐ
		        else if(str1.indexOf("resulto(")==0){
				    String ½á¹û="";str3=str3+' ';str4="";
		            for(int j=0;j<str3.length();j++)
		        	    if((ch=str3.charAt(j))!=' ')str4=str4+ch;
		        	    else{
		        	    	str4=wdj.thisSuperIndex(str4);
		        		    if(DP.gettype(str4)==null)throw new Exception("\nresulto(){}Óï¾ä´íÎó\nÎ´Öª±äÁ¿: "+str4);
		        		    else if(DP.gettype(str4).equals("bool"))½á¹û=½á¹û+str4+"="+DP.getbool(str4)+"\n";
		        		    else if(DP.gettype(str4).equals("digit"))½á¹û=½á¹û+str4+"="+DP.getdigit(str4)+"\n";
		        		    else if(DP.gettype(str4).equals("str"))½á¹û=½á¹û+str4+"=\""+DP.getstr(str4)+"\"\n";
		        		    str4="";
		        	    }
		            FileWriter fileout=new FileWriter(str2);
	    		    fileout.write(½á¹û);fileout.close();
		        }
    		    //if(){}Óï¾äÖ´ÐÐ
		        else if(str1.indexOf("if(")==0){DataBase XYZ=wdj.baseOperate(str2);if(XYZ.getbool())mainOperate(str3);}
				//while(){}Óï¾äÖ´ÐÐ
		        else{DataBase XYZ=wdj.baseOperate(str2);while(XYZ.getbool()){mainOperate(str3);XYZ=wdj.baseOperate(str2);}}
		        str1="";
			}
		    //´¦ÀíµÈÊ½Óï¾ä
			else if(str1.length()>0){
				if(str1.indexOf("=")<1||str1.indexOf("=")>str1.length()-2)throw new Exception("\nÓï¾ä´íÎó\n"+str1);
				else{
					str2=str1.substring(0, str1.indexOf('='));str3=str1.substring(str1.indexOf('=')+1);str1="";
					//Ë«ÒýºÅ" "´¦Àí
					if(str3.indexOf("\"")!=-1&&str3.indexOf("\"")==str3.lastIndexOf("\"")){
						while(i<str.length()&&(ch=str.charAt(i))!='\"'){str3=str3+ch;i++;}
						str3=str3+"\"";
					}
					//±ê¼ÇTempPiece
					str4="";
					if(str2.indexOf('.')!=-1)str4=str2.substring(0, str2.indexOf('.'));
					if(str4.length()>0&&!DP.ishave(str4))TempPiece.add(str2);
					else if(str4.length()==0&&!DP.ishave(str2))TempPiece.add(str2);
					//µÈÊ½´¦Àí
					if(str2.equals("this")&&!IsProce){
						wdj.equateOperate("DP_PIECE_TEMP",str3);
						DP.setbool(ThisPiece, DP.getbool("DP_PIECE_TEMP"));
						DP.setdigit(ThisPiece, DP.getdigit("DP_PIECE_TEMP"));
						DP.setstr(ThisPiece, DP.getstr("DP_PIECE_TEMP"));
						DP.settype(ThisPiece, DP.gettype("DP_PIECE_TEMP"));
					}
					else wdj.equateOperate(str2,str3);
				}
			}
		}
	}
	
	//Ô¤ÏÈ´¦Àí
	//´úÂë¹æÕû»¯
	private String pretreatOperate(String str)throws Exception{
		if(str.length()==0)return "";
		//È¥³ýÖÆ±í·û
		str=" "+str.replace('\t', ' ')+" ";
		//´¦Àíenter{}Óï¾ä
		int X=-1;
		int A=str.indexOf(" enter "),B=str.indexOf(" enter\n"),C=str.indexOf(" enter{");
		int D=str.indexOf("\nenter "),E=str.indexOf("\nenter\n"),F=str.indexOf("\nenter{");
		if(A>X)X=A;if(B>X)X=B;if(C>X)X=C;if(D>X)X=D;if(E>X)X=E;if(F>X)X=F;
		if(A!=-1&&A<X)X=A;if(B!=-1&&B<X)X=B;if(C!=-1&&C<X)X=C;
		if(D!=-1&&D<X)X=D;if(E!=-1&&E<X)X=E;if(F!=-1&&F<X)X=F;
		if(X!=-1){
			char ch;int Y=X;
			while(Y<str.length()&&(ch=str.charAt(Y++))!='{');
			if(Y==str.length())throw new Exception("\nenter{}Óï¾ä´íÎó\n"+str);
			int Z=Y,À¨ºÅ=1;
			while(Z<str.length()&&À¨ºÅ!=0){ch=str.charAt(Z++);if(ch=='{')À¨ºÅ++;else if(ch=='}')À¨ºÅ--;}
			if(À¨ºÅ!=0)throw new Exception("\nenter{}Óï¾ä´íÎó\n"+str);
			String strx=str.substring(0, X);
			String stry=str.substring(Y, Z-1);
			String strz=str.substring(Z);
			return pretreatOperate(strx)+" enter{"+stry+"} "+pretreatOperate(strz);
		}
		//´¦ÀíË«ÒýºÅ" "Óï¾ä
		if(str.indexOf("\"")!=-1){
			String strx="",stry="",strz="";
			strx=str.substring(0,str.indexOf("\""));
			stry=str.substring(str.indexOf("\"")+1);
			if(stry.indexOf("\"")!=-1){
				strz=stry.substring(stry.indexOf("\"")+1);
				stry=stry.substring(0,stry.indexOf("\""));
			}
			return pretreatOperate(strx)+"\""+stry+"\" "+pretreatOperate(strz);
		}
		//È¥³ý»Ø³µ·ûºÍ×¢ÊÍ
		char ch;String str1="",str2="",str3="";
		if(str.length()>0)str=str+'\n';
		for(int i=0;i<str.length();i++)
			if((ch=str.charAt(i))!='\n')str1=str1+ch;
			else{if(str1.indexOf("//")!=-1)str1=str1.substring(0, str1.indexOf("//"));
				 str2=str2+" "+str1;str1="";}
		//½«Á¬Ðø¿Õ¸ñ±äÎªµ¥Ò»¿Õ¸ñ
		while(str2.indexOf("  ")!=-1)str2=str2.substring(0, str2.indexOf("  "))+" "+str2.substring(str2.indexOf("  ")+2);
		if(str2.equals(" "))str2="";
		if(str2.length()>1&&str2.charAt(0)==' ')str2=str2.substring(1);
        if(str2.length()>1&&str2.charAt(str2.length()-1)==' ')str2=str2.substring(0, str2.length()-1);
		//È¥³ý+ - * / ^ > < = & | ! . , ( [ {Ç°ºóµÄ¿Õ¸ñ  È¥³ý) ] }Ç°µÄ¿Õ¸ñ
		for(int i=0;i<str2.length();i++)
			if(i<str2.length()-1&&str2.charAt(i)==' '&&
			  (str2.charAt(i+1)=='+'||str2.charAt(i+1)=='-'||str2.charAt(i+1)=='*'||str2.charAt(i+1)=='/'||str2.charAt(i+1)=='^'||
			   str2.charAt(i+1)=='>'||str2.charAt(i+1)=='<'||str2.charAt(i+1)=='='||str2.charAt(i+1)=='&'||str2.charAt(i+1)=='|'||
			   str2.charAt(i+1)=='!'||str2.charAt(i+1)=='('||str2.charAt(i+1)==')'||str2.charAt(i+1)=='['||str2.charAt(i+1)==']'||
			   str2.charAt(i+1)=='{'||str2.charAt(i+1)=='}'||str2.charAt(i+1)=='.'||str2.charAt(i+1)==','));
			else if(i<str2.length()-1&&str2.charAt(i+1)==' '&&
				   (str2.charAt(i)=='+'||str2.charAt(i)=='-'||str2.charAt(i)=='*'||str2.charAt(i)=='/'||str2.charAt(i)=='^'||
				    str2.charAt(i)=='>'||str2.charAt(i)=='<'||str2.charAt(i)=='='||str2.charAt(i)=='&'||str2.charAt(i)=='|'||
				    str2.charAt(i)=='!'||str2.charAt(i)=='('||str2.charAt(i)=='['||str2.charAt(i)=='{'||
				    str2.charAt(i)=='.'||str2.charAt(i)==','))str3=str3+str2.charAt(i++);
			else str3=str3+str2.charAt(i);
		return str3;
	}
}
