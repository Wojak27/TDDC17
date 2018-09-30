begin_version
3
end_version
begin_metric
0
end_metric
8
begin_variable
var0
-1
2
Atom at(agent, sq-1-3)
NegatedAtom at(agent, sq-1-3)
end_variable
begin_variable
var1
-1
2
Atom at(agent, sq-1-1)
NegatedAtom at(agent, sq-1-1)
end_variable
begin_variable
var2
-1
2
Atom at(the-box, sq-1-1)
NegatedAtom at(the-box, sq-1-1)
end_variable
begin_variable
var3
-1
2
Atom have(agent, the-box)
NegatedAtom have(agent, the-box)
end_variable
begin_variable
var4
-1
2
Atom is-turned-on(agent, sq-1-3)
NegatedAtom is-turned-on(agent, sq-1-3)
end_variable
begin_variable
var5
-1
2
Atom at(the-gold, sq-1-3)
NegatedAtom at(the-gold, sq-1-3)
end_variable
begin_variable
var6
-1
2
Atom is-turned-on(the-gold, sq-1-3)
NegatedAtom is-turned-on(the-gold, sq-1-3)
end_variable
begin_variable
var7
-1
2
Atom have(agent, the-gold)
NegatedAtom have(agent, the-gold)
end_variable
0
begin_state
1
1
0
1
1
0
1
1
end_state
begin_goal
1
7 0
end_goal
11
begin_operator
switch agent sq-1-3 the-box agent
2
0 0
3 0
1
0 4 1 0
1
end_operator
begin_operator
switch agent sq-1-3 the-box the-gold
3
0 0
5 0
3 0
1
0 6 1 0
1
end_operator
begin_operator
switch agent sq-1-3 the-gold agent
2
0 0
7 0
1
0 4 1 0
1
end_operator
begin_operator
switch agent sq-1-3 the-gold the-gold
3
0 0
5 0
7 0
1
0 6 1 0
1
end_operator
begin_operator
take-box agent the-box sq-1-1
1
1 0
2
0 2 0 1
0 3 1 0
1
end_operator
begin_operator
take-gold agent sq-1-3 the-gold agent
2
0 0
4 0
2
0 5 0 1
0 7 -1 0
1
end_operator
begin_operator
take-gold agent sq-1-3 the-gold the-gold
2
0 0
6 0
2
0 5 0 1
0 7 -1 0
1
end_operator
begin_operator
use-door-wide agent sq-1-1 sq-1-2
0
1
0 1 -1 1
1
end_operator
begin_operator
use-door-wide agent sq-1-2 sq-1-1
0
1
0 1 -1 0
1
end_operator
begin_operator
use-door-wide agent sq-1-2 sq-1-3
0
1
0 0 -1 0
1
end_operator
begin_operator
use-door-wide agent sq-1-3 sq-1-2
0
1
0 0 -1 1
1
end_operator
0
