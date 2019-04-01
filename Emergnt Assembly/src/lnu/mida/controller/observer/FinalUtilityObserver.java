package lnu.mida.controller.observer;

import java.io.PrintStream;
import java.util.ArrayList;

import lnu.mida.controller.init.OverloadFileInitializer;
import peersim.config.Configuration;
import peersim.core.*;
import peersim.util.*;

/**
 * I am an observer that prints, at each timestep, the minimum, average and
 * maximum utility of all fully resolved services.
 */
public class FinalUtilityObserver implements Control {

	// ///////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * The name of this observer in the configuration file. Initialized by the
	 * constructor parameter.
	 */
	private final String name;

	public static ArrayList<IncrementalStats> finalUtils;
	
	public static ArrayList<IncrementalStats> finalUtils_std;
	
	public static ArrayList<IncrementalStats> finalUtils_jain;

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
	public FinalUtilityObserver(String name) {
		this.name = name;
	}

	// ///////////////////////////////////////////////////////////////////////
	// Methods
	// ///////////////////////////////////////////////////////////////////////

	@Override
	public boolean execute() {
		
		int exp_number = OverloadFileInitializer.experiment_number;
		int total_exps = Configuration.getInt("simulation.experiments",1);
		
		// print data from last experiment
		if(exp_number==total_exps) {
			
			PrintStream ps = OverloadFileInitializer.getPs_final();
			
			int n = 1;
			for (IncrementalStats incrementalStats : finalUtils.subList( 1, finalUtils.size() ) ) {
								
				double globalU = incrementalStats.getAverage();
				
				IncrementalStats is_jain = finalUtils_jain.get(finalUtils.indexOf(incrementalStats));
				
				double jain = is_jain.getAverage();
			
				ps.print(n+" ");
				ps.print(globalU+" ");
				ps.println(jain);
				
				n+=1; // learning step
			}						
		}
		
		return false;
	}	
	
}