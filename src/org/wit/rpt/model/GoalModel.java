package org.wit.rpt.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.wit.rpt.semantic.SimilarityDegree;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class GoalModel {
	static OntModel gm;
	static String fpcc = "file:D://GCCModel//CCmodel.owl";	
	static String fpscc= "D:\\GCCModel\\CCmodel1.owl";
	static String fpcc1 = "file:D://GCCModel//CCmodel1.owl";
    static String fpcs = "file:D://GCCModel//contextstates1.owl";
	static String fpg = "file:D://GCCModel//goalmodel.owl";
//	static String fpg16= "file:D://GCCModel//goalmodel16.owl";
//	static String fpg32= "file:D://GCCModel//goalmodel32.owl";
	static String fpd = "file:D://GCCModel//Domain.owl";
    public static String gURI= "http://www.owl-ontologies.com/Ontology1512699842.owl#";
    public static String domainURI= "http://www.owl-ontologies.com/AGVs.owl#";

    public static void main(String[] args){
	        ContextStateModel csmi = new ContextStateModel (fpcs);
		    GoalModel gm = new GoalModel(fpg);
		    OntModel dm = csmi.getDomainmodel(fpd);
	    	CapabilityModel cm = new CapabilityModel(fpcc);  
	    	CapabilityModel cm1 = new CapabilityModel(fpcc1); 
	    	Goal g = new Goal(gm, "move_to_pickup"); 
	    	Capability c = new Capability(cm, "move_to_pickup"); 
	    	System.out.println(matchedByCapabilities(g, cm));
	    	currentMatchingbyWOCC(gm,  cm);
	    	currentMatchingbyWCC(gm, cm, fpscc);
	    	currentMatchingbyWCC1(gm, cm1);
		     
    }
    
	static void print(ArrayList<ContextState> iclofc){
		 for(int i=0; i<iclofc.size();i++){
			 ContextState cs = iclofc.get(i);
			 System.out.println(cs.getName());
	     }
	}
	
    public GoalModel(){
    	gm= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
				null);
		gm.read(fpg);		
    }
    
    public GoalModel(String fpg){
    	gm= ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
				null);
		gm.read(fpg);		
    }
    
	public static  OntModel getModel(){
	    	return gm;
	}
	
	public static void currentMatchingbyWOCC(GoalModel gm, CapabilityModel cm){
		System.out.println("this is the result in WOCC:");
		double gnum = 0;
		long start = System.currentTimeMillis();
    	OntModel gowl = getModel();
  	    OntClass goal =  gowl.getOntClass(gURI+"Goal");
  	    ArrayList<Goal> sglist = new ArrayList<Goal>();
  	    for (ExtendedIterator eiog = goal.listInstances(); eiog.hasNext();) { 
  	      gnum = gnum+1;
  	      boolean r = false;
  		  Individual ig = (Individual) eiog.next(); 
  		  Goal g = new Goal(gm, ig.getLocalName()); 
  		  System.out.println(ig.getLocalName());
  		  r = matchedByCapabilities(g, cm);  
  		  if(r){
  			  sglist.add(g);
  		  }
  		  if(!r){
  			 System.out.println(g.getName()+" can not be matched"); 
  			 System.out.println();
  		  }
  	    } 
   	    long end  = System.currentTimeMillis();
   	    System.out.println("耗时:"+(end-start));
   	    System.out.println(sglist.size()+","+gnum); 
   	    System.out.println("PMG is " +(double)sglist.size() / gnum);
	}
	
	//fpscc是生成的commitment保存的capabilitymodel的文件
	public static void currentMatchingbyWCC(GoalModel gm, CapabilityModel cm, String fpscc){
		fpscc = fpscc;
		System.out.println("this is the result in WCC:");
		double gnum = 0;
		long start = System.currentTimeMillis();
    	OntModel gowl = getModel();
  	    OntClass goal =  gowl.getOntClass(gURI+"Goal");
  	    ArrayList<Goal> sglist = new ArrayList<Goal>();
  	    for (ExtendedIterator eiog = goal.listInstances(); eiog.hasNext();) { 
  	      gnum = gnum+1;
  	      boolean r = false;
  		  Individual ig = (Individual) eiog.next(); 
  		  Goal g = new Goal(gm, ig.getLocalName()); 
  		  r = matchedByCapabilities(g, cm);
  		  if(r){
  			  sglist.add(g);
  		  }
//  		  如果g不能被capability匹配，r为false
  		  if(!r){
  			r = matchedByCommitments(g, cm, fpscc);
  			if(r){
  				 sglist.add(g);
  			}
  		  }
  		  if(!r){
  			 System.out.println("Goal: "+g.getName()+"can not be matched by any capability or commitment"); 
  			 System.out.println();
  		  }
  	    }
		try {
			cm.saveCCModel(fpscc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }  
   	    long end  = System.currentTimeMillis();
   	    System.out.println("耗时:"+(end-start));
   	    System.out.println(sglist.size()+","+gnum); 
   	    System.out.println((double)sglist.size() / gnum);
	}
	
	public static void currentMatchingbyWCC1(GoalModel gm, CapabilityModel cm1){
		System.out.println("this is the result in WCC with exist commitments:");
		double gnum = 0;
		long start = System.currentTimeMillis();
    	OntModel gowl = getModel();
  	    OntClass goal =  gowl.getOntClass(gURI+"Goal");
  	    ArrayList<Goal> sglist = new ArrayList<Goal>();
  	    for (ExtendedIterator eiog = goal.listInstances(); eiog.hasNext();) { 
  	      gnum = gnum+1;
  	      boolean r = false;
  		  Individual ig = (Individual) eiog.next(); 
  		  Goal g = new Goal(gm, ig.getLocalName()); 
  		  r = matchedByCapabilities(g, cm1);
  		  if(r){
  			  sglist.add(g);
  		  }
//  		  如果g不能被capability匹配，r为false
  		  if(!r){
  			r = matchedByECommitments(g, cm1);
  			if(r){
  				 sglist.add(g);
  			}
//  			else if(!r){
//  				r = matchedByCommitments(g, cm1, fpscc);
//  				if(r){
//  	  				 sglist.add(g);
//  	  			}
//  			}
  		  }
  		  if(!r){
  			 System.out.println("Goal: "+g.getName()+" can not be matched by any capability or commitment"); 
  			 System.out.println();
  		  }
  	    }
   	    long end  = System.currentTimeMillis();
   	    System.out.println("耗时:"+(end-start));
   	    System.out.println(sglist.size()+","+gnum); 
   	    System.out.println((double)sglist.size() / gnum);
	}
	
	public static boolean matchedByCapabilities(Goal g, CapabilityModel cm){ 
			 boolean r = false;
			 double tsim = 0;
		     OntClass capa =  cm.getCCModel().getOntClass(CapabilityModel.ccURI+"Capability");
		     for (ExtendedIterator eioc = capa.listInstances(); eioc.hasNext();) {  
		        Individual ic = (Individual) eioc.next();  
		        Capability c = new Capability(cm, ic.getLocalName());	
		        if(CheckMatchingCapability(g, c)) {
		        	r = true;
		        	g.setState("Active");
		            double simca = SimilarityDegree.getSimilarityDegree(g, c);
		            if(simca>tsim){
		            	tsim = simca;
		            	g.setMatchedCapability(c);
		            }
		            System.out.println("Capability:"+c.getName()+"能匹配"+"Goal:"+g.getName()+" ,sim:"+simca);
//		            System.out.println(g.getMatchedCapability().getName());
		            System.out.println();
		       }
		     }
		     return r;
	}
	
	public static Capability matchedByCapabilities1(Goal g, CapabilityModel cm){ 
		 Capability mc = null;
		 double tsim = 0;
	     OntClass capa =  cm.getCCModel().getOntClass(CapabilityModel.ccURI+"Capability");
	     for (ExtendedIterator eioc = capa.listInstances(); eioc.hasNext();) {  
	        Individual ic = (Individual) eioc.next();  
	        Capability c = new Capability(cm, ic.getLocalName());
//	        System.out.println(ic.getLocalName());
	        if(CheckMatchingCapability(g, c)) {
	        	mc = c;
	        	g.setState("Active");
	            double simca = SimilarityDegree.getSimilarityDegree(g, c);
	            System.out.println("Capability:"+c.getName()+"能匹配"+"Goal:"+g.getName()+" ,sim:"+simca);
	            if(simca>tsim){
	            	tsim = simca;
	            	g.setMatchedCapability(c);
	            }
//	            System.out.println("Capability:"+c.getName()+"能匹配"+"Goal:"+g.getName()+" ,sim:"+simca);
	          
	       }
	     }
	     if(g.getMatchedCapability()!=null){
		     System.out.println("last matched capability is "+g.getMatchedCapability().getName());
	         System.out.println();
	     }
	     return mc;
}
	public static boolean CheckMatchingCapability(Goal g, Capability c){
		        boolean r = false;
				ArrayList tclofg = g.getTrigConditions();
		      	ArrayList fslofg = g.getFinStates();
				ArrayList iclofc = c.getInConstraints();
				ArrayList oclofc = c.getOutConstraints();
				if(ContextStateModel.semanticMatch(iclofc,tclofg)&&ContextStateModel.semanticMatch(fslofg,oclofc)) {
					return r = true;
				}
			    return r;
	}
			
	public static boolean matchedByCommitments(Goal g, CapabilityModel cmi, String fpscc){
		    	  boolean r = false;
		    	  double tsim = 0;
		  		  ArrayList<Commitment> colist = CapabilityModel.genterateCommitments(g, cmi, fpscc);
		  		  if(colist.size()!=0){
		  			r = true;
		  	        for (int i=0;i<colist.size();i++) {
		  	    		g.setState("Active");	
		                Commitment coi = (Commitment) colist.get(i);
		                double simco = SimilarityDegree.getSimilarityDegree(g, coi);
		                System.out.println("Commitment:"+coi.getName()+"能匹配"+g.getName()+" ,sim:"+simco);
		                if(simco>tsim){
		                	tsim = simco;
		                	g.setMatchedCommitment(coi);
		                } 
		                System.out.println(g.getMatchedCommitment().getName());
		                System.out.println();
		  	        }
		  	      }
		  		  return r;
	 }
	        
	public static Commitment matchedByCommitments1(Goal g, CapabilityModel cmi, String fpscc){
          Commitment mco = null;
  	      double tsim = 0;
		  ArrayList<Commitment> colist = CapabilityModel.genterateCommitments(g, cmi, fpscc);
		  if(colist.size()!=0){
	        for (int i=0;i<colist.size();i++) {
	    		g.setState("Active");	
                Commitment co = (Commitment) colist.get(i);
                double simco = SimilarityDegree.getSimilarityDegree(g, co);
                System.out.println("Commitment:"+co.getName()+"能匹配"+g.getName()+" ,sim:"+simco);
                if(simco>tsim){
                    mco = co;
              	    tsim = simco;
              	    g.setMatchedCommitment(co);
               } 
              System.out.println(g.getMatchedCommitment().getName());
              System.out.println();
	        }
	      }
		  return mco;
}
	
	 public static boolean matchedByECommitments(Goal g, CapabilityModel cm1){
	        	 boolean r = false;
	        	 double tsim = 0;
	        	 OntModel cmi = cm1.getCCModel();
			     OntClass commit =  cmi.getOntClass(CapabilityModel.ccURI+"Commitment");
			     for (ExtendedIterator eioco = commit.listInstances(); eioco.hasNext();) {  
			       Individual ico = (Individual) eioco.next();  
			       Commitment co = new Commitment(cmi, ico.getLocalName());	
			       String con = co.getName();
			       if(CheckMatchingCommitment(g, co)) {
			         g.setState("Active");
			         r = true;
			         double simco = SimilarityDegree.getSimilarityDegree(g, co);
			         System.out.println("Commitment:"+co.getName()+"能匹配"+g.getName()+" ,sim:"+simco);
		             if(simco>tsim){
		                	tsim = simco;
		                	g.setMatchedCommitment(co);
		              }
//			         System.out.println(g.getMatchedCommitment().getName());
				     System.out.println();
			      }  
			     }
		  		  return r;
	 }
	 public static Commitment  matchedByECommitments1(Goal g, CapabilityModel cm1){
		 Commitment mco = null;
    	 double tsim = 0;
    	 OntModel cmi = cm1.getCCModel();
	     OntClass commit =  cmi.getOntClass(CapabilityModel.ccURI+"Commitment");
	     for (ExtendedIterator eioco = commit.listInstances(); eioco.hasNext();) {  
	       Individual ico = (Individual) eioco.next();  
	       Commitment co = new Commitment(cmi, ico.getLocalName());	
	       String con = co.getName();
	       if(CheckMatchingCommitment(g, co)) {
	         g.setState("Active");
	         double simco = SimilarityDegree.getSimilarityDegree(g, co);
	         System.out.println("Commitment:"+co.getName()+"能匹配"+g.getName()+" ,sim:"+simco);
             if(simco>tsim){
            	    mco = co;
                	tsim = simco;
                	g.setMatchedCommitment(co);
              }
	         System.out.println("the last matched commitment is  :"+g.getMatchedCommitment().getName());
		     System.out.println();
	      }  
	     }
  		  return mco;
     }
	 
     private static boolean CheckMatchingCommitment(Goal g, Commitment co) {
				boolean r= false;
				ArrayList tclofg = g.getTrigConditions();
		      	ArrayList fslofg = g.getFinStates();
				ArrayList iclofc = co.getAntecedent();
				ArrayList oclofc = co.getConsequent();
				if(ContextStateModel.semanticMatch(iclofc,tclofg)&&ContextStateModel.semanticMatch(fslofg,oclofc)) return true;
			    else return false;
	}
           
    public static void saveGoalModel( ) throws IOException {
    	File f = new File(fpg);
    	FileOutputStream file = new FileOutputStream(f);
        gm.write(file, "RDF/XML-ABBREV");
        file.close();
        System.out.println("save is ok");
    }
    
    public void addmatchedBy(String goalname, String  capaname) throws IOException {
		OntModel m = getModel();
		Individual agenti = m.getIndividual(gURI + goalname);
		Individual aci = m.getIndividual(CapabilityModel.ccURI + capaname);
		ObjectProperty matchedBy = m.getObjectProperty(gURI + "matchedBy");
		agenti.addProperty(matchedBy, aci);
		saveGoalModel ();
	}

}
    

