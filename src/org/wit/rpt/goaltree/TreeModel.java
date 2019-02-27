package org.wit.rpt.goaltree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.wit.rpt.model.ContextStateModel;

public class TreeModel {
	            static List<Node> tree;
	            static String fpcs = "file:D://GCCModel//contextstates1.owl";
	            
		public static void main(String[] args) throws IOException {
			    ContextStateModel csmi = null;
			    List<Node> suplt = new ArrayList<Node>();
			    Node g0 = init();
			    preOrder(suplt, g0, csmi);
			    String m_sentence="G6#G7;G8;G9;G10;G11;G13;G5";
			    System.out.println();
				List<String> m_list=cutstring(m_sentence);
				for(String tmp:m_list){
				    System.out.println(tmp);
//				    Node n = getNode(tmp);
//				    System.out.println(n.gname);
				 }
		}
		
		public static List<String> cutstring(String Stence) {
	         List<String> stringlist=new ArrayList<String>();//用来存储解析出来的元素
		         for(int i=0;i<Stence.length();i++){
		             if(Stence.charAt(i)==';'||Stence.charAt(i)=='#'){ 
		                 String temp="";//存储单词
		                 int wordlength=i;
		                 if(wordlength<3){
		                   String first =Stence.substring(0, wordlength);
		                   stringlist.add(first);
		                   
		                 }
		                 while(wordlength<Stence.length()-1&&Stence.charAt(++wordlength)!=';'){                                	
		                     temp+=Stence.charAt(wordlength);
		                 }
		                 stringlist.add(temp);
		             }
		         }
		         return stringlist;    
	      }

		public static Node init(){
		    tree = new ArrayList<Node>();
	        List<Node> sonList0 = new ArrayList<Node>();
	        List<Node> sonList1 = new ArrayList<Node>();
	        List<Node> sonList2 = new ArrayList<Node>();
	        List<Node> sonList3 = new ArrayList<Node>();
	        List<Node> sonList4 = new ArrayList<Node>();
		    Node g0 = new Node("G0","G2;G1;G3;G4;G5", sonList0);
			Node g1 = new Node("G1","G6#G7", sonList1);
			Node g2 = new Node("G2","G8;G9", sonList2);
			Node g3 = new Node("G3","G10;G11", sonList3);
			Node g4 = new Node("G4","G12|G13", sonList4);
			Node g5 = new Node("G5","", null);
			g5.setGname("upload_cart_to_waste_dump");
			sonList0.add(g1);
			sonList0.add(g2);
			sonList0.add(g3);
			sonList0.add(g4);
			sonList0.add(g5);
			Node g6 = new Node("G6", "", null);
			g6.setGname("get_cart_position");
			Node g7 = new Node("G7", "", null);
			g7.setGname("get_cart_destination");
			Node g8 = new Node("G8", "", null);
			g8.setGname("deliver_call");
			Node g9 = new Node("G9", "", null);
			g9.setGname("select_nearest_AGV");
			Node g10 = new Node("G10", "", null);
			g10.setGname("move_to_pickup");
			Node g11 = new Node("G11", "", null);
			g11.setGname("load_cart");
			Node g12 = new Node("G12","", null);
			g12.setGname("transport_cart_with_elevator");
			Node g13 = new Node("G13","", null);
			g13.setGname("transport_cart_to_destination_on_same_floor");
			sonList1.add(g6);
			sonList1.add(g7);
			sonList2.add(g8);
			sonList2.add(g9);
			sonList3.add(g10);
			sonList3.add(g11);
			sonList4.add(g12);
			sonList4.add(g13);
			g0.setType("notleaf");
			g1.setType("notleaf");
			g2.setType("notleaf");
			g3.setType("notleaf");
			g4.setType("notleaf");
			g5.setType("notleaf");
			g6.setType("leaf");
			g7.setType("leaf");
			g8.setType("leaf");
			g9.setType("leaf");
			g10.setType("leaf");
			g11.setType("leaf");
			g12.setType("leaf");
			g13.setType("leaf");
			tree.add(g0);
			tree.add(g1);
			tree.add(g2);
			tree.add(g3);
			tree.add(g4);
			tree.add(g5);
			tree.add(g6);
			tree.add(g7);
			tree.add(g8);
			tree.add(g9);
			tree.add(g10);
			tree.add(g11);
			tree.add(g12);
			tree.add(g13);
			return g0;
		}
	 
		/**
		 * @throws IOException 
		 * @方法的功能 目标树前序遍历
		 * @返回类型void
		 * @作者liuwei
		 * @日期2018年8月7日
		 */
		public static void preOrder(List<Node> suplt, Node root, ContextStateModel csmi) throws IOException {
			String result = "";
			if(root != null){		
				if(root.sonList != null){
					for (int i = 0; i < root.sonList.size(); i++) {				
						  preOrder(suplt, root.sonList.get(i), csmi);					
					}
				}else if(root.sonList == null){
					Node supn = getSupNode(root);
					if(!suplt.contains(supn)){
                      String fp = supn.flowpress;
                      if(!containSupNode(suplt, supn)){
                          suplt.add(supn);	
                          String or = "|";
                          if(fp.contains(or)){
                        	  String cfp = chooseNode(csmi, fp);
                        	  System.out.print(cfp); 

                          }else{
                          System.out.print(fp);

                          }
                      }else{
                    	  System.out.print(root.name); 

                      } 
                      String sign = getSign(supn);
                      System.out.print(sign);
				    }	
		     	}		
		    }	
		}
		
		//首先需要确定"|"涉及的目标，然后根据csmi判断哪些目标是有效的；
		public static String chooseNode(ContextStateModel csmi, String fp){		
			String cn = "G13";
			return cn;
		}
		public static String getSign(Node supn){
			String sign = "";
            Node supn1 = getSupNode(supn);
            if(supn1!=null){
              String fp1 = supn1.flowpress;
              String supname = supn.name;
              int snl = supname.length();
              int p = fp1.indexOf(supname);
              sign = fp1.substring(p+snl, p+snl+1);
            }
			return sign;
		}
		
		public static boolean containSupNode(List<Node> suplt, Node supn){
			boolean r = false;
			List<Node> slist = supn.sonList;
			for(int i=0;i<suplt.size();i++){
				Node sn = suplt.get(i);
				if(slist.contains(sn)){
					r = true;
					break;
				}			
			}
			return r;
		}
		
		public static Node getNode(String name){
			Node n = null;
			for(int i = 0; i<tree.size();i++){
				Node ni = tree.get(i);
				if(name.equals(ni.name)){
					n = ni;
					break;
				}
			}
			return n;
		}

		public static Node getSupNode(Node n){
			Node supn = null;
			for(int i = 0; i<tree.size();i++){
				Node ni = tree.get(i);
				List<Node> slist = ni.sonList;
				if(slist!=null&&slist.contains(n)){
					supn = ni;
					break;
				}			
			}
			return supn;
		}
}
