
public class Main {

    public static void main(String[] args) {
        // write testcode here
        Person pekka = new Person("Pekka Mikkola", "040-123123");

    System.out.println( pekka.getName() );
    System.out.println( pekka.getNumber() );

    pekka.changeNumber("050-333444");
    System.out.println( pekka );
    }
}
