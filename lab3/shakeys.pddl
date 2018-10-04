(define (problem shakeys_world)
    (:domain shakeys_world)
    (:objects 
        room1 room2 room3
        box1
        door1 door2 door3
        switch1 switch2 switch3
        shakey
        gripleft
        gripright
        empty
    )
    (:init 
        (room room1)
        (room room2)
        (room room3)
        (switch switch1)
        (switch switch2)
        (switch switch3)
        (off switch1)
        (off switch2)
        (off switch3)
        (door door1)
        (door door2)
        (door door3)
        (wide door1)
        (wide door3)
        (robot shakey)
        (grip gripleft)
        (grip gripright)
        (box box1)
        (at shakey room2)
        (holding gripleft empty)
        (holding gripright empty)
        (at switch1 room1)
        (at switch2 room2)
        (at switch3 room3)
        (at box1 room1)
        (connects door1 room1 room2)
        (connects door1 room2 room1)
        (connects door2 room2 room3)
        (connects door2 room3 room2)
        (connects door3 room2 room3)
        (connects door3 room3 room2)
    )
    (:goal ()
)
