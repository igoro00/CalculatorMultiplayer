package ordecha.igor.calculatormultiplayer;

public class Utils {


    String formatBackspace(String input) {
        if (input.length() > 0) {
            String output = "";
            int max = input.length() - 1;
            if (input.endsWith("\uD83D\uDE02")) {
                max--;
            }
            for (int i = 0; i < max; i++) {
                output += input.charAt(i);
            }
            return output;
        } else {
            return "";
        }
    }

    String commaReformat(String input) {
        String output;
        boolean wasThereComma = false;
        //idzie przez kazdy znak w stringu
        for (int i = 0; i < input.length(); i++) {
            //jesli widzi kropke pierwszy raz tylko zmienia zmienna na true
            //
            //jesli widzi kropke a zmienna juz jest na true to znaczy ze
            //kropka juz byla wiec obecna usuwa
            if (input.charAt(i) == '.') {
                if (wasThereComma) {
                    //zamiania na fire emoji. musi byc jakis charakterystyczny znak
                    //ktory nie zmieni dlugosci zmiennej ale pozwoli na latwe usuniecie pozniej
                    //to jest jedyne polecenie ktore skopiowalem z neta
                    input = input.substring(0, i)
                            + "\uD83D\uDD25"
                            + input.substring(i + 1);
                }
                wasThereComma = true;
            }
            //jesli napotka +-*/ to znaczy ze liczba sie skonczyla
            if (input.charAt(i) == '+' || input.charAt(i) == '-' || input.charAt(i) == '*' || input.charAt(i) == '/') {
                wasThereComma = false;
            }
        }
        //usuwa wszystkie fire emoji ktore byly wczesniej niepotrzebnymi kropkami
        output = input.replaceAll("\uD83D\uDD25", "");
        return output;
    }


    static String lineOut() {
        int level = 3;
        StackTraceElement[] traces;
        traces = Thread.currentThread().getStackTrace();
        return (" at " + traces[level] + " ");
    }
}