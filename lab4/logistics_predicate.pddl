(define (domain shakeys_world)
    (:requirements :strips)
    (:predicates
        (room ?r) (switch ?sw) (off ?sw) (door ?d) (wide ?door) (robot ?r)
        (grip ?g) (box ?b) (at ?thing ?place) (holding ?grip ?thing)
        (connects ?door ?place1 ?place2))

    (:action move
      :parameters (?robot ?from ?to)
      :precondition(and (robot ?r) (room ?from)
                        (room ?to) (at ?robot ?from)
                        ()
                        )
    )
)
