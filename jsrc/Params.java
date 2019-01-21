package bc19;

public class Params {
		static final int CASTLE=0;
		static final int CHURCH=1;
		static final int PILGRIM=2;
		static final int CRUSADER=3;
		static final int PROPHET=4;
		static final int PREACHER=5;
		
		static final int MEM_MAP_TIMEOUT = 10; //if a robot position is 10 turns old, it is untrustworthy.
		static final double INITIAL_PILGRIMS_FRAC = .5; //For the early game, what percentage of mines should be filled have a pilgrim.
		static final int CASTLE_GUARD_RADIUS = 16;
		
		static final boolean DEBUG = true; 
}
