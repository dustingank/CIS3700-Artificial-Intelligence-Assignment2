public class Resolution {

    public static void main(String[] args) {
        ClauseBase cb = new ClauseBase(args[0], args[1], args[2]);
        cb.loadFiles();
        //cb.check();
        if(cb.Resolution()){
            System.out.println("\nKB |= alpha");
        }
        else{
            System.out.println("\nKB !|= alpha");
        }
    }



}