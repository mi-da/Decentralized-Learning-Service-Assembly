package lnu.mida.protocol;

import java.util.ArrayList;
import lnu.mida.entity.Reputation;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Cleanable;
import peersim.core.Node;

public class OverloadApplication implements CDProtocol, Cleanable {

	// ///////////////////////////////////////////////////////////////////////
	// Constants
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * The component assambly protocol.
	 */
	private static final String COMP_PROT = "comp_prot";

	/**
	 * The strategy
	 */
	private static String STRATEGY = "";

	// ///////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * The component assembly protocol id.
	 */
	private final int component_assembly_pid;

	/** The learner's container */
	private ArrayList<Reputation> reputations;

	/**
	 * Initialize this object by reading configuration parameters.
	 * 
	 * @param prefix
	 *            the configuration prefix for this class.
	 */
	public OverloadApplication(String prefix) {
		super();
		STRATEGY = Configuration.getString("STRATEGY", "no strat");
		component_assembly_pid = Configuration.getPid(prefix + "." + COMP_PROT);
		reputations = new ArrayList<Reputation>();
	}

	public void addHistoryExperience(OverloadComponentAssembly server, double experienced_utility, double declared_utility) {
		int index = (int) server.getId();
		
		Reputation reputation = getOrCreateReputation(index);		
		reputation.setDeclared_utility(declared_utility);
		reputation.addExperiencedUtility(experienced_utility);

	}

	public ArrayList<Reputation> getHistories() {
		return reputations;
	}

	/**
	 * Makes a copy of this object. Needs to be explicitly defined, since we
	 * have array members.
	 */
	@Override
	public Object clone() {
		OverloadApplication result = null;
		try {
			result = (OverloadApplication) super.clone();
		} catch (CloneNotSupportedException ex) {
			System.out.println(ex.getMessage());
			assert (false);
		}
		result.reputations = new ArrayList<Reputation>();
		return result;
	}

	// returns true if comp > old
	public boolean chooseByStrategy(OverloadComponentAssembly comp, OverloadComponentAssembly old, OverloadComponentAssembly node) {

		// default composition strategy (best actual value)
		if (STRATEGY.equals("greedy")) {
			return chooseByDefaultStrategy(comp, old);
		}
		// random strategy
		if (STRATEGY.equals("random")) {
			return chooseByRandomStrategy(comp, old);
		}
		// average strategy - not used during paper
		if (STRATEGY.equals("average")) {
			return chooseByAverageStrategy(comp, old);
		}
		// future expected utility
		if (STRATEGY.equals("emergent")) {
			return chooseByFutureExpectedUtility(comp, old);
		}
		// approach to challenge
		if (STRATEGY.equals("shaerf")) {
			return chooseByChallengeStrategy(comp, old);
		}

		// exception is raised if a strategy is not selected
		else {
			try {
				throw new Exception("Strategy not selected");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
			return false;
		}
	}

	// returns true if comp > old
	private boolean chooseByDefaultStrategy(OverloadComponentAssembly comp, OverloadComponentAssembly old) {

		if (comp.getDeclaredUtility() >= old.getDeclaredUtility())
			return true;
		else
			return false;

	}

	// chooses a random component
	private boolean chooseByRandomStrategy(OverloadComponentAssembly comp, OverloadComponentAssembly old) {
		if (Math.random() < 0.5)
			return true;
		else
			return false;
	}

	// returns true if Avg(comp) > Avg(old)
	private boolean chooseByAverageStrategy(OverloadComponentAssembly comp, OverloadComponentAssembly old) {

		Reputation compReputation = getOrCreateReputation((int) comp.getId());
		Reputation oldReputation = getOrCreateReputation((int) old.getId());

		if (compReputation.getK() == 0)
			compReputation.setQk(comp.getCompoundUtility());

		if (oldReputation.getK() == 0)
			oldReputation.setQk(comp.getCompoundUtility());

		if (compReputation.getWindowAverage() > oldReputation.getWindowAverage())
			return true;

		else
			return false;
	}

	// future expected utility: two layer of reinforcement learning 
	private boolean chooseByFutureExpectedUtility(OverloadComponentAssembly comp, OverloadComponentAssembly old) {

		Reputation compReputation = getOrCreateReputation((int) comp.getId());
		Reputation oldReputation = getOrCreateReputation((int) old.getId());

		double compTrust = compReputation.getTk();
		double oldTrust = oldReputation.getTk();


		double	compFEU = compTrust * comp.getDeclaredUtility() + ((1.0 - compTrust) * compReputation.getWindowAverage());
		double	oldFEU = oldTrust * old.getDeclaredUtility() + ((1.0 - oldTrust) * oldReputation.getWindowAverage());

		// greedy selection
		if (compFEU > oldFEU)
			return true;
		else
			return false;
	}

	// approach to challenge
	private boolean chooseByChallengeStrategy(OverloadComponentAssembly comp, OverloadComponentAssembly old) {

		Reputation compReputation = getOrCreateReputation((int) comp.getId());
		Reputation oldReputation = getOrCreateReputation((int) old.getId());

		if (compReputation.getK() == 0 || oldReputation.getK() == 0)
			return chooseByRandomStrategy(comp, old);

		double comp_ee = compReputation.getEe();
		double old_ee = oldReputation.getEe();

		// if no experiences do the average
		if (compReputation.getK() == 0) {
			int n = 0;
			int sum = 0;
			for (Reputation reputation : reputations) {
				double ee = reputation.getEe();
				if (ee != 0) {
					sum += ee;
					n++;
				}
			}
			comp_ee = sum / n;
		}

		if (oldReputation.getK() == 0) {
			int n = 0;
			int sum = 0;
			for (Reputation reputation : reputations) {
				double ee = reputation.getEe();
				if (ee != 0) {
					sum += ee;
					n++;
				}
			}
			old_ee = sum / n;
		}

		double comp_probl1 = Math.pow(comp_ee, 3); // was 3
		double old_probl1 = Math.pow(old_ee, 3);

		double sigma = comp_probl1 + old_probl1;

		double comp_probl = comp_probl1 / sigma;
		double old_probl = old_probl1/sigma;

		if (old_probl < comp_probl) //  if (old_probl1 < comp_probl)
			return true;
		else
			return false;
	}

	public void resetLearningParameterK() {
		for (Reputation reputation : reputations) {
			reputation.setK(0);
		}
	}

	@Override
	public void onKill() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void nextCycle(Node node, int protocolID) {
		// TODO Auto-generated method stub

	}
	
	private Reputation getOrCreateReputation(int nodeId) {
		for (Reputation reputation : reputations) {
			if(reputation.getNodeID() == nodeId) {
				return reputation;
			}
		}
		Reputation newReputation = new Reputation(nodeId);
		reputations.add(newReputation);
		return newReputation;
	}

}
