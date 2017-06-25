package sandbox;

/**
 * Created by Aleksey Papushin on 20.06.2017.
 */
public class Sandbox {
    public static void main(String[] args) {
        int[] vals = {3, 5, 1, 4, 7, 9, 10, 6, 4, 3, 2, -100, 234, 0};
        selectionSort(vals);
        for (int value : vals) {
            System.out.print(value + " ");
        }
    }

    public static void selectionSort(int[] vals) {
        int fromIndex = 0;

        while (fromIndex < vals.length - 1) {
            for (int i = fromIndex + 1; i < vals.length; i++) {
                if (vals[i] < vals[fromIndex]) {
                    int temp = vals[i];
                    vals[i] = vals[fromIndex];
                    vals[fromIndex] = temp;
                }
            }
            fromIndex++;
        }
    }
}
