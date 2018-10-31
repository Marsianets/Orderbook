package com.marsianets.orderbook;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by Юрий on 28.09.13.
 * Класс-фильтр для ввода даты: ограничивает ввод цифрами и разделителями, используемыми для дат
 */
public class DateInputFilter implements InputFilter {

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
        String ds = "." + "/";
        String pattern = "([0]?[0-9]{1}||[12]{1}[0-9]{1}||3{1}[01])" +
                "([" + ds + "]{1}||[" + ds + "]{1}([0]?[1-9]{1}||[1]?[012]{1})||" +
                "[" + ds + "]{1}([0]?[1-9]{1}||[1]?[012]{1})([" + ds + "]{1}||[" + ds + "]{1}[0-9]{1,2}))";
        return pattern;
    }
}
