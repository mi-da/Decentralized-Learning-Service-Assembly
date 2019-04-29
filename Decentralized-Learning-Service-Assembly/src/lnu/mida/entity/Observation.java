package lnu.mida.entity;

import java.util.ArrayList;

public class Observation {
	
	private int time;
	private ArrayList<Double> server_utility;
	private double percentage_failed_connections;
	private double avg_utility_value;	
	private double variance;
	private double stdDev;
	
	public Observation() {
		server_utility = new ArrayList<Double>();
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public ArrayList<Double> getServer_utility() {
		return server_utility;
	}

	public void setServer_utility(ArrayList<Double> server_utility) {
		this.server_utility = server_utility;
	}

	public double getAvg_utility_value() {
		return avg_utility_value;
	}

	public void setAvg_utility_value(double avg_utility_value) {
		this.avg_utility_value = avg_utility_value;
	}

	public double getPercentage_failed_connections() {
		return percentage_failed_connections;
	}

	public void setPercentage_failed_connections(double percentage_failed_connections) {
		this.percentage_failed_connections = percentage_failed_connections;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

}
