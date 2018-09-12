package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class MyAgentProgram implements AgentProgram {

	private int initialRandomActions = 100;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public int iterationCounter = 1000;
	public MyAgentState state = new MyAgentState();
	
	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initialRandomActions--;
		System.out.println("update pos");
		state.updatePosition(percept);
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction < 0){ 
		    	state.agent_direction +=4;
		    }
		    state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
		    state.agent_last_action = state.ACTION_TURN_RIGHT;
		    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		} 
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}

	
	@Override
	public Action execute(Percept percept) {
		
		System.out.println("execute");
		// DO NOT REMOVE this if condition!!!
    	/*if (initialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} 
    	else if (initialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}*/
		
    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
    	    	
    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);
    	
		
	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    mapCurrentPosition(percept);
	    
	    state.updatePosition((DynamicPercept)percept);
	    state.printWorldDebug();
	    
	    if(state.checkIfDone()){
	    	 return state.goHome((DynamicPercept)percept);
	    }else return chooseNextAction(percept);

	}

	
    //This piece of code moves the "player" into a new position
    // Next action selection based on the percept value
	private Action chooseNextAction(Percept percept) {
		DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    if(state.checkIfDone()){
	    	//state.goHome(p);
	    	//state.agent_direction = state.WEST;
	    	System.out.println("Done");
	    }
	    
	    if(state.agent_last_action==state.ACTION_TURN_RIGHT){
	    	state.agent_last_action=state.ACTION_MOVE_FORWARD;
    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    }
	    if (dirt)
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } 
	    else
	    {
	    	if (bump)
	    	{
	    		System.out.println("bump");
	    		state.agent_last_action=state.ACTION_TURN_RIGHT;
	    		state.rotateAgentRight();
	    		return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    	}
	    	else
	    	{
	    		List<Integer> nextPos = state.getNextPosition(state.agent_direction);
	    		if(state.haveVisitedLocation(nextPos)){
	    			state.agent_last_action=state.ACTION_TURN_RIGHT;
		    		state.rotateAgentRight();
		    		return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    		}
	    		System.out.println("forward");
	    		state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    	}
	    }
		
	}


	private void mapCurrentPosition(Percept percept) {
		if(state.agent_x_position == 1 && state.agent_y_position == 1){
			return;
		}
		DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    //System.out.println("percept: " + p);
	    
	    // State update based on the percept value and the last action
	    
	    //bump value is false, and it caises the bug, needs fixing
	    System.out.println("agent x position" + state.agent_x_position);
	    if (bump) {
	    	// go to previous position
	    	//resetCorrectPosition();
	    	System.out.print("bump");
	    	System.out.println("Agent direction: "+state.agent_direction);
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:	
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
	    }else if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	System.out.print("clear");
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
		
	}


	private void resetCorrectPosition() {
		List<Integer> lastCoordinates = state.getLastPosition();
		state.agent_x_position = lastCoordinates.get(0);
		state.agent_y_position = lastCoordinates.get(1);
		
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
