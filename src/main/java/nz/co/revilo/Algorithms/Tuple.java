package nz.co.revilo.Algorithms;

/**
 * To represent the start time and processor number pair a node is assigned to
 * Java does not have a tuple class!! :O
 * 
 * Currently unused, as copied into BranchAndBound class
 * 
 * @author Abby S
 *
 */
class Tuple {
	int _startTime;
	int _processor;
	Tuple(int startTime, int processor){
		_startTime=startTime;
		_processor=processor;
	}
}
