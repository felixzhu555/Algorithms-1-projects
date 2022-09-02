/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Tests {
    static void unique_array(int[] my_array) {
        System.out.println("Original Array : ");

        for (int i = 0; i < my_array.length; i++) {
            System.out.print(my_array[i] + "\t");
        }

        //Assuming all elements in input array are unique

        int no_unique_elements = my_array.length;

        //Comparing each element with all other elements

        for (int i = 0; i < no_unique_elements; i++) {
            for (int j = i + 1; j < no_unique_elements; j++) {
                //If any two elements are found equal

                if (my_array[i] == my_array[j]) {
                    //Replace duplicate element with last unique element

                    my_array[j] = my_array[no_unique_elements - 1];

                    no_unique_elements--;

                    j--;
                }
            }
        }

        //Copying only unique elements of my_array into array1

        int[] array1 = Arrays.copyOf(my_array, no_unique_elements);

        //Printing arrayWithoutDuplicates

        System.out.println();

        System.out.println("Array with unique values : ");

        for (int i = 0; i < my_array.length; i++) {
            System.out.print(my_array[i] + "\t");
        }

        System.out.println();
        System.out.println(no_unique_elements);
        System.out.println("---------------------------");
    }

    public static void main(String[] args) {
        // unique_array(new int[] { 1, 1, 1, 2, 2, 2 });

        // unique_array(new int[] { 10, 22, 10, 20, 11, 22 });

        Point p1 = new Point(10000, 0);
        Point p2 = new Point(0, 10000);
        Point p3 = new Point(3000, 7000);
        LineSegment ls1 = new LineSegment(p1, p2);
        LineSegment ls2 = new LineSegment(p1, p3);
        LineSegment ls3 = new LineSegment(p2, p3);

        LineSegment[] a = new LineSegment[] { ls1, null, ls3, null };
        for (LineSegment ls : a) {
            StdOut.println(ls);
        }
        StdOut.println(a[1].toString().equals(a[3].toString()));
    }
}
