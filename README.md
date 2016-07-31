# Tuned Queue for Laravel

This simple Java program it is an Tuned Queue that it is used to dispatch more efficiently Laravel Queues. 

Introduction
-------------

When using Laravel Queues, one of the problems it is that you should dispatch the queue running an external command in the console. There's several approaches to do this, but in general determining the amount of workers that you need to dispatch the queue can be really difficult, and more when increasing this workers consumes more of your server resources. 

The Tuned Queue launches 1 Worker every a certein interval of time, and controls the maximum numbers of queues that can be held in the system. In this way, when your system it is requiring much more workers, it automatically will continue launching new workers up to the Maximum number)

How to use
-----------

* Place the TunedQueue.jar in your Laravel Route folder 
* Run the jar with `java -jar TunedQueue.jar <Max Workers> <Time millisecond>`

Example
--------
`java -jar TunedQueue.jar 50 500`



