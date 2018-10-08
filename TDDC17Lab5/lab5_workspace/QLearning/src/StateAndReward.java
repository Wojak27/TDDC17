public class StateAndReward {

	/*** Daniels notes:
	I spent my whole monday (8th october) figuring this sh*t out.
	I feel like I need to write this to summarize what I have been doing during the day.
	
	I got stuck with a rocket that kept either rotating infinitely and never converging on a "good enough" angle, or a rocket that converged on a good angle but
	was unable to even remotely trigger a sort of hovering-behavior (by fireing engines on/off). The rocket simply never learns to do so. I got stuck here around the early afternoon,
	so I started reading. I needed to find a way to reach this sort of behavior better, and thought of grouping angles more, when I looked at the current iteration of the code.
	I realized we never used the discretization functions that were already implemented and available and got curious about why they are here.
	
	I read a lot in the book, but I couldn't find any real information about the usefulness of discretization in learning Markov Decision Processes in the book at all.
	Discretization is barely even mentioned at all in the reinforcement learning chapter. I was only able to briefly find it in Ch. 21.6.2, without gaining any knowledge.
	Yet, there are supportive functions for discretization here. It obviously has some merit. A simple google search (not scholar) returned the following bachelor thesis:
	https://www.cs.ru.nl/bachelors-theses/2017/Luuk_Arts___4396863___Comparing_Discretization_Methods_for_Applying_Q-learning_in_Continuous_State-Action_Space.pdf
	It suggests using discretization to the right extent (don't overdo it) to give a better Q-table of states and actions. In the paper it groups movements as straight N/E/S/W moves,
	as opposed to a 360 degree movement range. Seeing as there is support for discretization here, the decision was made to re-write the code and grossly group sets of angles
	into bunched groups. I figured I'd try a pie chart of 10 pieces to begin with. Reading the discretization code, it gives you a discretization interval of even pieces
	in the area of MIN to MAX (according to the documentation at least), e.g. angles -3.0 to 3.0 would generate -3.0 -> -2.4, -2.4 -> -1.8 up to 2.4 -> 3.0 using 10 pieces. 
	This would mean that pieces 5 and 6 would be the "best" ones to be in (4 and 5 with 0-indexation), with the perfect state being on the border between both of them. 
	The "real" min/max angle values are -3.14 and 3.14.
	
	Moreover, the VY variable was discretized the same way. The default speed when the rocket is free falling is vy = 5. Figuring that we don't desire that kind of speed the maximum was set
	to 4, and minimum to -4 to reflect this. Interval was 10 here as well because I lack imagination. 
	
	Got everything to work with the currently listed values.*/
	
	//Initialize the static variables for discretization bounds:
	private static final double MINIMUM_ANGLE = -3;
	private static final double MAXIMUM_ANGLE = 3;
	private static final int AMOUNT_OF_ANGLE_STATES = 10;
	private static final double MINIMUM_Y_SPEED = -4;
	private static final double MAXIMUM_Y_SPEED = 4;
	private static final int AMOUNT_OF_Y_SPEED_STATES = 10;
	
	//returns the state a specific angle "maps" to.
	public static String getStateAngle(double angle, double vx, double vy){
		int discretizedAngle = discretize(angle, AMOUNT_OF_ANGLE_STATES, MINIMUM_ANGLE, MAXIMUM_ANGLE);
		return Integer.toString(discretizedAngle); //could also map the number of discretization to an angle name here, but couldn't be bothered for now.
	}
	
	//returns the reward of a specific angle.
	public static double getRewardAngle(double angle, double vx, double vy){
		if (angle > MAXIMUM_ANGLE || angle < MINIMUM_ANGLE){ //I guess absolute number could work here as well perhaps, but only as long as max and min are defined as equals.
			return 0; //We have a really bad angle close to abs(3.14).
		} else {
			return Math.pow((1 - Math.abs(angle)/MAXIMUM_ANGLE), 2); //Need to normalize angle to work with 1, e.g. an angle value of 1 would return (1 - 1/3) = 0.667
		}
	}
	
	//Returns states when we desire to hover, a simple concatenation of two numbers that are mappings (groupings) of angles and y-velocities. Can return 10^2 states.
	public static String getStateHover(double angle, double vx, double vy){
		String angleState = getStateAngle(angle, vx, vy);
		int discretizedVy = discretize(vy, AMOUNT_OF_Y_SPEED_STATES, MINIMUM_Y_SPEED, MAXIMUM_Y_SPEED);
		return angleState + ";" + Integer.toString(discretizedVy);
	}
	
	//Returns the reward for the full hover controller.
	public static double getRewardHover(double angle, double vx, double vy){
		//Suggestion from Mariusz Wzorek to have alfa/beta available for fine tuning if needed. But I honestly have no idea what should be worth more, angle or speed.
		int alfa = 1;
		int beta = 1;
		
		//The actual rewards for hovering. 
		double angleReward = getRewardAngle(angle, vx, vy);
		double vyReward = 0;
		if (vy < MAXIMUM_Y_SPEED && vy > MINIMUM_Y_SPEED){
			//vy is within the suggested bounds to be worth something.
			vyReward = Math.pow((1 - Math.abs(vy)/MAXIMUM_Y_SPEED),2); //Again, normalize vy.
		}
		return alfa * angleReward + beta * vyReward;
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
