/**
 * 
 */
package factRevision;

import org.apache.log4j.Logger;

import revision.Revisor;

/**
 * @author wsantos
 *
 */
public class FactRevision {

	private final static Logger LOGGER = Logger.getLogger(FactRevision.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Revisor revisor = new Revisor("c:/Users/wsantos/Documents/Prolog", "alignment_revision_build_thy.pl", "examples_build_thy.dat","classes.dat","alignment_predicates.dat");
		Revisor revisor = new Revisor("d:/Usuarios/010447310388/Documents/Prolog", "alignment_revision_build_thy.pl", "examples_build_thy.dat","classes.dat","alignment_predicates.dat");
//		Revisor revisor = new Revisor(args[0], args[1], args[2], args[3], args[4);
		try {
			revisor.execute();
			LOGGER.info("Program executed successfuly!!!");
		}catch(Exception e){
			LOGGER.error("Ocorreu o seguinte erro: ", e);
		}
	}

}
