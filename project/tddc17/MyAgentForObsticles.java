package tddc17;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class MyAgentProgram2 implements AgentProgram {

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
    	    	

    	
		
	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    mapCurrentPosition(percept);
	    state.printWorldDebug();
	    state.updatePosition((DynamicPercept)percept);
	    
	    
    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);
	    
    	return checkIfGoHome(percept);
	    
		
	}
	
	private Action checkIfGoHome(Percept percept){
		if(state.goHome){
	    	 return state.goHome((DynamicPercept)percept);
	    }else {
	    	if(state.checkIfDone()){
	    		state.goHome = true;
	    		if(state.agent_direction == 1){
	    			System.out.println("go home left");
	    			state.rotateAgentLeft();
	    			state.agent_last_action=state.ACTION_TURN_LEFT;
	    			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	    		}else if(state.agent_direction == 2){
	    			System.out.println("go home right");
	    			state.rotateAgentRight();
	    			state.agent_last_action=state.ACTION_TURN_RIGHT;
	    			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    		}else 
	    			System.out.println("go home forward");
	    			state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    			return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    	}
	    	return chooseNextAction(percept);
	    }
	}

	
    //This piece of code moves the "player" into a new position
    // Next action selection based on the percept value
	private Action chooseNextAction(Percept percept) {
	    
	    if(state.agent_last_action==state.ACTION_TURN_RIGHT){
	    	state.agent_last_action=state.ACTION_MOVE_FORWARD;
    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    }
	    if (getAttribute((DynamicPercept) percept, "dirt"))
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } 
	    else
	    {
	    	if (getAttribute((DynamicPercept) percept, "bump"))
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
	
	private boolean getAttribute(DynamicPercept p, String attribute){
		return (Boolean)p.getAttribute(attribute);
	}


	private void mapCurrentPosition(Percept percept) {
		if(state.agent_x_position == 1 && state.agent_y_position == 1){
			return;
		}
	    
	    // State update based on the percept value and the last action
	    //bump value is false, and it caises the bug, needs fixing
	    System.out.println("agent x position" + state.agent_x_position);
	    if (getAttribute((DynamicPercept) percept, "bump")) {
	    	
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
	    }else if (getAttribute((DynamicPercept) percept, "dirt"))
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
		
	}
}

public class MyAgentForObsticles extends AbstractAgent {
    public MyAgentForObsticles() {
    	super(new MyAgentProgram2());
	}
}
