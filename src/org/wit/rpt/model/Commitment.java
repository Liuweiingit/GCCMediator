package org.wit.rpt.model;

import java.util.ArrayList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class Commitment {
//	Co(Debtor, Creditor, Antecedent, Consequent)
	String coname;
	OntModel ccm;
	ContextStateModel csmi;
    OntModel csm;
    private Individual coi;
    String fpcs = "file:D://GCCModel//contextstates1.owl";
    
    public static void main(String[] args){
    	String fpcc = "file:D://GCCModel//CCmodel1.owl";
    	CapabilityModel cm = new CapabilityModel(fpcc);  
    }
    
	static void print(ArrayList<ContextState> iclofc){
		 for(int i=0; i<iclofc.size();i++){
			 ContextState cs = iclofc.get(i);
			 System.out.println(cs.getName());
	     }
	}
    public Commitment(OntModel cmi, String name){
    	ccm = cmi;
    	csmi = new ContextStateModel (fpcs);
    	csm = csmi.getCSModel();
    	coname = name;
    	coi = ccm.getIndividual(CapabilityModel.ccURI+name);	
    }

	public String getName(){
		return coname;
	}
    
    //未完
    public ArrayList<ContextState> getCreditor(){
        ArrayList agentslist;
        System.out.println(coi.getLocalName());
        Property ahc = ccm.getProperty(CapabilityModel.ccURI+"hasCreditor");
		return null;
    }
    
    public ArrayList<ContextState> getDebtor(){
        ArrayList agentslist;
        System.out.println(coi.getLocalName());
        Property ahc = ccm.getProperty(CapabilityModel.ccURI+"hasDebtor");
		return null;
    }
    
    public ArrayList<ContextState> getAntecedent(){
    	ArrayList<ContextState> InConstraints = new ArrayList();		
    	Property ahp = ccm.getProperty(CapabilityModel.ccURI+"hasAntecedent");
		for(NodeIterator pcs = ccm.listObjectsOfProperty(coi,ahp);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csmi, r.getLocalName());
			InConstraints.add(cs);
//			System.out.println(r.getLocalName());
        }
		return InConstraints;
    }
    
    public ArrayList<ContextState> getConsequent(){
    	ArrayList<ContextState> OutConstraints = new ArrayList();
    	Property ahe = ccm.getProperty(CapabilityModel.ccURI+"hasConsequence");
		for(NodeIterator pcs = ccm.listObjectsOfProperty(coi,ahe);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csmi, r.getLocalName());
			OutConstraints.add(cs);
//			System.out.println(r.getLocalName());
        }
		return OutConstraints;
    }
    
    
    public void setCreditor(Capability creditor){
    	ObjectProperty hasCreditor = ccm.getObjectProperty(CapabilityModel.ccURI+"hasCreditor");
        Individual c= ccm.getIndividual(CapabilityModel.ccURI+creditor.getName());
        coi.addProperty(hasCreditor, c);  	
    }
    
    public void setDebtor(Capability debtor){
    	ObjectProperty hasDebtor = ccm.getObjectProperty(CapabilityModel.ccURI+"hasDebtor");
        Individual d= ccm.getIndividual(CapabilityModel.ccURI+debtor.getName());
        coi.addProperty(hasDebtor, d);  
    }
    

    public void setAntecedent(ArrayList<ContextState> ant){
    	ObjectProperty hasAntecedent = ccm.getObjectProperty(CapabilityModel.ccURI+"hasAntecedent");
    	for(int i=0;i<ant.size();i++){
    		ContextState cs = ant.get(i);
    		Individual csi= csm.getIndividual(CapabilityModel.csURI+cs.getName());
            coi.addProperty(hasAntecedent, csi);
            }
        
    }
    
    public void setConsequent(ArrayList<ContextState> con){
    	ObjectProperty hasConsequent = ccm.getObjectProperty(CapabilityModel.ccURI+"hasConsequence");
    	for(int i=0;i<con.size();i++){
    		ContextState cs = con.get(i);
    		Individual csj= csm.getIndividual(CapabilityModel.csURI+cs.getName());
            coi.addProperty(hasConsequent, csj); 
    	}
    }
    
    //获得Antecedent的数量
  	public int getAntecedentNumber() {
  		int count=0;
      	Property ahp = ccm.getProperty(CapabilityModel.ccURI+"hasAntecedent");
  		for(NodeIterator pcs = ccm.listObjectsOfProperty(coi,ahp);pcs.hasNext();){
  			Resource r = (Resource) pcs.next();
  			ContextState cs = new ContextState(csmi, r.getLocalName());
  			count++;
          }
  		return count;
  	}
      

}
