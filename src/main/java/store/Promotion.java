package store;

import java.time.LocalDate;
import java.util.Arrays;

public enum Promotion {
    SODA_2_PLUS_1("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
    MD_RECOMMENDED("MD추천상품", 1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
    FLASH_DISCOUNT("반짝할인", 1, 1, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30)),
    NONE("null", 0, 0, null, null);

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion getPromotionByName(String name) {
        return Arrays.stream(Promotion.values())
            .filter(promotion -> promotion.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public String getName() {
        return name;
    }

    public int getPromotionUnit() {
        return buy + get;
    }

    public boolean isBetweenPromotionPeriod(LocalDate today) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
