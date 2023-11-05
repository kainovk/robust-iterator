public class Leaf implements Component {

    private String name;

    public Leaf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void operation() {
        System.out.println("Я лист: " + name);
    }
}
