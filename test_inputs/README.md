input1.dot

Results in the exact same graph as input for processor output (on a single processor)

`Node 0 - Start 0, Finish 7
Node 1 - Start 7, Finish 10
Node 2 - Start 10, Finish 15
Node 3 - Start 15, Finish 21
Node 4 - Start 21, Finish 25`

input2.dot

Results in the exact same graph as input for processor output (on a single processor)

See above for test results

input3.dot

`Node 0 - Start 0, Finish 4
Node 1/2/3 (in any order) - Start 4, Finish 6,8,10 (in any order)
Node 4/5 (in any order) - Start 10, Finish 15,20 (in any order)
Node 6 - Start 20, Finish 30`

input4.dot

`Node 0 - Start 0, Finish 5
Node 1/2/3/4 (in any order) - Start 5, Finish 10,15,20,25 (in any order)`


Client's provided graphs with the optimal length:

Scheduled on 2 processors:
Node_7_OutTree 28
Node_8_Random 581
Node_9_SeriesParallel 55
Node_10_Random 50
Node_11_OutTree 350
 
Scheduled on 4 processors:
Node_7_OutTree 22
Node_8_Random 581
Node_9_SeriesParallel 55
Node_10_Random 50
Node_11_OutTree 227