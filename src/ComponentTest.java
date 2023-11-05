import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testLeaf() {
        Component leaf = new Leaf("leaf");

        leaf.operation();

        String expected = "Я лист: leaf" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testComposite() {
        Composite root = new Composite("Root");
        Composite branch1 = new Composite("Branch 1");
        branch1.add(new Leaf("branch1-leaf1"));
        branch1.add(new Leaf("branch1-leaf2"));
        root.add(branch1);

        root.operation();
        String newLine = System.lineSeparator();
        assertEquals("Я компоновщик: Root" + newLine, outContent.toString());

        for (Component component : root) {
            component.operation();
        }

        String expected = "Я компоновщик: Root" + newLine +
                "Я компоновщик: Branch 1" + newLine +
                "Я лист: branch1-leaf1" + newLine +
                "Я лист: branch1-leaf2" + newLine;
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testDynamicStructureChange() {
        Composite root = new Composite("Root");
        Composite branch1 = new Composite("Branch 1");
        Leaf leaf1 = new Leaf("branch1-leaf1");
        Leaf leaf2 = new Leaf("branch1-leaf2");
        Leaf leaf3 = new Leaf("branch1-leaf3");
        branch1.add(leaf1);
        branch1.add(leaf2);
        branch1.add(leaf3);
        root.add(branch1);

        Iterator<Component> iterator = root.iterator();

        iterator.next().operation();
        outContent.reset();

        branch1.add(new Leaf("i'm not in iterator"));

        assertTrue(iterator.hasNext());
        iterator.next().operation();
        outContent.reset();

        leaf2.setName("Changed name leaf2");

        assertTrue(iterator.hasNext());
        iterator.next().operation();
        assertEquals("Я лист: Changed name leaf2" + System.lineSeparator(), outContent.toString());

        assertTrue(iterator.hasNext());
    }

    @Test
    public void testParallelIterator() {
        Composite root = prepareSimpleComposite();

        Iterator<Component> iterator1 = root.iterator();
        Iterator<Component> iterator2 = root.iterator();
        while (iterator1.hasNext() || iterator2.hasNext()) {
            assertTrue(iterator1.hasNext());
            Component component1 = iterator1.next();
            component1.operation();

            assertTrue(iterator2.hasNext());
            Component component2 = iterator2.next();
            component2.operation();

            assertEquals(component1, component2);
        }
    }

    @Test
    public void testParallelChangeIterator() {
        Composite root = prepareSimpleComposite();

        Iterator<Component> iterator1 = root.iterator();
        Iterator<Component> iterator2 = root.iterator();
        int count = 1;
        while (iterator1 != null && iterator1.hasNext() || iterator2.hasNext()) {
            if (iterator1 != null && iterator1.hasNext()) {
                Component component1 = iterator1.next();
                component1.operation();
                if (count == 2) {
                    iterator1 = null;
                }
            }

            assertNotNull(iterator2);
            Component component2 = iterator2.next();
            component2.operation();

            count++;
        }
    }

    private Composite prepareSimpleComposite() {
        Composite root = new Composite("Root");
        Composite branch1 = new Composite("Branch 1");
        Leaf leaf1 = new Leaf("branch1-leaf1");
        Leaf leaf2 = new Leaf("branch1-leaf2");
        Leaf leaf3 = new Leaf("branch1-leaf3");
        branch1.add(leaf1);
        branch1.add(leaf2);
        branch1.add(leaf3);
        root.add(branch1);
        return root;
    }
}
