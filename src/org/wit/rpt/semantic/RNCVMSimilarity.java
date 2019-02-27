package org.wit.rpt.semantic;
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
import org.wit.rpt.model.ContextStateModel;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * 计算两个概念向量间的语义相似度
 * */
public class RNCVMSimilarity {
	static OntModel m;
	static String fpcs = "file:D://GCCModel//contextstates1.owl";
	static String fpd = "file:D://GCCModel//Domain.owl";

	public static double getRNCVMSimilarity(String c1, String c2) {
		long start = System.currentTimeMillis();
		ContextStateModel cm=new ContextStateModel(fpcs);
		m=cm.getDomainmodel(fpd);
		
		/* 获取两个概念所在的类 */
		Individual ind1 = m.getIndividual(ContextStateModel.domainURI + c1);
		OntClass s1 = ind1.getOntClass();
		Individual ind2 = m.getIndividual(ContextStateModel.domainURI + c2);
		OntClass s2 = ind2.getOntClass();

		Integer int1 = new Integer(0);
		Integer int2 = new Integer(1);
		OntClass root1 = getRoot(s1);
		OntClass root2 = getRoot(s2);

		Vector v1 = new Vector();
		Vector v2 = new Vector();

		/* s1和s2有相同的root类 */
		if (root1.equals(root2)) {
			List<OntClass> l = new ArrayList<OntClass>();
			Collection<OntClass> c = new ArrayList();
			Collection<OntClass> sup = getSuperClasses(s1, c);
			/* 若s1为s2的子类，则互换 */
			if (sup.contains(s2)) {
//				System.out.println("此时"+s1.getLocalName()+"是"+s2.getLocalName()+"的子类");
				OntClass t;
				t = s1;
				s1 = s2;
				s2 = t;
			}
			int n = getSubclassNumber(root1);
			int n1 = getSubclassNumber(s1);
			int n2 = getSubclassNumber(s2);		
			/* 若s1和s2相同，或者s1和s2为等价类，则向量都为[1],则相似度也为1 */
			if (s1.equals(s2) || s1.hasEquivalentClass(s2)) {
				v1.add(int2);
				v2.add(int2);
				// System.out.println(v1);
				// System.out.println(v2);
//				System.out.println(getSimilarity(v1, v2));
			}
			/* root为s1的父类，s1为s2的父类 */
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
			/* s1为root，s2为s1的子类.root */
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
			/* s1为root，s2为s1子类的子类 */
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
			// System.out.println("耗时:"+(end-start));
			return getSimilarity(v1, v2);
		}
		else{
//			System.out.println(0.0);
			return 0.0;
		}
		
	}
	

	/*
	 * 取得一个类的子类的个数,当两个子类为等价类时，个数记为1
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
	 * 获得一个类的祖先类
	 */
	public static OntClass getRoot(OntClass oc) {
		OntClass root = null;
		OntClass temp = oc.getSuperClass();
		if (temp.getLocalName().equals("DomainConcepts")) {
			root = (OntClass) oc;
			return root;
		} else {
			return getRoot(oc.getSuperClass());
		}
	}

	/*
	 * 一个类的父类有多个
	 */
	public static Collection<OntClass> getSuperClasses(OntClass oc, Collection<OntClass> l) {
		ExtendedIterator spc1 = oc.listSuperClasses();
		while (spc1.hasNext()) {
			OntClass ocs1 = (OntClass) spc1.next();
			if (!oc.getSuperClass().getLocalName().equals("DomainConcepts")) {
				l.add(ocs1);
				getSuperClasses(ocs1, l);
			}
		}
		return l;
	}

	/* 计算两个向量的余弦值，也就是相似度值 */
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
		double sim=0.0;
		sim=getRNCVMSimilarity("cartinfo", "cartSite");
		System.out.println(sim);
	}
}

