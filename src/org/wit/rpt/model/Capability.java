package org.wit.rpt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class Capability {
	//C(A, InCts, P, OutCts)//
    //C(alist,prelist,process,efflist)//
	//可以考虑用Owl-s中的AtomicProcess和CompositeProcess描述//
	String name;
    OntModel ccm;
	String fpcs = "file:D://GCCModel//contextstates1.owl";
	ContextStateModel csm = new ContextStateModel (fpcs);
    private Individual csi;
    
    public Capability(CapabilityModel cmi, String sname){
    	ccm = cmi.getCCModel();
    	name = sname;
    	csi = ccm.getIndividual(CapabilityModel.ccURI+name);	
    }
    
    public static void main(String[] args){
    	String fpcc = "file:D://GCCModel//CCmodel.owl";
    	CapabilityModel cm = new CapabilityModel(fpcc);
    	Capability c1 = new Capability(cm, "deliver_agv_call");
    	ArrayList<ContextState> ic = c1.getInConstraints();
    	System.out.println("InConstraints of "+c1.getName()+" is:");
    	for(int i=0;i<ic.size();i++){
    		ContextState cs = ic.get(i);
    		System.out.println(cs.getName());
    	}
    	ArrayList<ContextState> oc = c1.getOutConstraints();
    	System.out.println("OutConstraints of "+c1.getName()+" is:");
    	for(int i=0;i<oc.size();i++){
    		ContextState cs = oc.get(i);
    		System.out.println(cs.getName());
    	}
    } 
    
	public String getName(){
		return name;
	}
    
    public OntResource getAgents(){
        Property hasActors = ccm.getProperty(CapabilityModel.ccURI+"hasActors");
        NodeIterator ni = csi.listPropertyValues(hasActors);
        OntResource agent = null;
		while(ni.hasNext()){
			agent =  (OntResource) ni.next();
		}
		return agent;
    }
    
    public ArrayList<ContextState> getInConstraints(){
      	ArrayList<ContextState> InConstraints = new ArrayList();		
    	Property ahp = ccm.getProperty(CapabilityModel.ccURI+"hasInConstraints");
		for(NodeIterator pcs = ccm.listObjectsOfProperty(csi,ahp);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csm, r.getLocalName());
			InConstraints.add(cs);
        }
		return InConstraints;
    }
    
    public ArrayList<ContextState> getOutConstraints(){
    	ArrayList<ContextState> OutConstraints = new ArrayList();
    	Property ahe = ccm.getProperty(CapabilityModel.ccURI+"hasOutConstraints");
		for(NodeIterator pcs = ccm.listObjectsOfProperty(csi,ahe);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csm, r.getLocalName());
			OutConstraints.add(cs);
        }
		return OutConstraints;
    }
    
    public void setInConstraints(ArrayList InConstraints){
    	
    }
    
    public void setOutConstraints(ArrayList OutConstraints){
    	
    }
    
	//获得InConstraints的数量
	public int getInConstraintsNumber() {
		int count=0;
    	Property ahp = ccm.getProperty(CapabilityModel.ccURI+"hasInConstraints");
		for(NodeIterator pcs = ccm.listObjectsOfProperty(csi,ahp);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csm, r.getLocalName());
			count++;
        }
		return count;
	}
    /**
	 * @author Lishuang
	 * 重写equals方法
	 */
	public boolean equals(Object obj) {
		  if (obj instanceof Capability) {
			  Capability t = (Capability) obj;
		   return this.name.equals(t.name);
		  }
		  return super.equals(obj);
		 }

}