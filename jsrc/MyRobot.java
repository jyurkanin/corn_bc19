package bc19;

public class MyRobot extends BCAbstractRobot {
	boolean run_once = true;
	int[][] robotMap;
	int[][] robotMemMap = new int[64][64];
	int[][] turnSeen = new int[64][64];
	int v_radius; //not squared.
	int v_radius_sq;
	
	Robot[] robotList;
	
	Point2D karboniteList[];
	Point2D fuelList[];
	Path path;
	
	//util
	int d_list[][] = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};
	
	Robot knownTeamBots[] = new Robot[4096]; //index is ID
	Robot knownEnemyBots[] = new Robot[4096]; 
	
	public boolean isInRange(Point2D p, int r_sq) {
		int dx = p.x - me.x;
		int dy = p.y - me.y;
		return (dx*dx + dy*dy) <= r_sq;
	}

	
	public boolean isUnOccupied(int x, int y) {
		return map[y][x] && robotMap[y][x] == 0;
	}
	
	public boolean isPointVisible(Point2D p) {
		return isInRange(p, SPECS.UNITS[me.unit].VISION_RADIUS);
	}
	
	public void getKarboniteList() {
		int count = 0;
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++){
				if(karboniteMap[y][x]) {
					count++;
				}
			}
		}
		
		karboniteList = new Point2D[count];
		count = 0;
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++){
				if(karboniteMap[y][x]) {
					karboniteList[count] = new Point2D(x, y);
					count++;
				}
			}
		}
	}
	
	public void getFuelList() {
		int count = 0;
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++){
				if(fuelMap[y][x]) {
					count++;
				}
			}
		}
		
		fuelList = new Point2D[count];
		count = 0;	
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++){
				if(fuelMap[y][x]) {
					fuelList[count] = new Point2D(x, y);
					count++;
				}
			}
		}
	}
	
	public Point2D getMove(Point2D s, Point2D t, int fuel) {
		log("start: (" + s.x + ", " + s.y + ")");
		log("target: (" + t.x + ", " + t.y + ")");
		
		path.bot = this; //so you can log. temporary.
		Point2D temp = path.get_move(s, t, fuel);
		if(temp != null)
			log("path result: (" + temp.x + ", " + temp.y + ")");
		else
			log("path result: nullll");
		return temp;
	}
	
	public void updateMemMap() {
		int x, y;
		for(int dy = -v_radius; dy <= v_radius; dy++) {
			for(int dx = -v_radius; dx <= v_radius; dx++) {
				if((dx*dx + dy*dy) > v_radius_sq) continue;
				x = dx + me.x;
				y = dy + me.y;
				
				robotMemMap[y][x] = robotMap[y][x];
				turnSeen[y][x] = me.turn;
			}
		}
	}
	
	//this is going to check the memMap and the age of the information and return a new value
	public int getMemMap(int y, int x) {
		int age = me.turn - turnSeen[y][x];
		if(robotMemMap[y][x] == -1 || age > Params.MEM_MAP_TIMEOUT) return -1; //nothing is known.
		else return robotMemMap[y][x];
	}
	
	public Action turn() {
		if(me.turn > 100) return null;
		
    	if(run_once) {
    		CastleTalker.bot = this;
    		v_radius = (int) Math.sqrt(SPECS.UNITS[me.unit].VISION_RADIUS);
    		v_radius_sq = SPECS.UNITS[me.unit].VISION_RADIUS;
    		path = new Path(map, me.unit, SPECS.UNITS[me.unit].VISION_RADIUS, SPECS.UNITS[me.unit].SPEED);
    		run_once = false;
    		getKarboniteList();
    		getFuelList();
    	}
    	
    	robotMap = getVisibleRobotMap();
    	robotList = getVisibleRobots();
    	updateMemMap();
    	
    	path.setRMap(robotMap);
		log("unit " + me.unit);
		
    	switch(me.unit) {
    	case Params.CASTLE:
    		if(Castle.bot == null) Castle.bot = this;
        	return Castle.turn();
    	case Params.CHURCH:
    		break;
    	case Params.PILGRIM:
    		if(Pilgrim.bot == null) Pilgrim.bot = this;
        	return Pilgrim.turn();
    	case Params.CRUSADER:
    		break;
    	case Params.PROPHET:
    		break;
    	case Params.PREACHER:
    		break;
    	}
    	return null;
    }
    
    public Point2D getClosestFuel() {
    	int dist_sq;
    	int min_dist = 1000000;
    	int dx, dy, tx, ty;
    	int index = -1;
    	for(int i = 0; i < fuelList.length; i++) {
    		tx = fuelList[i].x;
    		ty = fuelList[i].y;
    		
    		dx = tx - me.x;
    		dy = ty - me.y;
    		
    		dist_sq = dx*dx + dy*dy;
    		if(dist_sq < min_dist && map[ty][tx] && robotMap[ty][tx] <= 0) { //TODO robotMap memory
    			min_dist = dist_sq;
    			index = i;
    		}
    	}
    	if(index == -1) return null;
    	else return new Point2D(fuelList[index].x, fuelList[index].y);
    }
    
    public Point2D getClosestKarbonite() {
    	int dist_sq;
    	int min_dist = 1000000;
    	int dx, dy, tx, ty;
    	int index = -1;
    	for(int i = 0; i < karboniteList.length; i++) {
    		tx = karboniteList[i].x;
    		ty = karboniteList[i].y;
    		
    		dx = tx - me.x;
    		dy = ty - me.y;
    		
    		dist_sq = dx*dx + dy*dy;
    		if(dist_sq < min_dist && map[ty][tx] && robotMap[ty][tx] == 0) {//TODO robotMap memory
    			min_dist = dist_sq;
    			index = i;
    		}
    	}
    	if(index == -1) return null;
    	else return new Point2D(karboniteList[index].x, karboniteList[index].y);
    }
}