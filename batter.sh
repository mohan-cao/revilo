#!/usr/bin/env bash
start=$SECONDS
java -jar ./target/revilo-task-scheduler-1.0-SNAPSHOT.jar ./test_inputs/Fork_Nodes_30_CCR_1.02_WeightType_Random.gxl 4 -o temp.dot -p 1
duration=$(( SECONDS - start ))
printf 'took %d seconds to run\n' $duration