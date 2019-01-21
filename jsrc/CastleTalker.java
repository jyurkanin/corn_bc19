package bc19;


public class CastleTalker {
	public static MyRobot bot;
	
	//command grammar
	//BEGIN_TRANSMISSION
	//first two bits are always command type
	public static final int ALL_GOOD          = 0b00000000;
	public static final int ENEMY_NEAR        = 0b01000000;
	public static final int TEAM_CHURCH_NEAR  = 0b10000000;
	public static final int ENEMY_CHURCH_NEAR = 0b11000000;
	
	
	//IF First two bits indicate ALL_GOOD
	// the rest of the bits are unit type.
	public static final int PILGRIM  = 0b00111000;
	public static final int CRUSADER = 0b00010000;
	public static final int PROPHET  = 0b00100000;
	public static final int PREACHER = 0b00110000;
	public static final int CHURCH   = 0b00001000;
	public static final int CASTLE   = 0b00011000;
	//END_TRANSMISSION
	
	//IF First two bits indicate ENEMY_NEAR, TEAM_CHURCH_NEAR, or ENEMY_CHURCH_NEAR
	// then next six bits indicate a position, with 3 bytes for x and y each giving 
	//a bounding square that the unit is in.
	//END_TRANSMISSION
	
	
	//lets just xor with the fibonnaci sequence;
	public static int encrypt(int output) {
		return 0;
	}
	
	public static int decrypt(int input) {
		return 0;
	}
	
	public static int getCommand(int msg) {
		return 0b11000000 & msg;
	}
	
	public static Point2D decodePos(int msg) {
		int x = (msg & 0b00111000) >> 3;
		int y =  msg & 0b00000111;
		return new Point2D(x, y);
	}
	
	//this only encodes 3 bits of each coordinate
	public static int encodePos(int x, int y) {
		int tx = x >> 3;
		int ty = y >> 3;
		return (tx << 3) | y;
	}
	
	public static int getUnit(int msg) {
		int u = 0b00111000 & msg;
		switch(u) {
		case PILGRIM:
			return Params.PILGRIM;
		case CRUSADER:
			return Params.CRUSADER;
		case PROPHET:
			return Params.PROPHET;
		case PREACHER:
			return Params.PREACHER;
		case CHURCH:
			return Params.CHURCH;
		case CASTLE:
			return Params.CASTLE;
		}
		return -1;
	}
	
	public static void reportEnemy(Robot r) {
		int msg = ENEMY_NEAR;
		
	}
	
	//this function will handle serializing data
	public static void sendOK() {
		int msg = ALL_GOOD;
		switch(bot.me.unit) {
		case Params.PILGRIM:
			msg |= PILGRIM;
			break;
		case Params.CRUSADER:
			msg |= CRUSADER;
			break;
		case Params.PROPHET:
			msg |= PROPHET;
			break;
		case Params.PREACHER:
			msg |= PREACHER;
			break;
		}
		bot.castleTalk(msg);
	}
}
