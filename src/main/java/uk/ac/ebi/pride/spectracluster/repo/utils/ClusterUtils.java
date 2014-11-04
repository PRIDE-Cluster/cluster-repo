package uk.ac.ebi.pride.spectracluster.repo.utils;

import uk.ac.ebi.pride.spectracluster.repo.model.ClusterSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusteredPSMSummary;

import java.util.*;

/**
 * Utility methods for aiding cluster related computation
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class ClusterUtils {
    private static final ClusteredPSMRatioComparator comparator = new ClusteredPSMRatioComparator();

    /**
     * Update statistics on the clustered PSMs
     *
     * @param cluster cluster
     */
    public static void updateClusteredPSMStatistics(final ClusterSummary cluster) {
        float size = (float) cluster.getClusteredSpectrumSummaries().size();

        List<ClusteredPSMSummary> psmSummaries = cluster.getClusteredPSMSummaries();
        for (ClusteredPSMSummary clusteredPSMSummary : psmSummaries) {
            String sequence = clusteredPSMSummary.getSequence();
            List<ClusteredPSMSummary> clusteredPSMSummaries = cluster.getClusteredPSMSummaries(sequence);
            int count = countDistinctSpectra(clusteredPSMSummaries);
            float ratio = count / size;
            clusteredPSMSummary.setPsmRatio(ratio);
        }


        Collections.sort(psmSummaries, comparator);

        int rank = 0;
        float currentRatio = -1;
        for (ClusteredPSMSummary psmSummary : psmSummaries) {
            float psmRatio = psmSummary.getPsmRatio();
            if (psmRatio != currentRatio) {
                currentRatio = psmRatio;
                rank++;
            }
            psmSummary.setRank(rank);
        }

    }

    private static int countDistinctSpectra(List<ClusteredPSMSummary> psms) {
        Set<String> psmRepresentations = new HashSet<String>();

        for (ClusteredPSMSummary psm : psms) {
            psmRepresentations.add(psm.getSpectrumId() + "");
        }

        return psmRepresentations.size();
    }


    private static class ClusteredPSMRatioComparator implements Comparator<ClusteredPSMSummary> {

        @Override
        public int compare(ClusteredPSMSummary o1, ClusteredPSMSummary o2) {
            return -(Float.compare(o1.getPsmRatio(), o2.getPsmRatio()));
        }
    }
}
