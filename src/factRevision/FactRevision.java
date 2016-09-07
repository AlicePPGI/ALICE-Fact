/**
 * 
 */
package factRevision;

import revision.Revisor;

/**
 * @author wsantos
 *
 */
public class FactRevision {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Revisor revisor = new Revisor();
		try {
			revisor.execute();
			System.out.println("Program executed successfuly!!!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
