public class StateAndReward {

	
	//returns a double rounded to two decimals (needed write own funtion for that... thanks obama)
	private static double round(double num){
		double rounded = 100;
		rounded *= num;
		rounded = Math.round(rounded);
		rounded = rounded /100;
		return rounded;
	}
	
	/* State discretization function for the angle controller */
	public static String getStateAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		String state;
		angle = round(angle);					// karol
		if(Math.abs(angle) > 1){
			state = "Wrong angle!";
		}else
		if(Math.abs(angle) < 0.2 && vy<1 && vy>-5){
			if(Math.abs(vy) < 0.1) state = "Perfect!";
			else if(vy >-2) state = "almost there";
			else state = "Just right angle!";
		}else if(angle<0){
			state = "Tipping left!";
		}else{
			state = "Tipping right!";
		}
		
		return state;
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		angle = round(angle);
		if(Math.abs(angle) > 1){
			return -100;
		}else if(Math.abs(angle) < 0.2 && vy<1 && vy>-5){
			if(Math.abs(vy) < 0.1) return 100;
			else if(vy >-2) return (10 - vy);
			return 2;
		}
		double reward = angle < 0 ? angle : -angle; //karol

		return reward;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {
		angle = round(angle);
		
		if(Math.abs(angle) < 0.1)  return "Nice angle! "+getStateVel(angle, vx, vy);
		else if (Math.abs(angle) < 0.3)  return "Very good angle! " +getStateVel(angle, vx, vy);
		else if (Math.abs(angle) < 0.5)  return "good angle! "+getStateVel(angle, vx, vy);
		else if (Math.abs(angle) < 0.7)  return "Okay angle! " +getStateVel(angle, vx, vy);
		else if (Math.abs(angle) < 1)  return "decent angle! " +getStateVel(angle, vx, vy);
		else return "Totaly wrong angle! "+getStateVel(angle, vx, vy);
		
	}
	
	public static String getStateVel(double angle, double vx, double vy) {
		angle = round(angle);
		
		if(Math.abs(vy) < 0.1)  return "Nice vel!";
		else if (Math.abs(vy) < 0.5)  return "Very good vel!";
		else if (Math.abs(vy) < 1)  return "good wrong vel!";
		else if (Math.abs(vy) < 2)  return "Okay wrong vel!";
		else if (Math.abs(vy) < 3)  return "decent wrong vel!";
		else return "Totaly wrong vel!";
		
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {
		double rewardVel = 0;
		double rewardAng = 0;
		double alpha = 4;
		double beta = 1;
		angle = round(angle);
		if (Math.abs(angle) < 1){
			rewardAng = (Math.pow((Math.PI-Math.abs(angle)),2));
		}else rewardAng = -100;
		
		rewardVel = estimateVelocityReweward(vx, vy);
		
		return rewardVel*alpha + rewardAng*beta;
	}
	
	private static double estimateVelocityReweward(double vx, double vy){
		// Three states
		return (3-Math.abs(vy));
	}

	// ///////////////////////////////////////////////////////////
	// discretize() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 1 and nrValues-2 is returned.
	//
	// Use discretize2() if you want a discretization method that does
	// not handle values lower than min and higher than max.
	// ///////////////////////////////////////////////////////////
	public static int discretize(double value, int nrValues, double min,
			double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	// ///////////////////////////////////////////////////////////
	// discretize2() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 0 and nrValues-1 is returned.
	// ///////////////////////////////////////////////////////////
	public static int discretize2(double value, int nrValues, double min,
			double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}

}
