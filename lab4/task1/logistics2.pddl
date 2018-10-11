(define (problem logistics_problem)
    (:domain logistics)
    (:objects
     city1 city2 city3 city4                          ;; there are three cities,
     truck1 truck2 truck3 truck4 truck5 truck6        ;; one small truck in each city
                                                      ;; and one big truck,
     train1 train2                                    ;; two trains
     airplane1 airplane2                              ;; two airplanes,
     office1 office2 office3 office4                  ;; offices are "non-airport" locations
     airport2 airport3                                ;; airports
     trainstation1 trainstation2 trainstation4        ;; 2 trainstations
     packet1 packet2 packet3                          ;; six packages to be delivered
     packet4 packet5 packet6
     packet7 packet8 packet9
    )
    (:init
     ;; Type declarations:
     (object packet1) (object packet2) (object packet3)
     (object packet4) (object packet5) (object packet6)
     (object packet7) (object packet8) (object packet9)

     ;; all vehicles must be declared as both "vehicle" and their
     ;; appropriate subtype,
     (vehicle truck1) (vehicle truck2) (vehicle truck3) (vehicle truck4)
     (vehicle truck5) (vehicle truck6)
     (vehicle airplane1) (vehicle airplane2) (vehicle train1)
     (vehicle train2)
     (truck truck1) (truck truck2) (truck truck3)
     (truck truck4) (truck truck5) (truck truck6)
     (airplane airplane1) (airplane airplane2)
     (train train1) (train train2) (train train4)

     ;; likewise, airports must be declared both as "location" and as
     ;; the subtype "airport",
     (location office1) (location office2) (location office3) (location office4)
     (location airport2) (location airport3)
     (location trainstation1) (location trainstation2) (location trainstation4)
     (office office1) (office office2) (office office3) (office office4)
     (airport airport2) (airport airport3)
     (trainstation trainstation1) (trainstation trainstation2) (trainstation trainstation4)
     (city city1) (city city2) (city city3) (city city4)

     ;; "loc" defines the topology of the problem,
     (loc office1 city1)  (loc office2 city2) (loc office3 city3) (loc office4 city4)
     (loc airport3 city3) (loc airport2 city2)
     (loc trainstation2 city2) (loc trainstation1 city1) (loc trainstation4 city4)

     ;; Size of things
     (small packet1) (small packet2) (small packet3)
     (large packet4) (large packet5) (large packet6)
     (small packet7) (large packet8) (large packet9)
     (large truck1) (large truck2) (large truck3)
     (large truck4) (small truck5) (small truck6)


     ;; The actual initial state of the problem, which specifies the
     ;; initial locations of all packages and all vehicles:
     (at_o packet1 office1) (at_o packet2 office3) (at_o packet3 office2)
     (at_o packet4 office2) (at_o packet5 office3) (at_o packet6 office1)
     (at_o packet7 office4) (at_o packet8 office4) (at_o packet9 office1)
     (at_v truck1 trainstation1) (at_v truck2 airport2) (at_v truck3 office3)
     (at_v truck4 office4) (at_v truck5 office2) (at_v truck6 office4)
     (at_v airplane1 airport3) (at_v airplane2 airport2)
     (at_v train1 trainstation1) (at_v train2 trainstation2)
    )

    ;; The goal is to have both packages delivered to their destinations:
    (:goal  (and
              (at_o packet1 office3) (at_o packet2 office4) (at_o packet3 office2)
              (at_o packet4 office4) (at_o packet5 office2) (at_o packet6 office3)
              (at_o packet7 airport2) (at_o packet8 airport3) (at_o packet9 trainstation1)
            )
    )
)
