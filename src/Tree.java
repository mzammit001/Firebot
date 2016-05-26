/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

public class Tree {
    private int height;
    private int intensity;
    private boolean burntDown;

	public Tree(int height) {
        this.height = height;
        this.intensity = 0;
        this.burntDown = false;
	}

    /**
     * is the tree burnt down
     * @return
     */
    public boolean isBurntDown() {
        return this.burntDown;
    }

    /**
     * get the current height
     * @return
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * get the current fire intensity
     * @return
     */
    public int getIntensity() {
        return this.intensity;
    }

    /**
     * set the fire intensity
     * @param intensity
     */
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void tryBurn() {
        if (getIntensity() == 9 && getHeight() > 0)
            setHeight(getHeight()-1);
        else if (getIntensity() > 0)
            setIntensity(getIntensity()+1);
        else
            return;

        // all we got left is embers, farewell tree
        if (getIntensity() == 9 && getHeight() == 0) {
            this.burntDown = true;
            setIntensity(0);
        }
    }

    /**
     * get the trees contribution to pollution
     * @return
     */
    public int getPollution() {
        int p = 0;
        p -= getHeight();
        p += getIntensity();

        return p;
    }

    /**
     * get a char value that we can print for height
     * @return
     */
    public char getPrintableHeight() {
        // display 'x' if burnt down
        if (isBurntDown())
            return 'x';

        // display ' ' if height is 0, else display height
        // number to ascii char is 0x30 + value, 0x31 = '1'... 0x39 = '9'
        return (getHeight() == 0) ? ' ' : (char)(0x30+getHeight());
    }

    /**
     * get a char value we can print for fire intensity
     * @return
     */
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
