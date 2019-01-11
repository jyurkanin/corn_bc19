package bc19;


//Idea: something like potential pathing or sampling?
/*
 * Currently, this just picks the closest point near the line from target to start
 * that is still a valid point to move to
 */

class Point2D{
	int x, y;
	public Point2D(int _x, int _y) {
		x = _x;
		y = _y;
	}
}

public class Path {
	boolean map[][];
	int r_map[][];
	Point2D start;
	Point2D target;
	int unit;
	int vision_rs;
	int move_rs;
	
	public boolean isPointInBounds(Point2D p) {
		return isPointInBounds(p.x, p.y);
	}
	public boolean isPointInBounds(int x, int y) {
		return x >= 0 && x < map.length && y >= 0 && y < map.length;
	}
	
	public Point2D getMove(Point2D s, Point2D t, int r[][]) {
		target = t;
		start = s;
		r_map = r;
		
		int dx = (t.x-s.x);
		int dy = (t.y-s.y);
		double dist_squared = (dx*dx) + (dy*dy);
		
		if(dist_squared <= move_rs && map[t.y][t.x] && (r_map[t.y][t.x] == 0)) {
			return new Point2D(dx, dy);
		}
		
		double tempx = t.x;
		double tempy = t.y;
		double temp_px, temp_py; //move perpendicular to target.
		
		
		int testx, old_testx, test_px, old_test_px;
		int testy, old_testy, test_py, old_test_py;
		
		
		double temp_p, temp_p_max;
		double dist =  Math.sqrt(dx*dx + dy*dy);
		
		double dxx = dx/dist;
		double dyy = dy/dist;
		int minx, miny;
		double temp_p_min;
		
		old_testx = 1000000;
		old_testy = 1000000;
		
		
		do{
			tempx -= dxx;
			tempy -= dyy;
			
			testx = (int) Math.floor(tempx);
			testy = (int) Math.floor(tempy);
			
			if(testx == old_testx && testy == old_testy) continue; //nothing new
			if(!isPointInBounds(testx, testy)) continue;
			
			dist_squared = (testx*testx) + (testy*testy);
			if(dist_squared <= move_rs && map[testy][testx] && (r_map[testy][testx] == 0)) {
				return new Point2D(testx, testy);
			}
			
			old_testx = testx;
			old_testy = testy;
			
			temp_p_max = move_rs - dist_squared;
			temp_px = (-dyy*Math.sqrt(temp_p_max)) + tempx;
			temp_py = (dxx*Math.sqrt(temp_p_max)) + tempy;
			
			temp_p_min = 10000;
			
			old_test_px = 1000000;
			old_test_py = 1000000;
			do {// move perpendicular
				temp_px += dyy;
				temp_py -= dxx;
				temp_p = (temp_px*temp_px) + (temp_py*temp_py);
				temp_p_min = temp_p;
				minx = test_px;
				miny = test_py;
				test_px = (int) Math.floor(temp_px);
				test_py = (int) Math.floor(temp_py);
				
				if(temp_p > temp_p_max) break;
				if(test_px == old_test_px && test_py == old_test_py) continue; //nothing new here.
				if(!isPointInBounds(test_px, test_py)) continue;
				
				if(temp_p <= move_rs && temp_p < temp_p_min && map[test_py][test_px] && (r_map[test_py][test_px] == 0)) {
					temp_p_min = temp_p;
					minx = test_px;
					miny = test_py;
				}
				
				old_test_px = test_px;
				old_test_py = test_py;
				
			} while(true);
			
			if(temp_p <= move_rs  && map[test_py][test_px] && (r_map[test_py][test_px] == 0)) {
				return new Point2D(minx, miny);
			}
			
		} while(true);
	}
	public Path(boolean m[][], int u, int v, int mv) {
		map = m;
		unit = u;
		vision_rs = v;
		move_rs = mv;
	}
}
