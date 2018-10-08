(define (domain logistics)
    (:requirements :strips)
    (:predicates
        ;; Vehicle types
        (vehicle ?v)
        (truck ?t)
        (airplane ?a)
        (train ?t)

        ;; Location types
        (city ?c)
        (location ?l) ;; Location in city
        (trainstation ?t)
        (office ?o)
        (airport ?a)

        ;; Relations
        (at ?obj ?location)
        (in ?packet ?vehicle)
        (loc ?l ?c) ;; Location in city

        ;; Attributes
        (small ?obj)
        (medium ?obj)
        (large ?obj)
    )

    (:action LoadTruck
        :parameters (?o ?t ?l) ;; Packet, Truck, Location
        :precondition (and
            (packet ?o)
            (truck ?t)
            (location ?l)
            (at ?t ?l)
            (at ?o ?l)
        )
        :effect (and
            (in ?o ?t)
            (not (at ?o ?l))
        )
    )
    (:action LoadAirplane
        :parameters (?o ?a ?l) ;; Packet, Airplane, Location
        :precondition (and 
            (packet ?o)
            (airplane ?a)
            (airport ?l)
            (at ?a ?l)
            (at ?o ?l)
        )
        :effect (and
            (in ?o ?a)
            (not (at ?o ?l))
        )
    )
    (:action LoadTrain
        :parameters (?o ?t ?l) ;; Packet, Train, Location (trainstation)
        :precondition (and 
            (packet ?o)
            (train ?t)
            (trainstation ?l)
            (at ?t ?l)
            (at ?o ?l)
        )
        :effect (and
            (in ?o ?a)
            (not (at ?o ?l))
        )
    )
)
