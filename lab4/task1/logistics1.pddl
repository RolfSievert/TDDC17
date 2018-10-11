(define (problem logistics_problem)
    (:domain logistics)
    (:objects
     city1 city2                          ;; there are three cities,
     truck1 truck2 truck4                 ;; one small truck in each city
                                          ;; and one big truck,
     train1 train2                        ;; two trains
     airplane1 airplane2                  ;; two airplanes,
     office1 office2                      ;; offices are "non-airport" locations
     airport1 airport2                    ;; airports
     trainstation1 trainstation2          ;; 2 trainstations
     packet1 packet2 packet3              ;; six packages to be delivered
    )
    (:init
     ;; Type declarations:
     (object packet1) (object packet2) (object packet3)

     ;; all vehicles must be declared as both "vehicle" and their
     ;; appropriate subtype,
     (vehicle truck1) (vehicle truck2) (vehicle truck4)
     (vehicle airplane1) (vehicle airplane2) (vehicle train1) (vehicle train2)
     (truck truck1) (truck truck2) (truck truck4)
     (airplane airplane1) (airplane airplane2)
     (train train1) (train train2)

     ;; likewise, airports must be declared both as "location" and as
     ;; the subtype "airport",
     (location office1) (location office2)
     (location airport1) (location airport2)
     (location trainstation1) (location trainstation2)
     (office office1) (office office2)
     (airport airport1) (airport airport2)
     (trainstation trainstation1) (trainstation trainstation2)
     (city city1) (city city2)

     ;; "loc" defines the topology of the problem,
     (loc office1 city1)  (loc office2 city2)
     (loc airport1 city1) (loc airport2 city2)
     (loc trainstation2 city2) (loc trainstation1 city1)

     ;; Size of things
     (small packet1) (small packet2) (large packet3)
     (small truck4) (large truck1) (large truck2)


     ;; The actual initial state of the problem, which specifies the
     ;; initial locations of all packages and all vehicles:
     (at_o packet1 office1) (at_o packet2 trainstation1) (at_o packet3 office2)
     (at_v truck1 trainstation1) (at_v truck2 airport2)
     (at_v truck4 office1)
     (at_v airplane1 airport1) (at_v airplane2 airport2)
     (at_v train1 trainstation1) (at_v train2 trainstation2)
    )

    ;; The goal is to have both packages delivered to their destinations:
    (:goal  (and
              (at_o packet1 office2) (at_o packet2 office2) (at_o packet3 office1)
            )
    )
)
