package org.wit.rpt.semantic;

import java.util.Iterator;

import org.wit.rpt.model.Capability;
import org.wit.rpt.model.CapabilityModel;
import org.wit.rpt.model.Commitment;
import org.wit.rpt.model.ContextState;
import org.wit.rpt.model.Goal;
import org.wit.rpt.model.GoalModel;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * ����Goal-Capability��Goal-Commitment�����ƶȳ̶� getSimilarityDegree(Goal g,
 * Capability c) getSimilarityDegree(Goal g, Commitment co)
 */

public class SimilarityDegree {
	static String fpcc = "file:D://GCCModel//CCmodel.owl";
	static String fpcc1 = "file:D://GCCModel//CCmodel1.owl";	
	static GoalModel gm = new GoalModel();
	static CapabilityModel cm = new CapabilityModel(fpcc);
	static OntModel com;

	public static void main(String[] args) {
		//Goal-Capability���ƶȼ������ӣ�
		//1��Cap:move_to_elevator & Goal:transport_cart_with_elevator ���==0.66666
		//2��Cap:select_nearest_agvt & Goal:select_nearest_AGV ���==0.880558
		//3��Cap:select_nearest_agv & Goal:select_nearest_AGV ���==1.0
		String goalName = "select_nearest_AGV";
		String capabilityName = "select_nearest_agv";
		//Goal-Commitment���ƶȼ������ӣ�
		//1��Co:move_to_elevator_transport_cart_with_elevator & Goal:transport_cart_with_elevator ���==1.0
		//2��Co:move_to_elevator_transport_cart_with_elevator & Goal:take_off_cart ���==0.33333
		//3��Co:move_to_elevator_transport_cart_with_elevator & Goal:transport_cart_to_destination_on_same_floor ���==0.3333
		String goalNameCo = "get_cart_destination3";
		String commitmentName = "read_cart_tag_detect_cart";
		String commitmentName1 = "detect_cart_read_cart_ta";
		Goal g = new Goal(gm, goalName);
		Capability cap = new Capability(cm, capabilityName);
		String fpcc = "file:D://GCCModel//CCmodel1.owl";
		CapabilityModel cm = new CapabilityModel(fpcc1);
		Commitment co = new Commitment(cm.getCCModel(), commitmentName);
		Commitment co1 = new Commitment(cm.getCCModel(), commitmentName1);
		Goal gco = new Goal(gm, goalNameCo);
		
		// ����Goal-Capability֮������ƶ�
		double simca = getSimilarityDegree(g, cap);
		// ����Goal-Commitment֮������ƶ�
		double simco = getSimilarityDegree(gco, co);
		double simco1 = getSimilarityDegree(gco, co1);
		 System.out.println(simca);
		 System.out.println(simco);
		 System.out.println(simco1);
	}

	/**
	 * ����Goal-Capability֮������ƶ�
	 */
	public static double getSimilarityDegree(Goal g, Capability cap) {
		long start = System.currentTimeMillis();
		double sim = 0.0;
		/*
		 * ���㹫ʽΪ��
		 * sim(G,C)=(sim(InConstraints,triggerConditions)+sim(FinalStates,
		 * OutConstraints))/a(InConstraintNumber)+b(FinalStateNumber)
		 */
		// 1.���㣺sim(InConstraints,triggerConditions)���
		double sim1 = getInCTrigCSimilarity(cap, g);
//		System.out.println("sim1---------------:" + sim1);
		// 2.���㣺sim(FinalStates,OutConstraints)���
		double sim2 = getFinSOutCSimilarity(g, cap);
//		System.out.println("sim2---------------:" + sim2);
		// 3.���㣺a
		int a = cap.getInConstraintsNumber();
//		System.out.println("a is:" + a);
		// 4.���㣺b
		int b = g.getFinStatesNumber();
//		System.out.println("a is:" + b);
		// 5.��ʽ���㣺
		sim = (sim1 + sim2) / (a + b);// Ŀ������������ƶȼ��㹫ʽ
		long end = System.currentTimeMillis();
//		System.out.println("��ʱ:" + (end - start));
//		System.out.print(g.getName() + "��" + cap.getName() + "�����ƶ�Ϊ:" + sim);
//		System.out.println();
		return sim;
	}

	// ����sim1��sim(InConstraints,triggerConditions)���
	public static double getInCTrigCSimilarity(Capability cap, Goal g) {
		double sim = 0.0;
		for (Iterator p = cap.getInConstraints().listIterator(); p.hasNext();) {
			ContextState s1 = (ContextState) p.next();
			for (Iterator p1 = g.getTrigConditions().listIterator(); p1.hasNext();) {
				ContextState s2 = (ContextState) p1.next();
				sim += getContextStateSimilarity(s1, s2);
			}
		}
		return sim;
	}

	// ����sim2��sim(FinalStates,OutConstraints)���
	public static double getFinSOutCSimilarity(Goal g, Capability cap) {
		double sim = 0.0;
		for (Iterator p = g.getFinStates().listIterator(); p.hasNext();) {
			ContextState s1 = (ContextState) p.next();
			for (Iterator p1 = cap.getOutConstraints().listIterator(); p1.hasNext();) {
				ContextState s2 = (ContextState) p1.next();
				sim += getContextStateSimilarity(s1, s2);
			}
		}
		return sim;
	}

	/**
	 * ����Goal-Commitment֮������ƶ�
	 */
	public static double getSimilarityDegree(Goal g, Commitment co) {
		long start = System.currentTimeMillis();
		double sim = 0.0;
		/*
		 * ���㹫ʽΪ�� sim(G,Co)=(sim(Antecedent,triggerConditions)+sim(FinalStates,
		 * Consequent))/m(AntecedentNumber)+n(FinalStateNumber)
		 */
		// 1.���㣺sim(Antecedent,triggerConditions)���
		double sim1 = getAntTrigCSimilarity(co, g);
//		System.out.println("sim1---------------:" + sim1);
		// 2.���㣺sim(FinalStates,Consequent)���
		double sim2 = getFinSConseSimilarity(g, co);
//		System.out.println("sim2---------------:" + sim2);
		// 3.���㣺m
		int m = co.getAntecedentNumber();
//		System.out.println("a is:" + m);
		// 4.���㣺n
		int n = g.getFinStatesNumber();
//		System.out.println("a is:" + n);
		// 5.��ʽ���㣺
		sim = (sim1 + sim2) / (m + n);// Ŀ������������ƶȼ��㹫ʽ
		long end = System.currentTimeMillis();
//		System.out.println("��ʱ:" + (end - start));
//		System.out.print(g.getName() + "��" + co.getName() + "�����ƶ�Ϊ:" + sim);
//		System.out.println();
		return sim;
	}

	// ����sim1��sim(Antecedent,triggerConditions)���
	public static double getAntTrigCSimilarity(Commitment co, Goal g) {
		Commitment comm = new Commitment(cm.getCCModel(), co.getName());
		Goal goal = new Goal(gm, g.getName());
		double sim = 0.0;
		for (Iterator p = comm.getAntecedent().listIterator(); p.hasNext();) {
			ContextState s1 = (ContextState) p.next();
			for (Iterator p1 = goal.getTrigConditions().listIterator(); p1.hasNext();) {
				ContextState s2 = (ContextState) p1.next();
//				System.out.println("AT:  " + s1.getName() + "  ��  " + s2.getName() + " �����ƶ�Ϊ  ");
				sim += getContextStateSimilarity(s1, s2);
			}
		}
		return sim;
	}

	// ����sim2��sim(FinalStates,Consequent)���
	public static double getFinSConseSimilarity(Goal g, Commitment co) {
		Commitment comm = new Commitment(cm.getCCModel(), co.getName());
		GoalModel gm = new GoalModel();
		Goal goal = new Goal(gm, g.getName());
		double sim = 0.0;
		for (Iterator p = goal.getFinStates().listIterator(); p.hasNext();) {
			ContextState s1 = (ContextState) p.next();
			for (Iterator p1 = comm.getConsequent().listIterator(); p1.hasNext();) {
				ContextState s2 = (ContextState) p1.next();
				// System.out.println("FC: "+s1.getName()+"��"+s2.getName()+"
				// �����ƶ�Ϊ
				// "+getContextstateSimilarity(s1.getName(),s2.getName()));
				sim += getContextStateSimilarity(s1, s2);
			}
		}
		return sim;
	}

	// ���㣺����ContextState�����ƶ�(������״̬��SWRL��ʾ��2�ֱ�ʾ)
	public static double getContextStateSimilarity(ContextState cs1, ContextState cs2) {
		double n = 0.0;
		// SWRL��ʾ���ж�ContextState������
		String cs1Type = cs1.getType();
		String cs2Type = cs2.getType();
		// if��cs1 cs2������ͬ=ClassAtom
		if (cs1Type.equals(cs2Type) && cs1Type.equals("ClassAtom")) {
//			System.out.println("1.�����ǣ�ClassAtom");
			String argu11 = cs1.getIArgu();
			String argu12 = cs2.getIArgu();
			String pred1 = cs1.getClassPredicate();
			String pred2 = cs2.getClassPredicate();
			if (pred1.equals(pred2)) {
				double sim;
				sim = RNCVMSimilarity.getRNCVMSimilarity(argu11, argu12);
				if (sim == 1) {
					n = 1.0;
				} else {
					n = sim;
				}
			} else {
				n = 0.0;
			}
		}
		// if��cs1 cs2������ͬ=IndividualPropertyAtom
		else if (cs1Type.equalsIgnoreCase(cs2Type) && cs1Type.equalsIgnoreCase("IndividualPropertyAtom")) {
//			System.out.println("2.�����ǣ�IndividualPropertyAtom");
			String argu11 = cs1.getArgu1();
			String argu21 = cs2.getArgu1();
			String pred1 = cs1.getPredicate();
			String pred2 = cs2.getPredicate();
			String argu12 = cs1.getArgu2();
			String argu22 = cs2.getArgu2();
			if (pred1.equals(pred2)) {
				double sim1, sim2;
				sim1 = RNCVMSimilarity.getRNCVMSimilarity(argu11, argu21);
				sim2 = RNCVMSimilarity.getRNCVMSimilarity(argu12, argu22);
				if (sim1 == 1 && sim2 == 1) {
					n = 1.0;
				} else if (sim1 == 1 && sim2 != 1) {
					n = sim2;
				} else if (sim2 == 1 && sim1 != 1) {
					n = sim1;
				} else {
					n = 0.0;
				}
			} else {
				n = 0.0;
			}
		}
		// if��cs1 cs2���Ͳ�ͬ
		else {
//			System.out.println("3.�������Ͳ�ͬ�����ƶ�Ϊ��0.0");
			n = 0.0;
		}
//		System.out.println("�������ƶ�Ϊ��" + n);
		return n;
	}
}
