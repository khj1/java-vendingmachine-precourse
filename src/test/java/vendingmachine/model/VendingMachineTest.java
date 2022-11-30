package vendingmachine.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vendingmachine.Coin;
import vendingmachine.CoinConverter;
import vendingmachine.TestCoinAmountGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineTest {

    VendingMachine vendingMachine;
    Products products;

    @BeforeEach
    void setUp() {
        products = Products.of("[사과,100,20];[배,500,10]");
        CoinConverter converter = new CoinConverter(new TestCoinAmountGenerator());
        HoldingCoins holdingCoins = HoldingCoins.from(converter.convert(Money.from("760")));

        vendingMachine = VendingMachine.of(products, holdingCoins);
    }

    @DisplayName("상품을 구매하면 수량이 줄어든다.")
    @Test
    void buyProductThenQuantityDecrease() {
        vendingMachine.insertMoney(Money.from("5000"));
        vendingMachine.purchase("사과");

        assertThat(products.findByName("사과"))
                .usingRecursiveComparison()
                .isEqualTo(Product.from("[사과,100,19]"));
    }

    @DisplayName("상품을 구매하면 투입 금액이 줄어든다.")
    @Test
    void buyProductThenInsertedMoneyDecrease() {
        vendingMachine.insertMoney(Money.from("5000"));
        vendingMachine.purchase("사과");


        assertThat(vendingMachine.getInsertedMoney()).isEqualTo(4900);
    }

    @DisplayName("잔돈을 최소 동전으로 반환하는 기능")
    @Test
    void convertBalance() {
        vendingMachine.insertMoney(Money.from("900"));

        assertThat(vendingMachine.convertBalanceToCoins())
                .containsEntry(Coin.COIN_500, 1)
                .containsEntry(Coin.COIN_100, 2)
                .containsEntry(Coin.COIN_50, 1)
                .containsEntry(Coin.COIN_10, 1);
    }
}
