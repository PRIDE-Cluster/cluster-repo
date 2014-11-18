package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.spectracluster.repo.model.ClusterDetail;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusteredPSMDetail;

import java.util.*;

/**
 * Utility methods for aiding cluster related computation
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class ClusterUtils {
    private static final ClusteredPSMRatioComparator comparator = new ClusteredPSMRatioComparator();

    private ClusterUtils() {}

    /**
     * Update statistics on the clustered PSMs
     *
     * @param cluster cluster
     */
    public static void updateClusteredPSMStatistics(final ClusterDetail cluster) {
        float size = (float) cluster.getClusteredSpectrumSummaries().size();

        List<ClusteredPSMDetail> psmSummaries = cluster.getClusteredPSMSummaries();
        for (ClusteredPSMDetail clusteredPSMDetail : psmSummaries) {
            String sequence = clusteredPSMDetail.getSequence();
            List<ClusteredPSMDetail> clusteredPSMSummaries = cluster.getClusteredPSMSummaries(sequence);
            int count = countDistinctSpectra(clusteredPSMSummaries);
            float ratio = count / size;
            clusteredPSMDetail.setPsmRatio(ratio);
        }


        Collections.sort(psmSummaries, comparator);

        int rank = 0;
        float currentRatio = -1;
        for (ClusteredPSMDetail psmSummary : psmSummaries) {
            float psmRatio = psmSummary.getPsmRatio();
            if (psmRatio != currentRatio) {
                currentRatio = psmRatio;
                rank++;
            }
            psmSummary.setRank(rank);
        }

    }

    private static int countDistinctSpectra(List<ClusteredPSMDetail> psms) {
        Set<String> psmRepresentations = new HashSet<String>();

        for (ClusteredPSMDetail psm : psms) {
            psmRepresentations.add(psm.getSpectrumId() + "");
        }

        return psmRepresentations.size();
    }


    private static class ClusteredPSMRatioComparator implements Comparator<ClusteredPSMDetail> {

        @Override
        public int compare(ClusteredPSMDetail o1, ClusteredPSMDetail o2) {
            return -(Float.compare(o1.getPsmRatio(), o2.getPsmRatio()));
        }
    }
}
