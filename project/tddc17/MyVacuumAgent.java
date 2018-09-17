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

	private int initialRandomActions = 10000;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public int iterationCounter = 10000;
	public MyAgentState2 state = new MyAgentState2();
	private ArrayList<Action> actionMemory = new ArrayList();

	@Override
	public Action execute(Percept percept) {
		
		System.out.println("execute");		
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
			System.out.println("Going home");
	    	 return state.goHome((DynamicPercept)percept);
	    }else {
	    	//state.goHome = state.worldHasUnknowns();
	    	return chooseNextAction(percept);	    	
	    }
	}

	
    //This piece of code moves the "player" into a new position
    // Next action selection based on the percept value
	private Action chooseNextAction(Percept percept) {
	    
		//deal with dirt
		if (getAttribute((DynamicPercept) percept, "dirt")){
	    	return state.suck();
	    	
	    //deal with bump
	    }else if (getAttribute((DynamicPercept) percept, "bump")){
    		return surroundingsUnexplored();
    		
    	//deal with turns
    	}else if(!state.checkIfSurraundingsUnexplored()){
    			return surroundingsUnexplored();
		
    	} else return surroundingsExplored();

	    	//return state.agent_last_action != MyAgentState2.ACTION_MOVE_FORWARD ? state.moveAgentForward() : state.rotateAgentRandomDirection();
	}
	
	private Action surroundingsUnexplored(){
		
		if(state.isNextSquareUnexplored(MyAgentState2.FRONT_SQUARE) && state.agent_last_action != MyAgentState2.ACTION_MOVE_FORWARD)
    		return state.moveAgentForward();
	    else if (state.isNextSquareUnexplored(MyAgentState2.LEFT_SQUARE))
    		return state.rotateAgentLeft();
	    else return state.rotateAgentRight();
	}
	
	private Action surroundingsExplored(){
		
		if(state.isNextSquareWalkable(MyAgentState2.FRONT_SQUARE))
			return state.moveAgentForward();
		else
			return state.agent_last_action != MyAgentState2.ACTION_MOVE_FORWARD ? state.moveAgentForward() : state.rotateAgentRandomDirection();
		
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

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
