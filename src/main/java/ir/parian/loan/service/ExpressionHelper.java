package ir.parian.loan.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.Optional;
import java.util.function.Function;

public class ExpressionHelper {
    private static final BooleanExpression TRUE = Expressions.asBoolean(true).isTrue();

    public static String cleanSearch(String search) {
        return Optional.ofNullable(search)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(null);
    }

    public static <T> BooleanExpression safeExpression(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value)
                .map(function)
                .orElse(TRUE);
    }
}
