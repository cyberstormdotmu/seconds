package twitter4j;

public class Size implements MediaEntity.Size {
    private static final long serialVersionUID = -2515842281909325169L;
    int width;
    int height;
    int resize;

    public Size(JSONObject json) throws JSONException {
        width = json.getInt("w");
        height = json.getInt("h");
        resize = "fit".equals(json.getString("resize")) ? MediaEntity.Size.FIT : MediaEntity.Size.CROP;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getResize() {
        return resize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Size)) return false;

        Size size = (Size) o;

        if (height != size.height) return false;
        if (resize != size.resize) return false;
        if (width != size.width) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + resize;
        return result;
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                ", resize=" + resize +
                '}';
    }
}
