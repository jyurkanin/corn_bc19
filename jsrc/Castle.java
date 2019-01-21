package bc19;

public class Castle {
	public static MyRobot bot;
	enum State {
		EARLY, MID, LATE
	}
		
	static int numPilgrims, numPreachers, numProphets, numCrusaders, numChurches;
	static int numAdjCrusaders;
	
	
	static Robot pilgrimList[];
	
	static int numEnemyRobots;
	static Robot enemyRobotList[] = new Robot[4096];
	
	static State state = State.EARLY;
	
	static boolean unitCounted;
	static boolean nearbyEnemy;
	
	public static Action buildAnywhere(int u) {
    	if(bot.fuel < bot.SPECS.UNITS[u].CONSTRUCTION_FUEL) return null;
    	if(bot.karbonite < bot.SPECS.UNITS[u].CONSTRUCTION_KARBONITE) return null;
    	int x, y;
    	for(int i = 0; i < 8; i++) {
    		x = bot.me.x + bot.d_list[i][0];
    		y = bot.me.y + bot.d_list[i][1];
    		
    		if(bot.path.isPointInBounds(x, y) && (bot.robotMap[y][x] == 0) && bot.map[y][x]) {
    			return bot.buildUnit(u, bot.d_list[i][0], bot.d_list[i][1]);
    		}
    	}
    	return null;
    }
	
	//theres no real reason that a castle would have to parse signals I dont think...
	public static void parseSignal(int msg) {
		return;
	}
	
	public static void parseCastleTalk(int msg, Robot r) {
		int id, team, command, unit;
		
		command = CastleTalker.getCommand(msg);
		unit = CastleTalker.getUnit(msg);
		id = r.id;
		
		//Team Analysis START, counts units.
		if(command == CastleTalker.ALL_GOOD) {
			countUnits(unit);
			if(bot.knownTeamBots[id] == null) {
				bot.knownTeamBots[id] = r;
				bot.knownTeamBots[id].unit = unit;
			}
			else {
				bot.knownTeamBots[id].unit = unit;
			}
		}
		else if(bot.knownTeamBots[id] != null && bot.knownTeamBots[id].unit != -1){
			unit = bot.knownTeamBots[id].unit;
			countUnits(unit);
		}
		//TEAM Analysis END
		
		
	}
	
	public static void resetUnitCount() {
		numPilgrims = 0;
		numPreachers = 0;
		numProphets = 0;
		numCrusaders = 0;
		numChurches = 0;
		
		numAdjCrusaders = 0;
		
		numEnemyRobots = 0;
	}
	//should mine fuel or karbonite?
	public static void countUnits(int unit) {
		if(unitCounted) return; //this gets reset during iteration over robotList. Avoids double counting.
		unitCounted = true;
		switch(unit) {
		case Params.PILGRIM:
			numPilgrims++;
			break;
		case Params.CRUSADER:
			numCrusaders++;
			break;
		}		
	}
	//should mine fuel or karbonite?
	//counts robots on our team through castle_talk
	//gets reports and scans for enemy robots
	public static void processRobotList() {
		int id, team, command, unit, msg, tempx, tempy;
		
		boolean isVisible;
		nearbyEnemy = false;
		
		resetUnitCount();
		
		for(int i = 0; i < bot.robotList.length; i++) {
			id = bot.robotList[i].id;
			team = bot.robotList[i].team;
			unitCounted = false;
			
			//check for visible enemies
			if(team != bot.me.team) {		

				bot.knownEnemyBots[id] = bot.robotList[i];
				enemyRobotList[numEnemyRobots] = bot.robotList[i];
				numEnemyRobots++;
				if(bot.isVisible(bot.robotList[i])) {
					nearbyEnemy = true;
				}
				continue;
			}
			
			//these are just an experiment to see what it prints when it is out of sight range
			
//			isVisible = bot.robotList[i].x == -1;
			
			msg = bot.robotList[i].castle_talk;
			if(msg != 0) { //we got one
				parseCastleTalk(msg, bot.robotList[i]);
			}
			
			msg = bot.robotList[i].signal;
			if(msg != 0) {
				parseSignal(msg);
			}
			
			if(bot.isVisible(bot.robotList[i])) {
				unit = bot.robotList[i].unit;
				tempx = bot.robotList[i].x;
				tempy = bot.robotList[i].y;
				if(unit == Params.CRUSADER && bot.path.getSquareDist(bot.me.x, bot.me.y, tempx, tempy) <= 16) {
					numAdjCrusaders++;
				}
			}
			
			if(bot.knownTeamBots[id] == null) { //adds robot if its new
				bot.knownTeamBots[id] = bot.robotList[i];
			}
			else { //checks to see if it has new information about robot
				unit = bot.robotList[i].unit;
				if(bot.isVisible(bot.robotList[i])) {
					bot.knownTeamBots[id].unit = unit;
					countUnits(unit);
				} 
			}
			
			
			
		}		
	}
	
	//For defensive strategies
	public static Robot getClosestEnemy() {
		int minindex, mindist, minx, miny, dist, x, y;
		mindist = 100000000;
		minindex = -1;
		for(int i = 0; i < numEnemyRobots; i++) {
			x = enemyRobotList[i].x;
			y = enemyRobotList[i].y;
			
			dist = bot.path.getSquareDist(x, y, bot.me.x, bot.me.y);
			if(dist < mindist) {
				minx = x;
				miny = y;
				mindist = dist;
				minindex = i;
			}
		}
		
		if(minindex == -1) return null;
		return enemyRobotList[minindex];
	}
	
	public static void sendSignals() {
		Robot temp;
		if(nearbyEnemy) {
			temp = getClosestEnemy();
			if(temp != null) {
				Radio.enemyNearChurch(temp);
			}
		}
	}
	
	public static Action turn() {
		Robot temp;
		
		//data in. This processes nearby robots, parses signals, and castle talk.
		processRobotList();
		
		
		//data out
		sendSignals();
		
		
		switch(state) {
		case EARLY:
			temp = getClosestEnemy();
			bot.debug("Early Game");
			if(temp != null && bot.canAttack(temp.x, temp.y))
				return bot.attack(temp.x, temp.y);
			
			bot.debug("Didn't attack");
			if(numPilgrims < (bot.num_mines * Params.INITIAL_PILGRIMS_FRAC))
				return buildAnywhere(bot.SPECS.PILGRIM);
			bot.debug("Didn't build pilgrim");
			if(numCrusaders < 20)
				return buildAnywhere(bot.SPECS.CRUSADER);
			bot.debug("Didn't build crusader");
			break;
		case MID:
			break;
		case LATE:
			break;
		}
		return null;
	}
}
