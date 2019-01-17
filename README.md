COrn
Ultimate bc19 Justin Strategy and Tactics?

Overall:
Keep track of enemy and friendly units with castles.
Idea. Keep a robotMemMap for each robot. It keeps a map of seen robots at positions.
I will clear tiles that haven't been seen in X turns. It will refresh from robotMap

All units should by default avoid enemies units unless they are near a castle/church
	or a strike has been ordered.

Idea: since castle talk is free, every unit should do a STILL_ALIVE message every round so the castle knows what units it has.
Castles can always get castle talk from units, so units should use radios to communicate orders
	from castles to other units. Assuming units don't need to talk to each other.
Maybe castle could send out a long range radio message when it needs to broadcast to all units or just a single unit. This could be made more feasible by having all units stay within a max range from the castle they were constructed at.

Possibly Units could use radios to communicate with each other.
Possibly need multiturn communications to send more than 16bits.


Pilgrims. 
Macro
	Determine if we need fuel or karbonite more.
	Find the closest mine.
	Mine it.
	Consider building a chuch adjacent to mine for faster mining.
	Return to closest base as fast as possible, give to castle, repeat.
Micro
	Evade enemies by running away to an armed unit.	








Known bugs: 
Pilgrims keep trying to give resources to adjacent chucrhes that are not on its team...
Fixed: 

//pilgrims are not finding all the mines.
//some pilgrims are just standing motionless and retarded
//some pilgrims are standing frozen on top of mines saying can not read property 7 of undefined.



Pathfinding for pilgrims: if cant reach the castle to deposit stuff they give up. SHould hand resources to pilgrims adjacent to church/castle.








