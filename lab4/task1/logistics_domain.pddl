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

        ;; Objects
        (object ?o)

        ;; Relations
        (at_v ?vehicle ?location)
        (at_o ?object ?location)
        (in ?object ?vehicle)
        (loc ?l ?c) ;; Location in city

        ;; Attributes
        (small ?object)
        (large ?object)
    )

    (:action LoadSmallTruck
        :parameters (?o ?t ?l) ;; Packet, Truck, Location
        :precondition (and
            (object ?o)
            (small ?o)
            (truck ?t)
            (location ?l)
            (at_v ?t ?l)
            (at_o ?o ?l)
        )
        :effect (and
            (in ?o ?t)
            (not (at_o ?o ?l))
        )
    )
    (:action LoadBigTruck
        :parameters (?o ?t ?l) ;; Packet, Truck, Location
        :precondition (and
            (object ?o)
            (large ?o)
            (large ?t)
            (truck ?t)
            (location ?l)
            (at_v ?t ?l)
            (at_o ?o ?l)
        )
        :effect (and
            (in ?o ?t)
            (not (at_o ?o ?l))
        )
    )

    (:action LoadAirplane
        :parameters (?o ?a ?l) ;; Packet, Airplane, Location
        :precondition (and
            (object ?o)
            (airplane ?a)
            (airport ?l)
            (at_v ?a ?l)
            (at_o ?o ?l)
        )
        :effect (and
            (in ?o ?a)
            (not (at_v ?o ?l))
        )
    )

    (:action LoadTrain
        :parameters (?o ?t ?l) ;; Packet, Train, Location (trainstation)
        :precondition (and
          (object ?o)
          (train ?t)
          (trainstation ?l)
          (at_v ?t ?l)
          (at_o ?o ?l)
        )
        :effect (and
          (in ?o ?t)
          (not (at_o ?o ?l))
        )
    )

    (:action Unload
        :parameters (?o ?v ?l) ;; Packet, Train, Location
        :precondition(and
          (object ?o)
          (vehicle ?v)
          (location ?l)
          (at_v ?v ?l)
          (in ?o ?v)
        )
        :effect (and
          (not (in ?o ?v))
          (at_o ?o ?l)
        )
    )

    (:action MoveTruck
        :parameters (?t ?from ?to ?c) ;; Truck, From city, to city
        :precondition (and
            (truck ?t)
            (location ?from)
            (location ?to)
            (city ?c)
            (loc ?from ?c)
            (loc ?to ?c)
            (at_v ?t ?from)
        )
        :effect (and
            (at_v ?t ?to)
            (not (at_v ?t ?from))
        )
    )
    (:action MoveTrain
        :parameters (?t ?from ?to) ;; Train, From, to
        :precondition (and
            (train ?t)
            (trainstation ?from)
            (trainstation ?to)
            (at_v ?t ?from)
        )
        :effect (and
            (at_v ?t ?to)
            (not (at_v ?t ?from))
        )
    )
    (:action MoveAirplane
        :parameters (?a ?from ?to) ;; Airplane, From, to
        :precondition (and
            (airplane ?a)
            (airport ?from)
            (airport ?to)
            (at_v ?a ?from)
        )
        :effect (and
            (at_v ?a ?to)
            (not (at_v ?a ?from))
        )
    )
)
