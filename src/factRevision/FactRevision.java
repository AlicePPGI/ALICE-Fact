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
//		Revisor revisor = new Revisor("c:/Users/wsantos/Documents/Prolog", "alignment_revision_build_thy.pl", "examples_build_thy.dat","classes.dat");
		Revisor revisor = new Revisor("d:/Usuarios/010447310388/Documents/Prolog", "alignment_revision_build_thy.pl", "examples_build_thy.dat","classes.dat");
//		Revisor revisor = new Revisor(args[0], args[1], args[2], args[3]);
		try {
			revisor.execute();
			System.out.println("Program executed successfuly!!!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
