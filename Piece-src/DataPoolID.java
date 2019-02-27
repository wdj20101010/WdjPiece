import java.util.ArrayList;

public class DataPoolID {
	private int ID=0;
	private ArrayList<Integer> Free=new ArrayList<Integer>();
	private boolean IDorFree=true;
	public int get() {
		if(Free.size()==0)IDorFree=true;
		else if(Free.size()==1000)IDorFree=false;
		if(IDorFree){ID++;return ID-1;}
		else {int x=Free.get(0);Free.remove(0);return x;}
	}
	public void free(int id) {
		Free.add(id);
	}
}
