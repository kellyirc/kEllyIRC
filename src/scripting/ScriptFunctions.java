package scripting;

import java.util.Random;

public class ScriptFunctions {

    private Random gen = new Random();
	
    public final int rand(int i) {
    	return gen.nextInt(i);
    }
    
    public final int properRand(int i) {
    	return gen.nextInt(i)+1;
    }

	public final int rand(int x, int y) {
		return gen.nextInt(y - x) + x;
	}
	
	public final boolean prob(int x) {
    	return !(gen.nextInt(100) + 1 > x);
    }
	
	//TODO: file to string
	//TODO: hexapixel
}
