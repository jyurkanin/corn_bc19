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
	
	//is there a open adjacent tile around this church
	public static boolean isChurchReachable(int cx, int cy) {
		int x = 0;
		int y = 0;
		for(int i = 0; i < bot.d_list.length; i++) {
			x = cx + bot.d_list[i][0];
			y = cy + bot.d_list[i][1];
			if(bot.path.isPointInBounds(x, y) && (bot.robotMap[y][x] <= 0) && bot.map[y][x]) {
				return true;
			}
		}
		return false;
	}
	
	public static Point2D getClosestChurch() {
		int min_dist = 1000000000;
		int temp_dist;
		int min_index = -1;
		int x, y;
		
		for(int i = 0; i < numChurches; i++) {
			x = churchList[i].x;
			y = churchList[i].y;
			if(!isChurchReachable(x, y)) continue;
			temp_dist = x*x + y*y;
			
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_index = i;
			}
		}
		
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
			return bot.mine();
		}
		
		
		switch(state) {
		case MINE_K:			
			if(target == null) {target = bot.getClosestKarbonite();}
			if(target == null) {break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestKarbonite();
			}
			
			if(bot.karboniteMap[bot.me.y][bot.me.x] && (bot.karbonite >= 50) && (bot.fuel >= 200) && bot.adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
						
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			return bot.move(next_move.x, next_move.y);
		case MINE_F:			
			if(target == null) {target = bot.getClosestFuel();}
			if(target == null) {break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestFuel(); 
			}
			
			if(bot.fuelMap[bot.me.y][bot.me.x] && (bot.karbonite >= 50) && (bot.fuel >= 200) && bot.adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
						
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			return bot.move(next_move.x, next_move.y);
		case MINE_FK:			
			if(target == null) {target = bot.getClosestMine();}
			if(target == null) {break;}
			if(!(target.x == bot.me.x && target.y == bot.me.y) && !bot.isUnOccupied(target.x, target.y)) {
				target = bot.getClosestMine(); 
			}
			
			on_mine = bot.fuelMap[bot.me.y][bot.me.x] || bot.karboniteMap[bot.me.y][bot.me.x];
			if(on_mine && (bot.karbonite >= 50) && (bot.fuel >= 200) && bot.adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
						
			next_move = bot.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			return bot.move(next_move.x, next_move.y);
		case DEPOSIT:			
			target = getClosestChurch(); // actually want to move adjacent to the closest church...
			if(target != null)
				target = bot.getClosestAdjPoint(target);
			//bot.debug("PILGRIM TARGET: " + target.x + " " + target.y);
			
			if((adj = bot.adjacentChurch()) != null) {
				state = State.NOTHING;
				return bot.give(adj.x, adj.y, bot.me.karbonite, bot.me.fuel);
			}
			
			on_mine = bot.fuelMap[bot.me.y][bot.me.x] || bot.karboniteMap[bot.me.y][bot.me.x];
			if(on_mine && (bot.karbonite >= 50) && (bot.fuel >= 200) && bot.adjacentChurch() == null) {
				if((temp_action = buildAdjacentChurch()) != null) {
					return temp_action;
				}
			}
			
			next_move = null;
			//bot.debug("PILGRIM BLAH UGH "+ bot.path.getSquareDist(bot.me.x, bot.me.y, target.x, target.y) + " " + bot.move_radius_sq);
			if(target != null && bot.path.getSquareDist(bot.me.x, bot.me.y, target.x, target.y) <= bot.move_radius_sq) {
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
