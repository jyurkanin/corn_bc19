package bc19;

import java.awt.geom.Point2D;

//Idea: something like potential pathing or sampling?

public class Path {
	boolean map[][];
	int r_map[][];
	Point2D start;
	Point2D target;
	int unit;
	int vision_rs;
	int move_rs;
	
	public Point2D getMove(Point2D s, Point2D t, int r[][]) {
		target = t;
		start = s;
		r_map = r;
		
		int dx = (t.x-s.x);
		int dy = (t.y-s.y);
		dist_square = (dx*dx) + (dy*dy);
		
		if(dist_square <= move_rs && map[t.x][t.y] && (r_map[t.x][t.y] == 0)) {
			return new Point2D(dx, dy);
		}
		
		double tempx = t.x;
		double tempy = t.y;
		double temp_px, temp_py; //move perpendicular to target.
		
		
		int testx, old_testx, test_px, old_test_px;
		int testy, old_testy;
		
		double temp_p, temp_p_max;
		double dist =  Math.hypot(dx, dy);
		double dxx = dx/dist;
		double dyy = dy/dist;
		int minx, miny;
		double temp_p_min;
		
		do{
			tempx -= dxx;
			tempy -= dyy;
			
			testx = Math.floor(tempx);
			testy = Math.floor(tempy);
			
			if(testx == old_testx && testy == old_testy) continue; //nothing new
			
			dist_square = (testx*testx) + (testy*testy);
			if(dist_square <= move_rs && map[testx][testy] && (r_map[testx][testy] == 0)) {
				return new Point2D(testx, testy);
			}
			
			old_testx = testx;
			old_testy = testy;
			
			temp_p_max = move_rs - dist_squared;
			temp_px = (dyy*Math.sqrt(temp_p_max)) + testx;
			temp_py = (dxx*Math.sqrt(temp_p_max)) + testy;
			
			temp_p_min = 10000;
			do {// move perpendicular
				temp_px -= dyy;
				temp_py -= dxx;
				temp_p = (temp_px*temp_px) + (temp_py*temp_py);
				
				if(dist_square <= move_rs && temp_p < temp_p_min && map[testx][testy] && (r_map[testx][testy] == 0)) {
					temp_p_min = temp_p;
					minx = temp_px;
					miny = temp_py;
				}
			} while(1);
		} while(1);
	}
	public Path(boolean m[][], int u) {
		map = m;
		unit = u;
		vision_rs = SPECS.UNITS[SPECS.PILGRIM]['VISION_RADIUS'];
		move_rs = SPECS.UNITS[SPECS.PILGRIM]['SPEED'];
	}
}
