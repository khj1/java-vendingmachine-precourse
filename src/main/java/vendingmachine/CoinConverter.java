package vendingmachine;

import vendingmachine.model.Money;

import java.util.ArrayList;
import java.util.List;

public class CoinConverter {

    private final CoinAmountGenerator amountGenerator;

    public CoinConverter(CoinAmountGenerator amountGenerator) {
        this.amountGenerator = amountGenerator;
    }

    public List<Coin> convert(Money money) {
        List<Coin> coins = new ArrayList<>();
        while (money.convertable()) {
            int amount = amountGenerator.generate();
            if (money.convertable(amount)) {
                money.decrease(amount);
                coins.add(Coin.from(amount));
            }
        }
        return coins;
    }
}
