package lnu.mida.entity;

public class UnityTransferFunction extends TransferFunction {

	// Returns 1 for each value of lambda_tot. Simulates a "single call/user engaged"
	@Override
	public double calculate_tSd() {
		return 1;
	}

}
