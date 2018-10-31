package com.marsianets.orderbook;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by Yuri Golub on 16.09.13.
 * Класс-фильтр для вводимого числа: ограничивает ввод цифрами и десятичной точкой(запятой),
 * также ограничивает количество вводимых символов после точки
 */
public class RealNumberInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String checkedText = dest.subSequence(0, dstart).toString() +
                source.subSequence(start, end) +
                dest.subSequence(dend, dest.length()).toString();
        String pattern = getPattern();

        if (!Pattern.matches(pattern, checkedText)) {
            return "";
        }

        return null;
    }

    private String getPattern() {
        String ds = "," + ".";
        String pattern = "([0-9]{1,2})?+([" + ds + "]{1}||[" + ds + "]{1}[0-9]{1,2})?";
        return pattern;
    }
}
