package lnu.mida.controller.observer;

import lnu.mida.protocol.OverloadComponentAssembly;
import peersim.config.*;
import peersim.core.*;
import peersim.util.*;

/**
 * I am an observer that prints, at each timestep, the minimum, average and
 * maximum utility of all fully resolved services.
 */
public class UtilityObserver implements Control {

	// ///////////////////////////////////////////////////////////////////////
	// Constants
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * The protocol to operate on.
	 * 
	 * @config
	 */
	private static final String PAR_PROT = "protocol";

	/**
	 * Stop when the average utility is at least X%; default never stops
	 *
	 * @config
	 */
	private static final String PAR_STOPAT = "stopat";

	/**
	 * Stop when the minimum compound utility is at least this value; default
	 * never stops
	 *
	 * @config
	 */
	private static final String PAR_MINSTOPAT = "minstopat";

	// ///////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * The name of this observer in the configuration file. Initialized by the
	 * constructor parameter.
	 */
	private final String name;

	/** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
	private final int pid;

	private final double stopat;

	private final double minstopat;

	// ///////////////////////////////////////////////////////////////////////
	// Constructor
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * Standard constructor that reads the configuration parameters. Invoked by
	 * the simulation engine.
	 * 
	 * @param name
	 *            the configuration prefix for this class.
	 */
	public UtilityObserver(String name) {
		this.name = name;
		pid = Configuration.getPid(name + "." + PAR_PROT);
		stopat = Configuration.getDouble(name + "." + PAR_STOPAT, -1);
		minstopat = Configuration.getDouble(name + "." + PAR_MINSTOPAT, -1);
	}

	// ///////////////////////////////////////////////////////////////////////
	// Methods
	// ///////////////////////////////////////////////////////////////////////

	@Override
	public boolean execute() {
		
		long time = peersim.core.CommonState.getTime();
		
		IncrementalStats util = new IncrementalStats();
		int fully_resolved = 0;

		for (int i = 0; i < Network.size(); i++) {

			OverloadComponentAssembly n = (OverloadComponentAssembly) Network.get(i).getProtocol(pid);
			
			if(n.isFullyResolved()) {
				fully_resolved++;
			}
			
			// recursive utility calculation
			util.add(n.getEffectiveCU());
		
		}

		
		int index = (int) ((time/Configuration.getInt("COMPOSITION_STEPS",1)));
		
		IncrementalStats is = FinalUtilityObserver.finalUtils.get(index);
		
		IncrementalStats jain_is = FinalUtilityObserver.finalUtils_jain.get(index);

		is.add(util.getAverage());

		// calculate the jain's fairness	
		double jain_fairness = Math.pow(util.getSum(),2)/(util.getN()*util.getSqrSum());
		
		jain_is.add(jain_fairness);

		return false;
	}	
	
}