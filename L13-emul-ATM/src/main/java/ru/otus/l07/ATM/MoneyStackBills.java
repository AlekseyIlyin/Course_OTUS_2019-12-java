package ru.otus.l07.ATM;

import ru.otus.l07.ATM.banknote.BanknoteRus;
import ru.otus.l07.ATM.banknote.BanknotеCell;
import ru.otus.l07.ATM.banknote.Cell;
import ru.otus.l07.ATM.banknote.MoneyBox;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

public class MoneyStackBills extends MoneyBox {
    public static final int SIZE_IN_OUT_TRAY = 50;
    private int fullness = 0;

    public MoneyStackBills() {
        super("Лоток для приема/выдачи купюр");
    }

    @Override
    public void initialize() {
        fullness = 0;
        box.clear();
    }

    public MoneyStackBills addBanknotes(BanknoteRus typeBanknote, int nums) throws NoPlaceBanknoteException {
        BanknotеCell cell = getCell(typeBanknote);
        if (cell == null) {
            box.put(typeBanknote, new CellForMoneyStackBills(typeBanknote));
        }
        putBanknote(typeBanknote, nums);
        return this;
    }

    @Override
    public BanknotеCell getCell(BanknoteRus banknote) {
        BanknotеCell cell = box.get(banknote);
        if (cell == null) {
            cell = new MoneyStackBills.CellForMoneyStackBills(banknote);
            box.put(banknote, cell);
        }
        return cell;
    }

    private class CellForMoneyStackBills extends Cell {

        public CellForMoneyStackBills(BanknoteRus typeBanknote) {
            super(typeBanknote);
        }

        @Override
        public void putBanknote(int num) throws NoPlaceBanknoteException {
            if (fullness + num > SIZE_IN_OUT_TRAY) {
                throw new NoPlaceBanknoteException(typeBanknote.name());
            }
            fullness += num;
            numBanknotes += num;
        }
    }
}
