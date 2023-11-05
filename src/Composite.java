import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Composite implements Component, Iterable<Component> {

    private final String name;

    List<Component> children = new LinkedList<>();

    public Composite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Component component) {
        children.add(component);
    }

    @Override
    public void operation() {
        System.out.println("Я компоновщик: " + name);
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public Iterator<Component> iterator() {
        Stack<Component> stack = new Stack<>();
        stack.addAll(children);
        return new CompositeIterator(stack);
    }
}
