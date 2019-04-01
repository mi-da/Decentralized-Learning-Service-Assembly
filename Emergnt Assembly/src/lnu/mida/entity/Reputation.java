package lnu.mida.entity;

public class Reputation implements Cloneable  {
	
	
	// node id for which the experience is evaluated
	private int nodeID;
	// last declared utility
	private double declared_utility;
	// number of time the experience is done
	private int k;
	// current average (influenced by the window period of the learner)
	private double Qk;
	// trust value
	private double Tk;
	
	// window period of the learner
	private static int M;
	
	// approach to challenge
	private double ee;
	
	public Reputation(int nodeId) {
		this.nodeID=nodeId;
		declared_utility=-1;
		k=0;
		Qk=0.5;
		Tk=0.5;
		ee=0;
	}
	
	public void addExperiencedUtility(double experienced_utility) {
		
		// Updating the Average
        double beta = (k%M)+1;
		double Qk_new = Qk + ((experienced_utility-Qk)/beta);	

		Qk = Qk_new;
		
		// Updating the trust value
		double normalized_experienced_utility = experienced_utility;
		
		double efficiency = ( 1 - ( Math.abs(declared_utility - normalized_experienced_utility) / Math.abs(declared_utility) ) );
		
		double Tk_new = Tk + ((efficiency-Tk)/beta);
		
		Tk=Tk_new;
		
		// Approach to Challenge	
		double W = 0.2 + (0.8/k); // 0.2 + (0.8/k)
		if(k==0)
			W=1;
		
		double ee_new = W*experienced_utility + ( (1-W)*ee );
		
		ee = ee_new;
		
		k++;
	}
	
	public double getWindowAverage() {
        return Qk;
	}
	
	@Override
	public Object clone() {	
		Reputation result = null;
		try {
			result = (Reputation) super.clone();
		} catch (CloneNotSupportedException ex) {
			System.out.println(ex.getMessage());
			assert(false);
		}
		return result;
	}

	public int getK() {
		return k;
	}
	
	public void setK(int k) {
		this.k=k;
	}

	public static int getM() {
		return M;
	}

	public static void setM(int m) {
		M = m;
	}

	public double getDeclared_utility() {
		return declared_utility;
	}

	public void setDeclared_utility(double declared_utility) {
		this.declared_utility = declared_utility;
	}

	public double getTk() {
		return Tk;
	}

	public void setTk(double tk) {
		Tk = tk;                                                              
	}                                                                                

	public void setQk(double qk) {
		Qk = qk;
	}
	
	public double getQk() {
		return Qk;
	}

	public double getEe() {
		return ee;
	}
	
	public int getNodeID() {
		return nodeID;
	}

}
