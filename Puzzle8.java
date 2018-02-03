import java.io.*;
import java.util.*;

class Puzzle8 {

    //Key -> 1st String
    static HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
    static HashMap<String, String> parent = new HashMap<String, String>(); 
    static HashMap<String, Integer> level = new HashMap<String, Integer>();
    static HashMap<String, Integer> hOfNode = new HashMap<String, Integer>();
    static long nNodes;

    public static void generalSearch(String initialState, int searchType) {  //BFS / DFS / GREEDY //A*
	
	long startTime = System.currentTimeMillis();
	LinkedList<String> nodes = new LinkedList<String>();
	long totalTime;
	nodes.addLast(initialState);
	nNodes=0;
	
	while(!nodes.isEmpty()) {
	    
	    String actualState="";
	   
	    if(searchType == 5 || searchType == 4) {
		actualState = chooseBest(nodes);
		nodes.remove(actualState);
	    }
	    else
	        actualState = nodes.removeFirst();
	    
	    if(isGoal(actualState)) {
		totalTime = System.currentTimeMillis() - startTime;
		printPath(actualState, initialState, totalTime, 0);
		System.exit(0);
	    }
	    
	    nodes = makeDescendants(actualState, nodes, searchType, 0);
	    visited.put(actualState, true);
	    
	}
    }
    
    public static void generalSearchIDFS(String initialState, int searchType) {  //IDFS
	
	long startTime = System.currentTimeMillis();
	LinkedList<String> nodes = new LinkedList<String>();
	nodes.addLast(initialState);
	long totalTime;
	int lvl = 0;
	nNodes = 0;
	
	while(true){
	    
	    parent.clear();
	    level.clear();
	    visited.clear();
	    
	    parent.put(initialState, null);
	    level.put(initialState, 0);
	    visited.put(initialState, false);

	    nodes.addLast(initialState);
	    
	    while(!nodes.isEmpty()) {	    
		String actualState = nodes.removeFirst();
	    
		if(isGoal(actualState)) {
		    totalTime = System.currentTimeMillis() - startTime;
		    printPath(actualState, initialState, totalTime, 3);
		    System.exit(0);
		}
		
		nodes = makeDescendants(actualState, nodes, searchType, lvl);
		visited.put(actualState, true);
	    }
	    lvl++;
	}
    }
    public static String chooseBest(LinkedList<String> nodes) {

	int lowerH = Integer.MAX_VALUE;
	String best = "";
	
	for(int i=0 ; i<nodes.size() ; i++) {  
	    if( hOfNode.get(nodes.get(i)) <= lowerH ) {
		lowerH = hOfNode.get(nodes.get(i));
		best = nodes.get(i);
	    }
	}
        
	return best;
    }
    
    public static void printPath(String actualState, String initialState, long totalTime, int searchType) {
	
	LinkedList<String> Path = new LinkedList<String>();
      
	while(!actualState.equals(initialState)) {

	    Path.addFirst(actualState);
	    actualState = parent.get(actualState);
	    
	}

	Path.addFirst(initialState);
	while(!Path.isEmpty()){
	    String out = Path.removeFirst();
	    System.out.println("Nível: " + level.get(out));
	    for(int i = 0 ; i < 9 ; i+=3)
		System.out.println(out.charAt(i) + " " + out.charAt(i+1) + " " + out.charAt(i+2));
	    System.out.println();
				   

	}
	
	System.out.println("Nivel da soluçao: " + level.get("123804765"));
	System.out.println("Time: " + totalTime + " ms");
	System.out.println("Total nodes generated: " + nNodes);
		
    }
    public static LinkedList<String> makeDescendants(String actualState, LinkedList<String> nodes, int searchType, int profMax) {
	
	int hole = findHole(actualState);
	int parentLevel = level.get(actualState);
	String newState;
	LinkedList<String> descendantList = new LinkedList<String>();
	
	if(hole != 0 && hole != 1 && hole != 2) { //up
	    newState = swapPosition(actualState, hole, hole-3); 
	    descendantList.addLast(newState);
	}

	if(hole != 2 && hole != 5 && hole != 8) { //right
	    newState = swapPosition(actualState, hole, hole+1);
	    descendantList.addLast(newState); 
	}

	if(hole != 0 && hole != 3 && hole != 6) { //left
	    newState = swapPosition(actualState, hole, hole-1);
	    descendantList.addLast(newState);
	}

	if(hole != 6 && hole != 7 && hole != 8) { //down
	    newState = swapPosition(actualState, hole, hole+3);
	    descendantList.addLast(newState);
	}

	while(!descendantList.isEmpty()) {

	    String testState = descendantList.removeFirst();
		
	    switch (searchType) {
	    case 1:
		if(!visited.containsKey(testState)) {
		    visited.put(testState, false);
		    level.put(testState, parentLevel + 1);
		    parent.put(testState, actualState);
		    nodes.addLast(testState);
		    nNodes++;
		}
		break;	    		    
	    case 2:
		if(!visited.containsKey(testState)) {
		    visited.put(testState, false);
		    level.put(testState, parentLevel + 1);
		    parent.put(testState, actualState);
		    nodes.addFirst(testState);
		    nNodes++;
		}
		break;
	    case 3:
		if(visited.containsKey(testState)) {
		    
		    int testStatelevel = level.get(testState);
		    if(parentLevel + 1 <= testStatelevel) {

			parent.put(testState, actualState);
			level.put(testState, parentLevel + 1);
			visited.put(testState, false);
			nodes.addFirst(testState);
			nNodes++;
		    }
		}
		else
		    if(parentLevel + 1 <= profMax){
			visited.put(testState, false);
			level.put(testState, parentLevel + 1);
			parent.put(testState, actualState);
			nodes.addFirst(testState);
			nNodes++;
		    }
		
		break;
	    case 4:
		if(visited.containsKey(testState)) {	    
		    int testStatelevel = level.get(testState);
		    if(parentLevel + 1 <= testStatelevel) {
			parent.put(testState, actualState);
			level.put(testState, parentLevel + 1);
			visited.put(testState, false);
			calcHeuristic(testState, 4);
			nodes.addFirst(testState);
			nNodes++;
		    }
		}
		else if(!visited.containsKey(testState)) {
		    visited.put(testState, false);
		    parent.put(testState, actualState);
		    level.put(testState, parentLevel + 1);
		    calcHeuristic(testState, 4);
		    nodes.addFirst(testState);
		    nNodes++;
		}
		break;
	    case 5:
		if(visited.containsKey(testState)) {	    
		    int testStatelevel = level.get(testState);
		    if(parentLevel + 1 <= testStatelevel) {
			parent.put(testState, actualState);
			level.put(testState, parentLevel + 1);
			visited.put(testState, false);
			calcHeuristic(testState, 5);
			nodes.addFirst(testState);
			nNodes++;
		    }
		}
		else
		    if(!visited.containsKey(testState)) {
		    visited.put(testState, false);
		    parent.put(testState, actualState);
		    level.put(testState, parentLevel + 1);
		    calcHeuristic(testState, 5);
		    nodes.addFirst(testState);
		    nNodes++;
		}
		break;
	    }
	}	
	return nodes;
    }

    public static void calcHeuristic(String testState, int searchType) { 	//Manhattan distance

	String goalState = "123804765";
	int[][] CurrMatrix = new int[3][3];
	int[][] GoalMatrix = new int[3][3];
	int w = 0;
    
	for(int x=0 ; x<3; x++) 
	    for(int y=0; y<3 ;y++) {
		CurrMatrix[x][y] = testState.charAt(w);
		GoalMatrix[x][y] = goalState.charAt(w);
		w++;
	    }
	
	int ManDist = 0;
	for(int x=0 ; x<3 ; x++) {
	    for(int y=0 ; y<3 ;y++) {

		for(int u=0 ; u<3 ; u++) 
		    for(int v=0 ; v<3 ; v++) 
			if(GoalMatrix[u][v] == CurrMatrix[x][y]) 
			    ManDist += Math.abs(u-x) + Math.abs(v-y);
	    }
	}

	if(searchType == 5)
	    ManDist += level.get(testState);
	
	hOfNode.put(testState, ManDist);			    
    }
    
    public static String swapPosition(String actualState, int hole, int change) {

	char[] array = actualState.toCharArray();

        array[hole] = array[change];
        array[change] = '0';
	
	String swappedString = new String(array);
	
	return swappedString;
	
    }
    
    public static int findHole(String actualState) {
	
	for(int i=0 ; i<9 ; i++)
	    if(actualState.charAt(i) == '0')
		return i;

	return 0;
	
    }
    
    public static int isPossible(String actualState) {

	int paridade = 0;
	for(int i=0; i<8 ; i++)
	    for(int j=i+1; j<9; j++)
		if(actualState.charAt(i) != '0' && actualState.charAt(j) != '0' && actualState.charAt(i) > actualState.charAt(j))
		    paridade++;
	
	if(paridade % 2 == 0)
	    return 0;

	return 1;
    }    
    public static boolean isGoal(String actualState) {

	String goalState = "123804765";

	if(goalState.equals(actualState)){ return true; }

	return false;
	
    }
    
    public static void main(String[] args) {
	
	Scanner in = new Scanner(System.in);

	System.out.println("Introduza a matriz inicial:");
	String InitialState = in.nextLine();
	System.out.println("Introduza a matriz final:");
	String FinalState = in.nextLine();
	
	visited.put(InitialState, false);
	parent.put(InitialState, null);
	level.put(InitialState, 0);
	nNodes++;

	if(isGoal(InitialState)) {
	    System.out.println("Solution found: 0 steps");
	    System.exit(0);
	}
	
        if(isPossible(InitialState) != isPossible(FinalState)) {
	    System.out.println("Not possible");
	    System.exit(0);
	}
	System.out.println("Escolha o numero da pesquisa a efetuar:");
	System.out.printf(" 1 - Pesquisa em Largura (BFS) \n 2 - Pesquisa em Profundidade (DFS) \n 3 - Pesquisa em Profundidade Iterativa (PPI) \n 4 - Pesquisa Greedy \n 5 - Pesquisa A* \n");  

	int escolha = in.nextInt();


	
	switch (escolha) {

	case 1: generalSearch(InitialState, 1);
	    break;

	case 2: generalSearch(InitialState, 2);
	    break;
	    
	case 3:
	    generalSearchIDFS(InitialState, 3);
	    break;

	case 4:
	    calcHeuristic(InitialState, 4);
	    generalSearch(InitialState, 4);
	    break;
		
	case 5:
	    calcHeuristic(InitialState, 5);
	    generalSearch(InitialState, 5);
	    break;
	}
    }
}

