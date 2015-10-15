package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

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

    private static String getCommonAminoAcid(ModificationProvider mod, String sequence){
        if(mod.getPositionMap() != null && !mod.getPositionMap().isEmpty()){
            for(Integer position: mod.getPositionMap().keySet()){
                if(position != null && position < sequence.length()){
                    position = (position == 0)?1:position;
                    return sequence.substring(position-1, position);
                }
            }
        }
        return null;
    }

    public static String constructAnchorModificationString(List<ModificationProvider> modifications, String sequence) {
        if (modifications == null || modifications.isEmpty()) {
            return null;
        }

        ModReader modReader = ModReader.getInstance();

        String ptmStr = "";

        for (ModificationProvider modificationDetail : modifications) {
            String accession = modificationDetail.getAccession();
            String aa = getCommonAminoAcid(modificationDetail, sequence);
            List<PTM> anchorPTMs;
            if(aa != null)
                anchorPTMs = modReader.getAnchorModification(accession, aa);
            else
                anchorPTMs = modReader.getAnchorModification(accession);

            //System.out.println(anchorPTMs.size());

            if(anchorPTMs.size() == 1){
                ((Modification)modificationDetail).setAccession(anchorPTMs.get(0).getAccession());
                ((Modification)modificationDetail).setName(anchorPTMs.get(0).getName());
            }

            ptmStr += ModificationHelper.convertToString(modificationDetail) + PTM_SEPARATOR;
        }

        return ptmStr.substring(0, ptmStr.length() - PTM_SEPARATOR.length());
    }

    public static List<ModificationProvider> constructAnchorModification(List<ModificationProvider> modifications, String sequence) {
        List<ModificationProvider> listModifications = new ArrayList<ModificationProvider>();
        if (modifications == null || modifications.isEmpty()) {
            return null;
        }

        ModReader modReader = ModReader.getInstance();

        for (ModificationProvider modificationDetail : modifications) {
            String accession = modificationDetail.getAccession();
            String aa = getCommonAminoAcid(modificationDetail, sequence);
            List<PTM> anchorPTMs;
            if(aa != null)
                anchorPTMs = modReader.getAnchorModification(accession, aa);
            else
                anchorPTMs = modReader.getAnchorModification(accession);

            if(anchorPTMs.size() == 1){
                ((Modification)modificationDetail).setAccession(anchorPTMs.get(0).getAccession());
                ((Modification)modificationDetail).setName(anchorPTMs.get(0).getName());
            }

            listModifications.add(modificationDetail);
        }

        return listModifications;
    }
}
