import java.text.NumberFormat;
import java.util.Scanner;

public class MortgageCalculator {
    public static void main(String[] args) {
        final int MIN_PRINCIPAL_AMOUNT = 1000;
        final int MAX_PRINCIPAL_AMOUNT = 1000000;
        final byte MIN_MORTGAGE_PERIOD = 1;
        final byte MAX_MORTGAGE_PERIOD = 30;
        final float MIN_INTEREST_RATE = 1.0F;
        final float MAX_INTEREST_RATE = 30.0F;

        int principalAmount = (int) readNumber("Principal ($1k - $1M): ", MIN_PRINCIPAL_AMOUNT, MAX_PRINCIPAL_AMOUNT);
        float interestRate = (float) readNumber("Annual Interest Rate: ", MIN_INTEREST_RATE, MAX_INTEREST_RATE);
        short period = (short) readNumber("Period (Years): ", MIN_MORTGAGE_PERIOD, MAX_MORTGAGE_PERIOD);

        printMonthlyMortgage(principalAmount, interestRate, period);
        printPaymentSchedule(period, principalAmount, interestRate);
    }

    private static void printPaymentSchedule(short period, int principalAmount, float interestRate) {
        String formattedMortgageAmount;
        System.out.println("\nPAYMENT SCHEDULE\n----------------");
        for (short i = 1; i <= calculateTotalMortgagePeriod(period); i++) {
            double remainingBalance = calculateRemainingMortage(principalAmount, interestRate, period, i);
            formattedMortgageAmount = NumberFormat.getCurrencyInstance().format(remainingBalance);
            System.out.println(formattedMortgageAmount);
        }
    }

    private static void printMonthlyMortgage(int principalAmount, float interestRate, short period) {
        double mortgage = calculateMortgage(principalAmount, interestRate, period);
        String formattedMortgageAmount = NumberFormat.getCurrencyInstance().format(mortgage);
        System.out.println("\nMORTGAGE\n--------");
        System.out.println("Mortgage Payment: " + formattedMortgageAmount);
    }

    public static double calculateMortgage(int principalAmount, float interestRate, short period) {
        interestRate = calculateMonthlyInterest(interestRate);
        period = calculateTotalMortgagePeriod(period);
        double interestForLoanTenure = calculateMonthlyInterest(interestRate, period);
        return principalAmount * ( (interestRate * interestForLoanTenure )/ (interestForLoanTenure - 1));
    }

    public static double readNumber(String prompt, double min, double max) {
        Scanner scanner = new Scanner(System.in);
        double value;
        while (true) {
            System.out.print(prompt);
            value = scanner.nextDouble();
            if (value >= min && value <= max) {
                break;
            }
            System.out.println("Enter a number between " + min + " and " + max);
        }
        return value;
    }

    public static double calculateRemainingMortage(int principalAmount, float interestRate, short period, short numberOfPaymentsMade) {
        interestRate = calculateMonthlyInterest(interestRate);
        period = calculateTotalMortgagePeriod(period);
        double interestForLoanTenure = calculateMonthlyInterest(interestRate, period);
        double interestForAmountPaid = calculateMonthlyInterest(interestRate, numberOfPaymentsMade);

        return (principalAmount * (interestForLoanTenure - interestForAmountPaid)) / (interestForLoanTenure - 1);
    }

    public static float calculateMonthlyInterest(float interestRate) {
        final byte MONTHS_IN_YEAR = 12;
        final byte PERCENT = 100;

        return ((interestRate / PERCENT) / MONTHS_IN_YEAR);
    }

    public static short calculateTotalMortgagePeriod(short period) {
        final byte MONTHS_IN_YEAR = 12;

        return (short) (period * MONTHS_IN_YEAR);
    }

    public static double calculateMonthlyInterest(float interestRate, short times) {
        return Math.pow((1 + interestRate), times);
    }
}
