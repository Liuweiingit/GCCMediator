package experiments;

import java.util.ArrayList;

import org.wit.rpt.model.Capability;
import org.wit.rpt.model.CapabilityModel;
import org.wit.rpt.model.Commitment;
import org.wit.rpt.model.ContextState;
import org.wit.rpt.model.ContextStateModel;
import org.wit.rpt.model.Goal;
import org.wit.rpt.model.GoalModel;

import com.hp.hpl.jena.ontology.OntModel;

public class E1 {
	static String fpcc = "file:D://GCCModel//CCmodel10.owl";	 
	static String fpscc= "D:\\GCCModel\\CCmodel101.owl";
	static String fpcc1 = "file:D://GCCModel//CCmodel101.owl";	
    static String fpcs = "file:D://GCCModel//contextstates1.owl";
	static String fpd = "file:D://GCCModel//Domain.owl";
	static String fpg = "file:D://GCCModel//goalmodel.owl";
	static String fpg18= "file:D://GCCModel//goalmodel18.owl";
	static String fpg36= "file:D://GCCModel//goalmodel36.owl";
	public static String csURI= "http://www.owl-ontologies.com/Ontology1535514050.owl#";
    public static String domainURI= "http://www.owl-ontologies.com/AGVs.owl#";
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		matchingbyWOCC();
//		System.out.println();
		matchingbyWCC();

	}
	
	//没有基于commitment的匹配
	static void matchingbyWOCC(){
	     ContextStateModel csmi = new ContextStateModel (fpcs);
		 GoalModel gmi = new GoalModel(fpg);
		 OntModel dm = csmi.getDomainmodel(fpd);
		 CapabilityModel cmi = new CapabilityModel(fpcc);
	     GoalModel.currentMatchingbyWOCC(gmi, cmi);
	}
	
	//在线生成commitment，预计执行时间较长
	static void matchingbyWCC(){	 
	     ContextStateModel csmi = new ContextStateModel (fpcs);
		 GoalModel gmi = new GoalModel(fpg);
		 OntModel dm = csmi.getDomainmodel(fpd);
		 CapabilityModel cmi = new CapabilityModel(fpcc);
//		 此处fpscc是文件保存格式
	     GoalModel.currentMatchingbyWCC(gmi, cmi, fpscc);
	}
	
	//使用预先生成的commitments，预计执行时间较短
	static void matchingbyWCC1(){	 
	     ContextStateModel csmi = new ContextStateModel (fpcs);
		 GoalModel gmi = new GoalModel(fpg);
		 OntModel dm = csmi.getDomainmodel(fpd);
//		 此处fpscc1是文件调用格式
		 CapabilityModel cmi1 = new CapabilityModel(fpcc1);
	     GoalModel.currentMatchingbyWCC1(gmi, cmi1);
	}
	
	static void print(ArrayList<ContextState> iclofc){
		 for(int i=0; i<iclofc.size();i++){
			 ContextState cs = iclofc.get(i);
			 System.out.println(cs.getName());
	     }
	}
}
