package ru.otus.l07.ATM.banknote;

public enum BanknoteRus {
    nom_5000(5000),
    nom_1000(1000),
    nom_500(500),
    nom_200(200),
    nom_100(100),
    nom_50(50);

    private int cost;
    private static final String nameCurrency = "руб.";

    BanknoteRus(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return getCost() + " " + nameCurrency;
    }
}
