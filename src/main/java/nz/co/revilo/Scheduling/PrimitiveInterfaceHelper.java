package nz.co.revilo.Scheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * Primitive to Object conversion helper for results
 */
public class PrimitiveInterfaceHelper {
	/**
	 * Creates an object type 2d Boolean list from primitive matrix
	 * @param primMatrix the primitive boolean 2d matrix
	 * @return b the reference type list
	 */
    public static List<List<Boolean>> primToBoolean2D(boolean[][] primMatrix) {
        List<List<Boolean>> booleanMatrix = new ArrayList<>();
        for (int row = 0; row < primMatrix.length; row++) {
			booleanMatrix.add(new ArrayList<>());
			for (int col = 0; col < primMatrix[row].length; col++) {
				booleanMatrix.get(row).add(primMatrix[row][col]);
			}
		}
		return booleanMatrix;
	}

	/**
	 * Creates an object type 2d Integer list from primitive matrix
	 * @param primMatrix the primitive int 2d matrix
	 * @return n the reference type list
	 */
    public static List<List<Integer>> primToInteger2D(int[][] primMatrix) {
        List<List<Integer>> integerMatrix = new ArrayList<>();
        for (int row = 0; row < primMatrix.length; row++) {
			integerMatrix.add(new ArrayList<>());
			for (int col = 0; col < primMatrix[row].length; col++) {
				integerMatrix.get(row).add(primMatrix[row][col]);
			}
		}
		return integerMatrix;
	}

	/**
	 * Creates an object type Integer list from primitive int array
	 * 
	 * @param primArray
	 * @return
	 */
    public static List<Integer> primToInteger1D(int[] primArray) {
        ArrayList<Integer> integerList = new ArrayList<>();
        for (int i: primArray) {
			integerList.add(i);
		}
		return integerList;
	}
}
