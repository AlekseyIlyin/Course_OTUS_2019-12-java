package ru.otus.l07.ATM;

import ru.otus.l07.ATM.banknote.BanknoteRus;
import ru.otus.l07.ATM.banknote.BanknotеCell;
import ru.otus.l07.ATM.banknote.Cell;
import ru.otus.l07.ATM.banknote.MoneyBox;
import ru.otus.l07.ATM.exceptions.NoPlaceBanknoteException;

import java.util.*;

public class MoneyCassette extends MoneyBox implements State{
    protected final Set<CellForMoneyCassette> moneyBoxState = new HashSet();

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

    @Override
    public void saveState() {
        moneyBoxState.clear();
        box.values().stream()
                .map(cell -> (((CellForMoneyCassette)cell).copy()))
                .forEach(moneyBoxState::add);
    }

    @Override
    public void restoreState() {
        box.clear();
        for (CellForMoneyCassette cell : moneyBoxState) {
            box.put(cell.getTypeBanknote(),cell);
        }
    }

    private class CellForMoneyCassette extends Cell {
        static final int SIZE_OF_CELL = 200;

        public CellForMoneyCassette(BanknoteRus typeBanknote) {
            super(typeBanknote);
        }

        public CellForMoneyCassette(BanknoteRus typeBanknote, int numBanknotes) {
            super(typeBanknote, numBanknotes);
        }

        public CellForMoneyCassette copy() {
            return new CellForMoneyCassette(typeBanknote, numBanknotes);
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
