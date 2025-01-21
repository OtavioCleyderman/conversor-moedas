package br.com.java.main;

import java.util.Scanner;

public class CurrencyConverter {
    private final ExchangeRateClient client;

    public CurrencyConverter() {
        this.client = new ExchangeRateClient();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Conversor de Moedas!");
        System.out.println("Escolha uma das opções de conversão abaixo:");
        System.out.println("1. USD para BRL");
        System.out.println("2. BRL para USD");
        System.out.println("3. EUR para BRL");
        System.out.println("4. BRL para EUR");
        System.out.println("5. USD para EUR");
        System.out.println("6. EUR para USD");

        System.out.print("Digite a opção desejada (1-6): ");
        int option = scanner.nextInt();

        System.out.print("Digite o valor a ser convertido: ");
        double amount = scanner.nextDouble();

        String fromCurrency = "";
        String toCurrency = "";

        switch (option) {
            case 1 -> {
                fromCurrency = "USD";
                toCurrency = "BRL";
            }
            case 2 -> {
                fromCurrency = "BRL";
                toCurrency = "USD";
            }
            case 3 -> {
                fromCurrency = "EUR";
                toCurrency = "BRL";
            }
            case 4 -> {
                fromCurrency = "BRL";
                toCurrency = "EUR";
            }
            case 5 -> {
                fromCurrency = "USD";
                toCurrency = "EUR";
            }
            case 6 -> {
                fromCurrency = "EUR";
                toCurrency = "USD";
            }
            default -> {
                System.out.println("Opção inválida! Encerrando o programa.");
                return;
            }
        }

        try {
            double convertedValue = client.getExchangeRate(fromCurrency, toCurrency, amount);
            System.out.printf("Valor convertido: %.2f %s%n", convertedValue, toCurrency);
        } catch (Exception e) {
            System.out.println("Erro ao realizar a conversão: " + e.getMessage());
        }
    }
}
