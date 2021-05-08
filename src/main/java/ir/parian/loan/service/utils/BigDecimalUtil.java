package ir.parian.loan.service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {
    public static boolean hasNoValue(BigDecimal value) {
        return !hasValue(value);
    }

    public static boolean hasValue(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) != 0;
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (hasNoValue(a)) {
            return b;
        }

        if (hasNoValue(b)) {
            return a;
        }

        return a.add(b);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (hasNoValue(a)) {
            return b.negate();
        }

        if (hasNoValue(b)) {
            return a;
        }

        return a.subtract(b);
    }

    public static BigDecimal divide(final BigDecimal amount, final Long rate) {
        return divide(amount, BigDecimal.valueOf(rate));
    }

    public static BigDecimal divide(final BigDecimal amount, final Integer rate) {
        return divide(amount, BigDecimal.valueOf(rate));
    }

    public static BigDecimal divide(final BigDecimal amount, final Short rate) {
        return divide(amount, BigDecimal.valueOf(rate));
    }

    public static BigDecimal divide(final BigDecimal amount, final BigDecimal divisor) {
        return amount.divide(divisor, RoundingMode.FLOOR);
    }
}
