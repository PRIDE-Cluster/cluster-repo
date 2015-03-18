package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utilities for handling PTMs
 * <p/>
 *
 * @author Rui Wang
 * @version $Id$
 */
/* TODO Move this to index-utils */
public final class ModificationUtils {

    private static final String PTM_SEPARATOR = ",";

    public static List<ModificationProvider> getModifications(String ptms) {
        List<ModificationProvider> modificationDetails = new ArrayList<ModificationProvider>();

        // return if it is null or empty
        if (ptms == null) {
            return modificationDetails;
        }

        Set<String> splits = StringUtils.split(ptms, PTM_SEPARATOR);

        if (!splits.isEmpty()) {
            for (String split : splits) {
                final ModificationProvider mod = ModificationHelper.convertFromString(split);
                if(mod != null){
                    modificationDetails.add(mod);
                }
            }
        }

        return modificationDetails;
    }

    public static String constructModificationString(List<ModificationProvider> modificationDetails) {
        if (modificationDetails == null || modificationDetails.isEmpty()) {
            return null;
        }

        String ptmStr = "";

        for (ModificationProvider modificationDetail : modificationDetails) {
            ptmStr += ModificationHelper.convertToString(modificationDetail) + PTM_SEPARATOR;
        }

        return ptmStr.substring(0, ptmStr.length() - PTM_SEPARATOR.length());
    }

}
