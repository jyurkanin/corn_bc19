package bc19;

public class Castle {
	public static MyRobot bot;
	enum State {
		
	}
	static int numPilgrims, numPreachers, numProphets, numCrusaders, numChurches;
	Robot pilgrimList[];
	public static State state;
	
	static boolean unitCounted;
	
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
		
		//Team Analysis START, counts units.
		if(command == CastleTalker.ALL_GOOD) {
			bot.log("Got all good. Counting unit");
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
			bot.log("Got a different signal but we can still count the unit");
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
	}
	//should mine fuel or karbonite?
	public static void countUnits(int unit) {
		if(unitCounted) return; //this gets reset during iteration over robotList. Avoids double counting.
		unitCounted = true;
		switch(unit) {
		case Params.PILGRIM:
			numPilgrims++;
			break;
		}		
	}
	//should mine fuel or karbonite?
	//counts robots on our team through castle_talk
	//gets reports and scans for enemy robots
	public static void processRobotList() {
		int id, team, command, unit, msg;
		
		boolean isVisible;
		
		resetUnitCount();
		
		for(int i = 0; i < bot.robotList.length; i++) {
			id = bot.robotList[i].id;
			team = bot.robotList[i].team;
			unitCounted = false;
			
			//check for visible enemies
			if(team != bot.me.team) {
				bot.knownEnemyBots[id] = bot.robotList[i];
				continue;
			}
			
			//these are just an experiment to see what it prints when it is out of sight range
			bot.log("numPilgrims: " + numPilgrims);
//			bot.log("x: " + bot.robotList[i].x);
//			bot.log("CASTLE x, y " + bot.me.x + " " + bot.me.y);
			
//			isVisible = bot.robotList[i].x == -1;
			
			msg = bot.robotList[i].castle_talk;
			if(msg != 0) { //we got one
				bot.log("parsing castle talk");
				parseCastleTalk(msg, bot.robotList[i]);
			}
			
			msg = bot.robotList[i].signal;
			if(msg != 0) {
				parseSignal(msg);
			}
			
			if(bot.knownTeamBots[id] == null) {
				bot.knownTeamBots[id] = bot.robotList[i];
			}
			else {
				unit = bot.robotList[i].unit;
				if(bot.isVisible(bot.robotList[i])) {
					bot.log("Unit visible, counting it");
					bot.knownTeamBots[id].unit = unit;
					countUnits(unit);
				} 
			}
			
		}
		
		
	}
	
	public static Action turn() {
		
		processRobotList();
		
		if(numPilgrims < 2)
			return buildAnywhere(bot.SPECS.PILGRIM);
		return null;
	}
}
