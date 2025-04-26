public class MemoryManager2 extends MemoryManager {
    //Best Fit
    @Override
    int[] request(int size) {
        int bestIdx = -1; // to keep track of the best index
        int smallestDiff = Integer.MAX_VALUE;
        for (int i = 0; i < freeBlocks.size(); i++) {
            int[] block = freeBlocks.get(i);
            int diff = block[1] - size;
            if (diff >= 0 && diff < smallestDiff) { // Find the smallest block that fits
                bestIdx = i;// update the best index and smallest diff if better block is found
                smallestDiff = diff;
            }
        }
        if (bestIdx == -1) throw new RuntimeException("Memory Manager 2 (Worst Fit)- Allocation error: No block large enough.");
        
        int[] bestBlock = freeBlocks.get(bestIdx);
        int[] allocated = new int[]{bestBlock[0], size};
        allocatedBlocks.add(allocated);
        if (bestBlock[1] > size) { // size check and adding the left of block if the best fit is also bigger than the required size
            freeBlocks.set(bestIdx, new int[]{bestBlock[0] + size, bestBlock[1] - size});
        } else {
            freeBlocks.remove(bestIdx);
        }
        return allocated;
    }
}
