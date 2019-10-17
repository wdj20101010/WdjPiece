public class RunProceThread extends Thread {
	private String Proce;
	public RunProceThread(String proce) {Proce=proce;}
	public void run(){new RunProce(Proce);}
}
