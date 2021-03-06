package bc19;

public class MyRobot extends BCAbstractRobot {
	boolean run_once = true;
	int[][] robotMap;
	int[][] robotMemMap = new int[64][64];
	int[][] turnSeen = new int[64][64];
	int v_radius; //not squared.
	int v_radius_sq;
	int move_radius_sq;
	int attack_radius_sq;
	int attack_fuel_cost;
	
	int num_mines;
	
	Robot[] robotList;
	
	Point2D karboniteList[];
	Point2D fuelList[];
	Path path;
	
	//util
	int d_list[][] = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};
	
	Robot knownTeamBots[] = new Robot[4096]; //index is ID
	Robot knownEnemyBots[] = new Robot[4096]; 
	
	public void debug(String msg) {
		if(Params.DEBUG) log(msg);
	}
	
	public Point2D getClosestAdjPoint(Point2D c) {
		int x = 0;
		int y = 0;
		int minx = 0;
		int miny = 0;
		int closest = 65535;
		int temp = 0;
		for(int i = 0; i < d_list.length; i++) {
			x = c.x + d_list[i][0];
			y = c.y + d_list[i][1];
			
			temp = path.getSquareDist(x, y, me.x, me.y);
			if(path.isPointInBounds(x, y) && (robotMap[y][x] <= 0) && map[y][x] && temp < closest) {
				closest = temp;
				minx = x;
				miny = y;
			}
		}
		if(closest == 65535) return null;
		return new Point2D(minx, miny);
	}
	
	public Point2D adjacentChurch() {
		return adjacentChurch(me.x, me.y);
	}
	public Point2D adjacentChurch(int cx, int cy){
		int x, y;
		int u;
		Robot temp;
		for(int i = 0; i < 8; i++) {
			x = d_list[i][0] + cx;
			y = d_list[i][1] + cy;
			//so this is like, make sure its in bounds, and see if its a church or castle
			if(path.isPointInBounds(x, y) && (robotMap[y][x] > 0)) {
				temp = getRobot(robotMap[y][x]);
				if((temp.unit == SPECS.CASTLE || temp.unit == SPECS.CHURCH) && temp.team == me.team){
					return new Point2D(d_list[i][0], d_list[i][1]);
				}
			}
		}
		return null;
	}
	
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
		debug("Source: " + s.x + ", " + s.y);
		debug("Target: " + t.x + ", " + t.y);
		
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
				
				if(!path.isPointInBounds(x, y)) continue;
				
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
	
	public boolean canAttack(int x, int y) {
		int dx = me.x - x;
		int dy = me.y - y;
		debug("Attack radius sq " + attack_radius_sq);
		return robotMap[y][x] > 0 && ((dx*dx + dy*dy) <= attack_radius_sq) && fuel >= attack_fuel_cost;
	}
	
	public Action turn() {
		if(me.turn > 100) return null;

		
    	if(run_once) {
    		CastleTalker.bot = this;
    		Radio.bot = this;
    		v_radius = (int) Math.sqrt(SPECS.UNITS[me.unit].VISION_RADIUS);
    		v_radius_sq = SPECS.UNITS[me.unit].VISION_RADIUS;
    		move_radius_sq = SPECS.UNITS[me.unit].SPEED;
    		if(SPECS.UNITS[me.unit].ATTACK_RADIUS != null) {
    			attack_radius_sq = SPECS.UNITS[me.unit].ATTACK_RADIUS[1];
    		}
    		attack_fuel_cost = SPECS.UNITS[me.unit].ATTACK_FUEL_COST;
    		
    		path = new Path(map, me.unit, SPECS.UNITS[me.unit].VISION_RADIUS, SPECS.UNITS[me.unit].SPEED);
    		run_once = false;
    		getKarboniteList();
    		getFuelList();
    		num_mines = fuelList.length + karboniteList.length;
    	}
    	
    	robotMap = getVisibleRobotMap();
    	robotList = getVisibleRobots();
    	updateMemMap();
    	
    	path.setRMap(robotMap);
		log("unit " + me.unit + " at " + me.x + " " + me.y);
		
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
    		if(Crusader.bot == null) Crusader.bot = this;
    		return Crusader.turn();
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
    		if(dist_sq < min_dist && map[ty][tx] && robotMemMap[ty][tx] <= 0) {
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
    		if(dist_sq < min_dist && map[ty][tx] && robotMemMap[ty][tx] <= 0) {
    			min_dist = dist_sq;
    			index = i;
    		}
    	}
    	if(index == -1) return null;
    	else return new Point2D(karboniteList[index].x, karboniteList[index].y);
    }
    
    //either karbonite or fuel
    public Point2D getClosestMine() {
    	Point2D closest_k = getClosestKarbonite();
    	Point2D closest_f = getClosestFuel();
    	
    	//all cases handled I hope.
    	if(closest_k == null && closest_f == null) return null;
    	if(closest_k == null && closest_f != null) return closest_f;
    	if(closest_k != null && closest_f == null) return closest_k;
    		
    	int dxk = closest_k.x - me.x;
    	int dyk = closest_k.y - me.y;
    	
    	int dxf = closest_f.x - me.x;
    	int dyf = closest_f.y - me.y;
    	
    	int dist_f = (dxf*dxf) + (dyf*dyf);
    	int dist_k = (dxk*dxk) + (dyk*dyk);
    	
    	if(dist_f > dist_k) return closest_k;
    	else return closest_f;
    }
}