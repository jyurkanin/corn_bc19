package bc19;

import java.awt.geom.Point2D;


public class MyRobot extends BCAbstractRobot {
	boolean run_once = true;
	int[][] robotMap;
	Robot[] robotList;
	Path path;
	
	//util
	int d_list[][] = {{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}};
	
    public Action turn() {
    	if(run_once) {
    		path = new Path(map, m.unit);
    		run_once = false;
    	}
    	
    	robotMap = getVisibleRobotMap();
    	robotList = getVisibleRobots();
    	
    	switch(me.unit) {
    	case Params.CASTLE:
    		castleTurn();
    		break;
    	case Params.CHURCH:
    		castleTurn();
    		break;
    	case Params.PILGRIM:
    		break;
    	case Params.CRUSADER:
    		break;
    	case Params.PROPHET:
    		break;
    	case Params.PREACHER:
    		break;
    	}
    	return null;
    }
    
    public boolean buildAnywhere() {
    	if(fuel < SPECS.UNITS[SPECS.PILGRIM].CONSTRUCTION_FUEL) return false;
    	if(karbonite < SPECS.UNITS[SPECS.PILGRIM].CONSTRUCTION_KARBONITE) return false;
    	
    	int x, y;
    	
    	for(int i = 0; i < 8; i++) {
    		x = me.x + d_list[i][0];
    		y = me.y + d_list[i][1];
    		if((robotMap[x][y] == 0) && map[x][y]) {
    			buildUnit(SPECS.PILGRIM, d_list[i][0], d_list[i][1]);
    			return true;
    		}
    	}
    	return false;
    }
    
    public void castleTurn() {
    	log(buildUnit(SPECS.PILGRIM, 1, 0).toString());
    }
    
    public void pilgrimTurn() {
    	
    }
}