import java.util.ArrayList;


public class Main {

	public static void main(String[] args){
		Concrete1 c = new Concrete1();
		
		Abstract a = new Concrete1();
		Abstract a2 = new Concrete2();
		
		ArrayList<Abstract> list = new ArrayList();
		
		list.add(a);
		list.add(a2);
	}
}
