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

		angle = round(angle);	// karol
		double pi = Math.PI;
		
		if(Math.abs(angle) < 0.3)return "Going up";
		else if(angle <= -0.3 && angle > -pi)return "Going left";
		else return "Going right";
		
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		angle = round(angle);					// karol
		double pi = Math.PI;
		
		if(Math.abs(angle) < 0.3)return 1;
		else return -1;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {
		angle = round(angle);

		String stateAngle = getStateAngle(angle,vx,vy);
		String stateVel = getStateVel(angle, vx, vy);
		String stateXV = getStateXV(vx);
		return stateAngle + stateVel + stateXV;
		
	}
	
	private static String getStateXV(double vx) {
		vx = round(vx);
		
		if(Math.abs(vx) < 0.3)return "nice Speed";
		else if(Math.abs(vx)>= 0.3 && Math.abs(vx)< 3)return "too much";
		else return "Totaly wrong";
	}

	public static String getStateVel(double angle, double vx, double vy) {

		vy = round(vy);
		if(Math.abs(vy) < 0.3)return "nice Speed";
		else if(Math.abs(vy)>= 0.3 && Math.abs(vy)< 3)return "too much";
		else return "Totaly wrong";
		
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {
		
		double alpha = 2; //was 4
		double beta = 5;
		double theta = 1;
		angle = round(angle);
		
		double rewardAng = getRewardAngle(angle, vx, vy);
		double rewardVelSide = getRewardXV(vx);
		double rewardVelUp = estimateVelocityReward(vx, vy);
		
		return rewardVelUp*alpha + rewardAng*beta + theta*rewardVelSide;
	}
	
	private static double getRewardXV(double vx) {
		vx = round(vx);
		
		if(Math.abs(vx) < 1)return 1;
		else if(Math.abs(vx)>= 1 && Math.abs(vx)< 2)return -0.5;
		else return -1;
	}

	private static double estimateVelocityReward(double vx, double vy){
		vy = round(vy);
		
		if(Math.abs(vy) < 0.3)return 1;
		else if(Math.abs(vy)>= 0.3 && Math.abs(vy)< 1)return -0.5;
		else return -1;
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
