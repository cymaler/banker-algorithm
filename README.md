### The Homework of OS in USTB Software Engineer



##### Abstract

Simulate Banker Algorithm  used Java. Using this program, you can finish the homework of os in USTB Software Engineer.



##### Quick Start

jdk version must be above 17.

```
java -jar banker-algorithm-stable.jar
```



##### Command

```
systemresource: return current system resource.

java -ps [pid]：return [pid] information(PCB)

readyqueue：return current system ready queue.

blockqueue：return current block queue.

runningprocess: return current running process.

java -process [pid] -resource [resourceId1,resourceId2,...,resourceIdn] -count [count1,count2,...,countn]：execute banker algorithm.
```



##### Result

```
16:15:16.828 [pool-1-thread-1] INFO com.cymal.service.CPU - cpu is starting...

#bankalgorithm: systemresource
system resource = [
available=[1, 5, 2, 0]
max=[[0, 0, 1, 2], [1, 7, 5, 0], [2, 3, 5, 6], [0, 6, 5, 2], [0, 6, 5, 6]]
allocation=[[0, 0, 1, 2], [1, 0, 0, 0], [1, 3, 5, 4], [0, 6, 3, 2], [0, 0, 1, 4]]
need=[[0, 0, 0, 0], [0, 7, 5, 0], [1, 0, 0, 2], [0, 0, 2, 0], [0, 6, 4, 2]]
]

#bankalgorithm: java -process 0 -resource 0,1,2,3 -count 0,4,2,0
16:15:51.753 [bank-algorithm-alloctor-1] INFO com.cymal.service.BankerAlgorithmService - the request resource [1] of process [0] is above the need.
16:15:51.753 [bank-algorithm-alloctor-2] INFO com.cymal.service.BankerAlgorithmService - the request resource [2] of process [0] is above the need.
16:15:51.763 [bank-algorithm-alloctor-3] INFO com.cymal.service.BankerAlgorithmService - the request resource [3] of process [0] allocate success.
16:15:51.763 [bank-algorithm-alloctor-0] INFO com.cymal.service.BankerAlgorithmService - the request resource [0] of process [0] allocate success.

#bankalgorithm: blockqueue
blockingQueue:
PCB(process=0, status=2, resource=[0, 1, 1, 0], k=[0, 0, 0, 0])

#bankalgorithm: java -process 1 -resource 0,1,2,3 -count 0,4,2,0
16:16:09.231 [bank-algorithm-alloctor-7] INFO com.cymal.service.BankerAlgorithmService - the request resource [3] of process [1] allocate success.
16:16:09.230 [bank-algorithm-alloctor-4] INFO com.cymal.service.BankerAlgorithmService - the request resource [0] of process [1] allocate success.
16:16:09.231 [bank-algorithm-alloctor-6] INFO com.cymal.service.BankerAlgorithmService - the request resource [2] of process [1] allocate success.
16:16:09.231 [bank-algorithm-alloctor-5] INFO com.cymal.service.BankerAlgorithmService - the request resource [1] of process [1] allocate success.

#bankalgorithm: 16:16:19.252 [pool-1-thread-1] INFO com.cymal.service.CPU - cpu execute a process:[PCB(process=1, status=0, resource=[0, 0, 0, 0], k=[0, 0, 0, 0])] consuming:[10015] ms
16:16:19.252 [pool-1-thread-1] INFO com.cymal.service.CPU - notify a process:[PCB(process=0, status=2, resource=[0, 1, 1, 0], k=[0, 0, 0, 0])] form blockqueue

#bankalgorithm:blockqueue
blockingQueue:

#bankalgorithm: systemresource
system resource = [
available=[1, 1, 0, 0]
max=[[0, 0, 1, 2], [1, 7, 5, 0], [2, 3, 5, 6], [0, 6, 5, 2], [0, 6, 5, 6]]
allocation=[[0, 0, 1, 2], [1, 4, 2, 0], [1, 3, 5, 4], [0, 6, 3, 2], [0, 0, 1, 4]]
need=[[0, 0, 0, 0], [0, 3, 3, 0], [1, 0, 0, 2], [0, 0, 2, 0], [0, 6, 4, 2]]
]
```

