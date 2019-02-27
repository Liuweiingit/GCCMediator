package org.wit.rpt.model;

import javax.swing.JDialog;


import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class ContextState {
	public static String csURI= "http://www.owl-ontologies.com/Ontology1535514050.owl#";
    public static String domainURI= "http://www.owl-ontologies.com/AGVs.owl#";
    public static String swrlURI="http://www.w3.org/2017/11/swrl#";
    static OntModel csm;
    Individual csi;
    String csname;
    String cstype;
	String predicate;
	String argu1;
	String argu2;
	String classpredicate;
	String iargu;
	private Object csiname;

//"IndividualPropertyAtom";
//"ClassAtom";
    
	public static void main(String[] args) {	
		String fpcs = "file:D://GCCModel//contextstates1.owl";
		ContextStateModel csmi = new ContextStateModel (fpcs);
		ContextState cs1 = new ContextState (csmi, "cs8");
		System.out.println(cs1.getType());
		System.out.println(cs1.getPredicate());
		System.out.println(cs1.getArgu1());
		System.out.println(cs1.getArgu2());
		ContextState cs2= new ContextState(csmi, "cs2");
		System.out.println(cs2.getType());
		System.out.println(cs2.getClassPredicate());
		System.out.println(cs2.getIArgu());
		
	}
    
	public ContextState(ContextStateModel csmi, String name){
		csm= csmi.getCSModel();
		csname = name;
		csi = csm.getIndividual(csURI+name);
	}
	
   static void getAllStatements(ContextStateModel csm){
		StmtIterator ss = csm.getCSModel().listStatements();
		while(ss.hasNext()){
			Statement s= ss.nextStatement();
			Resource sub = s.getSubject();	
			Property pre = s.getPredicate();
			RDFNode obj = s.getObject();
			System.out.print(sub.getLocalName()+",");
			System.out.print(pre.getLocalName()+",");
			System.out.println(obj.toString());
		}
    }
	public String getName(){
		return csname;
	}
	
	public String getType(){
		Property t = csm.getProperty("http://www.owl-ontologies.com/Ontology1535514050.owl#type");
		NodeIterator oi = csm.listObjectsOfProperty(csi, t);
		RDFNode o = null;
		String type = null;
		while(oi.hasNext()){
			o = oi.nextNode();
			break;
		}
		if(o!=null){
		  int leng=o.toString().indexOf("^");
		  type = o.toString().substring(0,leng);
		  
		}else {
			System.out.println("can not identify the cs type!");
		}
		return type;
	}
	
//	IndividualPropertyAtom	
	public String getPredicate(){
		Property pre = csm.getProperty(swrlURI+"propertyPredicate");
		NodeIterator oi = csm.listObjectsOfProperty(csi, pre);
		RDFNode o = null;
		while(oi.hasNext()){
			o = oi.nextNode();
		}
		int leng = o.toString().indexOf("#");
		String prename = o.toString().substring(leng+1, o.toString().length());
		return prename;
	}
	
//	IndividualPropertyAtom	
	public String getArgu1(){
		Property arg1 = csm.getProperty(swrlURI+"argument1");
		NodeIterator oi = csm.listObjectsOfProperty(csi, arg1);
		RDFNode o = null;
		while(oi.hasNext()){
			o = oi.nextNode();
		}
		int leng = o.toString().indexOf("#");
		String agu1name = o.toString().substring(leng+1, o.toString().length());
		return agu1name;
	}
	
//	IndividualPropertyAtom	
	public String getArgu2(){
		Property arg2 = csm.getProperty(swrlURI+"argument2");
		NodeIterator oi = csm.listObjectsOfProperty(csi, arg2);
		RDFNode o = null;
		while(oi.hasNext()){
			o = oi.nextNode();
		}
		int leng = o.toString().indexOf("#");
		String agu2name = o.toString().substring(leng+1, o.toString().length());
		return agu2name;
	}
	
//	ClassAtom		
	public String getIArgu(){
		Property arg1 = csm.getProperty(swrlURI+"argument1");
		NodeIterator oi = csm.listObjectsOfProperty(csi, arg1);
		RDFNode o = null;
		while(oi.hasNext()){
			o = oi.nextNode();
		}
//		String iaguname = o.toString().substring(39);
		int leng = o.toString().indexOf("#");
		String iaguname = o.toString().substring(leng+1, o.toString().length());
		return iaguname;
	}
	
//	ClassAtom		
	public String getClassPredicate(){
		Property cp = csm.getProperty(swrlURI+"classPredicate");
		NodeIterator oi = csm.listObjectsOfProperty(csi, cp);
		RDFNode o = null;
		while(oi.hasNext()){
			o = oi.nextNode();
		}
		int leng = o.toString().indexOf("#");
		String classpredicate = o.toString().substring(leng+1, o.toString().length());
//		String classpredicate = o.toString().substring(39);
		return classpredicate;
	}	
	/**
	 * @author Lishuang
	 * ÷ÿ–¥equals∑Ω∑®
	 */
	public boolean equals(Object obj) {
		  if (obj instanceof ContextState) {
			  ContextState t = (ContextState) obj;
		   return this.csiname.equals(t.csiname);
		  }
		  return super.equals(obj);
		 }

}
