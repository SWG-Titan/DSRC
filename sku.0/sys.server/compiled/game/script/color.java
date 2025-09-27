// color.java


package script;

public class color
{
    public static final int DEFAULT_ALPHA = 255;
    public static final color BLACK = new color(0, 0, 0, DEFAULT_ALPHA);
    public static final color BLUE = new color(0, 0, 255, DEFAULT_ALPHA);
    public static final color GREEN = new color(0, 255, 0, DEFAULT_ALPHA);
    public static final color RED = new color(255, 0, 0, DEFAULT_ALPHA);
    public static final color WHITE = new color(255, 255, 255, DEFAULT_ALPHA);
    public static final color GOLD = new color(255, 215, 0, DEFAULT_ALPHA);
    public static final color GOLDENROD = new color(218, 165, 32, DEFAULT_ALPHA);
    public static final color DEEPPINK = new color(255, 20, 147, DEFAULT_ALPHA);
    public static final color ORANGE = new color(255, 165, 0, DEFAULT_ALPHA);
    public static final color PURPLE = new color(128, 0, 128, DEFAULT_ALPHA);
    public static final color CYAN = new color(0, 255, 255, DEFAULT_ALPHA);
    public static final color MAGENTA = new color(255, 0, 255, DEFAULT_ALPHA);
    public static final color BROWN = new color(139, 69, 19, DEFAULT_ALPHA);
    public static final color DARKGRAY = new color(169, 169, 169, DEFAULT_ALPHA);
    public static final color LIGHTGRAY = new color(211, 211, 211, DEFAULT_ALPHA);
    public static final color NAVY = new color(0, 0, 128, DEFAULT_ALPHA);
    public static final color TURQUOISE = new color(64, 224, 208, DEFAULT_ALPHA);
    public static final color LIME = new color(50, 205, 50, DEFAULT_ALPHA);

    private final int m_r;
    private final int m_g;
    private final int m_b;
    private final int m_a;

    public color(int r, int g, int b, int a)
    {
        if ((r < 0) || (r > 255) ||
                (g < 0) || (g > 255) ||
                (b < 0) || (b > 255) ||
                (a < 0) || (a > 255))
        {
            throw new IllegalArgumentException("color value arg out of valid range 0..255");
        }

        m_r = r;
        m_g = g;
        m_b = b;
        m_a = a;
    }

    public int getR()
    {
        return m_r;
    }

    public int getG()
    {
        return m_g;
    }

    public int getB()
    {
        return m_b;
    }

    public int getA()
    {
        return m_a;
    }
}
