/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

public class Tree {
    private int height;
    private int intensity;
    private boolean burntDown;
    //
    // TODO
    //

	public Tree(int height) {
        this.height = height;
        this.intensity = 0;
        this.burntDown = false;
	}

    public boolean isBurntDown() {
        return this.burntDown;
    }

    public int getHeight() {
        return this.height;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public char getPrintableHeight() {
        // display 'x' if burnt down
        if (isBurntDown())
            return 'x';

        // display ' ' if height is 0, else display height
        // number to ascii char is 0x30 + value, 0x31 = '1'... 0x39 = '9'
        return (getHeight() == 0) ? ' ' : (char)(0x30+getHeight());
    }

    public char getPrintableIntensity() {
        // display 'x' if burnt down
        if (isBurntDown())
            return 'x';

        // display ' ' if no height
        if (getHeight() == 0)
            return ' ';

        // display '.' if no fire else display intensity
        // number to ascii char is 0x30 + value, 0x31 = '1'... 0x39 = '9'
        return (getIntensity() == 0) ? '.' : (char)(0x30+getIntensity());
    }
}
