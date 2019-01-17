var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
/* Generated from Java with JSweet 2.0.0-SNAPSHOT - http://www.jsweet.org */
var bc19;
(function (bc19) {
    var Action = (function () {
        function Action(signal, signalRadius, logs, castleTalk) {
            this.signal = 0;
            this.signal_radius = 0;
            this.logs = null;
            this.castle_talk = 0;
            this.signal = signal;
            this.signal_radius = signalRadius;
            this.logs = logs;
            this.castle_talk = castleTalk;
        }
        return Action;
    }());
    bc19.Action = Action;
    Action["__class"] = "bc19.Action";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Pilgrim = (function () {
        function Pilgrim() {
        }
        Pilgrim.state_$LI$ = function () { if (Pilgrim.state == null)
            Pilgrim.state = Pilgrim.State.NOTHING; return Pilgrim.state; };
        ;
        Pilgrim.churchIDList_$LI$ = function () { if (Pilgrim.churchIDList == null)
            Pilgrim.churchIDList = (function (s) { var a = []; while (s-- > 0)
                a.push(0); return a; })(100); return Pilgrim.churchIDList; };
        ;
        Pilgrim.churchList_$LI$ = function () { if (Pilgrim.churchList == null)
            Pilgrim.churchList = new Array(100); return Pilgrim.churchList; };
        ;
        Pilgrim.churchesByID_$LI$ = function () { if (Pilgrim.churchesByID == null)
            Pilgrim.churchesByID = new Array(4096); return Pilgrim.churchesByID; };
        ;
        Pilgrim.determineState = function () {
            if (Pilgrim.bot.me.fuel === 100) {
                Pilgrim.state = Pilgrim.State.DEPOSIT;
                return;
            }
            if (Pilgrim.bot.me.karbonite === 20) {
                Pilgrim.state = Pilgrim.State.DEPOSIT;
                return;
            }
            switch ((Pilgrim.state_$LI$())) {
                case bc19.Pilgrim.State.NOTHING:
                    if (false) {
                    }
                    else if (Pilgrim.bot.karbonite < Pilgrim.bot.SPECS.UNITS[Pilgrim.bot.SPECS.CHURCH].CONSTRUCTION_KARBONITE) {
                        Pilgrim.state = Pilgrim.State.MINE_K;
                        Pilgrim.target = Pilgrim.bot.getClosestKarbonite();
                        if (Pilgrim.target == null)
                            Pilgrim.bot.log("uh oh spagettios K");
                    }
                    else if (Pilgrim.bot.fuel < 100) {
                        Pilgrim.state = Pilgrim.State.MINE_F;
                        Pilgrim.target = Pilgrim.bot.getClosestFuel();
                        if (Pilgrim.target == null)
                            Pilgrim.bot.log("uh oh spagettios F");
                    }
                    else {
                        Pilgrim.state = Pilgrim.State.MINE_FK;
                        Pilgrim.target = Pilgrim.bot.getClosestMine();
                        if (Pilgrim.target == null)
                            Pilgrim.bot.log("uh oh spagettios FK");
                    }
                    break;
                case bc19.Pilgrim.State.MINE_K:
                    if (Pilgrim.target == null) {
                        Pilgrim.target = Pilgrim.bot.getClosestKarbonite();
                        Pilgrim.bot.log("ya know what ya bish_1");
                    }
                    if (Pilgrim.target == null) {
                        Pilgrim.bot.log("ya know what ya bish_2");
                        break;
                    }
                    if (!(Pilgrim.target.x === Pilgrim.bot.me.x && Pilgrim.target.y === Pilgrim.bot.me.y) && !Pilgrim.bot.isUnOccupied(Pilgrim.target.x, Pilgrim.target.y)) {
                        Pilgrim.target = Pilgrim.bot.getClosestKarbonite();
                    }
                    break;
                case bc19.Pilgrim.State.MINE_F:
                    if (Pilgrim.target == null) {
                        Pilgrim.target = Pilgrim.bot.getClosestFuel();
                        Pilgrim.bot.log("frick off my nuts_1");
                    }
                    if (Pilgrim.target == null) {
                        Pilgrim.bot.log("frick off my nuts_2");
                        break;
                    }
                    if (!(Pilgrim.target.x === Pilgrim.bot.me.x && Pilgrim.target.y === Pilgrim.bot.me.y) && !Pilgrim.bot.isUnOccupied(Pilgrim.target.x, Pilgrim.target.y)) {
                        Pilgrim.target = Pilgrim.bot.getClosestFuel();
                    }
                    break;
                case bc19.Pilgrim.State.DEPOSIT:
                    break;
            }
        };
        Pilgrim.adjacentChurch$ = function () {
            return Pilgrim.adjacentChurch$int$int(Pilgrim.bot.me.x, Pilgrim.bot.me.y);
        };
        Pilgrim.adjacentChurch$int$int = function (cx, cy) {
            var x;
            var y;
            var u;
            for (var i = 0; i < 8; i++) {
                x = Pilgrim.bot.d_list[i][0] + cx;
                y = Pilgrim.bot.d_list[i][1] + cy;
                if (Pilgrim.bot.path.isPointInBounds$int$int(x, y)) {
                    if (Pilgrim.bot.robotMap[y][x] > 0) {
                        u = Pilgrim.bot.getRobot(Pilgrim.bot.robotMap[y][x]).unit;
                        if (u === Pilgrim.bot.SPECS.CASTLE || u === Pilgrim.bot.SPECS.CHURCH) {
                            return new bc19.Point2D(Pilgrim.bot.d_list[i][0], Pilgrim.bot.d_list[i][1]);
                        }
                    }
                }
            }
            ;
            return null;
        };
        Pilgrim.adjacentChurch = function (cx, cy) {
            if (((typeof cx === 'number') || cx === null) && ((typeof cy === 'number') || cy === null)) {
                return bc19.Pilgrim.adjacentChurch$int$int(cx, cy);
            }
            else if (cx === undefined && cy === undefined) {
                return bc19.Pilgrim.adjacentChurch$();
            }
            else
                throw new Error('invalid overload');
        };
        Pilgrim.buildAdjacentChurch = function () {
            var x;
            var y;
            var u;
            for (var i = 0; i < 8; i++) {
                x = Pilgrim.bot.d_list[i][0] + Pilgrim.bot.me.x;
                y = Pilgrim.bot.d_list[i][1] + Pilgrim.bot.me.y;
                if (Pilgrim.bot.path.isPointInBounds$int$int(x, y) && Pilgrim.bot.robotMap[y][x] === 0 && !Pilgrim.bot.fuelMap[y][x] && !Pilgrim.bot.karboniteMap[y][x]) {
                    return Pilgrim.bot.buildUnit(bc19.Params.CHURCH, Pilgrim.bot.d_list[i][0], Pilgrim.bot.d_list[i][1]);
                }
            }
            ;
            return null;
        };
        Pilgrim.canMine = function () {
            return (Pilgrim.bot.karboniteMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] && Pilgrim.bot.me.karbonite !== 20) || (Pilgrim.bot.fuelMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] && Pilgrim.bot.me.fuel !== 100);
        };
        Pilgrim.getClosestChurch = function () {
            var min_dist = 1000000000;
            var temp_dist;
            var min_index = -1;
            var x;
            var y;
            Pilgrim.bot.log("numChurches = " + Pilgrim.numChurches);
            for (var i = 0; i < Pilgrim.numChurches; i++) {
                x = Pilgrim.churchList_$LI$()[i].x;
                y = Pilgrim.churchList_$LI$()[i].y;
                temp_dist = x * x + y * y;
                if (temp_dist < min_dist) {
                    min_dist = temp_dist;
                    min_index = i;
                }
            }
            ;
            Pilgrim.bot.log("closest church " + min_index);
            if (min_index === -1)
                return null;
            else
                return Pilgrim.churchList_$LI$()[min_index];
        };
        Pilgrim.processEnvironment = function () {
            var id;
            var unit;
            var team;
            var x;
            var y;
            for (var i = 0; i < Pilgrim.bot.robotList.length; i++) {
                id = Pilgrim.bot.robotList[i].id;
                unit = Pilgrim.bot.robotList[i].unit;
                team = Pilgrim.bot.robotList[i].team;
                x = Pilgrim.bot.robotList[i].x;
                y = Pilgrim.bot.robotList[i].y;
                if (Pilgrim.bot.isVisible(Pilgrim.bot.robotList[i]) && team === Pilgrim.bot.me.team) {
                    if (unit === bc19.Params.CHURCH || unit === bc19.Params.CASTLE) {
                        if (Pilgrim.churchesByID_$LI$()[id] == null) {
                            Pilgrim.bot.log("Adding a church");
                            Pilgrim.churchesByID_$LI$()[id] = new bc19.Point2D(x, y);
                            Pilgrim.churchIDList_$LI$()[Pilgrim.numChurches] = id;
                            Pilgrim.churchList_$LI$()[Pilgrim.numChurches] = Pilgrim.churchesByID_$LI$()[id];
                            Pilgrim.numChurches++;
                        }
                    }
                }
            }
            ;
            var new_list = new Array(Pilgrim.numChurches);
            var new_id_list = (function (s) { var a = []; while (s-- > 0)
                a.push(0); return a; })(Pilgrim.numChurches);
            var count = 0;
            for (var i = 0; i < Pilgrim.numChurches; i++) {
                x = Pilgrim.churchList_$LI$()[i].x;
                y = Pilgrim.churchList_$LI$()[i].y;
                if (Pilgrim.bot.robotMap[y][x] !== 0) {
                    new_id_list[count] = Pilgrim.churchIDList_$LI$()[i];
                    new_list[count] = Pilgrim.churchList_$LI$()[i];
                    count++;
                }
                else {
                    Pilgrim.churchesByID_$LI$()[Pilgrim.churchIDList_$LI$()[i]] = null;
                }
            }
            ;
            Pilgrim.numChurches = count;
            Pilgrim.churchList = new_list;
        };
        Pilgrim.talkToCastle = function () {
            bc19.CastleTalker.sendOK();
        };
        Pilgrim.turn = function () {
            var adj;
            var next_move;
            var bot_pos = new bc19.Point2D(Pilgrim.bot.me.x, Pilgrim.bot.me.y);
            var is_safe = true;
            var on_mine;
            var temp_action;
            Pilgrim.talkToCastle();
            Pilgrim.processEnvironment();
            Pilgrim.determineState();
            if (Pilgrim.canMine() && is_safe) {
                Pilgrim.bot.log("MINING " + Pilgrim.bot.me.fuel + " " + Pilgrim.bot.me.karbonite);
                return Pilgrim.bot.mine();
            }
            Pilgrim.bot.log("(" + Pilgrim.bot.me.x + ", " + Pilgrim.bot.me.y + ")");
            switch ((Pilgrim.state_$LI$())) {
                case bc19.Pilgrim.State.MINE_K:
                    Pilgrim.bot.log("STATE MINE_K_1");
                    if (Pilgrim.bot.karboniteMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] && (Pilgrim.bot.karbonite >= 50) && (Pilgrim.bot.fuel >= 200) && Pilgrim.adjacentChurch() == null) {
                        if ((temp_action = Pilgrim.buildAdjacentChurch()) != null) {
                            return temp_action;
                        }
                    }
                    Pilgrim.bot.log("STATE MINE_K_2");
                    next_move = Pilgrim.bot.getMove(bot_pos, Pilgrim.target, Pilgrim.bot.fuel);
                    if (next_move == null)
                        return null;
                    Pilgrim.bot.log("STATE MINE_K_3");
                    return Pilgrim.bot.move(next_move.x, next_move.y);
                case bc19.Pilgrim.State.MINE_F:
                    Pilgrim.bot.log("STATE MINE_F_1");
                    if (Pilgrim.bot.fuelMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] && (Pilgrim.bot.karbonite >= 50) && (Pilgrim.bot.fuel >= 200) && Pilgrim.adjacentChurch() == null) {
                        if ((temp_action = Pilgrim.buildAdjacentChurch()) != null) {
                            return temp_action;
                        }
                    }
                    Pilgrim.bot.log("STATE MINE_F_2");
                    next_move = Pilgrim.bot.getMove(bot_pos, Pilgrim.target, Pilgrim.bot.fuel);
                    if (next_move == null)
                        return null;
                    Pilgrim.bot.log("STATE MINE_F_3");
                    return Pilgrim.bot.move(next_move.x, next_move.y);
                case bc19.Pilgrim.State.MINE_FK:
                    Pilgrim.bot.log("STATE MINE_FK_1");
                    on_mine = Pilgrim.bot.fuelMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] || Pilgrim.bot.karboniteMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x];
                    if (on_mine && (Pilgrim.bot.karbonite >= 50) && (Pilgrim.bot.fuel >= 200) && Pilgrim.adjacentChurch() == null) {
                        if ((temp_action = Pilgrim.buildAdjacentChurch()) != null) {
                            return temp_action;
                        }
                    }
                    Pilgrim.bot.log("STATE MINE_FK_2");
                    next_move = Pilgrim.bot.getMove(bot_pos, Pilgrim.target, Pilgrim.bot.fuel);
                    if (next_move == null)
                        return null;
                    Pilgrim.bot.log("STATE MINE_FK_3");
                    return Pilgrim.bot.move(next_move.x, next_move.y);
                case bc19.Pilgrim.State.DEPOSIT:
                    Pilgrim.bot.log("STATE DEPOSIT");
                    Pilgrim.target = Pilgrim.getClosestChurch();
                    Pilgrim.bot.log("STATE DEPOSIT_2");
                    if ((adj = Pilgrim.adjacentChurch()) != null) {
                        Pilgrim.state = Pilgrim.State.NOTHING;
                        return Pilgrim.bot.give(adj.x, adj.y, Pilgrim.bot.me.karbonite, Pilgrim.bot.me.fuel);
                    }
                    if (Pilgrim.bot.fuelMap[Pilgrim.bot.me.y][Pilgrim.bot.me.x] && (Pilgrim.bot.karbonite >= 50) && (Pilgrim.bot.fuel >= 200) && Pilgrim.adjacentChurch() == null) {
                        if ((temp_action = Pilgrim.buildAdjacentChurch()) != null) {
                            return temp_action;
                        }
                    }
                    Pilgrim.bot.log("STATE DEPOSIT_3");
                    next_move = Pilgrim.bot.getMove(bot_pos, Pilgrim.target, Pilgrim.bot.fuel);
                    if (next_move == null) {
                        return null;
                    }
                    Pilgrim.bot.log("STATE DEPOSIT_4");
                    return Pilgrim.bot.move(next_move.x, next_move.y);
            }
            return null;
        };
        return Pilgrim;
    }());
    Pilgrim.bot = null;
    Pilgrim.target = null;
    Pilgrim.numChurches = 0;
    bc19.Pilgrim = Pilgrim;
    Pilgrim["__class"] = "bc19.Pilgrim";
    (function (Pilgrim) {
        var State;
        (function (State) {
            State[State["BUILD_CHURCH"] = 0] = "BUILD_CHURCH";
            State[State["MINE_K"] = 1] = "MINE_K";
            State[State["MINE_F"] = 2] = "MINE_F";
            State[State["MINE_FK"] = 3] = "MINE_FK";
            State[State["DEPOSIT"] = 4] = "DEPOSIT";
            State[State["NOTHING"] = 5] = "NOTHING";
        })(State = Pilgrim.State || (Pilgrim.State = {}));
    })(Pilgrim = bc19.Pilgrim || (bc19.Pilgrim = {}));
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Params = (function () {
        function Params() {
        }
        return Params;
    }());
    Params.CASTLE = 0;
    Params.CHURCH = 1;
    Params.PILGRIM = 2;
    Params.CRUSADER = 3;
    Params.PROPHET = 4;
    Params.PREACHER = 5;
    Params.MEM_MAP_TIMEOUT = 10;
    bc19.Params = Params;
    Params["__class"] = "bc19.Params";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Castle = (function () {
        function Castle() {
            this.pilgrimList = null;
        }
        Castle.buildAnywhere = function (u) {
            if (Castle.bot.fuel < Castle.bot.SPECS.UNITS[u].CONSTRUCTION_FUEL)
                return null;
            if (Castle.bot.karbonite < Castle.bot.SPECS.UNITS[u].CONSTRUCTION_KARBONITE)
                return null;
            var x;
            var y;
            for (var i = 0; i < 8; i++) {
                x = Castle.bot.me.x + Castle.bot.d_list[i][0];
                y = Castle.bot.me.y + Castle.bot.d_list[i][1];
                if (Castle.bot.path.isPointInBounds$int$int(x, y) && (Castle.bot.robotMap[y][x] === 0) && Castle.bot.map[y][x]) {
                    return Castle.bot.buildUnit(u, Castle.bot.d_list[i][0], Castle.bot.d_list[i][1]);
                }
            }
            ;
            return null;
        };
        Castle.parseSignal = function (msg) {
            return;
        };
        Castle.parseCastleTalk = function (msg, r) {
            var id;
            var team;
            var command;
            var unit;
            command = bc19.CastleTalker.getCommand(msg);
            unit = bc19.CastleTalker.getUnit(msg);
            if (command === bc19.CastleTalker.ALL_GOOD) {
                Castle.bot.log("Got all good. Counting unit");
                Castle.countUnits(unit);
                if (Castle.bot.knownTeamBots[id] == null) {
                    Castle.bot.knownTeamBots[id] = r;
                    Castle.bot.knownTeamBots[id].unit = unit;
                }
                else {
                    Castle.bot.knownTeamBots[id].unit = unit;
                }
            }
            else if (Castle.bot.knownTeamBots[id] != null && Castle.bot.knownTeamBots[id].unit !== -1) {
                Castle.bot.log("Got a different signal but we can still count the unit");
                unit = Castle.bot.knownTeamBots[id].unit;
                Castle.countUnits(unit);
            }
        };
        Castle.resetUnitCount = function () {
            Castle.numPilgrims = 0;
            Castle.numPreachers = 0;
            Castle.numProphets = 0;
            Castle.numCrusaders = 0;
            Castle.numChurches = 0;
        };
        Castle.countUnits = function (unit) {
            if (Castle.unitCounted)
                return;
            Castle.unitCounted = true;
            switch ((unit)) {
                case bc19.Params.PILGRIM:
                    Castle.numPilgrims++;
                    break;
            }
        };
        Castle.processRobotList = function () {
            var id;
            var team;
            var command;
            var unit;
            var msg;
            var isVisible;
            Castle.resetUnitCount();
            for (var i = 0; i < Castle.bot.robotList.length; i++) {
                id = Castle.bot.robotList[i].id;
                team = Castle.bot.robotList[i].team;
                Castle.unitCounted = false;
                if (team !== Castle.bot.me.team) {
                    Castle.bot.knownEnemyBots[id] = Castle.bot.robotList[i];
                    continue;
                }
                Castle.bot.log("numPilgrims: " + Castle.numPilgrims);
                msg = Castle.bot.robotList[i].castle_talk;
                if (msg !== 0) {
                    Castle.bot.log("parsing castle talk");
                    Castle.parseCastleTalk(msg, Castle.bot.robotList[i]);
                }
                msg = Castle.bot.robotList[i].signal;
                if (msg !== 0) {
                    Castle.parseSignal(msg);
                }
                if (Castle.bot.knownTeamBots[id] == null) {
                    Castle.bot.knownTeamBots[id] = Castle.bot.robotList[i];
                }
                else {
                    unit = Castle.bot.robotList[i].unit;
                    if (Castle.bot.isVisible(Castle.bot.robotList[i])) {
                        Castle.bot.log("Unit visible, counting it");
                        Castle.bot.knownTeamBots[id].unit = unit;
                        Castle.countUnits(unit);
                    }
                }
            }
            ;
        };
        Castle.turn = function () {
            Castle.processRobotList();
            if (Castle.numPilgrims < 10)
                return Castle.buildAnywhere(Castle.bot.SPECS.PILGRIM);
            return null;
        };
        return Castle;
    }());
    Castle.bot = null;
    Castle.numPilgrims = 0;
    Castle.numPreachers = 0;
    Castle.numProphets = 0;
    Castle.numCrusaders = 0;
    Castle.numChurches = 0;
    Castle.state = null;
    Castle.unitCounted = false;
    bc19.Castle = Castle;
    Castle["__class"] = "bc19.Castle";
    (function (Castle) {
        var State;
        (function (State) {
        })(State = Castle.State || (Castle.State = {}));
    })(Castle = bc19.Castle || (bc19.Castle = {}));
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BCException = (function (_super) {
        __extends(BCException, _super);
        function BCException(errorMessage) {
            var _this = _super.call(this, errorMessage) || this;
            _this.message = errorMessage;
            Object.setPrototypeOf(_this, BCException.prototype);
            return _this;
        }
        return BCException;
    }(Error));
    bc19.BCException = BCException;
    BCException["__class"] = "bc19.BCException";
    BCException["__interfaces"] = ["java.io.Serializable"];
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BCAbstractRobot = (function () {
        function BCAbstractRobot() {
            this.SPECS = null;
            this.gameState = null;
            this.logs = null;
            this.__signal = 0;
            this.signalRadius = 0;
            this.__castleTalk = 0;
            this.me = null;
            this.id = 0;
            this.fuel = 0;
            this.karbonite = 0;
            this.lastOffer = null;
            this.map = null;
            this.karboniteMap = null;
            this.fuelMap = null;
            this.resetState();
        }
        BCAbstractRobot.prototype.setSpecs = function (specs) {
            this.SPECS = specs;
        };
        /*private*/ BCAbstractRobot.prototype.resetState = function () {
            this.logs = ([]);
            this.__signal = 0;
            this.signalRadius = 0;
            this.__castleTalk = 0;
        };
        BCAbstractRobot.prototype._do_turn = function (gameState) {
            this.gameState = gameState;
            this.id = gameState.id;
            this.karbonite = gameState.karbonite;
            this.fuel = gameState.fuel;
            this.lastOffer = gameState.last_offer;
            this.me = this.getRobot(this.id);
            if (this.me.turn === 1) {
                this.map = gameState.map;
                this.karboniteMap = gameState.karbonite_map;
                this.fuelMap = gameState.fuel_map;
            }
            var t = null;
            try {
                t = this.turn();
            }
            catch (e) {
                t = new bc19.ErrorAction(e, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
            }
            ;
            if (t == null)
                t = new bc19.Action(this.__signal, this.signalRadius, this.logs, this.__castleTalk);
            t.signal = this.__signal;
            t.signal_radius = this.signalRadius;
            t.logs = this.logs;
            t.castle_talk = this.__castleTalk;
            this.resetState();
            return t;
        };
        /*private*/ BCAbstractRobot.prototype.checkOnMap = function (x, y) {
            return x >= 0 && x < this.gameState.shadow[0].length && y >= 0 && y < this.gameState.shadow.length;
        };
        BCAbstractRobot.prototype.log = function (message) {
            /* add */ (this.logs.push(message) > 0);
        };
        BCAbstractRobot.prototype.signal = function (value, radius) {
            if (this.fuel < radius)
                throw new bc19.BCException("Not enough fuel to signal given radius.");
            if (value < 0 || value >= Math.pow(2, this.SPECS.COMMUNICATION_BITS))
                throw new bc19.BCException("Invalid signal, must be within bit range.");
            if (radius > 2 * Math.pow(this.SPECS.MAX_BOARD_SIZE - 1, 2))
                throw new bc19.BCException("Signal radius is too big.");
            this.__signal = value;
            this.signalRadius = radius;
            this.fuel -= radius;
        };
        BCAbstractRobot.prototype.castleTalk = function (value) {
            if (value < 0 || value >= Math.pow(2, this.SPECS.CASTLE_TALK_BITS))
                throw new bc19.BCException("Invalid castle talk, must be between 0 and 2^8.");
            this.__castleTalk = value;
        };
        BCAbstractRobot.prototype.proposeTrade = function (k, f) {
            if (this.me.unit !== this.SPECS.CASTLE)
                throw new bc19.BCException("Only castles can trade.");
            if (Math.abs(k) >= this.SPECS.MAX_TRADE || Math.abs(f) >= this.SPECS.MAX_TRADE)
                throw new bc19.BCException("Cannot trade over " + ('' + (this.SPECS.MAX_TRADE)) + " in a given turn.");
            return new bc19.TradeAction(f, k, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.buildUnit = function (unit, dx, dy) {
            if (this.me.unit !== this.SPECS.PILGRIM && this.me.unit !== this.SPECS.CASTLE && this.me.unit !== this.SPECS.CHURCH)
                throw new bc19.BCException("This unit type cannot build.");
            if (this.me.unit === this.SPECS.PILGRIM && unit !== this.SPECS.CHURCH)
                throw new bc19.BCException("Pilgrims can only build churches.");
            if (this.me.unit !== this.SPECS.PILGRIM && unit === this.SPECS.CHURCH)
                throw new bc19.BCException("Only pilgrims can build churches.");
            if (dx < -1 || dy < -1 || dx > 1 || dy > 1)
                throw new bc19.BCException("Can only build in adjacent squares.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t build units off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] !== 0)
                throw new bc19.BCException("Cannot build on occupied tile.");
            if (!this.map[this.me.y + dy][this.me.x + dx])
                throw new bc19.BCException("Cannot build onto impassable terrain.");
            if (this.karbonite < this.SPECS.UNITS[unit].CONSTRUCTION_KARBONITE || this.fuel < this.SPECS.UNITS[unit].CONSTRUCTION_FUEL)
                throw new bc19.BCException("Cannot afford to build specified unit.");
            return new bc19.BuildAction(unit, dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.move = function (dx, dy) {
            if (this.me.unit === this.SPECS.CASTLE || this.me.unit === this.SPECS.CHURCH)
                throw new bc19.BCException("Churches and Castles cannot move.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t move off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] === -1)
                throw new bc19.BCException("Cannot move outside of vision range.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] !== 0)
                throw new bc19.BCException("Cannot move onto occupied tile.");
            if (!this.map[this.me.y + dy][this.me.x + dx])
                throw new bc19.BCException("Cannot move onto impassable terrain.");
            var r = dx * dx + dy * dy;
            if (r > this.SPECS.UNITS[this.me.unit].SPEED)
                throw new bc19.BCException("Slow down, cowboy.  Tried to move faster than unit can.");
            if (this.fuel < r * this.SPECS.UNITS[this.me.unit].FUEL_PER_MOVE)
                throw new bc19.BCException("Not enough fuel to move at given speed.");
            return new bc19.MoveAction(dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.mine = function () {
            if (this.me.unit !== this.SPECS.PILGRIM)
                throw new bc19.BCException("Only Pilgrims can mine.");
            if (this.fuel < this.SPECS.MINE_FUEL_COST)
                throw new bc19.BCException("Not enough fuel to mine.");
            if (this.karboniteMap[this.me.y][this.me.x]) {
                if (this.me.karbonite >= this.SPECS.UNITS[this.SPECS.PILGRIM].KARBONITE_CAPACITY)
                    throw new bc19.BCException("Cannot mine, as at karbonite capacity.");
            }
            else if (this.fuelMap[this.me.y][this.me.x]) {
                if (this.me.fuel >= this.SPECS.UNITS[this.SPECS.PILGRIM].FUEL_CAPACITY)
                    throw new bc19.BCException("Cannot mine, as at fuel capacity.");
            }
            else
                throw new bc19.BCException("Cannot mine square without fuel or karbonite.");
            return new bc19.MineAction(this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.give = function (dx, dy, k, f) {
            if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || (dx === 0 && dy === 0))
                throw new bc19.BCException("Can only give to adjacent squares.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t give off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] <= 0)
                throw new bc19.BCException("Cannot give to empty square.");
            if (k < 0 || f < 0 || this.me.karbonite < k || this.me.fuel < f)
                throw new bc19.BCException("Do not have specified amount to give.");
            return new bc19.GiveAction(k, f, dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.attack = function (dx, dy) {
            if (this.me.unit !== this.SPECS.CRUSADER && this.me.unit !== this.SPECS.PREACHER && this.me.unit !== this.SPECS.PROPHET)
                throw new bc19.BCException("Given unit cannot attack.");
            if (this.fuel < this.SPECS.UNITS[this.me.unit].ATTACK_FUEL_COST)
                throw new bc19.BCException("Not enough fuel to attack.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t attack off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] === -1)
                throw new bc19.BCException("Cannot attack outside of vision range.");
            if (!this.map[this.me.y + dy][this.me.x + dx])
                throw new bc19.BCException("Cannot attack impassable terrain.");
            var r = dx * dx + dy * dy;
            if (r > this.SPECS.UNITS[this.me.unit].ATTACK_RADIUS[1] || r < this.SPECS.UNITS[this.me.unit].ATTACK_RADIUS[0])
                throw new bc19.BCException("Cannot attack outside of attack range.");
            return new bc19.AttackAction(dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.getRobot = function (id) {
            if (id <= 0)
                return null;
            for (var i = 0; i < this.gameState.visible.length; i++) {
                if (this.gameState.visible[i].id === id) {
                    return this.gameState.visible[i];
                }
            }
            ;
            return null;
        };
        BCAbstractRobot.prototype.isVisible = function (robot) {
            for (var x = 0; x < this.gameState.shadow[0].length; x++) {
                for (var y = 0; y < this.gameState.shadow.length; y++) {
                    if (robot.id === this.gameState.shadow[y][x])
                        return true;
                }
                ;
            }
            ;
            return false;
        };
        BCAbstractRobot.prototype.isRadioing = function (robot) {
            return robot.signal >= 0;
        };
        BCAbstractRobot.prototype.getVisibleRobotMap = function () {
            return this.gameState.shadow;
        };
        BCAbstractRobot.prototype.getPassableMap = function () {
            return this.map;
        };
        BCAbstractRobot.prototype.getKarboniteMap = function () {
            return this.karboniteMap;
        };
        BCAbstractRobot.prototype.getFuelMap = function () {
            return this.fuelMap;
        };
        BCAbstractRobot.prototype.getVisibleRobots = function () {
            return this.gameState.visible;
        };
        BCAbstractRobot.prototype.turn = function () {
            return null;
        };
        return BCAbstractRobot;
    }());
    bc19.BCAbstractRobot = BCAbstractRobot;
    BCAbstractRobot["__class"] = "bc19.BCAbstractRobot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var CastleTalker = (function () {
        function CastleTalker() {
        }
        CastleTalker.encoder = function (output) {
            return 0;
        };
        CastleTalker.decoder = function (input) {
            return 0;
        };
        CastleTalker.getCommand = function (msg) {
            return 192 & msg;
        };
        CastleTalker.getUnit = function (msg) {
            var u = 56 & msg;
            switch ((u)) {
                case 56 /* PILGRIM */:
                    return bc19.Params.PILGRIM;
                case 16 /* CRUSADER */:
                    return bc19.Params.CRUSADER;
                case 32 /* PROPHET */:
                    return bc19.Params.PROPHET;
                case 48 /* PREACHER */:
                    return bc19.Params.PREACHER;
                case 8 /* CHURCH */:
                    return bc19.Params.CHURCH;
                case 24 /* CASTLE */:
                    return bc19.Params.CASTLE;
            }
            return -1;
        };
        CastleTalker.sendOK = function () {
            var msg = CastleTalker.ALL_GOOD;
            switch ((CastleTalker.bot.me.unit)) {
                case bc19.Params.PILGRIM:
                    msg |= CastleTalker.PILGRIM;
                    break;
                case bc19.Params.CRUSADER:
                    msg |= CastleTalker.CRUSADER;
                    break;
                case bc19.Params.PROPHET:
                    msg |= CastleTalker.PROPHET;
                    break;
                case bc19.Params.PREACHER:
                    msg |= CastleTalker.PREACHER;
                    break;
            }
            CastleTalker.bot.castleTalk(msg);
        };
        return CastleTalker;
    }());
    CastleTalker.bot = null;
    CastleTalker.ALL_GOOD = 0;
    CastleTalker.ENEMY_NEAR = 64;
    CastleTalker.TEAM_CHURCH_NEAR = 128;
    CastleTalker.ENEMY_CHURCH_NEAR = 192;
    CastleTalker.PILGRIM = 56;
    CastleTalker.CRUSADER = 16;
    CastleTalker.PROPHET = 32;
    CastleTalker.PREACHER = 48;
    CastleTalker.CHURCH = 8;
    CastleTalker.CASTLE = 24;
    bc19.CastleTalker = CastleTalker;
    CastleTalker["__class"] = "bc19.CastleTalker";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Point2D = (function () {
        function Point2D(_x, _y) {
            this.x = 0;
            this.y = 0;
            this.x = _x;
            this.y = _y;
        }
        return Point2D;
    }());
    bc19.Point2D = Point2D;
    Point2D["__class"] = "bc19.Point2D";
    var Path = (function () {
        function Path(m, u, v, mv) {
            this.map = null;
            this.r_map = null;
            this.start = null;
            this.target = null;
            this.unit = 0;
            this.vision_rs = 0;
            this.max_move_rs = 0;
            this.bot = null;
            this.map = m;
            this.unit = u;
            this.vision_rs = v;
            this.max_move_rs = mv;
        }
        Path.prototype.isPointInBounds$bc19_Point2D = function (p) {
            return this.isPointInBounds$int$int(p.x, p.y);
        };
        Path.prototype.isPointInBounds$int$int = function (x, y) {
            return x >= 0 && x < this.map.length && y >= 0 && y < this.map.length;
        };
        Path.prototype.isPointInBounds = function (x, y) {
            if (((typeof x === 'number') || x === null) && ((typeof y === 'number') || y === null)) {
                return this.isPointInBounds$int$int(x, y);
            }
            else if (((x != null && x instanceof bc19.Point2D) || x === null) && y === undefined) {
                return this.isPointInBounds$bc19_Point2D(x);
            }
            else
                throw new Error('invalid overload');
        };
        Path.prototype.setRMap = function (r) {
            this.r_map = r;
        };
        Path.prototype.get_move = function (s, t, fuel) {
            var minimap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.map.length, this.map[0].length]);
            var cost = 1;
            var count_to_test = 0;
            var to_test = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([4096, 2]);
            var count_to_add = 0;
            var to_add = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([4096, 2]);
            var gotSolution = false;
            var y;
            var x;
            var ty;
            var tx;
            var rad_sq = this.max_move_rs;
            if (fuel < this.max_move_rs) {
                this.bot.log("PPP limited by fuel");
                rad_sq = fuel;
            }
            var temp_dx = t.x - s.x;
            var temp_dy = t.y - s.y;
            if (((temp_dx * temp_dx) + (temp_dy * temp_dy)) <= rad_sq) {
                if (this.map[t.y][t.x] && this.r_map[t.y][t.x] === 0) {
                    return new bc19.Point2D(temp_dx, temp_dy);
                }
                else {
                    for (var dx = -1; dx <= 1; dx++) {
                        for (var dy = -1; dy <= 1; dy++) {
                            if (dx === 0 && dy === 0)
                                continue;
                            x = t.x + dx;
                            y = t.y + dy;
                            temp_dx = dx + t.x - s.x;
                            temp_dy = dy + t.y - s.y;
                            if (this.map[y][x] && this.r_map[y][x] === 0 && (((temp_dx * temp_dx) + (temp_dy * temp_dy)) <= rad_sq)) {
                                return new bc19.Point2D(temp_dx, temp_dy);
                            }
                        }
                        ;
                    }
                    ;
                }
            }
            this.bot.log("PPP rad_sq is " + rad_sq);
            minimap[s.y][s.x] = cost;
            count_to_test = 1;
            to_test[0][0] = s.x;
            to_test[0][1] = s.y;
            var debug_count = 0;
            oloop: while ((true)) {
                cost++;
                this.bot.log("PPP cost " + cost);
                for (var i = 0; i < count_to_test; i++) {
                    tx = to_test[i][0];
                    ty = to_test[i][1];
                    for (var dx = -3; dx <= 3; dx++) {
                        for (var dy = -3; dy <= 3; dy++) {
                            if ((dx * dx + dy * dy) > rad_sq)
                                continue;
                            if (dy === 0 && dx === 0)
                                continue;
                            y = ty + dy;
                            x = tx + dx;
                            if ((t.x === x && t.y === y) || (this.isPointInBounds$int$int(x, y) && (cost < minimap[y][x] || minimap[y][x] === 0) && this.map[y][x] && this.r_map[y][x] <= 0)) {
                                minimap[y][x] = cost;
                                to_add[count_to_add][0] = x;
                                to_add[count_to_add][1] = y;
                                count_to_add++;
                                if (x === t.x && y === t.y)
                                    break oloop;
                            }
                        }
                        ;
                    }
                    ;
                }
                ;
                if (count_to_add === 0)
                    return null;
                count_to_test = count_to_add;
                for (var i = 0; i < count_to_add; i++) {
                    to_test[i][0] = to_add[i][0];
                    to_test[i][1] = to_add[i][1];
                }
                ;
                count_to_add = 0;
            }
            ;
            var failure = false;
            while ((true)) {
                failure = true;
                ofor: for (var dx = -3; dx <= 3; dx++) {
                    for (var dy = -3; dy <= 3; dy++) {
                        if ((dx * dx + dy * dy) > rad_sq)
                            continue;
                        if (dy === 0 && dx === 0)
                            continue;
                        ty = y + dy;
                        tx = x + dx;
                        if (this.isPointInBounds$int$int(tx, ty) && minimap[ty][tx] === cost - 1) {
                            failure = false;
                            cost--;
                            break ofor;
                        }
                    }
                    ;
                }
                ;
                if (ty === s.y && tx === s.x) {
                    return new bc19.Point2D(x - tx, y - ty);
                }
                if (failure) {
                    this.bot.log("shit failed miserably");
                    return null;
                }
                else {
                    this.bot.log("tx ty " + x + ", " + y);
                    x = tx;
                    y = ty;
                }
            }
            ;
        };
        return Path;
    }());
    bc19.Path = Path;
    Path["__class"] = "bc19.Path";
})(bc19 || (bc19 = {}));
var Radio = (function () {
    function Radio() {
    }
    Radio.prototype.foundOpenMine = function () {
    };
    Radio.prototype.parseMsg = function () {
    };
    return Radio;
}());
Radio["__class"] = "Radio";
(function (bc19) {
    var MineAction = (function (_super) {
        __extends(MineAction, _super);
        function MineAction(signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.action = "mine";
            return _this;
        }
        return MineAction;
    }(bc19.Action));
    bc19.MineAction = MineAction;
    MineAction["__class"] = "bc19.MineAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var ErrorAction = (function (_super) {
        __extends(ErrorAction, _super);
        function ErrorAction(error, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.error = null;
            _this.error = error.message;
            return _this;
        }
        return ErrorAction;
    }(bc19.Action));
    bc19.ErrorAction = ErrorAction;
    ErrorAction["__class"] = "bc19.ErrorAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var GiveAction = (function (_super) {
        __extends(GiveAction, _super);
        function GiveAction(giveKarbonite, giveFuel, dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.give_karbonite = 0;
            _this.give_fuel = 0;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "give";
            _this.give_karbonite = giveKarbonite;
            _this.give_fuel = giveFuel;
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return GiveAction;
    }(bc19.Action));
    bc19.GiveAction = GiveAction;
    GiveAction["__class"] = "bc19.GiveAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BuildAction = (function (_super) {
        __extends(BuildAction, _super);
        function BuildAction(buildUnit, dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.build_unit = 0;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "build";
            _this.build_unit = buildUnit;
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return BuildAction;
    }(bc19.Action));
    bc19.BuildAction = BuildAction;
    BuildAction["__class"] = "bc19.BuildAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var TradeAction = (function (_super) {
        __extends(TradeAction, _super);
        function TradeAction(trade_fuel, trade_karbonite, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.trade_fuel = 0;
            _this.trade_karbonite = 0;
            _this.action = "trade";
            _this.trade_fuel = trade_fuel;
            _this.trade_karbonite = trade_karbonite;
            return _this;
        }
        return TradeAction;
    }(bc19.Action));
    bc19.TradeAction = TradeAction;
    TradeAction["__class"] = "bc19.TradeAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var MoveAction = (function (_super) {
        __extends(MoveAction, _super);
        function MoveAction(dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "move";
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return MoveAction;
    }(bc19.Action));
    bc19.MoveAction = MoveAction;
    MoveAction["__class"] = "bc19.MoveAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var AttackAction = (function (_super) {
        __extends(AttackAction, _super);
        function AttackAction(dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "attack";
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return AttackAction;
    }(bc19.Action));
    bc19.AttackAction = AttackAction;
    AttackAction["__class"] = "bc19.AttackAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var MyRobot = (function (_super) {
        __extends(MyRobot, _super);
        function MyRobot() {
            var _this = _super.call(this) || this;
            _this.run_once = true;
            _this.robotMemMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([64, 64]);
            _this.turnSeen = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([64, 64]);
            _this.d_list = [[1, 0], [1, 1], [0, 1], [-1, 1], [-1, 0], [-1, -1], [0, -1], [1, -1]];
            _this.knownTeamBots = new Array(4096);
            _this.knownEnemyBots = new Array(4096);
            _this.robotMap = null;
            _this.v_radius = 0;
            _this.v_radius_sq = 0;
            _this.robotList = null;
            _this.karboniteList = null;
            _this.fuelList = null;
            _this.path = null;
            return _this;
        }
        MyRobot.prototype.isInRange = function (p, r_sq) {
            var dx = p.x - this.me.x;
            var dy = p.y - this.me.y;
            return (dx * dx + dy * dy) <= r_sq;
        };
        MyRobot.prototype.isUnOccupied = function (x, y) {
            return this.map[y][x] && this.robotMap[y][x] === 0;
        };
        MyRobot.prototype.isPointVisible = function (p) {
            return this.isInRange(p, this.SPECS.UNITS[this.me.unit].VISION_RADIUS);
        };
        MyRobot.prototype.getKarboniteList = function () {
            var count = 0;
            for (var y = 0; y < this.map.length; y++) {
                for (var x = 0; x < this.map[0].length; x++) {
                    if (this.karboniteMap[y][x]) {
                        count++;
                    }
                }
                ;
            }
            ;
            this.karboniteList = new Array(count);
            count = 0;
            for (var y = 0; y < this.map.length; y++) {
                for (var x = 0; x < this.map[0].length; x++) {
                    if (this.karboniteMap[y][x]) {
                        this.karboniteList[count] = new bc19.Point2D(x, y);
                        count++;
                    }
                }
                ;
            }
            ;
        };
        MyRobot.prototype.getFuelList = function () {
            var count = 0;
            for (var y = 0; y < this.map.length; y++) {
                for (var x = 0; x < this.map[0].length; x++) {
                    if (this.fuelMap[y][x]) {
                        count++;
                    }
                }
                ;
            }
            ;
            this.fuelList = new Array(count);
            count = 0;
            for (var y = 0; y < this.map.length; y++) {
                for (var x = 0; x < this.map[0].length; x++) {
                    if (this.fuelMap[y][x]) {
                        this.fuelList[count] = new bc19.Point2D(x, y);
                        count++;
                    }
                }
                ;
            }
            ;
        };
        MyRobot.prototype.getMove = function (s, t, fuel) {
            this.log("start: (" + s.x + ", " + s.y + ")");
            this.log("target: (" + t.x + ", " + t.y + ")");
            this.path.bot = this;
            var temp = this.path.get_move(s, t, fuel);
            if (temp != null)
                this.log("path result: (" + temp.x + ", " + temp.y + ")");
            else
                this.log("path result: nullll");
            return temp;
        };
        MyRobot.prototype.updateMemMap = function () {
            var x;
            var y;
            for (var dy = -this.v_radius; dy <= this.v_radius; dy++) {
                for (var dx = -this.v_radius; dx <= this.v_radius; dx++) {
                    if ((dx * dx + dy * dy) > this.v_radius_sq)
                        continue;
                    x = dx + this.me.x;
                    y = dy + this.me.y;
                    if (!this.path.isPointInBounds$int$int(x, y))
                        continue;
                    this.robotMemMap[y][x] = this.robotMap[y][x];
                    this.turnSeen[y][x] = this.me.turn;
                }
                ;
            }
            ;
        };
        MyRobot.prototype.getMemMap = function (y, x) {
            var age = this.me.turn - this.turnSeen[y][x];
            if (this.robotMemMap[y][x] === -1 || age > bc19.Params.MEM_MAP_TIMEOUT)
                return -1;
            else
                return this.robotMemMap[y][x];
        };
        MyRobot.prototype.turn = function () {
            if (this.me.turn > 100)
                return null;
            if (this.run_once) {
                bc19.CastleTalker.bot = this;
                this.v_radius = (Math.sqrt(this.SPECS.UNITS[this.me.unit].VISION_RADIUS) | 0);
                this.v_radius_sq = this.SPECS.UNITS[this.me.unit].VISION_RADIUS;
                this.path = new bc19.Path(this.map, this.me.unit, this.SPECS.UNITS[this.me.unit].VISION_RADIUS, this.SPECS.UNITS[this.me.unit].SPEED);
                this.run_once = false;
                this.getKarboniteList();
                this.getFuelList();
            }
            this.robotMap = this.getVisibleRobotMap();
            this.robotList = this.getVisibleRobots();
            this.updateMemMap();
            this.path.setRMap(this.robotMap);
            this.log("unit " + this.me.unit);
            switch ((this.me.unit)) {
                case bc19.Params.CASTLE:
                    if (bc19.Castle.bot == null)
                        bc19.Castle.bot = this;
                    return bc19.Castle.turn();
                case bc19.Params.CHURCH:
                    break;
                case bc19.Params.PILGRIM:
                    if (bc19.Pilgrim.bot == null)
                        bc19.Pilgrim.bot = this;
                    return bc19.Pilgrim.turn();
                case bc19.Params.CRUSADER:
                    break;
                case bc19.Params.PROPHET:
                    break;
                case bc19.Params.PREACHER:
                    break;
            }
            return null;
        };
        MyRobot.prototype.getClosestFuel = function () {
            var dist_sq;
            var min_dist = 1000000;
            var dx;
            var dy;
            var tx;
            var ty;
            var index = -1;
            for (var i = 0; i < this.fuelList.length; i++) {
                tx = this.fuelList[i].x;
                ty = this.fuelList[i].y;
                dx = tx - this.me.x;
                dy = ty - this.me.y;
                dist_sq = dx * dx + dy * dy;
                if (dist_sq < min_dist && this.map[ty][tx] && this.robotMemMap[ty][tx] <= 0) {
                    min_dist = dist_sq;
                    index = i;
                }
            }
            ;
            if (index === -1)
                return null;
            else
                return new bc19.Point2D(this.fuelList[index].x, this.fuelList[index].y);
        };
        MyRobot.prototype.getClosestKarbonite = function () {
            var dist_sq;
            var min_dist = 1000000;
            var dx;
            var dy;
            var tx;
            var ty;
            var index = -1;
            for (var i = 0; i < this.karboniteList.length; i++) {
                tx = this.karboniteList[i].x;
                ty = this.karboniteList[i].y;
                dx = tx - this.me.x;
                dy = ty - this.me.y;
                dist_sq = dx * dx + dy * dy;
                if (dist_sq < min_dist && this.map[ty][tx] && this.robotMemMap[ty][tx] <= 0) {
                    min_dist = dist_sq;
                    index = i;
                }
            }
            ;
            if (index === -1)
                return null;
            else
                return new bc19.Point2D(this.karboniteList[index].x, this.karboniteList[index].y);
        };
        MyRobot.prototype.getClosestMine = function () {
            var closest_k = this.getClosestKarbonite();
            var closest_f = this.getClosestFuel();
            if (closest_k == null && closest_f == null)
                return null;
            if (closest_k == null && closest_f != null)
                return closest_f;
            if (closest_k != null && closest_f == null)
                return closest_k;
            var dxk = closest_k.x - this.me.x;
            var dyk = closest_k.y - this.me.y;
            var dxf = closest_f.x - this.me.x;
            var dyf = closest_f.y - this.me.y;
            var dist_f = (dxf * dxf) + (dyf * dyf);
            var dist_k = (dxk * dxk) + (dyk * dyk);
            if (dist_f > dist_k)
                return closest_k;
            else
                return closest_f;
        };
        return MyRobot;
    }(bc19.BCAbstractRobot));
    bc19.MyRobot = MyRobot;
    MyRobot["__class"] = "bc19.MyRobot";
})(bc19 || (bc19 = {}));
bc19.Pilgrim.churchesByID_$LI$();
bc19.Pilgrim.churchList_$LI$();
bc19.Pilgrim.churchIDList_$LI$();
bc19.Pilgrim.state_$LI$();
//# sourceMappingURL=bundle.js.map
var specs = {"COMMUNICATION_BITS":16,"CASTLE_TALK_BITS":8,"MAX_ROUNDS":1000,"TRICKLE_FUEL":25,"INITIAL_KARBONITE":100,"INITIAL_FUEL":500,"MINE_FUEL_COST":1,"KARBONITE_YIELD":2,"FUEL_YIELD":10,"MAX_TRADE":1024,"MAX_BOARD_SIZE":64,"MAX_ID":4096,"CASTLE":0,"CHURCH":1,"PILGRIM":2,"CRUSADER":3,"PROPHET":4,"PREACHER":5,"RED":0,"BLUE":1,"CHESS_INITIAL":100,"CHESS_EXTRA":20,"TURN_MAX_TIME":200,"MAX_MEMORY":50000000,"UNITS":[{"CONSTRUCTION_KARBONITE":null,"CONSTRUCTION_FUEL":null,"KARBONITE_CAPACITY":null,"FUEL_CAPACITY":null,"SPEED":0,"FUEL_PER_MOVE":null,"STARTING_HP":100,"VISION_RADIUS":100,"ATTACK_DAMAGE":null,"ATTACK_RADIUS":null,"ATTACK_FUEL_COST":null,"DAMAGE_SPREAD":null},{"CONSTRUCTION_KARBONITE":50,"CONSTRUCTION_FUEL":200,"KARBONITE_CAPACITY":null,"FUEL_CAPACITY":null,"SPEED":0,"FUEL_PER_MOVE":null,"STARTING_HP":50,"VISION_RADIUS":100,"ATTACK_DAMAGE":null,"ATTACK_RADIUS":null,"ATTACK_FUEL_COST":null,"DAMAGE_SPREAD":null},{"CONSTRUCTION_KARBONITE":10,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":1,"STARTING_HP":10,"VISION_RADIUS":100,"ATTACK_DAMAGE":null,"ATTACK_RADIUS":null,"ATTACK_FUEL_COST":null,"DAMAGE_SPREAD":null},{"CONSTRUCTION_KARBONITE":20,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":9,"FUEL_PER_MOVE":1,"STARTING_HP":40,"VISION_RADIUS":36,"ATTACK_DAMAGE":10,"ATTACK_RADIUS":[1,16],"ATTACK_FUEL_COST":10,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":25,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":2,"STARTING_HP":20,"VISION_RADIUS":64,"ATTACK_DAMAGE":10,"ATTACK_RADIUS":[16,64],"ATTACK_FUEL_COST":25,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":30,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":3,"STARTING_HP":60,"VISION_RADIUS":16,"ATTACK_DAMAGE":20,"ATTACK_RADIUS":[1,16],"ATTACK_FUEL_COST":15,"DAMAGE_SPREAD":3}]};
var robot = new bc19.MyRobot(); robot.setSpecs(specs);