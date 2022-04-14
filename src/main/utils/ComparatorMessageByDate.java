package main.utils;

import main.domain.MessageByID;

import java.util.Comparator;

public class ComparatorMessageByDate implements Comparator<MessageByID> {
    public int compare(MessageByID a, MessageByID b) {
        if (a.getDateTime().isBefore(b.getDateTime()))
            return -1;
        if (a.getDateTime().equals(b.getDateTime()))
            return 0;
        return 1;
    }
}
