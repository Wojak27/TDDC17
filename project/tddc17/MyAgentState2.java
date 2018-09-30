package tddc17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.NoOpAction;
import aima.core.environment.liuvacuum.LIUVacuumEnvironment;

class MyAgentState2 {
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final static public int LEFT_SQUARE = 0;
	final static public int FRONT_SQUARE = 1;
	final static public int RIGHT_SQUARE = 2;
	final int ACTION_NONE 			= 0;
	final static int ACTION_MOVE_FORWARD 	= 1;
	final static int ACTION_TURN_RIGHT 	= 2;
	final static int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	public boolean goHome			= false;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;
	private ArrayList<List<Integer>> savedPositions = new ArrayList();
	public static int lastTurn = 0;
	
	MyAgentState2(){
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	
	public boolean checkIfSurraundingsUnexplored(){
		boolean isDone = false;
		
		if(world[agent_x_position+1][agent_y_position] != UNKNOWN && 
				world[agent_x_position-1][agent_y_position] != UNKNOWN && 
				world[agent_x_position][agent_y_position+1] != UNKNOWN && 
				world[agent_x_position][agent_y_position-1]!= UNKNOWN){
			isDone = true;
		}
		
		return isDone;
	}

	
	private int getNextDirection(int direction){
		int nextDirection = agent_direction;
		if(direction == 0)
			return nextDirection--;
		else if(direction == 1)
			return nextDirection;
		else if(direction == 2)
			return nextDirection++;
		else return -1;
	}
	
	public boolean isNextSquareUnexplored(int direction){

		int nextSquare = getNextSquareState(direction);
		System.out.println("nextSquare: "+ nextSquare);
		return nextSquare == 0 ? true : false;
	}
	public boolean isNextSquareWalkable(int direction){
		int nextSquare = getNextSquareState(direction);
		return nextSquare == 2 || nextSquare == 3 ? true : false;
	}
	public boolean isNextSquareBump(int direction){
		int nextSquare = getNextSquareState(direction);
		System.out.println("nextSquare: "+ nextSquare);
		return nextSquare == 1 ? true : false;
	}
	
	private int getNextSquareState(int direction){
		int nextSquare = 0;
		int agentX =agent_x_position;
		int agentY =agent_y_position;
		switch(getNextDirection(direction)){
			case MyAgentState.EAST: 
				nextSquare = world[agentX+1][agentY];
				break;
			case MyAgentState.WEST: 
				nextSquare = world[agentX-1][agentY];
				break;
			case MyAgentState.NORTH: 
				nextSquare = world[agentX][agentY-1];
				break;
			case MyAgentState.SOUTH: 
				nextSquare = world[agentX][agentY+1];
				break;
				
		}
		return nextSquare;
	}
	
	private int[] findWorldBounds(){
		int worldHeight = 0;
		int worldWidth = 0;
		for (int i = 1; i < world.length; i++){
			int currentWidth = 0;
			for (int j = 1; j < world[i].length; j++){
				if (world[i][j] == WALL && worldWidth < j){
					currentWidth = j;
				}
			}
			if(currentWidth != 0){
				worldWidth = currentWidth;
				worldHeight = i;
			}
		}
		System.out.println("width: "+worldWidth+" height: "+worldHeight);
		int[] bounds = {worldWidth, worldHeight};
		return bounds;
	}
	
	public boolean worldHasUnknowns(){
		int[] bounds = findWorldBounds();
		int width = bounds[0];
		int height = bounds[1];
		System.out.println("width: " + width);
		System.out.println("World width: " + width);
		System.out.println("World height: " + height);
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				if (world[i][j] == UNKNOWN){
					return true;
				}
			}
		}
		return false;
	}
	
	public Action rotateAgentRandomDirection(){
		Random r = new Random();
		return randomNumberInRange(1, 2) == 1 ? this.rotateAgentLeft(): this.rotateAgentRight();
	}
	
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
	
	public Action rotateAgentRight(){
		if(agent_direction<3){
			agent_direction++;
		}else agent_direction = 0;
		this.lastTurn = this.ACTION_TURN_RIGHT;
		agent_last_action = ACTION_TURN_RIGHT;
		return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	}
	
	public Action rotateAgentLeft(){
		if(agent_direction>0){
			agent_direction--;
		}else agent_direction = 3;
		agent_last_action = ACTION_TURN_LEFT;
		return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	}
	public Action moveAgentForward(){
		agent_last_action = ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}

	public List<Integer> getLastPosition(){
		return savedPositions.get(savedPositions.size() - 1);
	}
	
	
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p) {
		Boolean bump = (Boolean)p.getAttribute("bump");
		
		System.out.println("action: "+agent_last_action);
		
		if ((agent_last_action == ACTION_MOVE_FORWARD) && !bump )
	    {
			System.out.println("action forward");
			changeAgentPosition();
			
	    }
		
	}
	
	public Action goHome(DynamicPercept p){
		Boolean bump = (Boolean)p.getAttribute("bump");
		Boolean home = (Boolean)p.getAttribute("home");
		
		if(home){
			System.out.println("no op");
			return NoOpAction.NO_OP;
		}else 
		if(bump){
			if(agent_direction == 0){
				this.rotateAgentLeft();
				agent_last_action=ACTION_TURN_LEFT;
				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
			}else{
				this.rotateAgentRight();
				agent_last_action=ACTION_TURN_RIGHT;
				return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
			}
		}else{
			agent_last_action=ACTION_MOVE_FORWARD;
			return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
		}
		
	}
	
	private void changeAgentPosition(){
		switch (agent_direction) {
		case MyAgentState.NORTH:
			agent_y_position--;
			break;
		case MyAgentState.EAST:
			agent_x_position++;
			break;
		case MyAgentState.SOUTH:
			agent_y_position++;
			break;
		case MyAgentState.WEST:
			agent_x_position--;
			break;
		}
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		addNewPosition(x_position, y_position);
		world[x_position][y_position] = info;
	}
	
	private void addNewPosition(int x, int y) {
		List<Integer> list = new ArrayList<Integer>();
		System.out.println("add x: "+ x + " add y: "+ y +" list len: "+(savedPositions.size()+1));
		list.add(x);
		list.add(y);
		savedPositions.add(list);
	}

	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}

	public Action suck() {
		System.out.println("DIRT -> choosing SUCK action!");
    	agent_last_action=ACTION_SUCK;
    	return LIUVacuumEnvironment.ACTION_SUCK;
	}

}
