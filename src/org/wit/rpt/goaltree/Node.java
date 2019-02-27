package org.wit.rpt.goaltree;

import java.util.List;

public class Node {
 	protected String name;
 	public String gname;
 	public String flowpress;
 	public List<Node> sonList;
	public String type;
	public Node(String name, String flowpress,List<Node> sonList){
		this.name = name;
		this.flowpress = flowpress;
		this.sonList = sonList;
	}
	
	public void setGname(String gname){
		this.gname = gname;
	}
	
	public String getGname(){
		return gname;
	}
	
	public void setfp(String flowpress){
		this.flowpress = flowpress;
	}
	
	public String getfp(){
		return flowpress;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
	
}

