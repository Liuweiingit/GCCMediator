package org.wit.rpt.model;

import java.io.IOException;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Goal {
//	G(Act, TrigCd, FinSt)
    String name;
    private Individual gi;
	ArrayList<Agent> aal;
	OntModel gom;
    ContextStateModel csm;
//	states of goal include: terminated, active, suspended, failed.
	String state = null;
	Capability mc = null;
	Commitment mco = null;
	String fpcs = "file:D://GCCModel//contextstates1.owl";
	
    public Goal(GoalModel gm,String sname){
    	gom = gm.getModel();
        csm = new ContextStateModel (fpcs);
    	name = sname;
    	gi =gom.getIndividual(GoalModel.gURI+name);
    }
    

	public String getName(){
		return name;
	}
	
	public String getState(){
		return state;
	}
	
	public void setState(String s){
		state = s;		
        ObjectProperty hasState = gom.getObjectProperty(GoalModel.gURI+"hasState");
        Individual sn= gom.getIndividual(s);    
	}
	
	public void addAgent(Agent a){
		aal.add(a);
	}
	
	public void setMatchedCapability(Capability mc){
	    this.mc = mc;
    }
	
	public void setMatchedCommitment(Commitment mco){
	    this.mco = mco;
    }
	
	public Capability getMatchedCapability(){
	    return mc;
    }
	
	public Commitment getMatchedCommitment(){
	    return mco;
    }
	
	public ArrayList<ContextState> getTrigConditions(){
	      	ArrayList<ContextState> trigConditions = new ArrayList();		
	    	Property tcp = gom.getProperty(GoalModel.gURI+"hasTriggerConditions");
			for(NodeIterator pcs = gom.listObjectsOfProperty(gi,tcp);pcs.hasNext();){
				Resource r = (Resource) pcs.next();
				ContextState cs = new ContextState(csm, r.getLocalName());
				trigConditions.add(cs);
	        }
			return trigConditions;
	    }
	    
	public ArrayList<ContextState> getFinStates(){
	    	ArrayList<ContextState> OutConstraints = new ArrayList();		
	    	Property fsp = gom.getProperty(GoalModel.gURI+"hasFinalStates");
			for(NodeIterator pcs = gom.listObjectsOfProperty(gi,fsp);pcs.hasNext();){
				Resource r = (Resource) pcs.next();
				ContextState cs = new ContextState(csm, r.getLocalName());
				OutConstraints.add(cs);
	        }
			return OutConstraints;
	    }

	//获得FinStates的数量
	public int getFinStatesNumber() {
		int count=0;
    	Property fsp = gom.getProperty(GoalModel.gURI+"hasFinalStates");
		for(NodeIterator pcs = gom.listObjectsOfProperty(gi,fsp);pcs.hasNext();){
			Resource r = (Resource) pcs.next();
			ContextState cs = new ContextState(csm, r.getLocalName());
			count++;
        }
		return count;
	}
    
    public static void printlist(ArrayList<ContextState> al){
    	for(int i=0; i<al.size();i++){
    		ContextState cs = al.get(i);
    		System.out.println(cs.getName());
    	} 	
    }
}