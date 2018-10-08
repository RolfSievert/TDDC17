(define (problem logistics_problem)
    (:domain logistics)
    (:objects
     city1 city2 city3                  ;; there are three cities,
     truck1 truck2 truck3 truck4        ;; one small truck in each city
                                        ;; and one big truck,
     train1 train2                      ;; two trains
     airplane1 airplane2                ;; two airplanes,
     office1 office2 office3            ;; offices are "non-airport" locations
     airport2 airport3                  ;; airports
     trainstation1, trainstation2       ;; 2 trainstations
     packet1 packet2 packet3            ;; six packages to be delivered
     packet4 packet5 packet6
    )
    (:init
     ;; Type declarations:
     (object packet1) (object packet2) (object packet3)
     (object packet4) (object packet5) (object packet6)

     ;; all vehicles must be declared as both "vehicle" and their
     ;; appropriate subtype,
     (vehicle truck1) (vehicle truck2) (vehicle truck3) (vehicle truck4)
     (vehicle airplane1) (vehicle airplane2) (vehicle train1) (vehicle train2)
     (truck truck1) (truck truck2) (truck truck3) (truck truck4)
     (airplane airplane1) (airplane airplane2)
     (train train1) (train train2)

     ;; likewise, airports must be declared both as "location" and as
     ;; the subtype "airport",
     (location office1) (location office2) (location office3)
     (location airport2) (location airport3)
     (location trainstation1) (location trainstation2)
     (office office1) (office office2) (office office3)
     (airport airport2) (airport airport3)
     (trainstation trainstation1) (trainstation trainstation2)
     (city city1) (city city2) (city city3)

     ;; "loc" defines the topology of the problem,
     (loc office1 city1)  (loc office2 city2) (loc office3 city3)
     (loc airport3 city3) (loc airport2 city2)
     (loc trainstation2 city2) (loc trainstation1 city1)

     ;; Size of things
     (small packet1) (small packet2) (small packet3)
     (large packet4) (large packet5) (large packet6)
     (small truck4) (large truck1) (large truck2) (large truck3)


     ;; The actual initial state of the problem, which specifies the
     ;; initial locations of all packages and all vehicles:
     (at packet1 office1) (at packet2 office3) (at packet3 office2) (at packet4 office2)
     (at packet5 office3) (at packet6 office1)
     (at truck1 trainstation1) (at truck2 airport2) (at truck3 office3)
     (at truck4 office1)
     (at airplane1 airport3) (at airplane2 airport2)
     (at train1 trainstation1) (at train2 trainstation2)
    )

    ;; The goal is to have both packages delivered to their destinations:
    (:goal  (and
              (at packet1 office2) (at packet2 office2) (at packet3 office1)
              (at packet4 office3) (at packet5 office1) (at packet6 office2)
            )
    )
)
