package hello;

import java.util.List;

public class DijiWiseResultsArray {

/*	Changes By Sushant - 04-12-2015
 * ArrayList<?> list;

	public ArrayList getList() {
		return list;
	}

	public void setList(ArrayList list) {
		this.list = list;
	}*/

	List<?> list;

	public List getList() {
		return list;
	}

	public void setList(List<Greeting> arrayGreetings) {
		this.list = arrayGreetings;
	}
}