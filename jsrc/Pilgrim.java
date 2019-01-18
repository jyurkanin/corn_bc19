package bc19;

//holds pilgrim logic
public class Pilgrim {
	enum State {
		BUILD_CHURCH, MINE_K, MINE_F, MINE_FK, DEPOSIT, NOTHING
	}
	
	public static State state = State.NOTHING;
	public static MyRobot bot;
	public static Point2D target;
	
	public static int numChurches = 0;
	public static int churchIDList[] = new int[100];
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
			//this code had good intentions but it was stupid.
			/*else if(bot.karbonite < bot.SPECS.UNITS[bot.SPECS.PILGRIM].CONSTRUCTION_KARBONITE) {
				state = State.MINE_K;
				target = bot.getClosestKarbonite();
				if(target == null ) bot.log("uh oh spagettios K");
			} 
			else if(bot.fuel < 100){ //just a random ass value.
				state = State.MINE_F;
				target = bot.getClosestFuel();
				if(target == null ) bot.log("uh oh spagettios F");
			}*/
			else {
				state = State.MINE_FK;
				target = bot.getClosestMine();
				if(target == null ) bot.log("uh oh spagettios FK");
			}
			break;
		case MINE_K: //shid I don know
			break;
		case MINE_F: //shid
			break;
		case MINE_FK:
			break;
		case DEPOSIT:
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
	
	public static Point2D hasAdjacentUnit(int unit) {
		int x, y;
		int u;
		Robot temp;
		for(int i = 0; i < 8; i++) {
			x = bot.d_list[i][0] + bot.me.x;
			y = bot.d_list[i][1] + bot.me.y;
			//so this is like, make sure its in bounds, and see if its a church or castle
			if(bot.path.isPointInBounds(x, y) && bot.robotMap[y][x] > 0) {
				temp = bot.getRobot(bot.robotMap[y][x]);
				if(temp.unit == bot.me.unit && temp.team == bot.me.team) {
					return new Point2D(bot.d_list[i][0], bot.d_list[i][1]);
				}
			}
		}
		return null;
	}
	
	public static Action buildAdjacentChurch() {
		int x, y;
		int u;
		for(int i = 0; i < 8; i++) {
			x = bot.d_list[i][0] + bot.me.x;
			y = bot.d_list[i][1] + bot.me.y;
			//so this is like, make sure its in bounds, and see if its over a mineandno robots are there right now.
			if(bot.path.isPointInBounds(x, y) && bot.map[y][x] && bot.robotMap[y][x] == 0 && !bot.fuelMap[y][x] && !bot.karboniteMap[y][x]){
				return bot.buildUnit(Params.CHURCH, bot.d_list[i][0], bot.d_list[i][1]);
			}
		}
		return null;
	}
	
	public static boolean canMine() {
		return (bot.karboniteMap[bot.me.y][bot.me.x] && bot.me.karbonite != 20) || (bot.fuelMap[bot.me.y][bot.me.x] && bot.me.fuel != 100);
	}
	
	public static Point2D getClosestChurch() {
		int min_dist = 1000000000;
		int temp_dist;
		int min_index = -1;
		int x, y;
		
		bot.log("numChurches = " + numChurches);
		
		for(int i = 0; i < numChurches; i++) {
			x = churchList[i].x;
			y = churchList[i].y;
			temp_dist = x*x + y*y;
			
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_index = i;
			}
		}
		
		bot.log("closest church " + min_index);
		//if there are no available churches then shit homie idk find one
		if(min_index == -1) return null;
		else return churchList[min_index];
	}
	
	public static void processEnvironment() {
		int id, unit, team, x, y;
		for(int i = 0; i < bot.robotList.length; i++) {
			id = bot.robotList[i].id;
			unit = bot.robotList[i].unit;
			team = bot.robotList[i].team;
			x = bot.robotList[i].x;
			y = bot.robotList[i].y;
			
			//I should add a part here to look for radio signals by churches
			
			//this part adds the churches.
			if(bot.isVisible(bot.robotList[i]) && team == bot.me.team) {
				if(unit == Params.CHURCH || unit == Params.CASTLE) {
					if(churchesByID[id] == null) {
						bot.log("Adding a church");
						churchesByID[id] = new Point2D(x, y);
						churchIDList[numChurches] = id;
						churchList[numChurches] = churchesByID[id];
						numChurches++;
					}
				}
			}
		}
		
		//this block right here actually just removes churches that are no longer there.
		Point2D new_list[] = new Point2D[numChurches];
		int new_id_list[] = new int[numChurches];
		
		int count = 0;
		for(int i = 0; i < numChurches; i++) {
			x = churchList[i].x;
			y = churchList[i].y;
			
			if(bot.robotMap[y][x] != 0) {
				new_id_list[count] = churchIDList[i];
				new_list[count] = churchList[i];
				count++;
			}
			else {
				churchesByID[churchIDList[i]] = null;
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
		boolean on_mine;
		Action temp_action;
		 
		
		//TODO:look for enemies right here and set state to avoid
		talkToCastle();
		processEnvironment();

		//if(is_safe) return null; //short circuit this shit
		
		determineState();
		
		if(canMine() && is_safe) {
			bot.log("MINING " + bot.me.fuel + " " + bot.me.karbonite);
			return bot.mine();
		}
		
		bot.log("(" + bot.me.x + ", " + bot.me.y + ")");
		
		switch(state) {
		case MINE_K:
			bot.log("STATE MINE_K_1");
			
			if(target == null) {target = bot.getClosestKarbonite(); bot.log("ya know what ya bish_1");}
			if(target == null) {bot.log("ya know what ya bish_2"); break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestKarbonite();
			}
			
			if(bot.karboniteMap[bot.me.y][bot.me.x] && (bot.karbonite >= 50) && (bot.fuel >= 200) && adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
			
			bot.log("STATE MINE_K_2");
			
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("STATE MINE_K_3");
			return bot.move(next_move.x, next_move.y);
		case MINE_F:
			bot.log("STATE MINE_F_1");
			
			if(target == null) {target = bot.getClosestFuel(); bot.log("frick off my nuts_1");}
			if(target == null) {bot.log("frick off my nuts_2"); break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestFuel(); 
			}
			
			if(bot.fuelMap[bot.me.y][bot.me.x] && (bot.karbonite >= 50) && (bot.fuel >= 200) && adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
			
			bot.log("STATE MINE_F_2");
			
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("STATE MINE_F_3");
			return bot.move(next_move.x, next_move.y);
		case MINE_FK:
			bot.log("STATE MINE_FK_1");
			
			if(target == null) {target = bot.getClosestMine(); bot.log("frick off my nuts_1");}
			if(target == null) {bot.log("frick off my nuts_2"); break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestMine(); 
			}
			
			on_mine = bot.fuelMap[bot.me.y][bot.me.x] || bot.karboniteMap[bot.me.y][bot.me.x];
			if(on_mine && (bot.karbonite >= 50) && (bot.fuel >= 200) && adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
			
			bot.log("STATE MINE_FK_2");
			
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("STATE MINE_FK_3");
			return bot.move(next_move.x, next_move.y);
		case DEPOSIT:
			bot.log("STATE DEPOSIT");
			
			target = getClosestChurch();
			
			bot.log("STATE DEPOSIT_2");
			if((adj = adjacentChurch()) != null) {
				state = State.NOTHING;
				return bot.give(adj.x, adj.y, bot.me.karbonite, bot.me.fuel);
			}
			
			on_mine = bot.fuelMap[bot.me.y][bot.me.x] || bot.karboniteMap[bot.me.y][bot.me.x];
			if(on_mine && (bot.karbonite >= 50) && (bot.fuel >= 200) && adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
			
			bot.log("STATE DEPOSIT_3");
			next_move = null;
			if(bot.path.getSquareDist(bot.me.x, bot.me.y, target.x, target.y) <= bot.move_radius_sq) {
				next_move = bot.getMove(bot_pos, target, bot.fuel);
				return bot.move(next_move.x, next_move.y);
			}
			if(next_move == null) {
				//try to give the karb/fuel to a nearby pilgrim right here and hope they are closer than you.
				if((adj = hasAdjacentUnit(bot.SPECS.PILGRIM)) != null) {
					return bot.give(adj.x, adj.y, bot.me.karbonite, bot.me.fuel);
				}
				return null;
			}
		}
		return null;
	}
	
}
