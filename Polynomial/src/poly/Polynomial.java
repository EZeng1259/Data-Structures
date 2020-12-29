package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node p1 = poly1; 
		Node p2 = poly2; 
		Node sum = null; 
		
		if(poly1 == null && poly2 != null) 
			return poly2; 
		if(poly1 != null && poly2 == null) 
			return poly1; 
		
		while(p1 != null && p2 != null) {
			if(p1.term.degree > p2.term.degree) {
				if(sum == null) {
					sum = new Node(p2.term.coeff, p2.term.degree, null);
				}
				else {
					Node temp = sum; 
					while(temp.next != null) {
						temp = temp.next; 
					}
					temp.next = new Node(p2.term.coeff, p2.term.degree, null);
				}
				p2 = p2.next; 
			}
			else if(p2.term.degree > p1.term.degree) {
				if(sum == null) {
					sum = new Node(p1.term.coeff, p1.term.degree, null);
				}
				else {
					Node temp = sum; 
					while(temp.next != null) {
						temp = temp.next; 
					}
					temp.next = new Node(p1.term.coeff, p1.term.degree, null);
				}
				p1 = p1.next; 
			}
			else {
				float coeff = p1.term.coeff + p2.term.coeff; 
				if(sum == null) {
					sum = new Node(coeff, p1.term.degree, null); 
				}
				else {
					Node temp = sum; 
					while(temp.next != null) {
						temp = temp.next; 
					}
					if(coeff != 0) {
						temp.next = new Node(coeff, p1.term.degree, null); 
					}
				}
				p1 = p1.next;
				p2 = p2.next; 
			}
			
			Node temp = sum; 
			while(temp != null && temp.next != null) {
				temp = temp.next; 
			}
			if(p1 == null && p2 != null)
				temp.next = p2; 
			else if(p1 != null && p2 == null)
				temp.next = p1; 
		}
		return sum; 
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node product = null; 
		for(Node p1 = poly1; p1 != null; p1 = p1.next) {
			Node term1 = new Node(p1.term.coeff, p1.term.degree, null); 
			System.out.println("Poly1:" + p1.term.toString());
			for(Node p2 = poly2; p2 != null; p2 = p2.next) {
				Node term2 = new Node(p2.term.coeff, p2.term.degree, null); 
				float coeff = term1.term.coeff * term2.term.coeff; 
				int degree = term1.term.degree + term2.term.degree; 
				Node term = new Node(coeff, degree, null); 
				product = add(product, term); 
			}
		}
		return product; 
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		 float sum = 0;  
	     for(Node p = poly; p != null; p = p.next) {
	    	 float term = p.term.coeff * ((float) Math.pow(x, p.term.degree));
	    	 sum += term; 
	    	 System.out.println(sum); 
	     }
	     return sum; 
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
