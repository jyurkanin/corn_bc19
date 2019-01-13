package bc19;

//holds pilgrim logic
public class Pilgrim {
	enum State {
		BUILD_CHURCH, MINE_K, MINE_F, DEPOSIT, NOTHING
	}
	
	public static State state = State.NOTHING;
	public static MyRobot bot;
	public static Point2D target;
	
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
			else{//(bot.fuel < 100) {
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
		return;
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
	
	public static void processEnvironment() {
		
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
			bot.log(bot.me.x + " " + bot.me.y);
			next_move = bot.path.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("Herka");
			return bot.move(next_move.x, next_move.y);
		case MINE_F:
			bot.log(bot.me.x + " " + bot.me.y);
			next_move = bot.path.getMove(bot_pos, target, bot.fuel);
			if(next_move == null) return null;
			bot.log("Herka");
			return bot.move(next_move.x, next_move.y);
		case DEPOSIT:
			if((adj = adjacentChurch()) != null) {
				bot.give(adj.x, adj.y, bot.karbonite, bot.fuel);
				state = State.NOTHING;
			}
			break;
		}
		return null;
	}

}
