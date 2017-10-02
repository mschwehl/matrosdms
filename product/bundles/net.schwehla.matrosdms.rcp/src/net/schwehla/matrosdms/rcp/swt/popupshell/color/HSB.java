package net.schwehla.matrosdms.rcp.swt.popupshell.color;


/**
 * Represents the HSB (hue, saturation, brightness) color model
 */
public class HSB {
    public final float hue;
    public final float saturation;
    public final float brightness;

    /**
     * @param hue a degree of angle between 0 and 360
     * @param saturation a fraction between 0 and 1
     * @param brightness a fraction between 0 and 1
     */
    public HSB(float hue, float saturation, float brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }
}
