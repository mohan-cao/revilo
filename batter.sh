#!/usr/bin/env bash
set -x #echo on
start=$SECONDS
java -jar ./target/revilo-task-scheduler-1.0-SNAPSHOT.jar ./test_inputs/Nodes_7_OutTree.dot 2 -o temp.dot -p 1 | grep "Optimal length found:"
echo 28
java -jar ./target/revilo-task-scheduler-1.0-SNAPSHOT.jar ./test_inputs/Nodes_7_OutTree.dot 4 -o temp.dot -p 1 | grep "Optimal length found:"
echo 22
java -jar ./target/revilo-task-scheduler-1.0-SNAPSHOT.jar ./test_inputs/Nodes_11_OutTree.dot 2 -o temp.dot -p 1 | grep "Optimal length found:"
echo 350
java -jar ./target/revilo-task-scheduler-1.0-SNAPSHOT.jar ./test_inputs/Nodes_11_OutTree.dot 4 -o temp.dot -p 1 | grep "Optimal length found:"
echo  227
duration=$(( SECONDS - start ))
printf 'took %d seconds to run\n' $duration