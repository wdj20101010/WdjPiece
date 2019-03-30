import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JOptionPane;

public class RunClassThread extends Thread {
	String JarFile,ClassName;
	public RunClassThread(String jarfile,String classname) {
		JarFile=jarfile;ClassName=classname;
	}
	@SuppressWarnings("resource")
	public void run() {
		try {
			URLClassLoader loader=new URLClassLoader(new URL[] {new File(JarFile).toURI().toURL()});
			Class<?> myclass=loader.loadClass(ClassName);
			Method mymethod=myclass.getMethod("main",String[].class);
			mymethod.invoke(null,(Object)new String[]{});
		}
		catch(Exception ex) {JOptionPane.showMessageDialog(null, ex);}
	}
}
