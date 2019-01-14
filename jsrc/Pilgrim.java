package bc19;

//holds pilgrim logic
public class Pilgrim {
	enum State {
		BUILD_CHURCH, MINE_K, MINE_F, DEPOSIT, NOTHING
	}
	
	public static State state = State.NOTHING;
	public static MyRobot bot;
	public static Point2D target;
	
	public static int numChurches = 0;
	public static Point2D churchList[] = new Point2D[100];
	public static Point2D churchesByID[] = new Point2D[4096];
	
	public static void determineState() {
		if(bot.me.fuel == 100) {
			state = State.DEPOSIT;
			return;
		}
		if(bot.me.karbonite == 20) {
			state = State.DEPOSIT;
			return;
		}
		
		switch(state) {
		case NOTHING:
			if(false) { //should I try to build a church? not sure what logic we need here.
				
			}
			else if(bot.karbonite < bot.SPECS.UNITS[bot.SPECS.PILGRIM].CONSTRUCTION_KARBONITE) {
				state = State.MINE_K;
				target = bot.getClosestKarbonite();
			}
			else{//(bindices_to_rm[i] = true;ot.fuel < 100) {
				state = State.MINE_F;
				target = bot.getClosestFuel();
			}
			break;
		case MINE_K: //shid I don know
			break;
		case MINE_F: //shid
			break;
		case DEPOSIT:
			break;
		}
	}
	
	public static Point2D adjacentChurch() {
		int x, y;
		int u;
		for(int i = 0; i < 8; i++) {
			x = bot.d_list[i][0] + bot.me.x;
			y = bot.d_list[i][1] + bot.me.y;
			//so this is like, make sure its in bounds, and see if its a church or castle
			if(bot.path.isPointInBounds(x, y) && (bot.robotMap[bot.me.y][bot.me.x] > 0)) {
				u = bot.getRobot(bot.robotMap[bot.me.y][bot.me.x]).unit;
				if(u == bot.SPECS.CASTLE || u == bot.SPECS.CHURCH) {
					return new Point2D(bot.d_list[i][0], bot.d_list[i][1]);
				}
			}
		}
		return null;
	}
	
	public static boolean canMine() {
		return (bot.karboniteMap[bot.me.y][bot.me.x] && bot.me.karbonite != 20) || (bot.fuelMap[bot.me.y][bot.me.x] && bot.me.fuel != 100);
	}
	
	public static Point2D getClosestChurch() {
		int min_dist = 100000000;
		int temp_dist;
		int min_index;
		int x, y;
		
		bot.log("numchurches " + numChurches);
		
		for(int i = 0; i < numChurches; i++) {
			x = churchList[i].x;
			y = churchList[i].y;
			temp_dist = x*x + y*y;
			
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_index = i;
			}
		}
		
		//if there are no available churches then shit homie idk find one
		if(min_dist == 100000000) return null;
		else return churchList[min_index];
	}
	
	public static void processEnvironment() {
		int id, unit, x, y;
		for(int i = 0; i < bot.robotList.length; i++) {
			id = bot.robotList[i].id;
			unit = bot.robotList[i].unit;
			x = bot.robotList[i].x;
			y = bot.robotList[i].y;
			
			if(bot.isVisible(bot.robotList[i])) {
				if(unit == Params.CHURCH || unit == Params.CASTLE) {
					if(churchesByID[id] == null) {
						churchesByID[id] = new Point2D(x, y);
						churchList[numChurches] = churchesByID[id];
						numChurches++;
					}
				}
			}
		}
		
		
		Point2D new_list[] = new Point2D[numChurches];
		int count = 0;
		for(int i = 0; i < numChurches; i++) {
			x = churchList[i].x;
			y = churchList[i].y;
			
			if(bot.robotMap[y][x] != 0) {
				new_list[count] = churchList[i];
				count++;
			}
		}
		
		numChurches = count;
		churchList = new_list;
		
	}
	
	public static void talkToCastle() {
		CastleTalker.sendOK();
	}
	
	public static Action turn() {
		Point2D adj;
		Point2D next_move;
		Point2D bot_pos = new Point2D(bot.me.x, bot.me.y);
		boolean is_safe = true;
		
		//look for enemies right here
		talkToCastle();
		
		processEnvironment();
		
		determineState();
		if(canMine() && is_safe) return bot.mine();
		
		switch(state) {
		case MINE_K:
			bot.log("(" + bot.me.x + ", " + bot.me.y + ")");
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("Herka");
			return bot.move(next_move.x, next_move.y);
		case MINE_F:
			bot.log("(" + bot.me.x + ", " + bot.me.y + ")");
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("Herka");
			return bot.move(next_move.x, next_move.y);
		case DEPOSIT:
			bot.log("depositing ore");
			target = getClosestChurch();
			if((adj = adjacentChurch()) != null) {
				state = State.NOTHING;
				return bot.give(adj.x, adj.y, bot.karbonite, bot.fuel);
			}
			
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("Herka");
			return bot.move(next_move.x, next_move.y);
		}
		return null;
	}

}
