package bc19;

import java.awt.geom.Point2D;

public class Crusader {
	enum State{
		DEFEND, ATTACK
	}
	public static State state = State.DEFEND;
	
	public static MyRobot bot;
	public static boolean enemiesDetected;	
	public static boolean hasAttackCmd;
	public static boolean shouldDefend;
	boolean once = true;

	public static Point2D myChurch;//or castle
	
	public static int numEnemyRobots;
	public static Robot enemyRobotList = new Robot[4096];
	
	//for making changes to the state before acting.
	public static void determineState() {
		switch(state) {
		case DEFEND:
			if(hasAttackCmd) state = State.ATTACK;
			break;
		case ATTACK:
			if(shouldDefend) state = State.DEFEND;
			break;
		}
	}
	
	public static Point2D adjacentChurch() {
		return adjacentChurch(bot.me.x, bot.me.y);
	}
	
	public static Point2D adjacentChurch(int cx, int cy){
		int x, y;
		int u;
		Robot temp;
		for(int i = 0; i < 8; i++) {
			x = bot.d_list[i][0] + cx;
			y = bot.d_list[i][1] + cy;
			//so this is like, make sure its in bounds, and see if its a church or castle
			if(bot.path.isPointInBounds(x, y) && (bot.robotMap[y][x] > 0)) {
				temp = bot.getRobot(bot.robotMap[y][x]);
				if((temp.unit == bot.SPECS.CASTLE || temp.unit == bot.SPECS.CHURCH) && temp.team == bot.me.team){
					return new Point2D(bot.d_list[i][0], bot.d_list[i][1]);
				}
			}
		}
		return null;
	}
	
	public static boolean canAttack(int x, int y) {
		int dx = bot.me.x - x;
		int dy = bot.me.y - y;
		
		return ((dx*dx + dy*dy) <= bot.attack_radius_sq) && bot.fuel >= bot.attack_fuel_cost;
	}
	
	public static void processEnv() {
		int id, unit, team;
		
		numEnemyRobots = 0;
		
		for(int i = 0; i < bot.robotList.length; i++) {
			team = bot.robotList[i].team;
			id = bot.robotList[i].id;
			if(team != bot.me.team) {
				enemyRobotList[numEnemyRobots] = bot.robotList[i];
				bot.knownEnemyRobots[id] = bot.robotList[i];
				numEnemyRobots++;
				enemiesDetected = true;
			}
		}
	}
	
	public static void resetState() {
		enemiesDetected = false;
		hasAttackCmd = false;
		shouldCommand = true;
	}
	
	public static void talkToCastle() {
		CastleTalker.sendOK();
	}
	
	public static void processSignals() {
		
	}
	
	public static Point2D pickDefensePoint() {
		int x, y;
		for(int dx = -3; dx <= 3; dx++) {
			for(int dy = -3; dy <= 3; dy++) {
				x = bot.me.x + dx;
				y = bot.me.y + dy;
				
				if(!isPointInBounds(x, y)) continue;
				if(dy == 0 && dx == 0) continue;
				
				if(bot.r_map[y][x] == 0 && bot.map[y][x] && !bot.fuelMap[y][x] && !bot.karboniteMap[y][x]) {
					return new Point2D(x, y);
				}
			}
		}
		return null;
	}
	
	public static Point2D whereIsEnemy(Robot enemy) {
		if(bot.isVisible(enemy.id))
			return new Point2D(enemy.x, enemy.y);
		return null;
	}
	
	public static void defendAgainstEnemy(Robot enemy) {
		Point2D enemyPos;
	}
	
	public static Action turn() {
		Point2D target;
		Point2D source = new Point2D(bot.me.x, bot.me.y);
		Point2D dydx; //move
		
		if(once) {
			once = false;
			myChurch = adjacentChurch(); //this is always true. If not then logic as we know it must be abandoned
		}
		
		resetState();
		
		processEnv();
		processSignals();
		talkToCastle();
		determineState();
		
		switch(state) {
		case DEFEND: //get a tile away from the castle/church
			if(enemiesDetected) {
				for(int i = 0; i < numEnemyRobots; i++) {
					defendAgainstEnemy(enemyRobotList[i]);
				}
			}
			else {
				target = pickDefensePoint();
				dydx = bot.path.get_move(source, target, bot.fuel);
				return bot.move(dydx.getX(), dydx.y);
			}
			
			break;
		case ATTACK:
			break;
		}
		
		return null;
	}
	
}
