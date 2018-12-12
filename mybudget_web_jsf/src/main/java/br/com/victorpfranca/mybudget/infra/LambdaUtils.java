package br.com.victorpfranca.mybudget.infra;

import java.util.Optional;
import java.util.function.Function;

public class LambdaUtils {
	private LambdaUtils() {
	}

	public static <X, Y> Y nullSafeConvert(X x, Function<X, Y> func) {
		return Optional.ofNullable(x).map(func).orElse(null);
	}

	public static <X, Y, Z> Function<X, Z> compose(Function<X, Y> f1, Function<Y, Z> f2) {
		return f1.andThen(f2);
	}

}
