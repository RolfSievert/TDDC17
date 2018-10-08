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
)
