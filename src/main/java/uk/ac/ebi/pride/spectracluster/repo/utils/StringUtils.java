package uk.ac.ebi.pride.spectracluster.repo.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class for handling strings
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class StringUtils {

    private StringUtils() {}

    public static Set<String> split(String text, String separator) {
        if (text == null)
            return Collections.emptySet();

        Set<String> entries = new HashSet<String>();

        String[] parts = text.split(separator);
        for (String part : parts) {
            entries.add(part.trim());
        }

        return entries;
    }

    public static <T> String concatenate(List<T> contents, String separator) {
        if (contents == null || contents.isEmpty())
            return null;

        String concatenation = "";

        for (Object content : contents) {
            concatenation += content.toString() + separator;
        }

        return concatenation.substring(0, concatenation.length() - separator.length());
    }
}
