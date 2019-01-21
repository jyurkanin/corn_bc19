package bc19;

public class Crusader {
	enum State{
		DEFEND, ATTACK
	}
	public static State state = State.DEFEND;
	
	public static MyRobot bot;
	public static boolean enemiesDetected;
	public static boolean enemyDefeated;
	public static boolean hasAttackCmd;
	public static boolean shouldDefend;
	public static boolean hasRequestForTarget;
	public static boolean shouldRequestTarget;
	public static boolean hasNewTarget;
	public static boolean once = true;

	public static Point2D myChurch;//or castle
	
	public static int countNearbyTeamCombatants; // this counts the number of visible robots on our team that can attack
	public static int farthestTeamCombatant;
	
	public static int farthestTargetRequest;
	
	public static int numEnemyRobots;
	public static Robot enemyRobotList[] = new Robot[4096];
	public static Point2D targetedBot;
	public static int targetedBotUnit;
	
	//for making changes to the state before acting.
	public static void determineState() {
		switch(state) {
		case DEFEND:
			if(hasAttackCmd) state = State.ATTACK;
			break;
		case ATTACK:
			if(countNearbyTeamCombatants < 3 || !hasAttackCmd || shouldDefend) state = State.DEFEND;
			break;
		}
	}
	
	public static void sendSignals() {
		if(hasNewTarget && countNearbyTeamCombatants > 0) { //figure out how much range the signal needs.
			hasNewTarget = false; //lets just be really generous with the signal distance since the fuel cost is only proportional to the radius of the signal
			Radio.attackRobot(targetedBot, targetedBotUnit, 4*farthestTeamCombatant);
		}
		else if(shouldRequestTarget) {
			Radio.requestTarget(farthestTeamCombatant);
		}
		else if(hasRequestForTarget) {
			Radio.attackRobot(targetedBot, targetedBotUnit,farthestTargetRequest);
		}
		
		
	}
	
	public static void processEnv() {
		int id, unit, cmd, team, msg, temp_dist;
		Point2D temp;
		
		
		for(int i = 0; i < bot.robotList.length; i++) {
			team = bot.robotList[i].team;
			id = bot.robotList[i].id;
			if(team != bot.me.team) {
				enemyRobotList[numEnemyRobots] = bot.robotList[i];
				bot.knownEnemyBots[id] = bot.robotList[i];
				numEnemyRobots++;
				enemiesDetected = true;
				continue;
			}
			
			if(bot.isVisible(bot.robotList[i])) { // 
				unit = bot.robotList[i].unit;
				if(unit == Params.CRUSADER || unit == Params.PREACHER || unit == Params.PROPHET) {
					countNearbyTeamCombatants++;
					temp_dist = bot.path.getSquareDist(bot.me.x, bot.me.y, bot.robotList[i].x, bot.robotList[i].y);
					if(temp_dist > farthestTeamCombatant)
						farthestTeamCombatant = temp_dist;
				}
			}
			
			if(bot.isRadioing(bot.robotList[i])) {
				msg = bot.robotList[i].signal;
				cmd = Radio.getCmd(msg);
				unit = Radio.getUnit(msg);
				
				switch(cmd) {
				case Radio.ATTACK_COMMAND:
					if(hasAttackCmd) continue; //only recieve one attack comand per turn.
					targetedBot = Radio.getPos(msg);
					targetedBotUnit = unit;
					hasAttackCmd = true;
					break;
				case Radio.DEFEND_COMMAND:
					shouldDefend = true;
					break;
				case Radio.ASK_TARGET_LOCATION_COMMAND: //this should hopefully just never happen and lets ignore it until testing determines whether we need it.
					hasRequestForTarget = true;
					temp_dist = bot.path.getSquareDist(bot.me.x, bot.me.y, bot.robotList[i].x, bot.robotList[i].y);
					if(temp_dist > farthestTargetRequest)
						farthestTargetRequest = temp_dist;
					break;
				}
			}
		}
		
		if(bot.robotMap[targetedBot.y][targetedBot.x] == 0) { //this bitch empty find a new one
			targetedBot = getClosestEnemy();
			if(targetedBot == null) { //okay I guess it makes sense to request target info here
				shouldRequestTarget = true;
			}
			else hasNewTarget = true; //gonna have to signal this to nearby crusaders.
		}
	}
	
	public static void resetState() {
		enemiesDetected = false;
		hasAttackCmd = false;
		shouldDefend = true;
		hasRequestForTarget = false;
		shouldRequestTarget = false;
		
		numEnemyRobots = 0;
		countNearbyTeamCombatants = 0;
		farthestTeamCombatant = 0;
		
	}
	
	public static void talkToCastle() {
		CastleTalker.sendOK();
	}
	
	public static Point2D pickDefensePoint() {
		return pickDefensePoint(myChurch.x, myChurch.y);
	}
	public static Point2D pickDefensePoint(int cx, int cy) {
		int x = 0;
		int y = 0;
		for(int dx = -3; dx <= 3; dx++) {
			for(int dy = -3; dy <= 3; dy++) {
				if((dx*dx + dy*dy) < Params.CASTLE_GUARD_RADIUS)
				x = cx + dx;
				y = cy + dy;
				
				if(!bot.path.isPointInBounds(x, y)) continue;
				if(dy == 0 && dx == 0) continue;
				
				if(bot.robotMap[y][x] == 0 && bot.map[y][x] && !bot.fuelMap[y][x] && !bot.karboniteMap[y][x]) {
					return new Point2D(x, y);
				}
			}
		}
		return null;
	}
	
	
	public static Point2D whereIsEnemy(Robot enemy) {
		if(bot.isVisible(enemy))
			return new Point2D(enemy.x, enemy.y);
		return null;
	}
	
	public static Point2D getClosestEnemy() {
		int minindex, mindist, minx, miny, dist, x, y;
		mindist = 100000000;
		minindex = -1;
		minx = -1;
		miny = -1;
		
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
		return new Point2D(minx, miny);
	}
	
	public static Action turn() {
		Point2D target;
		Point2D source = new Point2D(bot.me.x, bot.me.y);
		Point2D dydx; //move
		Point2D temp;
		
		if(once) {
			once = false;
			myChurch = bot.adjacentChurch(); //this is always true. If not then logic as we know it must be abandoned
		}
		
		resetState();
		
		processEnv();
		talkToCastle();
		determineState();
		
		sendSignals();
		
		switch(state) {
		case DEFEND: //get a tile away from the castle/church
			bot.debug("CRUSADER STATE: DEFEND");
			if(bot.path.getSquareDist(myChurch.x, myChurch.y, bot.me.x, bot.me.y) < Params.CASTLE_GUARD_RADIUS) {
				if(enemiesDetected) {
					temp = getClosestEnemy();
					bot.debug("CRUSADER temp: " + temp.x + " " + temp.y);
					if(temp != null && bot.canAttack(temp.x, temp.y)) return bot.attack(temp.x, temp.y);
					else {
						dydx = bot.getMove(source, temp, bot.fuel);
						bot.debug("CRUSADER dydx: " + dydx.x + " " + dydx.y);
						if(dydx != null) return bot.move(dydx.x, dydx.y);
					}
				}
			}
			else {
				target = pickDefensePoint();
				bot.debug("CRUSADER target: " + target.x + " " + target.y);
				dydx = bot.getMove(source, target, bot.fuel);
				bot.debug("CRUSADER dydx: " + dydx.x + " " + dydx.y);
				if(dydx != null) return bot.move(dydx.x, dydx.y);
			}
			break;
		case ATTACK:
			bot.debug("CRUSADER STATE: ATTACK");
			if(temp != null && bot.canAttack(targetedBot.x, targetedBot.y)) { //kill a bish
				return bot.attack(targetedBot.x, targetedBot.y);
			}
			if(bot.path.getSquareDist(targetedBot.x, targetedBot.y, bot.me.x, bot.me.y) > bot.attack_radius_sq) {
				target = pickDefensePoint();
				dydx = bot.getMove(source, target, bot.fuel);
				if(dydx != null) return bot.move(dydx.x, dydx.y);
			}
			break;
		}
		
		return null;
	}
	
}
