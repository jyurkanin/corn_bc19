package bc19;

public class MyRobot extends BCAbstractRobot {
	boolean run_once = true;
	int[][] robotMap;
	Robot[] robotList;
	
	Point2D karboniteList[];
	Point2D fuelList[];
	Path path;
	
	//util
	int d_list[][] = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};
	
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
					karboniteList[count] = new Point2D(y, x);
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
					fuelList[count] = new Point2D(y, x);
					count++;
				}
			}
		}
	}
	
    public Action turn() {
    	if(run_once) {
    		int i = SPECS.UNITS[SPECS.PILGRIM].SPEED;
    		i = SPECS.UNITS[SPECS.PILGRIM].VISION_RADIUS;
    		path = new Path(map, me.unit, SPECS.UNITS[SPECS.PILGRIM].VISION_RADIUS, SPECS.UNITS[SPECS.PILGRIM].SPEED);
    		run_once = false;
    		getKarboniteList();
    		getFuelList();
    	}
    	
    	robotMap = getVisibleRobotMap();
    	robotList = getVisibleRobots();
    	
    	switch(me.unit) {
    	case Params.CASTLE:
    		return castleTurn();
    	case Params.CHURCH:
    		return churchTurn();
    	case Params.PILGRIM:
    		return pilgrimTurn();
    	case Params.CRUSADER:
    		break;
    	case Params.PROPHET:
    		break;
    	case Params.PREACHER:
    		break;
    	}
    	return null;
    }
    
    public Action buildAnywhere(int u) {
    	if(fuel < SPECS.UNITS[u].CONSTRUCTION_FUEL) return null;
    	if(karbonite < SPECS.UNITS[u].CONSTRUCTION_KARBONITE) return null;
    	int x, y;
    	
    	for(int i = 0; i < 8; i++) {
    		x = me.x + d_list[i][0];
    		y = me.y + d_list[i][1];
    		if((robotMap[y][x] == 0) && map[y][x]) {
    			return buildUnit(u, d_list[i][0], d_list[i][1]);
    		}
    	}
    	return null;
    }
    
    public Action castleTurn() {
    	return buildAnywhere(SPECS.PILGRIM);
    }
    
    public Action churchTurn() {
    	return null;
    }
    
    public Point2D getClosestFuel() {
    	int dist_sq;
    	int min_dist = 1000000;
    	int dx, dy;
    	int index;
    	for(int i = 0; i < fuelList.length; i++) {
    		dx = fuelList[i].x - me.x;
    		dy = fuelList[i].y - me.y;
    		dist_sq = dx*dx + dy*dy;
    		if(dist_sq < min_dist) {
    			min_dist = dist_sq;
    			index = i;
    		}
    	}
    	return new Point2D(fuelList[index].x, fuelList[index].y);
    }
    
    public Action pilgrimTurn() {
    	Point2D dydx;
    	Point2D closest;
    	
    	
    	return null;
    }
}