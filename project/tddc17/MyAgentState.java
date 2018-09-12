package tddc17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.liuvacuum.LIUVacuumEnvironment;

class MyAgentState {
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	final int ACTION_ROTATE_RIGHT 	= 5;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;
	private ArrayList<List<Integer>> savedPositions = new ArrayList();
	
	MyAgentState(){
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	
	//rotates the agent either 90,180 or 270 degrees
	public int directionRotator(int direction, int factor){
		return (direction+factor)%3;
		
	}
	
	public boolean checkIfDone(){
		boolean isDone = false;
		
		if(world[agent_x_position+1][agent_y_position] == CLEAR && 
				world[agent_x_position-1][agent_y_position] == CLEAR && 
				world[agent_x_position][agent_y_position+1] == CLEAR && 
				world[agent_x_position][agent_y_position-1] == CLEAR){
			isDone = true;
		}
		
		return isDone;
	}
	
	public void rotateAgentRight(){
		if(agent_direction<3){
			agent_direction++;
		}else agent_direction = 0;
	}
	
	public void rotateAgentLeft(){
		if(agent_direction>0){
			agent_direction--;
		}else agent_direction = 3;
	}
	
	public List getNextPosition(int direction){
		int nextX = agent_x_position;
		int nextY = agent_y_position;

		switch (agent_direction) {
		case MyAgentState.NORTH:
			nextY--;
			break;
		case MyAgentState.EAST:
			nextX++;
			break;
		case MyAgentState.SOUTH:
			nextY++;
			break;
		case MyAgentState.WEST:
			nextX--;
			break;
		}
		List<Integer> list = new ArrayList();
		list.add(nextX);
		list.add(nextY);
		return list;
	}
	
	public boolean haveVisitedLocation(List<Integer> list){
		
		return world[list.get(0)][list.get(1)] == CLEAR || world[list.get(0)][list.get(1)] == HOME;
		//return savedPositions.contains(list);
		
	}
	
	public List<Integer> getLastPosition(){
		return savedPositions.get(savedPositions.size() - 1);
	}
	
	
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p) {
		Boolean bump = (Boolean)p.getAttribute("bump");

		System.out.println(" x: "+ agent_x_position +" y: "+agent_y_position);
		System.out.println("action: "+agent_last_action);
		
		if ((agent_last_action == ACTION_MOVE_FORWARD) && !bump )
	    {
			System.out.println("action forward");
			changeAgentPosition();
			
	    }
		
	}
	
	public Action goHome(DynamicPercept p){
		Boolean bump = (Boolean)p.getAttribute("bump");
		if(agent_direction != 3){
			agent_direction = 3;
			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		}else return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
		
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
}
