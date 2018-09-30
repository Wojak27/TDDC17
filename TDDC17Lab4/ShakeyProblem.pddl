(define (problem shakey-c-1)
  (:domain shakey-c)
  (:objects sq-1-1 sq-1-2 sq-1-3
	    the-gold the-box
	    agent switch switch2 switch3 room1 room2 room3)
  (:init (adj sq-1-1 sq-1-2) (adj sq-1-2 sq-1-1)
	 (adj sq-1-2 sq-1-3) (adj sq-1-3 sq-1-2)
	 (is-gold the-gold)
	 (at the-gold sq-1-3)
	 (is-box the-box)
	 (at the-box sq-1-1)
	 (is-agent agent)
	 (not(have agent the-box))
	 (at agent sq-1-2)
	 (is-notOn-switch1 switch)
	 (not(is-turned-on switch sq-1-1))
	 (not(is-turned-on switch sq-1-2))
	 (not(is-turned-on switch sq-1-3))
	
)
  (:goal (and (have agent the-box) (have agent the-gold) ))
  )
