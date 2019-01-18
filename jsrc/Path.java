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
//	int move_rs;
	int max_move_rs;
	
	MyRobot bot;
	
	public int getSquareDist(Point2D t, Point2D s) {
		return (t.x - s.x)*(t.x - s.x) + (t.y - s.y) * (t.y-s.y);
	}
	
	public int getSquareDist(int tx, int ty, int sx, int sy) {
		return (tx - sx)*(tx - sx) + (ty - sy) * (ty-sy);
	}
	
	public boolean isPointInBounds(Point2D p) {
		return isPointInBounds(p.x, p.y);
	}
	public boolean isPointInBounds(int x, int y) {
		return x >= 0 && x < map.length && y >= 0 && y < map.length;
	}
	public void setRMap(int r[][]) {
		r_map = r;
	}
	
	//cost heuristic based on where robot remembers other robots to be.
	//hopefully avoiding oscillation and possibly avoiding other robots.
	public int costH(int x, int y) {
		if(bot.robotMemMap[y][x] == -1) return 0;
		if(bot.robotMemMap[y][x] == 0) return 0;
		//if(bot.robotMemMap[y][x] > 0){
		int age = bot.me.turn - bot.turnSeen[y][x];
		if(age < 3) return 2;
		else if(age < 5) return 1;
		return 0;
	}
	
	//djkistra. How the fuck do you spell this guys name
	public Point2D get_move(Point2D s, Point2D t, int fuel) {
		int minimap[][] = new int[map.length][map[0].length]; //remember its y,x
				
		int cost = 1; //cost in number of turns so we optimize for speed.
		
		int count_to_test = 0;
		int to_test[][] = new int [4096][2];
		
		int count_to_add = 0;
		int to_add[][] = new int[4096][2]; //it will always be quite a bit less than 4096.
				
		boolean gotSolution = false;
		int y, x, ty, tx;
		
		int rad_sq = max_move_rs;
		if(fuel < max_move_rs) {
			bot.log("PPP limited by fuel");
			rad_sq = fuel;
		}
				
		int temp_dx = t.x - s.x;
		int temp_dy = t.y - s.y;
		if(((temp_dx*temp_dx) + (temp_dy*temp_dy)) <= rad_sq) {
			if(map[t.y][t.x] && r_map[t.y][t.x] == 0) { //if we can move directly to the point.
				return new Point2D(temp_dx, temp_dy);
			}
			else { //move adjacent to the point
				for(int dx = -1; dx <= 1; dx++) {
					for(int dy = -1; dy <= 1; dy++) {
						if(dx == 0 && dy == 0) continue;
						x = t.x + dx;
						y = t.y + dy;
						
						temp_dx = dx + t.x - s.x;
						temp_dy = dy + t.y - s.y;
						
						if(map[y][x] && r_map[y][x] == 0 && (((temp_dx*temp_dx) + (temp_dy*temp_dy)) <= rad_sq)) { //if we can move directly to the point.
							return new Point2D(temp_dx, temp_dy);
						}
					}
				}
			}
		}
		
		bot.log("PPP rad_sq is " + rad_sq);
		
		minimap[s.y][s.x] = cost; 
		count_to_test = 1;
		to_test[0][0] = s.x;
		to_test[0][1] = s.y;
		
		int debug_count = 0; //for preventing infinite loops for debugging
		
oloop:	while(true) {
			cost++;
			//bot.log("PPP cost " + cost);
			//if(cost > 10) break; //only for debugging
			for(int i = 0; i < count_to_test; i++) {
				tx = to_test[i][0];
				ty = to_test[i][1];
				
				for(int dx = -3; dx <= 3; dx++) {
					for(int dy = -3; dy <= 3; dy++) {
						if((dx*dx + dy*dy) > rad_sq) continue;
						if(dy == 0 && dx == 0) continue;
						
						y = ty+dy;
						x = tx+dx;
						//Done. costH is a heuristic that uses robotMemMap tODO: r_map memory.
						if((t.x == x && t.y == y) || (isPointInBounds(x, y) && (cost < minimap[y][x] || minimap[y][x] == 0) && map[y][x] && r_map[y][x] <= 0) ) {
							minimap[y][x] = cost + costH(y, x);
							to_add[count_to_add][0] = x;
							to_add[count_to_add][1] = y;
							count_to_add++;
							if(x == t.x && y == t.y)
								break oloop;
						}
					}
				}
			}
			
			if(count_to_add == 0) return null;
			
			count_to_test = count_to_add;
			for(int i = 0; i < count_to_add; i++) {
				to_test[i][0] = to_add[i][0];
				to_test[i][1] = to_add[i][1];
			}
			count_to_add = 0;
		}
		
		boolean failure = false;
		
		int mincost;
		int mintx, minty;
		while(true) {
			//debug_count++;
			//bot.log("PPP debug_count " + debug_count);
			//if(debug_count > 10) return null;
			failure = true;
			mincost = cost;//4096;//it will never get higher than this.
			
ofor:		for(int dx = -3; dx <= 3; dx++) {
				for(int dy = -3; dy <= 3; dy++) {
					if((dx*dx + dy*dy) > rad_sq) continue;
					if(dy == 0 && dx == 0) continue;
					
					ty = y+dy;
					tx = x+dx;
					
					if(isPointInBounds(tx, ty) && minimap[ty][tx] != 0 && minimap[ty][tx] < mincost) {
						mincost = minimap[ty][tx];
						failure = false;
						minty = ty;
						mintx = tx;
					}
					/*if(isPointInBounds(tx, ty) && minimap[ty][tx] < cost) {
						failure = false;
						cost = minimap[ty][tx];
						break ofor;
					}*/
				}
			}
			
			
			
			if(failure) {
				bot.log("shit failed miserably");
				return null;
			}
			
			if(minty == s.y && mintx == s.x) {
				return new Point2D(x - mintx, y - minty);
			}
			
			cost = mincost;
			x = mintx;
			y = minty;
			
			bot.log("mincost tx ty " + mincost + " " + x + ", " + y);
			
		}
		
		
	}
	
	/*public Point2D get_move(Point2D s, Point2D t, int fuel) {
		target = t;
		start = s;

		if(target.x == start.x && target.y == start.y) {
			return null;
		}
		
		int move_rs = max_move_rs;
		if(fuel < max_move_rs) {
			bot.log("PPP limited by fuel");
			move_rs = fuel;
		}
		
		if(fuel == 0) return null;
		
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
		
		
		bot.log("PPP " +"beginning to debugify\n\n\n\n");
		bot.log("PPP " +"move_rs " + move_rs);
		bot.log("PPP " +"dxx, dyy "  + dxx + ", " + dyy);
		
		int debug_count = 0;
		int inner_count = 0;
		
		do{
			debug_count++;
			bot.log("PPP " +"outer loop iterations " + debug_count);
			
			tempx -= dxx;
			tempy -= dyy;
			
			testx = (int) Math.floor(tempx);
			testy = (int) Math.floor(tempy);
			
			bot.log("PPP " +"testx, testy "  + testx + ", " + testy);

			
			if(testx == old_testx && testy == old_testy) continue; //nothing new			
			dist_squared = (testx*testx) + (testy*testy);
			
			if(testx == s.x && testy == s.y) return null;
			
			//
			if(dist_squared <= move_rs && isPointInBounds(testx, testy) && 
					map[testy][testx] && (r_map[testy][testx] == 0)) {
				return new Point2D(testx, testy);
			}
			
			old_testx = testx;
			old_testy = testy;
			
			temp_p_max = move_rs - dist_squared;
			bot.log("PPP temp_p_max " + temp_p_max);
			
			temp_px = (-dyy*Math.sqrt(temp_p_max)) + tempx;
			temp_py = (dxx*Math.sqrt(temp_p_max)) + tempy;
			
			bot.log("PPP temp_px, temp_py "  + temp_px + ", " + temp_py);
			
			temp_p_min = 10000;
			
			old_test_px = 1000000;
			old_test_py = 1000000;
			
			inner_count = 0;
			do {// move perpendicular
				inner_count++;
				bot.log("PPP inner loop iterations " + inner_count);
				
				temp_px += dyy;
				temp_py -= dxx;
				temp_p = (temp_px*temp_px) + (temp_py*temp_py);

				test_px = (int) Math.floor(temp_px);
				test_py = (int) Math.floor(temp_py);
				
				bot.log("PPP " +"test_px, test_py "  + test_px + ", " + test_py);
				
				if(inner_count > 100) return null;
				
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
			
			bot.log("PPP " +"minx, miny "  + minx + ", " + miny);
			
			if(temp_p <= move_rs  && map[test_py][test_px] && (r_map[test_py][test_px] == 0)) {
				return new Point2D(minx, miny);
			}
			
			if(debug_count > 10) return null;
			
		} while(true);
		return null;
	}*/
	public Path(boolean m[][], int u, int v, int mv) {
		map = m;
		unit = u;
		vision_rs = v;
		max_move_rs = mv;
	}
}
