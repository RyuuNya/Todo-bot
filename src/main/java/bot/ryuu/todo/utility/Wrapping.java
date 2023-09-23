package bot.ryuu.todo.utility;

public final class Wrapping<T> {
    private T object;

    private Wrapping(T object) {
        this.object = object;
    }

    public static <T> Wrapping<T> of(T object) {
        return new Wrapping<>(object);
    }

    public T get() {
        return object;
    }

    public void set(T object) {
        this.object = object;
    }
}
