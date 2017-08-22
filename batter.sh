#!/usr/bin/env bash
start=$SECONDS
java -cp . nz.co.revilo.App /test_inputs/Fork_Nodes_30_CCR_1.02_WeightType_Random.gxl 4 -o temp.dot -p 1
duration=$(( SECONDS - start ))