package bc19;

public class Castle {
	public static MyRobot bot;
	enum State {
		
	}
	int numPilgrims;
	Robot pilgrimList[];
	public static State state;
	//should mine fuel or karbonite?
	
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
	
	public static parseCastleTalk() {
		
	}
	
	public static processRobotList() {
		int id, team, command, unit, msg;
		
		boolean isVisible;
		for(int i = 0; i < bot.robotList.length; i++) {
			id = bot.robotList[i].id;
			team = bot.robotList[i].team;
			
			//check for visible enemies
			if(team != bot.me.team) {
				knownEnemyBots[id] = bot.robotList[i];
				continue;
			}
			
			bot.log("x: " + bot.robotList[i].x);
//			isVisible = bot.robotList[i].x == -1;
			
			msg = bot.robotList[i].castle_talk;
			command = CastleTalker.getCommand();
			switch()

			
			
			
		}
		
		
		
	}
	
	public static Action turn() {
		
		parseRobotList();
		
		if(numPilgrims != 1)
			return buildAnywhere(bot.SPECS.PILGRIM);
		
	}
}
