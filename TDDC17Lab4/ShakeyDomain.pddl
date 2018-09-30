(define (domain shakey-c)
  (:requirements :strips)
  (:predicates
   (at ?what ?square)
   (adj ?square-1 ?square-2)
   (have ?who ?what)
   (is-agent ?who)
   (is-gold ?what)
   (is-box ?what)
   (is-turned-on ?switch ?where)
   (is-in-room ?what ?where)
   (is-notOn-switch1 ?what)
   (is-notOn-switch2 ?what)
   (is-notOn-switch3 ?what)
   (switch1 ?where)
   (switch2 ?where)
   (switch3 ?where))


  (:action switch 
    	:parameters (?who ?where ?what ?switch-on)
    	:precondition (and (is-agent ?who)
		       	(at ?who ?where)
			(not(is-turned-on ?switch-on ?where))
			(at ?switch-on ?where)
			(have ?who ?what))
    	:effect  (and (is-turned-on ?switch-on ?where))
)

  (:action take-box
	:parameters (?who ?what ?where)
    	:precondition (and (is-agent ?who)
			(is-box ?what)
		       	(at ?who ?where)
			(at ?what ?where)
		       	(not (have ?who ?what)))
    	:effect	(and (not (at ?what ?where))
			(have ?who ?what)
	)
)

  (:action use-door-wide
	:parameters (?who ?where ?to)
	:precondition (and (is-agent ?who)
			 (adj ?where ?to)
			 (at ?who ?where))
	:effect		(and (not(at ?who ?where))
				(at ?who ?to)
	)
)

  (:action take-gold
	:parameters (?who ?where ?what ?switch)
	:precondition (and (is-agent ?who)
			 (is-gold ?what)
			 (at ?who ?where)
			 (at ?what ?where)
			 (is-turned-on ?switch ?where))
	:effect		(and (not(at ?what ?where)) (have ?who ?what))
)
			

)

