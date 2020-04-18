package ru.otus.l07.ATM;

import ru.otus.l07.ATM.banknote.BanknoteRus;
import ru.otus.l07.ATM.banknote.BanknotеCell;
import ru.otus.l07.ATM.banknote.Cell;
import ru.otus.l07.ATM.banknote.MoneyBox;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

public class MoneyCassette extends MoneyBox {

    public MoneyCassette() {
        // crate cells for all banknote
        super("Основная кассета");
        for (BanknoteRus banknote : BanknoteRus.values()) {
            box.put(banknote, new CellForMoneyCassette(banknote));
        }
    }

    @Override
    public void initialize() {
        box.clear();
    }

    @Override
    public BanknotеCell getCell(BanknoteRus banknote) {
        BanknotеCell cell = box.get(banknote);
        if (cell == null) {
            cell = new MoneyCassette.CellForMoneyCassette(banknote);
            box.put(banknote, cell);
        }
        return cell;
    }

    private class CellForMoneyCassette extends Cell {
        static final int SIZE_OF_CELL = 200;

        public CellForMoneyCassette(BanknoteRus typeBanknote) {
            super(typeBanknote);
        }

        @Override
        public void putBanknote(int num) throws NoPlaceBanknoteException {
            numBanknotes += num;
            if (numBanknotes > SIZE_OF_CELL) {
                num = numBanknotes - SIZE_OF_CELL;
                numBanknotes =  SIZE_OF_CELL;
                throw new NoPlaceBanknoteException(toString());
            }
        }


    }

}
