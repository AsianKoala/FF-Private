public class ClassReferenceTest {
    public static void main(String[] args) {
        Person a = new Person(10);
        editPerson(a);
    }

    static void editPerson(Person b) {
        b.age = 5;
    }
}

class Person {
    int age;

    public Person(int age) {
        this.age = age;
    }
}
