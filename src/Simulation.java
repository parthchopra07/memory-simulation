import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedWriter;

public class Simulation {
    public static void main(String[] args) throws IOException {
        // Initialize memory managers
        MemoryManager manager1 = new MemoryManager1();
        MemoryManager manager2 = new MemoryManager2();

        // Initialize random number generator
        Random rand = new Random();

        // Variables to track errors and iterations
        int totalIterations = 0;
        int totalErrorsManager1 = 0;
        int totalErrorsManager2 = 0;

        try (FileWriter writer = new FileWriter("free_blocks.txt")) {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // Outer loop for 100 simulations
            for (int simulation = 1; simulation <= 100; simulation++) {
                // Reset memory managers for each simulation
                manager1.reset();
                manager2.reset();
                int iterations = 0;
                int errorsManager1 = 0;
                int errorsManager2 = 0;
                boolean errorOccurred = false;
                String errorMessage = "";

                // Inner loop until error or completion
                while (!errorOccurred) {
                    // Randomly decide whether to request memory or release memory
                    if (rand.nextBoolean()) {
                        int size = 4 + rand.nextInt(9997);
                        iterations++;
                        try {
                            manager1.request(size);
                        } catch (RuntimeException e) {
                            errorOccurred = true;
                            errorsManager1++;
                            errorMessage = "Error occurred in manager 1: " + e.getMessage();
                        }
                        try {
                            manager2.request(size);
                        } catch (RuntimeException e) {
                            errorOccurred = true;
                            errorsManager2++;
                            errorMessage = "Error occurred in manager 2: " + e.getMessage();
                        }
                    } else {
                        // Randomly release a block
                        if (!manager1.allocatedBlocks.isEmpty()) {
                            int index = rand.nextInt(manager1.allocatedBlocks.size());
                            manager1.release(manager1.allocatedBlocks.get(index));
                        }
                        if (!manager2.allocatedBlocks.isEmpty()) {
                            int index = rand.nextInt(manager2.allocatedBlocks.size());
                            manager2.release(manager2.allocatedBlocks.get(index));
                        }
                    }

                    // Check for completion condition
                    if (iterations >= 100000 || errorOccurred) {
                        errorOccurred = true; // Exit loop if maximum iterations reached or error occurred
                    }
                    writeFreeBlocksToFile(bufferedWriter, manager1, manager2, simulation, iterations, errorMessage);
                }

                // Update total errors and iterations
                totalIterations += iterations;
                totalErrorsManager1 += errorsManager1;
                totalErrorsManager2 += errorsManager2;
            }
        }
    
        // Print final report
        System.out.println("Memory Manager 1 Total Errors: " + totalErrorsManager1);
        System.out.println("Memory Manager 2 Total Errors: " + totalErrorsManager2);
        System.out.println("Total Iterations: " + totalIterations);
        if (totalErrorsManager1 < totalErrorsManager2) {System.out.println("First fit performed better");}
        if (totalErrorsManager1 > totalErrorsManager2) {System.out.println("Best fit performed better");}
        if (totalErrorsManager1 == totalErrorsManager2) {System.out.println("Both algorithms performed equally good");}
        System.out.println("List of free blocks for each manager per iteration and simulation along with manager which caused error first printed in a text file - text_block.txt");
    }

    private static void writeFreeBlocksToFile(BufferedWriter writer, MemoryManager manager1, MemoryManager manager2, int simulation, int iterations, String errorMessage) throws IOException {
        writer.write("Simulation: " + simulation + ", Iteration: " + iterations);
        writer.newLine();
        writer.write(errorMessage);
        writer.newLine();
        writer.write("Memory Manager 1 Free Blocks:");
        writer.newLine();
        writeFreeBlocks(writer, manager1.freeBlocks);
        writer.newLine();
        writer.write("Memory Manager 2 Free Blocks:");
        writer.newLine();
        writeFreeBlocks(writer, manager2.freeBlocks);
        writer.newLine();
    }

    private static void writeFreeBlocks(BufferedWriter writer, ArrayList<int[]> freeBlocks) throws IOException {
        for (int[] block : freeBlocks) {
            writer.write("Start: " + block[0] + ", Size: " + block[1]);
            writer.newLine();
        }
    }
}
