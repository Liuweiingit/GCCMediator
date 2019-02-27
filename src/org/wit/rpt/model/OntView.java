package org.wit.rpt.model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class OntView {
	static String filePath; 
	OntModel m;
	DefaultTreeModel treeModel;
    public static DefaultMutableTreeNode agent;
	DefaultMutableTreeNode root;
	DefaultMutableTreeNode agv;
	DefaultMutableTreeNode stake;
	DefaultMutableTreeNode lift;
	DefaultListModel listModel;
	static String fpcc = "file:D://GCCModel//CCmodel.owl";
	
	/**
	 * @param args
	 */
	public OntView(){
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OntView otv = new OntView();
		
	}
	
	
	public OntModel getOntModel(){
		return m;
	}
	
	//获取本体中所有内容
	public DefaultTreeModel formOWLtoAll(){
		root = new DefaultMutableTreeNode("Things");
		treeModel = new DefaultTreeModel(root);
		CapabilityModel om = new CapabilityModel(fpcc);
		m = om.getCCModel();
		ArrayList on = new ArrayList(); 
        Iterator<OntClass> i = m.listHierarchyRootClasses()
        .filterDrop(new Filter() {
                      public boolean accept( OntClass r ) {
                          return r.isAnon();
                      }
					@Override
					public boolean accept(Object arg0) {
						// TODO Auto-generated method stub
						return false;
					}});
        DefaultMutableTreeNode agentn = new DefaultMutableTreeNode("Agent");
		root.add(agentn);
	    while(i.hasNext()){
	    	   OntClass oc = i.next();
			   String ocn = oc.getLocalName();
			   ArrayList ons = new ArrayList();
			   ons.add("DomainConcept");
//			   ons.add("ContextState");
			   ons.add("Capability");			  
			   if(ons.contains(ocn)){
				   DefaultMutableTreeNode hierarchyRootnode = new DefaultMutableTreeNode(ocn);
				   root.add(hierarchyRootnode);
				   for(Iterator p=oc.listInstances();p.hasNext();){
						  Individual ii = (Individual) p.next();
						  String iin = ii.getLocalName();
						  DefaultMutableTreeNode insnode = new DefaultMutableTreeNode(iin);
						  hierarchyRootnode.add(insnode);
					  }
				   if(oc.hasSubClass()){
					  Iterator<OntClass> j = oc.listSubClasses();
					  while(j.hasNext()){
						  OntClass oc1 = j.next();
						  String ocsn = oc1.getLocalName();
						  if(!ocsn.equalsIgnoreCase("Agent")){
							 DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(ocsn);
							 hierarchyRootnode.add(subnode);
							 for(Iterator k=oc1.listInstances();k.hasNext();){
								  Individual ii = (Individual) k.next();
								  String iin = ii.getLocalName();
								  DefaultMutableTreeNode insnode = new DefaultMutableTreeNode(iin);
								  subnode.add(insnode);
							 }
							 
						  }else if(ocsn.equalsIgnoreCase("Agent")) {
							Iterator<OntClass> k = oc1.listSubClasses();
							while(k.hasNext()){					
							  OntClass sac = k.next();
							  DefaultMutableTreeNode sanode = new DefaultMutableTreeNode(sac.getLocalName());
							  agentn.add(sanode);
							  for(Iterator l=sac.listInstances();l.hasNext();){
								  Individual ii = (Individual) l.next();
								  String iin = ii.getLocalName();
								  DefaultMutableTreeNode insnode = new DefaultMutableTreeNode(iin);
								  sanode.add(insnode);
							  
							  }
							}  
						  }
						  
					  }
				   }
			   }
			   
	    }
		return treeModel;
        
	}
	
	//获取本体中所有领域概念 
    public DefaultTreeModel formOWLtoClasses(){
    	root = new DefaultMutableTreeNode("Things");
		treeModel = new DefaultTreeModel(root);
		CapabilityModel om = new CapabilityModel(fpcc);
		m = om.getCCModel();
		ArrayList on = new ArrayList(); 
        Iterator<OntClass> i = m.listHierarchyRootClasses()
        .filterDrop(new Filter() {
                      public boolean accept( OntClass r ) {
                          return r.isAnon();
                      }
					@Override
					public boolean accept(Object arg0) {
						// TODO Auto-generated method stub
						return false;
					}});
        DefaultMutableTreeNode agentn = new DefaultMutableTreeNode("Agent");
		root.add(agentn);
        while(i.hasNext()){
	    	   OntClass oc = i.next();
			   String ocn = oc.getLocalName();
			   ArrayList ons = new ArrayList();
			   ons.add("DomainConcept");		  
			   if(ons.contains(ocn)){
				   DefaultMutableTreeNode hierarchyRootnode = new DefaultMutableTreeNode(ocn);
				   root.add(hierarchyRootnode);
				   if(oc.hasSubClass()){
					  Iterator<OntClass> j = oc.listSubClasses();
					  while(j.hasNext()){
						  OntClass oc1 = j.next();
						  String ocsn = oc1.getLocalName();
						  if(!ocsn.equalsIgnoreCase("Agent")){
							 DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(ocsn);
							 hierarchyRootnode.add(subnode);							 
						  }else if(ocsn.equalsIgnoreCase("Agent")) {
							Iterator<OntClass> k = oc1.listSubClasses();
							while(k.hasNext()){					
							  OntClass sac = k.next();
							  DefaultMutableTreeNode sanode = new DefaultMutableTreeNode(sac.getLocalName());
							  agentn.add(sanode);

							}  
						  }
						  
					  }
				   }
			   }
			   
	    }
    	
        return treeModel;
    }
    
}
