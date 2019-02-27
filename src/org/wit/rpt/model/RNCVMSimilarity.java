package org.wit.rpt.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.wit.rpt.model.CapabilityModel;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class RNCVMSimilarity {
	static OntModel m;

	/*
	 * ��ȡ��������
	 */
	public static double getRNCVMSimilarity(String c1, String c2) {
		long start = System.currentTimeMillis();
		String fpcc = "file:D://GCCModel//CCmodel.owl";
		CapabilityModel om = new CapabilityModel(fpcc);
		m = om.getCCModel();
		/* ��ȡ�����������ڵ��� */
		Individual ind1 = m.getIndividual(CapabilityModel.ccURI + c1);
		OntClass s1 = ind1.getOntClass();
		Individual ind2 = m.getIndividual(CapabilityModel.ccURI + c2);
		OntClass s2 = ind2.getOntClass();

		Integer int1 = new Integer(0);
		Integer int2 = new Integer(1);
		OntClass root1 = getRoot(s1);
		OntClass root2 = getRoot(s2);

		Vector v1 = new Vector();
		Vector v2 = new Vector();

		/* s1��s2����ͬ��root�� */
		if (root1.equals(root2)) {
			List<OntClass> l = new ArrayList<OntClass>();
			Collection<OntClass> c = new ArrayList();
			Collection<OntClass> sup = getSuperClasses(s1, c);
			/* ��s1Ϊs2�����࣬�򻥻� */
			if (sup.contains(s2)) {
//				System.out.println("��ʱ"+s1.getLocalName()+"��"+s2.getLocalName()+"������");
				OntClass t;
				t = s1;
				s1 = s2;
				s2 = t;
			}
			int n = getSubclassNumber(root1);
			int n1 = getSubclassNumber(s1);
			int n2 = getSubclassNumber(s2);		
			/* ��s1��s2��ͬ������s1��s2Ϊ�ȼ��࣬��������Ϊ[1],�����ƶ�ҲΪ1 */
			if (s1.equals(s2) || s1.hasEquivalentClass(s2)) {
				v1.add(int2);
				v2.add(int2);
				// System.out.println(v1);
				// System.out.println(v2);
//				System.out.println(getSimilarity(v1, v2));
			}
			/* rootΪs1�ĸ��࣬s1Ϊs2�ĸ��� */
			else if (s2.getSuperClass().equals(s1) && s1.getSuperClass().equals(root1)) {
				v1.add(int2);
				v1.add(n);
				for (int i = 1; i < n; i++) {
					v1.add(int1);
				}
				for (int j = 0; j < n1; j++) {
					v1.add(n1);
				}

				v2.add(int2);
				v2.add(n);
				for (int i = 1; i < n; i++) {
					v2.add(int1);
				}
				v2.add(n1);
				for (int j = 1; j < n1; j++) {
					v2.add(int1);
				}
				// System.out.println(v1);
				// System.out.println(v2);
//				System.out.println(getSimilarity(v1, v2));
			}
			/* s1Ϊroot��s2Ϊs1������.root */
			else if (s1.equals(getRoot(s1)) && s2.getSuperClass().equals(s1)) {
				v1.add(int2);
				for (int i = 0; i < n1; i++) {
					v1.add(n1);
				}
				for (int j1 = 0; j1 < n2; j1++) {
					v1.add(n2);
				}

				v2.add(int2);
				v2.add(n1);
				for (int i = 1; i < n1; i++) {
					v2.add(int1);
				}
				for (int j2 = 0; j2 < n2; j2++) {
					v2.add(n2);
				}

				// System.out.println(v1);
				// System.out.println(v2);
//				System.out.println(getSimilarity(v1, v2));
			}
			/* s1Ϊroot��s2Ϊs1��������� */
			else if (s1.equals(getRoot(s1)) && s2.getSuperClass().getSuperClass().equals(s1)) {
				OntClass s = s2.getSuperClass();
				int n4 = getSubclassNumber(s);
				v1.add(int2);
				for (int i = 0; i < n1; i++) {
					v1.add(n1);
				}
				for (int j1 = 0; j1 < n4; j1++) {
					v1.add(n4);
				}

				v2.add(int2);
				v2.add(n1);
				for (int i = 1; i < n1; i++) {
					v2.add(int1);
				}
				v2.add(n4);
				for (int j2 = 1; j2 < n4; j2++) {
					v2.add(int1);
				}
				// System.out.println(v1);
//				// System.out.println(v2);
//				System.out.println(getSimilarity(v1, v2));
			}
			// long end = System.currentTimeMillis();
			// System.out.println("��ʱ:"+(end-start));
			return getSimilarity(v1, v2);
		}
		else{
//			System.out.println(0.0);
			return 0.0;
		}
		
	}
	/* �����ۣ������Ǹ��ͬһ������ */
	// /*����ͬ��������,1.ֱ������ ��������s1��s2�����໹�����ࣩ*/
	// if(l.contains(s1) && l.contains(s2)){
	// int n2=getSubclassNumber(s1);
	// int n3=getSubclassNumber(s2);
	// Vector v1 = new Vector();
	// v1.add(int2);
	// v1.add(n1);
	// for (int i = 1; i < n1; i++) {
	// v1.add(int1);
	// }
	// for (int i = 0; i < n2; i++) {
	// v1.add(n2);
	// }
	// for (int i = 0; i < n3; i++) {
	// v1.add(int1);
	// }
	// Vector v2 = new Vector();
	// v2.add(int2);
	// v2.add(int1);
	// v2.add(n1);
	// for (int i = 2; i < n1; i++) {
	// v2.add(int1);
	// }
	// for (int i = 0; i < n2; i++) {
	// v2.add(int1);
	// }
	// for (int i = 0; i < n3; i++) {
	// v2.add(n3);
	// }
	// System.out.println(v1);
	// System.out.println(v2);
	// System.out.println(getSimilarity(v1,v2));
	// }
	//
	// /*����ͬ��������,����ͬ�ĸ��ڵ㲻Ϊ����*/
	// else if(s1.getSuperClass().getSuperClass().equals(getRoot(s1))) {
	// OntClass root = getRoot(s1);
	// int n11 = getSubclassNumber(root);
	// int n2 = getSubclassNumber(s1.getSuperClass());
	// Integer int3 = new Integer(n11);
	// Integer int4 = new Integer(n2);
	// Vector v1 = new Vector(n11 + n2 + 1);
	// v1.add(int2);
	// v1.add(int3);
	// for (int i = 1; i < n11; i++) {
	// v1.add(int1);
	// }
	// v1.add(int4);
	// for (int j = 2; j < n2; j++) {
	// v1.add(int1);
	// }
	// Vector v2 = new Vector(n11 + n2 + 1);
	// v2.add(int2);
	// v2.add(int3);
	// for (int i = 1; i < n11; i++) {
	// v2.add(int1);
	// }
	// v2.add(int1);
	// v2.add(int4);
	// for (int j = 2; j < n2 + 1; j++) {
	// v1.add(int1);
	// }
	// System.out.println(v1);
	// System.out.println(v2);
	// System.out.println(getSimilarity(v1, v2));
	// }

	// public static void getVectors(String c1, String c2) {
	// CapabilityModel om = new CapabilityModel();
	// m = om.getModel();
	// /* ��ȡ�����������ڵ��� */
	// Individual ind1 = m.getIndividual(CapabilityModel.modelURI + c1);
	// OntClass s1 = ind1.getOntClass();
	// Individual ind2 = m.getIndividual(CapabilityModel.modelURI + c2);
	// OntClass s2 = ind2.getOntClass();
	// Collection<OntClass> l = new ArrayList();
	// getSuperClasses(s1, l);
	// System.out.println(l.size());
	// Integer int1 = new Integer(0);
	// Integer int2 = new Integer(1);
	//
	// /* �����������ͬ�ĸ��� ,����û������*/
	// if
	// (s1.getSuperClass().equals(s2.getSuperClass())&&getSubclassNumber(s1)==0&&getSubclassNumber(s2)==0)
	// {
	// /* ֱ�Ӹ����Ϊ���Ƚڵ� */
	// if (s1.getSuperClass().equals(getRoot(s1))) {
	// int n = getSubclassNumber(s1.getSuperClass());
	// Integer int3 = new Integer(n);
	// /* ��õ�һ���������ڵ����� */
	// Vector v1 = new Vector(n + 1);
	// v1.add(int2);
	// v1.add(int3);
	// for (int i = 2; i < n + 1; i++) {
	// v1.add(int1);
	// }
	// /* ��õڶ����������ڵ����� */
	// Vector v2 = new Vector(n);
	// v2.add(int2);
	// v2.add(int1);
	// v2.add(int3);
	// for (int i = 3; i < n + 1; i++) {
	// v2.add(int1);
	// }
	// System.out.println(v1);
	// System.out.println(v2);
	// }
	// /* ����ĸ���ĸ���Ϊ���ڵ� */
	// else if (s1.getSuperClass().getSuperClass().equals(getRoot(s1))) {
	// OntClass root = getRoot(s1);
	// int n1 = getSubclassNumber(root);
	// int n2 = getSubclassNumber(s1.getSuperClass());
	// Integer int3 = new Integer(n1);
	// Integer int4 = new Integer(n2);
	// Vector v1 = new Vector(n1 + n2 + 1);
	// v1.add(int2);
	// v1.add(int3);
	// for (int i = 1; i < n1; i++) {
	// v1.add(int1);
	// }
	// v1.add(int4);
	// for (int j = 2; j < n2; j++) {
	// v1.add(int1);
	// }
	// Vector v2 = new Vector(n1 + n2 + 1);
	// v2.add(int2);
	// v2.add(int3);
	// for (int i = 1; i < n1; i++) {
	// v2.add(int1);
	// }
	// v2.add(int1);
	// v2.add(int4);
	// for (int j = 2; j < n2 + 1; j++) {
	// v1.add(int1);
	// }
	// System.out.println(v1);
	// System.out.println(v2);
	// System.out.println(getSimilarity(v1, v2));
	// }
	// }
	// }

	/*
	 * ȡ��һ���������ĸ���,����������Ϊ�ȼ���ʱ��������Ϊ1
	 */
	public static int getSubclassNumber(OntClass oc) {
		int count = 0;
		Collection<OntClass> c1 = new ArrayList();
		for (Iterator p = oc.listSubClasses(); p.hasNext();) {
			OntClass d = (OntClass) p.next();
			c1.add(d);
			count++;
		}
		return count;
	}

	/*
	 * ���һ�����������
	 */
	public static OntClass getRoot(OntClass oc) {
		OntClass root = null;
		if (oc.getSuperClass().getLocalName().equals("DomainConcept")) {
			root = (OntClass) oc;
			return root;
		} else {
			return getRoot(oc.getSuperClass());
		}
	}

	/*
	 * һ����ĸ����ж��
	 */
	public static Collection<OntClass> getSuperClasses(OntClass oc, Collection<OntClass> l) {
		ExtendedIterator spc1 = oc.listSuperClasses();
		while (spc1.hasNext()) {
			OntClass ocs1 = (OntClass) spc1.next();
			if (!oc.getSuperClass().getLocalName().equals("DomainConcept")) {
				l.add(ocs1);
				getSuperClasses(ocs1, l);
			}
		}
		return l;
	}

	/* ������������������ֵ��Ҳ�������ƶ�ֵ */
	public static double getSimilarity(Vector v1, Vector v2) {
		double s1 = 0.0;
		double s2 = 0.0;
		double b1 = 0.0;
		double b2 = 0.0;
		for (int i = 0; i < v1.size(); i++) {
			double a1 = Double.parseDouble(v1.get(i).toString());
			double a2 = Double.parseDouble(v2.get(i).toString());
			s1 += a1 * a2;
		}
		for (int j = 0; j < v1.size(); j++) {
			b1 += Double.parseDouble(v1.get(j).toString()) * Double.parseDouble(v1.get(j).toString());
			b2 += Double.parseDouble(v2.get(j).toString()) * Double.parseDouble(v2.get(j).toString());
			s2 = Math.sqrt(b1) * Math.sqrt(b2);
		}
		return s1 / s2;
	}

	public static void main(String args[]) {
		RNCVMSimilarity.getRNCVMSimilarity("destinationSite", "destinationInfo");
	}
}

