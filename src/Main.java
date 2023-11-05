import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Composite root = new Composite("Root");

        Composite branch1 = new Composite("Branch 1");
        branch1.add(new Leaf("branch1-leaf1"));
        branch1.add(new Leaf("branch1-leaf3"));
        branch1.add(new Leaf("branch1-leaf2"));

        Composite branch2 = new Composite("Branch 2");
        branch2.add(new Leaf("branch2-leaf100"));
        branch2.add(new Leaf("branch2-leaf200"));

        root.add(branch1);
        root.add(branch2);

        root.operation();

        Iterator<Component> iterator1 = root.iterator();
        Iterator<Component> iterator2 = root.iterator();
        int count = 1;
        while (iterator1 != null && iterator1.hasNext() || iterator2 != null && iterator2.hasNext()) {
            System.out.println("START " + count);
            if (iterator1 != null && iterator1.hasNext()) {
                Component component1 = iterator1.next();
                component1.operation();
                if (count == 2) {
                    iterator1 = null;
                }
            }

            if (iterator2 != null && iterator2.hasNext()) {
                Component component2 = iterator2.next();
                component2.operation();
            }
            System.out.println("END " + count);
            count++;
        }
    }
}