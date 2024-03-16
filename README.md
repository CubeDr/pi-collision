# PI Collision Simulator
Ispired by 3B1B's Youtube video: [The most unexpected answer to a counting puzzle]([url](https://www.youtube.com/watch?v=HEfHFsfGXjs&t=1s)).

When two objects and a wall performs a perfectly elastic collision, the number of total collision is related to PI.\
In some special cases, where the mass ratio is the power of 100, total number of collisions matches the leading digits of PI.

https://github.com/CubeDr/pi-collision/assets/13654700/0c254c3b-c7ab-4fb8-bd92-c4be3a6d59c5

This project simulates this phenomenon using the following techniques:
1. Multithreading
    - Two threads are used to serve seamless and smooth experience.
    - One thread is to calculate the collision events.
    - Another is to update the canvas periodically.
1. Collision Handling
    - General collision detection where we move the object first and check if they collided won't work here.
    - Too many collisions are happening in such a short time and we need to count them all.
    - By calculating the times when the collision will happen, we can track all collisions without losing any.
1. Simulating
    - One thread that calculates the collision events put the events in the queue.
    - Another thread tracks the time passed between frames and consume the events from the queue.
    - If no events happen, move the object to the position calculated from the last collision.
