package util;

public class PrimeFactorsProtocol {
    public static boolean checkClientToServerMessage(String input) {
        String[] tokens = input.split(" ");
        System.out.println(tokens.length);
        if (tokens.length != 4) return false;
        else {
            if (!tokens[0].equals("factor")) return false;
            for (int i = 1; i < tokens.length; i++) {
                if (!tokens[i].matches("\\d+")) return false;
            }
        }
        return true;
    }
   public static void main(String[] args) {
       System.out.println(checkClientToServerMessage("factor 1332222 2 17"));
   }
}
