package nz.co.revilo.Algorithms;

/**
 * This algorithm stores all states in memory
 * Memory issue!
 * A* explores the least number of states, but may run out of memory before it finishes
 * 
 * IDA* and BBA* have a much smaller memory footprint
 * See the relevant classes
 * 
 * If A* runs out of memory, can use those classes for algorithm with comparable performance
 * Although cost is exploring more states, so only reasonably well performance
 * 
 * A* threads have to share the priority queue, and thus requires synchronisation
 * IDA* and BBA* can also be explored for parallelisation.
 * 
 * @author Abby S
 *
 */
public class AStar {

}
