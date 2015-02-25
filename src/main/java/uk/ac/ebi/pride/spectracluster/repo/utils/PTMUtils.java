package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.spectracluster.repo.model.PTMDetail;
import uk.ac.ebi.pridemod.controller.impl.PSIModDataAccessController;
import uk.ac.ebi.pridemod.controller.impl.UnimodDataAccessController;
import uk.ac.ebi.pridemod.model.PTM;

import java.io.InputStream;
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
public final class PTMUtils {

    private static final String PTM_SEPARATOR = ",";
    private static final String PTM_PART_SEPARATOR = "-";

    public static PSIModDataAccessController psiModController;
    public static UnimodDataAccessController uniModController;

    static {
        InputStream psiModStream = PTMUtils.class.getClassLoader().getResourceAsStream("mod/PSI-MOD.obo");
        psiModController = new PSIModDataAccessController(psiModStream);

        InputStream uniModStream = PTMUtils.class.getClassLoader().getResourceAsStream("mod/unimod.xml");
        uniModController = new UnimodDataAccessController(uniModStream);
    }

    public static List<PTMDetail> getPTMs(String ptms) {
        List<PTMDetail> ptmDetails = new ArrayList<PTMDetail>();

        // return if it is null or empty
        if (ptms == null) {
            return ptmDetails;
        }

        Set<String> splits = StringUtils.split(ptms, PTM_SEPARATOR);

        if (!splits.isEmpty()) {
            for (String split : splits) {
                String[] parts = split.trim().split(PTM_PART_SEPARATOR);
                if (parts.length == 2) {
                    PTMDetail ptmDetail = new PTMDetail();
                    // accession
                    ptmDetail.setAccession(parts[1]);

                    // position
                    if (isInteger(parts[0])) {
                        ptmDetail.setPosition(Integer.parseInt(parts[0]));
                    }

                    // name
                    String ptmName = getPTMName(parts[1]);
                    ptmDetail.setName(ptmName);
                }
            }
        }

        return ptmDetails;
    }

    public static String constructPTMString(List<PTMDetail> ptmDetails) {
        if (ptmDetails == null || ptmDetails.isEmpty()) {
            return null;
        }

        String ptmStr = "";
        for (PTMDetail ptmDetail : ptmDetails) {
            ptmStr += ptmDetail.getPosition() + PTM_PART_SEPARATOR + ptmDetail.getAccession() + PTM_SEPARATOR;
        }

        return ptmStr.substring(0, ptmStr.length() - PTM_SEPARATOR.length());
    }

    private static String getPTMName(String accession) {

        PTM ptm = psiModController.getPTMbyAccession(accession);
        if (ptm != null) {
            return ptm.getName();
        }

        ptm = uniModController.getPTMbyAccession(accession);
        if (ptm != null) {
            return ptm.getName();
        }

        return null;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
