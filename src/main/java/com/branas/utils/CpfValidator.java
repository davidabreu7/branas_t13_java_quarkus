package com.branas.utils;

public class CpfValidator {

    private CpfValidator() {
        throw new IllegalStateException("Utility class");
    }

    static public boolean validateCpf(String cpf) {
        if (cpf == null)
            return false;
        cpf = cleanCpf(cpf);
        if (!isValidLength(cpf))
            return false;
        String finalStr = cpf;
        if (allDigitsTheSame(cpf, finalStr))
            return false;
        int dg1 = calculateDigit(cpf, 10);
        int dg2 = calculateDigit(cpf, 11);
        int checkDigit = extractDigit(cpf);
        int calculatedDigit = Integer.parseInt("" + dg1 + dg2);
        return checkDigit == calculatedDigit;
        }

    private static int extractDigit(String cpf) {
        return Integer.parseInt(cpf.substring(cpf.length() - 2));
    }

    private static boolean allDigitsTheSame(String cpf, String finalStr) {
        return cpf.chars().allMatch(c -> c == finalStr.charAt(0));
    }

    private static boolean isValidLength(String cpf) {
        return cpf.length() == 11;
    }

    private static String cleanCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private static int calculateDigit(String cpf, int factor) {
        int sum = 0;
        for (String digit : cpf.split("")) {
         if (factor > 1)
             sum += Integer.parseInt(digit) * factor--;
        }
        int result = sum % 11;
        return result < 2 ? 0 : 11 - result;
    }
}
