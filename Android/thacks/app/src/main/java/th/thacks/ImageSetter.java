package th.thacks;

import java.io.IOException;

/**
 * Created by c1 on 3/25/17.
 */

public class ImageSetter implements Runnable{
    private final int[] colors;
    private final Main parent;
    public ImageSetter(final int[] colors,Main parent)
    {
        this.colors = colors;
        this.parent = parent;
    }

    @Override
    public void run()
    {
        try
        {
            parent.requestImage(colors);
        } catch(IOException ignored) {}
    }
}
