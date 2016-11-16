/**
 * Created by jacob on 2016/11/15.
 */
public class test {
    public static void main(String[] args) {
        String str = "https://ht.transparencytoolkit.org/Amministrazione/01 - CLIENTI/1 - Commesse/1 - Commesse 2011/Commessa052.2011Asystel                                                                                                                                                                                                                                                                                                                                                                                                        x I Cedri Spa.xls";
        char[]c = str.toCharArray();

        for (char c2:c){
            System.out.println(String.valueOf(c2).getBytes()[0]);
        }
        System.out.println(" ".getBytes().length);
    }

}
