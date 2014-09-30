package uk.ac.ebi.pride.spectracluster.repo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for handling collections
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class CollectionUtils {

    /**
     * Split a big list into fix sized chunks
     *
     * @param bigList   big list to split
     * @param chunkSize chunk size
     * @param <T>       element type
     * @return a list of lists
     */
    public static <T> List<List<T>> chunks(List<T> bigList, int chunkSize) {
        List<List<T>> chunks = new ArrayList<List<T>>();

        for (int i = 0; i < bigList.size(); i += chunkSize) {
            List<T> chunk = bigList.subList(i, Math.min(bigList.size(), i + chunkSize));
            chunks.add(chunk);
        }

        return chunks;
    }
}
