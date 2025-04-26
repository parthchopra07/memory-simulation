public class MemoryManager1 extends MemoryManager {
    //First Fit
    @Override
    int[] request(int size) {
        for (int i = 0; i < freeBlocks.size(); i++) {
            int[] block = freeBlocks.get(i);
            if (block[1] >= size) { // If the block is big enough
                int[] allocated = new int[]{block[0], size};// same location as our first found big block but with required size
                allocatedBlocks.add(allocated);// add to allocated block list
                if (block[1] > size) { // Update the free block if there's remaining space
                    freeBlocks.set(i, new int[]{block[0] + size, block[1] - size});// set the remaining space at i location-
                    //-starting at the end of our split up block (location + cutoff size) with ramianing the size
                } else {
                    freeBlocks.remove(i); // Remove the block from freelist if it's fully used
                }
                return allocated;
            }
        }
        throw new RuntimeException("Memory Manager 1 (First Fit)- Allocation error: No block large enough.");
    }
}
