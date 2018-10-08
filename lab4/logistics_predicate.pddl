(define (domain logistics)
    (:requirements :strips)
    (:predicates
        ;; Vehicle types
        (vehicle ?v)
        (truck ?t)
        (airplane ?a)
        (train ?t)

        ;; Location types
        (location ?l)
        (trainstation ?t)
        (city ?c)
        (office ?o)

        ;; Relations
        (at ?obj ?location)
        (in ?packet ?vehicle)

        ;; Attributes
        (small ?obj)
        (medium ?obj)
        (large ?obj)
    )

    (:action LoadTruck
        :parameters (?p ?t ?l) ;; Packet, Truck, Location
        :precondition (and
            (packet ?p)
            (truck ?t)
            (location ?l)
            (at ?t ?l)
            (at ?p ?l)
        )
        :effect (and
            (in ?p ?t)
            (not (at ?p ?l))
        )
    )

    (:action UnloadTruck
        :parameters (?o ?t ?l) ;; Object, Truck, Location
        :precondition (and
            (packet ?o)
            (truck ?t)
            (location ?l)
            (in ?o ?t)
            (at ?t ?l)
        )
        :effect (and
            (at ?p ?l)
            (not (in ?p ?t))
        )
    )

)
