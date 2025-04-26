import java.util.ArrayList;

public abstract class MemoryManager {
    protected int[] totalMemory = new int[100000]; //Array of total memory
    protected ArrayList<int[]> freeBlocks = new ArrayList<>(); //Array of location and size of free blocks
    protected ArrayList<int[]> allocatedBlocks = new ArrayList<>(); //Array of location and size of allocated blocks

    public MemoryManager() {
        initializeMemory();
    }   
    //To reset the memory for each simulation
    public void reset() {
        allocatedBlocks.clear(); // Clear the list of allocated blocks
        freeBlocks.clear(); // Clear the list of free blocks
        initializeMemory(); // Reinitialize the memory with initial free blocks
    }

    //Start off with whole memory as one empty block
    public void initializeMemory() {
        freeBlocks.add(new int[]{0, totalMemory.length});
    }

    abstract int[] request(int size); //induvidual classes will impliment it

    void release(int[] block) {
        freeBlocks.add(block); //Adding to freeblocks list when a block is released
        allocatedBlocks.remove(block);// Removing from allocatedblocks list when a block is released
    }
}
