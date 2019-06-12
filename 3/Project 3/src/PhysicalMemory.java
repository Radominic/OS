import java.util.Stack;
/**
 * Class to emulate the physical memory
 */
public class PhysicalMemory{
    /**
     * variable to emulate frames in memory
     */
    Frame[] frames;
    /**
     * we need a variable to store how many
     * frames are used
     */
    int currentFreeFrame;
    
    int victimFrame;
    
    int page_replace;
    
    String method;
    


    /**
     * Constructor
     */
    public PhysicalMemory(String method){
    	if(method == "ORIGINAL")
    		this.frames = new Frame[256];
    	else {
    		this.frames = new Frame[128];
    	}
        this.currentFreeFrame = 0;
        this.victimFrame = 0;
        this.page_replace = 0;
        this.method = method;
    }


    /**
     * function to add a new frame to memory
     *
     * @param f Frame to be added
     * @return int the frame number just added
     */
    
    public int addFrame(Frame f,PageTable pt){
    	if(method=="ORIGINAL") {
            this.frames[this.currentFreeFrame] = new Frame(f.data);
            this.currentFreeFrame++;
            return this.currentFreeFrame-1;
    	}else if(method == "FIFO") {
        	if(this.currentFreeFrame == this.frames.length) {
        		pt.remove(victimFrame);
        		this.frames[this.victimFrame] = new Frame(f.data);
        		int result = this.victimFrame;
        		this.victimFrame++;
        		this.victimFrame %= 128;
        		page_replace ++;
        		return result;
        	}else {
                this.frames[this.currentFreeFrame] = new Frame(f.data);
                this.currentFreeFrame++;
                return this.currentFreeFrame-1;
        	}
    	}else {
        	if(this.currentFreeFrame == this.frames.length) {
        		victimFrame = (int)pt.recent.firstElement();
        		pt.recent.removeElement(pt.recent.firstElement());
        		pt.remove(victimFrame);
        		this.frames[this.victimFrame] = new Frame(f.data);
        		pt.recent.push(this.victimFrame);
        		page_replace ++;
        		return this.victimFrame;
        	}else {
                this.frames[this.currentFreeFrame] = new Frame(f.data);
                pt.recent.push(this.currentFreeFrame);
                this.currentFreeFrame++;
                return this.currentFreeFrame-1;
        	}
    	}
    }

    /**
     * function to get value in memory
     *
     * @param f_num int frame number
     * @param offset int offset
     * @return int content in specified location
     */
    public int getValue(int f_num, int offset){
        Frame frame = this.frames[f_num];
        return frame.data[offset];
    }


}


/**
 * wrapper class to group all frame related logics
 */
class Frame {
    /**
     * variable to store datas of this frame
     */
    int[] data;


    /**
     * Constructor
     *
     * @param d int[256] for initializing frame
     */
    public Frame(int[] d){
        this.data = new int[256];
        for(int i=0;i<256;i++){
            this.data[i] = d[i];
        }
    }


    /**
     * Copy Constructor
     *
     * @param f Frame to be copied
     */
    public Frame(Frame f){
        this.data = new int[256];
        System.arraycopy(f.data, 0, this.data, 0, 256);
    }
}

