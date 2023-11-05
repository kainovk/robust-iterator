import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompositeIterator implements Iterator<Component> {

    private final List<Component> elements;

    public CompositeIterator(List<Component> elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return !elements.isEmpty();
    }

    @Override
    public Component next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Component component = elements.remove(0);
        if (component.isComposite()) {
            elements.addAll(0, ((Composite) component).children);
        }

        return component;
    }
}
