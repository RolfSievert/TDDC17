package tddc17;




import aima.core.environment.liuvacuum.*;
import aima.core.util.datastructure.Queue;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.TreeMap;

class Pos
{
	public int x;
	public int y;
	
	public Pos(int x, int y){
		this.x =x;
		this.y=y;
	}
}

class MyAgentState
{
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
	
	// States for the agent 
	public static final int SEARCH 		= 0; // Searching for the next unknown cell 
	public static final int MOVE		= 1; // Moving to chosen unknown cell
	public static final int COMPLETE 	= 2; // Done
	
	// Our current state
	public int agent_mode 				= 0; // Starting in search mode
										 //	where we find our next target unknown cell
	
	// boolean used to determine when we are finished
	public boolean last = false;
	
	// The chosen goal for A*
	public SimpleEntry<Integer, Integer> agent_goal; // current goal
	// path to goal produced by A*, not taking into account the need to turn.
	public LinkedList<SimpleEntry<Integer, Integer>> agent_path; 
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;

	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
	    {
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

	}

	public void updateWorld(int x_position, int y_position, int info)
	{
		if(x_position < world.length && x_position > -1 && y_position < world.length && y_position > -1){
		world[x_position][y_position] = info;
		}
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

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();

	// Here you can define your variables!
	public int iterationCounter = 10000;
	// Could use a TreeSet with a comparator for sorting if it's required.
	// Set containing the unknown cells the vacuum has cleaned.
	public HashSet<SimpleEntry<Integer, Integer>> unknownSet= new HashSet<SimpleEntry<Integer, Integer>>();
	
	public MyAgentState state = new MyAgentState();

	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction<0)
		    	state.agent_direction +=4;
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
		if(state.agent_mode == MyAgentState.COMPLETE){
			return NoOpAction.NO_OP;
		}

		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initnialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initnialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}

    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!

    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);


	    iterationCounter--;

	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);


	    // State update based on the percept value and the last action
	    state.updatePosition((DynamicPercept)percept);
	    if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				unknownSet.remove(new SimpleEntry<Integer, Integer>(state.agent_x_position, state.agent_y_position-1));
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				unknownSet.remove(new SimpleEntry<Integer, Integer>(state.agent_x_position+1, state.agent_y_position));
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				unknownSet.remove(new SimpleEntry<Integer, Integer>(state.agent_x_position, state.agent_y_position+1));
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				unknownSet.remove(new SimpleEntry<Integer, Integer>(state.agent_x_position-1, state.agent_y_position));
				break;
			}
			state.agent_mode = MyAgentState.SEARCH;
	    }
	    if (dirt && !home){
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    }
	    else if(!home){
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    }
	    
	   unknownSet.remove(new SimpleEntry<Integer, Integer>(state.agent_x_position, state.agent_y_position));


	    state.printWorldDebug();
	    
	    updateUnknownSet();
	    
	    // Next action selection based on the percept value
	    if (dirt)
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    }
	    else
	    {
	    	SimpleEntry<Integer, Integer> current_pos = new SimpleEntry<Integer, Integer>(state.agent_x_position, state.agent_y_position); 
	    	
	    	if(state.agent_mode == MyAgentState.SEARCH)
	    	{		
	    		state.agent_goal = chooseGoal();
	    		state.agent_path = A_star(current_pos, state.agent_goal);
	    		state.agent_mode = MyAgentState.MOVE;
	    	}
	    	if(state.agent_mode == MyAgentState.MOVE)
	    	{
	    		LinkedList<SimpleEntry<Integer, Integer>> path = state.agent_path;
	    		if(path.size() == 0){
	    			return NoOpAction.NO_OP;
	    		}
	    		int goal_x = path.peek().getKey();
	    		int goal_y = path.peek().getValue();
	    		int pos_x = current_pos.getKey();
	    		int pos_y = current_pos.getValue();
	    		
	    		int dy = goal_y - pos_y;
	    		int dx = goal_x - pos_x;
	    		
	    		int dir = state.agent_direction;
	    		// Rotation variables
	    		boolean r_right = false;
	    		//boolean r_left = false;
	    		if (dy < 0) {
	    			if (dir != MyAgentState.NORTH){
	    				r_right = true;
	    			}
	    		}
	    		else if(dy > 0) {
	    			if (dir != MyAgentState.SOUTH){
	    				r_right = true;
	    			}
	    		}
	    		else if(dx > 0) {
	    			if (dir != MyAgentState.EAST){
	    				r_right = true;
	    			}
	    		}
	    		else if(dx < 0) {
	    			if (dir != MyAgentState.WEST){
	    				r_right = true;
	    			}
	    		}
	    		if (r_right){
	    			state.agent_last_action=state.ACTION_TURN_RIGHT;
					updateDirection(state.ACTION_TURN_RIGHT);
			    	return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	    		}
	    		else{
	    			state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    			state.agent_path.pop();
	    			if(path.size() == 0){
		    			state.agent_mode = MyAgentState.SEARCH;
		    			if(state.last){
		    				state.agent_mode = MyAgentState.COMPLETE;
		    			}
	    			}
		    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    		}
	    	}
	    }
	    return NoOpAction.NO_OP;
	}

	public boolean inBounds(int x, int y){
		return (x < state.world.length && x > -1 && y < state.world.length && y > -1);
	}
	
	public double distFromAgent(int x , int y){
		return Math.sqrt(Math.pow(state.agent_x_position - x, 2) + Math.pow(state.agent_y_position - y, 2));
	}
	
	public double cellDist(SimpleEntry<Integer, Integer> start, SimpleEntry<Integer, Integer> goal){
		return Math.sqrt(Math.pow(start.getKey() - goal.getKey(), 2) + Math.pow(start.getValue() - goal.getValue(), 2));
	}
	
	public SimpleEntry<Integer, Integer> chooseGoal(){
		SimpleEntry<Integer, Integer> goal = null;
		double goalDistance = Double.POSITIVE_INFINITY;
		
		for(SimpleEntry<Integer, Integer> elem : unknownSet){
			double currentDistance = distFromAgent (elem.getKey(), elem.getValue());
			if(currentDistance < goalDistance){
				goal = elem;
				goalDistance = currentDistance;
			}
		}
		if (goal == null){
			goal = new SimpleEntry<Integer, Integer>(1, 1);
			state.last = true;
		}
		return goal;
	}
	
	public LinkedList<SimpleEntry<Integer, Integer>> A_star(SimpleEntry<Integer, Integer> start, SimpleEntry<Integer, Integer> goal){
		
		//The set of nodes already evaluated
		HashSet<SimpleEntry<Integer, Integer>> closedSet = new HashSet<SimpleEntry<Integer, Integer>>();
		
		// The set of currently discovered nodes that are not evaluated yet.
	    // Initially, only the start node is known.
	    HashSet<SimpleEntry<Integer, Integer>> openSet = new HashSet<SimpleEntry<Integer, Integer>>();
	    openSet.add(start);
	    
	    // For each node, which node it can most efficiently be reached from.
	    // If a node can be reached from many nodes, cameFrom will eventually contain the
	    // most efficient previous step.
	    HashMap<SimpleEntry<Integer, Integer>, SimpleEntry<Integer, Integer>> cameFrom = new HashMap<SimpleEntry<Integer, Integer>, SimpleEntry<Integer, Integer>>();
	    
	    // For each node, the cost of getting from the start node to that node.
	    HashMap<SimpleEntry<Integer, Integer>, Double> gScore = new HashMap<SimpleEntry<Integer, Integer>, Double>();
	    // The cost of going from start to start is zero.
	    gScore.put(start, (double) 0);
	    
	    // For each node, the total cost of getting from the start node to the goal
	    // by passing by that node. That value is partly known, partly heuristic.
	    HashMap<SimpleEntry<Integer, Integer>, Double> fScore = new HashMap<SimpleEntry<Integer, Integer>, Double>();
	    
	    // For the first node, that value is completely heuristic.
	    fScore.put(start, cellDist(start, goal));
	    
	    while (openSet.size() != 0){
	    	SimpleEntry<Integer, Integer> current = chooseCurrent(fScore, openSet);
	    	
	    	if(current.getKey() == goal.getKey() && current.getValue() == goal.getValue()){
	    		return reconstructPath(cameFrom, current);
	    	}
	    	
	    	openSet.remove(current);
	    	closedSet.add(current);
	    	ArrayList<SimpleEntry<Integer, Integer>> neighborList = neighbors(current);
	    	
	    	for(SimpleEntry<Integer, Integer> neighbour : neighborList){
	    		if(closedSet.contains(neighbour)){
	    			continue; // Ignore the neighbour which is already evaluated.
	    		}
	    		
	    		// The distance from start to a neighbour
	    		double tentativeGScore = gScore.get(current) + cellDist(current, neighbour);
	    		
	    		if(!openSet.contains(neighbour)){ // Discover a new node
	    			openSet.add(neighbour); 
	    		}
	    		else if( tentativeGScore >= gScore.get(neighbour)){ // This is not a better path.
	    			continue;
	    		}
	    		
	    		cameFrom.put(neighbour, current);
	    		gScore.put(neighbour, tentativeGScore);
	    		fScore.put(neighbour, gScore.get(neighbour) + cellDist(neighbour, goal));
	    		
	    	}
	    	
	    }
	  return null;
	}
	
	public LinkedList<SimpleEntry<Integer, Integer>> reconstructPath(HashMap<SimpleEntry<Integer, Integer>, SimpleEntry<Integer, Integer>> cameFrom, SimpleEntry<Integer, Integer> current){
	LinkedList<SimpleEntry<Integer, Integer>> queuePath = new LinkedList<SimpleEntry<Integer, Integer>>();
	queuePath.add(current);
	while(cameFrom.containsKey(current)){
		current = cameFrom.get(current);
		queuePath.addFirst(current);
	}
	queuePath.pop();
	return queuePath;
	}
	
	public SimpleEntry<Integer, Integer> chooseCurrent(Map<SimpleEntry<Integer, Integer>, Double> fScore, HashSet<SimpleEntry<Integer, Integer>> openSet ){
		SimpleEntry<Integer, Integer> bestEntry = null;
		double lowestScore = Double.POSITIVE_INFINITY;
		for( Map.Entry<SimpleEntry<Integer, Integer>, Double> entry :fScore.entrySet()){
			if(openSet.contains(entry.getKey()) && (entry.getValue() < lowestScore)){
				bestEntry = entry.getKey();
				lowestScore = entry.getValue();
			}
		}
		return bestEntry;
	}
	
	public ArrayList<SimpleEntry<Integer, Integer>> neighbors(SimpleEntry<Integer, Integer> cell){
		ArrayList<SimpleEntry<Integer, Integer>> neighborList = new ArrayList<SimpleEntry<Integer, Integer>>();
		// Can be turned into a for-loop
	    // Above
		
		int x = cell.getKey();
		int y = cell.getValue();
	    if(inBounds(x, y -1) && state.world[x][y - 1] != state.WALL ){
			neighborList.add(new SimpleEntry<Integer, Integer>(x , y - 1));
		}
	    // Right	    
	    if(inBounds(x + 1, y) && state.world[x + 1][y] != state.WALL){
			neighborList.add(new SimpleEntry<Integer, Integer>(x + 1, y));
		}
	    // Left
	    if(inBounds(x - 1, y) && state.world[x - 1][y] != state.WALL){
			neighborList.add(new SimpleEntry<Integer, Integer>(x - 1, y));
		}
	    // Below
	    if(inBounds(x, y + 1) && state.world[x][y + 1] != state.WALL){
			neighborList.add(new SimpleEntry<Integer, Integer>(x, y + 1));
		}
	    return neighborList;
	}
	
	
	public void updateUnknownSet(){
		// Can be turned into a for-loop
	    // Above
	    if(inBounds(state.agent_x_position, state.agent_y_position -1)
			&& (state.world[state.agent_x_position][state.agent_y_position -1] == state.UNKNOWN
			|| state.world[state.agent_x_position][state.agent_y_position -1] == state.HOME)){
			unknownSet.add(new SimpleEntry<Integer, Integer>(state.agent_x_position , state.agent_y_position -1));
		}
	    // Right	    
	    if(inBounds(state.agent_x_position + 1, state.agent_y_position )
	    	&& (state.world[state.agent_x_position + 1][state.agent_y_position] == state.UNKNOWN
	    	|| state.world[state.agent_x_position + 1][state.agent_y_position] == state.HOME)){
			unknownSet.add(new SimpleEntry<Integer, Integer>(state.agent_x_position + 1, state.agent_y_position));
		}
	    // Left
	    if(inBounds(state.agent_x_position -1, state.agent_y_position)
			&& (state.world[state.agent_x_position -1][state.agent_y_position] == state.UNKNOWN
			|| state.world[state.agent_x_position -1][state.agent_y_position] == state.HOME)){
			unknownSet.add(new SimpleEntry<Integer, Integer>(state.agent_x_position -1, state.agent_y_position));
		}
	    // Below
	    if(inBounds(state.agent_x_position, state.agent_y_position +1)
			&& (state.world[state.agent_x_position][state.agent_y_position +1] == state.UNKNOWN
			|| state.world[state.agent_x_position][state.agent_y_position +1] == state.HOME)){
			unknownSet.add(new SimpleEntry<Integer, Integer>(state.agent_x_position, state.agent_y_position +1));
		}
	}

	public void updateDirection(int action){
		//final int ACTION_NONE 			= 0;
		//final int ACTION_MOVE_FORWARD 	= 1;
		//final int ACTION_TURN_RIGHT 	= 2;
		//final int ACTION_TURN_LEFT 		= 3;
		//final int ACTION_SUCK	 		= 4;
		if (action == 3){ // LEFT
			switch(state.agent_direction){
				case MyAgentState.NORTH:
					state.agent_direction = MyAgentState.WEST;
					break;
				case MyAgentState.EAST:
					state.agent_direction = MyAgentState.NORTH;
					break;
				case MyAgentState.SOUTH:
					state.agent_direction = MyAgentState.EAST;
					break;
				case MyAgentState.WEST:
					state.agent_direction = MyAgentState.SOUTH;
					break;
			}
		}
		else if(action == 2){ //RIGHT
			switch(state.agent_direction){
				case MyAgentState.NORTH:
					state.agent_direction = MyAgentState.EAST;
					break;
				case MyAgentState.EAST:
					state.agent_direction = MyAgentState.SOUTH;
					break;
				case MyAgentState.SOUTH:
					state.agent_direction = MyAgentState.WEST;
					break;
				case MyAgentState.WEST:
					state.agent_direction = MyAgentState.NORTH;
					break;
				}
		}
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}