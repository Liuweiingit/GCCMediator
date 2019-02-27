package org.wit.rpt.swrl;

import java.awt.Label;
import org.wit.rpt.model.CapabilityModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.sun.org.apache.xml.internal.utils.URI;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.bridge.Argument;
import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.AtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.IndividualPropertyAtomImpl;


public class IndividualPropertyAtom{
	private String name;
	private String propertyPredicate;
	private String argument1;
	private String argument2;
	OntModel m;
	
	public IndividualPropertyAtom(String name,String propertyPredicate,String argument1,String argument2) {
	    this.name = name;
        this.propertyPredicate = propertyPredicate; 
        this.argument1 = argument1;
        this.argument2 = argument2;
	}
	
//	 public  ObjectProperty createObjectProperty(String uri) {
//		 OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
//					null);
//		 return m.createObjectProperty(uri);
//		 }
	
	public String getName(){
		return name;
	}
	public void setPropertyPredicate(String propertyPredicate){
		this.propertyPredicate = propertyPredicate;
	}
	public void setArgument1(String argument1){
		this.argument1 = argument1; 
	}
	
	public void setArgument2(String argument2) { 
		this.argument2 = argument2; 
	}
	
	public String getpropertyPredicate() { 
		return propertyPredicate; 
	}    
	public String getArgument1() { 
		return argument1;
	}
	public String getArgument2() { 
		return argument2; 
	}  

	public String toString() { 
		return getpropertyPredicate() + "(" + getArgument1() + ", " + getArgument2() + ")"; 
	} 
}


