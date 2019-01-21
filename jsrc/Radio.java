package bc19;

public class Radio {
	//first two bits are command type
	static final int ATTACK_COMMAND 			 = 0b0000000000000000; //initially sent by castles
	static final int DEFEND_COMMAND 			 = 0b1000000000000000;
	static final int ASK_TARGET_LOCATION_COMMAND = 0b0100000000000000; //when attacking robots send this to their neighboring robots, then it means that they want to know where the target is. and they will send an attack command
	//static final int GIVE_TARGET_INFO_COMMAND	 = 0b0100000000000000;
	
	static final int PILGRIM  					 = 0b0000000000000000;
	static final int CRUSADER_OR_PREACHER 		 = 0b0001000000000000; //not very different, dont need to handle differently.
	static final int PROPHET  					 = 0b0010000000000000;
	static final int CHURCH   					 = 0b0011000000000000;
	
	//if attack_command, thats 2 bits,
		//next two bits indicate unit type.
		//then next 12 bits indicate position
	
	static MyRobot bot;
	public static int getCmd(int msg) {
		return 0b1100000000000000 & msg;
	}
	
	public static int getUnit(int msg) {
		return 0b0011000000000000 & msg;
	}
	
	public static int encodeUnit(int msg, int u) {
		switch(u) {
		case Params.PILGRIM:
			return msg | PILGRIM;
		case Params.CRUSADER:
			return msg | CRUSADER_OR_PREACHER;
		case Params.PROPHET:
			return msg | CRUSADER_OR_PREACHER;
		case Params.PREACHER:
			return msg | PROPHET;
		case Params.CHURCH:
			return msg | CHURCH;
		case Params.CASTLE:
			return msg | CHURCH;
		}
		return -1;
	}
	
	public static int encodePos(int msg, int x, int y) {
		return msg | (x << 6) | y;
	}
	
	public static Point2D getPos(int msg) {
		int x = msg & (0b111111000000);
		int y = msg & (0b111111);
		return new Point2D(x, y);
	}
	
	public static void requestTarget(int dist) {
		int msg = ASK_TARGET_LOCATION_COMMAND;
		bot.signal(msg, dist);
	}
	
	public static void attackRobot(Robot enemy, int dist) {
		int msg = ATTACK_COMMAND;
		msg = encodeUnit(msg, enemy.unit);
		msg = encodePos(msg, enemy.x, enemy.y);
		bot.debug("Signalling " + msg);
		bot.signal(msg, dist);
	}
	
	public static void attackRobot(Point2D pos, int unit, int dist) {
		int msg = ATTACK_COMMAND;
		msg = encodeUnit(msg, unit);
		msg = encodePos(msg, pos.x, pos.y);
		bot.debug("Signalling " + msg);
		bot.signal(msg, dist);
	}
	
	public static void enemyNearChurch(Robot enemy) {
		int msg = ATTACK_COMMAND;
		msg = encodeUnit(msg, enemy.unit);
		bot.debug("Signalling " + msg);
		bot.signal(msg, 16);
	}
}
